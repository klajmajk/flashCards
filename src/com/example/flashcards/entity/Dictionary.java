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
	private List<Word> words;
	private List<Topic> topics;
	private Locale first;
	private Locale second;
	public Dictionary(Locale first, Locale second) {
		super();
		this.first = first;
		this.second = second;
		this.topics = new ArrayList<Topic>();
		this.words = new ArrayList<Word>();
	}
	@Override
	public String toString() {
		return  first.getDisplayLanguage() + " - " + second.getDisplayLanguage();
	}
	public List<Word> getWords() {
		return words;
	}
	private Topic addTopic(Topic topic) {

		if(!topics.contains(topic)){
			topics.add(topic);
		}else{
			topic = topics.get(topics.indexOf(topic));
		}
		return topic;
		
	}
	public List<Topic> getTopics() {
		return topics;
		
	}
	
	
	public String toStringFull() {
		return "Dictionary [topics=" + topics + ", first=" + first
				+ ", second=" + second + "]";
	}
	public void addWord(Word word) {
		words.add(word);
		addTopic(word.getTopic());
		
	}
	
	
	
	
}
