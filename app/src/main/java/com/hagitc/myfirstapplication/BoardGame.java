package com.hagitc.myfirstapplication;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.icu.text.RelativeDateTimeFormatter;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

public class BoardGame extends View
{
    Square [][] squares;
    Context context;
    Paint misgeret;
    Paint fill;
    GameLogic g = new GameLogic();

    MyCircle circle1;
    MyCircle circle2;

    GamePresenter presenter;



    public BoardGame(Context context)
    {
        super(context);
        this.context = context;
        squares = new Square [6][7];
        misgeret = new Paint();
        misgeret.setStyle(Paint.Style.STROKE);
        misgeret.setColor(Color.BLACK);
        misgeret.setStrokeWidth(10);
        fill = new Paint();
        fill.setColor(Color.WHITE);
        presenter = new GamePresenter(this,g);
    }
    // two phones constructor
    public BoardGame(Context context,String docReference,String player)
    {
        super(context);
        this.context = context;
        squares = new Square [6][7];
        misgeret = new Paint();
        misgeret.setStyle(Paint.Style.STROKE);
        misgeret.setColor(Color.BLACK);
        misgeret.setStrokeWidth(10);
        fill = new Paint();
        fill.setColor(Color.WHITE);
        // pass the context for firebase listening - reomve when activity is finished
        presenter = new GameRoomPresenter(this,g,docReference,player,(Activity)context);
        //הפרמטר  player מכיל את הזהות של השחקן הנוכחי שהתחבר לחדר המשחק.
        //הפרמטר (Activity)context מכיל את הפעילות (Activity) המכילה את הלוח. זה נחוץ לצורך סגירת הפעילות אם המשחק נגמר.
        //הפרמטר  docReference מכיל את הפנייה למסמך במסד הנתונים של Firebase שבו מאוחסן מידע על המשחק.
    }
    public GamePresenter getPresenter()
    {
        return this.presenter;
    }



    public GameLogic getGameLogic()
    {
        return g;
    }
    public void SetFill(int color)
    {
        fill.setColor(color);
    }

    protected void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);
        drawBoard(canvas);//מצייר לוח ריק
    }

    public void drawBoard(Canvas canvas)
    {
        //הפעולה מציירת את הלוח על המסך. תתי הפעולות שבתוכה מבצעות את הצעדים הבאים:
        //מגדירה את המשתנים x ו-y בכדי להתחיל לצייר את הריבועים במיקומם הראשוניים (0, 0) .
        int x = 0;
        int y = 0;
        int w = canvas.getWidth()/7;
        int h = canvas.getWidth()/6;
        //מחשבת את הרוחב והגובה של כל ריבוע על פי רוחב המסך, כך שיהיה 7 ריבועים ברוחב ו-6 בגובה.

        for(int i=0; i<squares.length; i++)
        {
            for(int j=0;j<squares[0].length; j++)
            {
                misgeret = new Paint();
                misgeret.setStyle(Paint.Style.STROKE);
                misgeret.setColor(Color.BLACK);
                misgeret.setStrokeWidth(10);

                // first time only create new square
                // after - we use the created ones, no need for new ones
                if(squares[i][j]==null)
                    squares[i][j] = new Square(this,x,y,w,h,  misgeret);
                squares[i][j].draw(canvas);
                x = x + w;
            }
            y = y + h;
            x = 0;
        }
        //מבצעת לולאה כפולה על כל הריבועים בלוח:
        //בתוך הלולאה החיצונית, מגדירה את הצבע, העובי והסגנון של קווי המסגרת.
        //בודקת אם הריבוע בתא המסוים של המטריצה כבר קיים (המשתנה (squares[i][j]) ואם לא, יוצרת ריבוע חדש ומציירת אותו על המסך.
        //מציירת את הריבוע על המסך בעזרת הפעולה squares[i][j].draw(canvas).
        //מעדכנת את ערך ה-x על מנת שהריבוע הבא יתווסף לימינו שלו.
        //לאחר סיום לולאת המקורה, מעדכנת את ערך ה-y כדי להתחיל לצייר את השורה הבאה בלוח, ומאפסת את ערך ה-x כדי להתחיל את הציור מהשורה הראשונה בכל שורה חדשה.
    }

    public boolean onTouchEvent(MotionEvent event)
    {
        if (event.getAction() == MotionEvent.ACTION_DOWN)
        {
            float x = event.getX();
            float y = event.getY();
            int touchedColumn = (int) (x / (getWidth() / 7));
            //מחשבת את העמודה (touchedColumn) בה נלחץ המגע על פי רוחב המסך ומספר העמודות בלוח המשחק.
            // זה נעשה על ידי חלוקת רוחב המסך על פי מספר העמודות בלוח.
            presenter.userClick(touchedColumn);
            //קוראת לפעולה userClick() של המציג  (presenter)  שמטפלת בלחיצת המשתמש ומעבירה לה את מיקום העמודה שנלחצה.

            return true;
            //הפעולה מחזירה true על מנת לציין שהיא טיפלה באירוע המגע.
        }
        return true;
    }


    public void updateBoard(int row,int col,int color)
    {
        squares[row][col].placeCircle(color);
        invalidate();
        //הפעולה קוראת ל־invalidate()  על מנת לגרום למבנה המשחק (במקרה זה, מחלקת BoardGam )
        // לצייר את עצמו מחדש עם השינויים החדשים. זה מבטיח שהלוח יעודכן גם על המסך.
    }

    public void displayMessage(String message)
    {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    public void showGameOver(String message)
    {
        // Create the object of AlertDialog Builder class
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        // Set the message show for the Alert time
        builder.setMessage("Click to return to menu");

        // Set Alert Title
        builder.setTitle(message);

        // Set Cancelable false for when the user clicks on the outside the Dialog Box then it will remain show
        builder.setCancelable(false);

        // Set the positive button with yes name Lambda OnClickListener method is use of DialogInterface interface.
        builder.setPositiveButton("EXIT", (DialogInterface.OnClickListener) (dialog, which) -> {
            // When the user click yes button then app will close

            if(context instanceof GameActivity)
            {

                ((GameActivity)context).finish();

            }
            else  if( context instanceof GameRoomActivity)
                ((GameRoomActivity)context).finish();


        });


        // Create the Alert dialog
        AlertDialog alertDialog = builder.create();
        // Show the Alert Dialog box
        alertDialog.show();

    }

}
