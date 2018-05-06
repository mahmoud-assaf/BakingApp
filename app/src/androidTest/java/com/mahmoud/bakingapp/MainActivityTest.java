package com.mahmoud.bakingapp;

import android.app.Instrumentation;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

/**
 * Created by Freeware Sys on 3/17/2018.
 * testing MainActivity ..clicking on item inside recyclerview
 * opens the details activity
 */
@RunWith(AndroidJUnit4.class)
public class MainActivityTest {
    private RecipesRecyclerViewIdlingResource idlingResource;
    @Rule
    //important IntentsTestRule instead of activitytestrule to check for opening activity
   public IntentsTestRule<MainActivity> mainActivityActivityTestRule=new IntentsTestRule<MainActivity>(MainActivity.class);

    @Before
    public void registerIntentServiceIdlingResource() {
        Instrumentation instrumentation = InstrumentationRegistry.getInstrumentation();
        idlingResource = new RecipesRecyclerViewIdlingResource();
        Espresso.registerIdlingResources(idlingResource);
    }

    @Test
    public void clickGridItem_OpenRecipeViewActivity(){
        onView(withId(R.id.recipes_rv))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        intended(hasComponent(RecipeDetailsActivity.class.getName()));

    }

    @After
    public void unregisterIntentServiceIdlingResource() {
        Espresso.unregisterIdlingResources(idlingResource);
    }


}
