package com.mahmoud.bakingapp.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

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
import com.mahmoud.bakingapp.R;
import com.mahmoud.bakingapp.Utils;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link PlayerFragment.OnPlayerFragmentListener} interface
 * to handle interaction events.
 * Use the {@link PlayerFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PlayerFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_VIDEO_URL = "video_url";
    private static final String VIDEO_POSITION = "video_position";
    private static final String VIDEO_PLAY = "video_play";
    // TODO: Rename and change types of parameters
    public String videoURL;
    public SimpleExoPlayer player;
    private SimpleExoPlayerView playerView;
    public long currentPosition = C.TIME_UNSET;
    public boolean playing = true;
    private OnPlayerFragmentListener mListener;
    public MediaSource videoSource;
    DataSource.Factory dataSourceFactory;
    TrackSelector trackSelector;
    LoadControl loadControl;
    DefaultBandwidthMeter bandwidthMeter;
    public boolean initiated = false;

    public PlayerFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @return A new instance of fragment PlayerFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PlayerFragment newInstance(String param1, long position, boolean playing) {
        PlayerFragment fragment = new PlayerFragment();
        Bundle args = new Bundle();
        args.putString(ARG_VIDEO_URL, param1);
        args.putLong(VIDEO_POSITION, position);
        args.putBoolean(VIDEO_PLAY, playing);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
              currentPosition = savedInstanceState.getLong(VIDEO_POSITION);
            playing = savedInstanceState.getBoolean(VIDEO_PLAY);
        }
        if (getArguments() != null) {
            videoURL = getArguments().getString(ARG_VIDEO_URL);

            if (!Utils.isOnline()) {
                Toast.makeText(getActivity(), R.string.noconnection, Toast.LENGTH_LONG).show();
                return;
            }

        }
    }

    private void initPlayer(String videoURL) {
        trackSelector = new DefaultTrackSelector();
        loadControl = new DefaultLoadControl();
        player = ExoPlayerFactory.newSimpleInstance(getActivity(), trackSelector, loadControl);

        String userAgent = Util.getUserAgent(getActivity(), "BakingApp");
        bandwidthMeter = new DefaultBandwidthMeter();
// Produces DataSource instances through which media data is loaded.
        dataSourceFactory = new DefaultDataSourceFactory(getActivity(), userAgent, bandwidthMeter);
// This is the MediaSource representing the media to be played.
        Uri videoURI = Uri.parse(videoURL);
        videoSource = new ExtractorMediaSource.Factory(dataSourceFactory).createMediaSource(videoURI);


// Prepare the player with the source.
        player.prepare(videoSource);
        playerView.setPlayer(player);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_player, container, false);

        playerView = view.findViewById(R.id.player);


        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putLong(VIDEO_POSITION,player.getCurrentPosition() );
        outState.putBoolean(VIDEO_PLAY,player.getPlayWhenReady() );


    }



    @Override
    public void onStart() {

        super.onStart();
        if(player==null) {

            initPlayer(videoURL);
        }
        player.setPlayWhenReady(playing);
        //play immediately when ready
        player.seekTo(currentPosition);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnPlayerFragmentListener) {
            mListener = (OnPlayerFragmentListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        currentPosition=player.getCurrentPosition();
        playing=player.getPlayWhenReady();
        releasePlayer();
        /*if (player != null) {
            mListener.onFragmentInteraction(player.getContentPosition(),player.getPlayWhenReady());

        }*/
    }

    private void releasePlayer() {
        if (player != null) {
            player.stop();
            player.release();
            player = null;

        }
    }




     /* See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnPlayerFragmentListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(long positoin, boolean isplaying);
    }
}
