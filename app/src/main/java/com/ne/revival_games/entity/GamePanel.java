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

import com.ne.revival_games.entity.WorldObjects.Entity.Entity;
import com.ne.revival_games.entity.WorldObjects.MyWorld;

import java.util.List;

public class GamePanel extends SurfaceView implements SurfaceHolder.Callback {

    private static final float WIDTH = 900;
    private static final float HEIGHT = 1600;
    private static final float ADJUST = (float) 0.02;
    private MainThread thread;
    private Background bg;
    MyWorld world;


    public GamePanel(Context context) {
        super(context);


        //add the callback to the surfaceholder to intercept events
        getHolder().addCallback(this);

        thread = new MainThread(getHolder(), this);

        //make gamePanel focusable so it can handle events
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
        thread.setRunning(true);
        thread.start();

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {


        return super.onTouchEvent(event);
    }

    public void update() {
        //where we update individual game objects (ex. move them, etc.)


    }

    @Override
    public void draw(Canvas canvas) {
        //seems a bit inefficient
        float scaleX = getWidth() / WIDTH;
        float scaleY = getHeight() / HEIGHT;

        //draw game objects and background
        if (canvas != null) {
            final int savedState = canvas.save();
            //scaling does funky stuff to length / width so be careful LMAO
            canvas.scale(scaleX, scaleY);
            bg.draw(canvas);

            canvas.restoreToCount(savedState);
        }
    }
}