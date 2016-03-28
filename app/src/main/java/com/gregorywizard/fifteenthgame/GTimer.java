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

import android.os.Handler;
import android.widget.TextView;

import java.util.Timer;

/**
 * Created by Gregory on 11/19/15.
 */
public class GTimer extends Timer {
    Handler  gHandler = new Handler();
    TextView gTextView;

    Timer    gTimer;
    long     startGTime,stopGTime,elapsedGTime;
    int      hours,mins,secs,msecs;
    long     millis;

    public GTimer(TextView textView) {
        gTimer    = new Timer();
        gTextView = textView;
    }

    public void start() {
        startGTime = System.currentTimeMillis();
        gHandler.postDelayed(timerRunnable, 0);
    }

    public void stop() {

        gHandler.removeCallbacks(timerRunnable);

        // -OLD-
        //gHandler.removeCallbacks(startGTimer);
    }

    //int i=0;
    public String timerToString(long time) { //mins:secs.seconds - 10:16.1
        secs  = (int)(time/1000);
        mins  = secs/60;
        hours = mins/60;

        String milliseconds = String.valueOf((long)time);
        if(milliseconds.length() > 3) {
            milliseconds = milliseconds.substring(milliseconds.length() - 3, milliseconds.length() - 2);
        } else if(milliseconds.length() == 3){
            milliseconds = milliseconds.substring(0,milliseconds.length()-2);
            //i++;
        }

        String seconds=String.valueOf(secs % 60);
        if(secs == 0){
            seconds = "00";
        }
        if(secs <10 && secs > 0){
            seconds = "0"+seconds;
        }

        String minutes=String.valueOf(mins % 60);
        if(mins == 0){
            minutes = "00";
        }
        if(mins <10 && mins > 0){
            minutes = "0"+minutes;
        }

        /*String shours=String.valueOf(hours);
        if(hours == 0){
            shours = "00";
        }
        if(hours <10 && hours > 0){
            shours = "0"+shours;
        }*/

        String stime = minutes + ":" + seconds + "." + milliseconds;
        return stime;
    }

    private Runnable startGTimer = new Runnable() {
        public void run() {
            elapsedGTime = System.currentTimeMillis();
            updateGTimer(elapsedGTime);
            gHandler.postDelayed(this,(int)100);
        }
    };

    private void updateGTimer(long time) {
        setGTimeOnTextView(time);
    }

    public void setGTimeOnTextView(long time) {
        String gt = timerToString(time);
        gTextView.setText(gt);
    }

    Runnable timerRunnable = new Runnable() {

        @Override
        public void run() {
            millis = System.currentTimeMillis() - startGTime;
            updateGTimer(millis);

            //int seconds = (int) (millis / 1000);
            //int minutes = seconds / 60;
            //seconds = seconds % 60;
            //String gt = timerToString(millis);
            //gTextView.setText(gt);
            //gTextView.setText(String.format("%d:%02d", minutes, seconds));

            gHandler.postDelayed(this,100);
        }
    };

}

