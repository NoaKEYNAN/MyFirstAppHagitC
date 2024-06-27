package com.hagitc.myfirstapplication;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class Square
{
    //המחלקה Square מייצגת ריבוע בלוח המשחק, והיא מתמקדת בפעולות הקשורות לניהול וייצוג הצורה של הריבוע בלוח המשחק.
    //מטרת המחלקה היא ליצור אובייקטים שמייצגים ריבועים בלוח המשחק,
    // עם היכולת להציגם על המסך ולבצע שינויים כגון הצבת עיגול בתוך הריבוע ושינוי צבעים.
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
        //הפעולה מציירה את הריבוע ובמידה והוא נתפס, מציירה עיגול בתוך הריבוע בצבע המתאים.
        canvas.drawRect(x,y, x+w, y+h, p);
        //   שורה זו מציירת ריבוע בקנבס.
        //   הפונקציה drawRect מקבלת את הקורדינטות של הפינה השמאלית העליונה של הריבוע (x, y),
        //   את הקורדינטות של הפינה הימנית התחתונה של הריבוע (x+w, y+h)
        //   ואת האובייקט  Paint(במקרה הזה p) שמגדיר את סגנון הציור (צבע, עובי הקו וכו').

        if(isOccupied) {
            Paint paint = new Paint();
            // יוצרים אובייקט חדש מסוג Paintשנקרא paint.
            // זהו אובייקט שמגדיר את מאפייני הציור עבור העיגול.
            paint.setColor(circleColor);
            canvas.drawCircle(x+w/2,y+h/2,w/2,paint);
        }
    }




}



