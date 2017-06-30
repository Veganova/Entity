package com.ne.revival_games.entity.WorldObjects.Shape;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;

import com.ne.revival_games.entity.WorldObjects.MyWorld;

import org.dyn4j.geometry.Convex;
import org.dyn4j.geometry.Triangle;
import org.dyn4j.geometry.Vector2;

/**
 * Created by vishn on 6/27/2017.
 */

public class ObjTriangle extends AShape {
    public Triangle triangle;

    public ObjTriangle(double x, double y, double [] points, MyWorld world){
        for(int z=0; z<points.length; z++){
            points[z] = points[z]/MyWorld.SCALE;
        }

        triangle = new Triangle(new Vector2(points[0], points[1]),
                new Vector2(points[2], points[3]), new Vector2(points[4], points[5]));
        initValues(triangle, x, y, DEFAULT_DENSITY, world);
    }

    public ObjTriangle(double x, double y, double [] points, double density, MyWorld world){
        for(int z=0; z<points.length; z++){
            points[z] = points[z]/MyWorld.SCALE;
        }

        triangle = new Triangle(new Vector2(points[0], points[1]),
                new Vector2(points[2], points[3]), new Vector2(points[4], points[5]));
        initValues(triangle, x, y, density, world);
    }

    public ObjTriangle(double x, double y, double [] points, double fixtureangle){
        for(int z=0; z<points.length; z++){
            points[z] = points[z]/MyWorld.SCALE;
        }

        triangle = new Triangle(new Vector2(points[0], points[1]),
                new Vector2(points[2], points[3]), new Vector2(points[4], points[5]));

        initValues(this.triangle, x, y, fixtureangle);
    }

    @Override
    public void draw(Canvas canvas){
        Vector2 point1 = triangle.getVertices()[0];
        Vector2 point2 = triangle.getVertices()[1];
        Vector2 point3 = triangle.getVertices()[2];

        Paint paint = new Paint();

        paint.setStrokeWidth((float) 0.5);
        paint.setColor(android.graphics.Color.RED);

        canvas.save();

        Path path = new Path();

        //not sure if it's addition or subtraction at this point q.q
        path.moveTo((float) point1.x + (float) getX()  - (float) this.body.getLocalCenter().x,
                (float) point1.y + (float) getY() - (float) this.body.getLocalCenter().y);
        path.lineTo((float) point2.x + (float) getX() - (float) this.body.getLocalCenter().x,
                (float) point2.y  + (float) getY()  - (float) this.body.getLocalCenter().y);
        path.lineTo((float) point3.x + (float) getX()  - (float) this.body.getLocalCenter().x,
                (float) point3.y  + (float) getY() - (float) this.body.getLocalCenter().y);
        path.lineTo((float) point1.x + (float) getX() - (float) this.body.getLocalCenter().x,
                (float) point1.y  + (float) getY()  - (float) this.body.getLocalCenter().y);
        path.close();

        this.angle = this.body.getTransform().getRotation();

        canvas.rotate((float)Math.toDegrees(this.angle),
                (float)this.getX(), (float)this.getY());
        canvas.drawPath(path, this.paint);
        canvas.restore();

    }

    @Override
    public double getOrientation() {
        return 0;
    }

    public Convex getShape(){
        return this.triangle;
    }

}
