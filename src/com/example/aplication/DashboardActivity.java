package com.example.aplication;



import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.aplication.Facebook.AsyncFacebookRunner;
import com.example.aplication.Facebook.Facebook;
import com.example.aplication.Facebook.FacebookError;
import com.example.aplication.Facebook.LoginButton;
import com.example.aplication.Facebook.SessionEvents;
import com.example.aplication.Facebook.SessionStore;
import com.example.aplication.Facebook.Util;
import com.example.aplication.Facebook.AsyncFacebookRunner.RequestListener;
import com.example.aplication.Facebook.SessionEvents.LogoutListener;
import com.example.aplication.library.UserFunctions;


public class DashboardActivity extends Activity 
{
	UserFunctions funcionesDeUsuario;
	Button btnSalir;
	Button btnImagen;
	private RelativeLayout layout;
	private static PendingIntent pendingIntent;
	
	
    
	@Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard);
        //empesarCuentaNotificacion();
        
        
        
        

		

		
        /**
         * Dashboard Screen for the application
         * */        
        // Check login status in database
        
        layout = (RelativeLayout)findViewById(R.id.RelativeLayy);
        Log.d("RelativeId",""+layout.getId());
        
        funcionesDeUsuario = new UserFunctions();
        if(funcionesDeUsuario.isUsuarioLogueado(getApplicationContext())){
        	//setContentView(R.layout.dashboard);
        	btnSalir = (Button) findViewById(R.id.btnLogout);
        	btnImagen= (Button) findViewById(R.id.btnImg);
        	
        	btnSalir.setOnClickListener(new View.OnClickListener() {
    			
    			public void onClick(View arg0) {
    				// TODO Auto-generated method stub
    				funcionesDeUsuario.cerrarSesion(getApplicationContext());
    				DesactivarMensaje();
    				Intent logueo = new Intent(getApplicationContext(), LoginActivity.class);
    	        	logueo.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
    	        	startActivity(logueo);
    	        	// Closing dashboard screen
    	        	//finish();
    			}
    		});
        	
        	btnImagen.setOnClickListener(new View.OnClickListener(){

				public void onClick(View v) {
					// TODO Auto-generated method stub
					
    				Intent verF = new Intent(getApplicationContext(), CargarImagenes.class);
    	        	verF.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
    	        	startActivity(verF);
    	        	// Closing dashboard screen
    	        	//finish();
					
				}
        		
        	});
        	
        }else{
        	// user is not logged in show login screen
        	Intent logueo = new Intent(getApplicationContext(), LoginActivity.class);
        	logueo.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        	startActivity(logueo);
        	// Closing dashboard screen
        	finish();
        }
    }
	
  /*  public void empesarCuentaNotificacion()
    {
    	if (pendingIntent == null)
    	{
    		//La alarma est‡ desactivada, la activamos
    		ActivarMensaje();
    	}else
    	{
    		//La alarma est‡ activada, la desactivamos
    		DesactivarMensaje();
    	}
    }*/
    
    public boolean onCreateOptionsMenu(Menu menu) 
	{
    	
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.cargarimagenes_menu, menu);
       
        return true;
    }
    
	public boolean onOptionsItemSelected(MenuItem item) 
	{
		switch (item.getItemId()) 
		{
	        case R.id.opc1:
	            
	            return true;
	        case R.id.opc2:
	        	
	        	Intent intent = new Intent(this, SettingsActivityLtF.class);
				startActivity(intent);
	            return true;
	        default:
	            return super.onOptionsItemSelected(item);
        }
    }
    
    @Override
    protected void onStart() 
	{
		super.onStart();
			
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        
        String colorPreference = sharedPref.getString(SettingsActivityLtF.PREF_COLOR,"White");
       
		Log.d("DashColor","Color por defecto: "+colorPreference);
		changeBackgroundColor(colorPreference);
	}
    
    private void changeBackgroundColor(String newColor) 
	{
    	if(newColor.equalsIgnoreCase("White")) 
    	{
    		layout.setBackgroundColor(getResources().getColor(android.R.color.white));
    		Log.d("DashColor","Color seleccionado: "+newColor);
    	}
    	else 
    	{
    		if(newColor.equalsIgnoreCase("Black"))
    		{
    			layout.setBackgroundColor(getResources().getColor(android.R.color.black));
    			Log.d("DashColor","Color seleccionado: "+newColor);
    		}
    		else
    		{
    			layout.setBackgroundColor(getResources().getColor(android.R.color.darker_gray));
    			Log.d("DashColor","Color seleccionado: "+newColor);
    		}
    	}
	}
    
	private void DesactivarMensaje()
	{	
		
		 AlarmManager alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);
		 alarmManager.cancel(pendingIntent);
		 
		 Toast.makeText(DashboardActivity.this, "Notificaciones detenidas", Toast.LENGTH_LONG).show();
		 try 
		 {
			finalize();
			Log.i("psss","siiiiiiiiiiii");
		 } 
		 catch (Throwable e) 
		 {
			// TODO Auto-generated catch block
			e.printStackTrace();
		 }
	 }
}