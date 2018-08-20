package com.ne.revival_games.entity.CustomViews;

import android.content.Context;
import android.graphics.Point;
import android.support.v4.view.GestureDetectorCompat;
import android.view.Display;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ne.revival_games.entity.GamePanel;
import com.ne.revival_games.entity.WorldObjects.Entity.Creators.Entities;
import com.ne.revival_games.entity.WorldObjects.Entity.Creators.EntityLeaf;
import com.ne.revival_games.entity.WorldObjects.MySettings;
import com.ne.revival_games.entity.WorldObjects.Players.Player;
import com.ne.revival_games.entity.WorldObjects.Query;

import java.util.Arrays;
import java.util.List;

public class MenuList extends LinearLayout {


    static int ANIMATION_DURATION = 600;
    static double SCREEN_PORTION = 0.75;
    private final ArrowPop tray;
    private Poppist poppist;

    int HEIGHT;

    public MenuList(Context context, int parentHeight, Player player, float SCREEN_WIDTH, boolean startHidden) {
        super(context);

        this.HEIGHT = (int) (parentHeight * 0.1);
        this.setOrientation(HORIZONTAL);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
                HEIGHT);

        params.bottomMargin = 50;

        this.poppist = new Poppist(context, Arrays.asList(Entities.values()), player);
        this.tray = new ArrowPop(context, poppist, startHidden, true, ArrowPop.SIDE.LEFT, SCREEN_WIDTH);

        this.setLayoutParams(params);
        this.addView(tray);
    }

    public ArrowPop getTray() {
        return this.tray;
    }

    class Poppist extends HorizontalScrollView {

        private List<Entities> toDisplay;

        private final int MAX_WIDTH;
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
            MAX_WIDTH = (int) (width * SCREEN_PORTION);

            this.setHorizontalScrollBarEnabled(false);
            this.setLayoutParams(new ViewGroup.LayoutParams(MAX_WIDTH, LayoutParams.MATCH_PARENT));

            LinearLayout container = new LinearLayout(context);
            container.setGravity(Gravity.CENTER);
            container.setBackgroundColor(GamePanel.cream);
            container.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT
                    , ViewGroup.LayoutParams.MATCH_PARENT));


            // Add all the entities as buttons to this scroll view
            for (Entities entityType : this.toDisplay) {
                container.addView(new EntButton(context, entityType.produceable, owner));
            }

            this.addView(container);
        }

//        private class EntScroll extends ScrollView {
//
//            public EntScroll(Context context, final Entities entType, Player player) {
//                super(context);
//
//                this.setVerticalScrollBarEnabled(false);
//
//                LinearLayout container = new LinearLayout(context);
//                container.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
//                        LinearLayout.LayoutParams.MATCH_PARENT));
//                container.setOrientation(LinearLayout.VERTICAL);
//
//                for (EntityLeaf producer : entType.produceables) {
//                    container.addView(new EntButton(context, producer, player));
//                }
//
//
//                this.addView(container);
//            }
//        }


        private class EntButton extends TextView {


            private final GestureDetectorCompat mDetector;

            public EntButton(final Context context, final EntityLeaf toProduce, final Player owner) {
                super(context);

                this.setGravity(Gravity.CENTER);
                this.setPadding(50, 0, 50, 0);

                this.setTextColor(GamePanel.background_dark);
                Double cost = MySettings.getEntityNum(String.valueOf(owner.team), new Query(toProduce.name, "cost"), true);
                String text = toProduce.name;
                this.setText(text);
                this.mDetector = new GestureDetectorCompat(context, new MenuDragListener());
                // TODO: 8/25/2017 for now manually setting the height.. might be kinda bad idk.
                this.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                        MenuList.this.HEIGHT));

//                this.setOnClickListener(new OnClickListener() {
//                    public void onClick(View v) {
//
//                    }
//                });
                EntButton.this.setOnTouchListener(new OnSwipeTouchListener(context, MenuList.this.HEIGHT) {
                    public void onSwipeOut(MotionEvent e) {
                        int[] location = new int[2];
                        EntButton.this.getLocationOnScreen(location);
//                        Toast.makeText(context, "out!!", Toast.LENGTH_SHORT).show();
                        owner.setGhost(toProduce, location[0] + e.getX(), location[1] + e.getY(), e);
                    }


                });

            }


//            @Override
//            public boolean onTouchEvent(MotionEvent event) {
//                int x = (int)event.getX();
//                int y = (int)event.getY();
//
//                switch (event.getAction()) {
//                    case MotionEvent.ACTION_DOWN:
//                        System.out.println("D: " + x + " " + y);
//                        break;
//                    case MotionEvent.ACTION_MOVE:
//                        System.out.println("M: " + x + " " + y);
//                        break;
//                    case MotionEvent.ACTION_UP:
//                        System.out.println("U: " + x + " " + y);
//                        break;
//                    default:
//                        System.out.println("asdf");
//                }
//                return false;
//            }
        }



    }
}
