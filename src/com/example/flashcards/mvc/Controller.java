package com.example.flashcards.mvc;

import java.util.List;

import android.util.Log;

import com.example.flashcards.entity.Dictionary;
import com.example.flashcards.entity.Topic;
import com.example.flashcards.entity.Word;

public class Controller {
	private static final String LOG_TAG = "CONTROLLER";
	private Model model;
	private static Controller instance;
	private Dictionary activeDictionary;

	public Controller() {
		super();
		this.model = new Model();
	}

	public static Controller getInstanceOf() {
		if (instance == null) {
			instance = new Controller();
		}
		return instance;

	}

	public Model getModel() {
		return model;
	}

	public Dictionary getActiveDictionary() {
		return activeDictionary;
	}

	public void setActiveDictionary(Dictionary dictionary) {
		this.activeDictionary = dictionary;

	}

	public void addNewWord(Word word) {

		Log.d(LOG_TAG, "Adding new word: " + word );
		activeDictionary.addWord(word);
		Log.d(LOG_TAG, "AddWord activeDict:" + activeDictionary.toStringFull());
	}

	public void addImportWords(List<Word> readImportWords) {
		for (Word word : readImportWords) {
			addNewWord(word);
		}
		
	}

}
