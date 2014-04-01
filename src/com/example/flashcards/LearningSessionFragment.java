package com.example.flashcards;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.view.ActionMode;
import android.util.Log;
import android.view.ActionMode.Callback;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.flashcards.mvc.Controller;
import com.example.flashcards.utilities.MyActionModeCallback;
import com.example.flashcards.utilities.IMyFragmentCallback;

public class LearningSessionFragment extends Fragment implements
		TextToSpeech.OnInitListener, IMyFragmentCallback{
	private static final String LOG_TAG = "LearningSessionFragment";
	private View mRootView;
	private Button mFlipButton;
	private ImageButton mSpeak;
	private TextToSpeech tts;
	private MyActionModeCallback mActionModeCallback;
	private ActionMode mActionMode;
	private TextView mTextView;

	public static Fragment newInstance() {
		Fragment fragment = new LearningSessionFragment();

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

		mActionModeCallback = new MyActionModeCallback(getActivity(), this);

		mFlipButton = (Button) mRootView.findViewById(R.id.flip_button);
		mFlipButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
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

		mTextView = (TextView) mRootView
				.findViewById(R.id.first_word_textView);

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

		tts = new TextToSpeech(getActivity(), (OnInitListener) this);
		return mRootView;

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
		text = text.replaceAll("\\(.*?\\)", "");
		tts.speak(text, TextToSpeech.QUEUE_FLUSH, null);
	}

	@Override
	public void setActionMode(ActionMode mode) {
		mActionMode = mode;
		
	}

	@Override
	public void refresh() {
		mTextView.setText(Controller.getInstanceOf().getFirstText());
		
	}
	
	

}
