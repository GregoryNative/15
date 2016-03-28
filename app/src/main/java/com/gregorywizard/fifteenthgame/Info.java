/*
 * 15 Puzzle game.
 * Copyright (C) 2016 Gregory Wizard
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
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Gregory on 1/11/16.
 */
public class Info extends AppCompatActivity {
    public static final String DEFAULT = "NONE";

    TextView info;
    TextView infoText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.info_layout);

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

        info = (TextView) findViewById(R.id.textInfo);
        infoText = (TextView) findViewById(R.id.textAbout);
        loadSettings();
    }

    private void loadSettings() {
        SharedPreferences shared = getSharedPreferences("myData", Context.MODE_PRIVATE);
        String lang    = shared.getString("lang",DEFAULT);


        //lang - true (ENGLISH), false (RUSSIAN)
        if(lang.equals(DEFAULT) || lang.equals("true")) {
            reloadScreen(true);
        } else {
            reloadScreen(false);
        }
    }

    public void reloadScreen(boolean _b) {
        if(_b) {
            info.setText(R.string.info);
            infoText.setText(R.string.infoabout);
        } else {
            info.setText(R.string.rinfo);
            infoText.setText(R.string.rinfoabout);
        }
    }
}
