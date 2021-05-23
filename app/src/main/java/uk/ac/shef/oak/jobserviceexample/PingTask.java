package uk.ac.shef.oak.jobserviceexample;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class PingTask extends AsyncTask<Void, Void, String> {

    public PingTask(){
        // prazen konstruktor koj kje go vikame kaj initializeTimerTask vo Service klasata
    }

    public static final String TAG = "PingTask" ;

    @Override
    protected String doInBackground(Void... voids) {
        /*
        try{
            Thread.sleep(600000) ;
            // thread treba da spie 600 000 za da se 10 minuti, ama zaradi testiranje kje bide na 5 sekundi (5 000)
        }catch (InterruptedException e){
            e.printStackTrace();
        }

        */
        Log.i(TAG, "doInBackground") ;
        return CheckingNetworkConnectivity.getBackendInfo();
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        //String pingResult = "";
        try{
            JSONArray JA = new JSONArray(s) ;

            for(int i = 0; i<JA.length();i++){
                JSONObject JO = JA.getJSONObject(i) ;

                Log.i(TAG, "jobType: " + JO.getString("jobType")) ;
                Log.i(TAG, "host: " + JO.getString("host")) ;
                Log.i(TAG, "count: " + JO.getString("count")) ;
                Log.i(TAG, "packetSize: " + JO.getString("packetSize")) ;
                Log.i(TAG, "jobPeriod: " + JO.getString("jobPeriod")) ;
                Log.i(TAG, "date: " + JO.getString("date")) ;

                String pingCmd = "ping  -c  " + JO.getString("count");
                pingCmd = pingCmd + " -s " + JO.getString("packetSize");
                pingCmd = pingCmd +" " + JO.getString("host");
                String pingResult = "";
                Runtime r = Runtime.getRuntime();
                Process p = r.exec(pingCmd);
                BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));
                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    pingResult += inputLine;
                }
                in.close();

                // kodot e slicen na kod od stranata:
                // https://www.codeproject.com/Questions/1098186/How-to-get-unix-timestamp-while-pinging-in-android

                HttpPost.ping = pingResult ;
                Log.i(TAG,"pingResult " + pingResult);
                Service.pingResult = pingResult;
            }
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
        }
    }

