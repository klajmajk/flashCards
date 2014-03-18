package com.example.flashcards.utilities;

import java.util.List;
import java.util.Random;

import android.util.Log;

import com.example.flashcards.entity.Word;

public class DictionaryRandomizer {
	private static final String LOG_TAG = "DictionaryRandomizer";
    static Random rand = new Random();


    public static Word getRandom(List<Word> words) {
    	Log.d(LOG_TAG, "Words: " + words);

        int totalSum = 0;
        for(Word word : words) {
            totalSum = totalSum + word.getProbabilityClass();        
            Log.d(LOG_TAG, "WOrd: "+ word+" Probability: "+ word.getProbabilityClass());
        }

        int index = rand.nextInt(totalSum);
        int sum = 0;
        int i=0;
        while(sum <= index ) {
             sum = sum + words.get(i++).getProbabilityClass();
        }
        Log.d(LOG_TAG, "index: "+index+" i: "+i+" sum: "+sum);

    	Log.d(LOG_TAG, "Getting random form: "+words+" got: "+words.get(i-1) );
        return words.get(i-1);
    }
}
