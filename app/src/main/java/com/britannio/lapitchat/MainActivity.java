package com.britannio.lapitchat;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
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

import es.dmoral.toasty.Toasty;

/* TODO
    -Check network state to ensure the user is connected to the internet
 */

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private android.support.v7.widget.Toolbar mToolbar;
    private boolean networkConnected = false;

    private ViewPager mViewPager; //linked to the xml viewpager

    private TabLayout mTabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();

        //Toolbar setup
        mToolbar = findViewById(R.id.main_page_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Lapit Chat");


        //TabLayout and ViewPager SETUP ############################################################
        //Adapter Setup for the viewPager
        mViewPager = findViewById(R.id.main_tabPager);
        mTabLayout = findViewById(R.id.main_tabLayout);
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

        //Add Fragments
        adapter.AddFragment(new RequestsFragment(), "REQUESTS");
        adapter.AddFragment(new ChatsFragment(), "CHAT");
        adapter.AddFragment(new FriendsFragment(), "FRIENDS");

        //giving the viewPager all the fragments then giving the tabLayout it's tabs
        mViewPager.setAdapter(adapter);
        mTabLayout.setupWithViewPager(mViewPager);

        //add an icon to each tab
        /*mTabLayout.getTabAt(0).setIcon(R.drawable.ic_requests);
        mTabLayout.getTabAt(1).setIcon(R.drawable.ic_chat);
        mTabLayout.getTabAt(2).setIcon(R.drawable.ic_friends);*/

        //##########################################################################################


        /*AlertDialog.Builder  builder  = new AlertDialog.Builder(MainActivity.this);
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


    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser == null) {

            sendToStart();

        } else { // should display Lapit Chat - @username
            getSupportActionBar().setTitle("Lapit Chat | @username");
            //Toasty.info(getApplicationContext(), mAuth.get)
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

        } else if (item.getItemId() == R.id.main_settings_btn) {

            Intent settingsIntent = new Intent(MainActivity.this, SettingsActivity.class);
            startActivity(settingsIntent);
        }


        return true;
    }
}
