package uk.ac.shef.oak.jobserviceexample;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class PingTask extends AsyncTask<Void, Void, String> {

    public static final String TAG = "PingTask" ;

    @Override
    protected String doInBackground(Void... voids) {

        try{
            Thread.sleep(600000) ;
        }catch (InterruptedException e){
            e.printStackTrace();
        }

        return CheckingNetworkConnectivity.getBackendInfo();
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);

        try{
            JSONArray JA = new JSONArray(s) ;

            for(int i = 0; i<JA.length();i++){
                JSONObject JO = JA.getJSONObject(i) ;

                Log.i(TAG, "type: " + JO.getString("jobType")) ;
                Log.i(TAG, "host: " + JO.getString("host")) ;
                Log.i(TAG, "count: " + JO.getString("count")) ;
                Log.i(TAG, "packetSize: " + JO.getString("packetSize")) ;
                Log.i(TAG, "jobPeriod: " + JO.getString("jobPeriod")) ;
                Log.i(TAG, "date: " + JO.getString("data")) ;

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
                Log.i(TAG,"pingResult " + pingResult);
                Service.ping = pingResult;


            }
        }catch (JSONException | IOException e){
            e.printStackTrace();
        }
    }
}
