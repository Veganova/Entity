package com.ne.revival_games.entity.WorldObjects.Shape;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

import com.ne.revival_games.entity.GamePanel;

import org.dyn4j.dynamics.Body;
import org.dyn4j.geometry.MassType;
import org.dyn4j.geometry.Rectangle;

/**
 * Represents the simple shape rectangle.
 */
public class ObjRectangle extends AShape {
    private double l;
    private double w;

    public ObjRectangle(int x, int y, int l, int w) {
        super(new Rectangle(l, w), x, y);
        this.l = l;
        this.w = w;
    }

    @Override
    public void draw(Canvas canvas) {
        //left, top, right, bottom
        double bottom = translateYtoCanvas(this.getY() - 0.5 * this.l, GamePanel.HEIGHT);
        double left = translateXtoCanvas(this.getX() - 0.5 * this.w, GamePanel.WIDTH);
        double right = translateXtoCanvas(this.getX() + 0.5 * this.w, GamePanel.WIDTH);
        double top = translateYtoCanvas(this.getY() + 0.5 * this.l, GamePanel.HEIGHT);

        Rect rectangle = new Rect((int) left, (int) top, (int) right, (int) bottom);
        canvas.drawRect(rectangle, new Paint());
    }
}
