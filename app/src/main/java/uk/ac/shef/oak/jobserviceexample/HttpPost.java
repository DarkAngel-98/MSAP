package uk.ac.shef.oak.jobserviceexample;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import static android.net.wifi.WifiConfiguration.Status.strings;

public class HttpPost extends AsyncTask<Void, Void, String> {

    public static String response ;
    //public static String response = "Hell";
    public static final String TAG = "POST" ;
    public static String ping ;

    public HttpPost() {
        // prazen konstruktor ...
    }

    @Override
    protected String doInBackground(Void... voids) {
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        try {
            // za moja IP adresa: http://192.168.0.105:5000/postresults
            // testiranje: https://reqres.in/api/users

            // sledniot kod e slichen na kodot od stranata:
            // https://www.baeldung.com/httpurlconnection-post

            URL url = new URL("http://192.168.0.105:5000/postresults");
            HttpURLConnection connectionPost = (HttpURLConnection)url.openConnection();
            connectionPost.setRequestMethod("POST");
            connectionPost.setRequestProperty("Content-Type","application/json; utf-8");
            connectionPost.setRequestProperty("Accept","application/json");
            connectionPost.setDoOutput(true);

                String jsonString = " {\"result\": \"" + ping + " \"} ";


                try (OutputStream os = connectionPost.getOutputStream()) {
                    byte[] input = jsonString.getBytes("utf-8");
                    os.write(input, 0, input.length);
                }
                try (BufferedReader br = new BufferedReader(
                        new InputStreamReader(connectionPost.getInputStream(), "utf-8"))) {
                    StringBuilder response = new StringBuilder();
                    String responseLine = null;
                    while ((responseLine = br.readLine()) != null) {
                        response.append(responseLine.trim());
                    }
                    //System.out.println(response.toString());
                    Log.i(TAG, "response is: " + response.toString());
                }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //Log.i(TAG, "This should be the POST response: " + response) ;
        return null;

    }


    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        //Log.i(TAG, "This is onPostExecute in HTTP_POST: " + s) ;

    }
}
