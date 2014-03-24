package com.example.flashcards;

import com.example.flashcards.NavigationDrawerFragment.NavigationDrawerCallbacks;
import com.example.flashcards.entity.Dictionary;
import com.example.flashcards.mvc.Controller;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.util.Log;

public class LearningActivity extends FragmentActivity implements NavigationDrawerCallbacks {
	private static final String LOG_TAG = "LearningActivity";
	private boolean fliped = false;
	
	

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);

		setContentView(R.layout.activity_main);
		Log.d(LOG_TAG, "created");
		
	}
	
	private void startAppropriateFragment(){
		if(fliped) {			
			FragmentManager fm = this.getSupportFragmentManager();	
			fm.beginTransaction().replace(R.id.container, LearningSessionFragment.newInstance()).addToBackStack(null).commit();
		}else{
			FragmentManager fm = this.getSupportFragmentManager();	
			fm.beginTransaction().replace(R.id.container, LearningSessionFlippedFragment.newInstance()).addToBackStack(null).commit();
		}
	}
	
	
	
	@Override
	protected void onPause() {
		super.onPause();
		Log.d(LOG_TAG, "Persisting");
		Controller.getInstanceOf().persist();
	}

	@Override
	public void onNavigationDrawerItemSelected(Dictionary dictionary) {
		// TODO Auto-generated method stub
		
	}
	

}
