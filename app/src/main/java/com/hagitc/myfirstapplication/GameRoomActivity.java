package com.hagitc.myfirstapplication;

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
    FirebaseFirestore fb = FirebaseFirestore.getInstance();
    //הפנייה שמאפשרת לי
    //להשתמש במחלקה של פיירבייס גם באקטיביטי הזה.

    CollectionReference colRef; //THE HEAD OT THE COLLECTION.
    private String gameId;
    String player="";
    DocumentReference gameRef;

    private BoardGame boardGame = new BoardGame(this, gameId, "HOST");

    private GamePresenter gamePresenter = boardGame.getPresenter();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //after you click to join to an exists game room.
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_room);

        // get intent data
        getIntentDataConfiguration();
      //gameRef = colRef.document(gameId);

        getRoomData();

      //  boardGame =
    }

    private void getIntentDataConfiguration() {
        /// game Id is the specific place of
        //the room game.
        //It is like a list
        //that the collection is the head of the list.

        gameId = getIntent().getStringExtra("gameId");
        colRef = fb.collection("GameRooms");
        gameRef = colRef.document("B5urP8uZ4zAjeg2SOapU ");

    }

    private void getRoomData() {

        // including - this which is a reference to the activity
        // this means that once Activity is finished -
        // it will remove the listening action

        gameRef.addSnapshotListener(this,new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
              // a change has happened in the room game parameters
                // we have been notified and received the new object

                RoomGame roomGame = documentSnapshot.toObject(RoomGame.class);
                if (roomGame.getStatus().equals("Created"))
                {
                    if(roomGame.getCurrentPlayer().equals(AppConstants.OTHER))
                    {
                        roomGame.setStatus("JOINED");
                    }
                }
                if(roomGame.getStatus().equals("JOINED"))
                {
                    if(roomGame.getCurrentPlayer().equals(AppConstants.HOST))
                    {
                        int touchedColumn = roomGame.getTouchedColumn();
                        gamePresenter.userClick(touchedColumn);


                    }
                }

                // if the status is created
                //      1. if I am the host - do nothing
                //      2. if I am other -> change status to JOINED

                // if status is joined -
                //      check current player - if current is host
                //      1. if am host play move:
                //              read from roomgame other player move
                //              update the view
                //              get host move
                //              set move and change curr player to other
                //      2. if am not host -> do nothing
                //
                //      if current player is other
                //      1. if host -> do nothing
                //      2. if other -> play and change current player to host



            }
        });
        gameRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    RoomGame room = (task.getResult().toObject(RoomGame.class));
                    if(room.getStatus().equals("CREATED"))
                    {
                        room.setStatus("Joined");
                        //After the first player will do the
                        //first action
                        //the status will be changed
                        //into started
                    }


                } else // not success
                {
                    Toast.makeText(GameRoomActivity.this, "NOT SUCCEEDED", Toast.LENGTH_LONG).show();
                    GameRoomActivity.this.finish();


                }
            }
        });


    }


}