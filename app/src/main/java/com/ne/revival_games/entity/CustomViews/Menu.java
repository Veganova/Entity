package com.ne.revival_games.entity.CustomViews;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.VectorDrawable;
import android.media.Image;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v4.content.ContextCompat;
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
import android.widget.ScrollView;
import android.widget.Scroller;
import android.widget.TextView;

import com.ne.revival_games.entity.GamePanel;
import com.ne.revival_games.entity.R;
import com.ne.revival_games.entity.WorldObjects.Entity.Entities;
import com.ne.revival_games.entity.WorldObjects.Entity.EntityLeaf;
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


    static int ANIMATION_DURATION = 600;
    private Popper popper;
    private Poppist poppist;

    static int HEIGHT = 180;//pixels

    public MenuList(Context context, Player player) {
        super(context);

        this.setOrientation(HORIZONTAL);
//        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
//                ViewGroup.LayoutParams.WRAP_CONTENT);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
                HEIGHT);//LayoutParams.WRAP_CONTENT);

        params.bottomMargin = 50;


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
    private boolean hidden = true;
    private int ORIGINAL_WIDTH;
    private int SCALED_WIDTH;

    Popper(Context context, final Poppist toPop) {
        super(context);
        this.toPop = toPop;

//        this.setText(">");
//        setBackgroundResource(R.drawable.ic_down_arrow);



        this.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        this.setAdjustViewBounds(true);
        this.setScaleType(ScaleType.FIT_XY);


        Drawable drawable = VectorDrawableCompat.create(getResources(), R.drawable.ic_down_arrow, null);
        this.setImageDrawable(drawable);
        
        
        this.post(new Runnable() {
            @Override
            public void run() {
                ORIGINAL_WIDTH = getMeasuredWidth();
                SCALED_WIDTH = ORIGINAL_WIDTH * 4 / 5;
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

                    out.setDuration(MenuList.ANIMATION_DURATION);
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

                    in.setDuration(MenuList.ANIMATION_DURATION);
                    in.start();
                }
                hidden = !hidden;
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
        this.owner = player;


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
        this.setLayoutParams(new ViewGroup.LayoutParams(0, LayoutParams.MATCH_PARENT));

        LinearLayout container = new LinearLayout(context);
        container.setGravity(Gravity.CENTER);
        container.setBackgroundColor(GamePanel.cream);
        container.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT
                , ViewGroup.LayoutParams.MATCH_PARENT));


        // Add all the entities as buttons to this scroll view
        for (Entities entityType: this.toDisplay) {
            EntScroll entScroll = new EntScroll(context, entityType, owner);
            container.addView(entScroll);
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

            out.setDuration(MenuList.ANIMATION_DURATION);
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

            in.setDuration(MenuList.ANIMATION_DURATION);
            in.start();
        }
        this.hidden = !this.hidden;
        return this.hidden;
    }

    private class EntScroll extends ScrollView {

        public EntScroll(Context context, final Entities entType, Player player) {
            super(context);

            this.setVerticalScrollBarEnabled(false);

            LinearLayout container = new LinearLayout(context);
            container.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.MATCH_PARENT));
            container.setOrientation(LinearLayout.VERTICAL);

            for (EntityLeaf producer: entType.produceables) {
                container.addView(new EntButton(context, producer, player));
            }


            this.addView(container);
        }
    }
    
    private class EntButton extends TextView {

        public EntButton(Context context, final EntityLeaf toProduce, final Player owner) {
            super(context);

            this.setGravity(Gravity.CENTER);
            this.setPadding(50, 0, 50, 0);

            this.setTextColor(GamePanel.background_dark);
            this.setText(toProduce.name);

            // TODO: 8/25/2017 for now manually setting the height.. might be kinda bad idk. 
            this.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                    MenuList.HEIGHT));

            this.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    owner.setGhost(toProduce);
                }
            });
        }
    }

}
