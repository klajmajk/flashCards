package com.example.flashcards;

import com.example.flashcards.mvc.Controller;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceActivity;

public class SettingsActivity extends PreferenceActivity {
	private Activity mActivity;
	protected void onCreate(Bundle savedInstanceState) {  
		   super.onCreate(savedInstanceState);
		   this.mActivity = this;
		   addPreferencesFromResource(R.xml.preferences);
		   Preference pref = findPreference("reset");
		   
		  
		   pref.setOnPreferenceClickListener(new OnPreferenceClickListener() {
		 
		      @Override
		      public boolean onPreferenceClick(Preference preference) {		 
		    	  show();
				return true;
		      }
		   });
	}
	
	 public void show() {
	        runOnUiThread(new Runnable() {
	            @Override
	            public void run() {
	            	 AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
	            	 builder.setMessage("Opravdu chcete smazat všechna uložená data?");
	            	 builder.setTitle("Reset");
	            	 builder.setPositiveButton("Ok", new OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {   	  
					    	  Controller.getInstanceOf().resetDictionaryData();
							
						}
					});
	            	 builder.setNegativeButton("Cancel", null);
	            	 AlertDialog alert = builder.create();
	                 alert.show();
	            }
	        });
	    }
	
}
