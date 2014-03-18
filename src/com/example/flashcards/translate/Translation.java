package com.example.flashcards.translate;

import java.util.List;

public class Translation {
	private List<String> otherTranslations;
	private String translation;
	public Translation(List<String> otherTranslations, String translation) {
		super();
		this.otherTranslations = otherTranslations;
		this.translation = translation;
	}
	public List<String> getOtherTranslations() {
		return otherTranslations;
	}
	public String getTranslation() {
		return translation;
	}
	@Override
	public String toString() {
		return "Translation [otherTranslations=" + otherTranslations
				+ ", translation=" + translation + "]";
	}
	
	
}
