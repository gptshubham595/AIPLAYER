package com.shubham.aiplayer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

public class Splash extends AppCompatActivity {
ImageView image;
TextView text,shu;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        image=findViewById(R.id.image);
        text=findViewById(R.id.text);
        shu=findViewById(R.id.shubham);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                image.performClick();
            }
        }, 50);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                text.setVisibility(View.VISIBLE);
                shu.setVisibility(View.VISIBLE);
            }
        }, 50);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i=new Intent(getApplicationContext(),MusicFiles.class);
                startActivity(i);
            }
        }, 4000);

        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animation animation = AnimationUtils.loadAnimation(getApplicationContext(),
                        R.anim.bottom_to_top);
                    Animation animationi = AnimationUtils.loadAnimation(getApplicationContext(),
                            R.anim.fade_in);
                    image.startAnimation(animation);
                    text.setVisibility(View.VISIBLE);
                shu.setVisibility(View.VISIBLE);

                text.startAnimation(animationi);
                shu.startAnimation(animationi);
            }
        });

    }
}
