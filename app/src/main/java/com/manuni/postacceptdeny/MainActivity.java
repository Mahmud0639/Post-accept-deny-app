package com.manuni.postacceptdeny;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.manuni.postacceptdeny.activities.AcceptedPostActivity;
import com.manuni.postacceptdeny.activities.LoginActivity;
import com.manuni.postacceptdeny.activities.PostActivity;
import com.manuni.postacceptdeny.activities.PostPermissionActivity;
import com.manuni.postacceptdeny.databinding.ActivityMainBinding;
import com.manuni.postacceptdeny.models.PostModel;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        auth = FirebaseAuth.getInstance();
        binding.allDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PostModel model = new PostModel();
                FirebaseDatabase.getInstance().getReference().child("acceptedPost").removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        FirebaseDatabase.getInstance().getReference().child("posts").removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(MainActivity.this, "All post has been deleted!", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
            }
        });

        binding.showWhoWantPermission.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, PostPermissionActivity.class);
                startActivity(intent);
            }
        });
        binding.createPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(MainActivity.this, PostActivity.class);
                startActivity(intent1);
            }
        });
        binding.logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                auth.signOut();
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
                finishAffinity();
            }
        });
        binding.showPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AcceptedPostActivity.class);
                startActivity(intent);
            }
        });
    }
}