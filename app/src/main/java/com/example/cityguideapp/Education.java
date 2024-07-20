package com.example.cityguideapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class Education extends AppCompatActivity implements View.OnClickListener {

    private CardView card1,card2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_education);

        card1 = (CardView) findViewById(R.id.c1);
        card2 = (CardView) findViewById(R.id.c2);

        card1.setOnClickListener(this);
        card2.setOnClickListener(this);
    }

    public void onClick(View v)
    {

        Intent i;

        switch (v.getId())
        {
            case R.id.c1 :
                i = new Intent(this,College.class);
                startActivity(i);
                break;

            case R.id.c2 :
                i = new Intent(this,School.class);
                startActivity(i);
                break;
        }

    }
}