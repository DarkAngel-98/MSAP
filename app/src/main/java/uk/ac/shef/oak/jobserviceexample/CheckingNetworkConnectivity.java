package uk.ac.shef.oak.jobserviceexample;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


public class CheckingNetworkConnectivity {
    //Context context ;
    public static final String TAG = "NETWORK" ;
    //static String data = "" ;
    static String singleParse = "" ;
    static String finalParse = "" ;

    CheckingNetworkConnectivity(){
        // prazen konstruktot
    }




    static String getBackendInfo(){
        Log.i(TAG, "Vnatre vo backendInfo") ;
        /*
        //String pom = "https://www.json-generator.com/api/json/get/cfYwknRxFe?indent=2" ;
            // http://10.0.2.2:5000/getjobs
            // http://localhost:5000/getjobs
            // http://192.168.0.105:5000/getjobs/hardware   ... faza3_fizicki ured
            URL url = new URL("https://www.json-generator.com/api/json/get/cfYwknRxFe?indent=2") ; // ja zemame adresata
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection(); // otvorame konekcija na dadena adresa
            httpURLConnection.setRequestMethod("GET");
            InputStream inputStream = httpURLConnection.getInputStream() ;
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream)) ;//ja imame konekcijata i sakame da ja procitame vo bafer
         */
        HttpURLConnection httpURLConnection = null;
        BufferedReader bufferedReader = null;
        String data = "";
        try{

            URL url = new URL("http://192.168.0.105:5000/getjobs/hardware") ;

            //Log.i(TAG,"Connecting to http://10.0.2.2:5000/getjobs");
            Log.i(TAG,"Connecting to http://192.168.0.105:5000/getjobs/hardware");


            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.connect();
            Log.i(TAG,"Connected");

            InputStream inputStream = httpURLConnection.getInputStream();
            bufferedReader = new BufferedReader(new InputStreamReader(inputStream)) ;

            String line = "";
            while(line != null)
            {
                line = bufferedReader.readLine() ;
                data = data + line ;
                Log.i(TAG, data) ;
            }

        }catch (IOException e){
            e.printStackTrace();
            return null;
        }finally {
            if(httpURLConnection!=null){
                httpURLConnection.disconnect();
            }
            if(bufferedReader != null)
            {
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        Log.i(TAG,data);
        return data;
    }
}
