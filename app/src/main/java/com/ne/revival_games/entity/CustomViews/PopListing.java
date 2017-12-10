package com.ne.revival_games.entity.CustomViews;

import android.content.Context;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.ne.revival_games.entity.WorldObjects.Entity.Creators.Entities;
import com.ne.revival_games.entity.WorldObjects.Players.Player;

import java.util.Arrays;

/**
 * Created by Veganova on 10/24/2017.
 */
public abstract class PopListing extends LinearLayout {

    protected Popper popImage;
    protected Poppist listing;

    final int HEIGHT;// pixels
    public PopListing(Context context, int parentHeight) {
        super(context);

        this.HEIGHT = (int) (parentHeight * 0.1);
        this.setOrientation(HORIZONTAL);
    }

    /**
     * Child must call this method.
     *
     * @param context
     */
    protected void create(Context context) {
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                HEIGHT);
        params.bottomMargin = 50;

        listing = createListing(context);
        popImage = createPopper(context, listing);

        this.addView(listing);
        this.addView(popImage);
        this.setLayoutParams(params);
    }

    void pop() {
        this.popImage.callOnClick();
    }

    protected abstract Poppist createListing(Context context);

    protected abstract Popper createPopper(Context context, Poppist listing);
}
