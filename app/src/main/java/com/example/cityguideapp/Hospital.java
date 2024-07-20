package com.example.cityguideapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.net.URL;
import java.util.List;

public class Hospital extends AppCompatActivity {
    LinearLayout layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hospital);

        layout = findViewById(R.id.scroll_college);

        TextView nameView = findViewById(R.id.t1);

        nameView.setText("Hospital");

        List<CityPlace> colleges = MainActivity.Instance.GetPlaces("hospital");

        for(CityPlace college : colleges) {
            addCard(college);
        }
    }

    private void addCard(CityPlace place) {
        final View view = getLayoutInflater().inflate(R.layout.entity_card, null);

        TextView nameView = view.findViewById(R.id.name);
        TextView addressView = view.findViewById(R.id.address);
        ImageView imageView = view.findViewById(R.id.img);
        Button button = view.findViewById(R.id.direction);
        // operations to be performed
        // when user tap on the button
        if (button != null) {
            button.setOnClickListener((View.OnClickListener)(new View.OnClickListener() {
                public final void onClick(View it) {
                    Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                            Uri.parse(place.directionUrl));
                    startActivity(intent);


                }
            }));
        }

        imageView.setTag(place.imageUrl);
        new DownloadImagesTask().execute(imageView);

        nameView.setText(place.name);
        addressView.setText(place.address);

        layout.addView(view);
    }
}