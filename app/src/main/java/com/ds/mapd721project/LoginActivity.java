package com.ds.mapd721project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    EditText email, password;
    Button register, login;
    FirebaseAuth mAuth;
    FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        register = findViewById(R.id.register);
        login = findViewById(R.id.login);

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser mUser = mAuth.getCurrentUser();
                if(mUser != null)
                {
                    Toast.makeText(LoginActivity.this, "You are logged in", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(LoginActivity.this, HomeScreen.class));
                }
                else
                {
                    Toast.makeText(LoginActivity.this, "Please Login", Toast.LENGTH_SHORT).show();
                }
            }
        };

    }

    public void login(View view) {
        String emailID = email.getText().toString();
        String pass = password.getText().toString();

        if(emailID.isEmpty())
        {
            email.setError("Please Enter Email");
            email.requestFocus();
        }
        else if(pass.isEmpty())
        {
            password.setError("Please Enter Password");
            password.requestFocus();
        }
        else if(emailID.isEmpty() && pass.isEmpty())
        {
            Toast.makeText(this, "Fields are Empty", Toast.LENGTH_SHORT).show();
        }
        else if(!(emailID.isEmpty() && pass.isEmpty()))
        {
            mAuth.signInWithEmailAndPassword(emailID, pass).addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(!task.isSuccessful())
                    {
                        Toast.makeText(LoginActivity.this, "Login Error. Please Login Again", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        startActivity(new Intent(LoginActivity.this, HomeScreen.class));
                    }
                }
            });
        }
        else
        {
            Toast.makeText(LoginActivity.this, "Some Error Occurred. Please try again", Toast.LENGTH_SHORT).show();
        }
    }


    public void register(View view) {
        startActivity(new Intent(LoginActivity.this, MainActivity.class));
    }


    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }
}
