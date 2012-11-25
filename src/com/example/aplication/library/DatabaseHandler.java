
package com.example.aplication.library;

import java.util.HashMap;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHandler extends SQLiteOpenHelper {

	// All Static variables
	// Database Version
	private static final int DATABASE_VERSION = 1;

	// Database Name
	private static final String DATABASE_NAME = "android_api";

	// Login table name
	private static final String TABLE_LOGIN = "login";

	// Login Table Columns names
	private static final String KEY_ID = "id";
	private static final String KEY_NAME = "name";
	private static final String KEY_EMAIL = "email";
	private static final String KEY_UID = "uid";
	private static final String KEY_CREATED_AT = "created_at";

	public DatabaseHandler(Context contexto) {
		super(contexto, DATABASE_NAME, null, DATABASE_VERSION);
	}

	// Creating Tables
	@Override
	public void onCreate(SQLiteDatabase db) {
		String CREATE_LOGIN_TABLE = "CREATE TABLE " + TABLE_LOGIN + "("
				+ KEY_ID + " INTEGER PRIMARY KEY," 
				+ KEY_NAME + " TEXT,"
				+ KEY_EMAIL + " TEXT UNIQUE,"
				+ KEY_UID + " TEXT,"
				+ KEY_CREATED_AT + " TEXT" + ")";
		db.execSQL(CREATE_LOGIN_TABLE);
	}

	// Upgrading database
	@Override
	public void onUpgrade(SQLiteDatabase db, int versionAnt, int versionNue) {
		// Drop older table if existed
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_LOGIN);

		// Create tables again
		onCreate(db);
	}

	/**
	 * Storing user details in database
	 * */
	public void registrarUsuario(String nombre, String email, String uid, String creado_en) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues valores = new ContentValues();
		valores.put(KEY_NAME, nombre); // Name
		valores.put(KEY_EMAIL, email); // Email
		valores.put(KEY_UID, uid); // Email
		valores.put(KEY_CREATED_AT, creado_en); // Created At

		// Inserting Row
		db.insert(TABLE_LOGIN, null, valores);
		db.close(); // Closing database connection
	}
	
	/**
	 * Getting user data from database
	 * */
	public HashMap<String, String> getDetalleDeUsuarios(){
		HashMap<String,String> usuario = new HashMap<String,String>();
		String selectQuery = "SELECT  * FROM " + TABLE_LOGIN;
		 
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // Move to first row
        cursor.moveToFirst();
        if(cursor.getCount() > 0){
        	usuario.put("name", cursor.getString(1));
        	usuario.put("email", cursor.getString(2));
        	usuario.put("uid", cursor.getString(3));
        	usuario.put("created_at", cursor.getString(4));
        }
        cursor.close();
        db.close();
		// return user
		return usuario;
	}

	/**
	 * Getting user login status
	 * return true if rows are there in table
	 * */
	public int getCantidadDeRegistros() {
		String contarRegistrosQuery = "SELECT  * FROM " + TABLE_LOGIN;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(contarRegistrosQuery, null);
		int cantidadRegistros = cursor.getCount();
		db.close();
		cursor.close();
		
		// return row count
		return cantidadRegistros;
	}
	
	/**
	 * Re crate database
	 * Delete all tables and create them again
	 * */
	public void resetearTablas(){
		SQLiteDatabase db = this.getWritableDatabase();
		// Delete All Rows
		db.delete(TABLE_LOGIN, null, null);
		db.close();
	}

}
