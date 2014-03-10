package com.example.flashcards.entity;

import java.util.List;

public class Word {
	//translations in first and second language where the second is the one to be learned
	private String first;
	private String second;
	private List<String> wordUsage;
	private double rating;
	
	
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
	public double getRating() {
		return rating;
	}
	public void setRating(double rating) {
		this.rating = rating;
	}
	
	
	
	
}
