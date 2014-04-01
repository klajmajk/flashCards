/*
 * Copyright 2012 The Android Open Source Project
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

package com.example.flashcards.oauth;

import java.io.IOException;

import org.json.JSONException;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.EditText;

import com.example.flashcards.ImportActivity;
import com.example.flashcards.R;
import com.example.flashcards.entity.DictionarySyncParam;
import com.example.flashcards.entity.ImportResult;
import com.example.flashcards.exceptions.ImportErrorException;
import com.example.flashcards.imports.GoogleDocsImport;
import com.example.flashcards.mvc.Controller;
import com.google.gdata.util.ServiceException;

/**
 * Display personalized greeting. This class contains boilerplate code to
 * consume the token but isn't integral to getting the tokens.
 */
public class ImportFromGoogleTask extends AsyncTask<Activity, Void, Void> {
	private static final String TAG = "TokenInfoTask";
	private static final String LOG_TAG = "ImportFromGoogleTask";
	protected ImportActivity mActivity;

	protected String mScope;
	protected String mEmail;
	protected ProgressDialog mProgressDialog;

	public ImportFromGoogleTask(ImportActivity activity, String email) {
		this.mActivity = activity;
		this.mEmail = email;
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		mProgressDialog = new ProgressDialog(mActivity);
		mProgressDialog.setTitle("Loading");
		mProgressDialog.setMessage("Poèkejte než se provede import...");
		mProgressDialog.show();
	}

	/*@Override
	protected void onPostExecute(Void result) {
		super.onPostExecute(result);

		// To dismiss the dialog
		mProgressDialog.dismiss();
	}*/

	@Override
	protected Void doInBackground(Activity... activities) {
		try {
			String first = ((EditText) activities[0]
					.findViewById(R.id.firstEditText)).getText().toString()
					.trim().toUpperCase();
			String second = ((EditText) activities[0]
					.findViewById(R.id.secondEditText)).getText().toString()
					.trim().toUpperCase();
			String name = ((EditText) activities[0]
					.findViewById(R.id.sheet_name)).getText().toString().trim();
			Log.d(LOG_TAG, first + second);
			if ((first.length() == 1) && (second.length() == 1)
					&& ((first.charAt(0) >= 'A') && (first.charAt(0) <= 'Z'))
					&& ((second.charAt(0) >= 'A') && (second.charAt(0) <= 'Z'))
					&& (name.length() > 0)) {
				ImportResult result = GoogleDocsImport.importFromGoogle(name,
						first.charAt(0), second.charAt(0), mActivity, mEmail, mActivity);

				mProgressDialog.dismiss();
				if (result != null){
					mActivity
							.toastAndFinish("Do pamìti bylo nahráno "
									+ result.addToDictionary
									+ " záznamù\nNa server bude nahráno "
									+ result.addToServer);
					Controller.getInstanceOf().setActiveSyncParams(new  DictionarySyncParam(name, first.charAt(0), second.charAt(0)));
				}
				
				// activities[0].finish();

			} else
				onError("Chybný formát zadaných parametrù", null);

		} catch (IOException ex) {
			onError("Following Error occured, please try again. "
					+ ex.getMessage(), ex);
		} catch (JSONException e) {
			onError("Bad response: " + e.getMessage(), e);
		} catch (ServiceException e) {
			onError("Nepodaøilo se naèíst data " + e.getMessage(), e);
			e.printStackTrace();
		} catch (ImportErrorException e) {
			onError("Import Error " + e.getMessage(), e);
		}
		return null;
	}

	protected void onError(String msg, Exception e) {
		if (e != null) {
			Log.e(TAG, "Exception: ", e);
		}
		mActivity.show(msg); // will be run in UI thread
	}	

}
