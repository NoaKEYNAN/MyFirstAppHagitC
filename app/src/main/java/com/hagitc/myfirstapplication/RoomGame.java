package com.hagitc.myfirstapplication;

public class RoomGame
{
    //מטרת המחלקה היא לאפשר ניהול ושליטה בפרטי המשחק בחדר,
    // כולל שמירה על מי הוא השחקן הנוכחי,
    // באיזה סטטוס נמצא המשחק וכיצד לבצע שינויים במשחק על פי הפעולות הנדרשות.
    private String status;//התחלתי משחק,הצטרפתי...
    private String namePlayer1;
    private String namePlayer2;
    private String currentPlayer;
    private int touchedColumn;



    public RoomGame() {
    }

    public RoomGame(String status, String namePlayer1, String namePlayer2, String currentPlayer, int touchedColumn)
    {
        this.status = status;
        this.namePlayer1 = namePlayer1;
        this.namePlayer2 = namePlayer2;
        this.currentPlayer= currentPlayer;
        this.touchedColumn = touchedColumn;

    }


    public String getStatus() {
        //"CREATED" or "JOINED"
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getNamePlayer1() {
        return namePlayer1;
    }

    public void setNamePlayer1(String namePlayer1) {
        this.namePlayer1 = namePlayer1;
    }

    public String getNamePlayer2() {
        return namePlayer2;
    }

    public void setNamePlayer2(String namePlayer2) {
        this.namePlayer2 = namePlayer2;
    }

    public String getCurrentPlayer() {
        return currentPlayer;
    }

    public void setCurrentPlayer(String currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    public int getTouchedColumn() {
        return touchedColumn;
    }

    public void setTouchedColumn(int touchedColumn) {
        this.touchedColumn = touchedColumn;
    }


    public void switchPlayer()
    {
        if(this.currentPlayer.equals(AppConstants.HOST))
            this.currentPlayer = AppConstants.OTHER;
        else
            this.currentPlayer = AppConstants.HOST;
    }
}
