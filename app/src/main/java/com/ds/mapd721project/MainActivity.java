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

public class MainActivity extends AppCompatActivity {

    EditText email, password;
    Button register, login;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Get instance of FirebaseAuth Class
        mAuth = FirebaseAuth.getInstance();
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        register = findViewById(R.id.register);
        login = findViewById(R.id.login);

    }

    //Function to register a new user
    public void register(View view) {
        String emailID = email.getText().toString();
        String pass = password.getText().toString();

        //Prompt the user to fill out all fields
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
            //Create new user with entered Email and Password, add a listener for completion
            mAuth.createUserWithEmailAndPassword(emailID, pass).addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    //if task result came out false, notify user, else login
                    if(!task.isSuccessful())
                    {
                        Toast.makeText(MainActivity.this, "SignUp unsuccessfull. Please try again", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        startActivity(new Intent(MainActivity.this, HomeScreen.class));
                    }
                }
            });
        }
        else
        {
            Toast.makeText(MainActivity.this, "Some Error Occurred. Please try again", Toast.LENGTH_SHORT).show();
        }
    }


    public void login(View view) {
        startActivity(new Intent(MainActivity.this, LoginActivity.class));
    }
}
