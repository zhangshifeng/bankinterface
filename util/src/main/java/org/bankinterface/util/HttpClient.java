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
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.security.cert.CertificateException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.codec.binary.Base64;
import org.bankinterface.exception.HttpClientException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Send HTTP GET/POST requests.
 */
public class HttpClient {
    private static final Logger logger            = LoggerFactory.getLogger(HttpClient.class);
    private int                 hostVerification  = SSLUtil.HOSTCERT_NORMAL_CHECK;
    private int                 timeout           = 3000;
    private boolean             lineFeed          = true;
    private boolean             trustAny          = false;
    private boolean             followRedirects   = true;
    private boolean             keepAlive         = false;

    private String              contentType       = null;
    private String              streamCharset     = null;
    private String              url               = null;
    private String              rawStream         = null;
    private String              clientCertAlias   = null;
    private String              basicAuthUsername = null;
    private String              basicAuthPassword = null;

    private Map<String, String> parameters        = null;
    private Map<String, String> headers           = null;

    private URL                 requestUrl        = null;
    private URLConnection       con               = null;

    /** Creates an empty HttpClient object. */
    public HttpClient() {
    }

    /** Creates a new HttpClient object. */
    public HttpClient(URL url) {
        this.url = url.toExternalForm();
    }

    /** Creates a new HttpClient object. */
    public HttpClient(String url) {
        this.url = url;
    }

    /** Creates a new HttpClient object. */
    public HttpClient(String url, Map<String, String> parameters) {
        this.url = url;
        this.parameters = parameters;
    }

    /** Creates a new HttpClient object. */
    public HttpClient(URL url, Map<String, String> parameters) {
        this.url = url.toExternalForm();
        this.parameters = parameters;
    }

    /** Creates a new HttpClient object. */
    public HttpClient(String url, Map<String, String> parameters, Map<String, String> headers) {
        this.url = url;
        this.parameters = parameters;
        this.headers = headers;
    }

    /** Creates a new HttpClient object. */
    public HttpClient(URL url, Map<String, String> parameters, Map<String, String> headers) {
        this.url = url.toExternalForm();
        this.parameters = parameters;
        this.headers = headers;
    }

    /** Sets the timeout for waiting for the connection (default sec) */
    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    /** Enables this request to follow redirect 3xx codes (default true) */
    public void followRedirects(boolean followRedirects) {
        this.followRedirects = followRedirects;
    }

    /** Turns on or off line feeds in the request. (default is on) */
    public void setLineFeed(boolean lineFeed) {
        this.lineFeed = lineFeed;
    }

    /** Set the raw stream for posts. */
    public void setRawStream(String stream) {
        this.rawStream = stream;
    }

    /** Set the URL for this request. */
    public void setUrl(URL url) {
        this.url = url.toExternalForm();
    }

    /** Set the URL for this request. */
    public void setUrl(String url) {
        this.url = url;
    }

    /** Set the parameters for this request. */
    public void setParameters(Map<String, String> parameters) {
        this.parameters = parameters;
    }

    /** Set an individual parameter for this request. */
    public void setParameter(String name, String value) {
        if (parameters == null)
            parameters = new HashMap<String, String>();
        parameters.put(name, value);
    }

