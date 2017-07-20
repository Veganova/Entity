package com.ne.revival_games.entity;

/**
 * Created by vishn on 6/6/2017.
 */

import android.graphics.Canvas;
import android.graphics.Color;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnDragListener;

import com.ne.revival_games.entity.WorldObjects.MyWorld;
import com.ne.revival_games.entity.WorldObjects.Player;

import org.dyn4j.geometry.Vector2;

import java.util.ArrayList;
import java.util.List;

public class GamePanel extends SurfaceView implements SurfaceHolder.Callback {
    //need a perspective type where we control what is drawn and when and why

    //private float scaleX = 1, scaleY = 1;
    public Vector2 scales;
    private Player myPlayer; //create an enum maybe ?
    public CameraController camera;
    public MyWorld world;
    MainActivity mainActivity;
    private List<Player> myPlayers = new ArrayList<>();


    public GamePanel(MainActivity mainActivity, MyWorld world) {
        super(mainActivity);
        this.mainActivity = mainActivity;
        this.scales = new Vector2(1, 1);
        camera = new CameraController(new Vector2(1,1), new Vector2(300, 300), this);
        getHolder().addCallback(this);
        this.world = world;


        // make mainPanel focusable so it can handle events
        setFocusable(true);
    }

    public void addPlayerListener(Player player) {
        this.myPlayers.add(player);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) throws RuntimeException {
        throw new RuntimeException("GG");
//            boolean retry = true;
//            int counter = 0;
//            while (retry && counter < 1000) {
//                counter++;
//                try {
//                    thread.setRunning(false);
//                    thread.join();
//                    retry = false;
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//
//            }

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        this.scales.x = getWidth()  / mainActivity.MAP_WIDTH;
        this.scales.y = getHeight() / mainActivity.MAP_HEIGHT;
    }

    public void setScales(){
        this.scales.x = getWidth()  / mainActivity.MAP_WIDTH;
        this.scales.y = getHeight() / mainActivity.MAP_HEIGHT;
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        for (Player player: myPlayers) {
            player.onTouch(this, event);
        }
        return true;
    }


    @Override
    public void draw(Canvas canvas) {
        //c.applyToCanvas(canvas); camera stuff.. could be usefull
//        canvas.scale(1f, 0.5f);
        super.draw(canvas);
        camera.update();
        //draw game objects and background
        if (canvas != null) {
            final int savedState = canvas.save();
            canvas.scale((float)scales.x, (float)-scales.y);
            canvas.translate(mainActivity.MAP_WIDTH / 2, -mainActivity.MAP_HEIGHT / 2);
            canvas.scale((float) MyWorld.SCALE, (float) MyWorld.SCALE);


            camera.applyTransforms(canvas);

            canvas.drawColor(Color.rgb(39,40,34));
            world.drawObjects(canvas);
            canvas.restoreToCount(savedState);
        }
    }

}

