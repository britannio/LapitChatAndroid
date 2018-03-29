package com.britannio.lapitchat;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class StartActivity extends AppCompatActivity {

    private Button mRegButton;
    private Button mLoginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

/*        AlertDialog.Builder  builder  = new AlertDialog.Builder(StartActivity.this);
        builder
                .setMessage("Lapit Chat cannot connect to the internet. Please check your network settings and retry ")
                .setTitle("Network Error")
                .setCancelable(false)
                .setPositiveButton("RETRY", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        }).setNegativeButton("EXIT", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();*/

        mRegButton = findViewById(R.id.start_reg_btn);

        mRegButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent reg_intent = new Intent(StartActivity.this, RegisterActivity.class);
                startActivity(reg_intent);
                finish();

            }
        });

        mLoginButton = findViewById(R.id.start_login_btn);

        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent login_intent = new Intent(StartActivity.this, LoginActivity.class);
                startActivity(login_intent);
                finish();

            }
        });


    }
}
