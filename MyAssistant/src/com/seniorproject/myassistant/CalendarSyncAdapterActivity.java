package com.seniorproject.myassistant;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
 
@SuppressLint("NewApi")
public class CalendarSyncAdapterActivity extends Activity {
 
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar_syncadapter);
        Button qry = (Button) findViewById(R.id.querybut);
        qry.setOnClickListener(new View.OnClickListener() {
 
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                query();
            }
        });
    }
 
    public void query() {
        String[] projection = { CalendarContract.Events._ID,
                CalendarContract.Events.TITLE };
        // Get a Cursor over the Events Provider.
        Cursor cursor = getContentResolver().query(
                CalendarContract.Events.CONTENT_URI, projection, null, null,
                null);
        // Get the index of the columns.
        int nameIdx = cursor
                .getColumnIndexOrThrow(CalendarContract.Events.TITLE);
        int idIdx = cursor.getColumnIndexOrThrow(CalendarContract.Events._ID);
        // Initialize the result set.
        String[] result = new String[cursor.getCount()];
        // Iterate over the result Cursor.
        while (cursor.moveToNext()) {
            // Extract the name.
            String name = cursor.getString(nameIdx);
            // Extract the unique ID.
            String id = cursor.getString(idIdx);
            result[cursor.getPosition()] = name + "(" + id + ")";
            Toast.makeText(this, name + "(" + id + ")", Toast.LENGTH_SHORT)
                    .show();
        }
        // Close the Cursor.
        cursor.close();
    }
 
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
 
}