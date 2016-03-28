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
import android.graphics.Color;
import android.media.AudioManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.SoundEffectConstants;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

/**
 * Created by Gregory on 1/10/16.
 */
public class MainMenuActivity extends AppCompatActivity {
    public static final String DEFAULT = "NONE";

    LinearLayout layout;
    ImageView    logo;
    Button       new_game;
    ImageButton  info,author,settings;
    boolean soundBoolean;
    AudioManager audioManager;

    // return and reload sounds and lang
    @Override
    protected void onResume() {
        super.onResume();

        loadSettings();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        /* no title bar */
        //this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        /* initialize variables */
        layout   = (LinearLayout) findViewById(R.id.main_layout);
        logo     = (ImageView)    findViewById(R.id.logo_main_layout);
        new_game = (Button)       findViewById(R.id.button_Newgame);
        info     = (ImageButton)  findViewById(R.id.info);
        author   = (ImageButton)  findViewById(R.id.author);
        settings = (ImageButton)  findViewById(R.id.settings);

        new_game.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                start_game(v);
            }
        });

        info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showInfo(v);
            }
        });

        author.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAuthor(v);
            }
        });

        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSettings(v);
            }
        });

        loadSettings();
        logoImageScale();

        ((Button)findViewById(R.id.button_Newgame)).getBackground().setAlpha(150);
    }

    private void logoImageScale() {
        Display display = getWindowManager().getDefaultDisplay();
        int width = display.getWidth();
        int height = display.getHeight();
        //Toast.makeText(getApplicationContext(), Integer.toString(width) + " : " + Integer.toString(height),
         //       Toast.LENGTH_LONG).show();

        // if mobile or tablet
        //if()
        int w = (int) width/2;
        logo.getLayoutParams().height = w;
        logo.getLayoutParams().width  = w;

        logo.setTranslationX((int) (w - w / 2));
        logo.setTranslationY((int) ((height / 2) - w / 2));

        logo.requestLayout();

        int w1=0,h1=0;
        int density= getResources().getDisplayMetrics().densityDpi;
        switch(density)
        {
            case DisplayMetrics.DENSITY_LOW:
                //Toast.makeText(this, "LDPI", Toast.LENGTH_SHORT).show();
                info.setPadding(5,0,0,0);
                info.requestLayout();
                setIconsSize(0.5);
                break;
            case DisplayMetrics.DENSITY_MEDIUM:
                //Toast.makeText(this, "MDPI", Toast.LENGTH_SHORT).show();
                w1 = (int) (info.getWidth()*0.7); h1 = (int) (info.getHeight()*0.7);
                info.setPadding(5,0,0,0);
                info.requestLayout();
                setIconsSize(0.6);
                break;
            case DisplayMetrics.DENSITY_HIGH:
                //Toast.makeText(this, "HDPI", Toast.LENGTH_SHORT).show();
                setIconsSize(0.8);
                break;
            case DisplayMetrics.DENSITY_XHIGH:
                //Toast.makeText(this, "XHDPI", Toast.LENGTH_SHORT).show();
                setIconsSize(1.0);
                break;
        }

        if( density >= DisplayMetrics.DENSITY_HIGH ) {
            //int logo_size = (int) width/
            int padding = 100;
            int dy = (int) ((height/2) - w/2) - padding;
            TranslateAnimation anim = new TranslateAnimation(0,0,0,-dy);
            anim.setInterpolator(new LinearInterpolator());
            anim.setRepeatCount(Animation.ABSOLUTE);
            anim.setFillAfter(true);
            anim.setDuration(1200);

            //} else {

            anim.setAnimationListener(new TranslateAnimation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                    //logo.startAnimation(anim);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {


                    //logo.requestLayout();

                    //logo.setAnimation(null);
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    //logo.requestLayout();
                    //logo.setAnimation(null);
                    showOtherButtons();
                }
            });
            logo.startAnimation(anim);

        } else {
            int padding = 20;
            int dy = (int) ((height/2) - w/2) - padding;

            logo.setTranslationY((int) ((height / 2) - w / 2 - dy));

            logo.requestLayout();

            //new_game.setY(40+w);
            //new_game.requestLayout();

            new_game.setVisibility(View.VISIBLE);
        }

    }

    private void setIconsSize(double _s) {
        ImageView[] arrIcon = {info, author, settings};
        for(int i=0;i<3;i++) {
            //int w1 = (int) (info.getLayoutParams().width*_s);
            int h1 = (int) (arrIcon[i].getLayoutParams().height*_s);

            //arrIcon[i].getLayoutParams().width  = w1;
            arrIcon[i].getLayoutParams().height = h1;

            arrIcon[i].requestLayout();
        }

    }

    private void showOtherButtons() {
        Display display = getWindowManager().getDefaultDisplay();
        int width = display.getWidth();

        //new_game.setX(width / 2);
        new_game.setY(100 + width / 2 + 100);

        new_game.setVisibility(View.VISIBLE);

        final Animation anim = AnimationUtils.loadAnimation(this,R.anim.settings_rotate);
        settings.startAnimation(anim);
    }

    /* start game */
    public void start_game(View view) {
        if(soundBoolean) { audioManager.playSoundEffect(SoundEffectConstants.CLICK, 0.8F); }
        Intent intent = new Intent(MainMenuActivity.this, GameActivity.class);
        startActivity(intent);
    }

    /* show author */
    public void showAuthor(View view) {
        if(soundBoolean) { audioManager.playSoundEffect(SoundEffectConstants.CLICK, 0.8F); }
        Log.d("Author", "showAuthor");
        ((ImageButton)findViewById(R.id.author)).setColorFilter(Color.rgb(58,171,75));
        Intent intent2 = new Intent(MainMenuActivity.this, Author.class);
        startActivity(intent2);
    }

    /* show info */
    public void showInfo(View view) {
        if(soundBoolean) { audioManager.playSoundEffect(SoundEffectConstants.CLICK, 0.8F); }
        ((ImageButton)findViewById(R.id.info)).setColorFilter(Color.rgb(58,171,75));
        Intent intent2 = new Intent(MainMenuActivity.this, Info.class);
        startActivity(intent2);
    }

    /* show settings */
    public void showSettings(View view) {
        if(soundBoolean) { audioManager.playSoundEffect(SoundEffectConstants.CLICK, 0.8F); }
        ((ImageButton)findViewById(R.id.settings)).setColorFilter(Color.rgb(58,171,75));
        Intent intent2 = new Intent(MainMenuActivity.this, Settings.class);
        startActivity(intent2);
    }


    //loadSettings
    private void loadSettings() {
        ((ImageButton)findViewById(R.id.author)).setColorFilter(Color.rgb(10,10,10));
        ((ImageButton)findViewById(R.id.info)).setColorFilter(Color.rgb(10,10,10));
        ((ImageButton)findViewById(R.id.settings)).setColorFilter(Color.rgb(10,10,10));


        SharedPreferences shared = getSharedPreferences("myData", Context.MODE_PRIVATE);
        String sound   = shared.getString("sound", DEFAULT);
        String lang    = shared.getString("lang",DEFAULT);

        // sounds - true (ON), false (OFF)
        if(sound.equals(DEFAULT) || sound.equals("true")) {
            soundBoolean=true;
            Log.d("SOUNDS","TRUE");
        } else {
            soundBoolean=false;
            Log.d("SOUNDS","FALSE");
        }

        //lang - true (ENGLISH), false (RUSSIAN)
        if(lang.equals(DEFAULT) || lang.equals("true")) {
            new_game.setText(R.string.action_newgame);
            Log.d("LANG","TRUE - ENG");
        } else {
            new_game.setText(R.string.raction_newgame);
            Log.d("LANG","FALSE - RUS");
        }
    }

}
