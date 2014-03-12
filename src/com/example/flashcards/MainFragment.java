package com.example.flashcards;

import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.flashcards.entity.Dictionary;
import com.example.flashcards.entity.Word;
import com.example.flashcards.mvc.Controller;

public class MainFragment extends Fragment {
	/**
	 * The fragment argument representing the section number for this fragment.
	 */
	private static final String ARG_DICTIONARY = "dictionary";
	private static final String LOG_TAG = "MAIN FRAGMENT";
	private Dictionary activeDictionary;

	/**
	 * Returns a new instance of this fragment for the given section number.
	 */
	public static MainFragment newInstance(Dictionary dictionary) {

		MainFragment fragment = new MainFragment();
		Bundle args = new Bundle();
		Controller.getInstanceOf().setActiveDictionary(dictionary);
		args.putSerializable(ARG_DICTIONARY, dictionary);
		fragment.setArguments(args);
		return fragment;
	}

	public MainFragment() {
		super();
	}

	// Here the content is put in
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		Log.d(LOG_TAG, "MainFragment onCreateView");
		activeDictionary = Controller.getInstanceOf().getActiveDictionary();
		
		View rootView = inflater.inflate(R.layout.fragment_main, container,
				false);
		ListView listView = (ListView) rootView.findViewById(R.id.words_listView);
		List<Word> list = activeDictionary.getWords();
		Log.d(LOG_TAG, "Words"+list.toString());
		Word[] words = list.toArray(new Word[list.size()]);
		listView.setAdapter(new ArrayAdapter<Word>(getActivity(),android.R.layout.simple_list_item_1, words));
		/*TextView textView = (TextView) rootView
				.findViewById(R.id.section_label);
		textView.setText("Obsah: "
				+ getArguments().getSerializable(ARG_DICTIONARY).toString());*/
		return rootView;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		Log.d(LOG_TAG, "onAttach dictionary: "
				+ getArguments().getSerializable(ARG_DICTIONARY));
		((MainActivity) activity).onSectionAttached((Dictionary) getArguments().getSerializable(ARG_DICTIONARY));
	}
}
