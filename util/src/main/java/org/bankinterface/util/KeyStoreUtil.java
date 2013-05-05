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

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.Reader;
import java.io.StringReader;
import java.net.URL;
import java.security.GeneralSecurityException;
import java.security.KeyFactory;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.Principal;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.Certificate;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Utilities for getting KeyManagers and TrustManagers
 */
public class KeyStoreUtil {
    private static final Logger          logger             = LoggerFactory.getLogger(KeyStoreUtil.class);
    static Map<String, Object[]>         customerCertStore  = new ConcurrentHashMap<String, Object[]>();
    static Map<String, KeyStore>         customerTrustStore = new ConcurrentHashMap<String, KeyStore>();
    private static Map<String, Object[]> signVerifyStore    = new ConcurrentHashMap<String, Object[]>();

    public static void registerCertStore(String url, String password, String type) {
        try {
            KeyStore ks = getStore(new URL(url), password, type);
            if (ks != null) {
                Object[] store = new Object[2];
                store[0] = password;
                store[1] = ks;
                customerCertStore.put(url, store);
            }
        } catch (Exception e) {
            logger.error("Register KeyStore Erorr, URL : " + url);
        }
    }

    public static void registerTrustStore(String url, String password, String type) {
        try {
            KeyStore ks = getStore(new URL(url), password, type);
            if (ks != null) {
                customerTrustStore.put(url, ks);
            }
        } catch (Exception e) {
            logger.error("Register KeyStore Erorr, URL : " + url);
        }
    }

    public static void registerSignVerifyStore(String url, String password, String type) {
        try {
            KeyStore ks = getStore(new URL(url), password, type);
            if (ks != null) {
                Object[] store = new Object[2];
                store[0] = ks;
                store[1] = password;
                signVerifyStore.put(url, store);
            }
        } catch (Exception e) {
            logger.error("Register KeyStore Erorr, URL : " + url);
        }
    }

    static PrivateKey getPrivateKey(String url, String alias) {
        Object[] store = signVerifyStore.get(url);
        try {
            KeyStore ks = (KeyStore) store[0];
            String password = (String) store[1];
            return (PrivateKey) ks.getKey(alias, password.toCharArray());
        } catch (Exception e) {
            logger.error("Get PrivateKey Erorr, URL : " + url + ", Alias :" + alias);
            return null;
        }
    }

    static PublicKey getPublicKey(String url, String alias) {
        Object[] store = signVerifyStore.get(url);
        try {
            KeyStore ks = (KeyStore) store[0];
            return ks.getCertificate(alias).getPublicKey();
        } catch (Exception e) {
            logger.error("Get PublicKey Erorr, URL : " + url + ", Alias :" + alias);
            return null;
        }
    }

    public static KeyStore getStore(URL url, String password) throws IOException, GeneralSecurityException {
        return getStore(url, password, "jks");
    }

    public static KeyStore getStore(URL url, String password, String type) throws IOException, GeneralSecurityException {
        if (type == null) {
            throw new IOException("Invalid keystore type; null");
        }
        KeyStore ks = KeyStore.getInstance(type);
        InputStream in = url.openStream();
        try {
            ks.load(in, password.toCharArray());
        } finally {
            in.close();
        }
        return ks;
    }

    public static KeyStore getSystemTrustStore() throws IOException, GeneralSecurityException {
        String javaHome = System.getProperty("java.home");
        String fileName = System.getProperty("javax.net.ssl.trustStore");
        String password = System.getProperty("javax.net.ssl.trustStorePassword");
        if (password == null) {
            password = "changeit";
        }

        KeyStore ks = KeyStore.getInstance("jks");
        File keyFile = null;
        if (fileName != null) {
            keyFile = new File(fileName);
        } else {
            keyFile = new File(javaHome + "/lib/security/jssecacerts");
            if (!keyFile.exists() || !keyFile.canRead()) {
                keyFile = new File(javaHome + "/lib/security/cacerts");
            }
        }

        if (keyFile.exists() && keyFile.canRead()) {
            InputStream in = new FileInputStream(keyFile);
            try {
                ks.load(in, password.toCharArray());
            } finally {
                in.close();
            }
        } else {
            ks.load(null, "changeit".toCharArray());
        }
        return ks;
    }

