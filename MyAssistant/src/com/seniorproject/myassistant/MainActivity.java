package com.seniorproject.myassistant;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TabHost;


@SuppressWarnings("deprecation")
public class MainActivity extends TabActivity {

	private TabHost mTabHost;
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        mTabHost = getTabHost();
        TabHost.TabSpec spec;
        
        //Service Volume Activity
        Intent intent;
        
        intent = new Intent(this, ServiceVolumeActivity.class);
        spec = mTabHost.newTabSpec("ServiceVulome").setIndicator("Service Volume").setContent(intent);
        
        mTabHost.addTab(spec);
        
        //Duplicate the same for other tabs here
              
        intent = new Intent(this, CalendarEventActivity.class);
        spec = mTabHost.newTabSpec("Calendar Event").setIndicator("Calendar Event").setContent(intent);
        
        mTabHost.addTab(spec);
        
       /* intent = new Intent(this, CalendarSyncAdapterActivity.class);
        spec = mTabHost.newTabSpec(" Calendar syncAdap").setIndicator("Calendar SyncAdap").setContent(intent);
        
        mTabHost.addTab(spec);
        */
        
        //Duplicate the same for other tabs here
        
        intent = new Intent(this, ShowLocationActivity.class);
        spec = mTabHost.newTabSpec(" Location base service").setIndicator("Location base service").setContent(intent);
        
        mTabHost.addTab(spec);
        
        intent = new Intent(this, ContactListActivity.class);
        spec = mTabHost.newTabSpec(" Contact List").setIndicator("Contact List").setContent(intent);
        
        mTabHost.addTab(spec);
        
      
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
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
