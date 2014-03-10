package com.example.flashcards.mvc;

public class Controller {
	private Model model;
	private static Controller instance;

	public Controller() {
		super();
		this.model = new Model();
	}
	
	public static Controller getInstanceOf(){
		if(instance == null){
			instance = new Controller();			
		}
		return instance;
		
	}

	public Model getModel() {
		return model;
	}
	
	

}
