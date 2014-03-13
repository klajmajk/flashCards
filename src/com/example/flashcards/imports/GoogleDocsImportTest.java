package com.example.flashcards.imports;

import java.util.Locale;

import org.junit.Test;

import com.example.flashcards.entity.Dictionary;

import junit.framework.TestCase;

public class GoogleDocsImportTest extends TestCase {

	@Test
	public void testImport() {

	   Dictionary dict = GoogleDocsImport.importFromGoogleDocs("adaklima", "Kun_ma_2_nohy");
	   // check if multiply(10,5) returns 50
	   assertEquals("dummy", new Dictionary(Locale.ENGLISH, Locale.GERMAN), dict);
	 } 

}
