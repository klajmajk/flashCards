package com.example.flashcards.translate;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Test;

public class TranslatorTest {

	@Test
	public void testTranslate() {
		Translation result;
		try {
			result = Translator.translate("cs", "de", "zákon o životním prostøedí");
			System.out.println(result.toString());

			assertEquals("zákon o životním prostøedí", result);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
