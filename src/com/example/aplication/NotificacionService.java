package com.example.aplication;

import java.util.ArrayList;
import java.util.Random;


import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;

public class NotificacionService extends Service  implements Runnable {

	

public static final int APP_ID_NOTIFICATION = 0; 
private NotificationManager mManager;

private final int MSG_KEY_ES_IMPAR = 1;
private final int MSG_KEY_ES_PAR = 2;


/**
 * M�todo del hilo as�ncrono, que obtiene un numero aleatorio y comprueba su paridad
 */
public void run() {
	
	final Random myRandom = new Random();
	
	int numeroAleatorio = myRandom.nextInt();
	
	Log.e("alarmChecker", "se genero un " + numeroAleatorio);
	if (numeroAleatorio % 2 > 0)
	{
		//Respondemos que es impar
		handler.sendEmptyMessage(MSG_KEY_ES_IMPAR);
	}else
	{
		//Respondemos que es par
		handler.sendEmptyMessage(MSG_KEY_ES_PAR);
	}	
}


/**
 * Procesa eventos desde el hilo run
 */
private  Handler handler = new Handler() {
	@Override
	public void handleMessage(Message msg) {
			switch (msg.what){
			case MSG_KEY_ES_PAR: //Hemos obtenido un numero par
				 Notificar();
				break;
			}
	}
};


/**
 * prepara y lanza la notificacion
 */
private void Notificar() {
		
	//Prepara la actividad que se abrira cuando el usuario pulse la notificacion
		Intent intentNot = new Intent(this, SaludoActivity.class);
		
	//Prepara la notificacion
		Notification notification = new Notification(R.drawable.ic_launcher, "Te Estan Pensando", System.currentTimeMillis());
		notification.setLatestEventInfo(this, getString(R.string.app_name), getString(R.string.notified), 
				PendingIntent.getActivity(this.getBaseContext(), 0, intentNot, PendingIntent.FLAG_CANCEL_CURRENT));
		
	//Le a�ade sonido
		notification.defaults |= Notification.DEFAULT_SOUND;
	//Le a�ade vibraci�n
		notification.defaults |= Notification.DEFAULT_VIBRATE;
		
	//Le a�ade luz mediante LED
		notification.defaults |= Notification.DEFAULT_LIGHTS;
		
	//La notificaci�n se detendr� cuando el usuario pulse en ella
		notification.flags = Notification.FLAG_AUTO_CANCEL;
		
	//Intenta establecer el color y el parpadeo de la bombilla lED
		try
		{
			notification.ledARGB = 0xff00ff00;
			notification.ledOnMS = 300;
			notification.ledOffMS = 1000;
			notification.flags |= Notification.FLAG_SHOW_LIGHTS;
		}catch(Exception ex)
		{
			//Nothing
		}
		
		//Lanza la notificaci�n
		mManager.notify(APP_ID_NOTIFICATION, notification);
		
	 }

	  
	  
@Override
public void onCreate() {
 // TODO Auto-generated method stub
// Toast.makeText(this, "MyAlarmService.onCreate()", Toast.LENGTH_LONG).show();
 
 mManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
}

@Override
public IBinder onBind(Intent intent) {
 // TODO Auto-generated method stub
// Toast.makeText(this, "MyAlarmService.onBind()", Toast.LENGTH_LONG).show();
 return null;
}

@Override
public void onDestroy() {
 // TODO Auto-generated method stub
 super.onDestroy();
// Toast.makeText(this, "MyAlarmService.onDestroy()", Toast.LENGTH_LONG).show();
}

@Override
public void onStart(Intent intent, int startId) {
 // TODO Auto-generated method stub
 super.onStart(intent, startId);
// Toast.makeText(this, "MyAlarmService.onStart()", Toast.LENGTH_LONG).show();
 
//Creamos un hilo que obtendra la informaci�n de forma as�ncrona
	Thread thread = new Thread(this);
	thread.start();
}

	 


@Override
public boolean onUnbind(Intent intent) {
 // TODO Auto-generated method stub
// Toast.makeText(this, "MyAlarmService.onUnbind()", Toast.LENGTH_LONG).show();
 return super.onUnbind(intent);
}

}