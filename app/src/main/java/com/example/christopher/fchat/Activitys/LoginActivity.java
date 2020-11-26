package com.example.christopher.fchat.Activitys;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.christopher.fchat.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    private EditText mEmailField;
    private EditText mPasswordField;

    private Button mLogin;
    private Button Create;

    private FirebaseAuth mAuth;

    private FirebaseAuth.AuthStateListener mAuthListener;

    // Tag for debugging in console
    private static final String TAG = "LoginActivity";

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
    }
    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Toolbar toolbar = findViewById(R.id.toolbar_11);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Login");
        mAuth = FirebaseAuth.getInstance();

        mEmailField = (EditText) findViewById(R.id.activity_login_Email);
        mPasswordField = (EditText) findViewById(R.id.activity_login_Password);

        mLogin = findViewById(R.id.activity_login_LoginBtn);
        Create = findViewById(R.id.activity_login_create);

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() != null) {
                    Log.d(TAG, "User is Singed in, launching MainActvity");
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                }
            }
        };
        mLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "Login Button got clicked");
                startSignIn();
            }
        });

        Create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               OpenRegister();
            }
        });


    }

    private void OpenRegister()
    {
        Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
        startActivity(intent);
    }


    private void startSignIn() {
        String email = mEmailField.getText().toString();
        String password = mPasswordField.getText().toString();

        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            Toast.makeText(LoginActivity.this, "Please insert Email and Password", Toast.LENGTH_LONG);
        } else {

            mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {

                    if (task.isSuccessful()) {
                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                        Toast.makeText(LoginActivity.this, "Login was sucsessfull", Toast.LENGTH_SHORT).show();
                        Log.d(TAG, "Sign in sucsessfull, launching MainActivity");

                    } else {
                        Toast.makeText(LoginActivity.this, "Login failed", Toast.LENGTH_LONG).show();
                        Log.d(TAG, "Sign in error");

                    }
                }
            });
        }
    }
}
