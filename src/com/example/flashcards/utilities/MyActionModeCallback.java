package com.example.flashcards.utilities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.view.ActionMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.example.flashcards.R;
import com.example.flashcards.entity.Word;
import com.example.flashcards.mvc.Controller;

public class MyActionModeCallback implements ActionMode.Callback{
	private static final String LOG_TAG = null;
	private Context mContext;
	private IMyFragmentCallback mCallback;
	private Word mWord;
	
	
	
	public MyActionModeCallback(Context mContext, IMyFragmentCallback callback) {
		super();
		this.mContext = mContext;
		this.mCallback = callback;
		this.mWord = null;
	}

	
	public void setmWord(Word mWord) {
		this.mWord = mWord;
	}


	private void showEditDialog(){
		// get prompts.xml view
		LayoutInflater li = LayoutInflater.from(mContext);
		View editView = li.inflate(R.layout.edit_word_dialog, null);

		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
				mContext);

		// set prompts.xml to alertdialog builder
		alertDialogBuilder.setView(editView);

		final EditText firstText = (EditText) editView
				.findViewById(R.id.dialog_first_editText);
		final EditText secondText = (EditText) editView
				.findViewById(R.id.dialog_second_editText);
		firstText.setText(mWord.getFirst());
		secondText.setText(mWord.getSecond());

		// set dialog message
		alertDialogBuilder
			.setCancelable(false)
			.setPositiveButton("OK",
			  new DialogInterface.OnClickListener() {
			    public void onClick(DialogInterface dialog,int id) {
			    	Controller.getInstanceOf().editWord(mWord,firstText.getText().toString(), secondText.getText().toString());
			    	mCallback.refresh();
			    }
			  })
			.setNegativeButton("Cancel",
			  new DialogInterface.OnClickListener() {
			    public void onClick(DialogInterface dialog,int id) {
				dialog.cancel();
			    }
			  }).setTitle("Editace slova");

		// create alert dialog
		AlertDialog alertDialog = alertDialogBuilder.create();

		// show it
		alertDialog.show();
	}

	@Override
	public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_word_edit:
			Log.d(LOG_TAG, "Editing");
			showEditDialog();
			return true;
		default:
			return ((Activity)mContext).onContextItemSelected(item);
		}
	}

	@Override
	public boolean onCreateActionMode(ActionMode mode, Menu menu) {
		MenuInflater inflater = ((Activity) mContext).getMenuInflater();
		inflater.inflate(R.menu.word, menu);
		return true;
	}

	@Override
	public void onDestroyActionMode(ActionMode mode) {
		mCallback.setActionMode(null);

	}

	@Override
	public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
		return false;
	}

}
