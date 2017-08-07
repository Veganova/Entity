package com.ne.revival_games.entity.WorldObjects;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;

import com.ne.revival_games.entity.WorldObjects.Entity.Entity;

import org.dyn4j.collision.AbstractBounds;
import org.dyn4j.collision.Bounds;
import org.dyn4j.collision.BoundsAdapter;
import org.dyn4j.collision.Collidable;
import org.dyn4j.collision.Fixture;
import org.dyn4j.dynamics.Body;
import org.dyn4j.geometry.Vector2;

/**
 * Created by Veganova on 8/2/2017.
 */

public class Boundary {

    private final double side;
    private final double minX;
    private final double minY;
    private final double maxX;
    private final double maxY;
    private MyWorld world;

    public Boundary(double s, MyWorld world) {
        this.world = world;
        this.side = s / world.SCALE;

        this.minX = -side / 2;
        this.minY = -side / 2;
        this.maxX = side / 2;
        this.maxY = side / 2;
    }

    public void checkOutside(Entity entity) {
        Vector2 center = entity.shape.body.getWorldCenter();
        if((Math.abs(center.x) > side) || (Math.abs(center.y) > side)) {
            if (entity.ghost) {
                entity.setColor(Color.YELLOW, world);
            } else {
                world.objectDatabase.remove(entity.shape.body);
            }
        }
    }

    public void draw(Canvas canvas) {
        RectF rect = new RectF((float)(2 * this.minX), (float)(2 * this.minY),
                (float)(2 * this.maxX), (float)(2 * this.maxY));

        Paint paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.WHITE);
        canvas.drawRect(rect, paint);
    }
}
