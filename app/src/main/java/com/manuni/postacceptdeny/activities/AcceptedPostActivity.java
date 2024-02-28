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
import com.manuni.postacceptdeny.R;
import com.manuni.postacceptdeny.adapters.PostAcceptedAdapter;
import com.manuni.postacceptdeny.databinding.ActivityAcceptedPostBinding;
import com.manuni.postacceptdeny.models.PostModel;

import java.util.ArrayList;
import java.util.List;

public class AcceptedPostActivity extends AppCompatActivity {
    ActivityAcceptedPostBinding binding;
    private DatabaseReference databaseReference;
    private List<PostModel> list;
    private PostAcceptedAdapter acceptedAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAcceptedPostBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        databaseReference = FirebaseDatabase.getInstance().getReference();

        databaseReference.child("acceptedPost").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list = new ArrayList<>();
                list.clear();
                for (DataSnapshot dataSnapshot: snapshot.getChildren()){
                   PostModel model = dataSnapshot.getValue(PostModel.class);
                   list.add(0,model);
                }
                binding.acceptedPostRV.setHasFixedSize(true);
                acceptedAdapter = new PostAcceptedAdapter(AcceptedPostActivity.this,list);
                binding.acceptedPostRV.setLayoutManager(new LinearLayoutManager(AcceptedPostActivity.this));
                binding.acceptedPostRV.setAdapter(acceptedAdapter);
                acceptedAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }
}