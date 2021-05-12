package com.example.startproject2;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class IntroActivity extends AppCompatActivity {
    Handler handler = new Handler();
    Runnable r = new Runnable() {
        @Override
        public void run() {

            Intent intent = new Intent(IntroActivity.this, MainActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.fadein, R.anim.fadeout);
            finish();
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);
        ImageView imageView2 = findViewById(R.id.imageView2);
        TextView textView5 = findViewById(R.id.textView5);
        Animation anim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate_anim);
        Animation anim2 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.alpha_anim);
        imageView2.startAnimation(anim);
        textView5.startAnimation(anim2);


    }

        protected void onResume() {
            super.onResume();
// 다시 화면에 들어어왔을 때 예약 걸어주기
            handler.postDelayed(r, 2000);
        }

        @Override
        protected void onPause() {
            super.onPause();
// 화면을 벗어나면, handler 에 예약해놓은 작업을 취소하자
            handler.removeCallbacks(r); // 예약 취소
        }




}
