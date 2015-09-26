package com.shirleydean.panicflower;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.math.Vector3;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.DisplayMetrics;

/*******************************************************************************
 * Copyright 2015 Dean Alvero (deanalvero@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
public class PanicFlowerActivity extends AndroidApplication implements SensorEventListener {

    public static final String EXTRA_NAME = "EXTRA_NAME";
    public static final String EXTRA_OCCASION = "EXTRA_OCCASION";

	DisplayMetrics metrics;
	PFGame game;
	Vector3 pointCamera;

	SensorManager mSensorManager;
	Sensor accelerometer;
	Sensor magnetometer;
	float[] mGravity;
	float[] mGeomagnetic;

	float azimuth;
	float azimuthAngle;
	float pitch;
	float roll;

	public void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		
		metrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metrics);

		pointCamera = new Vector3(0,0,0);
		game = new PFGame(){
			@Override
			public void render() {
				super.render();

				cam.position.set(-getZFromSpherical(), getYFromSpherical(), getXFromSpherical());
				cam.lookAt(0,0,0);
				cam.near = 0.1f;
				cam.far = 300f;
				cam.update();

				if (!loading){
					setRotationModelInstance(azimuthAngle);
				}
			}
		};
		
		game.width = metrics.widthPixels;
		game.height = metrics.heightPixels;
		
		initialize(game);

		mSensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
		accelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		magnetometer = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);


        String name = getString(R.string.default_name);
        String occasion = getString(R.string.default_occasion);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null){
            if (bundle.containsKey(EXTRA_NAME)){
                name = bundle.getString(EXTRA_NAME);
            }
            if (bundle.containsKey(EXTRA_OCCASION)){
                occasion = bundle.getString(EXTRA_OCCASION);
            }
        }

        game.setUpperText(name);
        game.setLowerText(occasion);
	}

	public float getXFromSpherical(){
		//	x = rho * sin (phi) * cos (theta)
		return (float) (2 * Math.sin(roll) * Math.cos(pitch));
	}

	public float getYFromSpherical(){
		//	y = rho * sin (phi) * sin (theta)
		return (float) (2 * Math.sin(roll) * Math.sin(pitch));
	}

	public float getZFromSpherical(){
		//	z = rho * cos (phi)
		return (float) (2 * Math.cos(roll));
	}
	
	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER)
			mGravity = event.values.clone();
		if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD)
			mGeomagnetic = event.values.clone();
		if (mGravity != null && mGeomagnetic != null) {
			float R[] = new float[9];
			float I[] = new float[9];
			boolean success = SensorManager.getRotationMatrix(R, I, mGravity, mGeomagnetic);
			if (success) {
				float orientation[] = new float[3];
				SensorManager.getOrientation(R, orientation);
				azimuthAngle = ( (3.14159f + orientation[0]) / 3.14159f * 180);
				azimuth = 3.14159f + orientation[0];
				pitch = (3.14159f/2) + orientation[1];
				roll = (3.14159f/2) + orientation[2];
			}
		}
	}
	
	protected void onResume() {
		super.onResume();
		mSensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_UI);
		mSensorManager.registerListener(this, magnetometer, SensorManager.SENSOR_DELAY_UI);
	}

	protected void onPause() {
		super.onPause();
		mSensorManager.unregisterListener(this);
	}

}

