package com.ne.revival_games.entity.CustomViews;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.ne.revival_games.entity.R;
import com.ne.revival_games.entity.WorldObjects.Entity.Entities;
import com.ne.revival_games.entity.WorldObjects.Players.Player;

import java.util.Arrays;
import java.util.List;

/**
 * Set of classes that make up the menu that the player can open and close and select items to build
 */

public class Menu extends RelativeLayout {

    public Menu(Context context, Player player, int gravity) {
        super(context);

        this.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.MATCH_PARENT));
        this.setGravity(gravity);

        MenuList listing = new MenuList(context, player);
        this.addView(listing);
    }
}


class MenuList extends LinearLayout {


    private Popper popper;
    private Poppist poppist;

    public MenuList(Context context, Player player) {
        super(context);

        this.setOrientation(HORIZONTAL);
//        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
//                ViewGroup.LayoutParams.WRAP_CONTENT);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);

//        params.topMargin = height;


//        this.setLayoutParams(params);

        poppist = new Poppist(context, Arrays.asList(Entities.values()), player);
        popper = new Popper(context, poppist);


        this.addView(poppist);
        this.addView(popper);
        this.setLayoutParams(params);
    }

}

class Popper extends ImageView {

    private final Poppist toPop;

    Popper(Context context, final Poppist toPop) {
        super(context);
        this.toPop = toPop;

//        this.setText(">");
        setBackgroundResource(R.drawable.ic_arrow);
        int width = 60;
        int height = 60;
        this.setScaleType(ScaleType.FIT_CENTER);
        this.setAdjustViewBounds(true);
        this.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        this.setVisibility(View.INVISIBLE);
        this.post(new Runnable() {

            @Override
            public void run() {
                ViewGroup.LayoutParams mParams;
                mParams = (ViewGroup.LayoutParams) Popper.this.getLayoutParams();
                mParams.width = Popper.this.getHeight();
                Popper.this.setLayoutParams(mParams);
                Popper.this.setVisibility(View.VISIBLE);
                Popper.this.postInvalidate();
            }
        });

        this.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                toPop.toggle();
            }
        });
    }
}

class Poppist extends HorizontalScrollView {

    private List<Entities> toDisplay;
    private boolean  hidden = true;

    private final int MAX_WIDTH;

    private ValueAnimator in, out;
    private Player owner;


    Poppist(Context context, List<Entities> toDisplay, Player player) {
        super(context);
        this.toDisplay = toDisplay;
        // TODO: 7/20/2017 might want to do this with weights instead.. for handling the changing screen orientation
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
//        System.out.println("WIDTH ---- " + width);
        MAX_WIDTH = (int)(width * 3) / 4;

        //this.getLayoutParams().width = MAX_WIDTH;
        this.setHorizontalScrollBarEnabled(false);
        this.setLayoutParams(new ViewGroup.LayoutParams(0, LayoutParams.WRAP_CONTENT));

        LinearLayout container = new LinearLayout(context);

        this.owner = player;

        // Add all the entities as buttons to this scroll view
        for (Entities entityType: this.toDisplay) {
            Button entButton = new EntButton(context, entityType, owner);
            container.addView(entButton);
        }

        this.addView(container);
    }

    /**
     * Toggles the poppist on and off - triggering the appropriate animation.
     *
     * @return Returns the boolean status of the hidden variable after the toggle
     */
    boolean toggle() {
        if (this.hidden) {
            out = ValueAnimator.ofInt(this.getMeasuredWidth(), MAX_WIDTH);
            out.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    int val = (Integer) valueAnimator.getAnimatedValue();
                    ViewGroup.LayoutParams layoutParams = getLayoutParams();
                    layoutParams.width = val;
                    setLayoutParams(layoutParams);
                }
            });

            out.setDuration(600);
            out.start();
        } else {
            in = ValueAnimator.ofInt(this.getMeasuredWidth(), 0);
            in.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    int val = (Integer) valueAnimator.getAnimatedValue();
                    ViewGroup.LayoutParams layoutParams = getLayoutParams();
                    layoutParams.width = val;
                    setLayoutParams(layoutParams);
                }
            });

            in.setDuration(600);
            in.start();
        }
        this.hidden = !this.hidden;
        return this.hidden;
    }

    class EntButton extends Button {

        private Entities entType;
        private final Player owner;

        public EntButton(Context context, final Entities entType, final Player owner) {
            super(context);

            this.entType = entType;

            this.setText(entType.toString());
            this.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT));


            this.owner = owner;

            this.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    owner.setGhost(entType);
                }
            });
        }
    }

}
