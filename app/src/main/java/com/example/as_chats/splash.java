package com.example.as_chats;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class splash extends AppCompatActivity {

    ImageView logo;
    TextView name, own1, own2;
    Animation topAni, botAni;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        logo = findViewById(R.id.logoimg);
        name = findViewById(R.id.logoNameImg);
        own1 = findViewById(R.id.own1);
        own2 = findViewById(R.id.own2);

        topAni = AnimationUtils.loadAnimation(this, R.anim.top_animation);
        botAni = AnimationUtils.loadAnimation(this, R.anim.bot_animation);

        logo.setAnimation(topAni);
        name.setAnimation(botAni);
        own1.setAnimation(botAni);
        own2.setAnimation(botAni);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(splash.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        }, 4000);
    }
}