package com.hagitc.myfirstapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class LeaderBoard extends AppCompatActivity {
    FirebaseAuth auth = FirebaseAuth.getInstance();
    FirebaseFirestore fb = FirebaseFirestore.getInstance(); // for everyone

    ArrayList<String> arr = new ArrayList<>();

    ArrayList<String> users = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leader_board);
        fb.collection("User").orderBy("wins", Query.Direction.DESCENDING).get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for(int i=0;i< queryDocumentSnapshots.getDocuments().size();i++)
                        {
                            User u = queryDocumentSnapshots.getDocuments().get(i).toObject(User.class);

                            String toShow = "" + (i+1) + ".\t" + u.getName() + " : " + u.getWins();
                            arr.add(toShow);
                            users.add(u.getName());

                        }

                        ListView lv = findViewById(R.id.listView);
                        ArrayAdapter<String> adapter = new ArrayAdapter<>(LeaderBoard.this, android.R.layout.simple_list_item_1,arr);

                        lv.setAdapter(adapter);

                        getUser();
                    }
                });

        // get this player's profile number of wins  and display in text view
        // show his profile - name, wins, losses... and rank


    }


    public void getUser() {

        fb.collection("User").document(auth.getCurrentUser().getUid()).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        User u = documentSnapshot.toObject(User.class);


                        int place  = users.indexOf(u.getName());
                          TextView tv = findViewById(R.id.textViewRank);
                          tv.setText("your rank is " + (place+1) + " wins: " + u.getWins());

                        //   tv.setText("Your number of wins is: " + u.getWins());
                    }
                });
    }


    public void backToMenu(View view)
    {
        Intent i = new Intent(this, GameChoice.class);
        startActivity(i);
    }
}


