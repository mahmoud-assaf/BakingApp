package com.mahmoud.bakingapp;

import android.content.Intent;
import android.os.Parcelable;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.Player;
import com.mahmoud.bakingapp.fragment.BlankFragment;
import com.mahmoud.bakingapp.fragment.ImageFragment;
import com.mahmoud.bakingapp.fragment.PlayerFragment;
import com.mahmoud.bakingapp.fragment.StepsFragment;
import com.mahmoud.bakingapp.model.Recipe;

public class RecipeDetailsActivity extends AppCompatActivity implements StepsFragment.OnStepsFragmentListener, PlayerFragment.OnPlayerFragmentListener {
    private static final String STEPPOSITION = "stepposition";
    private static final String VIDEOPOSITION = "videoposition";
    private static final String VIDEOPLAYING = "videoplaying";
    private static final String TAG_FRAGMENT = PlayerFragment.class.getName();
    private static final String TAG_FRAGMENT_STEPS = StepsFragment.class.getName();
    Recipe recipe;
    boolean ingredientsVisible = true;
    public TextView ingredientsToggletv;
    public TextView ingredients_tv;
    public boolean playerShown = false;
    private int currentPosition = 0;
    public long currentvideo_position = C.TIME_UNSET;
    public boolean player_playing = true;
    public StepsFragment stepsFragment;
    public PlayerFragment playerFragment = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getIntent().getExtras() == null)
            return;
        if (savedInstanceState != null) {
            currentPosition = savedInstanceState.getInt(STEPPOSITION);
            currentvideo_position = savedInstanceState.getLong(VIDEOPOSITION);
            player_playing = savedInstanceState.getBoolean(VIDEOPLAYING);

        }
        recipe = getIntent().getParcelableExtra("recipe");
        setContentView(R.layout.activity_recipe_details);
        getSupportActionBar().setTitle(recipe.name);
        // Toast.makeText(this,recipe.name,Toast.LENGTH_SHORT).show();

        //first set up ingredients section .the clickable textview and its drawable
        //check visibility
        ingredients_tv = findViewById(R.id.ingredients_textview);
        ingredients_tv.setText(Utils.formatIngredients(recipe.ingredients));
        ingredientsToggletv = findViewById(R.id.ingredients_toggle_label);

        if (ingredients_tv.getVisibility() == View.GONE) {
            ingredientsVisible = false;
            ingredientsToggletv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.down_arrow, 0, 0, 0);

        }
        //set click listner and left drwawable
        ingredientsToggletv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!ingredientsVisible) {
                    ingredients_tv.setVisibility(View.VISIBLE);
                    ingredientsVisible = true;
                    ingredientsToggletv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.up_arrow, 0, 0, 0);

                } else {
                    ingredients_tv.setVisibility(View.GONE);
                    ingredientsVisible = false;
                    ingredientsToggletv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.down_arrow, 0, 0, 0);

                }
            }
        });

        android.support.v4.app.FragmentManager fm = getSupportFragmentManager();
        stepsFragment = (StepsFragment) fm.findFragmentByTag(TAG_FRAGMENT_STEPS);
        if (stepsFragment == null) {
            Log.e("steps", "creating steps fragment");
            //set up steps fragment
            stepsFragment = StepsFragment.newInstance(recipe, currentPosition);
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.steps_container_frame, stepsFragment, StepsFragment.class.getName());

            // Commit the transaction
            fragmentTransaction.commit();
        }
        //if landscape and player view ..start with the first step
        ViewGroup playerview = findViewById(R.id.player_container_frame);
        if (playerview != null) {
            playerShown = true;
            String videourl = recipe.steps.get(stepsFragment.step_position).videoURL;
            if (!videourl.equals("")) {
                showPlayerFragment(videourl, currentvideo_position, player_playing);

            } else {
                //check if there is a thumbnail
                videourl = recipe.steps.get(stepsFragment.step_position).thumbnailURL;
                if (!videourl.equals("")) {
                    String mimetype = Utils.getMimeType(videourl);
                    Log.e("file mime", mimetype);
                    if (mimetype.startsWith("image")) {
                        showImageFragment(videourl);
                    } else {
                        //blank fragment
                        showBlankFragment();
                    }
                } else {
                    //blank fragment
                    showBlankFragment();
                }
            }
            TextView stepDescription = findViewById(R.id.step_description_txtv);
            stepDescription.setText(recipe.steps.get(stepsFragment.step_position).description);
            Log.e("step num details", String.valueOf(stepsFragment.step_position));
        }

    }

    //steps fragment list click interface
    @Override
    public void onStepSelected(int position) {
        // Toast.makeText(this,String.valueOf(recipe.steps.get(position).shortDescription),Toast.LENGTH_SHORT).show();
        currentPosition = position;
        if (!playerShown) {  //not there start step view activity
            Intent detailsIntent = new Intent(this, StepViewActivity.class);
            detailsIntent.putExtra("recipe", recipe);
            detailsIntent.putExtra("step_index", position);
            startActivity(detailsIntent);
        } else {
            String videourl = recipe.steps.get(position).videoURL;
            if (!videourl.equals("")) {
                showPlayerFragment(videourl, currentvideo_position, player_playing);

            } else {
                //check if there is a thumbnail
                videourl = recipe.steps.get(position).thumbnailURL;
                if (!videourl.equals("")) {
                    String mimetype = Utils.getMimeType(videourl);
                    Log.e("file mime", mimetype);
                    if (mimetype.startsWith("image")) {
                        showImageFragment(videourl);
                    }else {
                        //blank fragment
                        showBlankFragment();
                    }
               /* android.support.v4.app.FragmentManager fmm=getSupportFragmentManager();
                PlayerFragment fragment= (PlayerFragment) fmm.findFragmentByTag(TAG_FRAGMENT);
                if(fragment==null) {
                    Log.e("activity","create player fragment ");
                    showPlayerFragment(videourl, currentvideo_position, player_playing);*/

                } else {
                    //blank fragment
                    showBlankFragment();
                }
            }
            TextView stepDescription = findViewById(R.id.step_description_txtv);
            stepDescription.setText(recipe.steps.get(position).description);
        }


    }

    //
    @Override
    public void onFragmentInteraction(long pos, boolean playing) {

    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(STEPPOSITION, currentPosition);

/*if(playerFragment!=null) {
    outState.putLong(VIDEOPOSITION, playerFragment.player.getCurrentPosition());
    outState.putBoolean(VIDEOPLAYING, playerFragment.player.getPlayWhenReady());
}*/
    }

    public void showBlankFragment() {
        //balnk fragment
        BlankFragment blankFragment = new BlankFragment();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.player_container_frame, blankFragment, BlankFragment.class.getName());

        // Commit the transaction
        fragmentTransaction.commit();
    }

    public void showPlayerFragment(String videourl, long position, boolean playing) {
        playerFragment = PlayerFragment.newInstance(videourl, position, playing);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.player_container_frame, playerFragment, PlayerFragment.class.getName());

        // Commit the transaction
        fragmentTransaction.commit();
    }

    public void showImageFragment(String imageurl) {
        ImageFragment imageFragmen = ImageFragment.newInstance(imageurl);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.step_view_player_container, imageFragmen, ImageFragment.class.getName());

        // Commit the transaction
        fragmentTransaction.commit();
    }
}
