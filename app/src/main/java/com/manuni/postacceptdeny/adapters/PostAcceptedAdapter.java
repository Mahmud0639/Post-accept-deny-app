package com.manuni.postacceptdeny.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.manuni.postacceptdeny.R;
import com.manuni.postacceptdeny.databinding.PostSampleBinding;
import com.manuni.postacceptdeny.models.PostModel;
import com.manuni.postacceptdeny.models.User;
import com.squareup.picasso.Picasso;

import java.util.List;

public class PostAcceptedAdapter extends RecyclerView.Adapter<PostAcceptedAdapter.PostAcceptedViewHolder> {
    private Context context;
    private List<PostModel> list;

    public PostAcceptedAdapter(Context context, List<PostModel> list){
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public PostAcceptedViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.post_sample,parent,false);
        return new PostAcceptedViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostAcceptedViewHolder holder, int position) {
       PostModel model = list.get(position);
        try {
            Picasso.get().load(model.getPostImage()).placeholder(R.drawable.impl1).into(holder.binding.postImage);
        } catch (Exception e) {
            e.printStackTrace();
        }
        holder.binding.date.setText(model.getDate());
        holder.binding.time.setText(model.getTime());
        holder.binding.postDesc.setText(model.getPostDescription());
        FirebaseDatabase.getInstance().getReference().child("Users").child(model.getPostedBy()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    User user = snapshot.getValue(User.class);
                    holder.binding.posterName.setText(user.getUserName());
                    holder.binding.posterProfession.setText(user.getUserProfession());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class PostAcceptedViewHolder extends RecyclerView.ViewHolder {
        PostSampleBinding binding;

        public PostAcceptedViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = PostSampleBinding.bind(itemView);
        }
    }
}