    public static X509Certificate readCertificate(byte[] certChain) throws CertificateException {
        CertificateFactory cf = CertificateFactory.getInstance("X.509");
        ByteArrayInputStream bais = new ByteArrayInputStream(certChain);
        return (X509Certificate) cf.generateCertificate(bais);
    }

    public static Map<String, String> getCertX500Map(java.security.cert.X509Certificate cert) {
        return getX500Map(cert.getSubjectX500Principal());
    }

    public static Map<String, String> getCertX500Map(javax.security.cert.X509Certificate cert) {
        return getX500Map(cert.getSubjectDN());
    }

    public static Map<String, String> getX500Map(Principal x500) {
        Map<String, String> x500Map = new HashMap<String, String>();

        String name = x500.getName().replaceAll("\\\\,", "&com;");
        String[] x500Opts = name.split("\\,");
        for (String opt : x500Opts) {
            if (opt.indexOf("=") > -1) {
                String[] nv = opt.split("\\=", 2);
                x500Map.put(nv[0].replaceAll("&com;", ","), nv[1].replaceAll("&com;", ","));
            }
        }

        return x500Map;
    }

    public static void importPKCS8CertChain(KeyStore ks, String alias, byte[] keyBytes, String keyPass, byte[] certChain)
            throws InvalidKeySpecException, NoSuchAlgorithmException, CertificateException, KeyStoreException {
        // load the private key
        KeyFactory kf = KeyFactory.getInstance("RSA");
        PKCS8EncodedKeySpec keysp = new PKCS8EncodedKeySpec(keyBytes);
        PrivateKey pk = kf.generatePrivate(keysp);

        // load the cert chain
        CertificateFactory cf = CertificateFactory.getInstance("X.509");
        ByteArrayInputStream bais = new ByteArrayInputStream(certChain);

        Collection<? extends Certificate> certCol = cf.generateCertificates(bais);
        Certificate[] certs = new Certificate[certCol.toArray().length];
        if (certCol.size() == 1) {
            logger.info("Single certificate; no chain");
            bais = new ByteArrayInputStream(certChain);
            Certificate cert = cf.generateCertificate(bais);
            certs[0] = cert;
        } else {
            logger.info("Certificate chain length : " + certCol.size());
            certs = certCol.toArray(new Certificate[certCol.size()]);
        }

        ks.setKeyEntry(alias, pk, keyPass.toCharArray(), certs);
    }

    public static String certToString(Certificate cert) throws CertificateEncodingException {
        byte[] certBuf = cert.getEncoded();
        StringBuilder buf = new StringBuilder();
        buf.append("-----BEGIN CERTIFICATE-----\n");
        buf.append(new String(Base64.encodeBase64Chunked(certBuf)));
        buf.append("\n-----END CERTIFICATE-----\n");
        return buf.toString();
    }

    public static Certificate pemToCert(String certString) throws IOException, CertificateException {
        return pemToCert(new StringReader(certString));
    }

    public static Certificate pemToCert(File certFile) throws IOException, CertificateException {
        return pemToCert(new FileInputStream(certFile));
    }

    public static Certificate pemToCert(InputStream is) throws IOException, CertificateException {
        return pemToCert(new InputStreamReader(is));
    }

    public static Certificate pemToCert(Reader r) throws IOException, CertificateException {
        String header = "-----BEGIN CERTIFICATE-----";
        String footer = "-----END CERTIFICATE-----";

        BufferedReader reader = new BufferedReader(r);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos);

        String line;

        // ignore up to the header
        while ((line = reader.readLine()) != null && !line.equals(header)) {
        }

        // no header found
        if (line == null) {
            throw new IOException("Error reading certificate, missing BEGIN boundary");
        }

        // in between the header and footer is the actual certificate
        while ((line = reader.readLine()) != null && !line.equals(footer)) {
            line = line.replaceAll("\\s", "");
            ps.print(line);
        }

        // no footer found
        if (line == null) {
            throw new IOException("Error reading certificate, missing END boundary");
        }
        ps.close();

        // decode the buffer to a X509Certificate
        CertificateFactory cf = CertificateFactory.getInstance("X.509");
        byte[] certBytes = Base64.decodeBase64(baos.toByteArray());
        return cf.generateCertificate(new ByteArrayInputStream(certBytes));
    }

    public static String pemToPkHex(String certString) throws IOException, CertificateException {
        Certificate cert = pemToCert(certString);
        return Hex.encodeHex(cert.getPublicKey().getEncoded()).toString();
    }
}
