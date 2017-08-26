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

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.StateListAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Outline;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Property;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewOutlineProvider;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;

import com.ne.revival_games.entity.MainActivity;
import com.ne.revival_games.entity.MainThread;
import com.ne.revival_games.entity.R;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.util.Property;

import com.ne.revival_games.entity.GamePanel;
import com.ne.revival_games.entity.R;

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

        BackgroundImage(Context context) {
            super(context);

            this.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT));
            this.setAdjustViewBounds(true);

            Drawable drawable = VectorDrawableCompat.create(getResources(), R.drawable.ic_pp_box_2, null);
            this.setImageDrawable(drawable);
        }
    }

    private class PlayPauseView extends FrameLayout {
        //from https://github.com/alexjlockwood/material-pause-play-animation/tree/master/app/src/main/java/com/alexjlockwood/example/playpauseanimation

//    private static final Property<PlayPauseView, Integer> COLOR =
//            new Property<PlayPauseView, Integer>(Integer.class, "color") {
//                @Override
//                public Integer get(PlayPauseView v) {
//                    return v.getColor();
//                }
//
//                @Override
//                public void set(PlayPauseView v, Integer value) {
//                    v.setColor(value);
//                }
//            };

        private static final long PLAY_PAUSE_ANIMATION_DURATION = 200;

        private final PlayPauseDrawable mDrawable;
        private final Paint mPaint = new Paint();
//    private final int mPauseBackgroundColor;
//    private final int mPlayBackgroundColor;

        private AnimatorSet mAnimatorSet;
        //    private int mBackgroundColor;
        private int mWidth;
        private int mHeight;

        PlayPauseView(Context context, final MainActivity activity) {
            super(context);
            setWillNotDraw(false);
//        mBackgroundColor = R.color.purple;
            mPaint.setAntiAlias(true);
            mPaint.setStyle(Paint.Style.FILL);
            mDrawable = new PlayPauseDrawable(context);
            mDrawable.setCallback(this);

//        mPauseBackgroundColor = R.color.purple;
//        mPlayBackgroundColor = R.color.blue;

            this.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT));
            setPadding(0,0,0,0);

            StateListAnimator sla = AnimatorInflater.loadStateListAnimator(context, R.animator.button_elavation);
            this.setStateListAnimator(sla);

            this.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    toggle();
                    if (activity.isPaused()) {
                        activity.onResume();
                    } else {
                        activity.onPause();
                    }
                }
            });
        }

        @Override
        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            final int size = Math.min(getMeasuredWidth(), getMeasuredHeight());
            setMeasuredDimension(size, size);
        }

        @Override
        protected void onSizeChanged(final int w, final int h, int oldw, int oldh) {
            super.onSizeChanged(w, h, oldw, oldh);
            mDrawable.setBounds(0, 0, w, h);
            mWidth = w;
            mHeight = h;

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            setOutlineProvider(new ViewOutlineProvider() {
//                @TargetApi(Build.VERSION_CODES.LOLLIPOP)
//                @Override
//                public void getOutline(View view, Outline outline) {
//                    outline.setOval(0, 0, view.getWidth(), view.getHeight());
//                }
//            });
//            setClipToOutline(true);
//        }
        }

//    private void setColor(int color) {
//        mBackgroundColor = color;
//        invalidate();
//    }
//
//    private int getColor() {
//        return mBackgroundColor;
//    }

        @Override
        protected boolean verifyDrawable(Drawable who) {
            return who == mDrawable || super.verifyDrawable(who);
        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
//        mPaint.setColor(mBackgroundColor);
//        final float radius = Math.min(mWidth, mHeight) / 2f;
//        canvas.drawCircle(mWidth / 2f, mHeight / 2f, radius, mPaint);
            mDrawable.draw(canvas);
        }

        public void toggle() {
            if (mAnimatorSet != null) {
                mAnimatorSet.cancel();
            }

//        mAnimatorSet = new AnimatorSet();
//        final boolean isPlay = mDrawable.isPlay();
//        final ObjectAnimator colorAnim = ObjectAnimator.ofInt(this, COLOR, isPlay ? mPauseBackgroundColor : mPlayBackgroundColor);
//        colorAnim.setEvaluator(new ArgbEvaluator());
            final Animator mAnimatorSet = mDrawable.getPausePlayAnimator();
            mAnimatorSet.setInterpolator(new DecelerateInterpolator());
            mAnimatorSet.setDuration(PLAY_PAUSE_ANIMATION_DURATION);
//        mAnimatorSet.playTogether(colorAnim, pausePlayAnim);
            mAnimatorSet.start();
        }
    }

    private static class PlayPauseDrawable extends Drawable {

        static final Property<PlayPauseDrawable, Float> PROGRESS =
                new Property<PlayPauseDrawable, Float>(Float.class, "progress") {
                    @Override
                    public Float get(PlayPauseDrawable d) {
                        return d.getProgress();
                    }

                    @Override
                    public void set(PlayPauseDrawable d, Float value) {
                        d.setProgress(value);
                    }
                };

        private final Path mLeftPauseBar = new Path();
        private final Path mRightPauseBar = new Path();
        private final Paint mPaint = new Paint();
        private final RectF mBounds = new RectF();
        private final float mPauseBarWidth;
        private final float mPauseBarHeight;
        private final float mPauseBarDistance;

        private float mWidth;
        private float mHeight;

        private float mProgress;
        private boolean mIsPlay;

        PlayPauseDrawable(Context context) {
            final Resources res = context.getResources();
            mPaint.setAntiAlias(true);
            mPaint.setStyle(Paint.Style.FILL);
            mPaint.setColor(GamePanel.background_dark);
            mPauseBarWidth = res.getDimensionPixelSize(R.dimen.pause_bar_width);
            mPauseBarHeight = res.getDimensionPixelSize(R.dimen.pause_bar_height);
            mPauseBarDistance = res.getDimensionPixelSize(R.dimen.pause_bar_distance);
        }

        @Override
        protected void onBoundsChange(Rect bounds) {
            super.onBoundsChange(bounds);
            mBounds.set(bounds);
            mWidth = mBounds.width();
            mHeight = mBounds.height();
        }

        @Override
        public void draw(Canvas canvas) {
            mLeftPauseBar.rewind();
            mRightPauseBar.rewind();

            // The current distance between the two pause bars.
            final float barDist = lerp(mPauseBarDistance, 0, mProgress) - 1;
            // The current width of each pause bar.
            final float barWidth = lerp(mPauseBarWidth, mPauseBarHeight / 2f, mProgress);
            // The current position of the left pause bar's top left coordinate.
            final float firstBarTopLeft = lerp(0, barWidth, mProgress);
            // The current position of the right pause bar's top right coordinate.
            final float secondBarTopRight = lerp(2 * barWidth + barDist, barWidth + barDist, mProgress);

            // Draw the left pause bar. The left pause bar transforms into the
            // top half of the play button triangle by animating the position of the
            // rectangle's top left coordinate and expanding its bottom width.
            mLeftPauseBar.moveTo(0, 0);
            mLeftPauseBar.lineTo(firstBarTopLeft, -mPauseBarHeight);
            mLeftPauseBar.lineTo(barWidth, -mPauseBarHeight);
            mLeftPauseBar.lineTo(barWidth, 0);
            mLeftPauseBar.close();

            // Draw the right pause bar. The right pause bar transforms into the
            // bottom half of the play button triangle by animating the position of the
            // rectangle's top right coordinate and expanding its bottom width.
            mRightPauseBar.moveTo(barWidth + barDist, 0);
            mRightPauseBar.lineTo(barWidth + barDist, -mPauseBarHeight);
            mRightPauseBar.lineTo(secondBarTopRight, -mPauseBarHeight);
            mRightPauseBar.lineTo(2 * barWidth + barDist, 0);
            mRightPauseBar.close();

            canvas.save();

            // Translate the play button a tiny bit to the right so it looks more centered.
            canvas.translate(lerp(0, mPauseBarHeight / 8f, mProgress), 0);

            // (1) Pause --> Play: rotate 0 to 90 degrees clockwise.
            // (2) Play --> Pause: rotate 90 to 180 degrees clockwise.
            final float rotationProgress = mIsPlay ? 1 - mProgress : mProgress;
            final float startingRotation = mIsPlay ? 90 : 0;
            canvas.rotate(lerp(startingRotation, startingRotation + 90, rotationProgress), mWidth / 2f, mHeight / 2f);

            // Position the pause/play button in the center of the drawable's bounds.
            canvas.translate(mWidth / 2f - ((2 * barWidth + barDist) / 2f), mHeight / 2f + (mPauseBarHeight / 2f));

            // Draw the two bars that form the animated pause/play button.
            canvas.drawPath(mLeftPauseBar, mPaint);
            canvas.drawPath(mRightPauseBar, mPaint);

            canvas.restore();
        }

        public Animator getPausePlayAnimator() {
            final Animator anim = ObjectAnimator.ofFloat(this, PROGRESS, mIsPlay ? 1 : 0, mIsPlay ? 0 : 1);
            anim.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mIsPlay = !mIsPlay;
                }
            });
            return anim;
        }

        public boolean isPlay() {
            return mIsPlay;
        }

        private void setProgress(float progress) {
            mProgress = progress;
            invalidateSelf();
        }

        private float getProgress() {
            return mProgress;
        }

        @Override
        public void setAlpha(int alpha) {
            mPaint.setAlpha(alpha);
            invalidateSelf();
        }

        @Override
        public void setColorFilter(ColorFilter cf) {
            mPaint.setColorFilter(cf);
            invalidateSelf();
        }

        @Override
        public int getOpacity() {
            return PixelFormat.TRANSLUCENT;
        }

        /**
         * Linear interpolate between a and b with parameter t.
         */
        private static float lerp(float a, float b, float t) {
            return a + (b - a) * t;
        }
    }

}
