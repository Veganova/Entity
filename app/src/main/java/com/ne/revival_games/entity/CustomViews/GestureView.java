package com.ne.revival_games.entity.CustomViews;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.VideoView;

import com.airbnb.lottie.LottieAnimationView;
import com.ne.revival_games.entity.R;

/**
 * Created by veganova on 6/21/18.
 */

public class GestureView extends RelativeLayout{

    public GestureView(Context context) {
        super(context);
        this.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        this.setGravity(Gravity.BOTTOM);
        final LottieAnimationView videoView = new LottieAnimationView(context);

        videoView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        videoView.setAnimation(R.raw.tap);

        this.addView(videoView);


        videoView.playAnimation();

    }
}
