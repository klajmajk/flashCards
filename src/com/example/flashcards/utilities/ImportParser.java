package com.example.flashcards.utilities;

import java.util.ArrayList;
import java.util.List;

import android.util.Log;

import com.example.flashcards.entity.Topic;
import com.example.flashcards.entity.Word;
import com.example.flashcards.exceptions.ImportErrorException;
import com.google.gdata.data.spreadsheet.CellEntry;
import com.google.gdata.data.spreadsheet.WorksheetEntry;

public class ImportParser {

	private static final String LOG_TAG = "Import parser";

	public static List<Word> parseCellEntries(List<CellEntry> entries, WorksheetEntry worksheetEntry, char firstCol, char secondCol, String topic) throws ImportErrorException {
		int line = 1;
		String first = null;
		String second = null;
		List<Word> words = new ArrayList<>();
		for (int i = 0; i < entries.size(); i++) {
			String itemAddress = entries.get(i).getTitle().getPlainText();
			Log.d(LOG_TAG, "line: "+line+" itemAddress ss: "+itemAddress.substring(1));
			if (Integer.parseInt(itemAddress.substring(1)) != line) {
				// jsme na nov�m ��dku
				if ((first == null) || (second == null))
					throw new ImportErrorException(
							"Importovan� slovn�k obsahuje pr�zdn� pole");
				else {
					Word word = new Word(first, second, null,
							new Topic(topic));
					words.add(word);
					Log.d(LOG_TAG, "importing: " + word.toString());
					line++;
				}
			}
			if ((itemAddress.charAt(0) == firstCol) &&
			// TODO tady by to chtelo chytreji
					(line == Integer.parseInt(itemAddress
							.substring(1)))) {
				first = entries.get(i).getPlainTextContent();
			}

			if ((itemAddress.charAt(0) == secondCol) &&
			// TODO tady by to chtelo chytreji
					(line == Integer.parseInt(itemAddress
							.substring(1)))) {
				second = entries.get(i).getPlainTextContent();
			}
			// Print the cell's address in A1 notation
		}
		return words;
	}

}
