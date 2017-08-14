package com.lentcoding.dicetrifecta;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.preference.PreferenceManager;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Random;

class Dice {
    private static final Random rand = new Random();
    private static final ArrayList<Integer> dice = new ArrayList<>();

    static void renderDice(Context c) {
        for (int i = 0; i < 3; i++) {
            if (dice.isEmpty()) {
                dice.add(6);
                dice.add(6);
                dice.add(6);
            }
            String imageName = "die_" + dice.get(i) + MainActivity.diceType + ".png";
            try {
                InputStream stream = c.getAssets().open(imageName);
                Drawable d = Drawable.createFromStream(stream, null);
                MainActivity.diceImageViews.get(i).setImageDrawable(d);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    static void rollDice(Context c) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(c);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        String msg;
        int die1 = rand.nextInt(6) + 1;
        int die2 = rand.nextInt(6) + 1;
        int die3 = rand.nextInt(6) + 1;
        dice.clear();
        dice.add(die1);
        dice.add(die2);
        dice.add(die3);

        renderDice(c);

        die1 = dice.get(0);
        die2 = dice.get(1);
        die3 = dice.get(2);
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
