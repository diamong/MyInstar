package com.diamong.myinstar;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.github.chrisbanes.photoview.PhotoView;

public class FullPhotoActivity extends AppCompatActivity {

    PhotoView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_photo);

        imageView=findViewById(R.id.imageView);

        Intent intent=getIntent();
        String imageurl=intent.getStringExtra("imageurl");
        Glide.with(FullPhotoActivity.this).load(imageurl).into(imageView);
    }
}
