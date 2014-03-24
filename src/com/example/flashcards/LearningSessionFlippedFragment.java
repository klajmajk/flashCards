package com.example.flashcards;

import android.R.bool;
import android.os.Bundle;

import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.view.ActionMode;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.flashcards.mvc.Controller;
import com.example.flashcards.utilities.IMyFragmentCallback;
import com.example.flashcards.utilities.MyActionModeCallback;

public class LearningSessionFlippedFragment extends Fragment implements
		TextToSpeech.OnInitListener, IMyFragmentCallback {
	private static final String LOG_TAG = "LearningSessionFragment";
	private static final String ARG_FROM_FIRST = "from_first";
	private LayoutInflater mInflater;
	private ViewGroup mContainer;
	private View mRootView;
	private Button mCorrectButton;
	private Button mWrongButton;

	private ImageButton mSpeak;
	private TextToSpeech tts;
	private TextView mTextView;
	private MyActionModeCallback mActionModeCallback;
	private ActionMode mActionMode;

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

		mActionModeCallback = new MyActionModeCallback(getActivity(), this);

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
		mTextView = (TextView) mRootView
				.findViewById(R.id.second_view_textView);
		Log.d(LOG_TAG, "textView: " + mTextView);
		mTextView.setOnLongClickListener(new OnLongClickListener() {


			@Override
			public boolean onLongClick(View v) {
				if (mActionMode != null) {
					return false;
				}
				mActionModeCallback.setmWord(Controller.getInstanceOf().getActiveWord());

				mActionMode = ((ActionBarActivity)getActivity()).startSupportActionMode(mActionModeCallback);
				v.setSelected(true);
				return true;
			}
		});
		refresh();

		mCorrectButton = (Button) mRootView.findViewById(R.id.correct_button);
		mCorrectButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				correctClick(v);

			}
		});

		mSpeak = (ImageButton) mRootView.findViewById(R.id.speak_out_button);
		if(Controller.getInstanceOf().isFromFirst()) mSpeak.setVisibility(View.VISIBLE);
		else mSpeak.setVisibility(View.INVISIBLE);
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
		Controller.getInstanceOf().newWord();
		getActivity().getSupportFragmentManager().popBackStack();
		getActivity().getSupportFragmentManager().beginTransaction()
				.replace(R.id.container, LearningSessionFragment.newInstance())
				.addToBackStack(null).commit();
	}

	@Override
	public void onInit(int status) {
		if (status == TextToSpeech.SUCCESS) {
			Controller controller = Controller.getInstanceOf();
			int result = 0;
			if (controller.isFromFirst())
				result = tts.setLanguage(controller
						.getActiveDictionary().getSecond());
			else result = tts.setLanguage(controller
					.getActiveDictionary().getFirst());

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
		text = text.replaceAll("\\(.*?\\)","");
		tts.speak(text, TextToSpeech.QUEUE_FLUSH, null);

	}

	@Override
	public void setActionMode(ActionMode mode) {
		mActionMode = mode;
		
	}

	@Override
	public void refresh() {

		mTextView.setText(Controller.getInstanceOf().getSecondText());
		
	}
	
	

}
