package com.ne.revival_games.entity.CustomViews;

import android.content.Context;
import android.widget.RelativeLayout;

import com.ne.revival_games.entity.WorldObjects.Players.Player;

/**
 * Set of classes that make up the menu that the player can open and close and select items to build
 */

public class Menu extends RelativeLayout {

    private final MenuList listing;

    public Menu(Context context, int parentHeight, Player player, int gravity, float SCREEN_WIDTH, boolean startHidden) {
        super(context);

        this.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.MATCH_PARENT));
        this.setGravity(gravity);

        this.listing = new MenuList(context, parentHeight, player, SCREEN_WIDTH, startHidden);
        this.addView(listing);
    }

    public MenuList getListing() {
        return listing;
    }
}


