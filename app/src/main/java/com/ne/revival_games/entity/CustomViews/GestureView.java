package com.ne.revival_games.entity.CustomViews;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.airbnb.lottie.LottieAnimationView;
import com.airbnb.lottie.LottieDrawable;
import com.ne.revival_games.entity.R;

/**
 * Created by veganova on 6/21/18.
 */

public class GestureView extends LottieAnimationView {

    public GestureView(Context context, int rawJson, boolean loop) {
        super(context);
        this.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        this.setAnimation(rawJson);
        this.setScale(0.5f);
        if (loop) {
            this.setRepeatCount(LottieDrawable.INFINITE);
        }

        this.playAnimation();
    }
}
