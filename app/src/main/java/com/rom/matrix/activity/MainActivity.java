package com.rom.matrix.activity;

import android.app.Activity;
import android.content.res.AssetFileDescriptor;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.rom.matrix.R;
import com.rom.matrix.app.Constants;

import java.io.IOException;

public class MainActivity extends Activity implements Constants {

    /** Values */
    private static final int FIRST_DELAY_MILLIS = 20010;
    private static final int SECOND_DELAY_MILLIS = 21520;
    private static final String DIALOG_NAME = "DD2.mp3";

    private boolean firstHalfDialog = true;
    private int delayMillis = FIRST_DELAY_MILLIS; // init
    private boolean startedHandler = false;
    private String text1;
    private String text2;

    /** View elements */
    private TextView dialText;
    private Button startOrStop;

    /** Other */
    private Handler handler;
    private MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        mediaPlayer = new MediaPlayer();
        text1 = getString(R.string.main_text_1);
        text2 = getString(R.string.main_text_2);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
//        int id = item.getItemId();
//
//        if (id == R.id.action_settings) {
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Initialize view
     */
    private void initView() {
        /* Set custom font */
        dialText = (TextView) findViewById(R.id.dial_text);
        Typeface customFont = Typeface.createFromAsset(getAssets(), FONT_DIALOG_TEXT);
        dialText.setTypeface(customFont);

        startOrStop = (Button) findViewById(R.id.startOrStop);
        startOrStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!startedHandler) {
                    startHandler();
                } else {
                    stopHandler();
                }
            }
        });

        /* Set custom font */
        int titleId = getResources().getIdentifier("action_bar_title", "id", "android");
        TextView actionBarTitle = (TextView) findViewById(titleId);
        actionBarTitle.setTextColor(getResources().getColor(R.color.titleColor));
        customFont = Typeface.createFromAsset(getAssets(), FONT_ACTION_BAR_TITLE);
        actionBarTitle.setTypeface(customFont);
    }

    /**
     * Start Handler
     */
    private void startHandler() {
        if (handler == null) {
            handler = new Handler();
        }
        prepareMediaPlayer();

        startedHandler = true;
        firstHalfDialog = false;
        startOrStop.setText(R.string.stop);
        handler.postDelayed(runnable, FIRST_DELAY_MILLIS);
    }

    /**
     * Stop Handler
     */
    private void stopHandler() {
        if (handler != null) {
            handler.removeCallbacks(runnable);

            startedHandler = false;
            firstHalfDialog = true;
            startOrStop.setText(R.string.start);
            executeReplace(); // reset
            mediaPlayer.stop();
            mediaPlayer.reset();
        }
    }

    /**
     * Create task
     */
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
//          fixPost();
            executeReplace();
            handler.postDelayed(runnable, delayMillis);
        }
    };

    /**
     * This method determinate diff between player position & handler position
     * to the thread sleeps at for the given interval.
     */
    private void fixPost() {
        long diff = !firstHalfDialog ? Math.abs(delayMillis - mediaPlayer.getCurrentPosition())
                : Math.abs(FIRST_DELAY_MILLIS + SECOND_DELAY_MILLIS - mediaPlayer.getCurrentPosition());
        try {
            Thread.sleep(diff);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Replace text
     */
    private void executeReplace() {
        if (firstHalfDialog) {
            dialText.setText(text1);
            firstHalfDialog = false;
            delayMillis = FIRST_DELAY_MILLIS;
        } else {
            dialText.setText(text2);
            firstHalfDialog = true;
            delayMillis = SECOND_DELAY_MILLIS;
        }
    }

    /**
     * Prepare Media Player
     */
    private void prepareMediaPlayer() {
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
            mediaPlayer.reset();
        }
        try {
            AssetFileDescriptor afd;
            afd = getAssets().openFd(DIALOG_NAME);
            mediaPlayer.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
            mediaPlayer.prepare();
            mediaPlayer.start();
            mediaPlayer.setLooping(true);
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
