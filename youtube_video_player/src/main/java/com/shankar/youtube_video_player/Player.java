package com.shankar.youtube_video_player;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;

import java.util.Objects;

public class Player extends AppCompatActivity {

    YouTubePlayerView youTubePlayerView;
    private PlayerConstants.PlaybackQuality playbackQuality;
    TextView videoName;

    //For Full Screen

    private static final int UI_ANIMATION_DELAY = 300;
    private final Handler mHideHandler = new Handler();
    private final Runnable mHidePart2Runnable = new Runnable() {
        @SuppressLint("InlinedApi")
        @Override
        public void run() {
            // Delayed removal of status and navigation bar

            // Note that some of these constants are new as of API 16 (Jelly Bean)
            // and API 19 (KitKat). It is safe to use them, as they are inlined
            // at compile-time and do nothing on earlier devices.
            youTubePlayerView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        }
    };
    private final Runnable mShowPart2Runnable = () -> {
        // Delayed display of UI elements
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.show();
        }
    };
    private boolean mVisible;
    private final Runnable mHideRunnable = this::hide;
    /**
     * Touch listener to use for in-layout UI controls to delay hiding the
     * system UI. This is to prevent the jarring behavior of controls going away
     * while interacting with activity UI.
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        videoName = findViewById(R.id.videoName);
        playYoutubeVideo();


        mVisible = true;
        delayedHide(100);
    }

    private void playYoutubeVideo() {
        youTubePlayerView = findViewById(R.id.youtube_player_view);
        getLifecycle().addObserver(youTubePlayerView);

        youTubePlayerView.getPlayerUiController().showYouTubeButton(false);
        youTubePlayerView.getPlayerUiController().showFullscreenButton(false);
        youTubePlayerView.getPlayerUiController().showBufferingProgress(true);
        youTubePlayerView.getPlayerUiController().showMenuButton(false);
        youTubePlayerView.getPlayerUiController().showVideoTitle(true);

        youTubePlayerView.enterFullScreen();


        initPictureInPicture(youTubePlayerView);


        Bundle bundle = getIntent().getExtras();
        assert bundle != null;
        String videoUrl = bundle.getString("videoID");
        assert videoUrl != null;


        youTubePlayerView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {

            @Override
            public void onReady(@NonNull YouTubePlayer youTubePlayer) {
                super.onReady(youTubePlayer);
                youTubePlayer.loadVideo(videoUrl,0);


            }

            @Override
            public void onStateChange(@NonNull YouTubePlayer youTubePlayer, @NonNull PlayerConstants.PlayerState state) {
                super.onStateChange(youTubePlayer, state);
                playerStateToString(state);
            }


            @Override
            public void onCurrentSecond(@NonNull YouTubePlayer youTubePlayer, float second) {
                super.onCurrentSecond(youTubePlayer, second);
                initializeCustomButtons(second ,youTubePlayer);
            }


            @Override
            public void onError(@NonNull YouTubePlayer youTubePlayer, @NonNull PlayerConstants.PlayerError error) {
                super.onError(youTubePlayer, error);

                switch (error)
                {
                    case UNKNOWN:
                    case INVALID_PARAMETER_IN_REQUEST:
                    case VIDEO_NOT_FOUND:
                    case HTML_5_PLAYER:
                        break;
                    case VIDEO_NOT_PLAYABLE_IN_EMBEDDED_PLAYER:
                        youTubePlayerView.getPlayerUiController().showYouTubeButton(true);
                        Toast.makeText(Player.this, " Click on the Youtube Button to open this Movie/Video in Youtube : ", Toast.LENGTH_LONG).show();
                        break;
                }
            }

            @Override
            public void onPlaybackQualityChange(@NonNull YouTubePlayer youTubePlayer, @NonNull PlayerConstants.PlaybackQuality playbackQuality) {
                super.onPlaybackQualityChange(youTubePlayer, playbackQuality);

                QualityManager(youTubePlayerView, playbackQuality);

            }


        });
    }



    private void QualityManager(YouTubePlayerView youTubePlayerView, PlayerConstants.PlaybackQuality playbackQuality) {

        TextView qualityText = findViewById(R.id.qualityText);
        switch (playbackQuality) {

            case SMALL:
                qualityText.setText("240p");

                break;
            case MEDIUM:
                qualityText.setText("360p");

                break;
            case LARGE:
                qualityText.setText("480p");

                break;

            case HD720:
                qualityText.setText("HD720");

                break;
            case HD1080:
                qualityText.setText("HD1080");

                break;
            case HIGH_RES:
                qualityText.setText("HIGH_RES");

                break;
            case UNKNOWN:
                qualityText.setText("*");

                break;
            default:
                qualityText.setText("Def");
                break;
        }
        if (qualityText.getParent() != null) {
            ((ViewGroup) qualityText.getParent()).removeView(qualityText); // <- fix
        }
        youTubePlayerView.getPlayerUiController().addView(qualityText);

    }


    private void initPictureInPicture(YouTubePlayerView youTubePlayerView) {
        ImageView pictureInPictureView = new ImageView(this);
        pictureInPictureView.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_fullscreen_24dp));

        pictureInPictureView.setOnClickListener( view -> {
            if(youTubePlayerView.isFullScreen())
            {
                youTubePlayerView.exitFullScreen();
                pictureInPictureView.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_fullscreen_exit_24dp));
            }

            else
            {
                youTubePlayerView.enterFullScreen();
                pictureInPictureView.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_fullscreen_24dp));
            }
        });

        youTubePlayerView.getPlayerUiController().addView( pictureInPictureView );

    }


    private void playerStateToString(PlayerConstants.PlayerState state) {
        switch (state) {
            case UNKNOWN:
            case VIDEO_CUED:
                return;

            case UNSTARTED:
            case ENDED:
                removeCustomActionsFromPlayer();
                //youTubePlayerView.exitFullScreen();
                //setRequestedOrientation(SCREEN_ORIENTATION_PORTRAIT);
                //adContainer.setVisibility(View.VISIBLE);
                return;

            case PLAYING:
                showCustomActionsToPlayer();
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
                youTubePlayerView.getPlayerUiController().showVideoTitle(true);

                //adContainer.setVisibility(View.GONE);
                return;

            case PAUSED:
                showCustomActionsToPlayer();
                //youTubePlayerView.exitFullScreen();
                //setRequestedOrientation(SCREEN_ORIENTATION_PORTRAIT);
                //adContainer.setVisibility(View.VISIBLE);
                youTubePlayerView.getPlayerUiController().showVideoTitle(false);
                return;

            case BUFFERING:
                removeCustomActionsFromPlayer();
                if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                    // adContainer.setVisibility(View.GONE);

                } else if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                    // adContainer.setVisibility(View.VISIBLE);
                }

                return;

            default:
        }
    }


    private void initializeCustomButtons(float second,YouTubePlayer youTubePlayer) {
        Drawable customAction1Icon = ContextCompat.getDrawable(this, R.drawable.ic_replay_30_white_24);
        Drawable customAction2Icon = ContextCompat.getDrawable(this, R.drawable.ic_forward_30_white_24);
        assert customAction1Icon != null;
        assert customAction2Icon != null;

        youTubePlayerView.getPlayerUiController().setCustomAction1(customAction1Icon, view ->
                youTubePlayer.seekTo(second - 30));

        youTubePlayerView.getPlayerUiController().setCustomAction2(customAction2Icon, view ->
                youTubePlayer.seekTo(second + 30));

    }


    private void showCustomActionsToPlayer() {
        youTubePlayerView.getPlayerUiController().showCustomAction1(true);
        youTubePlayerView.getPlayerUiController().showCustomAction2(true);
    }


    private void removeCustomActionsFromPlayer() {
        youTubePlayerView.getPlayerUiController().showCustomAction1(false);
        youTubePlayerView.getPlayerUiController().showCustomAction2(false);
    }


    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        // Trigger the initial hide() shortly after the activity has been
        // created, to briefly hint to the user that UI controls
        // are available.
        delayedHide(100);
    }


    private void hide() {
        // Hide UI first
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        mVisible = false;

        // Schedule a runnable to remove the status and navigation bar after a delay
        mHideHandler.removeCallbacks(mShowPart2Runnable);
        mHideHandler.postDelayed(mHidePart2Runnable, UI_ANIMATION_DELAY);
    }


    private void delayedHide(int delayMillis) {
        mHideHandler.removeCallbacks(mHideRunnable);
        mHideHandler.postDelayed(mHideRunnable, delayMillis);
    }


    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfiguration) {
        super.onConfigurationChanged(newConfiguration);
        Objects.requireNonNull(youTubePlayerView.getPlayerUiController().getMenu()).dismiss();

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        youTubePlayerView.release();

    }


    @Override
    protected void onResume() {
        super.onResume();
        delayedHide(10);

    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public void onBackPressed() {


        youTubePlayerView.release();
        finish();
    }
}