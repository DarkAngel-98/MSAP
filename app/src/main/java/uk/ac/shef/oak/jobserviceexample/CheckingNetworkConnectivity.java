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
    Context context ;
    public static final String TAG = CheckingNetworkConnectivity.class.getSimpleName() ;
  // TAG ne raboti so:
  //public static final String TAG = "CheckingNetworkConnectivity" ;
    CheckingNetworkConnectivity(){
        // prazen konstruktot
    }
    // pravime proverka za konektivnost:
    /*
    public static boolean isWifiConnected(Context context) {
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if(info != null && info.isConnected() && info.isAvailable()) {
            Toast.makeText(context, "Connection is Established", Toast.LENGTH_SHORT).show();
            return true;
        }
        else {
            Toast.makeText(context, "Connection is NOT Established", Toast.LENGTH_SHORT).show();
            return false;
        }
    }

     */

    // sega otkako ja proverivme konekcijata, mozhe da prezememe info od backend-ot:

    static String getBackendInfo(){
        /*
        //String pom = "https://www.json-generator.com/api/json/get/cfYwknRxFe?indent=2" ;
            // http://10.0.2.2:5000/getjobs
            // http://localhost:5000/getjobs
            URL url = new URL("https://www.json-generator.com/api/json/get/cfYwknRxFe?indent=2") ; // ja zemame adresata
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection(); // otvorame konekcija na dadena adresa
            httpURLConnection.setRequestMethod("GET");
            InputStream inputStream = httpURLConnection.getInputStream() ;
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream)) ;//ja imame konekcijata i sakame da ja procitame vo bafer
         */
        HttpURLConnection httpURLConnection = null;
        BufferedReader bufferedReader = null;
        String JSONString;
        try{

            Uri builtUri = Uri.parse("http://10.0.2.2:5000/getjobs")
                    .buildUpon()
                    .build();

            Log.i(TAG,"Connecting to http://10.0.2.2:5000/getjobs");

            URL requestUrl = new URL(builtUri.toString());
            httpURLConnection = (HttpURLConnection) requestUrl.openConnection();
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.connect();
            Log.i(TAG,"Connected");

            InputStream inputStream = httpURLConnection.getInputStream();
            StringBuilder buffer = new StringBuilder();

            bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while((line = bufferedReader.readLine()) != null)
            {
                buffer.append(line);
                buffer.append("\n");
            }
            JSONString=buffer.toString();
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
        Log.i("KRAEN_STRING",JSONString);
        return JSONString;
    }
}
