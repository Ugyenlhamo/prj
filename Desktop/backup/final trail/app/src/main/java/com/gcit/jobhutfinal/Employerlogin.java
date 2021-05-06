package com.gcit.jobhutfinal;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class Employerlogin extends AppCompatActivity implements View.OnClickListener {
    private TextView loginE, forgotPasswordE;
    private ProgressBar progressE;
    private EditText emailE, passwordE,licenseE;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employerlogin);

        mAuth = FirebaseAuth.getInstance();
        licenseE=(EditText)findViewById(R.id.licenseNoE);
        emailE = (EditText) findViewById(R.id.editTextTextEmailAddressE);
        passwordE = (EditText) findViewById(R.id.editTextTextPasswordE);
        progressE = (ProgressBar) findViewById(R.id.progressE);


        loginE = (Button) findViewById(R.id.loginE);
        loginE.setOnClickListener(this);
        forgotPasswordE = (TextView) findViewById(R.id.forgetPasswordE);
        forgotPasswordE.setOnClickListener(this);
    }

    public void jobseeker(View view) {
        Intent job= new Intent(this, com.gcit.jobhutfinal.cidpush.class);
        startActivity(job);
    }

    public void employer(View view) {
        Intent employe= new Intent(this, com.gcit.jobhutfinal.EmployerActivity.class);
        Log.d("Error","this is login");
        startActivity(employe);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.loginE:
                loginUserA();
                break;
            case R.id.forgetPasswordE:
                startActivity(new Intent(this, forgetPassword.class));
                break;


        }
    }

    public void loginUserA() {
        String emailL = emailE.getText().toString().trim();
        String passwordL = passwordE.getText().toString().trim();
        String linn=licenseE.getText().toString().trim();
        if (emailL.isEmpty()) {
            emailE.setError("Email Required!");
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(emailL).matches()) {
            emailE.setError("Please Enter Valid Email");
            return;

        }
        if (passwordL.isEmpty()) {
            passwordE.setError("Password is required!");
            return;
        }

        progressE.setVisibility(View.VISIBLE);
        mAuth.signInWithEmailAndPassword(emailL, passwordL).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    if (user.isEmailVerified()) {
                        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Employer");
                        Query check = databaseReference.orderByChild("license").equalTo(linn);
                        check.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (snapshot.exists()) {
                                    licenseE.setError(null);
                                    licenseE.setEnabled(false);
                                    startActivity(new Intent(Employerlogin.this, Home.class));


                                } else {
                                    licenseE.setError("CID didn't matched OurDatabase");
                                    licenseE.requestFocus();
                                    return;
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

                    } else {
                        user.sendEmailVerification();
                        Toast.makeText(Employerlogin.this, "As you are new to Jobhut, Please Check your email and verify your account!", Toast.LENGTH_LONG).show();
                    }


                } else {
                    Toast.makeText(Employerlogin.this, "Fail to login! Check your Credentials", Toast.LENGTH_LONG).show();
                }
            }
        });

    }


    public void Employerlogin(View view) {
        startActivity(new Intent(Employerlogin.this, Employerlogin.class));

    }

    public void Jobseekerlogin(View view) {
        startActivity(new Intent(Employerlogin.this, MainActivity.class));
    }
}




