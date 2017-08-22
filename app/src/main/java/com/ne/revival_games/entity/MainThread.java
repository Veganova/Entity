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
    private boolean end = false;
    private MyWorld world;
    public static Canvas canvas;
    public HashMap<GamePanel, SurfaceHolder> screens = new HashMap<>();
    private boolean doWait = false;
    private Thread waitFor;


    public MainThread(MyWorld world)
    {
        super();
        this.world = world;
        initValues();
    }

    //    private boolean locked = false;
    private long startTime;
    private long timeMillis;
    private long waitTime;
    private long totalTime = 0;
    private int frameCount =0;
    private long targetTime = 1000/FPS;

    private void initValues() {
        running = true;
        end = false;
    }

    @Override
    public void run()
    {
//        System.out.println("LOOP - WAITING");
//        if (end) {
//
//        }
//        else if(doWait) {
//            try {
//                synchronized (waitFor) {
//                    waitFor.wait();
//                }
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            } finally {
//                waitFor = null;
//                if (this.end) {
//                    // then let the code run through and end the thread
//                } else {
//
//                    // when code gets here, it has been resumed from a pause.
//                    doWait = false;
//                    initValues();
//                    this.run();
//                }
//            }
//        }
        running = true;
        end = false;
        while(running) {
//            System.out.println("IN thread LOOP");
            startTime = System.nanoTime();
            canvas = null;
            update();
            for(GamePanel gamePanel : screens.keySet()) {
                SurfaceHolder holder = screens.get(gamePanel);
                // try locking the canvas for pixel editing
                canvas = holder.lockCanvas();
                try {

//                    System.out.println("CANVAS LOCKED");
                    // TODO: 7/25/2017 see if this ever breaks
                    synchronized (this) {
                        //main thread runs functions of game panel
                        synchronized (holder) {
                            if (canvas != null) {
                                gamePanel.draw(canvas);
                            }
                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.println("ERROR IN CANVAS CREATION");
                } finally {

// System.out.println("CANVAS UNLOCKED");
                }
                if (canvas != null) {
                    try {
                        synchronized (holder) {
                            holder.unlockCanvasAndPost(canvas);

                        }
                    } catch (Exception e) {
                        e.printStackTrace();
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

        System.out.println("LOOP - WAITING");
//        if (end) {
//
//        }
//        else if(doWait) {
//            try {
//                synchronized (waitFor) {
//                    waitFor.wait();
//                }
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            } finally {
//                waitFor = null;
//                if (this.end) {
//                    // then let the code run through and end the thread
//                } else {
//
//                    // when code gets here, it has been resumed from a pause.
//                    doWait = false;
//                    initValues();
//                    this.run();
//                }
//            }
//        }
        synchronized (this) {
            this.notify();
        }
        System.out.println("GAME LOOP THREAD ENDING");
    }

    public void addNewPanel(GamePanel gamePanel, SurfaceHolder surfaceHolder){
        screens.put(gamePanel, surfaceHolder);
    }

    public HashMap<GamePanel, SurfaceHolder> getScreens() {
        return this.screens;
    }

    public void setScreens(HashMap<GamePanel, SurfaceHolder> screens) {
        this.screens = screens;
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
//        for(GamePanel panel: screens.keySet()){
//            screens.get(panel).removeCallback(panel);
//        }
    }

    public void end() {
        this.end = true;
        setRunning(false);
        if (waitFor != null) {
            // if not null, loop is paused
            synchronized (waitFor) {
                waitFor.notify();
            }
        }
//        this.setRunning(false);

//        for(GamePanel panel: screens.keySet()){
//            screens.get(panel).removeCallback(panel);
//        }
    }
}