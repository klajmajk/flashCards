package com.example.flashcards;

import java.util.List;

import com.example.flashcards.entity.Dictionary;
import com.example.flashcards.entity.Topic;
import com.example.flashcards.entity.Word;
import com.example.flashcards.mvc.Controller;
import com.example.flashcards.wizard.NewWordWizardModel;
import com.example.flashcards.wizard.SandwichWizardModel;
import com.example.flashcards.wizardpager.wizard.model.AbstractWizardModel;
import com.example.flashcards.wizardpager.wizard.model.HandInputWordPage;
import com.example.flashcards.wizardpager.wizard.model.ModelCallbacks;
import com.example.flashcards.wizardpager.wizard.model.Page;
import com.example.flashcards.wizardpager.wizard.model.SingleTopicChoicePage;
import com.example.flashcards.wizardpager.wizard.ui.PageFragmentCallbacks;
import com.example.flashcards.wizardpager.wizard.ui.ReviewFragment;
import com.example.flashcards.wizardpager.wizard.ui.StepPagerStrip;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
public class NewWordFragment extends Fragment implements PageFragmentCallbacks,
		ReviewFragment.Callbacks, ModelCallbacks {
	private static final String LOG_TAG = "NEW_WORD_FRAGMENT";
	private ViewPager mPager;
	private MyPagerAdapter mPagerAdapter;

	private boolean mEditingAfterReview;

	private AbstractWizardModel mWizardModel = new NewWordWizardModel(getActivity());

	private boolean mConsumePageSelectedEvent;

	private Button mNextButton;
	private Button mPrevButton;

	private List<Page> mCurrentPageSequence;
	private StepPagerStrip mStepPagerStrip;

	public static NewWordFragment newInstance() {
		NewWordFragment fragment = new NewWordFragment();
		return fragment;
	}

	public NewWordFragment() {
		super();
	}
	
	private Bundle getJointBundle(){
		Bundle result = new Bundle();
		for (Page page : mCurrentPageSequence) {
			result.putAll(page.getData());
		}
		return result;
	}
	
	private void openDictionaryFragment() {
		FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
		getActivity().getSupportFragmentManager().popBackStack();
		transaction.replace(R.id.container, MainFragment.newInstance(Controller.getInstanceOf().getActiveDictionary()));
		transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
		transaction.commit();				
	}

	// Here the content is put in
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		Log.d(LOG_TAG, "NewWordFragment onCreateView");

		Log.d(LOG_TAG, "getActivity"+getActivity());
		View rootView = inflater.inflate(R.layout.activity_wizard, container,
				false);

		// ============================
		//setContentView(R.layout.activity_wizard);

		if (savedInstanceState != null) {
			mWizardModel.load(savedInstanceState.getBundle("model"));
		}

		mWizardModel.registerListener(this);

		mPagerAdapter = new MyPagerAdapter(getActivity().getSupportFragmentManager());
		mPager = (ViewPager) rootView.findViewById(R.id.pager);

		mStepPagerStrip = (StepPagerStrip) rootView.findViewById(R.id.strip);
		mPager.setAdapter(mPagerAdapter);
		
		
		mStepPagerStrip
				.setOnPageSelectedListener(new StepPagerStrip.OnPageSelectedListener() {
					@Override
					public void onPageStripSelected(int position) {
						position = Math.min(mPagerAdapter.getCount() - 1,
								position);
						if (mPager.getCurrentItem() != position) {
							mPager.setCurrentItem(position);
						}
					}
				});

		mNextButton = (Button) rootView.findViewById(R.id.next_button);
		mPrevButton = (Button) rootView.findViewById(R.id.prev_button);

		mPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
			@Override
			public void onPageSelected(int position) {
				mStepPagerStrip.setCurrentPage(position);

				if (mConsumePageSelectedEvent) {
					mConsumePageSelectedEvent = false;
					return;
				}

				mEditingAfterReview = false;
				updateBottomBar();
			}
		});

		mNextButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if (mPager.getCurrentItem() == mCurrentPageSequence.size()) {
					callNewWord();
					openDictionaryFragment();
				} else {
					if (mEditingAfterReview) {
						mPager.setCurrentItem(mPagerAdapter.getCount() - 1);
					} else {
						mPager.setCurrentItem(mPager.getCurrentItem() + 1);
					}
				}
			}

			

			private void callNewWord() {
				String first = getJointBundle().getString(HandInputWordPage.FIRST);
				String second = getJointBundle().getString(HandInputWordPage.SECOND);
				Topic topic = (Topic)getJointBundle().getSerializable(SingleTopicChoicePage.TOPIC);
				Controller.getInstanceOf().addNewWord(new Word(first, second, null, topic) );				
			}
		});

		mPrevButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				mPager.setCurrentItem(mPager.getCurrentItem() - 1);
			}
		});

		onPageTreeChanged();
		updateBottomBar();

		// =====================================
		return rootView;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		Log.d(LOG_TAG, " onAttach");
		//TODO tady musim obnovit i ty co sou pod ni
		//!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
		//!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
		//!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
		//!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
	}

	@Override
	public void onPageTreeChanged() {
		mCurrentPageSequence = mWizardModel.getCurrentPageSequence();
		recalculateCutOffPage();
		mStepPagerStrip.setPageCount(mCurrentPageSequence.size() + 1); // + 1 =
																		// review
																		// step
		mPagerAdapter.notifyDataSetChanged();
		updateBottomBar();
	}

	private void updateBottomBar() {
		int position = mPager.getCurrentItem();
		if (position == mCurrentPageSequence.size()) {
			mNextButton.setText(R.string.finish);
			mNextButton.setBackgroundResource(R.drawable.finish_background);
			mNextButton.setTextAppearance(getActivity(), R.style.TextAppearanceFinish);
		} else {
			mNextButton.setText(mEditingAfterReview ? R.string.review
					: R.string.next);
			mNextButton
					.setBackgroundResource(R.drawable.selectable_item_background);
			TypedValue v = new TypedValue();
			getActivity().getTheme().resolveAttribute(android.R.attr.textAppearanceMedium, v,
					true);
			mNextButton.setTextAppearance(getActivity(), v.resourceId);
			mNextButton.setEnabled(position != mPagerAdapter.getCutOffPage());
		}

		mPrevButton
				.setVisibility(position <= 0 ? View.INVISIBLE : View.VISIBLE);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		mWizardModel.unregisterListener(this);
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putBundle("model", mWizardModel.save());
	}

	@Override
	public AbstractWizardModel onGetModel() {
		return mWizardModel;
	}

	@Override
	public void onEditScreenAfterReview(String key) {
		for (int i = mCurrentPageSequence.size() - 1; i >= 0; i--) {
			if (mCurrentPageSequence.get(i).getKey().equals(key)) {
				mConsumePageSelectedEvent = true;
				mEditingAfterReview = true;
				mPager.setCurrentItem(i);
				updateBottomBar();
				break;
			}
		}
	}

	@Override
	public void onPageDataChanged(Page page) {
		if (page.isRequired()) {
			if (recalculateCutOffPage()) {
				mPagerAdapter.notifyDataSetChanged();
				updateBottomBar();
			}
		}
	}

	@Override
	public Page onGetPage(String key) {
		return mWizardModel.findByKey(key);
	}
	
	private boolean recalculateCutOffPage() {
        // Cut off the pager adapter at first required page that isn't completed
        int cutOffPage = mCurrentPageSequence.size() + 1;
        for (int i = 0; i < mCurrentPageSequence.size(); i++) {
            Page page = mCurrentPageSequence.get(i);
            if (page.isRequired() && !page.isCompleted()) {
                cutOffPage = i;
                break;
            }
        }

        if (mPagerAdapter.getCutOffPage() != cutOffPage) {
            mPagerAdapter.setCutOffPage(cutOffPage);
            return true;
        }

        return false;
    }

	public class MyPagerAdapter extends FragmentStatePagerAdapter {
		private int mCutOffPage;
		private Fragment mPrimaryItem;

		public MyPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int i) {
			if (i >= mCurrentPageSequence.size()) {
				return new ReviewFragment();
			}

			return mCurrentPageSequence.get(i).createFragment();
		}

		@Override
		public int getItemPosition(Object object) {
			// TODO: be smarter about this
			if (object == mPrimaryItem) {
				// Re-use the current fragment (its position never changes)
				return POSITION_UNCHANGED;
			}

			return POSITION_NONE;
		}

		@Override
		public void setPrimaryItem(ViewGroup container, int position,
				Object object) {
			super.setPrimaryItem(container, position, object);
			mPrimaryItem = (Fragment) object;
		}

		@Override
		public int getCount() {
			if (mCurrentPageSequence == null) {
				return 0;
			}
			return Math.min(mCutOffPage + 1, mCurrentPageSequence.size() + 1);
		}

		public void setCutOffPage(int cutOffPage) {
			if (cutOffPage < 0) {
				cutOffPage = Integer.MAX_VALUE;
			}
			mCutOffPage = cutOffPage;
		}

		public int getCutOffPage() {
			return mCutOffPage;
		}
	}
}
