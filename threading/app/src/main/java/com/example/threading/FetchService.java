package com.example.threading;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class FetchService extends Service {
    private final String TAG = "FetchService";
    private final int DELAY = 60000;
    private boolean bRunning = false;
    private FetchThread fetchThread;

    //opening new thread and logging move
    @Override
    public void onCreate() {
        super.onCreate();
        fetchThread = new FetchThread();
        Log.d(TAG, "in onCreate()");
    }

    //closing thread by interrupting it and setting it to null
    @Override
    public void onDestroy() {
        bRunning = false;
        fetchThread.interrupt();
        fetchThread = null;
        Log.d(TAG, "in onDestroy()");
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId){
        bRunning = true;
        fetchThread.start();
        Log.d(TAG, "service and thread start command running, start fetch thread");
        //sticky = stays alive on app exit
        //not sticky = kills thread on app exit
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }


    //new class for our thread - thread runs in a class setting -
    private class FetchThread extends Thread {
        public FetchThread() {
            super("FetchThread");
        }

        //actions thread takes while active, log action, sleep or pause for amount of time
        //if sleep is interrupted catch statement sets the running boolean to false
        @Override
        public void run()
        {
            while (bRunning == true)
            {
                try
                {
                    Log.d(TAG, "fetch executed on cycle");
                    Thread.sleep(DELAY);//delay in milliseconds
                } catch (InterruptedException e) {
                    bRunning = false;
                }
            }
        }
    }
}