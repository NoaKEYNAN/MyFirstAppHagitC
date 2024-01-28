package com.hagitc.myfirstapplication;

import android.graphics.Color;
import android.view.MotionEvent;
import android.widget.Toast;

public class GamePresenter
{
    //This class is responsible for the logic of this game.
    //It combines between both of the other classes.

    private BoardGame boardGame;
    private GameLogic gameLogic;

    public GamePresenter(BoardGame boardGame, GameLogic gameLogic)
    {
        this.boardGame = boardGame;
        this.gameLogic = gameLogic;
    }

    public void userClick(int column)
    {
        int row = gameLogic.userClick(column);
        boolean flag = false;//it will be change after a legal action
        if (row != (-1)) //אם זה מהלך חוקי
        {
            if (gameLogic.getCurrentPlayer() == 1)
            {
                boardGame.updateBoard(row, column, Color.RED);
                gameLogic.setCounter(gameLogic.getCounter() + 1);
            }
            else
            {
                boardGame.updateBoard(row, column, Color.YELLOW);
                gameLogic.setCounter(gameLogic.getCounter() + 1);

            }
            flag = true;
        }
        else
        {
            //if it is an illegal action.
            if (gameLogic.isBoardFull() == false)
            {
                boardGame.displayMessage("TRY AGAIN");
                gameLogic.switchPlayer();
            }
        }
        if (gameLogic.getCounter() >= 8 && gameLogic.getCounter() <= 42 && flag == true)
        //בודקת אחרי המהלך במידה והוא היה חוקי, אם יש ניצחון או שהלוח מלא וזה תיקו
        {
            boolean result = gameLogic.checkForWin();
            if (result == true)
            {
                int currentplayer1 = 0;
                if (gameLogic.getCurrentPlayer() == 1)
                {
                    currentplayer1 = 1;
                }
                else
                {
                    currentplayer1 = 2;
                }
                boardGame.displayMessage("PLAYER" + currentplayer1 + " WON!");

                //TO ADD A BUTTON THAT RESTART THE GAME
                if (gameLogic.isBoardFull() == true)
                {
                    boardGame.displayMessage("THE GAME IS END");
                    //אם אחרי הניצחון הלוח מלא אז המשחק נגמר
                    //צריך להוסיף כפתור שמסיים את המשחק
                }

            }
            if(gameLogic.isBoardFull()==true)
            {
                boardGame.displayMessage("IT IS A TIE AND THE GAME IS END");
                //TO ADD A RESTART BUTTON
            }
        }
    }


}