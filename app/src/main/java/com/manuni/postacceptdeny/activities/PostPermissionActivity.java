package com.manuni.postacceptdeny.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.manuni.postacceptdeny.adapters.PostPermissionAdapter;
import com.manuni.postacceptdeny.databinding.ActivityPostPermissionBinding;
import com.manuni.postacceptdeny.models.PostModel;

import java.util.ArrayList;
import java.util.List;

public class PostPermissionActivity extends AppCompatActivity {
    ActivityPostPermissionBinding binding;
    private DatabaseReference databaseReference;
    private List<PostModel> list;
    private PostPermissionAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPostPermissionBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child("posts").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list = new ArrayList<>();
                list.clear();
              for (DataSnapshot dataSnapshot: snapshot.getChildren()){
                 PostModel data = dataSnapshot.getValue(PostModel.class);
                 list.add(0,data);
              }
              adapter = new PostPermissionAdapter(PostPermissionActivity.this,list);
              binding.postPermitRV.setLayoutManager(new LinearLayoutManager(PostPermissionActivity.this));
              binding.postPermitRV.setHasFixedSize(true);
              adapter.notifyDataSetChanged();
              binding.postPermitRV.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }
}