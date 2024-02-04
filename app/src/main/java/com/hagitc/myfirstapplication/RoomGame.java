package com.hagitc.myfirstapplication;

public class RoomGame
{
    private String status;//התחלתי משחק,הצטרפתי...
    private String namePlayer1;
    private String namePlayer2;
    private int currentPlayer;
    private int touchedColumn;


    public RoomGame() {
    }

    public RoomGame(String status, String namePlayer1, String namePlayer2, int currentPlayer, int touchedColumn)
    {
        this.status = status;
        this.namePlayer1 = namePlayer1;
        this.namePlayer2 = namePlayer2;
        this.currentPlayer= currentPlayer;
        this.touchedColumn = touchedColumn;

    }


    public String getStatus() {
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

    public int getCurrentPlayer() {
        return currentPlayer;
    }

    public void setCurrentPlayer(int currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    public int getTouchedColumn() {
        return touchedColumn;
    }

    public void setTouchedColumn(int touchedColumn) {
        this.touchedColumn = touchedColumn;
    }
}
