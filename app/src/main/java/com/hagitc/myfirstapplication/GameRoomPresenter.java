package com.hagitc.myfirstapplication;

import static com.hagitc.myfirstapplication.AppConstants.CREATED;
import static com.hagitc.myfirstapplication.AppConstants.DEBUG_GAME_ID;
import static com.hagitc.myfirstapplication.AppConstants.HOST;
import static com.hagitc.myfirstapplication.AppConstants.JOINED;
import static com.hagitc.myfirstapplication.AppConstants.OTHER;
import static com.hagitc.myfirstapplication.AppConstants.TWO_PHONES;

import android.app.Activity;
import android.graphics.Color;
import android.util.Log;
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

public class GameRoomPresenter extends GamePresenter {

    private String currPlayer = "";
    private String docRef = "";

   // boolean flag = false;//it will be change after a legal action

    private Activity hostingActivity;
    //המשתנה `hostingActivity` במחלקה `GameRoomPresenter` מייצג את האקטיביטי שבו מתבצע המשחק, והוא משמש לצורך אינטראקציה עם המשתמש וה-Firebase Firestore בתוך האקטיביטי. המשתנה מוודא שהמאזינים ל-Firebase קשורים לאקטיביטי הנוכחי.
    FirebaseFirestore fb = FirebaseFirestore.getInstance();
    //הפנייה שמאפשרת לי
    //להשתמש במחלקה של פיירבייס גם באקטיביטי הזה.

    CollectionReference colRef; //THE HEAD OT THE COLLECTION.
    DocumentReference gameRef;

    private RoomGame roomGame = null;


    public GameRoomPresenter(BoardGame boardGame, GameLogic gameLogic, String docRef, String player, Activity c) {
        super(boardGame, gameLogic);
        this.gameConfig = TWO_PHONES;
        this.docRef = docRef;
        this.currPlayer = player;
        this.hostingActivity = c;


        // if HOST- wait for other to join -> listen for changes
        if (player.equals(HOST))
            listenForGameChanges();

        else
            // else - get room data, set status to joined and then listen for changes
            getRoomData();
    }

    // only OTHER player reaches

