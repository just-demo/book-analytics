package edu.self.utils;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class CustomFileUtils {

    public static String read2(InputStream inputStream) throws IOException {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        byte[] bytes = new byte[4096];
        int n;
        while ((n = inputStream.read(bytes)) != -1) {
            buffer.write(bytes, 0, n);
        }
        return buffer.toString("UTF-8");
    }

    public static String read(InputStream inputStream) throws IOException {
        StringBuffer result = new StringBuffer();
        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
        String strLine;
        try {
            while ((strLine = br.readLine()) != null) {
                result.append(strLine);
                result.append("\n");
            }
        } catch (IllegalStateException e) {
            //IOException is thrown if a file does not end with new line character
        }
        return result.toString();
    }

    public static String read3(InputStream inputStream) throws IOException {
        StringBuffer result = new StringBuffer();
        for (int c = inputStream.read(); c != -1; c = inputStream.read()) {
            result.append((char) c);
        }
        return result.toString();
    }

    public static String read(String fileName) throws IOException {
        String content = null;
        FileInputStream fstream = null;
        DataInputStream inputStream = null;
        try {
            fstream = new FileInputStream(fileName);
            inputStream = new DataInputStream(fstream);
            content = read(inputStream);
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return content;
    }

    /*
     public static void write(String fileName, String data) throws IOException{
         DataOutputStream outStream = null;
         try {
             FileOutputStream fstream = new FileOutputStream(fileName);
             outStream = new DataOutputStream(fstream);
             write(outStream, data);
         } finally {
             if (outStream != null) {
                 outStream.close();
             }
         }
     }

     public static void write(OutputStream outStream, String data) throws IOException {
         BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(outStream));
         try {
             bw.write(data);
         }
         catch (IllegalStateException e) {
             //IOException is thrown if a file does not end with new line character
         }
         finally {
             if (bw != null) {
                 bw.flush();
                 bw.close();
             }
         }
     }
     */
}
