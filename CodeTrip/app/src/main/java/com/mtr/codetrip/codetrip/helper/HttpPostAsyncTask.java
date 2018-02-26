package com.mtr.codetrip.codetrip.helper;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by jiajunzhou on 2018-02-26.
 */

public class HttpPostAsyncTask extends AsyncTask<String, Void, String> {
    // This is the JSON body of the post
    JSONObject postData;
    public AsyncResponse delegate = null;
    // This is a constructor that allows you to pass in the JSON body

    public HttpPostAsyncTask(String code) {

        Map<String, String> data = new HashMap<>();
        data.put("text", code);
        this.postData = new JSONObject(data);
    }

    // This is a function that we are overriding from AsyncTask. It takes Strings as parameters because that is what we defined for the parameters of our async task
    @Override
    protected String doInBackground(String... params) {
        String response = "";

        try {
            // This is getting the url from the string we passed in
            URL url = new URL("http://aws.jiajunzhou.ca:80");

            // Create the urlConnection
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

            urlConnection.setDoInput(true);
            urlConnection.setDoOutput(true);
            urlConnection.setRequestProperty("Content-Type", "application/json");
            urlConnection.setRequestMethod("POST");

            // OPTIONAL - Sets an authorization header
            urlConnection.setRequestProperty("Authorization", "someAuthString");

            StringBuilder responseBuilder = new StringBuilder();
            // Send the post body
            if (this.postData != null) {
                OutputStreamWriter writer = new OutputStreamWriter(urlConnection.getOutputStream());
                writer.write(postData.toString());
                writer.flush();
            }

            int statusCode = urlConnection.getResponseCode();

            if (statusCode ==  200) {
                BufferedReader br = new BufferedReader(
                        new InputStreamReader(urlConnection.getInputStream(), "utf-8"));
                String line = null;
                while ((line = br.readLine()) != null) {
                    responseBuilder.append(line + "\n");
                }
                br.close();
                response = responseBuilder.toString();
                // From here you can convert the string to JSON with whatever JSON parser you like to use
                // After converting the string to JSON, I call my custom callback. You can follow this process too, or you can implement the onPostExecute(Result) method
            } else {
                System.out.println(urlConnection.getResponseMessage());
                // Status code is not 200
                // Do something to handle the error
            }
        } catch (Exception e) {
            Log.d("ERROR", e.getLocalizedMessage());
        }
        return response;
    }

    @Override
    protected void onPostExecute(String response) {
        delegate.processFinish(response);
        //handleResponses(response);
    }
}
