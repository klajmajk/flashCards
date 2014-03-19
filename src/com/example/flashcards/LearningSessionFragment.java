package com.example.flashcards;

import java.util.List;

import com.example.flashcards.entity.Topic;
import com.example.flashcards.entity.Word;
import com.example.flashcards.mvc.Controller;

import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

public class LearningSessionFragment extends Fragment implements
		TextToSpeech.OnInitListener {
	private View mRootView;
	private Button mFlipButton;
	private ImageButton mSpeak;
	private TextToSpeech tts;

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
				getActivity()
						.getSupportFragmentManager()
						.beginTransaction()
						.addToBackStack(null)
						.replace(R.id.container,
								LearningSessionFlippedFragment.newInstance())
						.commit();

			}
		});

		mSpeak = (ImageButton) mRootView.findViewById(R.id.speak_out_button);
		if (Controller.getInstanceOf().isFromFirst())
			mSpeak.setVisibility(View.INVISIBLE);
		else
			mSpeak.setVisibility(View.VISIBLE);
		mSpeak.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				speakOut();

			}
		});
		
		

		newWord();

		tts = new TextToSpeech(getActivity(), (OnInitListener) this);
		return mRootView;

	}

	public void newWord() {
		TextView textView = (TextView) mRootView
				.findViewById(R.id.first_word_textView);
		Controller.getInstanceOf().newWord();
		textView.setText(Controller.getInstanceOf().getFirstText());
	}

	// TODO tohle je i ve flipped stejny lepší navrh by to chtìlo

	@Override
	public void onInit(int status) {
		if (status == TextToSpeech.SUCCESS) {
			Controller controller = Controller.getInstanceOf();
			int result = 0;
			if (controller.isFromFirst())
				result = tts.setLanguage(controller.getActiveDictionary()
						.getFirst());
			else
				result = tts.setLanguage(controller.getActiveDictionary()
						.getSecond());

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
		String text = Controller.getInstanceOf().getFirstText();
		text = text.replaceAll("\\(.*?\\)","");
		tts.speak(text, TextToSpeech.QUEUE_FLUSH, null);

	}

}
