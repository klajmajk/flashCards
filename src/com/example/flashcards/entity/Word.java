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
	//-1 when not synced
	private int spreadsheetLine;
	
	
	
	
	public Word(String first, String second, List<String> wordUsage, Topic topic) {
		super();
		this.first = first;
		this.second = second;
		this.wordUsage = wordUsage;
		this.probabilityClassFromFirst = 5;
		this.probabilityClassFromSecond = 5;
		this.topic = topic;
		this.spreadsheetLine = -1;
	}
	
	public Word(String first, String second, List<String> wordUsage, Topic topic, int line) {

		this.first = first;
		this.second = second;
		this.wordUsage = wordUsage;
		this.probabilityClassFromFirst = 5;
		this.probabilityClassFromSecond = 5;
		this.topic = topic;
		this.spreadsheetLine = line;
	}	
	
	
	public Word(String first, String second, List<String> wordUsage,
			Topic topic, int probabilityClassFromFirst,
			int probabilityClassFromSecond) {
		super();
		this.first = first;
		this.second = second;
		this.wordUsage = wordUsage;
		this.topic = topic;
		this.probabilityClassFromFirst = probabilityClassFromFirst;
		this.probabilityClassFromSecond = probabilityClassFromSecond;
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
	

	public int getSpreadsheetLine() {
		return spreadsheetLine;
	}
	
	public boolean mergable(Word word){
		if((this.getFirst().equals(word.getFirst()))&&(this.getSecond().equals(word.getSecond()))){
			return true;
		}
		return false;
	}

	@Override
	public String toString() {
		return first + " | " + second+" "+probabilityClassFromFirst+" "+probabilityClassFromSecond+" line: "+spreadsheetLine;
	}

	public void setSpreadsheetLine(int spreadsheetLine) {
		this.spreadsheetLine = spreadsheetLine;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((first == null) ? 0 : first.hashCode());
		result = prime * result + probabilityClassFromFirst;
		result = prime * result + probabilityClassFromSecond;
		result = prime * result + ((second == null) ? 0 : second.hashCode());
		result = prime * result + spreadsheetLine;
		result = prime * result + ((topic == null) ? 0 : topic.hashCode());
		result = prime * result
				+ ((wordUsage == null) ? 0 : wordUsage.hashCode());
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
		Word other = (Word) obj;
		if (first == null) {
			if (other.first != null)
				return false;
		} else if (!first.equals(other.first))
			return false;
		if (probabilityClassFromFirst != other.probabilityClassFromFirst)
			return false;
		if (probabilityClassFromSecond != other.probabilityClassFromSecond)
			return false;
		if (second == null) {
			if (other.second != null)
				return false;
		} else if (!second.equals(other.second))
			return false;
		if (spreadsheetLine != other.spreadsheetLine)
			return false;
		if (topic == null) {
			if (other.topic != null)
				return false;
		} else if (!topic.equals(other.topic))
			return false;
		if (wordUsage == null) {
			if (other.wordUsage != null)
				return false;
		} else if (!wordUsage.equals(other.wordUsage))
			return false;
		return true;
	}
	
	



	
	
	
	
	
	
}
