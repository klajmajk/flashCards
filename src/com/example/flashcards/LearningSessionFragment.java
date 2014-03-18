package com.example.flashcards;

import java.util.List;

import com.example.flashcards.entity.Topic;
import com.example.flashcards.entity.Word;
import com.example.flashcards.mvc.Controller;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class LearningSessionFragment extends Fragment {
	private View mRootView;
	private Button mFlipButton;

	public static Fragment newInstance() {
		LearningSessionFragment fragment = new LearningSessionFragment();

		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		ActionBar actionBar = ((ActionBarActivity) getActivity())
				.getSupportActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
		mRootView = inflater.inflate(R.layout.fragment_learning_session,
				container, false);

		mFlipButton = (Button) mRootView.findViewById(R.id.flip_button);
		mFlipButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				getActivity().getSupportFragmentManager().popBackStack();
				getActivity().getSupportFragmentManager().beginTransaction().addToBackStack(null)
						.replace(R.id.container, LearningSessionFlippedFragment.newInstance()).commit();

			}
		});
		newWord();
		return mRootView;

	}


	public void newWord() {
		TextView textView = (TextView) mRootView
				.findViewById(R.id.first_word_textView);
		textView.setText(Controller.getInstanceOf().getFirstText());
	}

}
