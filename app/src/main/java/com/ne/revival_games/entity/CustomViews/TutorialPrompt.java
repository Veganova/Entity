package com.ne.revival_games.entity.CustomViews;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.ne.revival_games.entity.GamePanel;
import com.ne.revival_games.entity.R;
import com.ne.revival_games.entity.WorldObjects.Entity.Defence.Nexus;

import java.util.List;

/**
 * Created by veganova on 6/25/18.
 */

public class TutorialPrompt extends android.support.v7.widget.AppCompatButton {


    public TutorialPrompt(Context context, String text) {
        super(context);
        this.setText(text);
        this.setTextColor(GamePanel.background_dark);
        this.setTextSize(15);
        this.setAllCaps(false);
//        this.setBackground(ContextCompat.getDrawable(context, R.drawable.roundedbox));
        this.setPadding(18, 10, 18, 10);
        this.setBackgroundColor(GamePanel.highlight);
    }

    public void setOnClickChain(final List<String> descriptions, final int startIndex) {
        this.setOnClickListener(new View.OnClickListener() {
            private int i = startIndex;
            @Override
            public void onClick(View v) {
                TutorialPrompt.this.setText(descriptions.get(i));
                i  = (i + 1) % descriptions.size();
            }
        });
    }

    public void setText(String txt) {
        super.setText(txt);

//        ValueAnimator anim = ValueAnimator.ofFloat(0, 1);
//        anim.setDuration(2000);
//        final int targetColor = GamePanel.cream;
//        final int startColor = GamePanel.highlight;
//
//        TutorialPrompt.this.setBackgroundColor(startColor);
//
//        int runColor;
//        int hue = 0;
//        final float[] hsv = new float[3]; // Transition color
//        hsv[1] = 1;
//        hsv[2] = 1;
//        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
//
//            @Override
//            public void onAnimationUpdate(ValueAnimator animation) {
//                System.out.println(animation.getAnimatedFraction());
//                TutorialPrompt.this.setBackgroundColor((int) (startColor * (animation.getAnimatedFraction())));
//            }
//        });
//        anim.start();

    }
}
