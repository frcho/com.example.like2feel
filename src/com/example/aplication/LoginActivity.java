
package com.example.aplication;

import java.util.Calendar;

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
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.aplication.library.DatabaseHandler;
import com.example.aplication.library.UserFunctions;



public class LoginActivity extends Activity {
	Button btnLoguear;
	Button btnIrARegistrar;
	EditText inputEmail;
	EditText inputClave;
	TextView loguearErrorMsg;
	private static PendingIntent pendingIntent;
	
	private TextView lblMensaje;

	// JSON Response node names
	private static String KEY_SUCCESS = "success";
	//private static String KEY_ERROR = "error";
	//private static String KEY_ERROR_MSG = "error_msg";
	private static String KEY_UID = "uid";
	private static String KEY_NAME = "name";
	private static String KEY_EMAIL = "email";
	private static String KEY_CREATED_AT = "created_at";
	
	private LinearLayout layout;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		
		lblMensaje = (TextView)findViewById(R.id.textView1);

		// Importing all assets like buttons, text fields
		inputEmail = (EditText) findViewById(R.id.loginEmail);
		inputClave = (EditText) findViewById(R.id.loginPassword);
		btnLoguear = (Button) findViewById(R.id.btnLogin);
		btnIrARegistrar = (Button) findViewById(R.id.btnLinkToRegisterScreen);
		loguearErrorMsg = (TextView) findViewById(R.id.login_error);
		
		layout = (LinearLayout) findViewById(R.id.LinearLayo);

		// Login button Click Event
		btnLoguear.setOnClickListener(new View.OnClickListener() {

			public void onClick(View view) {
				String email = inputEmail.getText().toString();
				String clave = inputClave.getText().toString();
				UserFunctions userFunction = new UserFunctions();
				Log.d("Button", "Login");
				// check for login response
				
				if(email.length()>0&&clave.length()>0)
				{
					JSONObject json = userFunction.loguearseUsuario(email, clave);
					try {
						if (json.getString(KEY_SUCCESS) != null) {
							loguearErrorMsg.setText("");
							
							String respuesta = json.getString(KEY_SUCCESS); 
							if(Integer.parseInt(respuesta) == 1){
								// user successfully logged in
								// Store user details in SQLite Database
								DatabaseHandler db = new DatabaseHandler(getApplicationContext());
								JSONObject json_user = json.getJSONObject("user");
								
								// Clear all previous data in database
								userFunction.cerrarSesion(getApplicationContext());
								db.registrarUsuario(json_user.getString(KEY_NAME), json_user.getString(KEY_EMAIL), json.getString(KEY_UID), json_user.getString(KEY_CREATED_AT));						
								
								// Launch Dashboard Screen
								Intent dashboard = new Intent(getApplicationContext(), DashboardActivity.class);
								
								// Close all views before launching Dashboard
								dashboard.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
								startActivity(dashboard);
								
								// Close Login Screen
								finish();
								ActivarMensaje();
							}else{
								// Error in login
								loguearErrorMsg.setText("Usuario/password incorrecto!");
							}
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
				else
				{
					//userFunction.cerrarSesion(getApplicationContext());
					loguearErrorMsg.setText("No has escrito nada!");
				}
					
			}
		});

		// Link to Register Screen
		btnIrARegistrar.setOnClickListener(new View.OnClickListener() {

			public void onClick(View view) {
				Intent i = new Intent(getApplicationContext(),
						RegisterActivity.class);
				startActivity(i);
				finish();
			}
		});
	}
	
	protected void onStart() 
	{
		super.onStart();
			
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        
        String colorPreference = sharedPref.getString(SettingsActivityLtF.PREF_COLOR, "white");
       
		Log.d("RegisterColor","Color por defecto: "+colorPreference);
		changeBackgroundColor(colorPreference);
	}
	
	private void changeBackgroundColor(String newColor) 
	{
    	if(newColor.equalsIgnoreCase("White")) 
    	{
    		layout.setBackgroundColor(getResources().getColor(android.R.color.white));
    		Log.d("RegisterColor","Color seleccionado: "+newColor);
    	}
    	else 
    	{
    		if(newColor.equalsIgnoreCase("Black"))
    		{
    			layout.setBackgroundColor(getResources().getColor(android.R.color.black));
    			Log.d("RegisterColor","Color seleccionado: "+newColor);
    		}
    		else
    		{
    			layout.setBackgroundColor(getResources().getColor(android.R.color.darker_gray));
    			Log.d("RegisterColor","Color seleccionado: "+newColor);
    		}
    	}
	}
	
	@Override
    public boolean onCreateOptionsMenu(Menu menu) 
	{
    	
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.login_menu, menu);
       
        return true;
    }
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) 
	{
		switch (item.getItemId()) 
		{
	        case R.id.opc1:
	            lblMensaje.setText("Opcion 1 pulsada!");
	            return true;
	        case R.id.opc2:
	        	lblMensaje.setText("Opcion 2 pulsada!");
	        	Intent intent = new Intent(this, SettingsActivityLtF.class);
				startActivity(intent);
	            return true;
	        default:
	            return super.onOptionsItemSelected(item);
        }
    }
	
	private void ActivarMensaje() 
	{
		int comprobacionIntervaloSegundos = 30;
    	   Log.i("mensaje","entroooooooo");
		   Intent myIntent = new Intent(LoginActivity.this, NotificacionService.class);
		   pendingIntent = PendingIntent.getService(LoginActivity.this, 0, myIntent, 0);

		   AlarmManager alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);

		   Calendar calendar = Calendar.getInstance();
		   calendar.setTimeInMillis(System.currentTimeMillis());
		   calendar.add(Calendar.SECOND, 10);
		   alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), comprobacionIntervaloSegundos * 1000, pendingIntent);

		   
		   
		   Toast.makeText(LoginActivity.this, "Alarma iniciada", Toast.LENGTH_LONG).show();
		// TODO Auto-generated method stub
		
	}
}
