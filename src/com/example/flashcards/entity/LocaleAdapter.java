package com.example.flashcards.entity;

import java.util.Locale;

public class LocaleAdapter {
	public Locale locale;

	public LocaleAdapter(Locale locale) {
		super();
		this.locale = locale;
	}

	public Locale getLocale() {
		return locale;
	}

	@Override
	public String toString() {
		return locale.getDisplayLanguage();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((locale == null) ? 0 : locale.hashCode());
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
		LocaleAdapter other = (LocaleAdapter) obj;
		if (locale == null) {
			if (other.locale != null)
				return false;
		} else if (!locale.equals(other.locale))
			return false;
		return true;
	}
	
	
	
	
}
