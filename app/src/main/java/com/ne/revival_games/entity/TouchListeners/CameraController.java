package com.ne.revival_games.entity.TouchListeners;

import android.graphics.Canvas;
import android.view.MotionEvent;
import android.view.View;

import com.ne.revival_games.entity.MainThread;
import com.ne.revival_games.entity.WorldObjects.MyWorld;

import org.dyn4j.geometry.Vector2;

/**
 * Created by vishn on 7/8/2017.
 */

public class CameraController{
       public Vector2 zoomXY;
       public Vector2 translateXY;

        public CameraController(Vector2 relativeZoomXY, Vector2 relativeTranslationXY){
                zoomXY = new Vector2(relativeZoomXY.x/ MyWorld.SCALE, relativeZoomXY.y/MyWorld.SCALE);
                translateXY = new Vector2(relativeTranslationXY.x/MyWorld.SCALE,
                        relativeTranslationXY.y/MyWorld.SCALE);
        }
        public void relativeZoom(double x, double y){
            zoomXY.x += x;
            zoomXY.y += y;
            MainThread.canvas.scale((float)zoomXY.x, (float) zoomXY.y);
        }

        public void zoom(double x, double y){
            zoomXY.x = x;
            zoomXY.y = y;
            MainThread.canvas.scale((float)zoomXY.x, (float) zoomXY.y);
        }

        public void relativeMove(double x, double y){
            translateXY.x -= x/MyWorld.SCALE;
            translateXY.y -= y/MyWorld.SCALE;
            MainThread.canvas.translate((float) translateXY.x, (float) translateXY.y);

        }

        public void move(double x, double y){
            translateXY.x = -1*x/MyWorld.SCALE;
            translateXY.y = -1*y/MyWorld.SCALE;
            MainThread.canvas.translate((float) translateXY.x, (float) translateXY.y);
        }




}
