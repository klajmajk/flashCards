package com.example.flashcards;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import com.example.flashcards.entity.LocaleAdapter;
import com.example.flashcards.mvc.Controller;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;

public class NewDictionaryFragment extends Fragment {

	private static final String LOG_TAG = "NewDictionaryFragment";
	private List<LocaleAdapter> mLocale;
	private Button submitButton;
	private AutoCompleteTextView first_editText, second_editText;
	private Fragment mFragment;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.fragment_new_dictionary,
				container, false);
		mLocale = new ArrayList<>();
		for (String lang : Locale.getISOLanguages()) {
			mLocale.add(new LocaleAdapter(new Locale(lang)));
		}
		mFragment = this;
		first_editText = (AutoCompleteTextView) rootView
				.findViewById(R.id.first_textView);
		second_editText = (AutoCompleteTextView) rootView
				.findViewById(R.id.second_textView);
		ArrayAdapter<LocaleAdapter> adapter = new ArrayAdapter<LocaleAdapter>(
				getActivity(), android.R.layout.simple_list_item_1, mLocale);
		Button submitButton = (Button) rootView.findViewById(R.id.add_button);
		submitButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Locale first = validLocale(first_editText);
				Locale second = validLocale(second_editText);
				if ((first != null) && (second != null)) {
					Controller.getInstanceOf().addNewDictionary(first, second);
					getActivity().getSupportFragmentManager().popBackStack();
					getActivity().getSupportFragmentManager()
							.beginTransaction().remove(mFragment).commit();
				}
			}

		});
		first_editText.setAdapter(adapter);
		first_editText.setThreshold(1);

		second_editText.setAdapter(adapter);
		second_editText.setThreshold(1);

		// TODO Auto-generated method stub
		return rootView;
	}

	protected Locale validLocale(AutoCompleteTextView editText) {
		for (LocaleAdapter localeAdapter : mLocale) {
			if (localeAdapter.getLocale().getDisplayLanguage()
					.equals(editText.getText().toString().trim()))
				return localeAdapter.getLocale();
		}
		editText.setError("Toto není známý jazyk");
		return null;
	}

}
