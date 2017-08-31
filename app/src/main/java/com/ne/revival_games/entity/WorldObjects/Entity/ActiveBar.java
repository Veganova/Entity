package com.ne.revival_games.entity.WorldObjects.Entity;

import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;

import com.ne.revival_games.entity.GamePanel;
import com.ne.revival_games.entity.WorldObjects.MyWorld;
import com.ne.revival_games.entity.WorldObjects.Shape.ObjRectangle;

/**
 * Class that represents the health bar and the status of the entity that it is tied to.
 * Status refers to whether its ability has been turned on or not. Class does not do much in terms
 * of functionality, it only serves as a visual aid for the status of the entity that it is tied to.
 */
public class ActiveBar {
//    private int ACTIVE_COLOR = Color.WHITE;
//    private int INACTIVE_COLOR = Color.GRAY;
    private int ACTIVE_COLOR = GamePanel.cream;
    private int INACTIVE_COLOR = GamePanel.background_dark;

    private float startingAngle = 225f;
    private Entity entity;
    private boolean on;
    private Paint paint, blur;

    public enum PathType {
        CIRCLE, RECTANGLE, ROUNDED_RECTANGLE, LINE;
    }

    // default
    private PathType pathType = PathType.CIRCLE;

    public ActiveBar(Entity entity, float strokeWidthFraction) {
        this.entity = entity;
        this.on = true;

        paint = new Paint();
        //smoothing
        paint.setAntiAlias(true);
        paint.setColor(ACTIVE_COLOR);
        paint.setStrokeWidth(0.16f * strokeWidthFraction);
        paint.setStyle(Paint.Style.STROKE);

        blur = new Paint();
        blur.set(paint);
        blur.setStrokeJoin(Paint.Join.ROUND);
        blur.setStrokeCap(Paint.Cap.ROUND);
        blur.setColor(Color.WHITE);
        blur.setStrokeWidth(0.45f * strokeWidthFraction);
        blur.setMaskFilter(new BlurMaskFilter(1.4f, BlurMaskFilter.Blur.INNER));

//        selected = new Paint();
//        selected.set(paint);
//        selected.setColor(Color.WHITE);
//        selected.setStrokeWidth(0.16f);
//        selected.setMaskFilter(new BlurMaskFilter(0.8f, BlurMaskFilter.Blur.NORMAL));
    }

    ActiveBar(Entity entity, boolean state) {
        this(entity, 1f);
        if (!state) {
            this.toggle();
        }
    }

    private double x, y;

    /**
     * Use this for lines and circles.
     *
     * @param type
     * @param x
     */
    public void setPathType(PathType type, double x) {
        this.pathType = type;
        this.x = x / MyWorld.SCALE;
    }

    /**
     * Use this for rectangles and other such shapes that require two inputs
     *
     * @param type
     * @param x
     * @param y
     */
    public void setPathType(PathType type, double x, double y) {
        this.pathType = type;
        this.x = x / MyWorld.SCALE;
        this.y = y / MyWorld.SCALE;
    }

    public void draw(Canvas c) {
        float cx = (float)this.entity.shape.getX();
        float cy = (float)this.entity.shape.getY();
        float angle = (float) this.entity.shape.body.getTransform().getRotation();
        float healthPercentage = (1.0f * entity.health) / entity.MAX_HEALTH;

        Path path = new Path();
        switch(pathType) {
            case CIRCLE:
                float radius = (float)this.entity.shape.body.getFixture(0).getShape().getRadius() / 2;
                RectF rectangle = new RectF(cx - radius, cy - radius, cx + radius, cy + radius);
                float sweepAngle = (360 * healthPercentage);

                path.addArc(rectangle, startingAngle - angle, sweepAngle);
                break;
            case RECTANGLE:
                // how to move only partway along a given path
                break;
            case LINE:
                double percentage = 0.75;
                double width = x * percentage * healthPercentage;

                // System.out.println(height + " " + width);
                float p1x, p1y, p2x, p2y;
                p1x = (float)(cx + Math.cos(angle) * width / 2);
                p1y = (float)(cy + Math.sin(angle) * width / 2);

                p2x = (float)(cx - Math.cos(angle) * width / 2);
                p2y = (float)(cy - Math.sin(angle) * width / 2);
                path.moveTo(p1x, p1y);
                path.lineTo(p2x, p2y);
                break;
        }

        if (this.on) {
            c.drawPath(path, blur);
        }
        c.drawPath(path, paint);
        // c.drawPath(path, selected);
    }

    /**
     * Toggles the Active bar.
     *
     * @return  Returns whether this activebar is currently on or not (after the toggle)
     */
    public boolean toggle() {
        if (this.on) {
            this.on = false;
            paint.setColor(INACTIVE_COLOR);
            return false;
        } else {
            this.on = true;
            paint.setColor(ACTIVE_COLOR);
            return true;
        }
    }



}
