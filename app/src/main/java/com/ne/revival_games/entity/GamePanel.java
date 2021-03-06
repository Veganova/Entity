package com.ne.revival_games.entity;

/**
 * Created by vishn on 6/6/2017.
 */

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.ne.revival_games.entity.WorldObjects.MyWorld;
import com.ne.revival_games.entity.WorldObjects.Players.Player;

import org.dyn4j.geometry.Vector2;

import java.util.ArrayList;
import java.util.List;

public class GamePanel extends SurfaceView implements SurfaceHolder.Callback {
    //need a perspective type where we control what is drawn and when and why

    public static final int cream = Color.rgb(224, 213, 218);
    public static final int background_dark = Color.rgb(39,40,34);
    public static final int background_red = Color.rgb(100,40,34);
    public static final int highlight = Color.rgb(226,171,61);
    // yellow Color.YELLOW
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
        camera = new CameraController(new Vector2(1,1), new Vector2(0, 0), this);

        this.world = world;



        // make mainPanel focusable so it can handle events
        setFocusable(true);
    }

    public void addPlayerListener(Player player) {
        this.myPlayer = player;
        // TODO: 8/13/2017 not sure why we need an array, one screen is associated with one player (and their inputs/menu, etc)
        this.myPlayers.add(player);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        System.out.println("CREATING SURFACE");
        this.scales.x = getWidth()  / mainActivity.MAP_WIDTH;
        this.scales.y = getHeight() / mainActivity.MAP_HEIGHT;
        SurfaceHolder old = getHolder();
        if (old != null) {
            old.removeCallback(this);
        }
        holder.addCallback(this);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        System.out.println("CHANGING SURFACE");
        getHolder().removeCallback(this);
        holder.addCallback(this);
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) throws RuntimeException {
        System.out.println("DESTROYING SURFACE");
        // TODO: 7/26/2017 do something here?
//        throw new RuntimeException("GG");
//        mainActivity.myThread.end();
            getHolder().removeCallback(this);


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
        camera.update();
        //draw game objects and background
        if (canvas != null) {
            final int savedState = canvas.save();
            canvas.scale((float)scales.x, (float)-scales.y);
            canvas.translate(mainActivity.MAP_WIDTH / 2, -mainActivity.MAP_HEIGHT / 2);
            canvas.scale((float) MyWorld.SCALE, (float) MyWorld.SCALE);

            camera.applyTransforms(canvas);

            canvas.drawColor(background_red);

            world.drawObjects(canvas);

            canvas.scale(1,-1);
            canvas.restoreToCount(savedState);

        }
    }

}

