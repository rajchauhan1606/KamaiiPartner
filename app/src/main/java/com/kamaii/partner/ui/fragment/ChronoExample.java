package com.kamaii.partner.ui.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.LinearLayout;
import android.widget.Toast;

public class ChronoExample extends Activity {
    Chronometer mChronometer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);

        mChronometer = new Chronometer(this);

        // Set the initial value
        mChronometer.setText("00:10");
        layout.addView(mChronometer);

        Button startButton = new Button(this);
        startButton.setText("Start");
        startButton.setOnClickListener(mStartListener);
        layout.addView(startButton);

        Button stopButton = new Button(this);
        stopButton.setText("Stop");
        stopButton.setOnClickListener(mStopListener);
        layout.addView(stopButton);

        Button resetButton = new Button(this);
        resetButton.setText("Reset");
        resetButton.setOnClickListener(mResetListener);
        layout.addView(resetButton);

        setContentView(layout);
    }

    private void showElapsedTime() {
        long elapsedMillis = SystemClock.elapsedRealtime() - mChronometer.getBase();
        Toast.makeText(ChronoExample.this, "Elapsed milliseconds: " + elapsedMillis,
                Toast.LENGTH_SHORT).show();
    }

    View.OnClickListener mStartListener = new View.OnClickListener() {
        public void onClick(View v) {
            int stoppedMilliseconds = 0;

            String chronoText = mChronometer.getText().toString();
            String array[] = chronoText.split(":");
            if (array.length == 2) {
                stoppedMilliseconds = Integer.parseInt(array[0]) * 60 * 1000
                        + Integer.parseInt(array[1]) * 1000;
            } else if (array.length == 3) {
                stoppedMilliseconds = Integer.parseInt(array[0]) * 60 * 60 * 1000
                        + Integer.parseInt(array[1]) * 60 * 1000
                        + Integer.parseInt(array[2]) * 1000;
            }

            mChronometer.setBase(SystemClock.elapsedRealtime() - stoppedMilliseconds);
            mChronometer.start();
        }
    };

    View.OnClickListener mStopListener = new View.OnClickListener() {
        public void onClick(View v) {
            mChronometer.stop();
            showElapsedTime();
        }
    };

    View.OnClickListener mResetListener = new View.OnClickListener() {
        public void onClick(View v) {
            mChronometer.setBase(SystemClock.elapsedRealtime());
            showElapsedTime();
        }
    };
}