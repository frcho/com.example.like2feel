package com.example.aplication;

import java.util.ArrayList;

//import android.app.Activity;
import android.app.ListActivity;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.ContactsContract.PhoneLookup;
import android.util.Log;
import android.widget.ArrayAdapter;

public class ContactosListActivity extends ListActivity 
{
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.inbox_list);
		
		ArrayList<String> nombrescontacts = new ArrayList<String>();
		Cursor contactos = getContentResolver().query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
		
		while(contactos.moveToNext())
		{
			int nombreIndice = contactos.getColumnIndex(PhoneLookup.DISPLAY_NAME);
			nombrescontacts.add(contactos.getString(nombreIndice));
			Log.d("Contactos",contactos.getString(nombreIndice));
		}
		
		String[] nombres = new String[nombrescontacts.size()];
		
		for(int i=0;i<nombrescontacts.size();i++)
		{
			nombres[i]=nombrescontacts.get(i);
		}
		
		setListAdapter(new ArrayAdapter<String>(this,R.layout.inbox_list_item, nombres));
	}

}
