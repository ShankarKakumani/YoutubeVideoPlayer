package com.shankar.youtube_video_player;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;

public class YoutubeVideoPlayer {


    public static void playVideo(Activity activity, String VideoID)
    {
        Intent i = new Intent(activity, Player.class);
        Bundle b = new Bundle();
        i.putExtra("videoID", VideoID);

        i.putExtras(b);
        activity.startActivity(i);
    }
}
