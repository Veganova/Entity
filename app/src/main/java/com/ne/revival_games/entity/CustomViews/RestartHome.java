package com.ne.revival_games.entity.CustomViews;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.ne.revival_games.entity.GamePanel;
import com.ne.revival_games.entity.MainActivity;
import com.ne.revival_games.entity.R;
import com.ne.revival_games.entity.WorldObjects.Entity.Creators.Entities;
import com.ne.revival_games.entity.WorldObjects.Entity.Creators.EntityLeaf;
import com.ne.revival_games.entity.WorldObjects.Players.Player;

import java.sql.SQLOutput;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Veganova on 9/10/2017.
 */

/*
public class RestartHome extends LinearLayout {

    private BreakListing restart;
    private BreakListingEnd home;

    public RestartHome(Context context, float parentHeight, MainActivity activity, MainMenu.GameMode mode) {
        super(context);
        this.setOrientation(VERTICAL);
        RelativeLayout.LayoutParams layout = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.MATCH_PARENT);
        layout.setMargins(0, 0, 0, 100);
        this.setLayoutParams(layout);
        this.setGravity(Gravity.LEFT | Gravity.CENTER);

        restart = new BreakListing(context, (int) parentHeight, activity, mode);
        restart.setClickable(true);
        home = new BreakListingEnd(context, (int) parentHeight, activity);
//        this.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                System.out.println("CLICKING MAIN");
//            }
//        });

        restart.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("CLICKING RESTART");
            }
        });
        home.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("CLICKING HOME");
            }
        });
        //listing.getLayoutParams().width = 0;
        this.addView(restart);
        this.addView(home);
    }

    public void pop() {
        this.restart.pop();
        this.home.pop();
    }
}

class BreakListing extends PopListing {

    private final MainActivity activity;
    private final MainMenu.GameMode mode;

    public BreakListing(Context context, int parentHeight, MainActivity activity, MainMenu.GameMode mode) {
        super(context, parentHeight);
        this.activity = activity;
        this.mode = mode;

        super.create(context);
        this.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("ONCLICK RESTART");
                ((BreakListing.BreakItems) (listing)).onClick();
            }
        });
    }

    @Override
    protected Poppist createListing(Context context) {
        return new BreakListing.BreakItems(context, activity, mode);
    }

    @Override
    protected Popper createPopper(Context context, Poppist listing) {
        return new BreakListing.BreakPop(context, listing);
    }

    class BreakPop extends Popper {
        BreakPop(Context context, final Poppist toPop) {
            super(context, toPop, R.drawable.ic_down_arrow, 0);

        }
    }

    class BreakItems extends Poppist {
        Runnable r;

        BreakItems(final Context context, final MainActivity activity, final MainMenu.GameMode mode) {
            super(context, 0, 0.13, 250);

            ImageView icon = new ImageView(context);
            Drawable drawable = VectorDrawableCompat.create(getResources(), R.drawable.ic_restart, null);
            icon.setColorFilter(GamePanel.background_dark);
            icon.setImageDrawable(drawable);

            this.setBackgroundColor(GamePanel.cream);

            this.container.addView(icon);
            this.container.setPadding(50, 0, 0, 0);

            r = new Runnable() {
                @Override
                public void run() {
                    final Intent intent = new Intent(context, MainActivity.class);
                    intent.putExtra("GameMode", mode);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);

                    // wait for the current activity thread to end. (so that it will let go of the canvas lock)
                    activity.finish();
                    if (activity.myThread.hasEnded()) {

                    } else {
                        try {
                            synchronized (activity.myThread) {
                                activity.myThread.wait();
                            }
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    activity.startActivity(intent);
                }
            };

            this.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    System.out.println("WAT UP BOI");
                }
            });
        }

        void onClick() {
            r.run();
        }


    }
}

class BreakListingEnd extends PopListing {

    private final MainActivity activity;

    public BreakListingEnd(Context context, int parentHeight, MainActivity activity) {
        super(context, parentHeight);
        this.activity = activity;

        super.create(context);
        this.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("ONCLICK HOME");
                ((BreakListing.BreakItems) (listing)).onClick();
            }
        });
    }

    @Override
    protected Poppist createListing(Context context) {
        return new BreakListingEnd.BreakItems(context, activity);
    }

    @Override
    protected Popper createPopper(Context context, Poppist listing) {
        return new BreakListingEnd.BreakPop(context, listing);
    }

    class BreakPop extends Popper {
        BreakPop(Context context, final Poppist toPop) {
            super(context, toPop, R.drawable.ic_down_arrow, 0);

        }
    }

    class BreakItems extends Poppist {

        Runnable r;

        BreakItems(final Context context, final MainActivity activity) {
            super(context, 0, 0.13, 250);

            ImageView icon = new ImageView(context);
            Drawable drawable = VectorDrawableCompat.create(getResources(), R.drawable.ic_restart, null);
            icon.setColorFilter(GamePanel.background_dark);
            icon.setImageDrawable(drawable);

            this.setBackgroundColor(GamePanel.cream);

            this.container.addView(icon);
            this.container.setPadding(50, 0, 0, 0);

            r = new Runnable() {
                @Override
                public void run() {
                    activity.finish();
                }
            };
        }

        void onClick() {
            r.run();
        }

    }
}
*/

