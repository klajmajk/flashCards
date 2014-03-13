package com.example.flashcards.imports;
import java.net.URL;
import java.util.List;

import com.example.flashcards.entity.Dictionary;
import com.google.gdata.client.spreadsheet.SpreadsheetService;
import com.google.gdata.data.spreadsheet.SpreadsheetEntry;
import com.google.gdata.data.spreadsheet.SpreadsheetFeed;


public class GoogleDocsImport {
	public static Dictionary importFromGoogleDocs(String username, String password){
		List<SpreadsheetEntry> entries = getSpreadsheets(username, password);
		System.out.println(entries.toString());
		return getDictionary(entries);
		
	}

	private static Dictionary getDictionary(List<SpreadsheetEntry> entries) {
		for (SpreadsheetEntry spreadsheetEntry : entries) {
			
		}
		return null;
	}

	private static List<SpreadsheetEntry> getSpreadsheets(String username, String password){
		SpreadsheetService service = null;
		try {
		    service = new SpreadsheetService("Spreadsheet");
		    service.setProtocolVersion(SpreadsheetService.Versions.V3);
		    service.setUserCredentials("username", "password");//permission required to add in Manifest
		    URL metafeedUrl = new URL("https://spreadsheets.google.com/feeds/spreadsheets/private/full");
		    SpreadsheetFeed feed = service.getFeed(metafeedUrl, SpreadsheetFeed.class);

		    return  feed.getEntries();
		} catch (Exception e) {
		    e.printStackTrace();
		}
		return null;
	}
}
