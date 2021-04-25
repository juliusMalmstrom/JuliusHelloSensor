package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.view.animation.RotateAnimation;
import android.view.animation.Animation;
import android.media.MediaPlayer;

public class RealCompass extends AppCompatActivity implements SensorEventListener{

    private SensorManager sensorManager;
    private Sensor accMeterSensor;
    private Sensor magMeterSenor;
    TextView DegreeTV;
    private ImageView compassImage;
    private TextView heading;
    //compass data
    private float[] lastAccelerometer = new float[3];
    private float[] lastMagnetometer = new float[3];
    private float[] rotationMatrix = new float[9];
    private float[] orientation = new float[3];
    private int printDeg;
    static final float ALPHA = 0.25f;

    boolean isLastAccArrayCopied = false;
    boolean isLastMagArrayCopied = false;

    long lastUpdated = 0;
    float currentDeg = 0f;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_real_compass);

        //image and text
        heading = (TextView) findViewById(R.id.Direction);
        compassImage = (ImageView) findViewById(R.id.Compass_image);
        DegreeTV = (TextView) findViewById(R.id.DegreeTv);
        //sensors
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        accMeterSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        magMeterSenor = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);

    }

    protected void onPause(){
        super.onPause();

        sensorManager.unregisterListener(this, accMeterSensor);
        sensorManager.unregisterListener(this, magMeterSenor);

    }

    @Override
    protected void onResume() {
        super.onResume();
        // code for system's orientation sensor registered listeners
        sensorManager.registerListener(this, accMeterSensor, sensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(this, magMeterSenor, sensorManager.SENSOR_DELAY_NORMAL);
    }

    public void onSensorChanged(SensorEvent event) {

        if(event.sensor == accMeterSensor){
            System.arraycopy(event.values,0, lastAccelerometer, 0, event.values.length  );
            lastAccelerometer = lowPass(event.values.clone(), lastAccelerometer);
            isLastAccArrayCopied = true;
        }else if(event.sensor == magMeterSenor){
            System.arraycopy(event.values,0, lastMagnetometer, 0, event.values.length  );
            lastMagnetometer = lowPass(event.values.clone(), lastMagnetometer);
            isLastMagArrayCopied = true;
        }if(isLastMagArrayCopied && isLastAccArrayCopied && System.currentTimeMillis()-lastUpdated>250){
            sensorManager.getRotationMatrix(rotationMatrix, null, lastAccelerometer, lastMagnetometer); 
            sensorManager.getOrientation(rotationMatrix, orientation);

            float aziRadians = orientation[0];
            float aziDeg = (float) Math.toDegrees(aziRadians);

            RotateAnimation rotateAnimation = new RotateAnimation(currentDeg, -aziDeg, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
            rotateAnimation.setDuration(250);
            rotateAnimation.setFillAfter(true);
            compassImage.startAnimation(rotateAnimation);

            currentDeg = -aziDeg;
            lastUpdated = System.currentTimeMillis();

            printDeg = (((int)currentDeg-360)%360)*-1;
            DegreeTV.setText(printDeg + " degrees");
            heading.setText(headingText());
        }
    }

    public String headingText(){
        String direction = "";
        if (printDeg >= 350 || printDeg <= 10) direction = "N";
        if (printDeg < 350 && printDeg > 280) direction = "NW";
        if (printDeg <= 280 && printDeg > 260) direction = "W";
        if (printDeg <= 260 && printDeg > 190) direction = "SW";
        if (printDeg <= 190 && printDeg > 170) direction = "S";
        if (printDeg <= 170 && printDeg > 100) direction = "SE";
        if (printDeg<= 100 && printDeg > 80) direction = "E";
        if (printDeg <= 80 && printDeg > 10) direction = "NE";
        return direction;
    }

    //my LowPass filter, makes the
    protected float[] lowPass( float[] input, float[] output ) {
        if ( output == null ) return input;
        for ( int i=0; i<input.length; i++ ) {
            output[i] = output[i] + ALPHA * (input[i] - output[i]);
        }
        return output;
    }
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // not in use
    }
}