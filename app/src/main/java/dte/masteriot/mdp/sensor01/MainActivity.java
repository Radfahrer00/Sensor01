package dte.masteriot.mdp.sensor01;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.os.Vibrator;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    Button bLight;
    TextView tvSensorValue;
    private SensorManager sensorManager;
    private Sensor lightSensor;
    boolean lightSensorIsActive;

    Button bAccelerometer;
    TextView tvAccelSensorValue;
    private Sensor accelSensor;
    boolean accelSensorIsActive;

    Button bStep;
    TextView tvStepSensorValue;
    private Sensor stepSensor;
    boolean stepSensorIsActive;
    private int numberOfSteps;
    Vibrator vibrator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lightSensorIsActive = false;
        accelSensorIsActive = false;
        stepSensorIsActive = false;

        // Get the references to the UI:
        bLight = findViewById(R.id.bLight); // button to start/stop light sensor's readings
        tvSensorValue = findViewById(R.id.lightMeasurement); // sensor's values

        bAccelerometer = findViewById(R.id.bAccelerometer); // button to start/stop accelerometer sensor's readings
        tvAccelSensorValue = findViewById(R.id.xAccelMeasurement);

        bStep = findViewById(R.id.bStep);
        tvStepSensorValue = findViewById(R.id.xStepMeasurement);

        // Get the reference to the sensor manager and the sensor:
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        // Obtain the reference to the default light sensor of the device:
        lightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        // Obtain the reference to the default accelerometer sensor of the device:
        accelSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        // Obtain the reference to the default step sensor of the device:
        stepSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR);
        vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);


        // Listener for the light button:
        bLight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (lightSensorIsActive) {
                    // unregister listener and make the appropriate changes in the UI:
                    sensorManager.unregisterListener(MainActivity.this, lightSensor);
                    bLight.setText(R.string.light_sensor_off);
                    Drawable button_off =
                            ResourcesCompat.getDrawable(getResources(), R.drawable.round_button_off, null);
                    bLight.setBackground(button_off);
                    tvSensorValue.setText("Light sensor is OFF");
                    lightSensorIsActive = false;
                } else {
                    // register listener and make the appropriate changes in the UI:
                    sensorManager.registerListener(MainActivity.this, lightSensor, SensorManager.SENSOR_DELAY_NORMAL);
                    bLight.setText(R.string.light_sensor_on);
                    Drawable button_on =
                            ResourcesCompat.getDrawable(getResources(), R.drawable.round_button_on, null);
                    bLight.setBackground(button_on);
                    tvSensorValue.setText("Waiting for first light sensor value");
                    lightSensorIsActive = true;
                    // Storing sensor state before configuration change
                    SharedPreferences sharedPreferences = getSharedPreferences("SensorPreferences", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean("isSensorEnabled", lightSensorIsActive);
                    editor.apply();
                }
            }
        });

        // Listener for the accelerometer button:
        bAccelerometer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (accelSensorIsActive) {
                    // unregister listener and make the appropriate changes in the UI:
                    sensorManager.unregisterListener(MainActivity.this, accelSensor);
                    bAccelerometer.setText(R.string.accel_sensor_off);
                    Drawable button_off =
                            ResourcesCompat.getDrawable(getResources(), R.drawable.round_button_off, null);
                    bAccelerometer.setBackground(button_off);
                    tvAccelSensorValue.setText("Accel sensor is OFF");
                    accelSensorIsActive = false;
                } else {
                    // register listener and make the appropriate changes in the UI:
                    sensorManager.registerListener(MainActivity.this, accelSensor, SensorManager.SENSOR_DELAY_NORMAL);
                    bAccelerometer.setText(R.string.accel_sensor_on);
                    Drawable button_on =
                            ResourcesCompat.getDrawable(getResources(), R.drawable.round_button_on, null);
                    bAccelerometer.setBackground(button_on);
                    tvAccelSensorValue.setText("Waiting for first accel sensor value");
                    accelSensorIsActive = true;
                }
            }
        });

        // Listener for the step button:
        bStep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (stepSensorIsActive) {
                    // unregister listener and make the appropriate changes in the UI:
                    sensorManager.unregisterListener(MainActivity.this, stepSensor);
                    bStep.setText(R.string.step_sensor_off);
                    Drawable button_off =
                            ResourcesCompat.getDrawable(getResources(), R.drawable.round_button_off, null);
                    bStep.setBackground(button_off);
                    tvStepSensorValue.setText("Step detector sensor is OFF");
                    stepSensorIsActive = false;
                    numberOfSteps = 0;
                } else {
                    // register listener and make the appropriate changes in the UI:
                    sensorManager.registerListener(MainActivity.this, stepSensor, SensorManager.SENSOR_DELAY_NORMAL);
                    bStep.setText(R.string.step_sensor_on);
                    Drawable button_on =
                            ResourcesCompat.getDrawable(getResources(), R.drawable.round_button_on, null);
                    bStep.setBackground(button_on);
                    tvStepSensorValue.setText("Waiting for first step detecor sensor value");
                    stepSensorIsActive = true;
                }
            }
        });
    }

    // Methods related to the SensorEventListener interface:

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {

        int sensorType = sensorEvent.sensor.getType();

        // Show the sensor's value in the UI:
        if (sensorType == Sensor.TYPE_LIGHT) {
            tvSensorValue.setText(Float.toString(sensorEvent.values[0]));
        }
        else if (sensorType == Sensor.TYPE_ACCELEROMETER) {
            tvAccelSensorValue.setText("X value: " + sensorEvent.values[0] + "\n" +
            "Y value: " + sensorEvent.values[1] + "\n" + "Z value: " + sensorEvent.values[2]);
        }
        else if (sensorType == Sensor.TYPE_STEP_DETECTOR) {
            numberOfSteps++;
            tvStepSensorValue.setText(Integer.toString(numberOfSteps));
            if (numberOfSteps % 20 == 0) {
                vibrator.vibrate(1000);
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {
        // In this app we do nothing if sensor's accuracy changes
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // React to orientation changes here
        SharedPreferences sharedPreferences = getSharedPreferences("SensorPreferences", Context.MODE_PRIVATE);
        boolean isSensorEnabled = sharedPreferences.getBoolean("isSensorEnabled", false);

        if (isSensorEnabled) {
            // Re-enable the sensor
        }

    }

}