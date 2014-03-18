package com.example.flashcards.persistence;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.io.StreamCorruptedException;


import android.content.Context;
import android.util.Log;

public class Persistence {
	private static final String LOG_TAG = "Persistence.db";
	static String FILENAME = "persistent";
	public static void save(Serializable obj, Context context){
		ObjectOutputStream os;

		try {
			
			FileOutputStream fos = context.openFileOutput(FILENAME, Context.MODE_PRIVATE);
			os = new ObjectOutputStream(fos);

			os.writeObject(obj);
			os.close();
		} catch (IOException e) {
			Log.e(LOG_TAG, "Error when saving", e);
		}
	}
	
	public static Object read(Context context){
		FileInputStream fis;
		Object obj = null;
		try {
			
			fis = context.openFileInput(FILENAME);
			ObjectInputStream is = new ObjectInputStream(fis);
			obj = (Object) is.readObject();
			is.close();
		} catch (FileNotFoundException e) {
			Log.e(LOG_TAG, "Error when reading", e);
		} catch (StreamCorruptedException e) {
			Log.e(LOG_TAG, "Error when reading", e);
		} catch (IOException e) {
			Log.e(LOG_TAG, "Error when reading", e);
		} catch (ClassNotFoundException e) {
			Log.e(LOG_TAG, "Error when reading", e);
		}
		return obj;
	}
}
