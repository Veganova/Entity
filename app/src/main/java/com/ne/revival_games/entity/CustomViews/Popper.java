package com.ne.revival_games.entity.CustomViews;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Looper;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.ne.revival_games.entity.R;

/**
 * Created by Veganova on 10/24/2017.
 */

public abstract class Popper extends ImageView {

    private boolean hidden = true;
    private int ORIGINAL_WIDTH;
    private int SCALED_WIDTH;

    public Popper(Context context, final Poppist toPop, int drawResource, final double startingPercent) {
        super(context);

        this.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        this.setAdjustViewBounds(true);
        this.setScaleType(ScaleType.FIT_XY);
        this.setVisibility(INVISIBLE);

        Drawable drawable = VectorDrawableCompat.create(getResources(), drawResource, null);
        this.setImageDrawable(drawable);

        this.post(new Runnable() {
            @Override
            public void run() {
                ORIGINAL_WIDTH = getMeasuredWidth();
                SCALED_WIDTH = ORIGINAL_WIDTH * 4 / 5;
                ORIGINAL_WIDTH = (int)(ORIGINAL_WIDTH * startingPercent);
                Popper.this.getLayoutParams().width = ORIGINAL_WIDTH;
                Popper.this.setVisibility(VISIBLE);
                postInvalidate();
            }
        });

        this.setOnClickListener(new View.OnClickListener() {
            private ValueAnimator in, out;

            public void onClick(View v) {
                toPop.toggle();

                if (hidden) {
                    final int w = getMeasuredWidth();
                    out = ValueAnimator.ofInt(w, SCALED_WIDTH);

                    out.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                        @Override
                        public void onAnimationUpdate(ValueAnimator valueAnimator) {
                            int val = (Integer) valueAnimator.getAnimatedValue();

                            ViewGroup.LayoutParams layoutParams = getLayoutParams();
                            layoutParams.width = val;
                            setLayoutParams(layoutParams);
                        }
                    });

                    out.setDuration(toPop.getDuration());
                    out.start();
                } else {
                    final int w = getMeasuredWidth();
                    in = ValueAnimator.ofInt(w, ORIGINAL_WIDTH);
                    in.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                        @Override
                        public void onAnimationUpdate(ValueAnimator valueAnimator) {
                            int val = (Integer) valueAnimator.getAnimatedValue();
                            ViewGroup.LayoutParams layoutParams = getLayoutParams();
                            layoutParams.width = val;
                            setLayoutParams(layoutParams);
                        }
                    });

                    in.setDuration(toPop.getDuration());
                    in.start();
                }
                hidden = !hidden;
            }
        });
    }
}

