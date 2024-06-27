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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firestore.v1.Value;

public class GameRoomPresenter extends GamePresenter
//המחלקה GameRoomPresenter  אחראית לניהול המשחק ברשת בזמן אמת בין שני שחקנים.
// היא מאזינה לשינויים במסמך המשחק ב-Firebase Firestore ומעדכנת את ממשק המשתמש בהתאם.
// המחלקה דואגת לסנכרון בין השחקנים ומוודאת שהמשחק מתנהל בצורה חלקה בין שני המכשירים.
{

    private String currPlayer = "";
    private String docRef = "";


    private Activity hostingActivity;
    //המשתנה `hostingActivity` במחלקה `GameRoomPresenter` מייצג את האקטיביטי שבו מתבצע המשחק,
    // והוא משמש לצורך אינטראקציה עם המשתמש וה-Firebase Firestore בתוך האקטיביטי.
    // המשתנה מוודא שהמאזינים ל-Firebase קשורים לאקטיביטי הנוכחי.
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
        //- `String docRef`: מזהה ייחודי עבור המסמך ב-Firebase Firestore המשמש לייצוג חדר המשחק.
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
        //פעולה זו משיגה את נתוני חדר המשחק מ-Firebase Firestore,
        // ואם השחקן הנוכחי הוא OTHER, מעדכנת את הסטטוס ל-JOINED
        // ולאחר מכן מאזינה לשינויים בחדר המשחק באמצעות listenForGameChanges.

        colRef = fb.collection("GameRooms");
        gameRef = colRef.document(this.docRef); // לחלץ את חדר המשחק הספציפי המבוקש



        //ביצוע בקשה אסינכרונית לקבלת הנתונים מהמסמך ב-Firebase Firestore
        //(ביצוע בקשה אסינכרונית משמעותו שהבקשה מתבצעת ברקע מבלי לחסום את המשך הרצת התוכנית.
        // במקום לחכות לסיום הבקשה, התוכנית ממשיכה לרוץ והשלמת הבקשה תטופל ברגע שהיא תסתיים).
        gameRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            //כאשר הבקשה מסתיימת, בודקים אם היא הצליחה.
            // אם כן, מתבצע ניסיון להמיר את התוצאה לאובייקט מסוג `RoomGame`.
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
        //מטרת הפעולה להאזין לשינויים בנתוני המשחק במסמך Firebase Firestore ולהגיב לשינויים אלו בזמן אמת.
        // פעולה זו מאפשרת לעדכן את המצב המקומי של המשחק באפליקציה ברגע שמתרחש שינוי בנתוני המשחק,
        // כמו מהלך חדש של שחקן או שינוי בסטטוס המשחק.

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
            //addSnapshotListener -האזנה לכל שינוי שקורה בזמן אמת
            //ברגע שיש שינוי הפעולה onEvent מזומנת.
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                // a change has happened in the room game parameters
                // we have been notified and received the new object
                //DocumentSnapshot documentSnapshot- מחזיק חדר משחק אחרי שינוי וכל פעם הוא מתעדכן

                if (documentSnapshot == null || !documentSnapshot.exists())
                    return;
                roomGame = documentSnapshot.toObject(RoomGame.class);//המרה לעצם מטפוס RoomGame
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
        // if we came from  firebase
        if(row==-1) {
            //אם השורה שהתקבלה היא `-1`,
            // זה אומר שהשינוי הגיע מ-Firebase
            // ולכן יש לחשב את השורה בהתאם לעמודה שהתקבלה.
            row = gameLogic.userClick(roomGame.getTouchedColumn());
        }
        //עדכון הלוח בהתאם לשחקן הנוכחי:
        //אם השחקן הנוכחי הוא השחקן שמחובר כרגע (השחקן המקומי), הצבע של העיגול יהיה אדום.
        //אם השחקן הנוכחי הוא השחקן השני, הצבע של העיגול יהיה צהוב.
        //מעדכנים את הלוח בהתאם ומעלים את מונה המהלכים.
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


                FirebaseFirestore fb = FirebaseFirestore.getInstance();
                FirebaseAuth auth = FirebaseAuth.getInstance();
                if(this.currPlayer.equals(HOST))
                {
                    if(currentplayer1==1)
                        fb.collection("User").document(auth.getCurrentUser().getUid())
                                .update("wins", FieldValue.increment(1));
                    else
                        fb.collection("User").document(auth.getCurrentUser().getUid())
                                .update("losts", FieldValue.increment(1));

                }
                else // this is other
                {
                    if(currentplayer1==1)
                        fb.collection("User").document(auth.getCurrentUser().getUid())
                                .update("losts", FieldValue.increment(1));
                    else
                        fb.collection("User").document(auth.getCurrentUser().getUid())
                                .update("wins", FieldValue.increment(1));

                }
                //update firebase with
                //
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
    //This function is only update in FB if it is a legal move!
    //this function is overriding the function userClick() in the presenter class.
    //this function update FB only (!!!) if it is a legal move. If it is not
    //a legal move it will not update FB.
            //הפונקציה בודקת אם המהלך שביצע המשתמש הוא חוקי,
    // ואם כן, היא מעדכנת את מצב המשחק בתצוגה וב-Firebase.
    // אם המהלך אינו חוקי, היא מציגה הודעת שגיאה למשתמש.
    {
        if(column ==-1)
            return;

        int row = gameLogic.userClick(column);
        //בודקת אם ניתן לבצע את המהלך בעמודה שנבחרה
        // ומחזירה את השורה שבה ניתן להניח את העיגול.
        // אם השורה שהתקבלה היא `-1`,
        // זה אומר שהעמודה מלאה והמהלך אינו חוקי.
        // במקרה כזה, מוצגת הודעה מתאימה למשתמש והפונקציה מסיימת את פעולתה.
        if (row ==-1)
        {
            boardGame.displayMessage("THIS COLUMN IS FULL");
            return;
        }
        // 1 move is legal
        // 2 logical board - udated
        // 3  user click in logic - first switches player
        // 4 update FB with move, RoomGame current player is ME (HOST Or OTHER)

        //אם המהלך חוקי, הפונקציה מעדכנת את העמודה שנבחרה ואת השחקן הנוכחי במשתנה `roomGame`.
        // לאחר מכן, היא מעדכנת את התצוגה בעזרת הפעולה `updateUI`.
            roomGame.setTouchedColumn(column);
            roomGame.setCurrentPlayer(currPlayer);
            updateUI(roomGame,row);

        // this means the move is legal this is why I need to update FB.
        ////עדכון המצב ב-Firebase:
        ////הפעולה מוודאת ש-`gameRef` מכיל את ההפניה למסמך ב-Firebase, ולאחר מכן מעדכנת את המסמך עם המצב החדש של המשחק.
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
    }

    private void switchFBPlayer(RoomGame roomGame) {

        if(roomGame.getCurrentPlayer().equals(HOST))
            roomGame.setCurrentPlayer(OTHER);
        else
            roomGame.setCurrentPlayer(HOST);

    }
}




