package com.mocoder.dingding.test.client;

import com.mocoder.dingding.utils.encryp.EncryptUtils;

import javax.net.ssl.*;
import java.io.*;
import java.net.*;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Iterator;
import java.util.Map;


public class HttpClientUtil {

    private final static String charset = "utf-8";
    private static Integer connectTimeout = null;
    private static Integer socketTimeout = null;
    private static String proxyHost = null;
    private static Integer proxyPort = null;



    public static String doGet(String url, Map<String, String> extraHeader) throws Exception {
        URLConnection connection = openConnection(url);
        HttpURLConnection httpURLConnection = (HttpURLConnection) connection;
        httpURLConnection.setRequestProperty("Accept-Charset", charset);
        httpURLConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        if (extraHeader != null && !extraHeader.isEmpty()) {
            for (Map.Entry<String, String> ent : extraHeader.entrySet()) {
                httpURLConnection.setRequestProperty(ent.getKey(), ent.getValue());
            }
        }
        InputStream inputStream = null;
        InputStreamReader inputStreamReader = null;
        BufferedReader reader = null;
        StringBuffer resultBuffer = new StringBuffer();
        String tempLine = null;
        if (httpURLConnection.getResponseCode() >= 300) {
            throw new Exception("HTTP Request is not success, Response code is " + httpURLConnection.getResponseCode());
        }
        try {
            inputStream = httpURLConnection.getInputStream();
            inputStreamReader = new InputStreamReader(inputStream);
            reader = new BufferedReader(inputStreamReader);
            while ((tempLine = reader.readLine()) != null) {
                resultBuffer.append(tempLine);
            }
        } finally {

            if (reader != null) {
                reader.close();
            }
            if (inputStreamReader != null) {
                inputStreamReader.close();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return resultBuffer.toString();
    }

    public static String doPost(String url, Map<String, String> extraHeader, Map parameterMap) throws Exception {
        /* Translate parameter map to parameter date string */
        StringBuffer parameterBuffer = new StringBuffer();
        if (parameterMap != null) {
            Iterator iterator = parameterMap.keySet().iterator();
            String key = null;
            String value = null;
            while (iterator.hasNext()) {
                key = (String) iterator.next();
                if (parameterMap.get(key) != null) {
                    value = (String) parameterMap.get(key);
                } else {
                    value = "";
                }
                parameterBuffer.append(key).append("=").append(value);
                if (iterator.hasNext()) {
                    parameterBuffer.append("&");
                }
            }
        }
        URLConnection connection = openConnection(url);
        HttpURLConnection httpURLConnection = (HttpURLConnection) connection;
        httpURLConnection.setDoOutput(true);
        httpURLConnection.setRequestMethod("POST");
        httpURLConnection.setRequestProperty("Accept-Charset", charset);
        httpURLConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        httpURLConnection.setRequestProperty("Content-Length", String.valueOf(parameterBuffer.length()));
        if (extraHeader != null && !extraHeader.isEmpty()) {
            for (Map.Entry<String, String> ent : extraHeader.entrySet()) {
                httpURLConnection.setRequestProperty(ent.getKey(), ent.getValue());
            }
        }
        OutputStream outputStream = null;
        OutputStreamWriter outputStreamWriter = null;
        InputStream inputStream = null;
        InputStreamReader inputStreamReader = null;
        BufferedReader reader = null;
        StringBuffer resultBuffer = new StringBuffer();
        String tempLine = null;
        try {
            outputStream = httpURLConnection.getOutputStream();
            outputStreamWriter = new OutputStreamWriter(outputStream);

            outputStreamWriter.write(parameterBuffer.toString());
            outputStreamWriter.flush();

            if (httpURLConnection.getResponseCode() >= 300) {
                throw new Exception("HTTP Request is not success, Response code is " + httpURLConnection.getResponseCode());
            }
            inputStream = httpURLConnection.getInputStream();
            inputStreamReader = new InputStreamReader(inputStream);
            reader = new BufferedReader(inputStreamReader);
            while ((tempLine = reader.readLine()) != null) {
                resultBuffer.append(tempLine);
            }
        } finally {
            if (outputStreamWriter != null) {
                outputStreamWriter.close();
            }
            if (outputStream != null) {
                outputStream.close();
            }
            if (reader != null) {
                reader.close();
            }
            if (inputStreamReader != null) {
                inputStreamReader.close();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return resultBuffer.toString();
    }

    private static URLConnection openConnection(String url) throws IOException {
        URL localURL = new URL(url);
        URLConnection connection;
        if (proxyHost != null && proxyPort != null) {
            Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(proxyHost, proxyPort));
            connection = localURL.openConnection(proxy);
        } else {
            connection = localURL.openConnection();
        }
        if(url.startsWith("https")){
            ((HttpsURLConnection)connection).setSSLSocketFactory(sslSocketFactory);
            ((HttpsURLConnection)connection).setHostnameVerifier(myHostnameVerifier);
        }
        return connection;
    }

    /**
     * Render request according setting
     *
     * @param connection
     */
    private static void renderRequest(URLConnection connection) {

        if (connectTimeout != null) {
            connection.setConnectTimeout(connectTimeout);
        }

        if (socketTimeout != null) {
            connection.setReadTimeout(socketTimeout);
        }

    }


    private static final HostnameVerifier myHostnameVerifier = new HostnameVerifier() {
        public boolean verify(String urlHostName, SSLSession session) {
            System.out.println("Warning: URL Host: " + urlHostName + " vs. "
                    + session.getPeerHost());
            return true;
        }
    };

    private static SSLSocketFactory sslSocketFactory;

    static {
        try {
            TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {

                @Override
                public X509Certificate[] getAcceptedIssuers() {
                    return null;
                }

                @Override
                public void checkServerTrusted(X509Certificate[] chain, String authType)
                        throws CertificateException {
                }

                @Override
                public void checkClientTrusted(X509Certificate[] chain, String authType)
                        throws CertificateException {
                }
            }};
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, trustAllCerts, null);
            sslSocketFactory = sslContext.getSocketFactory();
        } catch (Exception e) {
            new RuntimeException(e);
        }



    }


    public static void main(String[] args) {
        String tmpStr = "14589071547192919-5308-35d3d1ec-a2a7-48b7-b7ce-abafaf3be4ec66666";
        String targetToken = EncryptUtils.md5(tmpStr);
        System.out.println(targetToken);
    }

}