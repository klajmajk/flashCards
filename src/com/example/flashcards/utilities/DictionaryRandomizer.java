package com.example.flashcards.utilities;

import java.util.Random;

import com.example.flashcards.entity.Dictionary;
import com.example.flashcards.entity.Word;

public class DictionaryRandomizer {
	private Dictionary dictionary;
    Random rand = new Random();
    int totalSum = 0;

    public DictionaryRandomizer(Dictionary dictionary) {
    	this.dictionary = dictionary;
        for(Word word : dictionary.getWords()) {
            totalSum = totalSum + word.getProbabilityClass();            
        }
    }

    public Word getRandom() {

        int index = rand.nextInt(totalSum);
        int sum = 0;
        int i=0;
        while(sum < index ) {
             sum = sum + dictionary.getWords().get(i++).getProbabilityClass();
        }
        return dictionary.getWords().get(i-1);
    }
}
