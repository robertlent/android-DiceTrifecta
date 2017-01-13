package com.lentcoding.dicetrifecta;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
//import android.net.Uri;

//import com.google.android.gms.appindexing.Action;
//import com.google.android.gms.appindexing.AppIndex;
//import com.google.android.gms.appindexing.Thing;
//import com.google.android.gms.common.api.GoogleApiClient;

import java.util.ArrayList;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    //Suppressing the memory leak messages until I figure out a better way of accessing these TextViews without them being static
    @SuppressLint("StaticFieldLeak")
    public static TextView viewResult;
    @SuppressLint("StaticFieldLeak")
    public static TextView viewScore;
    @SuppressLint("StaticFieldLeak")
    public static TextView viewHiScore;
    static int score;
    static int hiScore;
    static ArrayList<ImageView> diceImageViews;
    static String diceType;
    private static String fabType;

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
//    private GoogleApiClient client;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getWindow().setBackgroundDrawableResource(R.drawable.felt);
        setToFullScreen();

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = sharedPreferences.edit();
        hiScore = sharedPreferences.getInt("hiScore", 0);
        diceType = sharedPreferences.getString("diceType", "");
        fabType = sharedPreferences.getString("fabType", "black");
        editor.apply();

        applyTypes();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dice.rollDice(getApplicationContext());
            }
        });

        score = 0;

        viewResult = (TextView) findViewById(R.id.viewResult);
        viewScore = (TextView) findViewById(R.id.viewScore);
        viewHiScore = (TextView) findViewById(R.id.viewHiScore);
        viewScore.setText(R.string.initial_score);
        viewHiScore.setText(getString(R.string.hi_score, hiScore));

        ImageView die1Image = (ImageView) findViewById(R.id.die1Image);
        ImageView die2Image = (ImageView) findViewById(R.id.die2Image);
        ImageView die3Image = (ImageView) findViewById(R.id.die3Image);

        diceImageViews = new ArrayList<>();
        diceImageViews.add(die1Image);
        diceImageViews.add(die2Image);
        diceImageViews.add(die3Image);

        //Firebasae Google App Indexing
//        Intent intent = getIntent();
//        String action = intent.getAction();
//        String data = intent.getDataString();
//        if (Intent.ACTION_VIEW.equals(action) && data != null) {
//
//        }
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
//        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    private void setToFullScreen() {
        ViewGroup rootLayout = (ViewGroup) findViewById(R.id.activity_main);

        rootLayout.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
    }

    @Override
    protected void onResume() {
        super.onResume();
        setToFullScreen();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_change_dice) {
            changeTypes();
        }

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

            viewResult.setText(R.string.msg_reset_hi_score);
            viewHiScore.setText(R.string.initial_hi_score);
        }

        if (id == R.id.action_exit) {
            Intent intent = new Intent(getApplicationContext(), Directions.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra("LOGOUT", true);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    //Initializes the dice and action button types, based on the values stored in SharedPreferences
    private void applyTypes() {
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        ImageView die1Image = (ImageView) findViewById(R.id.die1Image);
        ImageView die2Image = (ImageView) findViewById(R.id.die2Image);
        ImageView die3Image = (ImageView) findViewById(R.id.die3Image);

        if (Objects.equals(fabType, "black")) {
            fab.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.black)));
        } else if (Objects.equals(fabType, "holo_red_dark")) {
            fab.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.holo_red_dark)));
        }

        if (Objects.equals(diceType, "")) {
            die1Image.setImageResource(R.drawable.die_6);
            die2Image.setImageResource(R.drawable.die_6);
            die3Image.setImageResource(R.drawable.die_6);
        } else if (Objects.equals(diceType, "_redpips")) {
            die1Image.setImageResource(R.drawable.die_6_redpips);
            die2Image.setImageResource(R.drawable.die_6_redpips);
            die3Image.setImageResource(R.drawable.die_6_redpips);
        }
    }

    //Changes the active types for the dice and action button, and saves the new type in SharedPreferences
    private void changeTypes() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = sharedPreferences.edit();
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        ImageView die1Image = (ImageView) findViewById(R.id.die1Image);
        ImageView die2Image = (ImageView) findViewById(R.id.die2Image);
        ImageView die3Image = (ImageView) findViewById(R.id.die3Image);
        diceImageViews.clear();

        if (Objects.equals(diceType, "")) {
            editor.putString("diceType", "_redpips");
            editor.apply();
            diceType = sharedPreferences.getString("diceType", "_redpips");
            diceImageViews.add(die1Image);
            diceImageViews.add(die2Image);
            diceImageViews.add(die3Image);
            Dice.renderDice(getApplicationContext());
        } else if (Objects.equals(diceType, "_redpips")) {
            editor.putString("diceType", "");
            editor.apply();
            diceType = sharedPreferences.getString("diceType", "");
            diceImageViews.add(die1Image);
            diceImageViews.add(die2Image);
            diceImageViews.add(die3Image);
            Dice.renderDice(getApplicationContext());
        }

        if (Objects.equals(fabType, "black")) {
            fab.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.holo_red_dark)));
            editor.putString("fabType", "holo_red_dark");
            editor.apply();
            fabType = sharedPreferences.getString("fabType", "holo_red_dark");
        } else if (Objects.equals(fabType, "holo_red_dark")) {
            fab.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.black)));
            editor.putString("fabType", "black");
            editor.apply();
            fabType = sharedPreferences.getString("fabType", "black");
        }
    }

}

/**
 * ATTENTION: This was auto-generated to implement the App Indexing API.
 * See https://g.co/AppIndexing/AndroidStudio for more information.
 */
//    public Action getIndexApiAction() {
//        Thing object = new Thing.Builder()
//                .setName("Main Page") // TODO: Define a title for the content shown.
//                // TODO: Make sure this auto-generated URL is correct.
//                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
//                .build();
//        return new Action.Builder(Action.TYPE_VIEW)
//                .setObject(object)
//                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
//                .build();
//    }

//    @Override
//    public void onStart() {
//        super.onStart();
//
//        // ATTENTION: This was auto-generated to implement the App Indexing API.
//        // See https://g.co/AppIndexing/AndroidStudio for more information.
//        client.connect();
//        AppIndex.AppIndexApi.start(client, getIndexApiAction());
//    }
//
//    @Override
//    public void onStop() {
//        super.onStop();
//
//        // ATTENTION: This was auto-generated to implement the App Indexing API.
//        // See https://g.co/AppIndexing/AndroidStudio for more information.
//        AppIndex.AppIndexApi.end(client, getIndexApiAction());
//        client.disconnect();
//    }