package com.britannio.lapitchat;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

import es.dmoral.toasty.Toasty;

/*When the user registers an account it is handled here*/

public class RegisterActivity extends AppCompatActivity {

    private TextInputLayout mDisplayName;
    private TextInputLayout mUsername;
    private TextInputLayout mEmail;
    private TextInputLayout mPassword;
    private Button mCreateBtn;

    private android.support.v7.widget.Toolbar mToolbar;

    //ProgressDialog
    private ProgressDialog mRegProgress;

    //Firebase Auth
    private FirebaseAuth mAuth;

    //Firebase Database
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


        //Add toolbar for the register activity
        mToolbar = findViewById(R.id.register_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Create Account");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //enables the back arrow on the action bar


        mRegProgress = new ProgressDialog(this); //indefinite progress bar

        //Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        //Registration fields
        mDisplayName = findViewById(R.id.reg_display_name);
        mUsername = findViewById(R.id.reg_username);
        mEmail = findViewById(R.id.reg_email);
        mPassword = findViewById(R.id.reg_password);
        mCreateBtn = findViewById(R.id.reg_create_btn);

        //Create account
        mCreateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String display_name = mDisplayName.getEditText().getText().toString();
                String username = mUsername.getEditText().getText().toString();
                String email = mEmail.getEditText().getText().toString();
                String password = mPassword.getEditText().getText().toString();

                //tiny bit of validation
                if(!TextUtils.isEmpty(display_name) && !TextUtils.isEmpty(email)
                        && !TextUtils.isEmpty(password)
                        && !TextUtils.isEmpty(username)
                        && Patterns.EMAIL_ADDRESS.matcher(email).matches() ) {

                    mRegProgress.setTitle("Registering User");
                    mRegProgress.setMessage("Please wait while we create your account");
                    mRegProgress.setCanceledOnTouchOutside(false);
                    mRegProgress.show();
                    register_user(display_name,username, email, password);

                } else {
                    Toasty.error(getApplicationContext(), "Fill All Fields correctly", Toast.LENGTH_SHORT, true).show();
                }

            }
        });
    }

    private void register_user(final String display_name, final String username, String email, String password) {

        //Create user(email and password)
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            FirebaseUser current_user = mAuth.getCurrentUser();
                            //could check if current user is null but it isn't necessary here
                            String UID = current_user.getUid();

                            mDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(UID);

                            HashMap<String, String> userMap = new HashMap<>();
                            userMap.put("full_name", display_name);
                            userMap.put("username", username);
                            userMap.put("status", "Hi there, I'm using Lapit Chat");
                            userMap.put("image", "default");
                            userMap.put("thumb_image", "default");

                            mDatabase.setValue(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                    if (task.isSuccessful()) {

                                        // Sign in success, update UI with the signed-in user's information
                                        mRegProgress.dismiss();

                                        //Go to the MainActivity
                                        Intent mainIntent = new Intent(RegisterActivity.this, MainActivity.class);

                                        //prevents the user from going back to the start page once logged in
                                        //shouldn't be required since I have added the finish() call to the main activity.
                                        //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(mainIntent);
                                        finish(); //Prevents back button from sending user to the this activity

                                    }

                                }
                            });


                        } else {

                            mRegProgress.hide();
                            // If registration fails, display a message to the user.
                            Log.w("AUTH", "createUserWithEmail:failure", task.getException());


                            Toasty.error(RegisterActivity.this, "Registration failed. Please check the form and try again", Toast.LENGTH_LONG, true).show();


                        }

                            // ...
                    }
                });


    }
}
