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

import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import javax.net.ssl.X509TrustManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * MultiTrustManager used to authenticate the remote side of a secure socket.
 */
public class MultiTrustManager implements X509TrustManager {
    private static final Logger logger = LoggerFactory.getLogger(MultiTrustManager.class);
    protected List<KeyStore>    keystores;

    public MultiTrustManager(KeyStore ks) {
        this();
        keystores.add(ks);
    }

    public MultiTrustManager() {
        keystores = new ArrayList<KeyStore>();
    }

    public void add(KeyStore ks) {
        if (ks != null) {
            keystores.add(ks);
        }
    }

    public int getNumberOfKeyStores() {
        return keystores.size();
    }

    public void checkClientTrusted(X509Certificate[] certs, String alg) throws CertificateException {
        if (!isTrusted(certs)) {
            throw new CertificateException("No trusted certificate found");
        }
    }

    public void checkServerTrusted(X509Certificate[] certs, String alg) throws CertificateException {
        if (!isTrusted(certs)) {
            throw new CertificateException("No trusted certificate found");
        }
    }

    public X509Certificate[] getAcceptedIssuers() {
        List<X509Certificate> issuers = new ArrayList<X509Certificate>();
        for (KeyStore store : keystores) {
            try {
                Enumeration<String> e = store.aliases();
                while (e.hasMoreElements()) {
                    String alias = e.nextElement();
                    Certificate[] chain = store.getCertificateChain(alias);
                    if (chain != null) {
                        for (Certificate cert : chain) {
                            if (cert instanceof X509Certificate) {
                                if (logger.isDebugEnabled()) {
                                    logger.debug("Read certificate (chain) : "
                                            + ((X509Certificate) cert).getSubjectX500Principal().getName());
                                }
                                issuers.add((X509Certificate) cert);
                            }
                        }
                    } else {
                        Certificate cert = store.getCertificate(alias);
                        if (cert != null && cert instanceof X509Certificate) {
                            if (logger.isDebugEnabled()) {
                                logger.debug("Read certificate : "
                                        + ((X509Certificate) cert).getSubjectX500Principal().getName());
                            }
                            issuers.add((X509Certificate) cert);
                        }
                    }
                }
            } catch (KeyStoreException e) {
                logger.warn(e.getMessage());
            }
        }

        return issuers.toArray(new X509Certificate[issuers.size()]);
    }

    protected boolean isTrusted(X509Certificate[] cert) {
        if (cert != null) {
            X509Certificate[] issuers = this.getAcceptedIssuers();
            if (issuers != null) {
                for (X509Certificate issuer : issuers) {
                    for (X509Certificate c : cert) {
                        if (logger.isDebugEnabled())
                            logger.debug("--- Checking cert: " + issuer.getSubjectX500Principal()
                                    + " vs " + c.getSubjectX500Principal());
                        if (issuer.equals(c)) {
                            if (logger.isDebugEnabled())
                                logger.debug("--- Found trusted cert: " + issuer.getSerialNumber().toString(16)
                                        + " : " + issuer.getSubjectX500Principal());
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }
}
