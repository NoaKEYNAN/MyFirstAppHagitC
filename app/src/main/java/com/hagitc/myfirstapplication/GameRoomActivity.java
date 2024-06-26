package com.hagitc.myfirstapplication;

import static com.hagitc.myfirstapplication.AppConstants.DEBUG_GAME_ID;
import static com.hagitc.myfirstapplication.AppConstants.HOST;
import static com.hagitc.myfirstapplication.AppConstants.OTHER;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class GameRoomActivity extends AppCompatActivity {
    //ברגע שהמשתמש ילחץ על הכפתור CREATE GAME תפתח לו אפשרות לשתף את קוד המשחק עם משתמש אחר
    // ולאחר מכן הוא יועבר לActivity הזה.
    // לאחר שהמשתמש השני יקבל את הקוד למשחק הוא ילחץ על הכפתור JOIN GAME ובתיבת טקסט שתיפתח לו הוא יכניס את קוד המשחק שנשלח אליו.
    // במידה והקוד תקין הוא יכנס על ידי לחיצת כפתור נוספת לחדר המשחק בו נמצא כבר המשתמש שיצר את המשחק.
    // ברגע בו שני השחקנים ינכחו ביחד בחדר, המשתמש שיצר את המשחק יקבל הודעת Toast שעליו להתחיל את המשחק.

    private String gameId;
    String player="";

    private BoardGame boardGame;

    private GamePresenter gamePresenter;
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        //after you click to join to an exists game room.
        super.onCreate(savedInstanceState);
    //    setContentView(R.layout.activity_game_room);

        // get intent data
        getIntentDataConfiguration();
      //gameRef = colRef.document(gameId);

        boardGame= new BoardGame(this, gameId, player);
        gamePresenter = boardGame.getPresenter();

        setContentView(boardGame);
        //this line allows me to present the board game.


    }

    private void getIntentDataConfiguration()
    {
        /// game Id is the specific place of
        //the room game.
        //It is like a list
        //that the collection is the head of the list.
        gameId = getIntent().getStringExtra("gameId");//DEBUG_GAME_ID;//getIntent().getStringExtra("gameId");
        // OTHER or HOST
        player = getIntent().getStringExtra("player");
    }


}