package com.atulkumar.bro.fragment;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.atulkumar.bro.R;
import com.atulkumar.bro.model.UserModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import de.hdodenhof.circleimageview.CircleImageView;


public class UpdateProfileFragment extends Fragment {
    private static final int REQUEST_CODE_PICK_IMAGE = 101;
        private CircleImageView profileImg;
        private EditText username,email;
        private MaterialButton update;
        private String name,emailid,userId;
        private Uri selectedImageUri;
        private DatabaseReference databaseReference;
        private StorageReference storageReference;
        private ProgressBar progressBar;
        private ImageButton backbtn;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_update_profile, container, false);
        profileImg=view.findViewById(R.id.profile_img);
        username=view.findViewById(R.id.user);
        email=view.findViewById(R.id.email);
        update=view.findViewById(R.id.btnupdate_profile);
        progressBar=view.findViewById(R.id.progresbar3);
        backbtn=view.findViewById(R.id.udtback);

        backbtn.setOnClickListener(v -> {
            getParentFragmentManager().popBackStack();
        });
        userId=FirebaseAuth.getInstance().getCurrentUser().getUid();
        if(getArguments()!=null){
          name=getArguments().getString("USER");
          emailid=getArguments().getString("EMAIL");
        }
        username.setText(name);
        email.setText(emailid);
        profileImg.setOnClickListener(v -> {
            pickImage();
        });
        update.setOnClickListener(v -> {
            updateprofile();
        });
        return view;
    }

    private void updateprofile() {
        update.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
        if (selectedImageUri!=null){
            databaseReference = FirebaseDatabase.getInstance().getReference();
            storageReference = FirebaseStorage.getInstance().getReference("Profile_img");
            final StorageReference fileReference = storageReference.child( userId+ ".jpg");
            fileReference.putFile(selectedImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            String imageUrl=uri.toString();
                            String edtuser,edtemail;
                            edtuser=username.getText().toString();
                            edtemail=email.getText().toString();
                            if (edtuser.isEmpty()&&edtemail.isEmpty()){
                                return;
                            }
                            UserModel userModel=new UserModel(edtuser,edtemail,imageUrl);
                            databaseReference.child("User").child(userId).setValue(userModel).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    progressBar.setVisibility(View.GONE);
                                    update.setVisibility(View.VISIBLE);
                                    username.setText("");
                                    email.setText("");

                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    });
                }
            });
        }
    }

    private void pickImage() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, REQUEST_CODE_PICK_IMAGE);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_PICK_IMAGE && resultCode == RESULT_OK && data != null) {
            selectedImageUri = data.getData();
            if (selectedImageUri != null) {
                // Handle the image URI (e.g., display it in an ImageView)
                profileImg.setImageURI(selectedImageUri);
            }
        }
    }
}