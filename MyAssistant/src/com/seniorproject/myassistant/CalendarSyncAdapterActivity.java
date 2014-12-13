package com.seniorproject.myassistant;


import java.util.GregorianCalendar;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.provider.CalendarContract.Calendars;
import android.provider.CalendarContract.Events;
import android.view.View;
import android.widget.Toast;

public class CalendarSyncAdapterActivity extends Activity {
	// Parameters for quering the calendar
	// Projection array. Creating indices for this array instead of doing
	// dynamic lookups improves performance.
	public static final String[] EVENT_PROJECTION = new String[] {
			Calendars._ID, // 0
			Calendars.ACCOUNT_NAME, // 1
			Calendars.CALENDAR_DISPLAY_NAME // 2
	};

	// The indices for the projection array above.
	private static final int PROJECTION_ID_INDEX = 0;
	private static final int PROJECTION_ACCOUNT_NAME_INDEX = 1;
	private static final int PROJECTION_DISPLAY_NAME_INDEX = 2;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_calendar_syncadapter);
	}

	public void onClick(View view) {
		// Intent calIntent = new Intent(Intent.ACTION_INSERT);
		// calIntent.setData(CalendarContract.Events.CONTENT_URI);
		// startActivity(calIntent);

		Intent intent = new Intent(Intent.ACTION_INSERT);
		intent.setType("vnd.android.cursor.item/event");
		intent.putExtra(Events.TITLE, "Learn Android");
		intent.putExtra(Events.EVENT_LOCATION, "Home suit home");
		intent.putExtra(Events.DESCRIPTION, "Download Examples");

		// Setting dates
		GregorianCalendar calDate = new GregorianCalendar(2012, 10, 02);
		intent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME,
				calDate.getTimeInMillis());
		intent.putExtra(CalendarContract.EXTRA_EVENT_END_TIME,
				calDate.getTimeInMillis());

		// Make it a full day event
		intent.putExtra(CalendarContract.EXTRA_EVENT_ALL_DAY, true);

		// Make it a recurring Event
		intent.putExtra(Events.RRULE,
				"FREQ=WEEKLY;COUNT=11;WKST=SU;BYDAY=TU,TH");

		// Making it private and shown as busy
		intent.putExtra(Events.ACCESS_LEVEL, Events.ACCESS_PRIVATE);
		intent.putExtra(Events.AVAILABILITY, Events.AVAILABILITY_BUSY);

		startActivity(intent);

	}

	public void queryCalendar(View view) {
		// Run query
		Cursor cur = null;
		ContentResolver cr = getContentResolver();
		Uri uri = Calendars.CONTENT_URI;
		String selection = "((" + Calendars.ACCOUNT_NAME + " = ?) AND ("
				+ Calendars.ACCOUNT_TYPE + " = ?))";

		// Replace this with your own user and account type
		String[] selectionArgs = new String[] { "Lars.Vogel@gmail.com",
				"com.google" };
		// Submit the query and get a Cursor object back.
		cur = cr.query(uri, EVENT_PROJECTION, selection, selectionArgs, null);
		Toast.makeText(this, String.valueOf(cur.getCount()), Toast.LENGTH_LONG)
				.show();
		// Use the cursor to step through the returned records
		while (cur.moveToNext()) {
			long calID = 0;
			String displayName = null;
			String accountName = null;

			// Get the field values
			calID = cur.getLong(PROJECTION_ID_INDEX);
			displayName = cur.getString(PROJECTION_DISPLAY_NAME_INDEX);
			accountName = cur.getString(PROJECTION_ACCOUNT_NAME_INDEX);

			// Do something with the values...
			Toast.makeText(this, "Calendar " + displayName, Toast.LENGTH_SHORT)
					.show();
		}
	}
}

/*
import java.sql.Date;
import java.text.Format;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;

import android.R.bool;
import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.text.format.DateFormat;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

public class CalendarSyncAdapterActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_calendar_syncadapter);
		Button qry = (Button) findViewById(R.id.querybut);
	//	ListView dropdown = (ListView) findViewById(R.id.listCalendarEvents);
		query();

		qry.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// query();
			}
		});
	}

	public void query() {
		String[] projection = new String[] {
				CalendarContract.Events.CALENDAR_ID,
				CalendarContract.Events.TITLE,
				CalendarContract.Events.DESCRIPTION,
				CalendarContract.Events.DTSTART, CalendarContract.Events.DTEND,
				CalendarContract.Events.ALL_DAY,
				CalendarContract.Events.EVENT_LOCATION };

		// 0 = January, 1 = February, ...

		Calendar startTime = Calendar.getInstance();
		startTime.set(2014, 00, 01, 00, 00);

		Calendar endTime = Calendar.getInstance();
		endTime.set(2015, 00, 01, 00, 00);

		// the range is all data from 2014

		String selection = "(( " + CalendarContract.Events.DTSTART + " >= "
				+ startTime.getTimeInMillis() + " ) AND ( "
				+ CalendarContract.Events.DTSTART + " <= "
				+ endTime.getTimeInMillis() + " ))";

		Cursor cursor = this
				.getBaseContext()
				.getContentResolver()
				.query(CalendarContract.Events.CONTENT_URI, projection,
						selection, null, null);

		// output the events

		ArrayList<String> CalendarEvents = new ArrayList<String>();

		while (cursor.moveToNext()) {

			boolean islast = cursor.isLast();
			if (cursor == null || islast == true) {
				break;
			}
			Format df = DateFormat.getDateFormat(this);
			Format tf = DateFormat.getTimeFormat(this);

			int nameIndex = cursor.getInt(0);
			String name = " ";
			long dateTime = 0;
			
			if (cursor.isNull(nameIndex)) {
				break;
			}
			name = cursor.getString(nameIndex);
		    dateTime = cursor.getLong(1);
			// String dateTimeToStrig = df.format(dateTime);

			String contentToDispay = name + " on " + df.format(dateTime)
					+ " at " + tf.format(dateTime);

			CalendarEvents.add(contentToDispay);
		}

		ListView dropdown = (ListView) findViewById(R.id.listCalendarEvents);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, android.R.id.text1,
				CalendarEvents);
		dropdown.setAdapter(adapter);

		if (cursor.moveToFirst()) {
			do {
				Toast.makeText(
						this.getApplicationContext(),
						"Title: " + cursor.getString(1) + " Start-Time: "
								+ (new Date(cursor.getLong(3))).toString(),
						Toast.LENGTH_LONG).show();
			} while (cursor.moveToNext());
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
*/