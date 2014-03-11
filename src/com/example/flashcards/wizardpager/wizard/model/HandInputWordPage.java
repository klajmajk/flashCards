/*
 * Copyright 2013 Google Inc.
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

package com.example.flashcards.wizardpager.wizard.model;

import com.example.flashcards.wizardpager.wizard.ui.HandInputFragment;

import android.support.v4.app.Fragment;
import android.text.TextUtils;

import java.util.ArrayList;

/**
 * A page asking for a name and an email.
 */
public class HandInputWordPage extends Page {
    public static final String FIRST = "first";
    public static final String SECOND = "second";

    public HandInputWordPage(ModelCallbacks callbacks, String title) {
        super(callbacks, title);
    }

    @Override
    public Fragment createFragment() {
        return HandInputFragment.create(getKey());
    }

    @Override
    public void getReviewItems(ArrayList<ReviewItem> dest) {
        dest.add(new ReviewItem("�esky", mData.getString(FIRST), getKey(), -1));
        dest.add(new ReviewItem("P�eklad", mData.getString(SECOND), getKey(), -1));
    }

    @Override
    public boolean isCompleted() {
        return !(TextUtils.isEmpty(mData.getString(FIRST))||TextUtils.isEmpty(mData.getString(SECOND)));
    }
}
