package com.example.christopher.fchat.Activitys;

import android.content.Intent;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.christopher.fchat.Models.UserInformation;
import com.example.christopher.fchat.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {

    private Button Register;
    private EditText Name;
    private EditText Email;
    private EditText Password;

    // Firebase Auth
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();

    // Firebase Realtime Database
    DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
    // Debugging TAG
    private String TAG = "RegisterActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Toolbar toolbar = findViewById(R.id.toolbar10);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Create Account");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Register = findViewById(R.id.activity_register_registerbtn);
        Name = findViewById(R.id.activity_register_name);
        Email = findViewById(R.id.activity_register_email);
        Password = findViewById(R.id.activity_register_password);


        Register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateUser();
            }
        });


    }

    private void CreateUser() {

        mAuth.createUserWithEmailAndPassword(Email.getText().toString(), Password.getText().toString())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            Toast.makeText(RegisterActivity.this, "Account created, please Login", Toast.LENGTH_LONG).show();

                            writeDB();
                            OpenLogin();


                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(RegisterActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();

                        }

                    }
                });
    }

    public void OpenLogin() {

        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
        startActivity(intent);
    }

    private void writeDB() {
        String uid = mAuth.getCurrentUser().getUid();
        if (mAuth.getCurrentUser() != null) {

            UserInformation uInfo = new UserInformation();
            uInfo.setName(Name.getText().toString());
            uInfo.setEmail(Email.getText().toString());
            uInfo.setUID(uid);
            ref.child("users").child(uid).setValue(uInfo);

            Log.e(TAG, uInfo.getName());
            Log.e(TAG, uInfo.getEmail());
            Log.e(TAG, uInfo.getUID());
            //   Log.e(TAG, uInfo.getPublicKey());

        }


    }

}

