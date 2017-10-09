package com.ne.revival_games.entity;

import android.graphics.Camera;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.view.MotionEvent;
import android.view.View;

import com.ne.revival_games.entity.MainThread;
import com.ne.revival_games.entity.WorldObjects.Entity.Util;
import com.ne.revival_games.entity.WorldObjects.MyWorld;

import org.dyn4j.geometry.Vector2;

/**
 * Created by vishn on 7/8/2017.
 */

public class CameraController {
    public Vector2 zoomXY;
    public Vector2 translateXY;
    private GamePanel panel;
    private Vector2 translateUpdate = new Vector2(0, 0);
    private double zoom_max = 4;
    private double zoom_min = 0.5;

    public CameraController(Vector2 relativeZoomXY, Vector2 relativeTranslationXY, GamePanel gamePanel) {
        zoomXY = new Vector2(relativeZoomXY.x, relativeZoomXY.y);
        //set to 1,1 default early
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
        x = x / MyWorld.SCALE;
        y = y / MyWorld.SCALE;
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
        zoomXY.x = Math.max(zoom_min, Math.min(zoom_max, zoomXY.x));
        zoomXY.y = Math.max(zoom_min, Math.min(zoom_max, zoomXY.y));

        canvas.scale((float) zoomXY.x, (float) zoomXY.y);
        canvas.translate((float) translateXY.x, (float) translateXY.y);
    }

    public boolean nearEdge(double x, double y, double delta) {
        return (Util.nearValue(0D, x, delta) || Util.nearValue(0D, y, delta)
                || Util.nearValue(panel.getHeight(), y, delta) || Util.nearValue(panel.getWidth(), x, delta));
    }

    public void update() {
        this.relativeMove(translateUpdate.x, translateUpdate.y);
    }


    public boolean isZoomMaxorMin(){
        return zoomXY.x == zoom_min || zoomXY.x >= zoom_max - 0.2;
    }

}
