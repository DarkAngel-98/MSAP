/*
 * Copyright (c) 2019. This code has been developed by Fabio Ciravegna, The University of Sheffield. All rights reserved. No part of this code can be used without the explicit written permission by the author
 */

package uk.ac.shef.oak.jobserviceexample;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

import java.util.Timer;
import java.util.TimerTask;

import uk.ac.shef.oak.jobserviceexample.utilities.Notification;

public class Service extends android.app.Service {
    protected static final int NOTIFICATION_ID = 1337;
    //private static String TAG = "Service";
    private static String TAG = "SERVICE";
    private static Service mCurrentService;
    private int counter = 0;

    public int brojacZaSaveData = 0 ;
    public static final String SHARED_PREFS = "sharedPrefs" ;
    public static final String PING_RESULT = "PING_RESULT" ;

    public static String pingResult = null;
    private TimerTask timertask;

    public Service() {
        super();
    }


    @Override
    public void onCreate() {
        super.onCreate();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            restartForeground();
        }
        mCurrentService = this;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        Log.d(TAG, "restarting Service !!");
        //counter = 0;

        // it has been killed by Android and now it is restarted. We must make sure to have reinitialised everything
        if (intent == null) {
            ProcessMainClass bck = new ProcessMainClass();
            bck.launchService(this);
        }

        // make sure you call the startForeground on onStartCommand because otherwise
        // when we hide the notification on onScreen it will nto restart in Android 6 and 7
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            restartForeground();
        }

        ConnectivityManager manager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();

        try{
            if(networkInfo != null && networkInfo.isConnected())
            {
                Log.i(TAG, "Connected") ;
                startAsync();
            }
            else{
                brojacZaSaveData ++ ;
                if(brojacZaSaveData <= 3)
                for(int i = 0;i<brojacZaSaveData;i++) {
                    saveData();
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
       // MISLAM DEKA TREBA DA SE TRGNE startTimer()
        // startTimer();

        // return start sticky so if it is killed by android, it will be restarted with Intent null
        return START_STICKY;
    }

    public void startAsync(){
        Timer timer = new Timer();
        initializeTimerTask();
        Log.i(TAG, "Setting timer!") ;
        timer.schedule(timertask,1000,10000);
        // period: 120 000 (potsetnik).... 10 000 e za testiranje staveno
        Log.i(TAG, "Timer set!") ;
    }

    public void initializeTimerTask() {

        timertask = new TimerTask() {
            @Override
            public void run() {

                Log.i(TAG, "Before PingTask executes ...") ;
                new PingTask().execute();
                Log.i(TAG, "After we execute PingTask with PingTask().execute() ....") ;

                Log.i(TAG, "We start HttpPost with HttpPost().execute() .... ") ;
                new HttpPost().execute() ;
                Log.i(TAG, "We finish with HttpPost().execute() . ") ;

            }
        };
    }

    public void saveData(){
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE) ;
        // MODE_PRIVATE znachi deka ne mozhe druga aplikacija da promeni toa shto imame zachuvano.
        SharedPreferences.Editor editor = sharedPreferences.edit() ;
        editor.putString(PING_RESULT, pingResult) ;
        editor.apply();
    }

    public String loadData(){
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE) ;
        String zachuvanPing = sharedPreferences.getString(SHARED_PREFS, "") ;
        return zachuvanPing ;
    }



    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    /**
     * it starts the process in foreground. Normally this is done when screen goes off
     * THIS IS REQUIRED IN ANDROID 8 :
     * "The system allows apps to call Context.startForegroundService()
     * even while the app is in the background.
     * However, the app must call that service's startForeground() method within five seconds
     * after the service is created."
     */
    public void restartForeground() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Log.i(TAG, "restarting foreground");
            try {
                Notification notification = new Notification();
                startForeground(NOTIFICATION_ID, notification.setNotification(this, "Service notification", "This is the service's notification", R.drawable.ic_sleep));
                Log.i(TAG, "restarting foreground successful");
                //startTimer();
                new PingTask().execute();
            } catch (Exception e) {
                Log.e(TAG, "Error in notification " + e.getMessage());
            }
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "onDestroy called");
        // restart the never ending service
        Intent broadcastIntent = new Intent(Globals.RESTART_INTENT);
        sendBroadcast(broadcastIntent);
        //stoptimertask();
        new PingTask().execute() ;
    }


    /**
     * this is called when the process is killed by Android
     *
     * @param rootIntent
     */

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        super.onTaskRemoved(rootIntent);
        Log.i(TAG, "onTaskRemoved called");
        // restart the never ending service
        Intent broadcastIntent = new Intent(Globals.RESTART_INTENT);
        sendBroadcast(broadcastIntent);
        // do not call stoptimertask because on some phones it is called asynchronously
        // after you swipe out the app and therefore sometimes
        // it will stop the timer after it was restarted
        // stoptimertask();
    }


    public static Service getmCurrentService() {
        return mCurrentService;
    }

    public static void setmCurrentService(Service mCurrentService) {
        Service.mCurrentService = mCurrentService;
    }


}
