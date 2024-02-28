package com.manuni.postacceptdeny.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.manuni.postacceptdeny.R;
import com.manuni.postacceptdeny.databinding.PostPermissionBinding;
import com.manuni.postacceptdeny.models.PostModel;
import com.manuni.postacceptdeny.models.User;
import com.squareup.picasso.Picasso;

import java.util.List;

public class PostPermissionAdapter extends RecyclerView.Adapter<PostPermissionAdapter.PostPermissionViewHolder> {
    private Context context;
    private List<PostModel> list;

    public PostPermissionAdapter(Context context, List<PostModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public PostPermissionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.post_permission, parent, false);
        return new PostPermissionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostPermissionViewHolder holder, int position) {
        PostModel data = list.get(position);
        try {
            Picasso.get().load(data.getPostImage()).placeholder(R.drawable.impl1).into(holder.binding.postImage);
        } catch (Exception e) {
            e.printStackTrace();
        }
        holder.binding.date.setText(data.getDate());
        holder.binding.time.setText(data.getTime());
        holder.binding.postDes.setText(data.getPostDescription());

        holder.binding.denyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseDatabase.getInstance().getReference().child("posts").child(data.getPostId()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(context, "Post deleted successfully!", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        holder.binding.deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseDatabase.getInstance().getReference().child("acceptedPost").child(data.getPostId()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        FirebaseDatabase.getInstance().getReference().child("posts").child(data.getPostId()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(context, "Deleted from everywhere!", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
            }
        });
        FirebaseDatabase.getInstance().getReference().child("Users").child(data.getPostedBy()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    User user = snapshot.getValue(User.class);
                    holder.binding.posterName.setText(user.getUserName());
                    holder.binding.posterProfession.setText(user.getUserProfession());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        });
        FirebaseDatabase.getInstance().getReference().child("acceptedPost").child(data.getPostId()).child("postRecognizing").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    holder.binding.denyBtn.setVisibility(View.GONE);
                    holder.binding.deleteBtn.setVisibility(View.VISIBLE);
                    holder.binding.accept.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.accept_btn_disable));
                    holder.binding.accept.setTextColor(context.getResources().getColor(R.color.gray));
                    holder.binding.accept.setText(R.string.accepted);
                    holder.binding.accept.setEnabled(false);
                } else {
                    holder.binding.accept.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            FirebaseDatabase.getInstance().getReference().child("posts").child(data.getPostId()).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if (snapshot.exists()) {
                                        PostModel postModel = snapshot.getValue(PostModel.class);

                                        FirebaseDatabase.getInstance().getReference().child("acceptedPost").child(postModel.getPostId()).setValue(postModel).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Toast.makeText(context, "Post granted", Toast.LENGTH_SHORT).show();
                                                FirebaseDatabase.getInstance().getReference().child("acceptedPost").child(postModel.getPostId()).child("postRecognizing").setValue(postModel.getPostedBy()).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {

                                                       // holder.binding.accept.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.accept_btn_disable));
                                                       // holder.binding.accept.setTextColor(context.getResources().getColor(R.color.gray));
                                                       // holder.binding.accept.setText(R.string.accepted);
                                                        holder.binding.accept.setEnabled(false);
                                                        holder.binding.accept.setVisibility(View.GONE);
//                                                        holder.binding.accept.setVisibility(View.GONE);
                                                        holder.binding.deleteBtn.setVisibility(View.VISIBLE);
                                                        holder.binding.denyBtn.setVisibility(View.INVISIBLE);
                                                    }
                                                });

                                            }
                                        });
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                        }
                    });
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

    public class PostPermissionViewHolder extends RecyclerView.ViewHolder {
        PostPermissionBinding binding;

        public PostPermissionViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = PostPermissionBinding.bind(itemView);
        }
    }
}
