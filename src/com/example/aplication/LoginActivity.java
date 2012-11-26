
package com.example.aplication;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
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

import com.example.aplication.Facebook.AsyncFacebookRunner;
import com.example.aplication.Facebook.Facebook;
import com.example.aplication.Facebook.FacebookError;
import com.example.aplication.Facebook.LoginButton;
import com.example.aplication.Facebook.SessionEvents;
import com.example.aplication.Facebook.SessionStore;
import com.example.aplication.Facebook.Util;
import com.example.aplication.Facebook.AsyncFacebookRunner.RequestListener;
import com.example.aplication.Facebook.SessionEvents.AuthListener;
import com.example.aplication.Facebook.SessionEvents.LogoutListener;
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
	
	/*
	 * componentes del boton y etiqueta para login con facebook
	 * 
	 */
	private TextView txtFbStatus;
	private boolean  facebook_active = false;
	private AsyncFacebookRunner mAsyncRunner;
	public static final String APP_ID = "264102133708872";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		
		/*
		 * inicializacion de los componentes y otros atributos necesarios
		 */
		
		LoginButton mLoginButton = (LoginButton) findViewById(R.id.btnFbLogin);
		Facebook mFacebook = new Facebook(APP_ID);
		mAsyncRunner = new AsyncFacebookRunner(mFacebook);
		txtFbStatus = (TextView) this.findViewById(R.id.txtFbStatus);
		SessionStore.restore(mFacebook, this);
		facebook_active = mFacebook.isSessionValid();
		
		// fin de la inicializacion
		
		/*
		 * metodos y escuchas para funcionamiento del boton y login 
		 * mas mensajes de error 
		 */
		
		if (facebook_active) {
		    updateFbStatus();
		}
		SessionEvents.addAuthListener(new AuthListener() {
		    public void onAuthSucceed() {
		        updateFbStatus();
		    }
		 
		    
		    public void onAuthFail(String error) {
		        txtFbStatus.setText("Facebook status: imposible iniciar sesión " + error);
		    }
		});
		
		SessionEvents.addLogoutListener(new LogoutListener() {
	      
			public void onLogoutFinish() {
		    txtFbStatus.setText("Facebook status: sesión no iniciada");
			}
			
			public void onLogoutBegin() {
			txtFbStatus.setText("Facebook status: cerrando sesión...");
			}
			});
		mLoginButton.init(this, mFacebook);
		
		//fin metodos necesarios dentrod e onCreate()
		
		
		
		
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
		
		/*
		 * 
		 */

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
	
	/*
	 * metodo updateFbStatus 
	 * relativo a funcionamiento de login con facebook
	 * @see android.app.Activity#onStart()
	 */
	
	private void updateFbStatus(){
	    mAsyncRunner.request("me", new RequestListener() {
	        
	        public void onMalformedURLException(MalformedURLException e, Object state) {}
	 
	       
	        public void onIOException(IOException e, Object state) {}
	 
	        
	        public void onFileNotFoundException(FileNotFoundException e, Object state) {}
	 
	        
	        public void onFacebookError(FacebookError e, Object state) {}
	 
	        
	        public void onComplete(String response, Object state) {
	             try {
	                    JSONObject json = Util.parseJson(response);
	                    final String id = json.getString("id");
	                    final String name = json.getString("name");
	                    LoginActivity.this.runOnUiThread(new Runnable() {
	 
	                        public void run() {
	                            txtFbStatus.setText("Facebook status: sesión iniciada como " + name + " con el id " + id);
	                        }
	                    });
	                } catch (JSONException e) {
	                    e.printStackTrace();
	                } catch (FacebookError e) {
	                    e.printStackTrace();
	                }
	        }
	    });
	 
	}
	
	//fin del metodo
	
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
