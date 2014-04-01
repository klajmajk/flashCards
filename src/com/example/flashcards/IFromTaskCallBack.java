package com.example.flashcards;


public interface IFromTaskCallBack {
	
    public void show(final String message);
    public void handleException(final Exception e);
    public void taskFinished();
    
}
