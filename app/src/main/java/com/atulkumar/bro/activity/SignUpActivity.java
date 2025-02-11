package com.atulkumar.bro.activity;


import static android.widget.Toast.*;

import android.content.Intent;
import android.os.Bundle;

import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.atulkumar.bro.R;
import com.atulkumar.bro.model.UserModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class SignUpActivity extends AppCompatActivity {

    private static final String TAG = "SignUpActivity";
    private String name, email,password,repass;
    private FirebaseAuth mAuth;
    private ProgressBar progressBar;
    private Button signUpButton;
    private EditText emailEditText,passwordEditText,repasswordEditText,nameEditText;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        mAuth = FirebaseAuth.getInstance();
         emailEditText = findViewById(R.id.signupEmail);
          passwordEditText = findViewById(R.id.signupPass);
          repasswordEditText = findViewById(R.id.signup_rePass);
          nameEditText = findViewById(R.id.name);
        signUpButton = findViewById(R.id.signup);
        progressBar=findViewById(R.id.progresbar);



        signUpButton.setOnClickListener(v -> {
            email = emailEditText.getText().toString();
            password = passwordEditText.getText().toString().trim();
            name = nameEditText.getText().toString();
            repass=repasswordEditText.getText().toString().trim();
            progressBar.setVisibility(View.VISIBLE);
            signUpButton.setVisibility(View.GONE);

                if (TextUtils.isEmpty(name)){
                    nameEditText.setError("please enter name");
                    nameEditText.requestFocus();
                    return;
                }
                if (TextUtils.isEmpty(email)){
                    emailEditText.setError("please enter email");
                    emailEditText.requestFocus();
                    return;
                }
                if (TextUtils.isEmpty(password)){
                    passwordEditText.setError("please enter password");
                    passwordEditText.requestFocus();
                    return;
                }
                    signUp();

        });
    }

    private void signUp(){
        mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(this, task -> {

            if (task.isSuccessful()) {
                progressBar.setVisibility(View.GONE);
                signUpButton.setVisibility(View.VISIBLE);
                saveData();
                makeText(SignUpActivity.this,"User created successfully",LENGTH_LONG).show();
                startActivity(new Intent(this,SignInActivity.class));
                finish();

            } else {
                // If sign in fails, display a message to the user.
                Log.w(TAG, "createUserWithEmail:failure", task.getException());
                makeText(SignUpActivity.this, "Authentication failed.",
                        LENGTH_SHORT).show();

            }

        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, e.getMessage());
                progressBar.setVisibility(View.GONE);
                signUpButton.setVisibility(View.VISIBLE);
                makeText(SignUpActivity.this,e.getMessage(),LENGTH_LONG).show();
            }
        });
    }

    private void saveData() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference usersRef  = database.getReference("User");
        UserModel user=new UserModel(name,email,null);
        String userid=mAuth.getCurrentUser().getUid();
        usersRef.child(userid).setValue(user).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                emailEditText.setText("");
                passwordEditText.setText("");
            }
        });
    }
}
