package com.example.smt;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;

public class Viewgeneratedqr extends AppCompatActivity {

    ImageView i1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewgeneratedqr);

        i1=(ImageView)findViewById(R.id.imageView3);

        i1.setImageBitmap(qrcode.bitmap);
    }
}