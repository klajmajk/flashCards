package com.example.flashcards;

import android.os.Bundle;

import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.flashcards.mvc.Controller;

public class LearningSessionFlippedFragment extends Fragment implements
		TextToSpeech.OnInitListener {
	private static final String LOG_TAG = "LearningSessionFragment";
	private static final String ARG_FROM_FIRST = "from_first";
	private LayoutInflater mInflater;
	private ViewGroup mContainer;
	private View mRootView;
	private Button mCorrectButton;
	private Button mWrongButton;

	private ImageButton mSpeak;
	private TextToSpeech tts;;

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

		tts = new TextToSpeech(getActivity(), (OnInitListener) this);
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
		
		mSpeak = (ImageButton) mRootView.findViewById(R.id.speak_out_button);
		mSpeak.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				speakOut();

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
		getActivity().getSupportFragmentManager().beginTransaction()
				.replace(R.id.container, LearningSessionFragment.newInstance())
				.addToBackStack(null).commit();
	}

	@Override
	public void onInit(int status) {
		if (status == TextToSpeech.SUCCESS) {

			int result = tts.setLanguage(Controller.getInstanceOf()
					.getActiveDictionary().getSecond());

			if (result == TextToSpeech.LANG_MISSING_DATA
					|| result == TextToSpeech.LANG_NOT_SUPPORTED) {
				Log.e("TTS", "This Language is not supported");
			} else {
				mSpeak.setEnabled(true);
			}

		} else {
			Log.e("TTS", "Initilization Failed!");
		}

	}
	
	 @Override
	    public void onDestroy() {
	        // Don't forget to shutdown tts!
	        if (tts != null) {
	            tts.stop();
	            tts.shutdown();
	        }
	        super.onDestroy();
	    }
	 

	private void speakOut() {
		String text = Controller.getInstanceOf().getSecondText();
		int index = text.indexOf('-');
		if(index != -1) text  = text.substring(0, index-1);
		tts.speak(text, TextToSpeech.QUEUE_FLUSH, null);

	}

}
