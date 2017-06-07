package com.ne.revival_games.entity.WorldObjects.Shape;

import android.graphics.Canvas;

/**
 * Created by vishn on 6/7/2017.
 */

public class Triangle implements Shape {
    int x;
    int y;
    int x1, y1;
    int x2, y2;
    int x3, y3;
    //vector from center to point 1
    double [] vecOne = new double[2];
    //vector from center to point 2
    double [] vecTwo = new double[2];
    //vector from center to point 3
    double [] vecThree = new double[2];

    Triangle(int x, int y, int x1, int y1, int x2, int y2, int x3, int y3){
        this.x = x;
        this.y = y;
        this.x1 = x1;
        this.x2 = x2;
        this.x3 = x3;
        this.y1 = y1;
        this.y2 = y2;
        this.y3 = y3;
        vecOne[0] = this.x1-this.x;
        vecOne[1] = this.y1 - this.y;
        vecTwo[0] = this.x2 - this.x;
        vecTwo [1] = this.y2 - this.y;
        vecThree[0] = this.x3 - this.x;
        vecThree [1] = this.y3 - this.y;
        vecThree = equationSolver(vecOne, vecTwo, vecThree);

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
    void draw(Canvas canvas, int curX, int curY, int offsetX, int  offsetY);

    @Override
    public boolean collided(Shape other){
        return false;
    }

    @Override
    public boolean isWithin(double curX, double curY){
//        if(curX ){
//
//        }


        return false;
    }

    double[] equationSolver(double[] vec1Coeff, double[] vec2Coeff, double[] sum){
        double [] solutions = new double[2];
        solutions[0] = (vec2Coeff[0]*sum[1] - sum[0]*vec2Coeff[1])
                / (vec1Coeff[1]*vec2Coeff[0] - vec1Coeff[0]*vec2Coeff[1]);
        solutions[1] = (sum[0] - solutions[0]*vec1Coeff[0])/ vec2Coeff[0];


        return solutions;

    }

}
