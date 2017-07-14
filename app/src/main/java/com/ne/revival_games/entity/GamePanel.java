package com.ne.revival_games.entity;

/**
 * Created by vishn on 6/6/2017.
 */

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import com.ne.revival_games.entity.TouchListeners.CameraController;
import com.ne.revival_games.entity.WorldObjects.MyWorld;
import com.ne.revival_games.entity.WorldObjects.Player;

import org.dyn4j.geometry.Vector2;

import java.util.ArrayList;
import java.util.List;

public class GamePanel extends SurfaceView implements SurfaceHolder.Callback {

    public static final float WIDTH = 900;
    public static final float HEIGHT = 1600;
    //private float scaleX = 1, scaleY = 1;
    Vector2 scales;
    private static final float ADJUST = (float) 0.02;
    private MainThread thread;
    private Background bg;
    public CameraController camera;
    MyWorld world;
    private List<Player> players = new ArrayList<>();
    private List<OnTouchListener> listeners = new ArrayList<>();

    private abstract class DoubleClickListener implements OnTouchListener {

        private static final long DOUBLE_CLICK_TIME_DELTA = 300;//milliseconds

        private long lastClickTime = 0;


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
        this.scales = new Vector2(1, 1);
        camera = new CameraController(new Vector2(0,0), new Vector2(0,0));
        //add the callback to the surfaceholder to intercept events
        getHolder().addCallback(this);

        thread = new MainThread(getHolder(), this);

        world = new MyWorld(thread.canvas);
        world.addPlayers(players);
        // make gamePanel focusable so it can handle events
        setFocusable(true);
    }

    public void addPlayerListener(Player player) {
        this.players.add(player);
    }

    public void addListeners(OnTouchListener listener) {
        this.listeners.add(listener);
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
        for (Player player: players) {
            player.onTouch(this, event);
        }
        for (OnTouchListener listener: listeners) {
            listener.onTouch(this, event);
        }
        return true;
    }

    public void update() {
        //where we update individual game objects (ex. move them, etc.)

        world.objectUpdate();
    }

    void setScales() {
        this.scales.x = getWidth()  / WIDTH;
        this.scales.y = getHeight() / HEIGHT;
    }

    @Override
    public void draw(Canvas canvas) {
        //c.applyToCanvas(canvas); camera stuff.. could be usefull
//        canvas.scale(1f, 0.5f);

        super.draw(canvas);

        // perhaps only call this when there is a resize or something of the sort
        setScales();
        //draw game objects and background
        if (canvas != null) {



            final int savedState = canvas.save();





            //scaling does funky stuff to length / width so be careful LMAO
           canvas.scale((float)scales.x, (float)-scales.y);
            canvas.translate(WIDTH / 2, -HEIGHT / 2);
            canvas.scale((float)MyWorld.SCALE, (float) MyWorld.SCALE);

           // bg.draw(canvas);
            canvas.drawColor(Color.rgb(39,40,34));
            world.drawObjects(canvas);






            canvas.restoreToCount(savedState);
        }
    }
}