package com.gcit.jobhutfinal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class forgetPassword extends AppCompatActivity {
    private EditText emailEditText;
    private Button resetPasswordButton;
    private ProgressBar progressBar;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);

        emailEditText=findViewById(R.id.email);
        resetPasswordButton=findViewById(R.id.resetpassword);
        progressBar=findViewById(R.id.progressBar);
        auth=FirebaseAuth.getInstance();
    }

    public void boss(View view) {
        resetpassword();
    }
    private void resetpassword(){
        String email=emailEditText.getText().toString().trim();
        if(email.isEmpty()){
            emailEditText.setError("Email is required");
            emailEditText.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches())
        {
            emailEditText.setError("Please provide valid email!");
            emailEditText.requestFocus();
            return;
        }
        progressBar.setVisibility(View.VISIBLE);
        auth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
               if(task.isSuccessful()){
                   Toast.makeText(forgetPassword.this, "Check your email to reset your password", Toast.LENGTH_SHORT).show();
               }
               else
               {
                   Toast.makeText(forgetPassword.this, "Try again! Something wrong happened", Toast.LENGTH_SHORT).show();
               }
            }
        });
    }
}