package com.example.flashcards.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;



public class Dictionary implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
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
	public List<Word> getAllWords() {
		List<Word> result = new ArrayList<>();
		for (Topic topic : topics) {
			result.addAll(topic.getWords());			
		}
		return result;
	}
	public void addTopic(Topic topic) {
		topics.add(topic);
		
	}
	public List<Topic> getTopics() {
		return topics;
		
	}
	
	
	
	
}
