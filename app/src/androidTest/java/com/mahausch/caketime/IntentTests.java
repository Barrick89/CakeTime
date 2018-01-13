package com.mahausch.caketime;


import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.mahausch.caketime.activities.RecipeDetailActivity;
import com.mahausch.caketime.activities.RecipeOverviewActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

@RunWith(AndroidJUnit4.class)
public class IntentTests {

    @Rule
    public IntentsTestRule<RecipeOverviewActivity> mIntentTestRule = new IntentsTestRule<>(RecipeOverviewActivity.class);

    //Clicks on a RecyclerView item to check if RecipeDetailActivity is launched.
    @Test
    public void clickRecyclerViewItem_OpensRecipeDetailActivity() {
        onView(withId(R.id.recycler_view)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        intended(hasComponent(RecipeDetailActivity.class.getName()));
    }
}
