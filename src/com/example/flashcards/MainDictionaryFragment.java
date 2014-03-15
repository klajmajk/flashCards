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

	public static MainDictionaryFragment newInstance(Dictionary dictionary) {

		MainDictionaryFragment fragment = new MainDictionaryFragment();
		Bundle args = new Bundle();
		Controller.getInstanceOf().setActiveDictionary(dictionary);
		Log.d(LOG_TAG, dictionary.toString());
		args.putSerializable(WordsFragment.ARG_DICTIONARY, dictionary);
		fragment.setArguments(args);
		return fragment;
	}

	/**
	 * The number of pages (wizard steps) to show in this demo.
	 */
	private static final int NUM_PAGES = 2;

	private static final String LOG_TAG = "MainDictionaryFragment";

	/**
	 * The pager widget, which handles animation and allows swiping horizontally
	 * to access previous and next wizard steps.
	 */
	private ViewPager mPager;

	/**
	 * The pager adapter, which provides the pages to the view pager widget.
	 */
	private PagerAdapter mPagerAdapter;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.fragment_dictionary_pager,
				container, false);
		// Instantiate a ViewPager and a PagerAdapter.
		mPager = (ViewPager) rootView.findViewById(R.id.dictionary_pager);
		mPagerAdapter = new ScreenSlidePagerAdapter(getActivity()
				.getSupportFragmentManager());
		Log.d(LOG_TAG, "mPager " + mPager + " mPagerAdapter " + mPagerAdapter);
		mPager.setAdapter(mPagerAdapter);
		final ActionBar actionBar = ((ActionBarActivity) getActivity())
				.getSupportActionBar();
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
					.newInstance((Dictionary) getArguments().getSerializable(
							WordsFragment.ARG_DICTIONARY));
			return mFragment;
		case 1:
			
			mFragment = WordsFragment.newInstance((Dictionary) getArguments()
					.getSerializable(WordsFragment.ARG_DICTIONARY));
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
