package com.ne.revival_games.entity.WorldObjects.Shape;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;

import com.ne.revival_games.entity.GamePanel;
import com.ne.revival_games.entity.WorldObjects.MyWorld;

import org.dyn4j.collision.Fixture;
import org.dyn4j.dynamics.Body;
import org.dyn4j.geometry.Convex;
import org.dyn4j.geometry.MassType;
import org.dyn4j.geometry.Rectangle;
import org.dyn4j.geometry.Vector2;

import static android.R.attr.x;
import static android.R.attr.y;

/**
 * Represents the simple shape rectangle.
 */
public class ObjRectangle extends AShape {
    public Rectangle rect;

    public ObjRectangle(double x, double y, double w, double l, MyWorld world) {
        w = w/MyWorld.SCALE;
        l = l/MyWorld.SCALE;
        rect = new Rectangle(w, l);
        initValues(rect, x, y, world);
    }

    public ObjRectangle(double x, double y, double w, double l, double fixtureangle) {
        w = w/MyWorld.SCALE;
        l = l/MyWorld.SCALE;

        rect = new Rectangle(w, l);
        initValues(this.rect, x, y, fixtureangle);
    }

//    //this is a fixture right now
//    public ObjRectangle(double x, double y, int w, int l, Body newBody) {
//        rect = new Rectangle(w, l);
//        newBody.addFixture(rect);
//    }

    @Override
    public void draw(Canvas canvas) {
        double bottom = this.getY() - 0.5 * this.rect.getHeight() + this.rect.getCenter().y - this.body.getLocalCenter().y;
        double left =  this.getX() - 0.5 * this.rect.getWidth() + this.rect.getCenter().x - this.body.getLocalCenter().x;
        double right = this.getX() + 0.5 * this.rect.getWidth() + this.rect.getCenter().x - this.body.getLocalCenter().x;
        double top = this.getY() + 0.5 * this.rect.getHeight() + this.rect.getCenter().y - this.body.getLocalCenter().y;
//       this.angle = this.body.getTransform().getRotation() + this.rect.getRotation();
        RectF rectangle = new RectF((float) left, (float) top, (float) right, (float) bottom);

        this.angle = this.body.getTransform().getRotation();
        canvas.save();
        //ideal order would be rotate, translate
        ////getCenter is relative to orientation so I need to multiple by like sign or cosign of the angle

        canvas.rotate((float)Math.toDegrees(this.angle),
                (float)this.getX(), (float)this.getY());

        canvas.rotate((float)Math.toDegrees(this.rect.getRotation()),
                (float)(this.getX() + this.rect.getCenter().x), (float)(this.getY() + this.rect.getCenter().y));

        canvas.drawRect(rectangle, this.paint);

        canvas.restore();
    }

    @Override
    public double getOrientation(){
        return rect.getRotation();
    }

    public Convex getShape(){
        return this.rect;
    }
}

