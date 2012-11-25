package com.example.aplication;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
//import android.view.SubMenu;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
//import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
//import android.widget.AdapterView.AdapterContextMenuInfo;

public class CargarImagenes extends Activity 
{
	private RelativeLayout layout;
	
	private TextView lblMensaje;
	private ImageView imageView1;
	
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.presentacion);
		Log.d("Llegamos: ",":)");
		
		lblMensaje = (TextView)findViewById(R.id.textView1);
		
		imageView1 = (ImageView)findViewById(R.id.imageView1);
		
		layout = (RelativeLayout) findViewById(R.id.RelativeLay);
		
		registerForContextMenu(imageView1);
		
		new cargarImageTask().execute();
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
        inflater.inflate(R.menu.cargarimagenes_menu, menu);
       
        return true;
    }
	
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, 
	    		                        ContextMenuInfo menuInfo) 
	{
		super.onCreateContextMenu(menu, v, menuInfo);
			
		MenuInflater inflater = getMenuInflater();
			
		if(v.getId() == R.id.imageView1)
			inflater.inflate(R.menu.menucontextual_imagen, menu);
	
	}
	
	@Override
	public boolean onContextItemSelected(MenuItem item) 
	{

			String concat = "";
	    	
			switch (item.getItemId()) {
				case R.id.copc1:
					lblMensaje.setText("Enviar a contacto");
					Intent i = new Intent(getApplicationContext(),ContactosListActivity.class);
					startActivity(i);
					return true;
				case R.id.copc2:
					
					concat+="Otras imagenes";
					Intent j = new Intent(getApplicationContext(),GridImagenesDisponible.class);
					startActivity(j);
					lblMensaje.setText(concat);
					return true;
					
				default:
					return super.onContextItemSelected(item);
			}
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
	
	private class cargarImageTask extends AsyncTask <Void,Void,Integer>
	{
		ProgressDialog pd = null;
		int image=-1;
    	
    	protected void onPreExecute()
    	{
    		pd = ProgressDialog.show(CargarImagenes.this,
    								 "Cargango Imagen", 
    								 "... y cual es tu animo hoy?");
    	}

		@Override
		protected Integer doInBackground(Void... arg0) 
		{
			Log.d("Entre al doIn! ","");
			try 
			{
				Thread.sleep(3000);
			} 
			catch (InterruptedException e) 
			{
				Log.d("Error sleep: ",""+e.getMessage());
				e.printStackTrace();
			}
			
			while(image<0)
			{
				image=Integer.parseInt(""+(int)(Math.random()*7));
				
				if(image<=8)
				{
					break;
				}
			}
			
			Log.d("Final doIn! ","Image: "+(image-1));
			return new Integer(image);
		}
		
		@Override
		protected void onPostExecute(Integer i)
		{
			pd.dismiss();
			int id=0;
			Log.d("En el post! ","Image#: "+(i.intValue()-1));
			
			ImageView paper = (ImageView)findViewById(R.id.imageView1);
	        Resources res = getResources();
	        
	        switch (i.intValue())
	        {
	        	case 0:
	        	{
	        		id=getResources().getIdentifier("abrazo","drawable",getPackageName());
	        		break;
	        	}
	        	case 1:
	        	{
	        		id=getResources().getIdentifier("image_0","drawable",getPackageName());
	        		break;
	        	}
	        	case 2:
	        	{
	        		id=getResources().getIdentifier("image_1","drawable",getPackageName());
	        		break;
	        	}
	        	case 3:
	        	{
	        		id=getResources().getIdentifier("image_2","drawable",getPackageName());
	        		break;
	        	}
	        	case 4:
	        	{
	        		id=getResources().getIdentifier("image_3","drawable",getPackageName());
	        		break;
	        	}
	        	case 5:
	        	{
	        		id=getResources().getIdentifier("image_4","drawable",getPackageName());
	        		break;
	        	}
	        	case 6:
	        	{
	        		id=getResources().getIdentifier("image_5","drawable",getPackageName());
	        		break;
	        	}
	        	case 7:
	        	{
	        		id=getResources().getIdentifier("image_6","drawable",getPackageName());
	        		break;
	        	}
	        	case 8:
	        	{
	        		id=getResources().getIdentifier("image_7","drawable",getPackageName());
	        		break;
	        	}
	        }
	        
	        Drawable drawable = res.getDrawable(id);
	        paper.setImageDrawable(drawable);
	        
		}
	}
}



