package com.translator;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

public class TranslateService extends Service {

	private static String tag = "TranslateService";
	
	private final ITranslate.Stub mBinder = new ITranslate.Stub() {
		
		
		public String translate(String text, String from, String to) throws RemoteException {
			
			Log.v(tag+".translate call", "");
			
			try{
				return Translator.translate(text,from,to);
			}
			catch(Exception e){
				e.printStackTrace();
				return null;
			}
		}
	};
	
	
	@Override
	public IBinder onBind(Intent arg0) {
		Log.v(tag, "onBind call");
		return mBinder;
	}

}
