
package com.example.aplication;

import org.json.JSONException;
import org.json.JSONObject;

import com.example.aplication.library.DatabaseHandler;
import com.example.aplication.library.UserFunctions;

import android.app.Activity;
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

public class RegisterActivity extends Activity {
	Button btnRegistrar;
	Button btnIrALogin;
	Button extraInfo;
	EditText inputNombre;
	EditText inputEmail;
	EditText inputClave;
	TextView registroErrorMsg;
	
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
		setContentView(R.layout.register);
		
		lblMensaje = (TextView)findViewById(R.id.textView3);

		// Importing all assets like buttons, text fields
		inputNombre = (EditText) findViewById(R.id.registerName);
		inputEmail = (EditText) findViewById(R.id.registerEmail);
		inputClave = (EditText) findViewById(R.id.registerPassword);
		btnRegistrar = (Button) findViewById(R.id.btnRegister);
		//extraInfo = (Button) findViewById(R.id.button1);
		btnIrALogin = (Button) findViewById(R.id.btnLinkToLoginScreen);
		registroErrorMsg = (TextView) findViewById(R.id.register_error);
		
		layout = (LinearLayout) findViewById(R.id.LinearLay);
		
		// Register Button Click event
		btnRegistrar.setOnClickListener(new View.OnClickListener() {			
			public void onClick(View view) {
				String nombre = inputNombre.getText().toString();
				String email = inputEmail.getText().toString();
				String clave = inputClave.getText().toString();
				UserFunctions userFunction = new UserFunctions();
					
				Log.i("Nombre.legth: ",""+nombre.length());
				Log.i("Email.legth: ",""+email.length());
				Log.i("Clave.legth: ",""+clave.length());
				
				if(!(comprovarEspacio(nombre)||comprovarEspacio(email)||comprovarEspacio(clave))&&nombre.length()!=0&&email.length()!=0&&clave.length()!=0)
				{
					Log.d("Empty Spaces","Entro!");
					JSONObject json = userFunction.registrarUsuario(nombre, email, clave);
					// check for login response
					try {
						if (json.getString(KEY_SUCCESS) != null) {
							registroErrorMsg.setText("");
							String respuesta = json.getString(KEY_SUCCESS); 
							if(Integer.parseInt(respuesta) == 1){
								// user successfully registred
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
								// Close Registration Screen
								finish();
							}else{
								// Error in registration
								registroErrorMsg.setText("A ocurrido un error al intentar registrar!");
							}
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
				else
				{
					Log.d("Empty Spaces","No Entro!");
					//userFunction.cerrarSesion(getApplicationContext());
					registroErrorMsg.setText("No has escrito nada!");
				}
			}
		});

		// Link to Login Screen
		btnIrALogin.setOnClickListener(new View.OnClickListener() {

			public void onClick(View view) {
				Intent i = new Intent(getApplicationContext(),
						LoginActivity.class);
				
				startActivity(i);
				// Close Registration View
				
			}
		});
		/*extraInfo.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				Intent i = new Intent(getApplicationContext(),OtherInfoActivity.class);
				startActivity(i);
				
			}
		});*/
	}
	
	@Override
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
        inflater.inflate(R.menu.register_menu, menu);
       
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
	
	public boolean comprovarEspacio(String cadena)
	{
		boolean esEspacio = false;
		
		if(cadena.length()>0)
		{
			for (int i = 0; i < cadena.length(); i++)
			{
				if (Character.isSpace(cadena.charAt(i))||Character.isSpaceChar(cadena.charAt(i))||Character.isWhitespace(cadena.charAt(i))) 
		        {
					esEspacio = true;
		        } 
		    }
		}		
		return esEspacio;
	}
}
