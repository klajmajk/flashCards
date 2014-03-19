package com.example.flashcards;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar.Tab;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.flashcards.R;
import com.example.flashcards.entity.Dictionary;
import com.example.flashcards.mvc.Controller;

public class MainActivity extends ActionBarActivity implements
		NavigationDrawerFragment.NavigationDrawerCallbacks {

	private static final String LOG_TAG = "MAIN_ACTIVITY";
	/**
	 * Fragment managing the behaviors, interactions and presentation of the
	 * navigation drawer.
	 */
	private NavigationDrawerFragment mNavigationDrawerFragment;
	private MainDictionaryFragment dictFragment;

	/**
	 * Used to store the last screen title. For use in
	 * {@link #restoreActionBar()}.
	 */
	private CharSequence mTitle;
	List<WeakReference<Fragment>> fragList = new ArrayList<WeakReference<Fragment>>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		Controller.getInstanceOf().setContext(this);
		setContentView(R.layout.activity_main);

		mNavigationDrawerFragment = (NavigationDrawerFragment) getSupportFragmentManager()
				.findFragmentById(R.id.navigation_drawer);
		mTitle = getTitle();

		// Set up the drawer.
		mNavigationDrawerFragment.setUp(R.id.navigation_drawer,
				(DrawerLayout) findViewById(R.id.drawer_layout));

		ActionBar actionBar = getSupportActionBar();
		actionBar.setHomeButtonEnabled(false);

	}

	@Override
	public void onNavigationDrawerItemSelected(Dictionary dictionary) {
		// TODO this should be edited to a nicer form
		Log.d(LOG_TAG, "onNavigationDrawerItemSelected dictionary: "
				+ dictionary);
		// update the main content by replacing fragments
		FragmentManager fragmentManager = getSupportFragmentManager();
		FragmentTransaction transaction = fragmentManager.beginTransaction();

		dictFragment = MainDictionaryFragment.newInstance(dictionary);
		transaction.replace(R.id.container, dictFragment);

		transaction.commit();
	}

	public void onSectionAttached(Dictionary selected) {
		mTitle = "Sekce: " + selected.toString();
	}

	public void restoreActionBar() {
		ActionBar actionBar = getSupportActionBar();
		actionBar.setDisplayShowTitleEnabled(true);
		actionBar.setHomeButtonEnabled(true);
		actionBar.setTitle(mTitle);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		if (!mNavigationDrawerFragment.isDrawerOpen()) {
			// Only show items in the action bar relevant to this screen
			// if the drawer is not showing. Otherwise, let the drawer
			// decide what to show in the action bar.
			getMenuInflater().inflate(R.menu.main, menu);
			restoreActionBar();
			return true;
		}
		return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public void onPause(){
		super.onPause();
		Controller.getInstanceOf().persist();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		// int id = item.getItemId();
		Log.d(LOG_TAG, "item selected start");
		if (mNavigationDrawerFragment.onOptionsItemSelected(item)) {
			return true;
		}

		Log.d(LOG_TAG, "item selected end");
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onAttachFragment(Fragment fragment) {
		// TODO Auto-generated method stub
		super.onAttachFragment(fragment);

		fragList.add(new WeakReference(fragment));
		Log.d(LOG_TAG, "running fragments: " + getActiveFragments());
	}

	public List<Fragment> getActiveFragments() {
		ArrayList<Fragment> ret = new ArrayList<Fragment>();
		for (WeakReference<Fragment> ref : fragList) {
			Fragment f = ref.get();
			if (f != null) {
				if (f.isVisible()) {
					ret.add(f);
				}
			}
		}
		return ret;
	}

}
