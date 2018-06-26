package com.ne.revival_games.entity.CustomViews;

import android.content.Context;
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
        this.setBackground(ContextCompat.getDrawable(context, R.drawable.roundedbox));
        this.setPadding(25, 0, 25, 0);
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
}
