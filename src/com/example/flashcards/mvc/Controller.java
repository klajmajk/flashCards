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
	private Word activeWord;
	private boolean fromFirst;
	private List<Word> sessionWords;
	private Context context = null;
	private boolean  learningSessionRunning = false;

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
		if(this.context == null){

			Log.d(LOG_TAG, "Setting context and reading persistent data");
			this.context = context;
			readPersistentData();
		}else this.context = context;
		
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
		
		//persist();

	}
	
	public void learningSessionCheck(){
		if(learningSessionRunning){
			persist();
			learningSessionRunning = false;
		}
	}

	public void startLearningSession(List<Topic> topics, boolean fromFirst) {
		this.fromFirst = fromFirst;
		this.learningSessionRunning  = true;
		sessionWords = new ArrayList<>();
		Log.d(LOG_TAG, "Chosen topics: " + topics);
		for (Word word : activeDictionary.getWords()) {
			if (topics.contains(word.getTopic()))
				sessionWords.add(word);
		}
		newWord();
		Log.d(LOG_TAG, "seesionWord settup to: " + sessionWords);
	}

	public void persist() {
		Runnable toRun = new Runnable() {
			
			@Override
			public void run() {
				Persistence.save((Serializable) model.getDictionaries(), context);
				
			}
		};
		toRun.run();
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

	public boolean isFromFirst() {
		return fromFirst;
	}

	public void newWord() {
		activeWord = DictionaryRandomizer.getRandom(sessionWords, fromFirst);
		
	}
	public double[] getTopicLearnedTest(Topic topic, List<Word> allWords){
		List<Word> words = new ArrayList<>();
		for (Word word : allWords) {
			if(word.getTopic().equals(topic)) words.add(word);
		}	
		double learnedFirst = 0;
		double learnedSecond = 0;
		int size = words.size();
		for (Word word : words) {
			System.out.println();
			learnedFirst += (((double)Math.abs(word.getProbabilityClassFromFirst()-5))/4)*((double)1/size);
			learnedSecond += (((double)Math.abs(word.getProbabilityClassFromSecond()-5))/4)*((double)1/size);
		}
		
		double[] result = new double [2];
		result[0] = learnedFirst;
		result[1] = learnedSecond;
		return result;
		
	}
	
	public double[] getActiveTopicLearned(Topic topic){
		return getTopicLearnedTest(topic, activeDictionary.getWords());
	}

	

	public void editWord(Word mWord, String first, String second) {
		int pos = activeDictionary.getWords().indexOf(mWord);
		if(pos!=-1)		{	
			Word word = activeDictionary.getWords().get(pos);
			//TODO tady by to melo hazet vyjimku kdyz ta podminka neprojde
			if((first!=null)&&(second!=null)&&(first.trim()!="")&&(second.trim()!="")){
				word.setFirst(first.trim());
				word.setSecond(second.trim());
				persist();
			}		
			
		}
		
		
	}

	public Word getActiveWord() {
		return activeWord;
	}

}
