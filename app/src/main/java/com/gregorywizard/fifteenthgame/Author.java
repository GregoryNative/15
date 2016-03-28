/*
 * 15-Puzzle game.
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
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by Gregory on 1/11/16.
 */
public class Author extends AppCompatActivity {
    public static final String DEFAULT = "NONE";
    TextView title;
    TextView  text;
    Button githubLoad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.author_layout);

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

        title = (TextView) findViewById(R.id.title);
        text  = (TextView) findViewById(R.id.text);
        githubLoad = (Button) findViewById(R.id.openBrowser);
        githubLoad.setText("GitHub");

        githubLoad.setOnClickListener(new buttonDefaultListener());

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
            title.setText(R.string.about);
            text.setText(R.string.abouttext);
        } else {
            title.setText(R.string.rabout);
            text.setText(R.string.rabouttext);
        }
    }

    private class buttonDefaultListener implements View.OnClickListener {

        public void onClick(View _v) {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/GregoryWizard/15"));
            startActivity(browserIntent);

        }
    }
}
