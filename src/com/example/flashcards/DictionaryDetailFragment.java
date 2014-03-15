package com.example.flashcards;

import com.example.flashcards.entity.Dictionary;
import com.example.flashcards.mvc.Controller;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class DictionaryDetailFragment extends Fragment{
	

	private static final String ARG_DICTIONARY = "activeDictionary";
	private static final String LOG_TAG = "DictionaryDetailFragment";

	public static Fragment newInstance(Dictionary activeDictionary) {
		DictionaryDetailFragment fragment = new DictionaryDetailFragment();
		Bundle args = new Bundle();
		//TODO to remove
		Controller.getInstanceOf().setActiveDictionary(activeDictionary);
		args.putSerializable(ARG_DICTIONARY, activeDictionary);
		fragment.setArguments(args);
		return fragment;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		Log.d(LOG_TAG, "inflating fragment_dictionary detail");
		View rootView = inflater.inflate(R.layout.fragment_dictionary_detail, container,
				false);
		return rootView;
	}
	
}

