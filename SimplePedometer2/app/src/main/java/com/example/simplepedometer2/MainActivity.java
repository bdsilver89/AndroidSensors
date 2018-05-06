package com.example.simplepedometer2;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity implements SensorEventListener, StepListener {
    private static final double inchesPerStep = 26.4;
    private TextView TvSteps;
    private TextView TvTimer;
    private EditText EtDistance;
    private StepDetector simpleStepDetector;
    private SensorManager sensorManager;
    private Sensor accel;

    private Button BtnStart;
    private Button BtnStop;

    private CountDownTimer timer;

    private int numSteps;
    private double numMiles;
    private double calibrationSpeed = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Get an instance of the SensorManager
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        accel = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        simpleStepDetector = new StepDetector();
        simpleStepDetector.registerListener(this);

        TvSteps = (TextView) findViewById(R.id.tv_steps);
        TvTimer = (TextView) findViewById(R.id.tv_timer);
        EtDistance = (EditText) findViewById(R.id.et_distance);
        BtnStart = (Button) findViewById(R.id.btn_start);
        BtnStop = (Button) findViewById(R.id.btn_stop);
        BtnStop.setVisibility(View.INVISIBLE);

        BtnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                TvTimer.setText("");
                BtnStart.setVisibility(View.INVISIBLE);
                BtnStop.setVisibility(View.VISIBLE);
                numSteps = 0;
                sensorManager.registerListener(MainActivity.this, accel, SensorManager.SENSOR_DELAY_FASTEST);
                TvTimer.setTextSize(60);
                startTimer();
            }
        });

        BtnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                stopCounting(false);
            }
        });
    }


    private void startTimer() {
        timer = new CountDownTimer(30000,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                TvTimer.setText(String.valueOf(millisUntilFinished/1000));
            }

            @Override
            public void onFinish() {
                calibrationSpeed = numMiles/30.0;
                double userDistance = Double.parseDouble(EtDistance.getText().toString());
                double userTime = userDistance / calibrationSpeed;
                int hours = (int) userTime / 360;
                int minutes = (int) userTime /60;
                int seconds = (int) userTime % 60;
                EtDistance.setVisibility(View.INVISIBLE);
                TvTimer.setText("Time to walk that distance: " +  hours + " hours, " + minutes + " minutes, " + seconds + " seconds");

                stopCounting(true);
            }
        }.start();
        EtDistance.setVisibility(View.VISIBLE);
    }


    private void stopCounting(boolean finished) {
        sensorManager.unregisterListener(MainActivity.this);
        if(!finished) {
            TvTimer.setText("");
            timer.cancel();

        }

        TvTimer.setTextSize(15);
        BtnStart.setVisibility(View.VISIBLE);
        BtnStop.setVisibility(View.INVISIBLE);
        TvSteps.setText("");
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            simpleStepDetector.updateAccel(
                    event.timestamp, event.values[0], event.values[1], event.values[2]);
        }
    }

    @Override
    public void step(long timeNs) {
        numMiles += (double)  inchesPerStep / 63360.0;
        String miles = String.format("%.2f", numMiles);
        numSteps++;
        TvSteps.setText("Steps: " + numSteps + ", Distance: " + miles );
    }

}