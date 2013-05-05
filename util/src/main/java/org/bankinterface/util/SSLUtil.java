/*
 * Copyright 2013 bankinterface.org
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.bankinterface.util;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.KeyStore;
import java.security.Principal;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLPeerUnverifiedException;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509KeyManager;
import javax.net.ssl.X509TrustManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Utilities for setting up SSL connections with specific client certificates
 */
public class SSLUtil {
    private static final Logger logger                = LoggerFactory.getLogger(SSLUtil.class);
    public static final int     HOSTCERT_NO_CHECK     = 0;
    public static final int     HOSTCERT_MIN_CHECK    = 1;
    public static final int     HOSTCERT_NORMAL_CHECK = 2;

    public static boolean isClientTrusted(X509Certificate[] chain, String authType) {
        TrustManager[] mgrs = new TrustManager[0];
        try {
            mgrs = SSLUtil.getTrustManagers();
        } catch (Exception e) {
            // log.error(e.getMessage());
        }

        if (mgrs != null) {
            for (TrustManager mgr : mgrs) {
                if (mgr instanceof X509TrustManager) {
                    try {
                        ((X509TrustManager) mgr).checkClientTrusted(chain, authType);
                        return true;
                    } catch (CertificateException e) {
                        // do nothing; just loop
                    }
                }
            }
        }
        return false;
    }

    public static KeyManager[] getKeyManagers(String alias) throws IOException, GeneralSecurityException {
        List<KeyManager> keyMgrs = new ArrayList<KeyManager>();
        Object[] store;
        String password;
        KeyStore ks;
        for (String key : KeyStoreUtil.customerCertStore.keySet()) {
            store = KeyStoreUtil.customerCertStore.get(key);
            password = (String) store[0];
            ks = (KeyStore) store[1];
            List<KeyManager> newKeyManagers = Arrays.asList(getKeyManagers(ks, password, alias));
            keyMgrs.addAll(newKeyManagers);
        }
        return keyMgrs.toArray(new KeyManager[keyMgrs.size()]);
    }

    public static KeyManager[] getKeyManagers() throws IOException, GeneralSecurityException {
        return getKeyManagers(null);
    }

    public static TrustManager[] getTrustManagers() throws IOException, GeneralSecurityException {
        MultiTrustManager tm = new MultiTrustManager();
        tm.add(KeyStoreUtil.getSystemTrustStore());
        for (String key : KeyStoreUtil.customerTrustStore.keySet()) {
            tm.add(KeyStoreUtil.customerTrustStore.get(key));
        }
        return new TrustManager[] { tm };
    }

    public static TrustManager[] getTrustAnyManagers() {
        return new TrustManager[] { new TrustAnyManager() };
    }

    public static KeyManager[] getKeyManagers(KeyStore ks, String password, String alias)
            throws GeneralSecurityException {
        KeyManagerFactory factory = KeyManagerFactory.getInstance("SunX509");
        factory.init(ks, password.toCharArray());
        KeyManager[] keyManagers = factory.getKeyManagers();
        if (alias != null) {
            for (int i = 0; i < keyManagers.length; i++) {
                if (keyManagers[i] instanceof X509KeyManager) {
                    keyManagers[i] = new AliasKeyManager((X509KeyManager) keyManagers[i], alias);
                }
            }
        }
        return keyManagers;
    }

    public static TrustManager[] getTrustManagers(KeyStore ks) throws GeneralSecurityException {
        return new TrustManager[] { new MultiTrustManager(ks) };
    }

    public static SSLSocketFactory getSSLSocketFactory(KeyStore ks, String password, String alias) throws IOException,
            GeneralSecurityException {
        KeyManager[] km = SSLUtil.getKeyManagers(ks, password, alias);
        TrustManager[] tm = SSLUtil.getTrustManagers();

        SSLContext context = SSLContext.getInstance("SSL");
        context.init(km, tm, new SecureRandom());
        return context.getSocketFactory();
    }

