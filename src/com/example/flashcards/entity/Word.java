package com.example.flashcards.entity;

import java.io.Serializable;
import java.util.List;

public class Word  implements Serializable{
	//translations in first and second language where the second is the one to be learned
	private String first;
	private String second;
	private List<String> wordUsage;
	private Topic topic;
	//pravdìpodobnost zobrazení 5 nejèastìji (neumím) 1 nejménì èasto (umím)
	private int probabilityClassFromFirst;
	private int probabilityClassFromSecond;
	
	
	
	
	public Word(String first, String second, List<String> wordUsage, Topic topic) {
		super();
		this.first = first;
		this.second = second;
		this.wordUsage = wordUsage;
		this.probabilityClassFromFirst = 5;
		this.topic = topic;
	}
	
	
	
	public Word() {
		super();
		this.probabilityClassFromFirst = 1;
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
	public int getProbabilityClassFromFirst() {
		return probabilityClassFromFirst;
	}
	public void setProbabilityClassFromFirst(int rating) {
		this.probabilityClassFromFirst = rating;		
	}
	
	
	public int getProbabilityClassFromSecond() {
		return probabilityClassFromSecond;
	}



	public void setProbabilityClassFromSecond(int probablilityClassFromSecond) {
		this.probabilityClassFromSecond = probablilityClassFromSecond;
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
