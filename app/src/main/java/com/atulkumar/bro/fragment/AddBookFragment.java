package com.atulkumar.bro.fragment;

import static android.app.Activity.RESULT_OK;

import static com.atulkumar.bro.R.*;
import static com.atulkumar.bro.R.array.*;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import com.atulkumar.bro.R;
import com.atulkumar.bro.activity.HomeActivity;
import com.atulkumar.bro.model.BookModel;
import com.atulkumar.bro.model.UserModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;



public class AddBookFragment extends Fragment {
    private static final int PICK_IMAGE_REQUEST = 1;
    private static final int REQUEST_IMAGE_CAPTURE = 2;
    private Spinner firstSpinner;
    private Spinner secondSpinner;
    private EditText bookName, publication, description;
    private ImageView bookImage;
    private Button uploadImage, saveData;
    private Uri imageUri;
    private DatabaseReference databaseReference;
    private StorageReference storageReference;
    private FirebaseAuth mAuth;
   private TextView textView,book,errortext;
    private boolean isfisrstspineerInitial = true;
   UserModel userModel=new UserModel();
   private  String userId,username;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(layout.fragment_add_book, container, false);


        bookName = view.findViewById(id.book_name_edit_text);
        publication = view.findViewById(id.publication_edit_text);
        description = view.findViewById(id.description_edit_text);
        book=view.findViewById(id.book);
        textView=view.findViewById(id.book);
        bookImage = view.findViewById(id.bookimage);
        uploadImage = view.findViewById(id.uploadImage);
        saveData = view.findViewById(id.saveData);
        firstSpinner = view.findViewById(id.first_spinner);
        secondSpinner = view.findViewById(id.second_spinner);
        errortext=view.findViewById(id.errorText);
        //
        ((HomeActivity) getActivity()).hideBottomNavigation();

        mAuth = FirebaseAuth.getInstance();


        uploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openImageChooser();
            }
        });
        saveData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveBookData();
            }
        });

        setupFirstSpinner();

        return  view;
    }

    private void saveBookData() {

        final String bookNameStr = bookName.getText().toString().trim();
        final String publicationStr = publication.getText().toString().trim();
        final String decriptionStr = description.getText().toString().trim();
        final String yearOrSemesterStr = secondSpinner.getSelectedItem().toString().trim();


        if (TextUtils.isEmpty(bookNameStr)){
            bookName.setError("please enter book name");
            bookName.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(publicationStr)){
            publication.setError("please enter book name");
            publication.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(decriptionStr)){
            description.setError("please enter book name");
            description.requestFocus();
            return;
        }


        if (imageUri != null) {
            final ProgressDialog progressDialog = new ProgressDialog(getContext());
            progressDialog.setTitle("Uploading...");
            progressDialog.setIcon(drawable.logo);

            progressDialog.show();
            databaseReference = FirebaseDatabase.getInstance().getReference();
            storageReference = FirebaseStorage.getInstance().getReference("book_images");

            final StorageReference fileReference = storageReference.child(System.currentTimeMillis() + ".jpg");
            fileReference.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>(){

                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            progressDialog.dismiss();
                            String imageUrl = uri.toString();
                            FirebaseUser user= mAuth.getCurrentUser();
                            userId=user.getUid();

                            String bookId = databaseReference.child("User").child(userId).child("books").push().getKey();
                            BookModel book = new BookModel(bookNameStr,publicationStr,yearOrSemesterStr,imageUrl,decriptionStr,bookId,userId);
                            assert bookId != null;
                            databaseReference.child("User").child(userId).child("books").child(bookId).setValue(book);
                            Log.d("tt",userId);
                            bookName.setText(username);
                            publication.setText("");
                            description.setText("");
                            bookImage.setImageURI(null);

//                            getParentFragmentManager().beginTransaction().replace(R.id.main_container,new ProfileFragment()).commit();
                            Toast.makeText(getContext(), "Book saved successfully", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    progressDialog.dismiss();

                    Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(getContext(), "Please select an image", Toast.LENGTH_SHORT).show();
        }

    }

    private void openImageChooser() {
        CharSequence[] items = {"Take Photo", "Choose from Gallery"};
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Pick Image");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (item == 0) {
                    openCamera();
                } else if (item == 1) {
                    openGallery();
                }
            }
        });
        builder.show();
    }
    private void openCamera() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(requireActivity().getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK) {
            assert data != null;
            imageUri = data.getData();
            bookImage.setImageURI(imageUri);

        } else if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            assert data != null;
            Bundle extras = data.getExtras();
            assert extras != null;
            Bitmap imageBitmap = (Bitmap) extras.get("data");

            bookImage.setImageBitmap(imageBitmap);


        }
    }

    private void setupFirstSpinner() {
        ArrayAdapter<CharSequence> firstAdapter = ArrayAdapter.createFromResource(
                getActivity(), semester_year, android.R.layout.simple_spinner_item);
        firstAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        firstSpinner.setAdapter(firstAdapter);

        firstSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (isfisrstspineerInitial){
                    isfisrstspineerInitial=false;
                    return;
                }
                Log.d("SpinnerFragment", "First spinner selected position: " + position);
                if (position == 0) {
                    secondSpinner.setAdapter(null);
                    Toast.makeText(getActivity(), "please select valid option", Toast.LENGTH_SHORT).show();
                } else if (position == 1) {
                    setupSecondSpinner(array.year);

                } else if (position==2) {
                    setupSecondSpinner(array.semester);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                secondSpinner.setAdapter(null);
                firstSpinner.setAdapter(null);

            }
        });
    }

    private void setupSecondSpinner(int arrayResId) {
        ArrayAdapter<CharSequence> secondAdapter = ArrayAdapter.createFromResource(
                getActivity(), arrayResId, android.R.layout.simple_spinner_item);
        secondAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        secondSpinner.setAdapter(secondAdapter);
        Log.d("SpinnerFragment", "Second spinner set with array resource ID: " + arrayResId);


    }


}
