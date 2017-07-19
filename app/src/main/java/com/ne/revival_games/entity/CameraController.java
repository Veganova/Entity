package com.ne.revival_games.entity;

import android.graphics.Camera;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.view.MotionEvent;
import android.view.View;

import com.ne.revival_games.entity.MainThread;
import com.ne.revival_games.entity.WorldObjects.MyWorld;

import org.dyn4j.geometry.Vector2;

/**
 * Created by vishn on 7/8/2017.
 */

public class CameraController {
    public Vector2 zoomXY;
    public Vector2 translateXY;
    private Vector2 scales;
    private GamePanel panel;

    public CameraController(Vector2 relativeZoomXY, Vector2 relativeTranslationXY, GamePanel gamePanel) {
        zoomXY = new Vector2(relativeZoomXY.x, relativeZoomXY.y);
        //set to 1,1 default early
        this.scales = gamePanel.scales;
        translateXY = new Vector2(relativeTranslationXY.x / MyWorld.SCALE,
                relativeTranslationXY.y / MyWorld.SCALE);
        this.panel = gamePanel;
    }

    public void relativeZoom(double x, double y) {
        zoomXY.x *= x;
        zoomXY.y *= y;
    }

    public void setCenter(double x, double y) {
        zoomXY.x = x;
        zoomXY.y = y;
    }

    public void relativeMove(double x, double y) {

        translateXY.x -= (float) (x / MyWorld.SCALE);
        translateXY.y -= (float) (y / MyWorld.SCALE);
    }

    public void move(double x, double y) {
        x = x / MyWorld.SCALE;
        y = y / MyWorld.SCALE;
        translateXY.x = x;
        translateXY.y = y;

    }

    public void applyTransforms(Canvas canvas) {
        canvas.scale((float) zoomXY.x, (float) zoomXY.y);
        canvas.translate((float) translateXY.x, (float) translateXY.y);
    }



}
