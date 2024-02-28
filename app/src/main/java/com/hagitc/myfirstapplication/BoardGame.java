package com.hagitc.myfirstapplication;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.icu.text.RelativeDateTimeFormatter;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

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
        // pass the xcontext for firebase listening - reomve when activity is finished
        presenter = new GameRoomPresenter(this,g,docReference,player,(Activity)context);

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
        int x = 0;
        int y = 0;
        int w = canvas.getWidth()/7;
        int h = canvas.getWidth()/6;

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
    }

    public boolean onTouchEvent(MotionEvent event)
    {
        if (event.getAction() == MotionEvent.ACTION_DOWN)
        {
            float x = event.getX();
            float y = event.getY();
            //     int touchedRow = (int) (y / (getHeight() / 6));
            int touchedColumn = (int) (x / (getWidth() / 7));
            presenter.userClick(touchedColumn);
            /*
            int row = g.userClick(touchedColumn);
            if (row != (-1))
            {
                if (g.getCurrentPlayer() == 1)
                {
                    squares[row][touchedColumn].placeCircle(Color.RED);
                    invalidate();//
                    g.setCounter(g.getCounter()+1);
                }
                else
                {
                    squares[row][touchedColumn].placeCircle(Color.YELLOW);
                    invalidate();
                    g.setCounter(g.getCounter()+1);
                }
            }
            if (g.getCounter() >= 4 && g.getCounter()<=42)
            {
                boolean result = g.checkForWin();
                if(result == true)
                {
                    int currentplayer1 = 0;
                    if (g.getCurrentPlayer() == 1)
                    {
                        currentplayer1 = 1;
                    }
                    else
                    {
                        currentplayer1 = 2;
                    }
                    Toast.makeText(this.context, "PLAYER" + currentplayer1 + " WON!" , Toast.LENGTH_LONG).show();




                    return true;
                }

            }
            if(g.isBoardFull()==true)
            {
                Toast.makeText(this.context, "IT IS A TEKO" , Toast.LENGTH_LONG).show();
                //print a message that the game is end in "teko".
                return true;
            }

        }

             */
            return true;

        }
        return true;
    }


    public void updateBoard(int row,int col,int color)
    {
        squares[row][col].placeCircle(color);
        invalidate();
    }

    public void displayMessage(String message)
    {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

}
