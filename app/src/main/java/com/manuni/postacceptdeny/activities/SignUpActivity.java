package com.manuni.postacceptdeny.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.manuni.postacceptdeny.MainActivity;
import com.manuni.postacceptdeny.R;
import com.manuni.postacceptdeny.databinding.ActivitySignUpBinding;
import com.manuni.postacceptdeny.models.User;

public class SignUpActivity extends AppCompatActivity {
    ActivitySignUpBinding binding;
    private FirebaseAuth auth;
    private DatabaseReference databaseReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        auth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();

        binding.logIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        binding.signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkValidation();
            }
        });
    }
    private void checkValidation(){
        String name = binding.nameET.getText().toString().trim();
        String profession = binding.professionET.getText().toString().trim();
        String email = binding.emailET.getText().toString().trim();
        String password = binding.passwordET.getText().toString().trim();

        if (name.isEmpty()){
            binding.nameET.setError("Name required");
            binding.nameET.requestFocus();
        }else if (profession.isEmpty()){
            binding.professionET.setError("Profession required");
            binding.professionET.requestFocus();
        }else if (email.isEmpty()){
            binding.emailET.setError("Email required");
            binding.emailET.requestFocus();
        }else if (password.isEmpty()){
            binding.passwordET.setError("Password required");
            binding.passwordET.requestFocus();
        }else {
            auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()){
                        String uid = task.getResult().getUser().getUid();
                        User user = new User(name,profession,email,password,uid);
                        databaseReference.child("Users").child(uid).setValue(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(SignUpActivity.this, "User created!", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
                                startActivity(intent);
                                finishAffinity();
                            }
                        });
                    }
                }
            });

        }
    }
}