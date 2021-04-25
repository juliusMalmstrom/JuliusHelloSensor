package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    public void displayAccMeter(View view) {
        Intent intent = new Intent(this, DisplayAccelerometerActivity.class);
        startActivity(intent);
    }

    public void displayCompass(View view){
        Intent intent = new Intent(this, RealCompass.class);
        startActivity(intent);
    }
}