package com.example.flashcards.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Topic  implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private List<Word> words;
	private String name;	
	
	public Topic(String name) {
		super();
		this.name = name;
		this.words = new ArrayList<Word>();
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public List<Word> getWords() {
		return words;
	}	
	public void addWord(Word word){
		words.add(word);
	}
	
}
