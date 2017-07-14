package com.ne.revival_games.entity.WorldObjects.Entity;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.annotation.NonNull;

import com.ne.revival_games.entity.WorldObjects.Shape.ObjCircle;

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
    private Paint paint;

    public ActiveBar(Entity entity) {
        this.entity = entity;
        this.on = true;

        paint = new Paint();
        //smoothing
        paint.setAntiAlias(true);
        paint.setColor(ACTIVE_COLOR);
        paint.setStrokeWidth(0.16f);
        paint.setStyle(Paint.Style.STROKE);
    }

    ActiveBar(Entity entity, boolean state) {
        this(entity);
        if (!state) {
            this.toggle();
        }
    }

    public void draw(Canvas c) {
        float cx = (float)this.entity.shape.getX();
        float cy = (float)this.entity.shape.getY();
        float radius = (float)this.entity.shape.body.getFixture(0).getShape().getRadius() / 2;
        RectF rectangle = new RectF(cx - radius, cy - radius, cx + radius, cy + radius);

        // opacity
        //p.setAlpha(0x80); //
        //canvas.drawOval(rectF, p);
        float sweepAngle = (360f * entity.health) / entity.MAX_HEALTH;
        c.drawArc (rectangle, startingAngle, sweepAngle, false, paint);
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
