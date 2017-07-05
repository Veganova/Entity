package com.ne.revival_games.entity;

/**
 * Created by vishn on 6/6/2017.
 */

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import com.ne.revival_games.entity.WorldObjects.MyWorld;

public class GamePanel extends SurfaceView implements SurfaceHolder.Callback {

    public static final float WIDTH = 900;
    public static final float HEIGHT = 1600;
    private float scaleX = 1, scaleY = 1;
    private static final float ADJUST = (float) 0.02;
    private MainThread thread;
    private Background bg;
    MyWorld world;


//    private class TouchListener implements OnTouchListener {
//        private GestureDetector gestureDetector =
//                new GestureDetector(Test.this, new GestureDetector.SimpleOnGestureListener() {
//                    @Override
//                    public boolean onDoubleTap(MotionEvent e) {
//                        return super.onDoubleTap(e);
//                    }
//                });
//        @Override
//        public boolean onTouch(View v, MotionEvent event) {
//            gestureDetector.onTouchEvent(event);
//            return true;
//        }
//    }

    private abstract class DoubleClickListener implements OnTouchListener {

        private static final long DOUBLE_CLICK_TIME_DELTA = 300;//milliseconds

        long lastClickTime = 0;


        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                long clickTime = System.currentTimeMillis();
                if (clickTime - lastClickTime < DOUBLE_CLICK_TIME_DELTA) {
                    onDoubleClick(event);
                } else {
                    onSingleClick(event);
                }
                lastClickTime = clickTime;
                return true;
            }
            return false;
        }

        abstract void onSingleClick(MotionEvent v);
        abstract void onDoubleClick(MotionEvent v);
    }

    public GamePanel(Context context) {


        super(context);


        //add the callback to the surfaceholder to intercept events
        getHolder().addCallback(this);

        thread = new MainThread(getHolder(), this);
        world = new MyWorld(thread.canvas);

        this.setOnTouchListener(new DoubleClickListener() {
            @Override
            void onSingleClick(MotionEvent event) {
                System.out.println("SINGLE");
                double canvasX = event.getX() / scaleX;
                double canvasY = event.getY()  / scaleY;
                world.nex.shape.body.translateToOrigin();
                world.nex.shape.body.translate((canvasX - WIDTH/2)/MyWorld.SCALE,
                        -1*(canvasY - HEIGHT/2)/MyWorld.SCALE);
            }

            @Override
            void onDoubleClick(MotionEvent event) {
                System.out.println("DOUBLE");
//                if (world.ghost.canPlace()) {
//                    System.out.println("PLACED");
//                    world.nex = world.ghost.place();
//                }
            }
        });

        // make gamePanel focusable so it can handle events
        setFocusable(true);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        boolean retry = true;
        int counter = 0;
        while (retry && counter < 1000) {
            counter++;
            try {
                thread.setRunning(false);
                thread.join();
                retry = false;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        bg = new Background(BitmapFactory.decodeResource(getContext().getResources(),
                R.drawable.whitebackground1));
        //we can safely start the game loop
        //TODO: we want to initialize the engineWorld here!
        //add some intial objects like the nexus

        thread.setRunning(true);
        thread.start();

    }

//    @Override
//    public boolean onTouchEvent(MotionEvent event) {
//        if (event.getAction() == MotionEvent.ACTION_DOWN) {
//
//            return true;
//        }
//        return super.onTouchEvent(event);
//    }

    public void update() {
        //where we update individual game objects (ex. move them, etc.)

        world.objectUpdate();
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        //seems a bit inefficient
        scaleX = getWidth()  / WIDTH;
        scaleY = getHeight() / HEIGHT;

        //draw game objects and background
        if (canvas != null) {
            final int savedState = canvas.save();
            //scaling does funky stuff to length / width so be careful LMAO
           canvas.scale(scaleX, -scaleY);
            canvas.translate(WIDTH / 2, -HEIGHT / 2);
            canvas.scale((float)MyWorld.SCALE, (float) MyWorld.SCALE);


            bg.draw(canvas);
            world.drawObjects(canvas);

            canvas.restoreToCount(savedState);
        }
    }
}