/*
 * 15 Puzzle game.
 * Copyright (C) 2016 Gregory Galushka
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along
 * with this program; if not, write to the Free Software Foundation, Inc.,
 * 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 */


package com.gregorywizard.fifteenthgame;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SwitchCompat;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Gregory on 1/11/16.
 */
public class Settings extends AppCompatActivity {
    public static final String DEFAULT = "NONE";
    SwitchCompat sounds;
    SwitchCompat languages;
    Button buttonDefault;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.settings_layout);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        double scale_w = 0.7; double scale_h = 0.7;
        int density= getResources().getDisplayMetrics().densityDpi;
        switch(density)
        {
            case DisplayMetrics.DENSITY_LOW:
                scale_w = 0.6;
                break;
            case DisplayMetrics.DENSITY_MEDIUM:
                scale_w = 0.6;
                break;
            case DisplayMetrics.DENSITY_HIGH:
                scale_w = 0.8;
                break;
            case DisplayMetrics.DENSITY_XHIGH:
                scale_w = 0.7;
                break;
        }


        getWindow().setLayout((int) (dm.widthPixels * scale_w), (int) (dm.heightPixels * scale_h));

        sounds = (SwitchCompat) findViewById(R.id.switchSound);
        languages  = (SwitchCompat) findViewById(R.id.switchLang);
        buttonDefault = (Button) findViewById(R.id.buttonDefault);

        loadSettings();

        sounds.setShowText(true);
        languages.setShowText(true);

        sounds.setOnCheckedChangeListener(new switchCompatListener());
        languages.setOnCheckedChangeListener(new switchCompatLangListener());
        buttonDefault.setOnClickListener(new buttonDefaultListener());
    }

    public class switchCompatListener implements CompoundButton.OnCheckedChangeListener
    {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if(buttonView == sounds) {
                if (isChecked) {
                    updateProfile("sound", "true");
                    Log.d("SOUNDS+", "TRUE");
                    return;
                } else {
                    updateProfile("sound", "false");
                    Log.d("SOUNDS+", "FALSE");
                    return;
                }
            }
        }

    }

    public class switchCompatLangListener implements CompoundButton.OnCheckedChangeListener
    {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if(buttonView == languages) {
                if(isChecked) {
                    updateProfile("lang", "true");
                    Log.d("LANG+", "TRUE - ENG");
                    reloadScreen(true);
                    return;
                } else {
                    updateProfile("lang", "false");
                    Log.d("LANG+", "FALSE - RUS");
                    reloadScreen(false);
                    return;
                }
            }
        }

    }

    private void loadSettings() {
        SharedPreferences shared = getSharedPreferences("myData", Context.MODE_PRIVATE);
        String sound   = shared.getString("sound", DEFAULT);
        String lang    = shared.getString("lang",DEFAULT);

        // sounds - true (ON), false (OFF)
        if(sound.equals(DEFAULT) || sound.equals("true")) {

            sounds.setChecked(true);
            Log.d("SOUNDS", "TRUE");
        } else {

            sounds.setChecked(false);
            Log.d("SOUNDS","FALSE");
        }

        //lang - true (ENGLISH), false (RUSSIAN)
        if(lang.equals(DEFAULT) || lang.equals("true")) {

            languages.setChecked(true);
            Log.d("LANG", "TRUE - ENG");
            reloadScreen(true);

        } else {

            languages.setChecked(false);
            Log.d("LANG", "FALSE - RUS");
            reloadScreen(false);
        }
    }

    private void updateProfile(String key,String value) {
        SharedPreferences shared = getSharedPreferences("myData", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = shared.edit();
        editor.putString(key, value);
        editor.commit();
    }

    private class buttonDefaultListener implements View.OnClickListener {

        public void onClick(View _v) {
            updateProfile("sound", "true");
            Log.d("SOUNDS+", "TRUE");
            sounds.setChecked(true);

            updateProfile("lang", "true");
            Log.d("LANG+", "TRUE");
            languages.setChecked(true);

            updateProfile("level",     DEFAULT);
            updateProfile("xp",        DEFAULT);
            updateProfile("best_time", DEFAULT);
            updateProfile("count_g",   DEFAULT);
            updateProfile("music",     DEFAULT);
            updateProfile("sound",     DEFAULT);
            updateProfile("theme",     DEFAULT);

            reloadScreen(true);
        }
    }

    public void reloadScreen(boolean _b) {
        TextView t1 = (TextView) findViewById(R.id.textSettings);
        TextView t2 = (TextView) findViewById(R.id.textSounds);
        TextView t3 = (TextView) findViewById(R.id.textLanguage);

        if(_b) {
            t1.setText(R.string.settings);
            t2.setText(R.string.sounds);
            t3.setText(R.string.language);
            buttonDefault.setText(R.string.i_default);
        } else {
            t1.setText(R.string.rsettings);
            t2.setText(R.string.rsounds);
            t3.setText(R.string.rlanguage);
            buttonDefault.setText(R.string.ri_default);
        }
    }
}
