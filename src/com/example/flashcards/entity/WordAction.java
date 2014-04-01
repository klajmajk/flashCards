package com.example.flashcards.entity;

import java.io.Serializable;

public class WordAction implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Word word;
	private ActionType type;
	public WordAction(Word word, ActionType type) {
		super();
		this.word = word;
		this.type = type;
	}
	public Word getWord() {
		return word;
	}
	public ActionType getType() {
		return type;
	}
	@Override
	public String toString() {
		return "WordAction [word=" + word + ", type=" + type + "]";
	}
	
	
	
	
}
