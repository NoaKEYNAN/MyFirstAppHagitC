package com.hagitc.myfirstapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Firebase;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {
    FirebaseAuth fuser = FirebaseAuth.getInstance(); ////הפנייה למשתנה שייבא את כל הספריית תמיכה

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //כל משתמש שרוצה להצטרף

        if (fuser.getCurrentUser() != null)
        {
            //זה אומר שכבר נרשמו לאפליקצייה וצריך לעבור לדף הבא ישירות
            String mail = fuser.getCurrentUser().getEmail();
            String userId = fuser.getCurrentUser().getUid();
            Toast.makeText(this, userId, Toast.LENGTH_LONG).show();
            Intent intent = new Intent (MainActivity.this, GameChoice.class);
            startActivity(intent);
        }

    }

    public void Register(View view)
    {
        EditText etPassword = findViewById(R.id.editTextTextPassword);
        EditText etEmail = findViewById(R.id.editTextTextEmailAddress);
        String email = etEmail.getText().toString();
        String password = etPassword.getText().toString();
        fuser.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful() == true)
                        {
                            Toast.makeText(MainActivity.this, "Register succeeded", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(MainActivity.this, GameChoice.class);
                            startActivity(intent);
                        }
                        else
                        {
                            Toast.makeText(MainActivity.this, " " + task.getException(), Toast.LENGTH_LONG).show();

                        }
                    }
                });







    }


}