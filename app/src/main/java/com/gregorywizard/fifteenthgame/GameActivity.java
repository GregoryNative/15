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

import android.animation.ObjectAnimator;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ShapeDrawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.Display;
import android.view.SoundEffectConstants;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.DecelerateInterpolator;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Gregory on 8/16/15.
 */

// refresh main activity upon close of dialog box
//intent refresh = new intent(this, clsmainuiactivity.class);
//        startactivity(refresh);
//        this.finish(); //

public class GameActivity extends AppCompatActivity {
    public static final String DEFAULT    = "NONE";
    Random rand = new Random();

    GTimer gTimer;
    long startTime = 0;

    // ---- Layouts ----
    LinearLayout layoutButtons, gameLayout;

    // ---- States ----
    boolean firstTimeButtonPressed=false;

    Button level;
    ProgressBar xp;
    TextView bestTime;
    TextView currTime;
    TextView countGame;

    String levels;
    String xps;
    String best_times;
    String count_gs;
    String musics;
    String sounds;
    String themes;
    int    xpForNextLevel;
    int    youGainXp = 0;


    // ---- Static Variables ----
    private static final double MIN_TIME  = 1;
    private static final double MAX_TIME  = 180;
    private static final int MIN_COUNT    = 1;
    private static final int MAX_COUNT    = 100;

    private static final int MAX_LVL      = 99;
    private static final int MIN_XP       = 10;
    private static final int MAX_XP       = 1000;

    private static final double COEF = 1.06; // coef of increasing xp for next lvl
    // --------------------------

    int COUNT_CLICKS = 0;
    Boolean soundBoolean = false;
    Boolean lang_eng = true;

    Button buts[] = new Button[16];
    List<String> strNum = new ArrayList<String>();
    String str[]  = {"1" ,"2" ,"3" ,"4" ,
                     "5" ,"6" ,"7" ,"8" ,
                     "9" ,"10","11","12",
                     "13","14","15","Null"
    };
    final int[] rIds = { R.id.button1,  R.id.button2,  R.id.button3,  R.id.button4,
            R.id.button5,  R.id.button6,  R.id.button7,  R.id.button8,
            R.id.button9,  R.id.button10, R.id.button11, R.id.button12,
            R.id.button13, R.id.button14, R.id.button15, R.id.button16
    };

    LinearLayout layout;
    GridLayout gridLayout;
    Button button,button2;
    TextView xpTextView;
    AudioManager audioManager;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        //MediaPlayer.create(this, R.raw.background_sound).start();
        currTime      = (TextView) findViewById(R.id.currTime);
        xpTextView    = (TextView) findViewById(R.id.myTextProgress);
        layoutButtons = (LinearLayout) findViewById(R.id.llButtons);
        gameLayout    = (LinearLayout) findViewById(R.id.game_layout);

        gTimer = new GTimer(currTime);
        gTimer.stop();
        loadFullProfile();
        //manageButtons();
        loadLanguageSettings();

        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        //
        squareButton();

        ((LinearLayout)findViewById(R.id.llProgress)).getBackground().setAlpha(170);
        ((LinearLayout)findViewById(R.id.llTime)).getBackground().setAlpha(170);
        ((LinearLayout)findViewById(R.id.llBest)).getBackground().setAlpha(170);
        ((LinearLayout)findViewById(R.id.llCount)).getBackground().setAlpha(170);
        ((LinearLayout)findViewById(R.id.llButtons)).getBackground().setAlpha(170);

