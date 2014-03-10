package com.example.flashcards.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class Dictionary {
	private List<Topic> topics;
	private Locale first;
	private Locale second;
	public Dictionary(Locale first, Locale second) {
		super();
		this.first = first;
		this.second = second;
		this.topics = new ArrayList<Topic>();
	}
	@Override
	public String toString() {
		return  first.getDisplayLanguage() + " - " + second.getDisplayLanguage();
	}
	
	
	
	
}
