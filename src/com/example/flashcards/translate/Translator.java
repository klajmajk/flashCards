package com.example.flashcards.translate;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Translator {
	public static Translation translate(String sl, String tl, String text)
			throws IOException {
		// fetch
		URL url = new URL(
				"http://translate.google.com/translate_a/t?client=p&hl=en&sl="
						+ sl
						+ "&tl="
						+ tl
						+ "&ie=UTF-8&oe=UTF-8&multires=1&oc=1&otf=2&ssel=0&tsel=0&sc=1&q="
						+ URLEncoder.encode(text, "UTF-8"));
		URLConnection urlConnection = url.openConnection();
		urlConnection.setRequestProperty("User-Agent", "Something Else");
		InputStreamReader in = new InputStreamReader(
				urlConnection.getInputStream(), "utf-8");
		BufferedReader br = new BufferedReader(in);
		String result = br.readLine();
		br.close();
		// parse

		System.out.println(result);
		return parse(result);
		
	}

	private static Translation parse(String result) {
		ObjectMapper mapper = new ObjectMapper();
		Map<String, Object> userData;
		List<String> otherTranslations= null;
		try {
			userData = mapper.readValue(result, Map.class);
			List<Object> list = (List<Object>) userData.get("dict");
			Map<String, Object> dictData = null;
			try {
				dictData = (Map<String, Object>) list
						.get(0);
				otherTranslations = (List<String>) (dictData
						.get("terms"));
			} catch (NullPointerException e) {
				//e.printStackTrace();
			}

			list = (List<Object>) userData.get("sentences");
			dictData = (Map<String, Object>) list.get(0);
			String translation = dictData.get("trans").toString();

			return new Translation(otherTranslations, translation);

		} catch (JsonParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;

	}
}
