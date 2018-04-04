package com.example.compasstest;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private static SensorManager sensorService;
    private MyCompassView compassView;
    private Sensor sensor;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        compassView = new MyCompassView(this);
        setContentView(compassView);

        sensorService = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
        sensor = sensorService.getDefaultSensor(Sensor.TYPE_ORIENTATION);
        if (sensor != null) {
            sensorService.registerListener(mySensorEventListener, sensor,
                    SensorManager.SENSOR_DELAY_NORMAL);
            Log.i("Compass MainActivity", "Registered for ORIENTATION Sensor");
        }
        else {
            Log.e("Compass MainActivity", "Registered for ORIENTATION Sensor");
            Toast.makeText(this, "ORTIENTATION Sensor not found",
                    Toast.LENGTH_LONG).show();
            finish();
        }
    }

    private SensorEventListener mySensorEventListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {
            float azimuth = event.values[0];
            compassView.updateData(azimuth);
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(sensor != null) {
            sensorService.unregisterListener(mySensorEventListener);
        }
    }
}
