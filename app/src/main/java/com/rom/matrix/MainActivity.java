package com.rom.matrix;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.graphics.Typeface;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class MainActivity extends Activity {

    private final static String FONT_DIALOG_TEXT = "Titillium/Titillium-Regular.otf";
    private final static String FONT_ACTION_BAR_TITLE = "taurusmono-master/Taurus-Mono-Outline-Bold.otf";

    private boolean nextHalfDial = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

		/* Set custom font */
        final TextView dialText = (TextView) findViewById(R.id.dial_text);
        Typeface customFont = Typeface.createFromAsset(getAssets(), FONT_DIALOG_TEXT);
        dialText.setTypeface(customFont);

        final Button next = (Button) findViewById(R.id.nextButton);
        next.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (nextHalfDial) {
                    dialText.setText(R.string.main_text_1);
                    next.setText(">> 2");
                    nextHalfDial = false;
                } else {
                    dialText.setText(R.string.main_text_2);
                    next.setText("1 <<");
                    nextHalfDial = true;
                }
            }
        });

        /* Set custom font */
        int titleId = getResources().getIdentifier("action_bar_title", "id", "android");
        TextView actionBarTitle = (TextView) findViewById(titleId);
        actionBarTitle.setTextColor(getResources().getColor(R.color.titleColor));
//        actionBarTitle.setTextSize(24f);
        customFont = Typeface.createFromAsset(getAssets(), FONT_ACTION_BAR_TITLE);
        actionBarTitle.setTypeface(customFont);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
