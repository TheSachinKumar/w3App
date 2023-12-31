package com.example.whatsapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.whatsapp.Models.Users;
import com.example.whatsapp.databinding.ActivitySignUpBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class SignUpActivity extends AppCompatActivity {
    ActivitySignUpBinding binding;
    private FirebaseAuth auth;
    FirebaseDatabase database;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().hide();
        FirebaseApp.initializeApp(this);
        auth= FirebaseAuth.getInstance();

        database = FirebaseDatabase.getInstance();
        progressDialog = new ProgressDialog(SignUpActivity.this);
        progressDialog.setTitle("Creating Account");
        progressDialog.setMessage("We are creating your account");
        binding.btnSignUp.setOnClickListener(new View.OnClickListener() {

            @Override

            public void onClick(View v) {
                progressDialog.show();
                auth.createUserWithEmailAndPassword(binding.etEmail.getText().toString(),binding.etPassword.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressDialog.dismiss();
                        if(task.isSuccessful()){
                            Users user= new Users(binding.etuserName.getText().toString(),binding.etEmail.getText().toString(),binding.etPassword.getText().toString());
                            String id = task.getResult().getUser().getUid();
                            database.getReference().child("Users").child(id).setValue(user);
                            //TODO BELOW LINES ARE ALSO SELF SHIT ADDED TO ADD THE VERIFICATION CODE FUNCTIONALITY
                            auth.getCurrentUser().sendEmailVerification();
                            auth.signOut();
                            //TODO SELF SHIT=LENGTH_LONG
                            Toast.makeText(SignUpActivity.this,"User Created Successfully, check mail to verify", Toast.LENGTH_LONG).show();
                        }
                        else{
                            Toast.makeText(SignUpActivity.this,task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });
        binding.tvAlreadyAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignUpActivity.this,SignInActivity.class));

            }
        });

    }
}