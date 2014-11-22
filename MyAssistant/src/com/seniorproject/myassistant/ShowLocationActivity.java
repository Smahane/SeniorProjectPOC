package com.seniorproject.myassistant;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import android.app.Activity;
import android.content.Context;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

public class ShowLocationActivity extends Activity implements LocationListener {
	private TextView m_latituteField;
	private TextView m_longitudeField;
	private TextView m_address;

	private double m_lattitude;
	private double m_longitude;

	private LocationManager m_getLocationManager;
	private Geocoder m_georecorder;

	private List<Address> m_addresses;
	private String m_provider;

	/** Called when the activity is first created. */

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_show_location);

		m_latituteField = (TextView) findViewById(R.id.TextView02);
		m_longitudeField = (TextView) findViewById(R.id.TextView04);
		m_address = (TextView) findViewById(R.id.address);

		m_getLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE); // Get
																								// the
																								// location
																								// manager

		Criteria criteria = new Criteria(); // Define the criteria how to select
											// the location provider -> use
		m_provider = m_getLocationManager.getBestProvider(criteria, false);

		Location location = getLastKnownLocation();

		// Initialize the location fields
		if (location != null) {
			System.out
					.println("Provider " + m_provider + " has been selected.");
			onLocationChanged(location);
		} else {
			m_latituteField.setText("Location not available");
			m_longitudeField.setText("Location not available");
		}
	}

	private Location getLastKnownLocation() {
		m_getLocationManager = (LocationManager) getApplicationContext()
				.getSystemService(LOCATION_SERVICE);
		List<String> providers = m_getLocationManager.getProviders(true);
		Location bestLocation = null;
		for (String provider : providers) {
			Location l = m_getLocationManager.getLastKnownLocation(provider);
			if (l == null) {
				continue;
			}
			if (bestLocation == null
					|| l.getAccuracy() < bestLocation.getAccuracy()) {
				// Found best last known location: %s", l);
				bestLocation = l;
			}
		}
		return bestLocation;
	}

	
	/* Request updates at startup */
	@Override
	protected void onResume() {
		super.onResume();
		m_getLocationManager.requestLocationUpdates(m_provider, 400, 1, this);
	}

	/* Remove the locationlistener updates when Activity is paused */
	@Override
	protected void onPause() {
		super.onPause();
		m_getLocationManager.removeUpdates(this);
	}

	@Override
	public void onLocationChanged(Location location) {
		m_lattitude = (int) (location.getLatitude());
		m_longitude = (int) (location.getLongitude());
		m_latituteField.setText(String.valueOf(m_lattitude));
		m_longitudeField.setText(String.valueOf(m_longitude));

		try {
			m_georecorder = new Geocoder(ShowLocationActivity.this,
					Locale.ENGLISH);
			m_addresses = m_georecorder.getFromLocation(m_lattitude,
					m_longitude, 1);
			StringBuilder strBuilder = new StringBuilder();

			if (Geocoder.isPresent()) {
				Toast.makeText(getApplicationContext(), "geocoder present",
						Toast.LENGTH_SHORT).show();
				Address getAddress = m_addresses.get(0);

			//	String locality = getAddress.getLocality();
				String city = getAddress.getCountryName();
				String countryCode = getAddress.getCountryCode();
				String zipCode = getAddress.getPostalCode();
				String addressLine = getAddress.getFeatureName();
				String state = getAddress.getAdminArea();
				
				strBuilder.append(addressLine + " ");
				strBuilder.append(city + " ");
				strBuilder.append(state + " ");
				strBuilder.append(countryCode + " ");
				strBuilder.append(zipCode + " ");
				
				//strBuilder.append(getAddress);

				m_address.setText(strBuilder);

				Toast.makeText(getApplicationContext(), strBuilder,
						Toast.LENGTH_SHORT).show();
			} else {
				Toast.makeText(getApplicationContext(), "geocoder not present",
						Toast.LENGTH_SHORT).show();
			}
		} catch (IOException e) {
			Log.e("tag", e.getMessage());
		}
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onProviderEnabled(String provider) {
		Toast.makeText(this, "Enabled new provider " + provider,
				Toast.LENGTH_SHORT).show();

	}

	@Override
	public void onProviderDisabled(String provider) {
		Toast.makeText(this, "Disabled provider " + provider,
				Toast.LENGTH_SHORT).show();
	}
}