package com.ranairu.creation;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;


public class Login extends AppCompatActivity {

    private Button btSigIn;
    private EditText etEmail;
    private EditText etPassword;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ini_login);


        btSigIn = findViewById(R.id.bt_signin);
        etEmail = findViewById(R.id.et_email);
        etPassword = findViewById(R.id.et_password);
        mAuth = FirebaseAuth.getInstance();
        btSigIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });
//        FirebaseMessaging.getInstance().subscribeToTopic("all");

        Button btnlogout = findViewById(R.id.logout);
        View Lbelum = findViewById(R.id.belumlogin);
        View Lsudah = findViewById(R.id.sudahlogin);

        mAuth = FirebaseAuth.getInstance();
        //sudah login
        if (mAuth.getCurrentUser() != null){
Lbelum.setVisibility(View.GONE);
            btnlogout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mAuth.signOut();
                    finish();
                    Intent form2 = new Intent(Login.this,MainActivity.class);
                    startActivity(form2);

                }
            });

            //belum login
        } else {
            Lsudah.setVisibility(View.GONE);
        }

    }

    private void signIn(){

        mAuth.signInWithEmailAndPassword(etEmail.getText().toString(),etPassword.getText().toString()).addOnCompleteListener(this, new
                OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        {
                            if(task.isSuccessful()){
                                Intent form2 = new Intent(Login.this,MainActivity.class);
                                startActivity(form2);
                            } else {
                                Toast.makeText(Login.this,"Login Failed", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
    }

    public void register(View view) {
        Intent intent = new Intent(Login.this,Register.class);
        startActivity(intent);
    }

}