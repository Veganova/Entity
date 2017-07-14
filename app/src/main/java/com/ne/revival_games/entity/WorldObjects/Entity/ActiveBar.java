package com.ne.revival_games.entity.WorldObjects.Entity;

import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;

/**
 * Class that represents the health bar and the status of the entity that it is tied to.
 * Status refers to whether its ability has been turned on or not. Class does not do much in terms
 * of functionality, it only serves as a visual aid for the status of the entity that it is tied to.
 */
public class ActiveBar {
    private int ACTIVE_COLOR = Color.WHITE;
    private int INACTIVE_COLOR = Color.GRAY;

    private float startingAngle = 225f;
    private Entity entity;
    private boolean on;
    private Paint paint, selected, blur;

    public enum PathType {
        CIRCLE, RECTANGLE, ROUNDED_RECTANGLE;
    }
    private PathType pathType = PathType.CIRCLE;

    public ActiveBar(Entity entity) {
        this.entity = entity;
        this.on = true;

        paint = new Paint();
        //smoothing
        paint.setAntiAlias(true);
        paint.setColor(ACTIVE_COLOR);
        paint.setStrokeWidth(0.16f);
        paint.setStyle(Paint.Style.STROKE);

        blur = new Paint();
        blur.set(paint);
        blur.setStrokeJoin(Paint.Join.ROUND);
        blur.setStrokeCap(Paint.Cap.ROUND);
        blur.setColor(Color.WHITE);
        blur.setStrokeWidth(0.45f);
        blur.setMaskFilter(new BlurMaskFilter(1.4f, BlurMaskFilter.Blur.INNER));

        selected = new Paint();
        selected.set(paint);
        selected.setColor(Color.BLACK);
        selected.setStrokeWidth(0.16f);
        selected.setMaskFilter(new BlurMaskFilter(0.8f, BlurMaskFilter.Blur.NORMAL));
    }

    ActiveBar(Entity entity, boolean state) {
        this(entity);
        if (!state) {
            this.toggle();
        }
    }

    public void setPathType(PathType type) {
        this.pathType = type;
    }

    public void draw(Canvas c) {
        float cx = (float)this.entity.shape.getX();
        float cy = (float)this.entity.shape.getY();
        float radius = (float)this.entity.shape.body.getFixture(0).getShape().getRadius() / 2;
        RectF rectangle = new RectF(cx - radius, cy - radius, cx + radius, cy + radius);

        // opacity
        //p.setAlpha(0x80);
        // if want to draw red for health missing
        //canvas.drawOval(rectF, redpaint);

        // calculated percentage health remaining
        float sweepAngle = (360f * entity.health) / entity.MAX_HEALTH;

        Path path = new Path();

        switch(pathType) {
            case CIRCLE:
                path.addArc(rectangle, startingAngle, sweepAngle);
                break;
            case RECTANGLE:
                // how to move only partway along a given path
                break;
        }

        c.drawPath(path, blur);
        c.drawPath(path, paint);

        // c.drawArc (rectangle, startingAngle, sweepAngle, false, blur);
       // c.drawArc (rectangle, startingAngle, sweepAngle, false, paint);

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
