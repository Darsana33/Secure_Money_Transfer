package com.example.smt;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

public class qrcode extends AppCompatActivity {

    EditText editText;
    Button button;

    public static Bitmap bitmap;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrcode);

        editText = (EditText)findViewById(R.id.etamount);
        button = (Button)findViewById(R.id.button);


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(login.usertype.equalsIgnoreCase("merchant"))
                {
                    MultiFormatWriter multiFormatWriter = new MultiFormatWriter();

                    try {
                        BitMatrix bitMatrix = multiFormatWriter.encode(login.logid + "-" + login.merchantname + "-" + editText.getText().toString(), BarcodeFormat.QR_CODE, 500, 500);
                        BarcodeEncoder

                                barcodeEncoder = new BarcodeEncoder();
                        bitmap = barcodeEncoder.createBitmap(bitMatrix);
                        startActivity(new Intent(getApplicationContext(), Viewgeneratedqr.class));

//                    imageView.setImageBitmap(bitmap);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                if(login.usertype.equalsIgnoreCase("user"))
                {
                    MultiFormatWriter multiFormatWriter = new MultiFormatWriter();

                    try {
                        BitMatrix bitMatrix = multiFormatWriter.encode(login.logid + "-" + login.merchantname + "-" + editText.getText().toString(), BarcodeFormat.QR_CODE, 500, 500);
                        BarcodeEncoder
                                barcodeEncoder = new BarcodeEncoder();
                        bitmap = barcodeEncoder.createBitmap(bitMatrix);
                        startActivity(new Intent(getApplicationContext(), Viewgeneratedqr.class));

//                    imageView.setImageBitmap(bitmap);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });

    }

    public void onBackPressed()
    {
        // TODO Auto-generated method stub
        if(login.usertype.equalsIgnoreCase("merchant"))
        {
            startActivity(new Intent(getApplicationContext(),homemerchant.class));
        }
        if(login.usertype.equalsIgnoreCase("user"))
        {
            startActivity(new Intent(getApplicationContext(),homeuser.class));
        }


    }


}