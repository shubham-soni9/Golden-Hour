package com.goldenhour.util;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * This class helps to access the json data from URL over the network
 */
public class HttpRequester {

    // constructor
    public HttpRequester() {
    }

    public String getJSONFromUrl(String stringUrl) {

        String json = "";

        try {

            URL url = new URL(stringUrl);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            InputStream inputStream = new BufferedInputStream(urlConnection.getInputStream());

            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "ISO-8859-1"), 8);
            StringBuilder stringBuilder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null)
                stringBuilder.append(line + "\n");

            json = stringBuilder.toString();
            inputStream.close();
            Log.info("json", "==" + json);

        } catch (Exception e) {
            Log.error("Buffer Error", "Error parsing result " + e.toString());
        }

        return json;
    }
}