package com.mycompany.myfcmapp;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;


public class DisplayMessageActivity extends ActionBarActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
    // Q. Don't know what the Bundle and 'super' is.
	// A. A Bundle is the saved instance state for this activity. The
	// Bundle is saved if the activity is destroyed, e.g. screen rotation.
	// Super seems to mean the class immediately above in the inheritance
	// hierarchy.
        super.onCreate(savedInstanceState);
        // Every activity is invoked by an 'Intent'. The next line gets the Intent.
        Intent intent = getIntent();
        // Now get the string passed in the Intent named by the MyActivity
        // EXTRA_MESSAGE static String. This is the key-value pair that we
        // put in the Activity when the Send button was pressed.
        String message = intent.getStringExtra(MyActivity.EXTRA_MESSAGE);

        // Now let's display the message we have retrieved.
        // First we need to create a TextView object and set up the message
        // and its display properties.
        TextView textView = new TextView(this);
        textView.setTextSize(20);
        textView.setText(message);

        // Then add the TextView as the root view of the activity’s layout by passing
        // it to setContentView().
        setContentView(textView);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_display_message, menu);
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
