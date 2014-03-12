package com.example.flashcards.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Topic  implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String name;	
	
	public Topic(String name) {
		super();
		this.name = name;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String toString() {
		return "Topic [ name=" + name + "]";
	}
	
	
	
	
	
	
	
}
