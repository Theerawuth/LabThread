package com.example.theerawuth_p.labthread;

import android.content.Context;
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

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Object>  {

    int counter;
    TextView tvCounter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        counter = 0;
        tvCounter = (TextView) findViewById(R.id.tvCounter);

        //**
        // Thread Method 6: AsyncTaskLoader
        // **//
        getSupportLoaderManager().initLoader(1, null, this);

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
            if(result != null) {
                deliverResult(result);
            }
            forceLoad();
        }

        @Override
        public Integer loadInBackground() {
            // Background Thread
            Log.d("ATL", "LoadInBackground");
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {

            }

            result = a + b;
            return result;
        }

        @Override
        protected void onStopLoading() {
            super.onStopLoading();
            Log.d("ATL", "onStopLoading");
        }
    }
}
