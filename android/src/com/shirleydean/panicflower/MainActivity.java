package com.shirleydean.panicflower;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

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
public class MainActivity extends AppCompatActivity {

    static final String KEY_NAME = "KEY_NAME";
    static final String KEY_OCCASION = "KEY_OCCASION";
    RadioGroup radioGroup;
    SharedPreferences sharedPref;
    String name, occasion;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);


        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        ((RadioButton) findViewById(R.id.radioButton_normal)).setChecked(true);
		
		findViewById(R.id.floatingActionButton).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                switch (radioGroup.getCheckedRadioButtonId()) {
                    case R.id.radioButton_normal:
                        intent = new Intent(MainActivity.this, PanicFlowerActivity.class);
                        intent.putExtra(PanicFlowerActivity.EXTRA_NAME, name);
                        intent.putExtra(PanicFlowerActivity.EXTRA_OCCASION, occasion);
                        startActivity(intent);
                        break;

                    case R.id.radioButton_cardBoard:
                        intent = new Intent(MainActivity.this, PanicFlowerCardBoard.class);
                        startActivity(intent);
                        break;
                }
            }
        });

		findViewById(R.id.imageButton_settings).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
                showSettingsDialog();
			}
		});

        sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        name = getString(sharedPref, KEY_NAME);
        occasion = getString(sharedPref, KEY_OCCASION);
	}

    String getString(SharedPreferences sharedPreferences, String KEY){
        return sharedPreferences.getString(KEY, "");
    }

    void setString(SharedPreferences sharedPreferences, String KEY, String VALUE){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY, VALUE);
        editor.commit();
    }

    private void showSettingsDialog() {
        View view = LayoutInflater.from(this).inflate(R.layout.layout_settings, null);

        final EditText inputName = (EditText) view.findViewById(R.id.editTextName);
	    final EditText inputOccasion = (EditText) view.findViewById(R.id.editTextOccasion);
        inputName.setText(name);
        inputOccasion.setText(occasion);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(view)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        String iName = inputName.getText().toString();
                        String iOccasion = inputOccasion.getText().toString();

                        setString(sharedPref, KEY_NAME, iName);
                        setString(sharedPref, KEY_OCCASION, iOccasion);

                        name = iName;
                        occasion = iOccasion;
                    }
                });

        builder.create().show();

    }

}
