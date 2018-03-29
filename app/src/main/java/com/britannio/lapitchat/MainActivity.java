package com.britannio.lapitchat;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toolbar;

import com.afollestad.materialdialogs.MaterialDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private android.support.v7.widget.Toolbar mToolbar;
    private boolean networkConnected = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();

        mToolbar = findViewById(R.id.main_page_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Lapit Chat");

        /*ConnectivityManager conMan = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = conMan.getActiveNetworkInfo();
        Log.d("Connected state",networkInfo.getState().toString());
        if(networkInfo.isConnected()){
            Log.d("Connected",networkInfo.toString());
            networkConnected = true;

        } else {

*//*
            AlertDialog.Builder  builder  = new AlertDialog.Builder(MainActivity.this);
            builder.setMessage("Lapit Chat cannot connect to the internet. Please check your network settings and retry ").setTitle("Network Error").setPositiveButton("RETRY", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            }).setNegativeButton("EXIT", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });

            AlertDialog alertDialog = builder.create();
            alertDialog.show();
*//*

        }*/


    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser == null) {

            sendToStart();

        }
    }

    private void sendToStart() {

        Intent startIntent = new Intent(MainActivity.this, StartActivity.class);
        startActivity(startIntent);
        finish(); //Prevents back button from sending user to the previous activity

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //To make our menu work we need to inflate it

        getMenuInflater().inflate(R.menu.main_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId() == R.id.main_logout_btn) {

            FirebaseAuth.getInstance().signOut();
            sendToStart();

        }

        return true;
    }
}
