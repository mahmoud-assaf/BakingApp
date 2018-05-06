package com.mahmoud.bakingapp.rest;

import com.mahmoud.bakingapp.model.Recipe;



import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by mahmoud on 18/02/2018.
 */

public interface RetrofitApiInterface {
    @GET("baking.json")
    Call<List<Recipe>> getRecipes();


}
