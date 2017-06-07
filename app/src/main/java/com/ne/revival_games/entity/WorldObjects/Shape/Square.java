package com.ne.revival_games.entity.WorldObjects.Shape;

        import android.graphics.Canvas;

/**
 * Represents the simple shape square.
 */
public class Square implements Shape {

    int x;
    int y;
    int side;

    /**
     * Constructor for the square object.
     *
     * @param x The x of the center of the shape
     * @param y The x of the center of the shape
     * @param side  The side length
     */
    Square (int x, int y, int side) {
        this.x = x;
        this.y = y;
        this.side = side;
    }

    @Override
    public int getX(){
        return this.x;
    }

    @Override
    public int getY(){
        return this.y;
    }

    @Override
    public void draw(Canvas canvas, int curX, int curY, int offsetX, int offsetY) {
        // TODO: 6/7/2017
    }

    @Override
    public boolean collided(Shape other){
        double vectX = other.getX() - this.x;
        double vectY = other.getY() - this.y;
        double scale = Math.pow(Math.pow(vectX,2) + Math.pow(vectY,2), 0.5);
        vectX = vectX/scale;
        vectY = vectY/scale;
        return isWithin(this.x + vectX*Math.abs(vectX)
                , this.y + vectY*Math.abs(vectX));
    }

    @Override
    public boolean isWithin(double curX, double curY){
        return (this.x+0.5*this.side >= curX) && (this.x - 0.5*this.side <=curX )
                && (this.y + 0.5*this.side >= curY) && (this.y - 0.5*this.side <=curY);
    }
}
