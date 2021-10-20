package com.example.samchatterappgentwo;

import android.net.Uri;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class NetworkAPI {
    public byte[] getResponseBytes(String urlString) throws Exception {
        URL postUrl = new URL(urlString);
        HttpURLConnection connection = (HttpURLConnection) postUrl.openConnection();
        connection.setRequestMethod("GET");
        //Read the Response From the inputStream as binary
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        InputStream in = connection.getInputStream();

        int responseCode = connection.getResponseCode();
        if (responseCode != HttpURLConnection.HTTP_OK){
            String message = String.format("ERROR: %d \n ERROR MSG %s",responseCode, connection.getResponseMessage());
            throw new Exception(message);
        }

        //read bytes in a buffer
        int bytesRead = 0;
        byte[] buffer = new byte[1024];
        while ((bytesRead = in.read(buffer)) > 0){
            //write bytes to our output stream
            out.write(buffer, 0, bytesRead);
        }
        out.close();
        //return byte array
        return out.toByteArray();
    }
    public String getResponseString(String urlString) throws Exception{
        byte[] responseBytes = getResponseBytes(urlString);
        String responseString = new String(responseBytes);
        return responseString;
    }
    public int postFormData(String urlString, Map<String, String> formDataPairs){
        int responseCode = HttpURLConnection.HTTP_BAD_REQUEST;
        // step1: create URL object "https://capstone1.app.dmitcapstone.ca/api/jitters/JitterServlet"
        try {
            URL postUrl = new URL(urlString);
            //step2: open a connection -> https
            HttpURLConnection connection = (HttpURLConnection) postUrl.openConnection();
            //step3: set the request method
            connection.setRequestMethod("POST");
            //step4: set the request content-type header parameter
            connection.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
            //step5: enable the connection to send output
            connection.setDoOutput(true);
            //step6: create request body pairs
           //done before method call
            //step7: convert body key-value pairs from the request form to a query string
            //format "PARAMNAME1=value&PARAM2=value2...
            StringBuilder requestBodyBuilder = new StringBuilder();
            formDataPairs.forEach((key,value) -> {
                if(requestBodyBuilder.length() > 0){
                    requestBodyBuilder.append("&");
                }
                requestBodyBuilder.append(String.format("%s=%s", key, value, Uri.encode(value, "utf-8")));
            });
            String requestBody = requestBodyBuilder.toString();

            OutputStream out = new BufferedOutputStream(connection.getOutputStream());
            out.write(requestBody.getBytes(StandardCharsets.UTF_8));
            out.close();

            responseCode = connection.getResponseCode();
            if (responseCode != HttpURLConnection.HTTP_OK){
                //error
            }
            connection.disconnect();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return responseCode;
    }
}