    public static SSLSocketFactory getSSLSocketFactory(String alias, boolean trustAny) throws IOException,
            GeneralSecurityException {
        KeyManager[] km = SSLUtil.getKeyManagers(alias);
        TrustManager[] tm;
        if (trustAny) {
            tm = SSLUtil.getTrustAnyManagers();
        } else {
            tm = SSLUtil.getTrustManagers();
        }

        SSLContext context = SSLContext.getInstance("SSL");
        context.init(km, tm, new SecureRandom());
        return context.getSocketFactory();
    }

    public static SSLSocketFactory getSSLSocketFactory(String alias) throws IOException, GeneralSecurityException {
        return getSSLSocketFactory(alias, false);
    }

    public static SSLSocketFactory getSSLSocketFactory() throws IOException, GeneralSecurityException {
        return getSSLSocketFactory(null);
    }

    public static SSLServerSocketFactory getSSLServerSocketFactory(KeyStore ks, String password, String alias)
            throws IOException, GeneralSecurityException {
        TrustManager[] tm = SSLUtil.getTrustManagers();
        KeyManager[] km = SSLUtil.getKeyManagers(ks, password, alias);

        SSLContext context = SSLContext.getInstance("SSL");
        context.init(km, tm, new SecureRandom());
        return context.getServerSocketFactory();
    }

    public static SSLServerSocketFactory getSSLServerSocketFactory(String alias) throws IOException,
            GeneralSecurityException {
        TrustManager[] tm = SSLUtil.getTrustManagers();
        KeyManager[] km = SSLUtil.getKeyManagers(alias);

        SSLContext context = SSLContext.getInstance("SSL");
        context.init(km, tm, new SecureRandom());
        return context.getServerSocketFactory();
    }

    public static HostnameVerifier getHostnameVerifier(int level) {
        switch (level) {
        case HOSTCERT_MIN_CHECK:
            return new HostnameVerifier() {
                public boolean verify(String hostname, SSLSession session) {
                    javax.security.cert.X509Certificate[] peerCerts;
                    try {
                        peerCerts = session.getPeerCertificateChain();
                    } catch (SSLPeerUnverifiedException e) {
                        // cert not verified
                        logger.warn(e.getMessage());
                        return false;
                    }
                    for (javax.security.cert.X509Certificate peerCert : peerCerts) {
                        if (logger.isInfoEnabled()) {
                            Principal x500s = peerCert.getSubjectDN();
                            Map<String, String> subjectMap = KeyStoreUtil.getX500Map(x500s);
                            logger.info(peerCert.getSerialNumber().toString(16) + " :: " + subjectMap.get("CN"));
                        }
                        try {
                            peerCert.checkValidity();
                        } catch (Exception e) {
                            // certificate not valid
                            logger.warn("Certificate is not valid!");
                            return false;
                        }
                    }
                    return true;
                }
            };
        case HOSTCERT_NO_CHECK:
            return new HostnameVerifier() {
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            };
        default:
            return null;
        }
    }

    static class TrustAnyManager implements X509TrustManager {

        public void checkClientTrusted(X509Certificate[] certs, String string) throws CertificateException {
            logger.info("Trusting (un-trusted) client certificate chain:");
            for (X509Certificate cert : certs) {
                logger.info("---- " + cert.getSubjectX500Principal().getName() + " valid: " + cert.getNotAfter());
            }
        }

        public void checkServerTrusted(X509Certificate[] certs, String string) throws CertificateException {
            logger.info("Trusting (un-trusted) server certificate chain:");
            for (X509Certificate cert : certs) {
                logger.info("---- " + cert.getSubjectX500Principal().getName() + " valid: " + cert.getNotAfter());
            }
        }

        public X509Certificate[] getAcceptedIssuers() {
            return new X509Certificate[0];
        }
    }
}
