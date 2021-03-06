/*
 * Copyright 2013 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.flashcards.wizardpager.wizard.ui;

import com.example.flashcards.MainActivity;
import com.example.flashcards.NewWordFragment;
import com.example.flashcards.R;
import com.example.flashcards.entity.Topic;
import com.example.flashcards.mvc.Controller;
import com.example.flashcards.wizardpager.wizard.model.Page;
import com.example.flashcards.wizardpager.wizard.model.SingleFixedChoicePage;
import com.example.flashcards.wizardpager.wizard.model.SingleTopicChoicePage;


import android.support.v7.app.ActionBarActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class TopicChoiceFragment extends ListFragment {
	private static final String ARG_KEY = "key";

	private static final String LOG_TAG = null;

	private PageFragmentCallbacks mCallbacks;
	private List<String> mChoices;
	private String mKey;
	private Page mPage;

	public static TopicChoiceFragment create(String key) {
		Bundle args = new Bundle();
		args.putString(ARG_KEY, key);

		TopicChoiceFragment fragment = new TopicChoiceFragment();
		fragment.setArguments(args);
		return fragment;
	}

	public TopicChoiceFragment() {
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Bundle args = getArguments();
		mKey = args.getString(ARG_KEY);
		Log.d(LOG_TAG, "key: " + mKey);
		mPage = mCallbacks.onGetPage(mKey);

		Log.d(LOG_TAG, "page: " + mPage);

		SingleTopicChoicePage fixedChoicePage = (SingleTopicChoicePage) mPage;
		mChoices = new ArrayList<String>();
		for (int i = 0; i < fixedChoicePage.getOptionCount(); i++) {
			mChoices.add(fixedChoicePage.getOptionAt(i));
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		Log.d(LOG_TAG, "TopicFragment onCreateView");
		View rootView = inflater.inflate(R.layout.fragment_page, container,
				false);
		Log.d(LOG_TAG, "TopicFragment infalted");
		((TextView) rootView.findViewById(android.R.id.title)).setText(mPage
				.getTitle());

		final ListView listView = (ListView) rootView
				.findViewById(android.R.id.list);
		setListAdapter(new ArrayAdapter<String>(getActivity(),
				android.R.layout.simple_list_item_single_choice,
				android.R.id.text1, mChoices));
		listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

		// Pre-select currently selected item.
		new Handler().post(new Runnable() {
			@Override
			public void run() {
				String selection = mPage.getData().getString(
						Page.SIMPLE_DATA_KEY);
				for (int i = 0; i < mChoices.size(); i++) {
					if (mChoices.get(i).equals(selection)) {
						listView.setItemChecked(i, true);
						break;
					}
				}
			}
		});

		return rootView;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);

        if (!(activity instanceof PageFragmentCallbacks)) {
            throw new ClassCastException("Activity must implement PageFragmentCallbacks");
        }

        mCallbacks = (PageFragmentCallbacks) activity;
	}

	@Override
	public void onDetach() {
		super.onDetach();
		mCallbacks = null;
	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		Log.d(LOG_TAG, "Object: " + mChoices.get(position));
		if (mChoices.get(position) != "Nov�") {
			SingleTopicChoicePage fixedChoicePage = (SingleTopicChoicePage) mPage;
			mPage.getData().putSerializable(SingleTopicChoicePage.TOPIC,
					(Topic) fixedChoicePage.getOptionObjectAt(position));

			mPage.notifyDataChanged();
		} else {
			getNewTopicName();
		}
	}

	private void createNewTopic(String value) {
		// make changes on page
		if (value.trim() != "")
		{
			Log.d(LOG_TAG, "Name: " + value);
			SingleTopicChoicePage fixedChoicePage = (SingleTopicChoicePage) mPage;
			Topic topic = new Topic(value);
			fixedChoicePage.addTopic(topic);
			ArrayAdapter<Object> adapter = (ArrayAdapter) getListAdapter();
			adapter.insert(topic.getName(), adapter.getCount() - 1);
			// continue like normally
			mPage.getData().putSerializable(SingleTopicChoicePage.TOPIC, topic);
			mPage.notifyDataChanged();
		}
	}

	private void getNewTopicName() {

		AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());

		alert.setTitle("Vlo�te sekci");
		alert.setMessage("Jm�no");

		// Set an EditText view to get user input
		final EditText input = new EditText(getActivity());
		alert.setView(input);

		alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				String value = input.getText().toString();
				createNewTopic(value);
			}

		});

		alert.setNegativeButton("Cancel",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						// Canceled.
					}
				});

		alert.show();
	}

}
