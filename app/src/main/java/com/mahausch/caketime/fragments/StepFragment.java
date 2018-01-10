package com.mahausch.caketime.fragments;


import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.mahausch.caketime.R;
import com.mahausch.caketime.RecipeStep;
import com.mahausch.caketime.activities.RecipeDetailActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.content.ContentValues.TAG;
import static android.content.res.Configuration.ORIENTATION_LANDSCAPE;
import static android.content.res.Configuration.ORIENTATION_PORTRAIT;

public class StepFragment extends Fragment implements ExoPlayer.EventListener, View.OnClickListener {

    private static MediaSessionCompat mMediaSession;
    private PlaybackStateCompat.Builder mStateBuilder;
    private SimpleExoPlayer mExoPlayer;
    OnArrowClickListener mCallback;

    @BindView(R.id.playerView)
    SimpleExoPlayerView mPlayerView;

    @BindView(R.id.step_description)
    TextView mStepDescription;

    @BindView(R.id.step_left_arrow)
    ImageView mLeftArrow;

    @BindView(R.id.step_right_arrow)
    ImageView mRightArrow;

    RecipeStep mStep;
    long mVideoPosition;

    public StepFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            mCallback = (OnArrowClickListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement OnArrowClickListener");
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_step, container, false);
        ButterKnife.bind(this, rootView);

        Bundle bundle = getArguments();
        mStep = bundle.getParcelable("step");
        int orientation = getResources().getConfiguration().orientation;

        if (!RecipeDetailActivity.mTwoPane && (mStep.getVideoUrl().isEmpty() || orientation == ORIENTATION_PORTRAIT)) {
            mLeftArrow.setVisibility(View.VISIBLE);
            mLeftArrow.setOnClickListener(this);
            if (RecipeDetailActivity.recipeStepCount != mStep.getId() + 1) {
                mRightArrow.setVisibility(View.VISIBLE);
                mRightArrow.setOnClickListener(this);
            }
        }

        if (orientation == ORIENTATION_LANDSCAPE && !RecipeDetailActivity.mTwoPane && !mStep.getVideoUrl().isEmpty()) {
            mPlayerView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FILL);

            ViewGroup.LayoutParams params = mPlayerView.getLayoutParams();
            params.height = ViewGroup.LayoutParams.MATCH_PARENT;
            mPlayerView.setLayoutParams(params);

            mStepDescription.setVisibility(View.INVISIBLE);
            ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
            if (null != actionBar) {
                actionBar.hide();

                if (Build.VERSION.SDK_INT < 16) {
                    getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                            WindowManager.LayoutParams.FLAG_FULLSCREEN);
                } else {
                    View decorView = getActivity().getWindow().getDecorView();
                    int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
                    decorView.setSystemUiVisibility(uiOptions);
                }
            }
        }

        if (!mStep.getVideoUrl().isEmpty()) {
            initializeMediaSession();
            initializePlayer(Uri.parse(mStep.getVideoUrl()));
            mPlayerView.setVisibility(View.VISIBLE);
        }

        mStepDescription.setText(mStep.getDescription());

        return rootView;
    }

    private void initializeMediaSession() {

        // Create a MediaSessionCompat.
        mMediaSession = new MediaSessionCompat(getContext(), TAG);

        // Enable callbacks from MediaButtons and TransportControls.
        mMediaSession.setFlags(
                MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS |
                        MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS);

        // Do not let MediaButtons restart the player when the app is not visible.
        mMediaSession.setMediaButtonReceiver(null);

        // Set an initial PlaybackState with ACTION_PLAY, so media buttons can start the player.
        mStateBuilder = new PlaybackStateCompat.Builder()
                .setActions(
                        PlaybackStateCompat.ACTION_PLAY |
                                PlaybackStateCompat.ACTION_PAUSE |
                                PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS |
                                PlaybackStateCompat.ACTION_PLAY_PAUSE);

        mMediaSession.setPlaybackState(mStateBuilder.build());

        // Start the Media Session since the activity is active.
        mMediaSession.setActive(true);

    }

    private void initializePlayer(Uri mediaUri) {
        if (mExoPlayer == null) {
            // Create an instance of the ExoPlayer.
            TrackSelector trackSelector = new DefaultTrackSelector();
            LoadControl loadControl = new DefaultLoadControl();
            mExoPlayer = ExoPlayerFactory.newSimpleInstance(getContext(), trackSelector, loadControl);
            mPlayerView.setPlayer(mExoPlayer);

            // Set the ExoPlayer.EventListener to this activity.
            mExoPlayer.addListener(this);

            // Prepare the MediaSource.
            String userAgent = Util.getUserAgent(getContext(), "CakeTime");
            MediaSource mediaSource = new ExtractorMediaSource(mediaUri, new DefaultDataSourceFactory(
                    getContext(), userAgent), new DefaultExtractorsFactory(), null, null);
            mExoPlayer.prepare(mediaSource);
            mExoPlayer.setPlayWhenReady(true);
        }
    }

    private void releasePlayer() {
        mExoPlayer.stop();
        mExoPlayer.release();
        mExoPlayer = null;
    }

    @Override
    public void onTimelineChanged(Timeline timeline, Object manifest) {

    }

    @Override
    public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

    }

    @Override
    public void onLoadingChanged(boolean isLoading) {

    }

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
        if ((playbackState == ExoPlayer.STATE_READY) && playWhenReady) {
            mStateBuilder.setState(PlaybackStateCompat.STATE_PLAYING,
                    mExoPlayer.getCurrentPosition(), 1f);
        } else if ((playbackState == ExoPlayer.STATE_READY)) {
            mStateBuilder.setState(PlaybackStateCompat.STATE_PAUSED,
                    mExoPlayer.getCurrentPosition(), 1f);
        }
        mMediaSession.setPlaybackState(mStateBuilder.build());
    }

    @Override
    public void onPlayerError(ExoPlaybackException error) {

    }

    @Override
    public void onPositionDiscontinuity() {

    }

    @Override
    public void onStop() {
        super.onStop();
        if (mPlayerView.isShown()) {
            releasePlayer();
            mMediaSession.setActive(false);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mPlayerView.isShown()) {
            mExoPlayer.setPlayWhenReady(false);
            mExoPlayer.getPlaybackState();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!mStep.getVideoUrl().isEmpty()) {
            mExoPlayer.setPlayWhenReady(true);
            mExoPlayer.getPlaybackState();
            mExoPlayer.seekTo(mVideoPosition);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        if (mExoPlayer != null) {
            mVideoPosition = mExoPlayer.getCurrentPosition();
            outState.putLong("videoPosition", mVideoPosition);
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null)
            mVideoPosition = savedInstanceState.getLong("videoPosition");
    }

    @Override
    public void onClick(View view) {
        int stepId = mStep.getId();
        int viewId = view.getId();

        switch (viewId) {

            case R.id.step_left_arrow:
                mCallback.onPreviousSelected(stepId);
                break;

            case R.id.step_right_arrow:
                mCallback.onNextSelected(stepId);
        }
    }

    public interface OnArrowClickListener {
        void onPreviousSelected(int stepId);

        void onNextSelected(int stepId);
    }

}
