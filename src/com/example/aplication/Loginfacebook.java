package com.example.aplication;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;

import org.json.JSONException;
import org.json.JSONObject;

import com.example.aplication.Facebook.AsyncFacebookRunner;
import com.example.aplication.Facebook.AsyncFacebookRunner.RequestListener;
import com.example.aplication.Facebook.Facebook;
import com.example.aplication.Facebook.FacebookError;
import com.example.aplication.Facebook.LoginButton;
import com.example.aplication.Facebook.SessionEvents;
import com.example.aplication.Facebook.SessionEvents.AuthListener;
import com.example.aplication.Facebook.SessionEvents.LogoutListener;
import com.example.aplication.Facebook.SessionStore;
import com.example.aplication.Facebook.Util;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class Loginfacebook extends Activity 
{
	private TextView txtFbStatus;
	private boolean  facebook_active = false;
	private AsyncFacebookRunner mAsyncRunner;
	public static final String APP_ID = "264102133708872";
	
	/*
	 * prueba teorica de funcionalidad github
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		LoginButton mLoginButton = (LoginButton) findViewById(R.id.btnFbLogin);
		Facebook mFacebook = new Facebook(APP_ID);
		mAsyncRunner = new AsyncFacebookRunner(mFacebook);
		txtFbStatus = (TextView) this.findViewById(R.id.txtFbStatus);
		SessionStore.restore(mFacebook, this);
		facebook_active = mFacebook.isSessionValid();
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
		
	}
		
		
		
	
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
	                    Loginfacebook.this.runOnUiThread(new Runnable() {
	 
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
}
