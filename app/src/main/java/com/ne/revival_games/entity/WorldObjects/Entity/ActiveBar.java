package com.ne.revival_games.entity.WorldObjects.Entity;

import android.animation.ValueAnimator;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.view.ViewGroup;

import com.ne.revival_games.entity.GamePanel;
import com.ne.revival_games.entity.WorldObjects.Entity.SpecialEffects.Effect;
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
    private Paint paint, paint2, blur;
    private Effect effect;

    public enum PathType {
        FILLED_CIRCLE, CIRCLE, RECTANGLE, ROUNDED_RECTANGLE, LINE;
    }

    // default
    private PathType pathType = PathType.CIRCLE;

    private int lastHealth, targetHealth = 0;

    public ActiveBar(Entity entity, float strokeWidthFraction) {
        this.entity = entity;
        this.on = true;

        this.lastHealth = entity.health;
        this.targetHealth = entity.health;

        paint = new Paint();
        //smoothing
        paint.setAntiAlias(true);
        paint.setColor(ACTIVE_COLOR);
        paint.setStrokeWidth(0.16f * strokeWidthFraction);
        paint.setStyle(Paint.Style.STROKE);



        paint2 = new Paint();
        //smoothing
        paint2.setAntiAlias(true);
        paint2.setColor(INACTIVE_COLOR);
        paint2.setStrokeWidth(0.16f * strokeWidthFraction);
        paint2.setStyle(Paint.Style.STROKE);


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

//    ActiveBar(Entity entity, boolean state) {
//        this(entity, 1f);
//        if (!state) {
//            this.toggle();
//        }
//    }

    private double x, y;

    /**
     * Use this for lines and circles.
     *
     * @param type
     * @param x    radius, length, etc.
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

    private IntAnimator healthAnim;
    public void draw(Canvas c) {
        if (effect != null && effect.getStatus() != this.on) {
            this.toggle();
        }

        // Entity health is changed
        if (this.entity.health != targetHealth) {
            this.targetHealth = this.entity.health;
            // animation change! animate lastHealth from lastHealth to targetHealth
//            System.out.println("VALUE - " + 5.0f / (targetHealth - lastHealth));
            healthAnim = new IntAnimator(lastHealth, targetHealth, 20.0f / (lastHealth - targetHealth));
        } else {
            // will be null first time when health is full
            if (healthAnim != null) {
//                System.out.println("ANIMATION---");
                this.lastHealth = healthAnim.update();
            }
        }

        float cx = (float)this.entity.shape.getX();
        float cy = (float)this.entity.shape.getY();
        float angle = (float) this.entity.shape.body.getTransform().getRotation();
        float healthPercentage = (1.0f * lastHealth) / entity.MAX_HEALTH;
//        System.out.println("Health percentage: " + healthPercentage);

        Path path = new Path();
        switch(pathType) {
            case FILLED_CIRCLE:
                paint2.setStyle(Paint.Style.FILL);
                paint.setStyle(Paint.Style.FILL);
            case CIRCLE:
                float radius = (float)this.entity.shape.body.getFixture(0).getShape().getRadius() / 2;

                RectF rectangle = new RectF(cx - radius, cy - radius, cx + radius, cy + radius);

                float sweepAngle = (360 * healthPercentage);
                float startAngle = 0;//(float) (angle * 180 / Math.PI);//startingAngle - angle;
                path.addArc(rectangle, startAngle, sweepAngle);
                break;
            case RECTANGLE:
                // how to move only partway along a given path
                break;
            case LINE:
                double percentage = 0.75;
                double width = x  * healthPercentage * percentage;

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

        // bar will represent the cooldown as well
        if (this.effect != null) {
            this.drawPath(c, path, paint2, false);
            double frac = (effect.getMaxCooldown() - effect.getCooldown())/(1.0 * effect.getMaxCooldown());
            if (frac > 1) { // need to do this due to error in arithmetic when using type double
                frac = 1;
            }
            int alpha = (int)(255 * frac);
            paint.setColor(ACTIVE_COLOR);
//            System.out.println(alpha);
            paint.setAlpha(alpha);

            this.drawPath(c, path, paint, false);
        } else {
            this.drawPath(c, path, paint, false);
        }
    }

    private void drawPath(Canvas c, Path path, Paint paint, boolean blur) {
        if (blur && this.on) {
            c.drawPath(path, paint);

        }
        c.drawPath(path, paint);
//        c.drawPath(path, selected);
    }

    private boolean disable() {
        this.on = false;

//        paint.setColor((int)((INACTIVE_COLOR * 0.5) + (ACTIVE_COLOR * 0.5)));
        paint.setColor(INACTIVE_COLOR);
        return false;
    }

    private boolean enable() {
        this.on = true;
        paint.setColor(ACTIVE_COLOR);
        return true;
    }


    /**
     * Toggles the Active bar.
     *
     * @return  Returns whether this activebar is currently on or not (after the toggle)
     */
    public boolean toggle() {
        if (this.effect != null) {
            this.effect.toggle();
        }
        
        if (this.on) {
            return this.disable();
        } else {
            return this.enable();
        }
    }

    public void linkEffect(Effect e) {
        this.effect = e;
    }

    public void unlinkEffect() {
        this.effect = null;
    }

    // Does'nt work
    private RectF getRotatedRectangle(float x1, float y1, float x2, float y2, float angle, float cx, float cy) {

        float x1_new = (float) (Math.cos(angle) * (x1 - cx) - Math.sin(angle) * (y1 - cy)) + cx;
        float y1_new = (float) (Math.sin(angle) * (x1 - cx) + Math.cos(angle) * (y1 - cy)) + cy;

        float x2_new = (float) (Math.cos(angle) * (x2 - cx) - Math.sin(angle) * (y2 - cy)) + cx;
        float y2_new = (float) (Math.sin(angle) * (x2 - cx) + Math.cos(angle) * (y2 - cy)) + cy;

        return new RectF(x1_new, y1_new, x2_new, y2_new);
    }


}
