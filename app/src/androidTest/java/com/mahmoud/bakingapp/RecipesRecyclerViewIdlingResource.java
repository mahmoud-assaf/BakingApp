package com.mahmoud.bakingapp;

import android.support.test.espresso.IdlingResource;

/**
 * Created by Freeware Sys on 3/17/2018.
 * idling resource for espresso
 */

public class RecipesRecyclerViewIdlingResource implements IdlingResource {
    private ResourceCallback  resourceCallback;

    @Override
    public String getName() {
        return RecipesRecyclerViewIdlingResource.class.getName();

    }

    @Override
    public boolean isIdleNow() {
        boolean idle = MainActivity.allRecipesDownloaded;
        if (idle && resourceCallback != null) {
            resourceCallback.onTransitionToIdle();
        }
        return idle;
    }

    @Override
    public void registerIdleTransitionCallback(ResourceCallback callback) {
        this.resourceCallback = callback;

    }
}
