package com.mycompany.myfcmapp;

import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

// Import seems to be the equivalent of a C++ #include.
// Quite cool that Android Studio finds the correct Import
// file just by pressing Alt+Enter.
import android.content.Intent;
import android.view.View;
import android.widget.EditText;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;


public class MyActivity extends ActionBarActivity
{
    // Defining a class static used as a key name in the Intent class.
    // I don't know what 'final' means but the rest is exactly the same
    // as a C++ class static variable declaration
    public final static String EXTRA_MESSAGE = "com.mycompany.myfcmapp.MESSAGE";
    // The text immediately below refers to the static variable declaration above.
    // For the next activity to query the extra data, you should define the key for your
    // intent's extra using a public constant. It's generally a good practice to define keys
    // for intent extras using your app's package name as a prefix. This ensures the keys are
    // unique, in case your app interacts with other apps.

    // Called when the user clicks the Send button.
    // Is this an inline function? Does java have the concept of headers and bodies (modules)?
    public void sendMessage(View view) {
        // Do something in response to button
        Intent intent = new Intent(this, DisplayMessageActivity.class);

        // R must refer to the java Resource file where ids are recorded.
        // Looks like the find result is being cast to the EditText type ...
        // ... so no strong typing then.
        EditText editText = (EditText) findViewById(R.id.edit_message);
        String fcmFileName = editText.getText().toString();

        String message = AnalyseFCM(fcmFileName);

        intent.putExtra(EXTRA_MESSAGE, message);
        startActivity(intent);
    }

    String AnalyseFCM(String fcmFileName)
    {
        // I am assuming that String supports copy on write. Seems to.
        String message = fcmFileName;

        // ********************************************************************
        // Experimental file reading code.
        // TODO: Learn how to put this method into a library as part of a new
        //       class.
        /* Checks if external storage is available to at least read */
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state))
        {
            message = "WRITE and READ: " + message;
        }
        else if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state))
        {
            message = "READ ONLY: " + message;
        }
        else
        {
            message = "INACCESSIBLE: " + message;
        }

        // Notes: suggest reading files from DIRECTORY_DOWNLOADS and writing
        //        files to DIRECTORY_DOCUMENTS.
        // Create a file. Test out using DIRECTORY_DOWNLOADS.
        // Create a path for the directory.
        File fcmFileDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DOWNLOADS),"FCM");
        // And a path for the file.
        File fcmFile = new File(fcmFileDir, fcmFileName);
        message = fcmFile.toString() + " : " + message;

        if (fcmFileDir.mkdirs())
        {
            // The directory including path has been created.
            message = "mkdirs=true : " + message;

        }
        else
        {
            // The directory already existed or there was a problem.
            message = "mkdirs=false : " + message;
        }

        // Write some data to it
        try
        {
            // Open the file
            BufferedReader fcmReadBuf = new BufferedReader(new FileReader(fcmFile));
            // Get the data out and add it into message.
            String fcmLine;
            final int MAX_LINES = 100000;
            int lineNumber = 0;
            int pubLines = 0;
            int subLines = 0;
            int otherLines = 0;
            char char1;

            // OK, that is enough hacking. NEXT ...
            // * DONE. Learn how to define functions on this class.
            // * Learn how to define new classes.
            // * Create a class that extracts the pub/sub IP address
            // * Find a data structure that can store IP addresses with efficient look up
            //   i.e. if (listOfIPs.exists("10.30.75.14")) { // do stuff };
            // * And can count its entries, i.e. listOfIPs.count().
            // * Learn to write unit tests for classes written.
            // * DONE. Learn how to use GitHub for source control.

            while (((fcmLine = fcmReadBuf.readLine()) != null)
                    && lineNumber < MAX_LINES)
            {
                char1 = fcmLine.charAt(0);
                switch (char1)
                {
                    case 'P': pubLines++;
                        break;
                    case 'S': subLines++;
                        break;
                    default: otherLines++;
                        break;
                }
                lineNumber++;
            }
            message = String.format("#Pub/Sub lines = %d/%d \n",pubLines, subLines) + message;
            message = String.format("#Lines/other = %d/%d \n", lineNumber, otherLines) + message;

            // Close the file
            fcmReadBuf.close();
        }
        catch (IOException e)
        {
            message = "IO exception[" + e.getMessage() + "]: " + message;
        }
        // ********************************************************************
        return message;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_my, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
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

    @Override
    public void onPause()
    {
        super.onPause();
    }

    @Override
    public void onResume()
    {
        super.onResume();
    }

    @Override
    protected void onStart()
    {
        super.onStart();
    }
}
