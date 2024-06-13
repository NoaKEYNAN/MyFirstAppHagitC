package com.hagitc.myfirstapplication;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class Square
{
    BoardGame boardGame;
    float x, y, w, h;
    private Paint p;
    private boolean isOccupied=false;
    private int circleColor=Color.WHITE;
    public Square (BoardGame boardGame, float x, float y, float w, float h,Paint p)
    {
        this.boardGame = boardGame;
        this.x = x;
        this.y = y;
        this.boardGame = boardGame;
      //  int color = Color.WHITE;
        //if(this.p!=null)
          //  color = this.p.getColor();
        this.p = p;
     //   this.p.setColor(color);
        this.w = w;
        this.h = h;
    }

    public void placeCircle(int circleColor)
    {
        isOccupied=true; //סימון הריבוע כנתפס
        this.circleColor =circleColor;
    }

    public void changeColor(int c)
    {
        p.setColor(c);

    }


    public void draw (Canvas canvas)
    {
        canvas.drawRect(x,y, x+w, y+h, p);

        if(isOccupied) {
            Paint paint = new Paint();
            paint.setColor(circleColor);
            canvas.drawCircle(x+w/2,y+h/2,w/2,paint);
        }
    }




}



