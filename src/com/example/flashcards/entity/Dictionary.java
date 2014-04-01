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
	private List<WordAction> actionQueue;
	private DictionarySyncParam syncParams;
	
	public Dictionary(Locale first, Locale second) {
		super();
		this.first = first;
		this.second = second;
		this.topics = new ArrayList<>();
		this.words = new ArrayList<>();
		this.actionQueue = new ArrayList<>();
		this.syncParams = null;
	}
	@Override
	public String toString() {
		return  first.getDisplayLanguage() + " - " + second.getDisplayLanguage();
	}
	public List<Word> getWords() {
		return words;
	}
	
	public DictionarySyncParam getSyncParams() {
		return syncParams;
	}
	public void setSyncParams(DictionarySyncParam syncParams) {
		this.syncParams = syncParams;
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
	public Locale getFirst() {
		return first;
	}
	public Locale getSecond() {
		return second;
	}
	public List<WordAction> getActionQueue() {
		return actionQueue;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((first == null) ? 0 : first.hashCode());
		result = prime * result + ((second == null) ? 0 : second.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Dictionary other = (Dictionary) obj;
		if (first == null) {
			if (other.first != null)
				return false;
		} 
		if (second == null) {
			if (other.second != null)
				return false;
		} 
		if(!(first.equals(other.first)&&(second.equals(other.second))||(first.equals(other.second)&&(second.equals(other.first))))) return false;
		return true;
	}
	
	
	
	
	
	
	
	
	
}
