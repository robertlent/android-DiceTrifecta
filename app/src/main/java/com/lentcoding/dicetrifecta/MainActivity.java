package com.lentcoding.dicetrifecta;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    TextView viewResult, viewScore, viewHiScore;
    int score, hiScore, die1, die2, die3;
    ArrayList<Integer> dice;
    ArrayList<ImageView> diceImageViews;
    Random rand;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rollDice(view);
            }
        });

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = sharedPreferences.edit();
        hiScore = sharedPreferences.getInt("hiScore", 0);
        editor.apply();

        score = 0;
        rand = new Random();

        viewResult = (TextView) findViewById(R.id.viewResult);
        viewScore = (TextView) findViewById(R.id.viewScore);
        viewScore.setText(R.string.initialScore);
        viewHiScore = (TextView) findViewById(R.id.viewHiScore);
        viewHiScore.setText("Hi-Score: " + hiScore);

        dice = new ArrayList<>();
        ImageView die1Image = (ImageView) findViewById(R.id.die1Image);
        ImageView die2Image = (ImageView) findViewById(R.id.die2Image);
        ImageView die3Image = (ImageView) findViewById(R.id.die3Image);

        diceImageViews = new ArrayList<>();
        diceImageViews.add(die1Image);
        diceImageViews.add(die2Image);
        diceImageViews.add(die3Image);
    }

    public void rollDice(View v) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        String msg;
        die1 = rand.nextInt(6) + 1;
        die2 = rand.nextInt(6) + 1;
        die3 = rand.nextInt(6) + 1;

        dice.clear();
        dice.add(die1);
        dice.add(die2);
        dice.add(die3);

        for (int i = 0; i < 3; i++) {
            String imageName = "die_" + dice.get(i) + ".png";
            try {
                InputStream stream = getAssets().open(imageName);
                Drawable d = Drawable.createFromStream(stream, null);
                diceImageViews.get(i).setImageDrawable(d);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (die1 == die2 && die1 == die3) {
            int scoreDelta = die1 * 100;
            msg = "You rolled a triple " + die1 + "! You score " + scoreDelta + " points!";
            score += scoreDelta;
        } else if (die1 == die2 || die1 == die3 || die2 == die3) {
            msg = "You rolled doubles for 50 points!";
            score += 50;
        } else {
            msg = "You didn't score this roll. Try again!";
        }

        if (score > hiScore) {
            hiScore = score;
            editor.putInt("hiScore", score);
            editor.apply();
            viewHiScore.setText("Hi-Score: " + hiScore);
        }
        viewResult.setText(msg);
        viewScore.setText("Score: " + score);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }

        if (id == R.id.action_newGame) {
            finish();
            startActivity(getIntent());
        }

        if (id == R.id.action_clear) {
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            SharedPreferences.Editor editor = sharedPreferences.edit();

            hiScore = 0;
            editor.putInt("hiScore", 0);
            editor.apply();

            viewResult.setText("Your Hi-Score has been reset to 0.");
            viewHiScore.setText("Hi-Score: 0");
        }

        if (id == R.id.action_exit) {
            Intent intent = new Intent(getApplicationContext(), Directions.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra("LOGOUT", true);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }
}
