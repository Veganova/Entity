package com.ne.revival_games.entity.CustomViews;

import android.content.Context;
import android.view.Gravity;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ne.revival_games.entity.GamePanel;

public class MoneyPopUp extends RelativeLayout {

    private final TextView textView;
    private static float startAlpha = 0.0f;
    private static float endAlpha = 0.75f;
    private static int duration = 2000;

    public MoneyPopUp(Context context) {
        super(context);

        this.setGravity(Gravity.CENTER);
        this.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        this.textView = new TextView(context);
        this.textView.setTextSize(20);
        this.textView.setTextColor(GamePanel.cream);


        this.addView(textView);
    }


    public void displayMoney(String money) {
        this.textView.setText(String.format("+$%s", money));
        AlphaAnimation animation = new AlphaAnimation(startAlpha, endAlpha);
        animation.setDuration(duration / 3);
        animation.setFillAfter(true);

        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                // fade back ou
                MoneyPopUp.this.hideMoney();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        this.textView.startAnimation(animation);
    }

    private void hideMoney() {
        AlphaAnimation animation = new AlphaAnimation(endAlpha, startAlpha);
        animation.setStartOffset(duration);
        animation.setDuration(duration);
        animation.setFillAfter(true);
        this.textView.startAnimation(animation);
    }






}
