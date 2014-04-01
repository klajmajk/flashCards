package com.example.flashcards.imports;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;

import android.app.Activity;
import android.util.Log;

import com.example.flashcards.IFromTaskCallBack;
import com.example.flashcards.entity.ActionType;
import com.example.flashcards.entity.DictionarySyncParam;
import com.example.flashcards.entity.ImportResult;
import com.example.flashcards.entity.Topic;
import com.example.flashcards.entity.Word;
import com.example.flashcards.entity.WordAction;
import com.example.flashcards.exceptions.ImportErrorException;
import com.example.flashcards.mvc.Controller;
import com.example.flashcards.utilities.ImportParser;
import com.google.android.gms.auth.GoogleAuthException;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.auth.UserRecoverableAuthException;
import com.google.gdata.client.spreadsheet.SpreadsheetService;
import com.google.gdata.data.PlainTextConstruct;
import com.google.gdata.data.spreadsheet.CellEntry;
import com.google.gdata.data.spreadsheet.CellFeed;
import com.google.gdata.data.spreadsheet.SpreadsheetEntry;
import com.google.gdata.data.spreadsheet.SpreadsheetFeed;
import com.google.gdata.data.spreadsheet.WorksheetEntry;
import com.google.gdata.data.spreadsheet.WorksheetFeed;
import com.google.gdata.util.ServiceException;

public class GoogleDocsImport {
	private static final String LOG_TAG = "GoogleDocsImport";
	private static final String SCOPE = "oauth2:https://spreadsheets.google.com/feeds/spreadsheets/private/full https://spreadsheets.google.com/feeds/";

	/**
	 * Slova importovaná z docu zmerguje s tìmi v zaøízení Ty co v zaøízení
	 * neexistují v zaøízení vytvoøí Ty co neexistují v docu pøidá do fronty
	 * WordAction
	 * 
	 * @param my
	 *            slovíèka v telefonu
	 * @param imported
	 *            slovíèka importovaná z google docu
	 * @return
	 */
	public static ImportResult merge(List<Word> my, List<Word> imported,
			Controller controller) {
		List<Word> result = new ArrayList<>();
		List<Word> toDelete = new ArrayList<>();
		Log.d(LOG_TAG, "my: " + my + "\nimported: " + imported);
		int addToServer = 0;
		int addToDictionary = 0;
		for (Word wordMy : my) {
			boolean merged = false;
			for (Word wordImported : imported) {
				if ((wordMy.getTopic().equals(wordImported.getTopic()))
						&& (wordMy.getSpreadsheetLine() == wordImported.getSpreadsheetLine())) {
					wordMy.setFirst(wordImported.getFirst());
					wordMy.setSecond(wordImported.getSecond());
					toDelete.add(wordImported);
					merged = true;
					break;
				} else if (wordMy.mergable(wordImported)) {
					wordMy.setSpreadsheetLine(wordImported.getSpreadsheetLine());
					toDelete.add(wordImported);
					merged = true;
					break;
				}
			}
			result.add(wordMy);
			if (!merged) {
				// TODO add to WordActions musí být pøidáno na server
				controller.addToWordActionQueue(wordMy, ActionType.ADD);
				addToServer++;
			}
		}
		// Bez tech co byly zmergovany
		imported.removeAll(toDelete);
		addToDictionary += imported.size();
		result.addAll(imported);
		Log.d(LOG_TAG, "toDelete: " + toDelete);
		Log.d(LOG_TAG, "imported: " + imported);
		Log.d(LOG_TAG, "result: " + result);
		return new ImportResult(addToServer, addToDictionary, result);
	}

