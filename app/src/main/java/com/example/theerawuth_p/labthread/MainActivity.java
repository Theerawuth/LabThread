package com.example.theerawuth_p.labthread;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.example.theerawuth_p.labthread.service.CounterIntentService;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Object> {

    int counter;
    TextView tvCounter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        counter = 0;
        tvCounter = (TextView) findViewById(R.id.tvCounter);

        //**
        // Thread Method 7: IntentService
        // **//
        Intent intent = new Intent(MainActivity.this, CounterIntentService.class);
        intent.putExtra("abc","123");
        startService(intent);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public Loader<Object> onCreateLoader(int id, Bundle args) {
        if (id == 1) {
            return new AdderAsyncTaskLoader(MainActivity.this, 5, 11);
        }

        return null;
    }

    @Override
    public void onLoadFinished(Loader<Object> loader, Object data) {
        Log.d("ATL", "onLoadFinish");
        if (loader.getId() == 1) {
            Integer result = (Integer) data;
            tvCounter.setText(result + "");
        }
    }

    @Override
    public void onLoaderReset(Loader<Object> loader) {

    }

    static class AdderAsyncTaskLoader extends AsyncTaskLoader<Object> {

        int a, b;

        Integer result;

        Handler handler;

        public AdderAsyncTaskLoader(Context context, int a, int b) {
            super(context);
            this.a = a;
            this.b = b;
        }

        @Override
        protected void onStartLoading() {
            super.onStartLoading();
            Log.d("ATL", "onStartLoading");
            // Check Result
            if (result != null) {
                deliverResult(result);
            }
            // Initialize Handler receive content changed
            if (handler == null) {
                handler = new Handler() {
                    @Override
                    public void handleMessage(Message msg) {
                        super.handleMessage(msg);
                        a = (int) (Math.random() * 100);
                        b = (int) (Math.random() * 100);
                        onContentChanged();
                        handler.sendEmptyMessageDelayed(0, 3000);
                    }
                };
                handler.sendEmptyMessageDelayed(0, 3000);
            }
            if (takeContentChanged() || result == null) {
                forceLoad();
            }
        }

        @Override
        public Integer loadInBackground() {
            // Background Thread
            Log.d("ATL", "LoadInBackground");
//            try {
//                Thread.sleep(5000);
//            } catch (InterruptedException e) {
//
//            }
            result = a + b;
            return result;
        }

        @Override
        protected void onStopLoading() {
            super.onStopLoading();
            Log.d("ATL", "onStopLoading");
        }

        @Override
        protected void onReset() {
            super.onReset();
            //Destroy handler
            if (handler != null) {
                handler.removeCallbacksAndMessages(null);
                handler = null;
            }
        }
    }
}
