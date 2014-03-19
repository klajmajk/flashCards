package com.example.flashcards;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.example.flashcards.entity.Topic;
import com.example.flashcards.mvc.Controller;

public class TopicArrayListAdapter extends ArrayAdapter<Topic>{
	private List<Topic> topics;
	private Context mContext;
	private Button startButton;

	public TopicArrayListAdapter(Context context, List<Topic> topics, Button startButton) {
		super(context, android.R.id.text1, topics);
		this.mContext = context;
		this.topics = topics;
		this.startButton = startButton;
		// TODO Auto-generated constructor stub
		this.mContext = context;
		mInflater = LayoutInflater.from(mContext);
		mSparseBooleanArray = new SparseBooleanArray();
	}

	LayoutInflater mInflater;
	SparseBooleanArray mSparseBooleanArray;

	
	public ArrayList<Topic> getCheckedItems() {
		ArrayList<Topic> mTempArry = new ArrayList<Topic>();
		for (int i = 0; i < topics.size(); i++) {
			if (mSparseBooleanArray.get(i)) {
				mTempArry.add(topics.get(i));
			}
		}

		return mTempArry;

	}

	@Override
	public int getCount() {
		return topics.size();
	}

	@Override
	public Topic getItem(int position) {
		return topics.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.row_check_item, null);
		}
		convertView.setFocusable(false);
		CheckBox mCheckBox = (CheckBox) convertView
				.findViewById(R.id.topic_checkBox);
		TextView firstLearned = (TextView) convertView
				.findViewById(R.id.learned_first_textView);
		TextView secondLearned = (TextView) convertView
				.findViewById(R.id.learned_second_textView);
		double [] learned = Controller.getInstanceOf().getActiveTopicLearned(getItem(position));

        DecimalFormat df = new DecimalFormat("#.#");
		
		firstLearned.setText("Z èeštiny: "+ df.format(learned[0]*100)+" %");
		secondLearned.setText("Do èeštiny: "+ df.format(learned[1]*100)+" %");
		
		mCheckBox.setText(topics.get(position).getName());
		mCheckBox.setTag(position);
		mCheckBox.setChecked(mSparseBooleanArray.get(position));
		mCheckBox.setOnCheckedChangeListener(mCheckedChangeListener);
		return convertView;
	}

	OnCheckedChangeListener mCheckedChangeListener = new OnCheckedChangeListener() {

		@Override
		public void onCheckedChanged(CompoundButton buttonView,
				boolean isChecked) {
			mSparseBooleanArray.put((Integer) buttonView.getTag(), isChecked);
			startButton.setEnabled(isChecked);

		}

	};

}
