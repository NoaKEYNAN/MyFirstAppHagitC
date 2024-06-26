package com.hagitc.myfirstapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class MainPage extends AppCompatActivity {
    //העמוד הזה מופיע בכל פעם שתכנסו לאפליקציה שלי ומופיעה בו תמונה שעליה כתוב
    //LET’S START THIS GAME למשך זמן שהגדרתי בקוד (למשך חמש שניות).

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);
        // add image view animation
        ImageView imageView = findViewById(R.id.imageView);
  //      Animation animation = AnimationUtils.loadAnimation(this, R.anim.rotate);
    //    imageView.startAnimation(animation);
        // start the countdown timer
        countDownTimer.start();
    }

    CountDownTimer countDownTimer = new CountDownTimer(5000, 1000) {
        @Override
        public void onTick(long l) {

        }

        public void onFinish() {
            // later add the transition
            Intent intent = new Intent(MainPage.this, MainActivity.class);
            startActivity(intent);


            finish();
        }
    };


}




