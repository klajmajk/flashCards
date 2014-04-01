package com.example.flashcards;

import java.util.ArrayList;
import java.util.List;

import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
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
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.example.flashcards.entity.Dictionary;
import com.example.flashcards.entity.Topic;
import com.example.flashcards.imports.GoogleDocsImport;
import com.example.flashcards.mvc.Controller;
import com.example.flashcards.utilities.ActivityServices;
import com.example.flashcards.utilities.Constants;

public class DictionaryDetailFragment extends Fragment implements IFromTaskCallBack {

	private static final String LOG_TAG = "DictionaryDetailFragment";

	private ListView listView;
	private ToggleButton toggleButton;
	private Button mStartButton;
	private String mEmail;
	private ProgressBar mSyncProgress;

	private TextView mSyncText;
	private static DictionaryDetailFragment mFragment;
	
	public static Fragment newInstance() {
		mFragment = new DictionaryDetailFragment();
		return mFragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		Log.d(LOG_TAG, "inflating fragment_dictionary detail");
		View rootView = inflater.inflate(R.layout.fragment_dictionary_detail,
				container, false);
		listView = (ListView) rootView.findViewById(R.id.topics_listView);
		mStartButton = (Button) rootView.findViewById(R.id.start_button);
		toggleButton = (ToggleButton) rootView
				.findViewById(R.id.direction_togglebutton);
		mSyncProgress = (ProgressBar)rootView.findViewById(R.id.sync_progressBar);
		mSyncProgress.setVisibility(View.INVISIBLE);
		mSyncProgress.setEnabled(false);
		mSyncText = (TextView) rootView.findViewById(R.id.sync_textView);
    	mEmail = PreferenceManager.getDefaultSharedPreferences(getActivity()).getString(Constants.PREF_EMAIL, null);

		Controller.getInstanceOf().learningSessionCheck();

		mStartButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				startLearningSession(v);

			}
		});
		mSyncText.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if(ActivityServices.isDeviceConnected(getActivity())){
					synchronize();
				}else{
					Toast.makeText(getActivity(), "Pøipojení nedostupné", Toast.LENGTH_SHORT);
				}
			}
		});
		return rootView;
	}

	@Override
	public void onResume() {
		super.onResume();
		refresh();
		/*if(ActivityServices.isDeviceConnected(getActivity())){
			Log.d(LOG_TAG, "onResume activity: "+getActivity());
			synchronize();
		}*/
	}

	private void refresh() {
		mSyncText.setText(Controller.getInstanceOf().getSyncStatusText());
		Log.d(LOG_TAG, "Topics for adapter "+Controller.getInstanceOf().getActiveDictionary().getTopics()+" activity "+getActivity()+" start button "+mStartButton);
		ListAdapter adapter = new TopicArrayListAdapter(getActivity(),
				Controller.getInstanceOf().getActiveDictionary().getTopics(),
				mStartButton);
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapterView, View view,
					int arg2, long arg3) {

				CheckBox mCheckBox = (CheckBox) view
						.findViewById(R.id.topic_checkBox);
				if (mCheckBox.isChecked()) {
					mCheckBox.setChecked(false);
				} else
					mCheckBox.setChecked(true);
				Log.d(LOG_TAG, "item clicked");
			}
		});
		
	}

	public void startLearningSession(View view) {
		List<Topic> topics = getSelectedTopics();
		ActionBar actionBar = ((ActionBarActivity) getActivity())
				.getSupportActionBar();

		FragmentManager fm = getActivity().getSupportFragmentManager();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);

		Controller.getInstanceOf().startLearningSession(topics,
				toggleButton.isChecked());
		fm.beginTransaction()
				.replace(R.id.container, LearningSessionFragment.newInstance())
				.addToBackStack(null).commit();
	}

	private List<Topic> getSelectedTopics() {
		return ((TopicArrayListAdapter) listView.getAdapter())
				.getCheckedItems();
	}

	@Override
	public void show(String message) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void handleException(Exception e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void taskFinished() {
		mSyncProgress.setEnabled(false);
		mSyncProgress.setVisibility(View.INVISIBLE);
		refresh();
	}

	private void synchronize() {
		mSyncProgress.setVisibility(View.VISIBLE);
		mSyncProgress.setEnabled(true);
		mSyncText.setText("Probíhá synchronizace");
		//mSyncProgress.setProgress(0);
		new AsyncTask<Void, Void, Void>() {

			@Override
			protected Void doInBackground(Void... params) {
				
				GoogleDocsImport.syncWithServer(getActivity(), mEmail, Controller.getInstanceOf(), mFragment);
				return null;
			}

			@Override
			protected void onPostExecute(Void result) {
				// TODO Auto-generated method stub
				super.onPostExecute(result);
				taskFinished();
			}
			
			
		}.execute();
	}

}
