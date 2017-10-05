package com.ne.revival_games.entity.CustomViews;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.percent.PercentLayoutHelper;
import android.support.percent.PercentRelativeLayout;
import android.support.v7.widget.LinearLayoutCompat;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ne.revival_games.entity.GamePanel;
import com.ne.revival_games.entity.MainActivity;
import com.ne.revival_games.entity.R;

/**
 * Created by Veganova on 9/10/2017.
 */

public class RestartHome extends RelativeLayout {
    private static final int ANIMATION_DURATION = 600;
    private int HEIGHT;
    private RestartButton rb;
    private HomeButton home;

    public RestartHome(Context context, float parentHeight, MainActivity activity, MainMenu.GameMode mode) {
        super(context);
        this.HEIGHT = (int) (parentHeight * 0.7);
        this.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.MATCH_PARENT));

        this.setGravity(Gravity.LEFT | Gravity.CENTER);


        LinearLayout l = new LinearLayout(context);
        l.setPadding(HEIGHT / 8, HEIGHT / 5, HEIGHT / 8, HEIGHT / 5);
        l.setBackgroundColor(GamePanel.cream);
        l.setElevation(0);
        l.setStateListAnimator(null);

        ImageView image = new ImageView(context);
        Drawable drawable = VectorDrawableCompat.create(getResources(), R.drawable.ic_restart, null);
        image.setImageDrawable(drawable);
        image.setColorFilter(GamePanel.background_dark);


        l.addView(image);

//        rb = new RestartButton(context, activity, mode);
        home = new HomeButton(context, activity);
//        containerVerticle.addView(rb);
//        containerHome.addView(t);
        Container c = new Container(context, l, home);

        this.addView(c);
    }

    public void pop() {
        // move the image out
    }

    class Container extends LinearLayout {
        public Container(Context context, View icon, View image) {
            super(context);

            this.setOrientation(HORIZONTAL);
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
                    HEIGHT);
            params.bottomMargin = 50;

            this.addView(icon);
            this.addView(image);
        }
    }

    private class HomeButton extends ImageView {
        HomeButton(Context context, final MainActivity activity) {
            super(context);

            this.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.MATCH_PARENT));
            this.setAdjustViewBounds(true);
            this.setScaleType(ScaleType.FIT_XY);

            Drawable drawable = VectorDrawableCompat.create(getResources(), R.drawable.ic_isosceles, null);
            this.setImageDrawable(drawable);

            this.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    activity.finish();
                }
            });
        }
    }

    private class RestartButton extends ImageView {
        RestartButton(final Context context, final MainActivity activity, final MainMenu.GameMode mode) {
            super(context);


            this.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    final Intent intent = new Intent(context, MainActivity.class);
                    intent.putExtra("GameMode", mode);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);

                    // wait for the current activity thread to end. (so that it will let go of the canvas lock)
                    activity.finish();
                    if (activity.myThread.hasEnded()) {

                    } else {
                        try {
                            synchronized (activity.myThread) {
                                activity.myThread.wait();
                            }
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    activity.startActivity(intent);
                }
            });
        }
    }
}
