package com.example.flashcards.mvc;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import com.example.flashcards.entity.Dictionary;

public class Model {
	private List<Dictionary> dictionaries;

	public Model() {
		super();
		this.dictionaries = new ArrayList<Dictionary> ();
		dictionaries.add(new Dictionary(new Locale("cs", "CZ"), Locale.GERMAN)); 
		dictionaries.add(new Dictionary(new Locale("cs", "CZ"), Locale.ENGLISH)); 		
	}

	public List<Dictionary> getDictionaries() {
		return dictionaries;
	}
	
	
	
	
}
