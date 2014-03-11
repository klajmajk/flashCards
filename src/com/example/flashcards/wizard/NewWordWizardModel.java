package com.example.flashcards.wizard;

import android.content.Context;

import com.example.flashcards.mvc.Controller;
import com.example.flashcards.wizardpager.wizard.model.AbstractWizardModel;
import com.example.flashcards.wizardpager.wizard.model.BranchPage;
import com.example.flashcards.wizardpager.wizard.model.HandInputWordPage;
import com.example.flashcards.wizardpager.wizard.model.MultipleFixedChoicePage;
import com.example.flashcards.wizardpager.wizard.model.PageList;
import com.example.flashcards.wizardpager.wizard.model.SingleTopicChoicePage;

public class NewWordWizardModel extends AbstractWizardModel {

	public NewWordWizardModel(Context context) {
		super(context);
	}

	@Override
	protected PageList onNewRootPageList() {
		return new PageList(
				
				new SingleTopicChoicePage(this, "Vyberte sekci").setChoices(Controller
						.getInstanceOf().getActiveDictionary().getTopics()),
				new BranchPage(this, "Vybrat zadávání")
						.addBranch(
								"Automaticky",
								new HandInputWordPage(this, "")
										.setRequired(true))

						.addBranch(
								"Ruènì",
								new HandInputWordPage(this, "Ruèní zadání")
										.setRequired(true))

						.setRequired(true)

		);
	}
}
