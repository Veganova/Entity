package com.ne.revival_games.entity.CustomViews;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.ne.revival_games.entity.GamePanel;
import com.ne.revival_games.entity.MainActivity;
import com.ne.revival_games.entity.MainMenuActivity;
import com.ne.revival_games.entity.MainThread;
import com.ne.revival_games.entity.R;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Created by Veganova on 7/25/2017.
 */

public class PlayPauseArea extends RelativeLayout {


    public PlayPauseArea(final Context context, final MainActivity activity) {
        super(context);

        int imageBound = (int) (activity.SCREEN_WIDTH * 0.245);

        this.setGravity(Gravity.LEFT | Gravity.TOP);
        this.setLayoutParams(new LinearLayout.LayoutParams(imageBound, imageBound));

        final PlayPauseView playPause = new PlayPauseView(context, activity);

        int buttonBound = (int) (activity.SCREEN_WIDTH * 0.153);
        RelativeLayout container = new RelativeLayout(context);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(buttonBound, buttonBound);
        params.leftMargin = 10;
        container.setLayoutParams(params);


        this.addView(new BackgroundImage(context));

        container.addView(playPause);
        this.addView(container);
    }

    private class BackgroundImage extends ImageView {

        public BackgroundImage(Context context) {
            super(context);

            this.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT));
            this.setAdjustViewBounds(true);

            Drawable drawable = VectorDrawableCompat.create(getResources(), R.drawable.ic_pp_box_2, null);
            this.setImageDrawable(drawable);
        }
    }
}
