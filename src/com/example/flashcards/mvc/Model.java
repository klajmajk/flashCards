package com.example.flashcards.mvc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import com.example.flashcards.entity.Dictionary;
import com.example.flashcards.entity.Topic;
import com.example.flashcards.entity.Word;

public class Model {
	private List<Dictionary> dictionaries;

	public Model() {
		super();
		/*this.dictionaries = new ArrayList<Dictionary> ();
		dictionaries.add(new Dictionary(new Locale("cs", "CZ"), Locale.GERMAN)); 
		dictionaries.add(new Dictionary(new Locale("cs", "CZ"), Locale.ENGLISH)); 	
		String [] array = {"one","two"};
		dictionaries.get(0).addWord(new Word("pes", "der Hund", new ArrayList<String>(Arrays.asList(array)),new Topic("misc")));
		*/
		}

	public List<Dictionary> getDictionaries() {
		return dictionaries;
	}

	public void setDictionaries(List<Dictionary> dictionaries) {
		if (dictionaries == null){ 
			this.dictionaries = new ArrayList<Dictionary> ();			
			this.dictionaries.add(new Dictionary(new Locale("cs", "CZ"), Locale.GERMAN)); 
			this.dictionaries.add(new Dictionary(new Locale("cs", "CZ"), Locale.ENGLISH)); 	
		}else		this.dictionaries = dictionaries;
	}
	
	
	
	
	
	
	
	
}
