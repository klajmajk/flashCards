package com.example.flashcards;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ToggleButton;

import com.example.flashcards.entity.Dictionary;
import com.example.flashcards.entity.Topic;
import com.example.flashcards.mvc.Controller;

public class DictionaryDetailFragment extends Fragment {

	private static final String LOG_TAG = "DictionaryDetailFragment";
	
	private ListView listView;
	private ToggleButton toggleButton;
	private Button startButton;

	public static Fragment newInstance() {
		DictionaryDetailFragment fragment = new DictionaryDetailFragment();
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		Log.d(LOG_TAG, "inflating fragment_dictionary detail");
		View rootView = inflater.inflate(R.layout.fragment_dictionary_detail,
				container, false);
		listView = (ListView) rootView
				.findViewById(R.id.topics_listView);
		startButton = (Button) rootView.findViewById(R.id.start_button);

		toggleButton = (ToggleButton) rootView.findViewById(R.id.direction_togglebutton);
		

		
		
		startButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				startLearningSession(v);
				
			}
		});
		return rootView;
	}
	
	
	@Override
	public void onResume() {
		super.onResume();
		ListAdapter adapter = new TopicArrayListAdapter(getActivity(), Controller.getInstanceOf()
				.getActiveDictionary().getTopics(), startButton);
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> adapterView, View view, int arg2,
					long arg3) {

				CheckBox mCheckBox = (CheckBox) view
						.findViewById(R.id.topic_checkBox);
				if(mCheckBox.isChecked()){
					mCheckBox.setChecked(false);
				}
				else mCheckBox.setChecked(true);
				Log.d(LOG_TAG, "item clicked");
				
				
			}
		});
		
		
	}

	public void startLearningSession(View view){
		List<Topic> topics = getSelectedTopics();
		ActionBar actionBar = ((ActionBarActivity) getActivity())
				.getSupportActionBar();
		
		FragmentManager fm = getActivity().getSupportFragmentManager();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);	

		Controller.getInstanceOf().startLearningSession(topics, toggleButton.isChecked());
		fm.beginTransaction().replace(R.id.container, LearningSessionFragment.newInstance()).addToBackStack(null).commit();
	}

	private List<Topic> getSelectedTopics() {
		return ((TopicArrayListAdapter)listView.getAdapter()).getCheckedItems();
	}

}


