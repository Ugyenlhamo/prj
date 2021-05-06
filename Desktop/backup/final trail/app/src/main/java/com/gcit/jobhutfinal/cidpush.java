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
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class cidpush extends AppCompatActivity implements View.OnClickListener {
    private FirebaseAuth mAuth;
    private EditText cids,lics;
    private Button register;
    FirebaseDatabase rootNode;
    DatabaseReference reference;
    FirebaseAuth firebaseAuth;


    private ProgressBar progres;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cidpush);
        mAuth = FirebaseAuth.getInstance();
        register = (Button) findViewById(R.id.submit);
        register.setOnClickListener(this);
        firebaseAuth = FirebaseAuth.getInstance();
        cids = (EditText) findViewById(R.id.cids);
        lics=(EditText) findViewById(R.id.lic) ;

        progres = (ProgressBar) findViewById(R.id.progress);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.submit:
                registerEm();
                break;
        }

    }

    private void registerEm() {
        String cid_no = cids.getText().toString().trim();
        String lic = lics.getText().toString().trim();


        if (cid_no.isEmpty()) {
            cids.setError("CID required!");
            cids.requestFocus();
            return;
        } else {
            rootNode = FirebaseDatabase.getInstance();
            reference = rootNode.getReference("Bhutan_cid");
            usercid userHelperClass = new usercid(cid_no,lic);
            reference.child(cid_no).setValue(userHelperClass);
            Toast.makeText(getApplicationContext(),"Added successfully",Toast.LENGTH_LONG).show();
            cids.setText("");
        }
    }
}






