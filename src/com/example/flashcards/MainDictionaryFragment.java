package com.example.flashcards;

import com.example.flashcards.entity.Dictionary;
import com.example.flashcards.mvc.Controller;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBar.Tab;
import android.support.v7.app.ActionBarActivity;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class MainDictionaryFragment extends Fragment implements
		ActionBar.TabListener {
	private Fragment mFragment;
	private static final int NUM_PAGES = 2;
	private static final String LOG_TAG = "MainDictionaryFragment";
	private ViewPager mPager;
	private PagerAdapter mPagerAdapter;
	private ActionBar actionBar;

	public static MainDictionaryFragment newInstance(Dictionary dictionary) {

		MainDictionaryFragment fragment = new MainDictionaryFragment();
		Controller.getInstanceOf().setActiveDictionary(dictionary);
		return fragment;
	}
	
	

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.fragment_dictionary_pager,
				container, false);
		// Instantiate a ViewPager and a PagerAdapter.
		mPager = (ViewPager) rootView.findViewById(R.id.dictionary_pager);
		mPagerAdapter = new ScreenSlidePagerAdapter(getChildFragmentManager());
		Log.d(LOG_TAG, "mPager " + mPager + " mPagerAdapter " + mPagerAdapter);
		mPager.setAdapter(mPagerAdapter);

		final ActionBar actionBar = ((ActionBarActivity)getActivity()).getSupportActionBar();

		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		Tab tab = actionBar.newTab().setText("Slovník").setTabListener(this);
		actionBar.removeAllTabs();
		actionBar.addTab(tab);

		tab = actionBar.newTab().setText("Slovíèka").setTabListener(this);
		actionBar.addTab(tab);
		mPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
			 
		    @Override
		    public void onPageSelected(int position) {
		        // on changing the page
		        // make respected tab selected
		        actionBar.setSelectedNavigationItem(position);
		    }
		 
		    @Override
		    public void onPageScrolled(int arg0, float arg1, int arg2) {
		    }
		 
		    @Override
		    public void onPageScrollStateChanged(int arg0) {
		    }
		});
		
		return rootView;
	}

	/*
	 * @Override public void onBackPressed() { if (mPager.getCurrentItem() == 0)
	 * { // If the user is currently looking at the first step, allow the system
	 * to handle the // Back button. This calls finish() on this activity and
	 * pops the back stack. super.onBackPressed(); } else { // Otherwise, select
	 * the previous step. mPager.setCurrentItem(mPager.getCurrentItem() - 1); }
	 * }
	 */

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}	
	
	
	

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}




	/**
	 * A simple pager adapter that represents 5 ScreenSlidePageFragment objects,
	 * in sequence.
	 */
	private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
		public ScreenSlidePagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			return getCurrentItem(position);
		}

		@Override
		public int getCount() {
			return NUM_PAGES;
		}
	}

	public Fragment getCurrentItem(int position) {

		Log.d(LOG_TAG, "position: " + position);
		switch (position) {
		case 0:
			mFragment =DictionaryDetailFragment
					.newInstance();
			return mFragment;
		case 1:
			
			mFragment = WordsFragment.newInstance();
			return mFragment;

		default:
			break;
		}
		return null;
	}

	@Override
	public void onTabReselected(Tab arg0, FragmentTransaction arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onTabSelected(Tab tab, FragmentTransaction transaction) {
		mPager.setCurrentItem(tab.getPosition());
	}

	@Override
	public void onTabUnselected(Tab arg0, FragmentTransaction transaction) {
		

	}
	
	

}
