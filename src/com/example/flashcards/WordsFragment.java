package com.example.flashcards;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.view.ActionMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.flashcards.R;
import com.example.flashcards.entity.Dictionary;
import com.example.flashcards.entity.Word;
import com.example.flashcards.mvc.Controller;
import com.example.flashcards.utilities.MyActionModeCallback;
import com.example.flashcards.utilities.IMyFragmentCallback;

public class WordsFragment extends Fragment implements IMyFragmentCallback{
	/**
	 * The fragment argument representing the section number for this fragment.
	 */
	private static final String LOG_TAG = "WordsFragment";
	private View rootView;
	private MyActionModeCallback mActionModeCallback;
	private ActionMode mActionMode;
	private Word mWordToEdit;
	/**
	 * Returns a new instance of this fragment for the given section number.
	 */
	public static WordsFragment newInstance() {

		WordsFragment fragment = new WordsFragment();
		Log.d(LOG_TAG, "newInstance from args dictionary: ");
		return fragment;
	}

	public WordsFragment() {
		super();
	}

	// Here the content is put in
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		Log.d(LOG_TAG, "MainFragment onCreateView");
		
		rootView = inflater.inflate(R.layout.fragment_main, container,
				false);

		mActionModeCallback = new MyActionModeCallback(getActivity(), this);
		ListView listView = (ListView) rootView.findViewById(R.id.words_listView);
		listView.setLongClickable(true);
		listView.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> adapterView, View v,
					int position, long arg3) {
				if (mActionMode != null) {
					return false;
				}
				mWordToEdit = (Word) adapterView.getItemAtPosition(position);
				mActionModeCallback.setmWord(mWordToEdit);
				mActionMode = ((ActionBarActivity)getActivity()).startSupportActionMode(mActionModeCallback);
				v.setSelected(true);
				return true;
			}
		});
		
		return rootView;
	}

	
	
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		refresh();
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		((MainActivity) activity).onSectionAttached(Controller.getInstanceOf().getActiveDictionary());
	}

	@Override
	public void setActionMode(ActionMode mode) {
		mActionMode = mode;
		
	}

	@Override
	public void refresh() {
		ListView listView = (ListView) rootView.findViewById(R.id.words_listView);
		List<Word> list = Controller.getInstanceOf().getActiveDictionary().getWords();
		Word[] words = list.toArray(new Word[list.size()]);
		listView.setAdapter(new ArrayAdapter<Word>(getActivity(),android.R.layout.simple_list_item_1, words));
		
	}
}
