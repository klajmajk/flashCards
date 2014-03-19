package com.example.flashcards.mvc;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import android.R.bool;
import android.content.Context;
import android.util.Log;

import com.example.flashcards.entity.Dictionary;
import com.example.flashcards.entity.Topic;
import com.example.flashcards.entity.Word;
import com.example.flashcards.persistence.Persistence;
import com.example.flashcards.utilities.DictionaryRandomizer;

public class Controller {
	private static final String LOG_TAG = "CONTROLLER";
	private Model model;
	private static Controller instance;
	private Dictionary activeDictionary;
	private DictionaryRandomizer randomizer;
	private Word activeWord;
	private boolean fromFirst;
	private List<Word> sessionWords;
	private Context context;

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

	public void setContext(Context context) {
		this.context = context;
		readPersistentData();
	}

	public Model getModel() {
		return model;
	}

	public Dictionary getActiveDictionary() {
		return activeDictionary;
	}

	public void setActiveDictionary(Dictionary dictionary) {
		this.activeDictionary = dictionary;
		Log.d(LOG_TAG, "Setting active dict: " + dictionary);

	}

	public void addNewWord(Word word) {
		activeDictionary.addWord(word);
		persist();
	}

	public void addImportWords(List<Word> readImportWords) {
		for (Word word : readImportWords) {
			activeDictionary.addWord(word);
		}
		persist();

	}

	public String getFirstText() {
		try {
			activeWord = DictionaryRandomizer.getRandom(sessionWords, fromFirst);

			Log.d(LOG_TAG, "getFirstText activeWord: " + activeWord);
			if (fromFirst)
				return activeWord.getFirst();
			else
				return activeWord.getSecond();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public String getSecondText() {
		try {
			Log.d(LOG_TAG, "activeWord activeWord: " + activeWord);
			if (fromFirst)
				return activeWord.getSecond();
			else
				return activeWord.getFirst();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public void wrongAnswer() {
		if (fromFirst) {
			int probabilityClass = activeWord.getProbabilityClassFromFirst();
			if (probabilityClass < 5)
				activeWord.setProbabilityClassFromFirst(probabilityClass + 1);
		} else {
			int probabilityClass = activeWord.getProbabilityClassFromSecond();
			if (probabilityClass < 5)
				activeWord.setProbabilityClassFromSecond(probabilityClass + 1);
		}

	}

	public void correctAnswer() {

		if (fromFirst) {
			int probabilityClass = activeWord.getProbabilityClassFromFirst();
			if (probabilityClass > 1)
				activeWord.setProbabilityClassFromFirst(probabilityClass - 1);
		} else {
			int probabilityClass = activeWord.getProbabilityClassFromSecond();
			if (probabilityClass > 1)
				activeWord.setProbabilityClassFromSecond(probabilityClass - 1);

		}

	}

	public void startLearningSession(List<Topic> topics, boolean fromFirst) {
		this.fromFirst = fromFirst;
		sessionWords = new ArrayList<>();
		Log.d(LOG_TAG, "Chosen topics: " + topics);
		for (Word word : activeDictionary.getWords()) {
			if (topics.contains(word.getTopic()))
				sessionWords.add(word);
		}
		Log.d(LOG_TAG, "seesionWord settup to: " + sessionWords);
	}

	public void persist() {
		Persistence.save((Serializable) model.getDictionaries(), context);
	}

	public void readPersistentData() {
		List<Dictionary> dicts = (List<Dictionary>) Persistence.read(context);
		model.setDictionaries(dicts);
		Log.d(LOG_TAG, "Dictionaries loaded: " + model.getDictionaries());

	}

	public void resetDictionaryData() {
		model.setDictionaries(null);
		this.activeDictionary = model.getDictionaries().get(0);
		persist();

	}

}
