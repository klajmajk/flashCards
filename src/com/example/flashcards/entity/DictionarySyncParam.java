package com.example.flashcards.entity;

import java.io.Serializable;

public class DictionarySyncParam implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String name;
	private char firstCol;
	private char secondCol;
	public DictionarySyncParam(String name, char firstCol, char secondCol) {
		super();
		this.name = name;
		this.firstCol = firstCol;
		this.secondCol = secondCol;
	}
	public String getName() {
		return name;
	}
	public char getFirstCol() {
		return firstCol;
	}
	public char getSecondCol() {
		return secondCol;
	}
	
	
	
	
}
