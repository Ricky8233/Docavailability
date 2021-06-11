package com.example.android.docavailability;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;

public class WelcomeActivity extends AppCompatActivity {


    public Integer REQUEST_EXIT = 9;
    public FirebaseAuth mAuth;
    public FirebaseUser currentUser;
    Button signUpButton;
    Button signInButton;
    Button check_availability;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);


        mAuth = FirebaseAuth.getInstance();


        signUpButton = findViewById(R.id.welcomeSignUpButton);
        signInButton = findViewById(R.id.welcomeSignInButton);
        check_availability = findViewById(R.id.check_availability);

        if (mAuth.getCurrentUser() != null) {
            mAuth.getCurrentUser().reload().addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {

                    currentUser = mAuth.getCurrentUser();
                    signInButton.setVisibility(INVISIBLE);
                    signUpButton.setVisibility(INVISIBLE);
                    if (currentUser != null )
                    {
                        Intent i = new Intent(WelcomeActivity.this, Hospital_details.class);
                        startActivity(i);
                        WelcomeActivity.this.finish();
                    }
                }
            });

        } else {

            signInButton.setVisibility(VISIBLE);
            signUpButton.setVisibility(VISIBLE);
        }
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent signUpIntent = new Intent(WelcomeActivity.this, SignUpActivity.class);
                startActivity(signUpIntent);
                finish();
            }
        });

        check_availability.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(WelcomeActivity.this, Check_availability_home.class);
                startActivity(i);
                finish();
            }
        });
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent signInIntent = new Intent(WelcomeActivity.this, SignInActivity.class);
                startActivityForResult(signInIntent, REQUEST_EXIT);
                finish();

            }
        });

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_EXIT) {
            if (resultCode == RESULT_OK) {
                this.finish();

            }
        }
    }



}