package com.manuni.postacceptdeny.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.manuni.postacceptdeny.R;
import com.manuni.postacceptdeny.databinding.ActivityPostBinding;
import com.manuni.postacceptdeny.models.PostModel;
import com.manuni.postacceptdeny.models.User;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class PostActivity extends AppCompatActivity {
    ActivityPostBinding binding;

    private final int REQ = 1;
    private Uri imgUri;
    private Bitmap imgBitmap;
    private StorageReference storageReference, mStorageRef;
    private DatabaseReference databaseReference;
    private UploadTask uploadTask;
    private String downloadUrl = "";
    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPostBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        dialog = new ProgressDialog(this);
        dialog.setTitle("Please wait");
        dialog.setMessage("Uploading...");


        storageReference = FirebaseStorage.getInstance().getReference();
        databaseReference = FirebaseDatabase.getInstance().getReference();

        binding.addPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGallery();
            }
        });
        binding.postBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                check();
            }
        });
        binding.postDescribe.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
               if (!binding.postDescribe.getText().toString().isEmpty()){
                   binding.postBtn.setBackgroundDrawable(ContextCompat.getDrawable(PostActivity.this,R.drawable.post_btn_bg));
                   binding.postBtn.setTextColor(getResources().getColor(R.color.white));
                   binding.postBtn.setEnabled(true);
               }else {
                   binding.postBtn.setBackgroundDrawable(ContextCompat.getDrawable(PostActivity.this,R.drawable.accept_btn_disable));
                   binding.postBtn.setTextColor(getResources().getColor(R.color.gray));
                   binding.postBtn.setEnabled(false);
               }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


    }
    private void check(){
        if (binding.postDescribe.getText().toString().isEmpty()){
            Toast.makeText(this, "Write something", Toast.LENGTH_SHORT).show();
        } else if (imgBitmap == null) {
           gotoDatabase();
        }else {
            gotoStorage();
        }
    }
    private void gotoStorage(){
        dialog.show();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        imgBitmap.compress(Bitmap.CompressFormat.JPEG,50,baos);

        byte[] postImage = baos.toByteArray();
        mStorageRef = storageReference.child("post_images").child(postImage+"."+getFileExtension(imgUri));

       uploadTask = mStorageRef.putBytes(postImage);
       uploadTask.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
           @Override
           public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
               if (task.isSuccessful()){
                   uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                       @Override
                       public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                           mStorageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                               @Override
                               public void onSuccess(Uri uri) {
                                   downloadUrl = String.valueOf(uri);
                                   gotoDatabase();
                               }
                           });
                       }
                   });
               }
           }
       });

    }
    private String getFileExtension(Uri uri){
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(getContentResolver().getType(uri));
    }
    private void gotoDatabase(){
        dialog.show();
        PostModel model = new PostModel();
        model.setPostedBy(FirebaseAuth.getInstance().getUid());
        model.setPostDescription(binding.postDescribe.getText().toString());
        model.setPostId(databaseReference.push().getKey());
        model.setPostImage(downloadUrl);

        //date
        Calendar calForDate = Calendar.getInstance();
        SimpleDateFormat dateFormatForDate = new SimpleDateFormat("dd MMM, yy");
        String date = dateFormatForDate.format(calForDate.getTime());

        model.setDate(date);

        //time
        Calendar calForTime = Calendar.getInstance();
        SimpleDateFormat dateFormatForTime = new SimpleDateFormat("hh:mm a");
        String time = dateFormatForTime.format(calForTime.getTime());

        model.setTime(time);

        databaseReference.child("posts").child(model.getPostId()).setValue(model).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                dialog.dismiss();
                Toast.makeText(PostActivity.this, "Post uploaded!", Toast.LENGTH_SHORT).show();
                binding.postDescribe.setText("");
                binding.postImage.setImageBitmap(null);
            }
        });



    }
    private void openGallery(){
        Intent imgPick = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(imgPick,REQ);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQ && resultCode == RESULT_OK && data.getData() != null){
            imgUri = data.getData();

            try {
                imgBitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),imgUri);
            } catch (IOException e) {
                e.printStackTrace();
            }
            binding.postImage.setImageBitmap(imgBitmap);
        }
    }
}