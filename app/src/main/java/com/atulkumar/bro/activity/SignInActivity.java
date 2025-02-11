package com.atulkumar.bro.activity;


import static android.widget.Toast.LENGTH_LONG;
import static android.widget.Toast.makeText;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.atulkumar.bro.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;


public class SignInActivity extends AppCompatActivity {

    private static final String TAG = "SignInActivity";
    private String email,password;
    private FirebaseAuth mAuth;
    private EditText emailEditText, passwordEditText;
    private ProgressBar progressBar;
   private Button signInButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        mAuth=FirebaseAuth.getInstance();
        progressBar=findViewById(R.id.progressbar2);
        emailEditText = findViewById(R.id.signinEmail);
        passwordEditText = findViewById(R.id.signinPass);
        signInButton = findViewById(R.id.signin);
        TextView textView=findViewById(R.id.txtregister);
        textView.setOnClickListener(v -> {
            startActivity(new Intent(this,SignUpActivity.class));
        });

        signInButton.setOnClickListener(v -> {
              email = emailEditText.getText().toString();
              password = passwordEditText.getText().toString();
              signInButton.setVisibility(View.GONE);
              progressBar.setVisibility(View.VISIBLE);
              signin();

        });
    }

    private void signin() {
          mAuth.signInWithEmailAndPassword(email,password)
                  .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                      @Override
                      public void onComplete(@NonNull Task<AuthResult> task) {
                          if (task.isSuccessful()) {
                              Log.d(TAG, "signInWithEmail:success");
                              Toast.makeText(SignInActivity.this, "Authentication Successful", Toast.LENGTH_SHORT).show();
                               progressBar.setVisibility(View.GONE);
                               signInButton.setVisibility(View.VISIBLE);
                               saveLoginState(true);
                              startActivity(new Intent(SignInActivity.this,HomeActivity.class));
                              finish();
                          } else {
                              Log.w(TAG, "signInWithEmail:failure", task.getException());
                              Toast.makeText(SignInActivity.this, "Authentication failed: " + task.getException().getMessage(),
                                      Toast.LENGTH_SHORT).show();
                          }
                      }
                  }).addOnFailureListener(new OnFailureListener() {
                      @Override
                      public void onFailure(@NonNull Exception e) {
                          progressBar.setVisibility(View.GONE);
                          signInButton.setVisibility(View.VISIBLE);
                          makeText(SignInActivity.this,e.getMessage(),LENGTH_LONG).show();
                      }
                  });
    }
    private void saveLoginState(boolean isLoggedIn) {
        SharedPreferences prefs = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean("isLoggedIn", isLoggedIn);
        editor.apply();
    }
}
