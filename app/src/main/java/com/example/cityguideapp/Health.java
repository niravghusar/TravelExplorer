package com.example.cityguideapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class Health extends AppCompatActivity implements View.OnClickListener {

    private CardView card1,card2,card3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_health);

        card1 = (CardView) findViewById(R.id.c1);
        card3 = (CardView) findViewById(R.id.c3);

        card1.setOnClickListener(this);
        card3.setOnClickListener(this);
    }

    public void onClick(View v)
    {

        Intent i;

        switch (v.getId())
        {
            case R.id.c1 :
                i = new Intent(this,Hospital.class);
                startActivity(i);
                break;

            case R.id.c3 :
                i = new Intent(this,MedicalStore.class);
                startActivity(i);
                break;
        }

    }
}