package com.ne.revival_games.entity;

/**
 * Created by vishn on 6/6/2017.
 */

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.ne.revival_games.entity.WorldObjects.Entity.Entity;
import com.ne.revival_games.entity.WorldObjects.MyWorld;

import org.dyn4j.geometry.Transform;

import java.util.List;

import static com.ne.revival_games.entity.MainThread.canvas;

public class GamePanel extends SurfaceView implements SurfaceHolder.Callback {

    public static final float WIDTH = 900;
    public static final float HEIGHT = 1600;
    private float scaleX = 1, scaleY = 1;
    private static final float ADJUST = (float) 0.02;
    private MainThread thread;
    private Background bg;
    MyWorld world;


    public GamePanel(Context context) {
        super(context);


        //add the callback to the surfaceholder to intercept events
        getHolder().addCallback(this);

        thread = new MainThread(getHolder(), this);
        world = new MyWorld(thread.canvas);

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
        //TODO: we want to initialize the engineWorld here!
        //add some intial objects like the nexus

        thread.setRunning(true);
        thread.start();

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            double canvasX = event.getX() / scaleX;
            double canvasY = event.getY()  / scaleY;
            for (Entity entity :world.objectDatabase.values()) {
//                System.out.println(entity.toString());
            }
//            System.out.println(world.rect.collided(world.barrier.shape));
            world.circ.body.translateToOrigin();
            world.circ.body.translate((canvasX - WIDTH/2)/MyWorld.SCALE, -1*(canvasY - HEIGHT/2)/MyWorld.SCALE);
            // System.out.println("X,Y"  + canvasX + ", " + canvasY);
            return true;
        }
        return super.onTouchEvent(event);
    }

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