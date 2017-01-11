package com.lentcoding.dicetrifecta;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.preference.PreferenceManager;
import android.view.View;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Random;

class Dice {

    static void rollDice(View v, Context c) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(c);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        String msg;
        Random rand = new Random();
        int die1 = rand.nextInt(6) + 1;
        int die2 = rand.nextInt(6) + 1;
        int die3 = rand.nextInt(6) + 1;

        ArrayList<Integer> dice = new ArrayList<>();
        dice.clear();
        dice.add(die1);
        dice.add(die2);
        dice.add(die3);

        for (int i = 0; i < 3; i++) {
            String imageName = "die_" + dice.get(i) + ".png";
            try {
                InputStream stream = c.getAssets().open(imageName);
                Drawable d = Drawable.createFromStream(stream, null);
                MainActivity.diceImageViews.get(i).setImageDrawable(d);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (die1 == die2 && die1 == die3) {
            int scoreDelta = die1 * 100;
            msg = "You rolled a triple " + die1 + "! You score " + scoreDelta + " points!";
            MainActivity.score += scoreDelta;
        } else if (die1 == die2 || die1 == die3 || die2 == die3) {
            msg = "You rolled doubles for 50 points!";
            MainActivity.score += 50;
        } else {
            msg = "You didn't score this roll. Try again!";
        }

        if (MainActivity.score > MainActivity.hiScore) {
            MainActivity.hiScore = MainActivity.score;
            editor.putInt("hiScore", MainActivity.score);
            editor.apply();
            MainActivity.viewHiScore.setText(c.getString(R.string.hi_score, MainActivity.hiScore));
        }
        MainActivity.viewResult.setText(msg);
        MainActivity.viewScore.setText(c.getString(R.string.score, MainActivity.score));
    }

}
