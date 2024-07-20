package com.example.cityguideapp;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

import java.net.MalformedURLException;
import java.net.URL;

public class DownloadImagesTask extends AsyncTask<ImageView, Void, Bitmap> {
    ImageView imageView = null;
    @Override
    protected Bitmap doInBackground(ImageView... imageViews) {
        this.imageView = imageViews[0];
        return download_Image((String)imageView.getTag());
    }

    @Override
    protected void onPostExecute(Bitmap result) {
        imageView.setImageBitmap(result);
    }

    private Bitmap download_Image(String url) {
        URL urlc = null;
        try {
            urlc = new URL(url);
            return BitmapFactory.decodeStream(urlc.openConnection().getInputStream());

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }
}