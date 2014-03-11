package com.example.flashcards.mvc;

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
	
	public static Controller getInstanceOf(){
		if(instance == null){
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
		this.activeDictionary= dictionary;
		
	}

	public void addNewWord(Word word, Topic topic) {

		Log.d(LOG_TAG, "Adding new word: "+word+" topic: "+topic);
		
	}
	
	
	

}
