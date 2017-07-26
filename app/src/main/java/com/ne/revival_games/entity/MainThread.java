package com.ne.revival_games.entity;

/**
 * Created by vishn on 6/6/2017.
 */

import android.graphics.Canvas;
import android.view.SurfaceHolder;

import com.ne.revival_games.entity.WorldObjects.MyWorld;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.SynchronousQueue;

/**
 * Created by vishn on 6/26/2016.
 */

public class MainThread extends Thread
{
    private int FPS = 40;
    private double averageFPS;
    private boolean running;
    private MyWorld world;
    public static Canvas canvas;
    public HashMap<GamePanel, SurfaceHolder> screens = new HashMap<>();
    private boolean doWait = false;
    private Thread waitFor;


    public MainThread(MyWorld world)
    {
        super();
        this.world = world;
    }

    //    private boolean locked = false;
    private long startTime;
    private long timeMillis;
    private long waitTime;
    private long totalTime = 0;
    private int frameCount =0;
    private long targetTime = 1000/FPS;
    @Override
    public void run()
    {
        running = true;
        while(running) {
            startTime = System.nanoTime();
            canvas = null;
            update();
            for(GamePanel gamePanel : screens.keySet()) {
                // try locking the canvas for pixel editing
                try {
                    canvas = screens.get(gamePanel).lockCanvas();
                    // TODO: 7/25/2017 see if this ever breaks
                    synchronized (this) {//screens.get(gamePanel)) {

                        //main thread runs functions of game panel
//                    System.out.println("Updating!");
                        if (canvas !=null) {
                            gamePanel.draw(canvas);
                        }

                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.println("ERROR IN CANVAS CREATION");
                } finally {
                    if (canvas != null) {
                        try {
                            screens.get(gamePanel).unlockCanvasAndPost(canvas);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
            timeMillis = (System.nanoTime() - startTime) / 1000000;
            waitTime = targetTime-timeMillis;

            try{
                this.sleep(waitTime);
            }catch(Exception e){}

            totalTime += System.nanoTime()-startTime;
            frameCount++;
            if(frameCount == FPS)
            {
                averageFPS = 1000/((totalTime/frameCount)/1000000);
                frameCount =0;
                totalTime = 0;
            }

        }

        System.out.println("WAITING");
        if(doWait) {
            try {
                synchronized (waitFor) {
                    waitFor.wait();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                doWait = false;
                this.run();
            }
        }
        System.out.println("NOT WAITING");
    }

    public void addNewPanel(GamePanel gamePanel, SurfaceHolder surfaceHolder){
        screens.put(gamePanel, surfaceHolder);
    }

    public void update() {
        //where we update individual game objects (ex. move them, etc.)
        world.objectUpdate();
    }

    public void setRunning(boolean b)
    {
        running=b;
    }

    public void pause(Thread a) throws InterruptedException {
        this.doWait = true;
        this.waitFor = a;
        this.setRunning(false);
    }

    public void end() {
        this.setRunning(false);

    }
}