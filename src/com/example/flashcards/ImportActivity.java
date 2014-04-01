/*
 * Copyright 2012 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.flashcards;

import java.util.prefs.Preferences;

import android.accounts.AccountManager;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.flashcards.oauth.ImportFromGoogleTask;
import com.example.flashcards.utilities.ActivityServices;
import com.example.flashcards.utilities.Constants;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.auth.GooglePlayServicesAvailabilityException;
import com.google.android.gms.auth.UserRecoverableAuthException;
import com.google.android.gms.common.AccountPicker;
import com.google.android.gms.common.GooglePlayServicesUtil;

/**
 * The TokenInfoActivity is a simple app that allows users to acquire, inspect and invalidate
 * authentication tokens for a different accounts and scopes.
 *
 * In addition see implementations of {@link ImportFromGoogleTask} for an illustration of how to use
 * the {@link GoogleAuthUtil}.
 */
public class ImportActivity extends Activity implements IFromTaskCallBack {
    private static final String TAG = "PlayHelloActivity";
    
    public static final String EXTRA_ACCOUNTNAME = "extra_accountname";

    private final Context mContext = this;

    static final int REQUEST_CODE_PICK_ACCOUNT = 1000;
    static final int REQUEST_CODE_RECOVER_FROM_AUTH_ERROR = 1001;
    static final int REQUEST_CODE_RECOVER_FROM_PLAY_SERVICES_ERROR = 1002;
	private static final String LOG_TAG = "HelloActivity";

    private String mEmail;


    public static String TYPE_KEY = "type_key";
    public static enum Type {FOREGROUND, BACKGROUND, BACKGROUND_WITH_SYNC}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
    	mEmail = PreferenceManager.getDefaultSharedPreferences(this).getString(Constants.PREF_EMAIL, null);
    	super.onCreate(savedInstanceState);
        setContentView(R.layout.accounts_tester);
        setTitle("Import");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_PICK_ACCOUNT) {
            if (resultCode == RESULT_OK) {
                mEmail = data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
                SharedPreferences.Editor edit = PreferenceManager.getDefaultSharedPreferences(this).edit();
                edit.putString(Constants.PREF_EMAIL, mEmail);
                edit.commit();
                importWordsIntoDictionary();
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "You must pick an account", Toast.LENGTH_SHORT).show();
            }
        } else if ((requestCode == REQUEST_CODE_RECOVER_FROM_AUTH_ERROR ||
                requestCode == REQUEST_CODE_RECOVER_FROM_PLAY_SERVICES_ERROR)
                && resultCode == RESULT_OK) {
            handleAuthorizeResult(resultCode, data);
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void handleAuthorizeResult(int resultCode, Intent data) {
        if (data == null) {
            show("Unknown error, click the button again");
            return;
        }
        if (resultCode == RESULT_OK) {
            Log.i(TAG, "Retrying");
            (new ImportFromGoogleTask(this, mEmail)).execute(this);
            return;
        }
        if (resultCode == RESULT_CANCELED) {
            show("User rejected authorization.");
            return;
        }
        show("Unknown error, click the button again");
    }

    /** Called by button in the layout */
    public void importWords(View view) {
        importWordsIntoDictionary();
    }

    /** Attempt to get the user name. If the email address isn't known yet,
     * then call pickUserAccount() method so the user can pick an account.
     */
    private void importWordsIntoDictionary() {
        if (mEmail == null) {
            pickUserAccount();
        } else {
            if (ActivityServices.isDeviceConnected(this)) {
                (new ImportFromGoogleTask(this, mEmail)).execute(this);
            } else {
                Toast.makeText(this, "No network connection available", Toast.LENGTH_SHORT).show();
            }
        }
    }

    /** Starts an activity in Google Play Services so the user can pick an account */
    private void pickUserAccount() {
        String[] accountTypes = new String[]{"com.google"};
        Intent intent = AccountPicker.newChooseAccountIntent(null, null,
                accountTypes, false, null, null, null, null);
        
        startActivityForResult(intent, REQUEST_CODE_PICK_ACCOUNT);
    }    


    /**
     * This method is a hook for background threads and async tasks that need to update the UI.
     * It does this by launching a runnable under the UI thread.
     */
    @Override
    public void show(final String message) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
            	 AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
            	 builder.setMessage(message);
            	 builder.setTitle("Chyba");
            	 builder.setNeutralButton("OK", null);
            	 AlertDialog alert = builder.create();
                 alert.show();
            }
        });
    }
    
    public void toastAndFinish(final String message) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
            	Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
            	finish();

            }
        });
    }

    /**
     * This method is a hook for background threads and async tasks that need to provide the
     * user a response UI when an exception occurs.
     */
    @Override
    public void handleException(final Exception e) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (e instanceof GooglePlayServicesAvailabilityException) {
                    // The Google Play services APK is old, disabled, or not present.
                    // Show a dialog created by Google Play services that allows
                    // the user to update the APK
                    int statusCode = ((GooglePlayServicesAvailabilityException)e)
                            .getConnectionStatusCode();
                    Dialog dialog = GooglePlayServicesUtil.getErrorDialog(statusCode,
                            ImportActivity.this,
                            REQUEST_CODE_RECOVER_FROM_PLAY_SERVICES_ERROR);
                    dialog.show();
                } else if (e instanceof UserRecoverableAuthException) {
                    // Unable to authenticate, such as when the user has not yet granted
                    // the app access to the account, but the user can fix this.
                    // Forward the user to an activity in Google Play services.
                    Intent intent = ((UserRecoverableAuthException)e).getIntent();
                    startActivityForResult(intent,
                            REQUEST_CODE_RECOVER_FROM_PLAY_SERVICES_ERROR);
                }
            }
        });
    }

	@Override
	public void taskFinished() {
		// TODO Auto-generated method stub
		
	}

   
}