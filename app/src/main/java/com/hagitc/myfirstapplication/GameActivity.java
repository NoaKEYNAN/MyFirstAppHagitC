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

        String gameConfig = getIntent().getStringExtra(GAME_CONFIG);
        // this means single phone
        if(gameConfig.equals(ONE_PHONE))
             boardGame = new BoardGame(this);
        // else this means two phones, we need to get
        // doc reference and owner,
        // these should be passed to presenter,
        // which is created in board game
        else {
            //צריך להפעיל פה בנאי אחר שמתאים למשחק עם שני טלפונים
            //boardGame = new BoardGame(this,);
            //Context context,String docReference,String player
        }
        setContentView(boardGame);






        //
    }
}