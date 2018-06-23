package edu.self.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

public class WebUtils {
    public static String load(String path) {
        return load(path, null);
    }

    public static String load(String path, Map<String, String> headers) {
        StringBuffer result = new StringBuffer();
        try {
            URL url;
            url = new URL(path);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            if (headers != null) {
                for (String header : headers.keySet()) {
                    connection.setRequestProperty(header, headers.get(header));
                }
            }
            // connection.setDoOutput(true);
            // connection.setRequestProperty("X-HTTP-Method-Override", "GET");
            // connection.setRequestProperty("Host", "translate.google.com.ua");
            // connection.setRequestProperty("User-Agent",
            // "Mozilla/5.0 (Windows; U; Windows NT 6.1; en-US; rv:1.9.2.25) Gecko/20111212 Firefox/3.6.25 GTB7.1");
            // connection.setRequestProperty("Accept",
            // "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
            // connection.setRequestProperty("Accept-Language",
            // "en-us,en;q=0.5");
            // connection.setRequestProperty("Accept-Encoding", "gzip,deflate");
            // connection.setRequestProperty("Accept-Charset",
            // "ISO-8859-1,utf-8;q=0.7,*;q=0.7");
            // connection.setRequestProperty("Keep-Alive", "115");
            // connection.setRequestProperty("Connection", "keep-alive");

            // connection.setRequestProperty("Referer",
            // "http://translate.google.com.ua/");

            // DataInputStream input = new
            // DataInputStream(connection.getInputStream());
            // for (int c = input.read(); c != -1; c = input.read()){
            // System.out.print((char) c);
            // }
            // input.close();
            BufferedReader in = null;
            try {
                in = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    if (result.length() > 0) {
                        result.append("\n");
                    }
                    result.append(inputLine);
                }

            } finally {
                if (in != null) {
                    in.close();
                }
            }

            // BufferedReader in = new BufferedReader(new
            // InputStreamReader(url.openStream()));
            // String inputLine;
            // while ((inputLine = in.readLine()) != null) {
            // System.out.println(inputLine);
            // }
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return result.toString();
    }
}
