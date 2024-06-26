package com.hagitc.myfirstapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class MainActivity extends AppCompatActivity {
    //הActivity הזה מיועד להרשמה.
    // ברגע שנכנסים לאפליקציה בפעם הראשונה הActivity הזה מופיע וצריך לבצע הרשמה.
    // בפעמים הבאות בהן המשתמשים יכנסו לאפליקציה שלי הם יועברו ישר לActivity הבא.
    private User user;
    FirebaseAuth auth = FirebaseAuth.getInstance(); ////הפנייה למשתנה שייבא את כל הספריית תמיכה


    @Override

    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //כל משתמש שרוצה להצטרף

        if (auth.getCurrentUser() != null)
        {
            //זה אומר שכבר נרשמו לאפליקצייה וצריך לעבור לדף הבא ישירות
            String mail = auth.getCurrentUser().getEmail();
            String userId = auth.getCurrentUser().getUid();
            Toast.makeText(this, userId, Toast.LENGTH_LONG).show();
            Intent intent = new Intent (MainActivity.this, GameChoice.class);
            startActivity(intent);
        }

    }

    public void Register(View view)
    {
        EditText etName = findViewById(R.id.editTextName);
        EditText etPassword = findViewById(R.id.editTextTextPassword);
        EditText etEmail = findViewById(R.id.editTextTextEmailAddress);


        if(TextUtils.isEmpty(etPassword.getText()))
        {
            Toast.makeText(MainActivity.this, "THE PASSWORD BOX IS EMPTY", Toast.LENGTH_LONG).show();
            return;
        }
        if(TextUtils.isEmpty(etEmail.getText()))
        {
            Toast.makeText(MainActivity.this, "THE EMAIL BOX IS EMPTY", Toast.LENGTH_LONG).show();
            return;
        }
        if(TextUtils.isEmpty(etName.getText()))
        {
            Toast.makeText(MainActivity.this, "THE NAME BOX IS EMPTY", Toast.LENGTH_LONG).show();
            return;
        }

        String email = etEmail.getText().toString();
        String password = etPassword.getText().toString();
        String name = etName.getText().toString();

        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful() == true)
                        {

                            User u = new User();
                            u.setName(name);
                            u.setEmail(email);

                            FirebaseFirestore fb = FirebaseFirestore.getInstance();

                            fb.collection("User").document(auth.getCurrentUser().getUid()).set(u)
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void unused) {
                                                    Toast.makeText(MainActivity.this, "Register succeeded", Toast.LENGTH_LONG).show();
                                                    Intent intent = new Intent(MainActivity.this, GameChoice.class);
                                                    startActivity(intent);
                                                }
                                            });



                        }
                        else
                        {
                            Toast.makeText(MainActivity.this, " " + task.getException(), Toast.LENGTH_LONG).show();

                        }
                    }
                });


    }

}