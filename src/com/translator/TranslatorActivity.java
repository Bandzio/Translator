package com.translator;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

public class TranslatorActivity extends Activity implements OnClickListener{
    /** Called when the activity is first created. */
	
	private static String tag = "TranslatorActivity"; 
	
	
	private ITranslate mTranslateService;
	
	private ServiceConnection mTranslateConn = new ServiceConnection() {
		
		public void onServiceConnected(ComponentName name, IBinder service) {
			Log.v("serviceConnection", "onServiceConnected");
			mTranslateService = ITranslate.Stub.asInterface(service);
			if(mTranslateService != null){
				mButton.setEnabled(true);
			}
			else{
				mButton.setEnabled(false);
			}
			
		}
		
		public void onServiceDisconnected(ComponentName name) {
			Log.v("serviceConnection", "onServiceDisconnected");
			mButton.setEnabled(false);
			mTranslateService = null;
		}
		
	};


	private EditText mEditTextInput;
	private EditText mEditTextOutput;
	private Spinner mSpinnerFrom;
	private Spinner mSpinnerTo;
	private Button mButton;
	private String[] languageValues= null;

	private Handler mHandler = new Handler();
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        Log.v(tag, "onCreate call");
        
        mEditTextInput = (EditText)findViewById(R.id.input);
        mEditTextInput.selectAll();
        
        mEditTextOutput = (EditText)findViewById(R.id.translation);
        
        languageValues = getResources().getStringArray(R.array.language_values);
        
        
        mSpinnerFrom = (Spinner)findViewById(R.id.from);
        ArrayAdapter<?> fromAdapter =
        	ArrayAdapter.createFromResource(this, R.array.languages, android.R.layout.simple_spinner_item);
		mSpinnerFrom.setAdapter(fromAdapter);
		mSpinnerFrom.setSelection(1);
        
		
        
		mSpinnerTo = (Spinner)findViewById(R.id.to);
		ArrayAdapter<?> toAdapter =
        	ArrayAdapter.createFromResource(this, R.array.languages, android.R.layout.simple_spinner_item);
        mSpinnerTo.setAdapter(toAdapter);
        mSpinnerTo.setSelection(2);
        
        
        mButton = (Button)findViewById(R.id.translateBtn);
        mButton.setOnClickListener(this);
        
        Intent service = new Intent(Intent.ACTION_VIEW);
        bindService(service, mTranslateConn, Context.BIND_AUTO_CREATE);
        
    }

	public void onClick(View v) {
		Log.v(tag, "onClick call");
		doTranslate();
		
	}
	
	public void doStuff(){
		//TODO: only for test;
	}

	private void doTranslate() {
		Log.v(tag, "doTranslate call");
		mHandler.post(new Runnable() {
			
			public void run() {

				try{
					String text = mEditTextInput.getText().toString();
					int from = mSpinnerFrom.getSelectedItemPosition();
					int to = mSpinnerTo.getSelectedItemPosition();
					
					Log.v("translate from ", languageValues[from]);
					Log.v("translate to", languageValues[to]);
					String result = 
						mTranslateService.translate(text, languageValues[from], languageValues[to]);
					
					Log.v("string = ",""+result);
					
					mEditTextOutput.setText(result);
					mEditTextInput.selectAll();
					
				}
				catch(Exception e){
					Log.v("Error", ""+e.toString());
					e.printStackTrace();
				}
			}
		});
		
	}
}