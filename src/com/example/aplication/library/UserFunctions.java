
package com.example.aplication.library;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import android.content.Context;

public class UserFunctions {
	
	private JSONParser jsonParser;
	
	private static String loginURL = "http://10.0.2.2/android_login_api/";
	private static String registerURL = "http://10.0.2.2/android_login_api/";
	
	private static String login_tag = "login";
	private static String register_tag = "register";
	
	// constructor
	public UserFunctions(){
		jsonParser = new JSONParser();
	}
	
	/**
	 * function make Login Request
	 * @param email
	 * @param clave
	 * */
	public JSONObject loguearseUsuario(String email, String clave){
		// Building Parameters
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("tag", login_tag));
		params.add(new BasicNameValuePair("email", email));
		params.add(new BasicNameValuePair("password", clave));
		JSONObject json = jsonParser.getJSONDeUrl(loginURL, params);
		// return json
		// Log.e("JSON", json.toString());
		return json;
	}
	
	/**
	 * function make Login Request
	 * @param nombre
	 * @param email
	 * @param clave
	 * */
	public JSONObject registrarUsuario(String nombre, String email, String clave){
		// Building Parameters
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("tag", register_tag));
		params.add(new BasicNameValuePair("name", nombre));
		params.add(new BasicNameValuePair("email", email));
		params.add(new BasicNameValuePair("password", clave));
		
		// getting JSON Object
		JSONObject json = jsonParser.getJSONDeUrl(registerURL, params);
		// return json
		return json;
	}
	
	/**
	 * Function get Login status
	 * */
	public boolean isUsuarioLogueado(Context context){
		DatabaseHandler db = new DatabaseHandler(context);
		int contador = db.getCantidadDeRegistros();
		if(contador > 0){
			// user logged in
			return true;
		}
		return false;
	}
	
	/**
	 * Function to logout user
	 * Reset Database
	 * */
	public boolean cerrarSesion(Context contexto){
		DatabaseHandler db = new DatabaseHandler(contexto);
		db.resetearTablas();
		return true;
	}
	
}
