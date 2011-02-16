package com.translator;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

import org.json.JSONObject;

import android.util.Log;

public class Translator {

	private static String tag = "Translator";
	
	
	private static String URLBASE = 
		"http://ajax.googleapis.com/ajax/services/language/translate?v=1.0&langpair=";

	public static String translate(String text, String from, String to) throws Exception{
	
		Log.v(tag, "translate call");
		try{
			StringBuilder url = new StringBuilder();
			url.append(URLBASE).append(from).append("%7C").append(to);
			url.append("&q=").append(URLEncoder.encode(text, "UTF-8"));
			
			
			URLConnection openConnection = new URL(url.toString()).openConnection();
			openConnection.setDoInput(true);
			openConnection.setDoOutput(true);
	
			try{
				InputStream inputStream = openConnection.getInputStream();
				String result = makeResult(inputStream);
				
				JSONObject jsonObject = new JSONObject(result);
				String string = ((JSONObject)jsonObject.get("responseData")).getString("translatedText");
				
				Log.v("Json string = ", ""+string);
				return string;
			}
			finally{
				
				openConnection.getInputStream().close();
				
			}
		}
		catch(Exception e){
			Log.v("Error",""+e.toString() );
			e.printStackTrace();
		}
		
		
		return null;
	}

	private static String makeResult(InputStream inputStream) throws Exception {
		Log.v(tag, "makeResult call");
		StringBuilder sb = new StringBuilder();
		
		try{
			String readLine;
			if(inputStream!=null){
				BufferedReader bufferedReader =
					new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
				
				while(null != (readLine = bufferedReader.readLine())){
					sb.append(readLine).append("\n");
				}
			}
		}
		catch(Exception ex){
			ex.printStackTrace();
			Log.v("Error", ""+ex.toString());
		}
		return sb.toString();
	}

}
