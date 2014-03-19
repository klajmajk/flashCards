package com.example.flashcards.mvc;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import com.example.flashcards.entity.*;

public class ControllerTest {

	@Test
	public void testGetTopicLearnedTest() {
		List<Word> words = new ArrayList<>();
		Topic topic = new Topic("dpc");
		words.add(new Word("a", "b", null, topic,5,5));
		words.add(new Word("d", "c", null, topic,1,1));
		double[] expResult = {0.5,0.5};
		double[] result = Controller.getInstanceOf().getTopicLearnedTest(topic, words);
		assertArrayEquals(expResult, result,0.000001);
		
		
		
	}

}