	public static void syncWithServer(Activity activity, String email,
			Controller controller, IFromTaskCallBack forDialog) {

		try {
			DictionarySyncParam params = controller.getActiveDictionary()
					.getSyncParams();
			if (params != null) {
				synchronize(activity, email, controller, forDialog, params);
			}

		} catch (IOException | ServiceException | ImportErrorException
				| JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Log.d(LOG_TAG, "sync finished");

	}

	private static void synchronize(Activity activity, String email,
			Controller controller, IFromTaskCallBack forDialog,
			DictionarySyncParam params) throws IOException, ServiceException,
			ImportErrorException, JSONException {
		String token = fetchToken(activity, email, forDialog);
		if (token == null) {
			// error has already been handled in fetchToken()
			return;
		}
		List<WordAction> queue = controller.getActiveDictionary()
				.getActionQueue();
		List<WordAction> done = new ArrayList<>();
		Log.d(LOG_TAG, "Queue: " + queue);
		if (queue.size() > 0) {
			SpreadsheetService service = setUpService(token);
			SpreadsheetEntry wordsFile;
			Log.d(LOG_TAG, "params: " + params + " token: " + token
					+ " service: " + service);
			wordsFile = getWordsSpredsheet(params.getName(), token, service);
			WorksheetFeed worksheetFeed = service.getFeed(
					wordsFile.getWorksheetFeedUrl(), WorksheetFeed.class);

			List<WorksheetEntry> worksheets = worksheetFeed.getEntries();
			for (WordAction action : queue) {
				WorksheetEntry topicSheet = null;
				for (WorksheetEntry worksheetEntry : worksheets) {
					if (worksheetEntry.getTitle().getPlainText()
							.equals(action.getWord().getTopic().getName())) {
						topicSheet = worksheetEntry;
						break;
					}
				}
				if (topicSheet == null)
					topicSheet = getNewWorksheet(service, wordsFile, action
							.getWord().getTopic());
				boolean handeled = handleAction(topicSheet, action, service,
						params, controller);
				if (handeled)
					done.add(action);
			}
			queue.removeAll(done);

		}
		importFromGoogle(params.getName(), params.getFirstCol(),
				params.getSecondCol(), activity, email, forDialog);
	}

	private static WorksheetEntry getNewWorksheet(SpreadsheetService service,
			SpreadsheetEntry wordsFile, Topic topic) throws IOException,
			ServiceException {
		WorksheetEntry worksheet = new WorksheetEntry();
		worksheet.setTitle(new PlainTextConstruct(topic.getName()));
		worksheet.setColCount(10);
		worksheet.setRowCount(20);

		// Send the local representation of the worksheet to the API for
		// creation. The URL to use here is the worksheet feed URL of our
		// spreadsheet.
		URL worksheetFeedUrl = wordsFile.getWorksheetFeedUrl();
		service.insert(worksheetFeedUrl, worksheet);
		WorksheetFeed worksheetFeed = service.getFeed(
				wordsFile.getWorksheetFeedUrl(), WorksheetFeed.class);
		return worksheetFeed.getEntries().get(
				worksheetFeed.getEntries().size() - 1);
	}

	private static boolean handleAction(WorksheetEntry worksheetEntry,
			WordAction action, SpreadsheetService service,
			DictionarySyncParam params, Controller controller) {
		Log.d(LOG_TAG, "Handling: " + action);
		switch (action.getType()) {
		case ADD: {
			worksheetEntry.setRowCount(worksheetEntry.getRowCount() + 1);
			// Fetch the cell feed of the worksheet.
			URL cellFeedUrl = worksheetEntry.getCellFeedUrl();
			CellFeed cellFeed;
			try {
				cellFeed = service.getFeed(cellFeedUrl, CellFeed.class);
				int firstEmptyRow = firstEmptyRow(cellFeed);
				insertOnLine(action, params, cellFeed, firstEmptyRow);
				controller.setWordsLine(action.getWord(), firstEmptyRow);
				return true;
			} catch (IOException | ServiceException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			break;
		}
		case UPDATE:
			URL cellFeedUrl = worksheetEntry.getCellFeedUrl();
			CellFeed cellFeed;
			try {
				cellFeed = service.getFeed(cellFeedUrl, CellFeed.class);
				insertOnLine(action, params, cellFeed, action.getWord()
						.getSpreadsheetLine());
				return true;
			} catch (IOException | ServiceException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			break;
		case DELETE:

			break;

		default:
			break;
		}

		return false;
	}

	private static void insertOnLine(WordAction action,
			DictionarySyncParam params, CellFeed cellFeed, int line)
			throws ServiceException, IOException {
		int firstColumn = Character.toLowerCase(params.getFirstCol()) - 'a' + 1;
		int secondColumn = Character.toLowerCase(params.getSecondCol()) - 'a' + 1;
		CellEntry first = new CellEntry(line, firstColumn, action.getWord()
				.getFirst());
		cellFeed.insert(first);
		CellEntry second = new CellEntry(line, secondColumn, action.getWord()
				.getSecond());
		cellFeed.insert(second);
	}

	private static int firstEmptyRow(CellFeed cellFeed) {
		int highestFilledRow = 0;
		for (CellEntry cell : cellFeed.getEntries()) {
			String itemAddress = cell.getTitle().getPlainText();
			int line = Integer.parseInt(itemAddress.substring(1));
			Log.d(LOG_TAG, "line: " + line + " highest: " + highestFilledRow);
			if (line > highestFilledRow)
				highestFilledRow = line;
		}
		return highestFilledRow + 1;
	}

	public static ImportResult importFromGoogle(String name, char firstCol,
			char secondCol, Activity activity, String email,
			IFromTaskCallBack forDialog) throws IOException, JSONException,
			ServiceException, ImportErrorException {
		String token = fetchToken(activity, email, forDialog);
		if (token == null) {
			// error has already been handled in fetchToken()
			return null;
		}

		SpreadsheetService service = setUpService(token);
		SpreadsheetEntry wordsFile = getWordsSpredsheet(name, token, service);
		List<Word> imported = readImportWords(wordsFile, service, firstCol,
				secondCol);
		ImportResult result = Controller.getInstanceOf().handleImport(imported);

		return result;
	}

	private static List<Word> readImportWords(SpreadsheetEntry wordsFile,
			SpreadsheetService service, char firstCol, char secondCol) {
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
					List<Word> toAdd = ImportParser.parseCellEntries(entries,
							worksheetEntry, firstCol, secondCol, worksheetEntry
									.getTitle().getPlainText());

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

	private static SpreadsheetEntry getWordsSpreadsheetFromFeed(String name,
			List<SpreadsheetEntry> entries) throws ImportErrorException {
		for (SpreadsheetEntry spreadsheetEntry : entries) {
			if (spreadsheetEntry.getTitle().getPlainText().equals(name))
				return spreadsheetEntry;
		}
		throw new ImportErrorException("Zvolený soubor neexistuje");
	}

	/**
	 * Get a authentication token if one is not available. If the error is not
	 * recoverable then it displays the error message on parent activity.
	 */
	protected static String fetchToken(Activity activity, String email,
			IFromTaskCallBack forDialog) throws IOException {
		try {
			Log.d(LOG_TAG, "email: " + email);
			return GoogleAuthUtil.getToken(activity, email, SCOPE);
		} catch (UserRecoverableAuthException userRecoverableException) {
			// GooglePlayServices.apk is either old, disabled, or not present,
			// which is
			// recoverable, so we need to show the user some UI through the
			// activity.
			forDialog.handleException(userRecoverableException);
		} catch (GoogleAuthException fatalException) {
			onError("Unrecoverable error " + fatalException.getMessage(),
					fatalException, forDialog);
		}
		return null;
	}

	private static SpreadsheetEntry getWordsSpredsheet(String name,
			String token, SpreadsheetService service) throws IOException,
			ServiceException, ImportErrorException {
		Log.d(LOG_TAG, token);
		URL metafeedUrl = new URL(
				"https://spreadsheets.google.com/feeds/spreadsheets/private/full");
		SpreadsheetFeed feed = service.getFeed(metafeedUrl,
				SpreadsheetFeed.class);

		return getWordsSpreadsheetFromFeed(name, feed.getEntries());
	}

	private static SpreadsheetService setUpService(String token) {
		SpreadsheetService service = null;
		service = new SpreadsheetService("Spreadsheet");
		service.setProtocolVersion(SpreadsheetService.Versions.V3);
		service.setHeader("Authorization", "Bearer " + token);
		return service;
	}

	protected static void onError(String msg, Exception e,
			IFromTaskCallBack forDialog) {
		if (e != null) {
			Log.e(LOG_TAG, "Exception: ", e);
		}
		forDialog.show(msg); // will be run in UI thread
	}

}
