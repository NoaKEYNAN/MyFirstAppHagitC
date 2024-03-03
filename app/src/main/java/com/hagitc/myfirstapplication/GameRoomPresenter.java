package com.hagitc.myfirstapplication;

import static com.hagitc.myfirstapplication.AppConstants.HOST;
import static com.hagitc.myfirstapplication.AppConstants.OTHER;
import static com.hagitc.myfirstapplication.AppConstants.TWO_PHONES;

import android.app.Activity;
import android.graphics.Color;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

public class GameRoomPresenter extends GamePresenter{


    private String currPlayer="";
    private String docRef="";

    private Activity hostingActivity;

    private

    FirebaseFirestore fb = FirebaseFirestore.getInstance();
    //הפנייה שמאפשרת לי
    //להשתמש במחלקה של פיירבייס גם באקטיביטי הזה.

    CollectionReference colRef; //THE HEAD OT THE COLLECTION.
    DocumentReference gameRef;



    public GameRoomPresenter(BoardGame boardGame, GameLogic gameLogic, String docRef, String player,Activity c) {
            super(boardGame, gameLogic);
            this.gameConfig = TWO_PHONES;
            this.docRef = docRef;
            this.currPlayer = player;
            this.hostingActivity =  c;


        // if HOST- wait for other to join -> litsen for changes
        if(player.equals(HOST))
            listenForGameChanges();

        else
            // else - get room data, set status to joined and then listen for changes
            getRoomData();

        //  boardGame =

    }



    // only OTHER player reaches

    private void getRoomData() {

        colRef = fb.collection("GameRooms");
        gameRef = colRef.document("B5urP8uZ4zAjeg2SOapU"); // docRef



        gameRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    RoomGame room = (task.getResult().toObject(RoomGame.class));
                    // if the status is created
                    //      1. if I am the host - cannot reach here...
                    //      2. if I am other -> change status to JOINED
                    if(room.getStatus().equals("CREATED") && currPlayer.equals(OTHER))
                    {
                        room.setStatus("Joined");
                        //After the first player will do the
                        //first action
                        //the status will be changed
                        //into started
                        listenForGameChanges();
                    }


                } else // not success
                {
                    Toast.makeText(hostingActivity, "NOT SUCCEEDED", Toast.LENGTH_LONG).show();
                    hostingActivity.finish();


                }
            }
        });


    }


    //
    private void listenForGameChanges() {

        // including - this which is a reference to the activity
        // this means that once Activity is finished -
        // it will remove the listening action

        gameRef.addSnapshotListener(hostingActivity,new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                // a change has happened in the room game parameters
                // we have been notified and received the new object


                if (documentSnapshot == null || !documentSnapshot.exists())
                    return;
                RoomGame roomGame = documentSnapshot.toObject(RoomGame.class);
                // this means HOST recieved before other joined...
                // should nut happen but better be safe than sorry:-)
                if (roomGame.getStatus().equals("Created")) {
                    return;
                }

                if (roomGame.getStatus().equals("JOINED")) {
                    if (roomGame.getCurrentPlayer().equals(HOST)) {
                        // if current column is -1 this means start game
                        int touchedColumn = roomGame.getTouchedColumn();

                        if (touchedColumn == -1) {
                            Toast.makeText(hostingActivity, " Let's start...", Toast.LENGTH_LONG).show();
                            return;
                        }
                        // else we know that:
                        // other player has already played
                        // reac the touched column
                        // set

                        userClick(touchedColumn);


                    }
                    //roomGame.getCurrentPlayer().equals(OTHER))
                    else // this means it is OTHER
                    {

                        int touchedColumn = roomGame.getTouchedColumn();

                        if (touchedColumn == -1) {
                            Toast.makeText(hostingActivity, " Wait for first player to start...", Toast.LENGTH_LONG).show();
                            return;
                        }
                        // this means that there is a move
                        userClick(touchedColumn);


                    }
                }


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

            ;
        });


}
    public void userClick(int column)
    //this function is overriding the function
            //userClick() in the presenter class.
            //I need to update the move in fb.
    {
        int row = gameLogic.userClick(column);
        boolean flag = false;//it will be change after a legal action
        if (row != (-1)) //אם זה מהלך חוקי
        {
            if (gameLogic.getCurrentPlayer() == 1)
            {
                boardGame.updateBoard(row, column, Color.RED);
                gameLogic.setCounter(gameLogic.getCounter() + 1);
                //update in fb
                gameRef = colRef.document(docRef);
                gameRef.update("touchedColumn", column);
            }
            else
            {
                boardGame.updateBoard(row, column, Color.YELLOW);
                gameLogic.setCounter(gameLogic.getCounter() + 1);
                //update in fb
                gameRef = colRef.document(docRef);
                gameRef.update("touchedColumn", column);

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
                //to update in fb the column to become -1
                if (gameLogic.isBoardFull() == true)
                {
                    boardGame.displayMessage("THE GAME IS END");
                    //אם אחרי הניצחון הלוח מלא אז המשחק נגמר
                    //צריך להוסיף כפתור שמסיים את המשחק
                    //to update in fb the column to become -1.
                    gameRef = colRef.document(docRef);
                    gameRef.update("touchedColumn", -1);
                }

            }
            if(gameLogic.isBoardFull()==true)
            {
                boardGame.displayMessage("IT IS A TIE AND THE GAME IS END");
                //TO ADD A RESTART BUTTON
                //to update in fb the touched column to become -1.
                gameRef = colRef.document(docRef);
                gameRef.update("touchedColumn", column);
            }
        }
    }
}
