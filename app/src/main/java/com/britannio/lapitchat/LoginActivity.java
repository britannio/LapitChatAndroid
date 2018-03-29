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

import es.dmoral.toasty.Toasty;

public class LoginActivity extends AppCompatActivity {


    private TextInputLayout mloginEmail;
    private TextInputLayout mloginPassword;
    private Button mLogin_btn;

    private android.support.v7.widget.Toolbar mToolbar;

    //ProgressDialog
    private ProgressDialog mLoginProgress;

    //Firebase Auth
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Add toolbar for the register activity
        mToolbar = findViewById(R.id.login_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Login");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //enables the back arrow on the action bar


        mLoginProgress = new ProgressDialog(this); //indefinite progress bar

        //Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        
        //Login fields
        mloginEmail = findViewById(R.id.login_email);
        mloginPassword = findViewById(R.id.login_password);
        mLogin_btn = findViewById(R.id.login_btn);
        
        mLogin_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = mloginEmail.getEditText().getText().toString();
                String password = mloginPassword.getEditText().getText().toString();

                if(!TextUtils.isEmpty(email) //tiny bit of validation
                        && !TextUtils.isEmpty(password)
                        && Patterns.EMAIL_ADDRESS.matcher(email).matches() ) {

                    mLoginProgress.setTitle("Logging in");
                    mLoginProgress.setMessage("Please wait while we check your credentials");
                    mLoginProgress.setCanceledOnTouchOutside(false);
                    mLoginProgress.show();
                    login_user(email, password);

                } else {
                    Toasty.error(getApplicationContext(), "Fill All Fields correctly", Toast.LENGTH_SHORT, true).show();
                }
                
            }
        });
    }

    private void login_user(String email, String password) {

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("AUTH", "signInWithEmail:success");
                            Toasty.success(getApplicationContext(), "Login Successful", Toast.LENGTH_SHORT, true).show();
                            FirebaseUser user = mAuth.getCurrentUser();

                            mLoginProgress.dismiss();

                            //go to the main activity
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("AUTH", "signInWithEmail:failure", task.getException());
                            Toasty.error(LoginActivity.this, "Incorrect email or password",
                                    Toast.LENGTH_SHORT, true).show();

                            mLoginProgress.hide();


                        }

                        // ...
                    }
                });
        
    }
}
