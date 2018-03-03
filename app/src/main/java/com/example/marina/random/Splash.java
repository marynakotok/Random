package com.example.marina.random;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class Splash extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        Thread clock = new Thread() {
            public void run() {
                try {
                    sleep(3000);
//                Intent openSecondActivity = new Intent(getApplicationContext(),MainActivity.class);
//                startActivity(openSecondActivity);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    Intent openSecondActivity = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(openSecondActivity);
                }}};
        clock.start();
    }
}

