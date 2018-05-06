package com.mahmoud.bakingapp.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.RemoteViews;

import com.google.gson.Gson;
import com.mahmoud.bakingapp.R;
import com.mahmoud.bakingapp.RecipeDetailsActivity;
import com.mahmoud.bakingapp.Utils;
import com.mahmoud.bakingapp.model.Recipe;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Freeware Sys on 3/14/2018.
 */

public class BakingAppWidgetProvider extends AppWidgetProvider {

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);

        /*final int N = appWidgetIds.length;

        // Perform this loop procedure for each App Widget that belongs to this provider
        for (int i=0; i<N; i++) {
            int appWidgetId = appWidgetIds[i];


            // Get the layout for the App Widget and attach an on-click listener
            // to the button
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.bakingapp_appwidget);
            views.setTextViewText(R.id.widget_recipe_name_tv,recipe.name);
            views.setTextViewText(R.id.widget_recipe_ingredients_tv, Utils.formatIngredients(recipe.ingredients));
            views.setOnClickPendingIntent(R.id.widget_recipe_name_tv, pendingIntent);

            // Tell the AppWidgetManager to perform an update on the current app widget
            appWidgetManager.updateAppWidget(appWidgetId, views);
        }*/
    }
    }

