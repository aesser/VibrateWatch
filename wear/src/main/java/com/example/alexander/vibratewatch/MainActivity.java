package com.example.alexander.vibratewatch;

import android.os.Bundle;
import android.os.Vibrator;
import android.support.wearable.activity.WearableActivity;
import android.support.wearable.view.BoxInsetLayout;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import com.loopj.android.http.*;

import cz.msebera.android.httpclient.Header;

public class MainActivity extends WearableActivity {
    private BoxInsetLayout mContainerView;
    private Button mButton;
    private TextView mResult;
    private TextView mWebResult;
    private ScheduledExecutorService mScheduledExecutorService;

    //private NotificationRunner notifier;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setAmbientEnabled();

        mContainerView = (BoxInsetLayout) findViewById(R.id.container);
        mButton = (Button) findViewById(R.id.btn);
        mResult = (TextView) findViewById(R.id.result);
        mWebResult = (TextView) findViewById(R.id.webresult);

        mScheduledExecutorService = Executors.newScheduledThreadPool(1);

        mScheduledExecutorService.scheduleAtFixedRate(new Runnable() {
            public void run() {
                Debugger.log("refreshed " + new Date().toString());

                // If you need update UI, simply do this:
                runOnUiThread(new Runnable() {
                    public void run() {
                        // update your UI component here.
                        mResult.setText("last update: " + new Date().toString());
                    }
                });
            }
        }, 5, 5, TimeUnit.SECONDS);

    }

    public void runRequest(){
        AsyncHttpClient client = new AsyncHttpClient();
        client.get("http://noti.azurewebsites.net", new AsyncHttpResponseHandler() {

            @Override
            public void onStart() {
                // called before request is started
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, final byte[] response) {
                // called when response HTTP status is "200 OK"
                runOnUiThread(new Runnable() {
                    public void run() {
                        String res = response != null && response.length > 0
                                ? new String(response)
                                : "";
                        mWebResult.setText(res);
                    }
                });
            }

            @Override
            public void onFailure(final int statusCode, Header[] headers, final byte[] errorResponse, Throwable e) {
                // called when response HTTP status is "4XX" (eg. 401, 403, 404)
                runOnUiThread(new Runnable() {
                    public void run() {
                        mWebResult.setText("Oh no, error: " + statusCode);
                    }
                });
            }

            @Override
            public void onRetry(int retryNo) {
                // called when request is retried
            }
        });
    }


    @Override
    protected void onDestroy() {
        //mScheduledExecutorService.shutdown();
        super.onDestroy();
    }

    @Override
    public void onEnterAmbient(Bundle ambientDetails) {
        super.onEnterAmbient(ambientDetails);
        updateDisplay();
    }

    @Override
    public void onUpdateAmbient() {
        super.onUpdateAmbient();
        updateDisplay();
    }

    @Override
    public void onExitAmbient() {
        updateDisplay();
        super.onExitAmbient();
    }



    public void runCmd(View view){
        runRequest();
        runVibrate();
    }

    private void runVibrate(){
        Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        long[] vibrationPattern = {0, 500, 50, 300};
        //-1 - don't repeat
        final int indexInPatternToRepeat = -1;
        try {
            vibrator.vibrate(vibrationPattern, indexInPatternToRepeat);
        } catch (Exception ex) {
            Debugger.log(ex);
        }
    }

    private void updateDisplay() {
        if (isAmbient()) {
            mContainerView.setBackgroundColor(getResources().getColor(android.R.color.black));
            mButton.setTextColor(getResources().getColor(android.R.color.white));
            mResult.setTextColor(getResources().getColor(android.R.color.white));
        } else {
            mContainerView.setBackground(null);
            mButton.setTextColor(getResources().getColor(android.R.color.black));
            mResult.setTextColor(getResources().getColor(android.R.color.black));
        }
    }

}
