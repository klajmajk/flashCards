package com.example.flashcards.entity;

import java.io.Serializable;
import java.util.List;

public class Word  implements Serializable{
	//translations in first and second language where the second is the one to be learned
	private String first;
	private String second;
	private List<String> wordUsage;
	private Topic topic;
	//pravd�podobnost zobrazen� 5 nej�ast�ji (neum�m) 1 nejm�n� �asto (um�m)
	private int rating;
	
	
	
	
	public Word(String first, String second, List<String> wordUsage, Topic topic) {
		super();
		this.first = first;
		this.second = second;
		this.wordUsage = wordUsage;
		this.rating = 5;
		this.topic = topic;
	}
	
	
	
	public Word() {
		super();
		this.rating = 1;
	}



	public String getFirst() {
		return first;
	}
	public void setFirst(String first) {
		this.first = first;
	}
	public String getSecond() {
		return second;
	}
	public void setSecond(String second) {
		this.second = second;
	}
	public List<String> getWordUsage() {
		return wordUsage;
	}
	public void setWordUsage(List<String> wordUsage) {
		this.wordUsage = wordUsage;
	}
	public int getProbabilityClass() {
		return rating;
	}
	public void setProbablilityClass(int rating) {
		this.rating = rating;
	}
	
	public Topic getTopic() {
		return topic;
	}



	public void setTopic(Topic topic) {
		this.topic = topic;
	}



	@Override
	public String toString() {
		return first + " | " + second;
	}
	
	
	
	
	
}
