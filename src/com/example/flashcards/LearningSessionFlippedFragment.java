package com.example.flashcards;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.flashcards.mvc.Controller;

public class LearningSessionFlippedFragment extends Fragment {
	private static final String LOG_TAG = "LearningSessionFragment";
	private static final String ARG_FROM_FIRST = "from_first";
	private LayoutInflater mInflater;
	private ViewGroup mContainer;
	private View mRootView;
	private Button mCorrectButton;
	private Button mWrongButton;

	public static Fragment newInstance() {
		LearningSessionFlippedFragment fragment = new LearningSessionFlippedFragment();

		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		ActionBar actionBar = ((ActionBarActivity) getActivity())
				.getSupportActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);

		mInflater = inflater;
		mContainer = container;
		Log.d(LOG_TAG, "infalting after flip");
		mRootView = mInflater.inflate(R.layout.fragment_learning_session_flip,
				mContainer, false);
		flip(mRootView);
		return mRootView;

	}

	public void flip(View view) {
		TextView textView = (TextView) mRootView
				.findViewById(R.id.second_view_textView);
		Log.d(LOG_TAG, "textView: " + textView);
		textView.setText(Controller.getInstanceOf().getSecondText());

		mCorrectButton = (Button) mRootView.findViewById(R.id.correct_button);
		mCorrectButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				correctClick(v);

			}
		});

		mWrongButton = (Button) mRootView.findViewById(R.id.wrong_button);
		mWrongButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				wrongClick(v);

			}
		});
	}

	public void wrongClick(View view) {
		Controller.getInstanceOf().wrongAnswer();
		newWord();
	}

	public void correctClick(View view) {
		Controller.getInstanceOf().correctAnswer();
		newWord();
	}

	public void newWord() {
		getActivity().getSupportFragmentManager().popBackStack();
		getActivity()
				.getSupportFragmentManager()
				.beginTransaction()
				.replace(R.id.container,
						LearningSessionFragment.newInstance())
				.addToBackStack(null).commit();
	}

}
