package com.example.aplication;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.ImageView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.RelativeLayout;

public class OtherInfoActivity extends Activity 
{
	
	public static final String PREF_COLOR = "pref_color";
	public static final String PREF_IMAGE = "pref_image";
	
	private RelativeLayout layout;
	private ImageView imageView;
	
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.other_info);
		
		
		layout = (RelativeLayout) findViewById(R.id.RelativeLayout1);
        imageView = (ImageView) findViewById(R.id.imageView1);
		 
	}
	
	public void onStart()
	{
		super.onStart();
		
		SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        
        String colorPreference = sharedPref.getString(SettingsActivityLtF.PREF_COLOR, "white");
        boolean onOffImage = sharedPref.getBoolean(SettingsActivityLtF.PREF_IMAGE, false);
        
        changeBackgroundColor(colorPreference);
        toggleActivityImage(onOffImage);
	}
	
	private void toggleActivityImage(boolean onOff) {
		if(onOff) {
			imageView.setBackgroundDrawable(getResources().getDrawable(R.drawable.ic_launcher));
		}
		else {
			SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
	        String newColor = sharedPref.getString(SettingsActivityLtF.PREF_COLOR, "white");
	        if(newColor.equalsIgnoreCase("black")) {
	    		imageView.setBackgroundColor(getResources().getColor(android.R.color.black));
	    	}
	    	else {
	    		imageView.setBackgroundColor(getResources().getColor(android.R.color.white));
	    	}
		}
	}
    
    private void changeBackgroundColor(String newColor) {
    	if(newColor.equalsIgnoreCase("black")) {
    		layout.setBackgroundColor(getResources().getColor(android.R.color.black));
    	}
    	else {
    		layout.setBackgroundColor(getResources().getColor(android.R.color.white));
    	}
	}
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.preferences_menu, menu);
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	switch (item.getItemId()) {
		case R.id.menu_settings:
			Intent intent = new Intent(this, SettingsActivityLtF.class);
			startActivity(intent);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
    }
}
