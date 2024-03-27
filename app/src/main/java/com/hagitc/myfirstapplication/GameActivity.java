package com.hagitc.myfirstapplication;

import static com.hagitc.myfirstapplication.AppConstants.GAME_CONFIG;
import static com.hagitc.myfirstapplication.AppConstants.ONE_PHONE;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;

public class GameActivity extends AppCompatActivity {
    BoardGame boardGame;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//THIS ACTIVITY IS ONLY FOR A GAME FROM ONE PHONE.
        int gameConfig = getIntent().getIntExtra(GAME_CONFIG,ONE_PHONE);
        // this means single phone
        boardGame = new BoardGame(this);
        // else this means two phones, we need to get
        // doc reference and owner,
        // these should be passed to presenter,
        // which is created in board game
        setContentView(boardGame);







        //
    }
}