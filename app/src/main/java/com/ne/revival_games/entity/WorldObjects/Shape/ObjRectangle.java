package com.ne.revival_games.entity.WorldObjects.Shape;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;

import com.ne.revival_games.entity.GamePanel;
import com.ne.revival_games.entity.WorldObjects.MyWorld;

import org.dyn4j.collision.Fixture;
import org.dyn4j.dynamics.Body;
import org.dyn4j.geometry.MassType;
import org.dyn4j.geometry.Rectangle;

/**
 * Represents the simple shape rectangle.
 */
public class ObjRectangle extends AShape {
    private Rectangle rect;

    public ObjRectangle(double x, double y, int w, int l, MyWorld world) {
        rect = new Rectangle(w, l);
        initValues(rect, x, y, world);
    }


    @Override
    public void draw(Canvas canvas) {
        //left, top, right, bottom
        double bottom = this.getY() - 0.5 * this.rect.getHeight();
        double left = this.getX() - 0.5 * this.rect.getWidth();
        double right = this.getX() + 0.5 * this.rect.getWidth();
        double top = this.getY() + 0.5 * this.rect.getHeight();

       // RectF rectangle = new RectF((int) left, (int) top, (int) right, (int) bottom);
        Rect rectangle = new Rect((int) left, (int) top, (int) right, (int) bottom);
        canvas.save();
        canvas.rotate((float)Math.toDegrees(this.rect.getRotation()),
                (float)this.body.getWorldCenter().x, (float)this.body.getWorldCenter().y);

        canvas.drawRect(rectangle, new Paint());
        canvas.restore();

        //canvas.drawRoundRect(rectangle, 0, 0, new Paint());
    }

    /**
     * In degrees (not radians)
     *
     * @param degrees above
     */
    public void rotate(double degrees) {
        this.angle += degrees;
        double theta = Math.PI / 180.0 * degrees;
        //this.rect.rotate(theta);
        this.rect.rotate(theta, this.getX(), this.getY());
        //this.body.setTransform();
    }
}

