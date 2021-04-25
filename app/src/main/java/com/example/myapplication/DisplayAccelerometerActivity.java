package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.TextView;
import android.media.MediaPlayer;

public class DisplayAccelerometerActivity extends AppCompatActivity implements SensorEventListener {
    private SensorManager sensorManager;
    private Sensor sensor;
    private TextView textView;
    private MediaPlayer mp = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_accelerometer);

        textView = findViewById(R.id.textView2);
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(this, sensor, sensorManager.SENSOR_DELAY_NORMAL);
        mp = MediaPlayer.create(this, R.raw.incubus);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        songPlayer(event);
        textView.setText(event.values[0] + "\n" + event.values[1] + "\n" + event.values[2]);
    }

    public void songPlayer(SensorEvent event){
        if(event.values[2]<0){
            mp.start();
        }else mp.pause();
    }
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}