        new ShuffleButtons().execute();
    }


    private void loadFullProfile() {
        String progressString = "";

        SharedPreferences shared = getSharedPreferences("myData", Context.MODE_PRIVATE);
        levels     = shared.getString("level", DEFAULT);
        xps        = shared.getString("xp", DEFAULT);
        best_times = shared.getString("best_time", DEFAULT);
        count_gs   = shared.getString("count_g",DEFAULT);
        musics     = shared.getString("music",DEFAULT);
        sounds     = shared.getString("sound",DEFAULT);
        themes     = shared.getString("theme",DEFAULT);

        Log.d("LEVELS_READ",levels);
        level = (Button) findViewById(R.id.levelButton);
        if(levels.equals(DEFAULT)) {
            Log.d("LEVELS_DEF",levels);
            levels = "1";
            Log.d("LEVELS_AFTER",levels);
            level.setText("1");
        } else {
            Log.d("LEVELS_NOT_DEF",levels);
            level.setText(levels);
        }

        xp = (ProgressBar) findViewById(R.id.xpBar);
        if(xps.equals(DEFAULT)) {
            xp.setProgress(0);
            xpForNextLevel = 120;
            xp.setMax(xpForNextLevel);
            xps = "0";
            Log.d("XP_NEXT_LEVEL", Integer.toString(xpForNextLevel));
        } else {
            xpForNextLevel = (int) (120 * Math.pow(COEF, Integer.parseInt(levels)));
            xp.setMax(xpForNextLevel);
            xp.setProgress(Integer.parseInt(xps));
            Log.d("XP_NEXT_LEVEL", Integer.toString(xpForNextLevel));
        }

        progressString = xps + "/" + xpForNextLevel;
        xpTextView.setText(progressString);

        bestTime = (TextView) findViewById(R.id.bestTime);
        if(best_times.equals(DEFAULT)) {
            bestTime.setText("00:00.0");
        } else {
            long l = Long.parseLong(best_times);
            bestTime.setText(gTimer.timerToString(l));
        }

        currTime.setText("00:00.0");

        countGame = (TextView) findViewById(R.id.countGame);
        if(count_gs.equals(DEFAULT)) {
            countGame.setText("0");
        } else {
            countGame.setText(count_gs);
        }

        //---------------     SETTINGS     ------------------------

        if(musics.equals(DEFAULT) || musics.equals("true")) {
            //MediaPlayer mp = MediaPlayer.create(this, R.raw.background_sound);
            //mp.setLooping(true);
            //mp.start();
        }

        if(sounds.equals(DEFAULT) || sounds.equals("true")) {
            soundBoolean = true;
        } else { soundBoolean = false; }

        if(themes.equals(DEFAULT) || themes.equals("true")) {
            //
        }


    }


    private void updateProfile(String key,String value) {
        SharedPreferences shared = getSharedPreferences("myData", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = shared.edit();
        editor.putString(key, value);
        editor.commit();
    }

    void manageButtons() {
        layout = (LinearLayout) findViewById(R.id.game_layout);
        int sizeX = layout.getWidth();
        Display display = getWindowManager().getDefaultDisplay();
        int width = display.getWidth();
        int height = display.getHeight();


        int top = (int)(height/3);

        int butPaddingTop       = (int) ( (height-top)/6 );
        int shapeSize           = (int) ( (height-top)-butPaddingTop );
        int butFirstPaddingTop  = (int) ( top+(height-top-shapeSize)/2 );
        int butFirstPaddingLeft = (int) ( (width-shapeSize)/2 );
        int butPaddingLeft      = (int) ( width-(height-top)+butPaddingTop );
        int butPaddingBetween   = (int) ( shapeSize/25 );
        int butHeight           = (int) ( (shapeSize-butPaddingBetween*5)/4 );
        int butWidth            = butHeight;


    }

    public void buttonClick(View view) {
        if(soundBoolean) { audioManager.playSoundEffect(SoundEffectConstants.CLICK, 0.8F); }
        int butId = view.getId();
        int butDisabled = notEnabledButton();

        //if( (butId == butDisabled-1) || (butId == butDisabled+1) ||
        //        (butId == butDisabled-4) || (butId == butDisabled+4) )
        boolean step = false;
        List<Integer> allMoves = checkMoves(getButtonIndex(butDisabled));
        for(int i=0;i<allMoves.size();i++) {
            if(allMoves.get(i) == getButtonIndex(butId)) {
                step = true;
                Log.d("Butkit",String.valueOf(i) + "," + String.valueOf(allMoves.get(i)));
                break;
            }
        }
        if(step)
        {
            if(!firstTimeButtonPressed) {
                gTimer = new GTimer(currTime);
                gTimer.start(); //timer start
                //startTime = System.currentTimeMillis();
                //timerHandler.postDelayed(timerRunnable, 0);
                firstTimeButtonPressed=true;
            }


            COUNT_CLICKS++;
            changeButton(butId, butDisabled);

            setColorForButtons(rIds, Color.parseColor("#C4CDE0"));
            changeColor(butId,Color.parseColor("#58d175"));
            //changeColor((Button) findViewById(butDisabled), Color.BLUE);


            if(checkFinish() == true) {

                gTimer.stop(); //timer stop

                setColorForButtons(rIds, Color.parseColor("#58d175"));

                countGame = (TextView) findViewById(R.id.countGame);
                int buf = Integer.parseInt((String) countGame.getText()) + 1;

                updateProfile("count_g", Integer.toString(buf));
                countGame.setText(Integer.toString(buf));

                Log.d("FUCK", String.valueOf(gTimer.millis));
                updateBestTime(COUNT_CLICKS, gTimer.millis); // + UP LEVEL
                //Toast.makeText(getApplicationContext(), gTimer.timerToString(gTimer.millis)+"FINISH!!!!!",
                 //       Toast.LENGTH_LONG).show();

                COUNT_CLICKS=0;
                firstTimeButtonPressed=false;

                showFinishDialog();
            }
        }


    }

    //           UPDATE BEST TIME + GAIN XP (UP LEVEL)
    private void updateBestTime(int _c, long _m) {
        Log.d("FUCK2", String.valueOf(_m));
        if(best_times.equals(DEFAULT) || (_m < Long.parseLong(best_times)) ) {
            best_times = String.valueOf(_m);
            bestTime.setText(gTimer.timerToString(_m));
            Log.d("FUCK3", String.valueOf(_m));
            updateProfile("best_time", String.valueOf(_m));
        }


        double time = findTimeFromString(gTimer.timerToString(_m));
        findXpGained(_c, time);
    }

    private void findXpGained(int _c, double _t) {
        int result=0;

        if(_c <= MIN_COUNT) {
            _c  = MIN_COUNT;
        } else if (_c >= MAX_COUNT) {
            _c  = MAX_COUNT;
        }
        //_c = MAX_COUNT - _c;

        int xpGained = (int) ((1000 - 5.53 * (_t-1) )*0.4 + (1000 - 9.5 * (_c-1) )*0.6);
        youGainXp = xpGained;
        levelUp(xpGained);

        //levelUp(90);

    }

    private void levelUp(int _xp) {
        int xp1  = Integer.parseInt(xps);
        Log.d("LEVELS_UP",levels);
        int lvl = Integer.parseInt(levels);

        int new_xp = xp1 + _xp;

        // add xp to current xp (ANIMATION)
        if(new_xp > xpForNextLevel) {
            Log.d("GAIN_XP","");
            while(new_xp > xpForNextLevel) {
                lvl++;
                if(lvl < 100) {
                    Log.d("GAIN_NEW_LVL+XP","");
                    animatingProgressBar(xpForNextLevel);
                    levels = Integer.toString(lvl);
                    updateProfile("level", levels);

                    level.setText(levels);

                    new_xp -= xpForNextLevel;
                    xpForNextLevel = (int) (120 * Math.pow(COEF, lvl));

                    updateProfile("xp", String.valueOf(new_xp));
                    xps = Integer.toString(new_xp);
                    xp.setMax(xpForNextLevel);
                } else {
                    if(new_xp >= xpForNextLevel) {
                        Log.d("MAX_XP","");
                        new_xp = xpForNextLevel;
                        updateProfile("xp", String.valueOf(new_xp));

                        xps = Integer.toString(new_xp);
                        animatingProgressBar(new_xp);

                        String progressString = xps + "/" + xpForNextLevel;
                        xpTextView.setText(progressString);
                    }
                }

            }

        }

        Log.d("NEW_XP",String.valueOf(new_xp));
        updateProfile("xp", String.valueOf(new_xp));
        xps = Integer.toString(new_xp);
        animatingProgressBar(new_xp);

        String progressString = xps + "/" + xpForNextLevel;
        xpTextView.setText(progressString);

    }

    private void animatingProgressBar(int _xp) {

        if(android.os.Build.VERSION.SDK_INT >= 11){
            // will update the "progress" propriety of seekbar until it reaches progress
            ObjectAnimator animation = ObjectAnimator.ofInt(xp, "progress", _xp);
            animation.setDuration(1000); // 0.5 second
            animation.setInterpolator(new DecelerateInterpolator());
            animation.start();
        } else {
            xp.setProgress(_xp);
        }

    }

    private double findTimeFromString(String _b) {
        double result=0;

        String store1 = ""; // min
        String store2 = ""; // sec
        String store3 = ""; // msec

        boolean first = false;
        Log.d("STORE",_b);
        for(char c: _b.toCharArray()) {
            if(!first) {
                if (c == ':') {
                    first = true;
                } else {
                    store1 += c;
                }
            } else {
                if(c != '.') {
                    store2 += c;
                } else {
                    break;
                }
            }
        }
        store3 = _b.substring(_b.length()-1,_b.length());

        Log.d("store1",store1);
        Log.d("store2",store2);
        Log.d("store3",store3);

        result += Integer.parseInt(store1)*60;
        result += Integer.parseInt(store2);
        result += Integer.parseInt(store3) * 0.1;

        Log.d("result_store",Double.toString(result));

        if(result <= MIN_TIME) {
            return MIN_TIME;
        } else if (result >= MAX_TIME) {
            return MAX_TIME;
        }

        return result;
    }

    private void getXP(int xp_gained) {

        //levelup();
    }

    public int notEnabledButton() {
        int result = 0;

        for(int i=0;i<16;i++) {
            button = (Button) findViewById(rIds[i]);
            if(button.getVisibility() == View.INVISIBLE) {
                result = rIds[i];
            }
        }

        return result;
    }

    public void changeButton(int b1, int b2) {
        button  = (Button) findViewById(b1);
        button2 = (Button) findViewById(b2);

        button2.setText(button.getText());
        button.setText("");

        button.setVisibility(View.INVISIBLE);
        button2.setVisibility(View.VISIBLE);
    }


    public boolean checkFinish() {
        for(int i=0;i<15;i++) {
            button = (Button) findViewById(rIds[i]);
            if(button.getText() == "") {
                return false;
            }
            if( (i+1) != Integer.parseInt((String) button.getText())) {
                return false;
            }
        }

        return true;
    }

    //make square buttons
    void squareButton() {
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int measuredWidth = metrics.widthPixels;

        Log.d("WIDTH", Integer.toString(measuredWidth));
        int padding = 40;
        int density= getResources().getDisplayMetrics().densityDpi;
        switch(density)
        {
            case DisplayMetrics.DENSITY_LOW:
                padding = 5;
                break;
            case DisplayMetrics.DENSITY_MEDIUM:
                padding = 10;
                break;
            case DisplayMetrics.DENSITY_HIGH:
                padding = 40;
                break;
            case DisplayMetrics.DENSITY_XHIGH:
                padding = 60;
                break;
            case DisplayMetrics.DENSITY_XXHIGH:
                padding = 80;
                break;
            case DisplayMetrics.DENSITY_XXXHIGH:
                padding = 100;
                break;
        }

        makePaddingForLayout(gameLayout, padding);

        for(int i=0;i<16;i++) {
            Button temp=(Button) findViewById(rIds[i]);
            int l=temp.getWidth();
            Log.d("WIDTH_PRE_BUTTON",Integer.toString(l));

            l=(int) ( (measuredWidth-padding*2)/4);
            //l=(int) (measuredWidth/4);
            Log.d("WIDTH_AFT_BUTTON",Integer.toString(l));

            temp.setMaxWidth(l);
            temp.setMaxHeight(l);
            temp.setMinimumWidth(l);
            temp.setMinimumHeight(l);

            ViewGroup.LayoutParams param = temp.getLayoutParams();
            param.width = l;
            param.height = l;
            temp.setLayoutParams(param);
            temp.requestLayout();

        }

    }

    private void makePaddingForLayout(LinearLayout _layout, int _pad) {
        _layout.setPadding(_pad, 0, _pad, 0);

        _layout.requestLayout();
    }

    private void loadLanguageSettings() {
        SharedPreferences shared = getSharedPreferences("myData", Context.MODE_PRIVATE);
        String lang    = shared.getString("lang",DEFAULT);

        //lang - true (ENGLISH), false (RUSSIAN)
        if(lang.equals(DEFAULT) || lang.equals("true")) {
            Log.d("LANG", "TRUE - ENG");
            reloadScreen(true);
            lang_eng = true;
        } else {
            Log.d("LANG", "FALSE - RUS");
            reloadScreen(false);
            lang_eng = false;
        }
    }

    public void reloadScreen(boolean _b) {
        TextView t1 = (TextView) findViewById(R.id.textBest);
        TextView t2 = (TextView) findViewById(R.id.textCur);
        TextView t3 = (TextView) findViewById(R.id.textNow);

        if(_b) {
            t1.setText(R.string.besttime);
            t2.setText(R.string.curtime);
            t3.setText(R.string.countgame);
        } else {
            t1.setText(R.string.rbesttime);
            t2.setText(R.string.rcurtime);
            t3.setText(R.string.rcountgame);
        }
    }

    public void changeColor(int _button, int _color) {
        try {
            ((Button)findViewById(getButtonId(getButtonIndex(_button)-4))).getBackground().setColorFilter(_color, PorterDuff.Mode.MULTIPLY);
        } catch(Exception e) {  }

        try {
            ((Button)findViewById(getButtonId(getButtonIndex(_button)+4))).getBackground().setColorFilter(_color, PorterDuff.Mode.MULTIPLY);
        } catch(Exception e) { }
        Log.d("id+index:",Integer.toString(getButtonIndex(_button))+":"+Integer.toString(getButtonId(getButtonIndex(_button))));
        int i = getButtonIndex(_button);
        if(i == 12 || i == 8 || i == 4) {
        } else {
            try {
                Log.d("but","i try left");
                ((Button)findViewById(getButtonId(i-1))).getBackground().setColorFilter(_color, PorterDuff.Mode.MULTIPLY);
            } catch(Exception e) { }
        }

        if(i == 11 || i == 7 || i == 3) {
        } else {
            try {
                Log.d("but","i try right");
                ((Button)findViewById(getButtonId(i+1))).getBackground().setColorFilter(_color, PorterDuff.Mode.MULTIPLY);
            } catch(Exception e) { }
        }
    }

    public void setColorForButtons(int[] _buts,int _color) {
        for(int i=0; i<_buts.length;i++) {
            ((Button)findViewById(_buts[i])).getBackground().setColorFilter(_color, PorterDuff.Mode.MULTIPLY);
        }
    }

    public int getButtonId(int _id) {
        for(int i=0;i<rIds.length;i++) {
            if( i == _id) return rIds[i];
        }

        return 0;
    }

    public int getButtonIndex(int _but) {
        for(int i=0;i<rIds.length;i++) {
            if( rIds[i] == _but) return i;
        }

        return -1;
    }

    public void shuffle() {
        for(int i=0;i<str.length;i++) {
            strNum.add(str[i]);
        }
        for(int i=0;i<strNum.size();i++) {
            Log.d("JJJJ", strNum.get(i));
        }

        for(int i=0;i<300;i++) {
            int isNull = findNum(String.valueOf("Null"));
            Log.d("NULL",Integer.toString(isNull));
            List<Integer> movesAvailable = checkMoves(isNull);
            int j = rand.nextInt(movesAvailable.size());
            Log.d("J",Integer.toString(isNull));
            move(isNull, movesAvailable.get(j));
        }




    }

    public List<Integer> checkMoves(int index) {
        List<Integer> arr = new ArrayList<Integer>();
        int res = 0;
        if(index == 0) {
            res = 0;
        } else if(index == 3) {
            res = 1;
        } else if(index == 12) {
            res = 2;
        } else if(index == 15) {
            res = 3;
        } else if(index == 2 || index == 1) {
            res = 4;
        } else if(index == 13 || index == 14) {
            res = 6;
        } else if(index == 4 || index == 8) {
            res = 7;
        } else if(index == 7 || index == 11) {
            res = 8;
        } else {
            res = 5;
        }
        switch (res) {
            case 0: // 1 (2 6)
                arr.add(1); arr.add(4);
                break;
            case 1: // 4 (4 2)
                arr.add(2); arr.add(7);
                break;
            case 2: // 13 (8 6)
                arr.add(8); arr.add(13);
                break;
            case 3: // 16 (4 8)
                arr.add(14); arr.add(11);
                break;
            case 4: // 2,3 (4 2 6)
                arr.add(index-1); arr.add(index+4); arr.add(index+1);
                break;
            case 5: // 6,7,10,11,14,15 (4 8 6 2)
                arr.add(index-1); arr.add(index+4); arr.add(index+1); arr.add(index-4);
                break;
            case 6: // 14,15 (4 8 6)
                arr.add(index-1); arr.add(index-4); arr.add(index+1);
                break;
            case 7: // 5,9 (8 6 2)
                arr.add(index-4); arr.add(index+4); arr.add(index+1);
                break;
            case 8: // 8,12 (4 8 2)
                arr.add(index-4); arr.add(index+4); arr.add(index-1);
                break;
        }

        return arr;
    }

    public void move(int _b1, int _b2) {
        String foo = strNum.get(_b1);
        String bar = strNum.get(_b2);
        strNum.set(_b2,foo); strNum.set(_b1,bar);

        String buf = "";
        for(int i=0;i<strNum.size();i++) {
            buf += strNum.get(i) + ",";
        }
        Log.d("MOVE",buf);

    }

    public int findNum(String _s) {
        for(int i=0;i<strNum.size();i++) {
            if(strNum.get(i).equals(_s)) { return i; }
        }
        return 0;
    }


    public class ShuffleButtons extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            shuffle();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            //super.onPostExecute(aVoid);
            //((Button)findViewById(getButtonId(1))).getBackground().setColorFilter(Color.parseColor("#58d175"), PorterDuff.Mode.MULTIPLY);
            setColorForButtons(rIds,Color.parseColor("#C4CDE0"));
            for(int i=0;i<strNum.size();i++) {
                if(strNum.get(i).equals("Null")) {
                    ((Button)findViewById(rIds[i])).setText("");
                    ((Button)findViewById(rIds[i])).setVisibility(View.INVISIBLE);
                    changeColor(getButtonId(i),Color.parseColor("#58d175"));
                } else {
                    ((Button)findViewById(rIds[i])).setText(strNum.get(i));
                    ((Button)findViewById(rIds[i])).setVisibility(View.VISIBLE);
                }
            }

            /*SharedPreferences shared = getSharedPreferences("myData", Context.MODE_PRIVATE);
            levels     = shared.getString("level", DEFAULT);

            Log.d("LEVELS_post",levels);
            if(!levels.equals(DEFAULT)) {
                if (Integer.parseInt(levels) == 99) {
                    ((Button) findViewById(R.id.levelButton)).getBackground().setColorFilter(Color.parseColor("#58d175"), PorterDuff.Mode.MULTIPLY);
                }
            }*/

            //progressDialog.cancel();
        }


        @Override
        protected void onPreExecute() {
            //progressDialog = new ProgressDialog(GameActivity.this);
            //progressDialog.setMessage("Loading...");
            //progressDialog.show();
        }
    }


    public void showFinishDialog(){
        AlertDialog.Builder adb = new AlertDialog.Builder( new ContextThemeWrapper(this,R.style.AlertDialogCustom));
        //adb.setView(alertDialogView); // rus eng+++
        String ok_button = "", cancel_button = "";
        if(lang_eng) {
            adb.setTitle("You gain: " + String.valueOf(youGainXp) + " XP. Do you wanna play again?");
            ok_button = "New Game"; cancel_button = "Menu";
        } else {
            adb.setTitle("Вы получили: " + String.valueOf(youGainXp) + " XP. Хотите сыграть еще?");
            ok_button = "Новая игра"; cancel_button = "Меню";
        }
        //adb.setIcon(android.R.drawable.ic_dialog_alert);
        adb.setPositiveButton(ok_button, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = getIntent();
                finish();
                startActivity(intent);

                //EditText et = (EditText)alertDialogView.findViewById(R.id.EditText1);


                //Toast.makeText(Tutoriel18_Android.this, et.getText(), Toast.LENGTH_SHORT).show();
            } });

        adb.setNegativeButton(cancel_button, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(GameActivity.this, MainMenuActivity.class);
                startActivity(intent);
            } });
        adb.show();
    }
}