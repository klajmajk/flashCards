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

package com.example.flashcards.wizardpager.wizard.model;

import com.example.flashcards.entity.Topic;
import com.example.flashcards.wizardpager.wizard.ui.TopicChoiceFragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A page offering the user a number of mutually exclusive choices.
 */
public class SingleTopicChoicePage extends SingleFixedChoicePage {
	public static final String TOPIC = "topic";
	private static final String LOG_TAG = "SingleTopicChoicePage";
    protected ArrayList<Object> mChoices = new ArrayList<>();

    public SingleTopicChoicePage(ModelCallbacks callbacks, String title) {
        super(callbacks, title);
    }

    @Override
    public Fragment createFragment() {
        return TopicChoiceFragment.create(getKey());
    }

    public String getOptionAt(int position) {
    	Log.d(LOG_TAG, "mChoices.get(position): "+mChoices.get(position)+" instanfe of topic: "+ (mChoices.get(position) instanceof Topic));
    	if(mChoices.get(position) instanceof Topic){
    		return ((Topic) mChoices.get(position)).getName();
    	}else
    		return mChoices.get(position).toString();
    }
    
    public Object getOptionObjectAt(int position) {
    	return mChoices.get(position);
	}

    public int getOptionCount() {
        return mChoices.size();
    }

    @Override
    public void getReviewItems(ArrayList<ReviewItem> dest) {
        dest.add(new ReviewItem(getTitle(), ((Topic) mData.getSerializable(TOPIC)).getName(), getKey()));
    }

    @Override
    public boolean isCompleted() {
        return !TextUtils.isEmpty(mData.getString(SIMPLE_DATA_KEY));
    }

    public SingleTopicChoicePage setChoices(List<Topic> choices) {
        mChoices.addAll(choices);
        mChoices.add("Nový");
        return this;
    }

    public SingleTopicChoicePage setValue(String value) {
        mData.putString(SIMPLE_DATA_KEY, value);
        return this;
    }

	public void addTopic(Topic topic) {
		mChoices.remove("Nový");
		mChoices.add(topic);
		mChoices.add("Nový");
		
	}

	

	
}
