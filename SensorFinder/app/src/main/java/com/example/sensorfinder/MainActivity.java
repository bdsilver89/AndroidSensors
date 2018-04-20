package com.example.sensorfinder;

import android.hardware.Sensor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        boolean sensorPresent;


        List<Sensor> sensorList = deviceSensorManager.getSensorList(Sensor.TYPE_ACCELEROMETER);
        if (sensorList.size() > 0) {
            sensorPresent = true;
            sensor = sensorList.get(0);
        }
        else
            sensorPresent = false;
        /* Set the face TextView to display sensor presence */
        face = (TextView) findViewById(R.id.face);
        if (sensorPresent)
            face.setText(“Sensor present!”)
else
        face.setText(“Sensor absent.”);
    }
}
