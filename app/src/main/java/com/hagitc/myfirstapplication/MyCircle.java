package com.hagitc.myfirstapplication;

import android.graphics.Color;
import android.graphics.Paint;

public class MyCircle {
    //המחלקה  MyCircle מייצגת עיגול עם מאפיינים של מיקום x,y, רדיוס וצבע.
    // היא כוללת פונקציות גישה לשינוי וקבלת המאפיינים הללו,
    // וכן בנאי שמאפשר יצירה קלה של עיגול חדש עם הערכים הרצויים.
    private float x;
    private float y;
    private float r;

    private Paint paint;
    public MyCircle(float x, float y, float r,int color)
    {
        this.x = x;
        this.y = y;
        this.r = r;
        paint = new Paint();
        paint.setColor(color);


    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getR() {
        return r;
    }

    public void setR(float r) {
        this.r = r;
    }

    public Paint getPaint() {
        return paint;
    }

    public void setPaint(Paint paint)
    {
        this.paint = paint;
    }

}
