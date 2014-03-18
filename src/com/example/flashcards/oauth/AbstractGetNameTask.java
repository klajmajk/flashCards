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

import com.example.flashcards.HelloActivity;
import com.example.flashcards.entity.Topic;
import com.example.flashcards.entity.Word;
import com.example.flashcards.exceptions.ImportErrorException;
import com.example.flashcards.mvc.Controller;
import com.example.flashcards.utilities.ImportParser;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.gdata.client.spreadsheet.CellQuery;
import com.google.gdata.client.spreadsheet.SpreadsheetService;
import com.google.gdata.data.spreadsheet.CellEntry;
import com.google.gdata.data.spreadsheet.CellFeed;
import com.google.gdata.data.spreadsheet.SpreadsheetEntry;
import com.google.gdata.data.spreadsheet.SpreadsheetFeed;
import com.google.gdata.data.spreadsheet.Worksheet;
import com.google.gdata.data.spreadsheet.WorksheetEntry;
import com.google.gdata.data.spreadsheet.WorksheetFeed;
import com.google.gdata.util.ServiceException;

import com.example.flashcards.R;
import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.EditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Display personalized greeting. This class contains boilerplate code to
 * consume the token but isn't integral to getting the tokens.
 */
public abstract class AbstractGetNameTask extends
		AsyncTask<Activity, Void, Void> {
	private static final String TAG = "TokenInfoTask";
	private static final String NAME_KEY = "given_name";
	private static final String LOG_TAG = "AbstractGetNameTask";
	protected HelloActivity mActivity;

	protected String mScope;
	protected String mEmail;
	protected ProgressDialog mProgressDialog;

	AbstractGetNameTask(HelloActivity activity, String email, String scope) {
		this.mActivity = activity;
		this.mScope = scope;
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

	@Override
	protected void onPostExecute(Void result) {
		super.onPostExecute(result);

		// To dismiss the dialog
		mProgressDialog.dismiss();
	}

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
				fetchSheetsFromGoogle(name, first.charAt(0), second.charAt(0));
				activities[0].finish();

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

	/**
	 * Get a authentication token if one is not available. If the error is not
	 * recoverable then it displays the error message on parent activity.
	 */
	protected abstract String fetchToken() throws IOException;

	private void fetchSheetsFromGoogle(String name, char firstCol,
			char secondCol) throws IOException, JSONException,
			ServiceException, ImportErrorException {
		String token = fetchToken();
		if (token == null) {
			// error has already been handled in fetchToken()
			return;
		}
		SpreadsheetService service = null;
		service = new SpreadsheetService("Spreadsheet");
		service.setProtocolVersion(SpreadsheetService.Versions.V3);
		service.setHeader("Authorization", "Bearer " + token);
		Log.d(LOG_TAG, token);
		URL metafeedUrl = new URL(
				"https://spreadsheets.google.com/feeds/spreadsheets/private/full");
		SpreadsheetFeed feed = service.getFeed(metafeedUrl,
				SpreadsheetFeed.class);
		
		SpreadsheetEntry wordsFile = getWordsSheet(name, feed.getEntries());
		List<Word> list = readImportWords(wordsFile, service, firstCol,
				secondCol);
		Controller.getInstanceOf().addImportWords(list);
		

		Log.d(LOG_TAG, "first:" + firstCol + secondCol);
		return;
	}

	private List<Word> readImportWords(SpreadsheetEntry wordsFile,
			SpreadsheetService service, char firstCol, char secondCol)
			{
		WorksheetFeed worksheetFeed;
		List<Word> result = new ArrayList<>();
		try {
			worksheetFeed = service.getFeed(wordsFile.getWorksheetFeedUrl(),
					WorksheetFeed.class);

			List<WorksheetEntry> worksheets = worksheetFeed.getEntries();
			for (WorksheetEntry worksheetEntry : worksheets) {

				URL cellFeedUrl;
				try {
					cellFeedUrl = new URI(worksheetEntry.getCellFeedUrl()
							.toString()).toURL();
					CellFeed cellFeed = service.getFeed(cellFeedUrl,
							CellFeed.class);

					// Iterate through each cell, printing its value.
					List<CellEntry> entries = cellFeed.getEntries();
					// Importing takes place here

					List<Word> toAdd = ImportParser.parseCellEntries(entries,
							worksheetEntry, firstCol, secondCol, worksheetEntry
									.getTitle().getPlainText());

					Log.d(LOG_TAG, "toAdd:" + toAdd.toString());
					result.addAll(toAdd);
				} catch (URISyntaxException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		} catch (IOException | ServiceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}

	private SpreadsheetEntry getWordsSheet(String name,
			List<SpreadsheetEntry> entries) throws ImportErrorException {
		for (SpreadsheetEntry spreadsheetEntry : entries) {
			if (spreadsheetEntry.getTitle().getPlainText().equals(name))
				return spreadsheetEntry;
		}
		throw new ImportErrorException("Zvolený soubor neexistuje");
	}

	/**
	 * Reads the response from the input stream and returns it as a string.
	 */
	private static String readResponse(InputStream is) throws IOException {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		byte[] data = new byte[2048];
		int len = 0;
		while ((len = is.read(data, 0, data.length)) >= 0) {
			bos.write(data, 0, len);
		}
		return new String(bos.toByteArray(), "UTF-8");
	}

	/**
	 * Parses the response and returns the first name of the user.
	 * 
	 * @throws JSONException
	 *             if the response is not JSON or if first name does not exist
	 *             in response
	 */
	private String getFirstName(String jsonResponse) throws JSONException {
		JSONObject profile = new JSONObject(jsonResponse);
		return profile.getString(NAME_KEY);
	}
}
