package com.hagitc.myfirstapplication;

import static com.hagitc.myfirstapplication.AppConstants.GAME_CONFIG;
import static com.hagitc.myfirstapplication.AppConstants.HOST;
import static com.hagitc.myfirstapplication.AppConstants.ONE_PHONE;
import static com.hagitc.myfirstapplication.AppConstants.OTHER;
import static com.hagitc.myfirstapplication.AppConstants.TWO_PHONES;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class GameChoice extends AppCompatActivity {
    //במסך הזה תהיה למשתמשים אפשרות לבחור בין שלושה כפתורים- PRACTICE, CREATE GAME, JOIN GAME.
    LinearLayout linearLayout;
    BoardGame boardGame;
    private RoomGame roomGame;

    private String gameId = "";
    FirebaseFirestore fb = FirebaseFirestore.getInstance();
    CollectionReference colRef;
    DocumentReference gameRef;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        //הפעולה מגדירה את תצוגת הפעילות.
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_choice);

    }
    private void addRoomToFB()
    {
        //הפעולה מוסיפה חדר משחק חדש למסד הנתונים של Firestore .
        // זו פעולה סינכרונית המצפה לתוצאה על ידי הוספת חדר משחק חדש לאוסף "GameRooms" ב-Firestore  כאשר התוצאה מצליחה (בוצעה בהצלחה), הפעולה מציגה את קוד המשחק החדש שנוצר בעזרת Toast ומציגה תמונה (ImageView) של סמן שיתוף.
        // הפעולה גם מעדכנת את המשתנה  gameId עם המזהה הייחודי של המשחק החדש, שמקורו בתוצאת ההצלחה של הוספת חדר המשחק שנוצר.
        FirebaseFirestore fb = FirebaseFirestore.getInstance();
        fb.collection("GameRooms").add(roomGame).addOnSuccessListener(new OnSuccessListener<DocumentReference>()
        {
            @Override
            public void onSuccess(DocumentReference documentReference)
            {
                TextView codeTextView = findViewById(R.id.codeTextView);
                ImageView shareImage = findViewById(R.id.shareicon);
                gameId = documentReference.getId();
                //codeTextView.setText("Hello! THIS IS THE CODE FOR" + creator.getName() + "'S GAME." + creator.getName() + "IS INVITING YOU TO JOIN! THE CODE IS: " + gameId);
                codeTextView.setText("Your game code is: " + gameId + " .Share it with your friends!!!");
                codeTextView.setVisibility(View.VISIBLE);
                shareImage.setVisibility(View.VISIBLE);

                Log.d("ONSUCCESS", "id:" + documentReference.getId());
             //   shareWithFriends(gameId);



            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("ONFAILER", "onFailure: " + e.getMessage());
                        //e presents a message that explains why it did not success.
                    }
                });


    }

    //ה־ActivityResultLauncher<Intent> מכיל לוגיקה המנהלת את פעולת האפליקציה עם פעילות חיצונית
    // וקורא את  GameRoomActivity  כשהפעולה מצליחה.
    ActivityResultLauncher<Intent> mActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {

                    Intent i = new Intent(this,GameRoomActivity.class);
                    i.putExtra("gameId",gameId);
                    i.putExtra("player",HOST);
                    startActivity(i);
            //    }
            }
    );

    public void shareWithFriends(View view)
    {
        // implicit intent - אינטרנט מרומז
        Intent shareIntent = new Intent(Intent.ACTION_SEND);//אני מצהירה שאני רוצה לבצע פעולה של שליחה
        //this action indicates that you want to send data.
        shareIntent.setType("text/plain"); // for sharing text
        shareIntent.putExtra(Intent.EXTRA_TEXT, "Hello! THIS IS THE CODE FOR THE GAME: " + gameId + " JOIN THE GAME! THE CREATOR IS WAITING FOR YOU!");
        //startActivityForResult(Intent.createChooser(shareIntent, "Share using"),1);
        shareIntent.createChooser(shareIntent, "Share using");

        //כשאני אומרת לו פור ריזאלט אני בעצם רושמת את עצמי לפעולה שמחכה לסיום השליחה
        mActivityResultLauncher.launch(shareIntent);

    }



    public void onclickCreateGame(View view)
    {
        roomGame = new RoomGame();
        roomGame.setStatus(AppConstants.CREATED);
        roomGame.setCurrentPlayer(HOST);
        roomGame.setTouchedColumn(-1);
        //in this way I know If this is the first move.
        addRoomToFB();
    }

    public void practicefunction(View view)
    {
        Intent intent = new Intent(GameChoice.this, GameActivity.class);
        startActivity(intent);
    }

    public void joinGame(View view)
    {
        //I need do check in FB if the code exists
        EditText enterCodeText = findViewById(R.id.entercodeET);
        Button clickToJoin = findViewById(R.id.joinroomgameB);
        enterCodeText.setVisibility(View.VISIBLE);
        clickToJoin.setVisibility(View.VISIBLE);

    }

    public void joinIntoAnExistRoomGame(View view)
    {
        //אחרי שהשחקן הוזמן למשחק על ידי משתמש אחר, הוא יכניס את הקוד שהוא קיבל.
        //לאחר שהוא יכניס את הקוד שהוא קיבל הוא ילחץ על הכפתור של הצטרפות למשחק.
        //בפעולה שלהלן הכפתור ישלח את השחקן לחדר משחק בהתאם לקוד שהוא הכניס.
        EditText etCode = findViewById(R.id.entercodeET);
        String gameCode = etCode.getText().toString();
        Intent i = new Intent(this, GameRoomActivity.class);
        i.putExtra("gameId",gameCode);
        //put extra = צירוף שדות למסך בדרך של מפתח ערך
        i.putExtra("player",OTHER);
        startActivity(i);
    }



    public void questionMarkClicked(View view) {
        Dialog dialog = new Dialog (this);
        dialog.setContentView(R.layout.custom_dialog_box_instructions);
        dialog.show();
    }

    public void leaderTable(View view)
    {
        Intent i = new Intent(this, LeaderBoard.class);
        startActivity(i);
    }
}
