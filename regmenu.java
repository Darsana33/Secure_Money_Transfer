package com.example.smt;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.ImageView;

public class regmenu extends AppCompatActivity {
    ImageView im1,im2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regmenu);
        im1=(ImageView)findViewById(R.id.img1);
        im2=(ImageView)findViewById(R.id.img2);
        im1.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),merchantreg.class));


            }
        });

        im2.setOnClickListener(new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            startActivity(new Intent(getApplicationContext(),userreg.class));


        }
    });

}
    }
