package com.ne.revival_games.entity.CustomViews;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Build;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Space;
import android.widget.TextView;

import com.ne.revival_games.entity.GamePanel;
import com.ne.revival_games.entity.WorldObjects.FrameTime;

public class LoadingBar extends RelativeLayout {

    private ProgressBar progressBar;
    private TextView textView;
    private int progressStatus = 0;

//    private Handler handler = new Handler();
    public LoadingBar(final Context context) {
        super(context);

        this.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        this.setGravity(Gravity.CENTER | Gravity.TOP);
        this.setPadding(0, 10, 0, 0);

        final LinearLayout verticalContainer = new LinearLayout(context);
        verticalContainer.setOrientation(LinearLayout.VERTICAL);
        verticalContainer.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));


        progressBar = new ProgressBar(context, null, android.R.attr.progressBarStyleHorizontal);
        progressBar.getProgressDrawable().setColorFilter(GamePanel.cream, PorterDuff.Mode.SRC_IN);


        this.textView = new TextView(context);
//        this.textView.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        this.textView.setTextColor(GamePanel.cream);
        this.textView.setTextSize(18);
        this.textView.setGravity(Gravity.CENTER);

        final LinearLayout progressBarContainer = new LinearLayout(context);
        LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        progressBarContainer.setLayoutParams(param);


//         Create new layout parameters for progress bar
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1.0f);
        Space s1 = new Space(context);
        Space s2 = new Space(context);
        s1.setLayoutParams(lp);
        s2.setLayoutParams(lp);
        progressBar.setLayoutParams(lp);

        // Set the progress bar color
        progressBar.getProgressDrawable().setColorFilter(GamePanel.cream, PorterDuff.Mode.SRC_IN);

        // Finally,  add the progress bar to layout
        progressBarContainer.addView(s1);
        progressBarContainer.addView(progressBar);
        progressBarContainer.addView(s2);



        verticalContainer.addView(textView);
        verticalContainer.addView(progressBarContainer);
        this.addView(verticalContainer);

        this.progressBar.setVisibility(INVISIBLE);
        this.textView.setVisibility(INVISIBLE);
    }

    /**
     * @param frameTime     Amount of frames to load for.
     */
    public void setProgress(final int frameTime, String message) {
        System.out.println("SETTING PROGRESS!");
        this.progressBar.setVisibility(VISIBLE);

        setMessage(message);
        for (int i = 1; i <= frameTime; i++) {
            final int finalI = i;
            FrameTime.addCallBackAtDeltaFrames(i, new Runnable() {
                @Override
                public void run() {
//                    System.out.println("Progress: " + finalI + " " + ((int) ((100 * finalI)/ (1.0 * frameTime))) + " " + progressBar.getProgress());
                    progressBar.setProgress((int) ((100 * finalI)/ (1.0 * frameTime)));
                }
            });
        }
    }

    public void hideBar() {
        progressBar.setVisibility(INVISIBLE);
    }

    public void setMessage(String message) {
        this.textView.setVisibility(VISIBLE);
        this.textView.setText(message);
    }

    public void hideMessage() {
        this.textView.setVisibility(INVISIBLE);
    }
}
