package com.gcit.jobhutfinal;

import android.content.Intent;
import android.os.Bundle;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class jobseekerActivity extends AppCompatActivity implements View.OnClickListener{
    private FirebaseAuth mAuth;
    private EditText cid,name,email,password,confirm_passj;
    private TextView submit;
    private ProgressBar progres;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jobseeker);
        mAuth=FirebaseAuth.getInstance();
        submit=(Button)findViewById(R.id.submit);
        submit.setOnClickListener(this);
        firebaseAuth = FirebaseAuth.getInstance();

        cid=(EditText)findViewById(R.id.cidj);
        name=(EditText)findViewById(R.id.namej);
        email=(EditText)findViewById(R.id.emailj);
        password=(EditText)findViewById(R.id.passwordj);
        confirm_passj=(EditText)findViewById(R.id.confirm_passj);


        progres=(ProgressBar) findViewById(R.id.progressq);




    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.submit:
                registerJobseeker();
                break;
        }

    }
    private void registerJobseeker(){
        String  cid_noj=cid.getText().toString().trim();
        String  namej=name.getText().toString().trim();;
        String emailj=email.getText().toString().trim();
        String  passwordj=password.getText().toString().trim();
        String confirm=confirm_passj.getText().toString().trim();
        if(cid_noj.isEmpty()){
            cid.setError("CID required!");
            cid.requestFocus();
            return;
        }else{

            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Bhutan_cid");
            Query checkUser = databaseReference.orderByChild("cid").equalTo(cid_noj);

            checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        cid.setError(null);
                        cid.setEnabled(false);
                        if(namej.isEmpty()){
                            name.setError("Name required!");
                            name.requestFocus();
                            return;
                        }
                        if(emailj.isEmpty()){
                            email.setError("Email required!");
                            email.requestFocus();
                            return;
                        }
                        if(!Patterns.EMAIL_ADDRESS.matcher(emailj).matches()){
                            email.setError("Please provide valid email");
                            email.requestFocus();
                            return;

                        }

                        if(passwordj.isEmpty()){
                            password.setError("Password is required!");
                            password.requestFocus();
                            return;
                        }
                        if(passwordj.length()<6){
                            password.setError("Atleast provide Six characters!");
                        }
                        if(!confirm.equals(confirm)){
                            confirm_passj.setError("Password didn't matched!");
                            confirm_passj.requestFocus();
                            return;
                        }
                        progres.setVisibility(View.VISIBLE);
                        mAuth.createUserWithEmailAndPassword(emailj,passwordj)
                                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if(task.isSuccessful()){

                                            com.gcit.jobhutfinal.jobuser jobseeker=new com.gcit.jobhutfinal.jobuser(cid_noj,namej,emailj);
                                            FirebaseDatabase.getInstance().getReference("Jobseekers")
                                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                                    .setValue(jobseeker).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if(task.isSuccessful()){
                                                        Toast.makeText(com.gcit.jobhutfinal.jobseekerActivity.this,"User has been registered successfully!",Toast.LENGTH_LONG).show();
                                                        Intent ob=new Intent(getApplicationContext(),MainActivity.class);
                                                        startActivity(ob );
                                                    }else{
                                                        Toast.makeText(com.gcit.jobhutfinal.jobseekerActivity.this, "Failed to register! Try again!", Toast.LENGTH_LONG).show();
                                                    }
                                                    progres.setVisibility(View.GONE);
                                                }


                                            });


                                        }else{
                                            Toast.makeText(com.gcit.jobhutfinal.jobseekerActivity.this, "Failed to register! Try again!", Toast.LENGTH_LONG).show();
                                            progres.setVisibility(View.GONE);
                                        }
                                    }
                                });




                    } else {
                            cid.setError("CID didn't matched OurDatabase");
                            cid.requestFocus();
                        }
                    }


                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }


    }

}