public class RestartHome extends RelativeLayout {
    private static final int ANIMATION_DURATION = 600;
    private int HEIGHT;
    private RestartButton rb;
    private HomeButton home;

    public RestartHome(Context context, float parentHeight, MainActivity activity, MainMenu.GameMode mode) {
        super(context);
        this.HEIGHT = (int) (parentHeight * 0.2);
        this.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.MATCH_PARENT));

        this.setGravity(Gravity.LEFT | Gravity.CENTER);

        ImageView image = new ImageView(context);
        Drawable drawable = VectorDrawableCompat.create(getResources(), R.drawable.ic_restart, null);
        image.setColorFilter(GamePanel.background_dark);
        image.setImageDrawable(drawable);

//        rb = new RestartButton(context, activity, mode);
        home = new HomeButton(context, activity);
        rb = new RestartButton(context, activity, mode);
        Container c = new Container(context, image, home);
        Container c2  = new Container(context, image, home);
        this.addView(c);
        this.addView(c2);

    }

    public void pop() {
        // move the image out
    }

    class Container extends LinearLayout {
        public Container(Context context, ImageView icon, final View image) {
            super(context);

            LinearLayout l = new LinearLayout(context);
            l.setBackgroundColor(GamePanel.cream);
            l.addView(icon);
            l.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.MATCH_PARENT));
            l.setGravity(Gravity.CENTER);

            this.setOrientation(HORIZONTAL);
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
                    HEIGHT);
            this.setLayoutParams(params);

            this.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    image.callOnClick();
                }
            });

            this.addView(l);
            this.addView(image);
        }
    }

    private class HomeButton extends ImageView {
        HomeButton(Context context, final MainActivity activity) {
            super(context);

            this.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.MATCH_PARENT));
            this.setAdjustViewBounds(true);
            this.setScaleType(ScaleType.FIT_XY);

            Drawable drawable = VectorDrawableCompat.create(getResources(), R.drawable.ic_down_arrow, null);
            this.setImageDrawable(drawable);

            this.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    activity.finish();
                }
            });
        }
    }

    private class RestartButton extends ImageView {
        RestartButton(final Context context, final MainActivity activity, final MainMenu.GameMode mode) {
            super(context);

            this.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.MATCH_PARENT));
            this.setAdjustViewBounds(true);
            this.setScaleType(ScaleType.FIT_XY);

            Drawable drawable = VectorDrawableCompat.create(getResources(), R.drawable.ic_isosceles, null);
            this.setImageDrawable(drawable);


            this.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    final Intent intent = new Intent(context, MainActivity.class);
                    intent.putExtra("GameMode", mode);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);

                    // wait for the current activity thread to end. (so that it will let go of the canvas lock)
                    activity.finish();
                    if (activity.myThread.hasEnded()) {

                    } else {
                        try {
                            synchronized (activity.myThread) {
                                activity.myThread.wait();
                            }
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    activity.startActivity(intent);
                }
            });
        }
    }
}