    private void getRoomData() {

        colRef = fb.collection("GameRooms");
        gameRef = colRef.document(this.docRef); // docRef


        gameRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    RoomGame room = (task.getResult().toObject(RoomGame.class));
                    // if the status is created
                    //      1. if I am the host - cannot reach here...
                    //      2. if I am other -> change status to JOINED
                    if (room.getStatus().equals(CREATED) && currPlayer.equals(OTHER)) {
                        room.setStatus(JOINED);
                        //After the first player will do the
                        //first action
                        //the status will be changed
                        //into started
                        gameRef.set(room);
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
        if (gameRef == null)
        {
            colRef = fb.collection("GameRooms");
            gameRef = colRef.document(this.docRef); // docRef

        }


        gameRef.addSnapshotListener(hostingActivity, new EventListener<DocumentSnapshot>() {
            //הפנייה לאקטיביטי עצמו
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                // a change has happened in the room game parameters
                // we have been notified and received the new object

                if (documentSnapshot == null || !documentSnapshot.exists())
                    return;
                roomGame = documentSnapshot.toObject(RoomGame.class);
                // this means HOST recieved before other joined...
                // should nut happen but better be safe than sorry:-)
                if (roomGame.getStatus().equals(CREATED)) {
                    return;
                }

                if (roomGame.getStatus().equals(JOINED)) {
                    // if localplayer equals FB Player
                    // this means it is my TURN
                    // only for first move column -1
                        // if current column is -1 this means start game
                        int touchedColumn = roomGame.getTouchedColumn();

                        // will be true only if HOST first MOVE!
                        if (touchedColumn == -1) {
                            Toast.makeText(hostingActivity, " Let's start...", Toast.LENGTH_LONG).show();
                            return;
                        }
                        // else we know that:
                        // other player has already played
                        // reach the touched column
                        // set

                    if (!roomGame.getCurrentPlayer().equals(currPlayer)) {

                        // userClick(touchedColumn);
                        updateUI(roomGame,-1);
                    }
                    /*
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

                     */
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

    private void updateUI(RoomGame roomGame,int row)
    {

        Log.d("UPDATE UI ", "update UI Entrance : " + row + " , " + roomGame.getTouchedColumn() + roomGame.getCurrentPlayer());

        // if we came from  firebase
        if(row==-1) {
            row = gameLogic.userClick(roomGame.getTouchedColumn());
            Log.d("UPDATE UI ", "from firebase : " + row + " , " + roomGame.getTouchedColumn() + roomGame.getCurrentPlayer());
        }
            if (roomGame.getCurrentPlayer().equals(currPlayer))//in FB
            {
                boardGame.updateBoard(row, roomGame.getTouchedColumn(), Color.RED);
                gameLogic.setCounter(gameLogic.getCounter() + 1);
            }
            else
            {
                boardGame.updateBoard(row, roomGame.getTouchedColumn(), Color.YELLOW);
                gameLogic.setCounter(gameLogic.getCounter() + 1);
            }

        if (gameLogic.getCounter() >= 7 && gameLogic.getCounter() <= 42)
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
                //boardGame.displayMessage("PLAYER" + currentplayer1 + " WON!");
                String message = "PLAYER " + currentplayer1 + " WON!";
                boardGame.showGameOver(message);

            }
            if(gameLogic.isBoardFull()==true)
            {
                //boardGame.displayMessage("IT IS A TIE AND THE GAME IS OVER");
                String message = "IT IS A TIE AND THE GAME IS OVER";
                boardGame.showGameOver(message);
                //TO ADD A RESTART BUTTON
                //to update in fb the touched column to become -1.
                roomGame.setTouchedColumn(-1);
                roomGame.setCurrentPlayer(HOST);
                gameRef.set(roomGame);
            }
        }







    }




    @Override
    public void userClick(int column)
    //This function is only update in FB if it is al legal move!
    //this function is overriding the function userClick() in the presenter class.
    //this function update FB only (!!!) if it is a legal move. If it is not
    //a legal move it will not update FB.
    {
        if(column ==-1)
            return;

        int row = gameLogic.userClick(column);
        if (row ==-1) //if it is a legal move -> update firebase
        {
            boardGame.displayMessage("THIS COLUMN IS FULL");
            return;
        }
        // 1 move is legal
        // 2 logical board - udated
        // 3  user click in logic - first switches player
        // 4 update FB with move, RoomGame current player is ME (HOST Or OTHER)
            roomGame.setTouchedColumn(column);
            roomGame.setCurrentPlayer(currPlayer);
            updateUI(roomGame,row);

        // this means the move is legal this is why I need to update FB.
            gameRef = colRef.document(docRef);
            if (roomGame != null)
            {
             //   roomGame.setTouchedColumn(column);
           //     switchFBPlayer(roomGame);//From "host" to "other" or from "other" to "host".
                //     gameLogic.switchPlayer();//From (1) to (-1) or from (-1) to (1) in the gameLogic.
                gameRef.set(roomGame);// update FB.
            }



        // selected row without updating the logical board and trhe ui
        // they will be updated once onEvent is triggered by the firebase after the change

        //userClick() return which row you can paint- if the column is full it will return (-1)
        //else the function will return the row in the touched column you can paint.
/*
        if (row != (-1)) //if it is a legal move -> update firebase
        {
            flag = true;
            updateUI(roomGame);
        }
        else
        {
            updateUI(roomGame);
        }

 */
    }

    private void switchFBPlayer(RoomGame roomGame) {

        if(roomGame.getCurrentPlayer().equals(HOST))
            roomGame.setCurrentPlayer(OTHER);
        else
            roomGame.setCurrentPlayer(HOST);

    }
}




