/*
 * Copyright (c) 2019. This code has been developed by Fabio Ciravegna, The University of Sheffield. All rights reserved. No part of this code can be used without the explicit written permission by the author
 */

package uk.ac.shef.oak.jobserviceexample;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import uk.ac.shef.oak.jobserviceexample.restarter.RestartServiceBroadcastReceiver;

public class MainActivity extends Activity {

    public static final String TAG = "MainActivity" ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);

        Log.i(TAG, "onCreate") ;


        //finish() ;
    }
    /*
    @Override
    protected void onStart() {
        super.onStart();
        Log.i(TAG, "onStart") ;
    }

     */

    @Override
    protected void onResume() {
        super.onResume();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            RestartServiceBroadcastReceiver.scheduleJob(getApplicationContext());
        } else {
            ProcessMainClass bck = new ProcessMainClass();
            bck.launchService(getApplicationContext());
        }

        Log.i(TAG, "onResume") ;
        finish();

    }
    /*
    @Override
    protected void onPause() {
        super.onPause();
        Log.i(TAG, "onPause") ;
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i(TAG, "onStop") ;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "onDestroy") ;
    }

     */
}
