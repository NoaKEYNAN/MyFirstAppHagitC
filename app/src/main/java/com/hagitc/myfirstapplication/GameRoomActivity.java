package com.hagitc.myfirstapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class GameRoomActivity extends AppCompatActivity {
    FirebaseFirestore fb = FirebaseFirestore.getInstance();
    //הפנייה שמאפשרת לי
    //להשתמש במחלקה של פיירבייס גם באקטיביטי הזה.

    CollectionReference colRef;
    DocumentReference gameRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //after you click to join to an exists game room.
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_room);
        //לשים בתוך פעולה.
        String gameId = getIntent().getStringExtra("gameId");
        colRef = fb.collection("GameRooms");
        gameRef = colRef.document(gameId);

        getRoomData();

      //  boardGame =
    }

    private void getRoomData() {

        gameRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {


                    RoomGame room = (task.getResult().toObject(RoomGame.class));
                    if(room.getStatus() == "Created")
                    {
                        room.setStatus("Joined");
                        //After the first player will do the
                        //first action
                        //the status will be changed
                        //into started
                    }


                } else // not sucess
                {
                    Toast.makeText(GameRoomActivity.this, "Succeeded", Toast.LENGTH_LONG).show();
                    GameRoomActivity.this.finish();


                }
            }
        });


    }


}