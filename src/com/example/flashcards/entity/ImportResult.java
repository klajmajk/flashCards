package com.example.flashcards.entity;

import java.util.List;

public class ImportResult {
	public int addToServer;
	public int addToDictionary;
	public List<Word> words;
	public ImportResult(int addToServer, int addToDictionary, List<Word> words) {
		super();
		this.addToServer = addToServer;
		this.addToDictionary = addToDictionary;
		this.words = words;
	}
	
	
	
}
