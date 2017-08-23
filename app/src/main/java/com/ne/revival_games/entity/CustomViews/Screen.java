package com.ne.revival_games.entity.CustomViews;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.ne.revival_games.entity.GamePanel;

import org.dyn4j.geometry.Vector2;

/**
 * Created by Veganova on 8/23/2017.
 */

public class Screen extends RelativeLayout {
    GamePanel panel;

    public Screen(Context context, GamePanel panel) {
        super(context);
        this.panel = panel;

        panel.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        this.addView(panel);
    }


    public GamePanel getGamePanel() {
        return panel;
    }
}