    /** Set the headers for this request. */
    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }

    /** Set an individual header for this request. */
    public void setHeader(String name, String value) {
        if (headers == null)
            headers = new HashMap<String, String>();
        headers.put(name, value);
    }

    /** Return a Map of headers. */
    public Map<String, String> getHeaders() {
        return headers;
    }

    /** Return a Map of parameters. */
    public Map<String, String> getParameters() {
        return parameters;
    }

    /** Return a string representing the requested URL. */
    public String getUrl() {
        return url;
    }

    /** Sets the content-type */
    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    /** Returns the content type */
    public String getContentType() {
        return this.contentType;
    }

    /** Sets the scream charset */
    public void setStreamCharset(String streamCharset) {
        this.streamCharset = streamCharset;
    }

    /** Returns the stream charset */
    public String getStreamCharset() {
        return this.streamCharset;
    }

    /** Toggle keep-alive setting */
    public void setKeepAlive(boolean keepAlive) {
        this.keepAlive = keepAlive;
    }

    /** Return keep-alive setting */
    public boolean getKeepAlive() {
        return this.keepAlive;
    }

    /**
     * Sets the client certificate alias (from the keystore) to use for this SSL
     * connection.
     */
    public void setClientCertificateAlias(String alias) {
        this.clientCertAlias = alias;
    }

    /**
     * Returns the alias of the client certificate to be used for this SSL
     * connection.
     */
    public String getClientCertificateAlias() {
        return this.clientCertAlias;
    }

    /** Sets the server hostname verification level */
    public void setHostVerificationLevel(int level) {
        this.hostVerification = level;
    }

    /** Returns the current server hostname verification level */
    public int getHostVerificationLevel() {
        return this.hostVerification;
    }

    /** Allow untrusted server certificates */
    public void setAllowUntrusted(boolean trustAny) {
        this.trustAny = trustAny;
    }

    /** Do we trust any certificate */
    public boolean getAllowUntrusted() {
        return this.trustAny;
    }

    public void setBasicAuthInfo(String basicAuthUsername, String basicAuthPassword) {
        this.basicAuthUsername = basicAuthUsername;
        this.basicAuthPassword = basicAuthPassword;
    }

    /** Invoke HTTP request GET. */
    public String get() throws HttpClientException {
        return sendHttpRequest("get");
    }

    /** Invoke HTTP request GET. */
    public InputStream getStream() throws HttpClientException {
        return sendHttpRequestStream("get");
    }

    /** Invoke HTTP request POST. */
    public String post() throws HttpClientException {
        return sendHttpRequest("post");
    }

    /** Invoke HTTP request POST and pass raw stream. */
    public String post(String stream) throws HttpClientException {
        this.rawStream = stream;
        return sendHttpRequest("post");
    }

    /** Invoke HTTP request POST. */
    public InputStream postStream() throws HttpClientException {
        return sendHttpRequestStream("post");
    }

    /** Returns the value of the specified named response header field. */
    public String getResponseHeader(String header) throws HttpClientException {
        if (con == null) {
            throw new HttpClientException("Connection not yet established");
        }
        return con.getHeaderField(header);
    }

    /** Returns the key for the nth response header field. */
    public String getResponseHeaderFieldKey(int n) throws HttpClientException {
        if (con == null) {
            throw new HttpClientException("Connection not yet established");
        }
        return con.getHeaderFieldKey(n);
    }

    /**
     * Returns the value for the nth response header field. It returns null of
     * there are fewer then n fields.
     */
    public String getResponseHeaderField(int n) throws HttpClientException {
        if (con == null) {
            throw new HttpClientException("Connection not yet established");
        }
        return con.getHeaderField(n);
    }

    /** Returns the content of the response. */
    public Object getResponseContent() throws java.io.IOException, HttpClientException {
        if (con == null) {
            throw new HttpClientException("Connection not yet established");
        }
        return con.getContent();
    }

    /** Returns the content-type of the response. */
    public String getResponseContentType() throws HttpClientException {
        if (con == null) {
            throw new HttpClientException("Connection not yet established");
        }
        return con.getContentType();
    }

    /** Returns the content length of the response */
    public int getResponseContentLength() throws HttpClientException {
        if (con == null) {
            throw new HttpClientException("Connection not yet established");
        }
        return con.getContentLength();
    }

    /** Returns the content encoding of the response. */
    public String getResponseContentEncoding() throws HttpClientException {
        if (con == null) {
            throw new HttpClientException("Connection not yet established");
        }
        return con.getContentEncoding();
    }

    public int getResponseCode() throws HttpClientException {
        if (con == null) {
            throw new HttpClientException("Connection not yet established");
        }
        if (!(con instanceof HttpURLConnection)) {
            throw new HttpClientException("Connection is not HTTP; no response code");
        }

        try {
            return ((HttpURLConnection) con).getResponseCode();
        } catch (IOException e) {
            throw new HttpClientException(e.getMessage(), e);
        }
    }

    public String sendHttpRequest(String method) throws HttpClientException {
        InputStream in = sendHttpRequestStream(method);
        if (in == null)
            return null;

        StringBuilder buf = new StringBuilder();
        try {
            String charset = null;
            String contentType = con.getContentType();
            if (contentType == null) {
                try {
                    contentType = URLConnection.guessContentTypeFromStream(in);
                } catch (IOException ioe) {
                    logger.warn("Problems guessing content type from steam");
                }
            }

            if (logger.isDebugEnabled()) {
                logger.debug("Content-Type: " + contentType);
            }

            if (contentType != null) {
                contentType = contentType.toUpperCase();
                int charsetEqualsLoc = contentType.indexOf("=", contentType.indexOf("CHARSET"));
                int afterSemiColon = contentType.indexOf(";", charsetEqualsLoc);
                if (charsetEqualsLoc >= 0 && afterSemiColon >= 0) {
                    charset = contentType.substring(charsetEqualsLoc + 1, afterSemiColon);
                } else if (charsetEqualsLoc >= 0) {
                    charset = contentType.substring(charsetEqualsLoc + 1);
                }

                if (charset != null) {
                    charset = charset.trim();
                }

                if (logger.isDebugEnabled()) {
                    logger.debug("Getting text from HttpClient with charset: " + charset);
                }
            }

            BufferedReader post = new BufferedReader(charset == null ? new InputStreamReader(in)
                    : new InputStreamReader(in, charset));
            String line = "";

            if (logger.isDebugEnabled()) {
                logger.debug("---- HttpClient Response Content ----");
            }

            while ((line = post.readLine()) != null) {
                if (logger.isDebugEnabled()) {
                    logger.debug("[HttpClient] : " + line);
                }
                buf.append(line);
                if (lineFeed) {
                    buf.append("\n");
                }
            }
        } catch (Exception e) {
            throw new HttpClientException("Error processing input stream", e);
        }
        return buf.toString();
    }

    private InputStream sendHttpRequestStream(String method) throws HttpClientException {
        return sendHttpRequestStream(method, false);
    }

    private InputStream sendHttpRequestStream(String method, boolean overrideTrust) throws HttpClientException {

        String arguments = null;
        InputStream in = null;

        if (url == null) {
            throw new HttpClientException("Cannot process a null URL.");
        }

        if (rawStream != null) {
            arguments = rawStream;
        } else if (parameters != null) {
            arguments = urlEncodeArgs(parameters);
        }

        // Append the arguments to the query string if GET.
        if (method.equalsIgnoreCase("get") && arguments != null) {
            if (url.contains("?")) {
                url = url + "&" + arguments;
            } else {
                url = url + "?" + arguments;
            }
        }

        // Create the URL and open the connection.
        try {
            requestUrl = new URL(url);
            if (overrideTrust) {
                con = URLConnector.openUntrustedConnection(requestUrl, timeout, clientCertAlias, hostVerification);
            } else {
                con = URLConnector.openConnection(requestUrl, timeout, clientCertAlias, hostVerification);
            }

            if (logger.isDebugEnabled()) {
                logger.debug("Connection opened to : " + requestUrl.toExternalForm());
            }

            if ((con instanceof HttpURLConnection)) {
                ((HttpURLConnection) con).setInstanceFollowRedirects(followRedirects);
                if (logger.isDebugEnabled()) {
                    logger.debug("Connection is of type HttpURLConnection, more specifically: "
                            + con.getClass().getName());
                }
            }

            // set the content type
            if (contentType != null) {
                con.setRequestProperty("Content-type", contentType);
            }

            // connection settings
            con.setDoOutput(true);
            con.setUseCaches(false);
            if (keepAlive) {
                con.setRequestProperty("Connection", "Keep-Alive");
            }

            if (method.equalsIgnoreCase("post")) {
                if (contentType == null) {
                    con.setRequestProperty("Content-type", "application/x-www-form-urlencoded");
                }
                con.setDoInput(true);
            }

            // if there is basicAuth info set the request property for it
            if (basicAuthUsername != null) {
                String token = basicAuthUsername + ":" + (basicAuthPassword == null ? "" : basicAuthPassword);
                String basicAuthString = "Basic " + Base64.encodeBase64String(token.getBytes());
                con.setRequestProperty("Authorization", basicAuthString);
                if (logger.isDebugEnabled()) {
                    logger.debug("Header - Authorization: " + basicAuthString);
                }
            }

            if (headers != null) {
                for (Map.Entry<String, String> entry : headers.entrySet()) {
                    String headerName = entry.getKey();
                    String headerValue = entry.getValue();
                    con.setRequestProperty(headerName, headerValue);
                    if (logger.isDebugEnabled()) {
                        logger.debug("Header - " + headerName + ": " + headerValue);
                    }
                }
            }

            if (method.equalsIgnoreCase("post")) {
                OutputStreamWriter out = new OutputStreamWriter(con.getOutputStream(),
                        this.streamCharset != null ? this.streamCharset : "UTF-8");
                if (logger.isDebugEnabled()) {
                    logger.debug("Opened output stream");
                }

                if (arguments != null) {
                    out.write(arguments);
                    if (logger.isDebugEnabled()) {
                        logger.debug("Wrote arguements (parameters) : " + arguments);
                    }
                }

                out.flush();
                out.close();
                if (logger.isDebugEnabled()) {
                    logger.debug("Flushed and closed buffer");
                }
            }

            if (logger.isDebugEnabled()) {
                Map<String, List<String>> headerFields = con.getHeaderFields();
                logger.debug("Header Fields : " + headerFields);
            }

            in = con.getInputStream();
        } catch (IOException ioe) {
            if ((trustAny && !overrideTrust) && (ioe.getCause() instanceof CertificateException)) {
                logger.warn("Try again override Trust");
                return sendHttpRequestStream(method, true);
            }
            throw new HttpClientException("IO Error processing request", ioe);
        } catch (Exception e) {
            throw new HttpClientException("Error processing request", e);
        }

        return in;
    }

    private String urlEncodeArgs(Map<String, String> parameters) {
        StringBuilder sb = new StringBuilder();
        if (parameters != null) {
            boolean isFirst = true;
            String charset = this.streamCharset != null ? this.streamCharset : "UTF-8";
            for (String key : parameters.keySet()) {
                if (isFirst) {
                    isFirst = false;
                } else {
                    sb.append("&");
                }
                sb.append(encodeString(key, charset));
                sb.append("=");
                sb.append(encodeString(parameters.get(key), charset));
            }
        }
        return sb.toString();
    }

    private String encodeString(String value, String charset) {
        if (value == null) {
            return "";
        }
        try {
            return URLEncoder.encode(value, charset);
        } catch (UnsupportedEncodingException e) {
            return value;
        }
    }

    /**
     * Get the content of a URL;
     * 
     * @param url
     * @return
     * @throws HttpClientException
     */
    public static String getUrlContent(String url) throws HttpClientException {
        HttpClient client = new HttpClient(url);
        return client.get();
    }

    /**
     * Test a URL is valid.
     * 
     * @param url
     * @return response code
     * @throws HttpClientException
     */
    public static int checkHttpRequest(String url) throws HttpClientException {
        HttpClient client = new HttpClient(url);
        client.get();
        return client.getResponseCode();
    }

}
