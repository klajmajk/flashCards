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

public class MainFrangment extends Fragment {
	/**
	 * The fragment argument representing the section number for this fragment.
	 */
	private static final String ARG_DICTIONARY = "dictionary";
	private static final String LOG_TAG = "MAIN FRAGMENT";
	private Dictionary activeDictionary;

	/**
	 * Returns a new instance of this fragment for the given section number.
	 */
	public static MainFrangment newInstance(Dictionary dictionary) {

		MainFrangment fragment = new MainFrangment();
		Bundle args = new Bundle();
		args.putSerializable(ARG_DICTIONARY, dictionary);
		fragment.setArguments(args);
		return fragment;
	}

	public MainFrangment() {
		super();
	}

	// Here the content is put in
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		activeDictionary = (Dictionary) getArguments().getSerializable(ARG_DICTIONARY);
		View rootView = inflater.inflate(R.layout.fragment_main, container,
				false);
		ListView listView = (ListView) rootView.findViewById(R.id.words_listView);
		List<Word> list = activeDictionary.getAllWords();
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
