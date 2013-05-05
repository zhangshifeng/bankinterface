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
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.security.GeneralSecurityException;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Create URLConnection.
 */
public class URLConnector {
    private static final Logger logger          = LoggerFactory.getLogger(URLConnector.class);
    private static int          timeout         = 3000;
    private URLConnection       connection      = null;
    private URL                 url             = null;
    private String              clientCertAlias = null;
    private boolean             timedOut        = false;
    private boolean             trustAnyCert    = false;
    private int                 hostCertLevel   = 2;

    protected URLConnector() {
    }

    protected URLConnector(URL url, String clientCertAlias, int hostCertLevel, boolean trustAnyCert) {
        this.clientCertAlias = clientCertAlias;
        this.url = url;
        this.trustAnyCert = trustAnyCert;
        this.hostCertLevel = hostCertLevel;
    }

    protected synchronized URLConnection openConnection(int timeout) throws IOException {
        Thread t = new Thread(new URLConnectorThread());
        t.start();

        try {
            this.wait(timeout);
        } catch (InterruptedException e) {
            if (connection == null) {
                timedOut = true;
            } else {
                close(connection);
            }
            throw new IOException("Connection never established");
        }

        if (connection != null) {
            return connection;
        } else {
            timedOut = true;
            throw new IOException("Connection timed out");
        }
    }

    // trusted certs only
    public static URLConnection openConnection(URL url) throws IOException {
        return openConnection(url, timeout);
    }

    public static URLConnection openConnection(URL url, int timeout) throws IOException {
        return openConnection(url, timeout, null, SSLUtil.HOSTCERT_NORMAL_CHECK);
    }

    public static URLConnection openConnection(URL url, String clientCertAlias) throws IOException {
        return openConnection(url, timeout, clientCertAlias, SSLUtil.HOSTCERT_NORMAL_CHECK);
    }

    public static URLConnection openConnection(URL url, int timeout, String clientCertAlias, int hostCertLevel)
            throws IOException {
        URLConnector uc = new URLConnector(url, clientCertAlias, hostCertLevel, false);
        return uc.openConnection(timeout);
    }

    // allow untrusted certs
    public static URLConnection openUntrustedConnection(URL url) throws IOException {
        return openConnection(url, timeout);
    }

    public static URLConnection openUntrustedConnection(URL url, int timeout) throws IOException {
        return openConnection(url, timeout, null, SSLUtil.HOSTCERT_NORMAL_CHECK);
    }

    public static URLConnection openUntrustedConnection(URL url, String clientCertAlias) throws IOException {
        return openConnection(url, timeout, clientCertAlias, SSLUtil.HOSTCERT_NORMAL_CHECK);
    }

    public static URLConnection openUntrustedConnection(URL url, int timeout, String clientCertAlias, int hostCertLevel)
            throws IOException {
        URLConnector uc = new URLConnector(url, clientCertAlias, hostCertLevel, true);
        return uc.openConnection(timeout);
    }

    // special thread to open the connection
    private class URLConnectorThread implements Runnable {
        public void run() {
            URLConnection con = null;
            try {
                con = url.openConnection();

                if ("HTTPS".equalsIgnoreCase(url.getProtocol())) {
                    HttpsURLConnection scon = (HttpsURLConnection) con;
                    try {
                        scon.setSSLSocketFactory(SSLUtil.getSSLSocketFactory(clientCertAlias, trustAnyCert));
                        HostnameVerifier hv = SSLUtil.getHostnameVerifier(hostCertLevel);
                        if (hv != null) {
                            scon.setHostnameVerifier(hv);
                        }
                    } catch (GeneralSecurityException e) {
                        logger.error(e.getMessage());
                    }
                }
            } catch (IOException e) {
                logger.error(e.getMessage());
            }

            synchronized (URLConnector.this) {
                if (timedOut && con != null) {
                    close(con);
                } else {
                    connection = con;
                    URLConnector.this.notify();
                }
            }
        }
    }

    // closes the HttpURLConnection does nothing to others
    private static void close(URLConnection con) {
        if (con instanceof HttpURLConnection) {
            ((HttpURLConnection) con).disconnect();
        }
    }
}
