package com.example.musicapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ProfileActivity extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST = 1;
    private FirebaseAuth mAuth;
    private ImageView imgUser;
    private EditText edtUserName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser != null) {
            String userName = currentUser.getDisplayName();
            String userEmail = currentUser.getEmail();

            TextView txtUserEmail = findViewById(R.id.txtUserEmail);
            imgUser = findViewById(R.id.imgUser);
            edtUserName = findViewById(R.id.edtUserName);

            edtUserName.setText(userName != null ? userName : "User Name");
            txtUserEmail.setText(userEmail != null ? userEmail : "User Email");

            imgUser.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, PICK_IMAGE_REQUEST);
                }
            });

            if (currentUser.getPhotoUrl() != null) {
                Glide.with(this).load(currentUser.getPhotoUrl()).into(imgUser);
            }
        } else {
            Intent intent = new Intent(ProfileActivity.this, DangNhap.class);
            startActivity(intent);
            finish();
        }

        Button btnLogout = findViewById(R.id.btnLogout);
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                Intent intent = new Intent(ProfileActivity.this, TrangChu.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        });

        Button btnUpdate = findViewById(R.id.btnUpdate);
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateUserName();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri uri = data.getData();

            // Update profile image
            FirebaseUser currentUser = mAuth.getCurrentUser();
            if (currentUser != null) {
                UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                        .setPhotoUri(uri)
                        .build();

                currentUser.updateProfile(profileUpdates)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    // Load the new image using Glide
                                    Glide.with(ProfileActivity.this).load(uri).into(imgUser);

                                    // Update profile image
                                    updateProfileImageUrl(uri.toString());

                                    // Update profile image in trang chu
                                    updateMainPageProfileImage(uri.toString());

                                    updateLibraryProfileImage(uri.toString());
                                } else {
                                    Toast.makeText(ProfileActivity.this, "Failed to update profile photo", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        }
    }

    private void updateProfileImageUrl(String imageUrl) {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();
            DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("users").child(userId);

            // Save profile image URL to Realtime Database
            userRef.child("profileImageUrl").setValue(imageUrl)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(ProfileActivity.this, "Profile photo URL saved successfully", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(ProfileActivity.this, "Failed to save profile photo URL", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }

    private void updateUserName() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            String newUserName = edtUserName.getText().toString().trim();
            if (!newUserName.isEmpty()) {
                UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                        .setDisplayName(newUserName)
                        .build();

                currentUser.updateProfile(profileUpdates)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(ProfileActivity.this, "User name updated successfully", Toast.LENGTH_SHORT).show();

                                    // Update user name
                                    DatabaseReference userRef = FirebaseDatabase.getInstance().getReference()
                                            .child("users").child(currentUser.getUid());
                                    userRef.child("displayName").setValue(newUserName)
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {

                                                    } else {

                                                    }
                                                }
                                            });
                                } else {
                                    Toast.makeText(ProfileActivity.this, "Failed to update user name", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            } else {
                Toast.makeText(ProfileActivity.this, "Please enter a new user name", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void updateMainPageProfileImage(String imageUrl) {
        Intent intent = new Intent(ProfileActivity.this, TrangChu.class);
        intent.putExtra("profileImageUrl", imageUrl);
        startActivity(intent);
    }
    private void updateLibraryProfileImage(String imageUrl) {
        Intent intent = new Intent(ProfileActivity.this, LibraryActivity.class);
        intent.putExtra("profileImageUrl", imageUrl);
        startActivity(intent);
    }
}
