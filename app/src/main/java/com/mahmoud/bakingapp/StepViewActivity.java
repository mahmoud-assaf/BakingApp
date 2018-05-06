package com.mahmoud.bakingapp;

import android.app.FragmentManager;
import android.content.res.Configuration;
import android.net.Uri;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.mahmoud.bakingapp.fragment.BlankFragment;
import com.mahmoud.bakingapp.fragment.ImageFragment;
import com.mahmoud.bakingapp.fragment.PlayerFragment;
import com.mahmoud.bakingapp.model.Recipe;

public class StepViewActivity extends AppCompatActivity implements PlayerFragment.OnPlayerFragmentListener, View.OnClickListener {
    private static final String TAG_FRAGMENT = PlayerFragment.class.getName();
    private static final String TAG_FRAGMENT_IMAGE = ImageFragment.class.getName();
    public Recipe recipe;
    public int stepIndex;
    public int numOfSteps;
    Button nextBtn, previousBtn;
    TextView stepDescription;
    public   boolean isLandscape=false;
public  PlayerFragment playerFragment=null;
    public  long currentvideo_position= C.TIME_UNSET;
    public boolean player_playing=true;
    private static final String VIDEOPOSITION ="videoposition" ;
    private static final String VIDEOPLAYING ="videoplaying" ;

    private PlayerFragment.OnPlayerFragmentListener mListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getIntent().getExtras() == null)
            return;
        recipe = getIntent().getParcelableExtra("recipe");
        if (savedInstanceState != null) {
            stepIndex = savedInstanceState.getInt("step_index");
          //  currentvideo_position=savedInstanceState.getLong(VIDEOPOSITION);
           // player_playing=savedInstanceState.getBoolean(VIDEOPLAYING);
           // Log.e("on restore activity pos", String.valueOf(currentvideo_position));

           // Log.e("on restore activity", String.valueOf(player_playing));

        } else{
         stepIndex = getIntent().getIntExtra("step_index", 0);
        }
        numOfSteps = recipe.steps.size();
        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
            setTheme(R.style.fullSCRTheme);
            isLandscape=true;
        }
        setContentView(R.layout.activity_step_view);
        if(!isLandscape)
        getSupportActionBar().setTitle(recipe.name);
        String videourl = recipe.steps.get(stepIndex).videoURL;

        if (!videourl.equals("")) {
            android.support.v4.app.FragmentManager fm=getSupportFragmentManager();
            PlayerFragment fragment= (PlayerFragment) fm.findFragmentByTag(TAG_FRAGMENT);
            if(fragment==null) {
                showPlayerFragment(videourl, currentvideo_position, player_playing);
            }

        } else {
            //check if there is a thumbnail
            videourl = recipe.steps.get(stepIndex).thumbnailURL;
            if (!videourl.equals("")) {
                String mimetype=Utils.getMimeType(videourl);
                Log.e("file mime",mimetype);
                if (mimetype.startsWith("image")){
                    android.support.v4.app.FragmentManager fm = getSupportFragmentManager();
                    ImageFragment fragment = (ImageFragment) fm.findFragmentByTag(TAG_FRAGMENT_IMAGE);
                    if (fragment == null) {
                        showImageFragment(videourl);
                    }
                }else {
                    //balnk fragment
                    showBlankFragment();
                }
            } else {
                //balnk fragment
                showBlankFragment();
            }
        }
        stepDescription = findViewById(R.id.step_description_tv);
        if (stepDescription != null) {  // wew may be in full screen mode landscape
            stepDescription.setText(recipe.steps.get(stepIndex).description);
        }
        previousBtn = findViewById(R.id.previous_step_btn);
        if (previousBtn != null) {
            previousBtn.setOnClickListener(this);
            previousBtn.setTransformationMethod(null);
            //also next button
            if (stepIndex == 0) {
                previousBtn.setEnabled(false);

            }
            nextBtn = findViewById(R.id.next_step_btn);
            if (nextBtn != null) {
                nextBtn.setOnClickListener(this);
                nextBtn.setTransformationMethod(null);
                if (stepIndex == numOfSteps - 1)
                    nextBtn.setEnabled(false);
            }

        }
    }

    @Override
    protected void onPause() {
        super.onPause();

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("step_index", stepIndex);

    }
    @Override
    public void onFragmentInteraction(long pos,boolean isplaying) {

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.previous_step_btn:
                stepIndex -= 1;
                if (stepIndex == 0) {
                    previousBtn.setEnabled(false);

                } else {
                    previousBtn.setEnabled(true);
                }
                nextBtn.setEnabled(true);
                String videourl = recipe.steps.get(stepIndex).videoURL;

                if (!videourl.equals("")) {

                        showPlayerFragment(videourl, currentvideo_position, player_playing);


                } else {
                    //check if there is a thumbnail
                    videourl = recipe.steps.get(stepIndex).thumbnailURL;
                    if (!videourl.equals("")) {
                        String mimetype=Utils.getMimeType(videourl);
                        Log.e("file mime",mimetype);
                        if (mimetype.startsWith("image")){
                            showImageFragment(videourl);
                        }else {
                            //balnk fragment
                            showBlankFragment();
                        }

                    } else {
                        //balnk fragment
                        showBlankFragment();
                    }
                }
                stepDescription.setText(recipe.steps.get(stepIndex).description);
                break;
            case R.id.next_step_btn:
                stepIndex += 1;
                if (stepIndex == numOfSteps - 1) {
                    nextBtn.setEnabled(false);

                } else {
                    nextBtn.setEnabled(true);
                }
                previousBtn.setEnabled(true);
                String videour2 = recipe.steps.get(stepIndex).videoURL;

                if (!videour2.equals("")) {

                        showPlayerFragment(videour2, currentvideo_position, player_playing);

                } else {
                    //check if there is a thumbnail
                    videour2 = recipe.steps.get(stepIndex).thumbnailURL;
                    if (!videour2.equals("")) {
                        String mimetype=Utils.getMimeType(videour2);
                        Log.e("file mime",mimetype);
                        if (mimetype.startsWith("image")){
                            showImageFragment(videour2);
                        }else {
                            //balnk fragment
                            showBlankFragment();
                        }

                    } else {
                        //balnk fragment
                        showBlankFragment();
                    }
                }
                stepDescription.setText(recipe.steps.get(stepIndex).description);
                break;

            default:
                break;
        }
    }

    public  void showBlankFragment() {
        //balnk fragment
        BlankFragment blankFragment = new BlankFragment();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.step_view_player_container, blankFragment, BlankFragment.class.getName());

        // Commit the transaction
        fragmentTransaction.commit();
    }

    public void showPlayerFragment(String videourl,long position,boolean playing) {

        playerFragment = PlayerFragment.newInstance(videourl,position,playing);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.step_view_player_container, playerFragment, PlayerFragment.class.getName());

        // Commit the transaction
        fragmentTransaction.commit();
    }

    public  void showImageFragment(String imageurl){
       ImageFragment imageFragmen = ImageFragment.newInstance(imageurl);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.step_view_player_container, imageFragmen, ImageFragment.class.getName());

        // Commit the transaction
        fragmentTransaction.commit();
    }

}
