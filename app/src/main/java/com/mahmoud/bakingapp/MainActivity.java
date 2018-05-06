package com.mahmoud.bakingapp;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearSmoothScroller;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;
import android.widget.TextView;


import com.mahmoud.bakingapp.adapter.RecipesGridAdapter;
import com.mahmoud.bakingapp.model.Recipe;
import com.mahmoud.bakingapp.rest.RetrofitApiInterface;
import com.mahmoud.bakingapp.rest.RetrofitClient;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements RecipesGridAdapter.ItemClickListener {
    private static final String SCROLL_POSITION = "scrollposition";
    RecyclerView rvrecipes;
    RecipesGridAdapter adapter;
    List<Recipe> recipes = new ArrayList<Recipe>();
    RetrofitApiInterface recipesService;
    Utils utils;
    public int mAppWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;
    public boolean widgetConfig = false;
    TextView tvStatus;
    public static boolean  allRecipesDownloaded=false;

public GridLayoutManager gridLayoutManager;
public  int scroll_position=0;
    public Parcelable mLayoutManagerState;
    private static final String LAYOUT_MANAGER_STATE = "LAYOUT_MANAGER_STATE";
public Bundle savedState;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setResult(RESULT_CANCELED);
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        //r we configuring a widget ?
        if (extras != null) {
            mAppWidgetId = extras.getInt(
                    AppWidgetManager.EXTRA_APPWIDGET_ID,
                    AppWidgetManager.INVALID_APPWIDGET_ID);
            widgetConfig = true;
            if (mAppWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
                finish();
                return;
            }
        }
        if(savedInstanceState != null)
        {
            scroll_position = savedInstanceState.getInt(SCROLL_POSITION);
            savedState=savedInstanceState;
        }
        setContentView(R.layout.activity_main);
        tvStatus = findViewById(R.id.status_tv);
        rvrecipes = findViewById(R.id.recipes_rv);

        rvrecipes.setAdapter(new RecipesGridAdapter(this, recipes)); //just empty adapter to avoid warning  "No adapter attached; skipping layout"
        utils=new Utils(this);
        int numberOfColumns = utils.calculateNoOfColumns();
        // Log.e("num of columns ", "total: " + numberOfColumns);
        gridLayoutManager=new GridLayoutManager(getApplicationContext(), numberOfColumns);
        rvrecipes.setLayoutManager(gridLayoutManager);

        recipesService = RetrofitClient.getClient().create(RetrofitApiInterface.class);
        getRecipes();
    }

    @Override
    public void onItemClick(View view, int position) {
        if (widgetConfig) {   //its a click for selecting favorite widget recipe
            configNewWidget(recipes.get(position));

        } else {
            Intent detailsIntent = new Intent(MainActivity.this, RecipeDetailsActivity.class);
            detailsIntent.putExtra("recipe", recipes.get(position));
            startActivity(detailsIntent);
        }
    }

    private void configNewWidget(Recipe recipe) {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);

        RemoteViews views = new RemoteViews(this.getPackageName(), R.layout.bakingapp_appwidget);

        views.setTextViewText(R.id.widget_recipe_name_tv, recipe.name);
        views.setTextViewText(R.id.widget_recipe_ingredients_tv, Utils.formatIngredients(recipe.ingredients));
        Intent intent = new Intent(this, RecipeDetailsActivity.class);

        intent.putExtra("recipe", recipe);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(new Random().nextInt(50) + "_action"); //requred so as not to reuse the same intent extras https://stackoverflow.com/questions/3127957/why-the-pendingintent-doesnt-send-back-my-custom-extras-setup-for-the-intent/3128271#3128271
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

        // Get the layout for the App Widget and attach an on-click listener
        views.setOnClickPendingIntent(R.id.my_widget, pendingIntent);
        appWidgetManager.updateAppWidget(mAppWidgetId, views);

        Intent resultValue = new Intent();
        resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);
        setResult(RESULT_OK, resultValue);
        finish();
    }

    public void getRecipes() {

        if (!Utils.isOnline()) {
            utils.showMessage(getString(R.string.no_connection), tvStatus, true);
            return;
        }

        utils.showMessage(getString(R.string.status_connecting), tvStatus, false);

        Call<List<Recipe>> call = recipesService.getRecipes();
        call.enqueue(new Callback<List<Recipe>>() {
            @Override
            public void onResponse(Call<List<Recipe>> call, Response<List<Recipe>> response) {
                utils.showMessage(getString(R.string.status_laoding), tvStatus, false);
                // Log.e("code",String.valueOf(response.code()));
                if (response.code() > 200) {
                    utils.showMessage(getString(R.string.rest_error), tvStatus, true);
                    return;
                }
                recipes = response.body();
                //Log.e("num of recipes ", "total: " + recipes.size());

                adapter = new RecipesGridAdapter(getApplicationContext(), recipes);
                adapter.setClickListener(MainActivity.this);
                rvrecipes.setAdapter(adapter);
                if (savedState != null) {

                    mLayoutManagerState = savedState.getParcelable(LAYOUT_MANAGER_STATE);
                    rvrecipes.getLayoutManager().onRestoreInstanceState(mLayoutManagerState);
                }
                tvStatus.setVisibility(View.GONE);
                allRecipesDownloaded=true;
            }

            @Override
            public void onFailure(Call<List<Recipe>> call, Throwable t) {
                // Log error here since request failed
                Log.e("ERROR", t.toString());
                utils.showMessage(getString(R.string.rest_error), tvStatus, true);
            }
        });


    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt(SCROLL_POSITION,scroll_position);
        outState.putParcelable(LAYOUT_MANAGER_STATE, mLayoutManagerState);
    }
    @Override
    protected void onPause() {
        super.onPause();
        mLayoutManagerState = rvrecipes.getLayoutManager().onSaveInstanceState();
    }

}
