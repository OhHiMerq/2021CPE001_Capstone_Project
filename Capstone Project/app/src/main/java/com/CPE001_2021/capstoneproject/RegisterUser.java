package com.CPE001_2021.capstoneproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Locale;

public class RegisterUser extends AppCompatActivity {
    EditText userRegName, userEmail,userPassword;
    private Button regButton;
    private LoadingDialog ld;

    private FirebaseDatabase database;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);

        database = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();

        ld = new LoadingDialog(RegisterUser.this,"Registering User");
        regButton = (Button) findViewById(R.id.userRegButton);
        userRegName = (EditText) findViewById(R.id.userRegName);
        userEmail = (EditText) findViewById(R.id.userRegEmail);
        userPassword = (EditText) findViewById(R.id.userRegPassword);

        regButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = userRegName.getText().toString();
                String email = userEmail.getText().toString();
                String pass = userPassword.getText().toString();

                if (name.isEmpty()){
                    userRegName.setError("Username is Required!");
                    userRegName.requestFocus();
                    return;
                }

                if (email.isEmpty()){
                    userEmail.setError("Email is Required!");
                    userEmail.requestFocus();
                    return;
                }


                if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                    userEmail.setError("Invalid Email!");
                    userEmail.requestFocus();
                    return;
                }

                if (pass.isEmpty()){
                    userPassword.setError("Password is Required!");
                    userPassword.requestFocus();
                    return;
                }

                if (pass.length() < 6){
                    userPassword.setError("Minimum of 6 Characters is Required!");
                    userPassword.requestFocus();
                    return;
                }

                ld.startLoadingDialog();
                mAuth.createUserWithEmailAndPassword(email,pass)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful()){
                                    Profile profile = new Profile(name,email);
                                    FirebaseDatabase.getInstance().getReference("CapstoneDatabase/UserProfile")
                                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                            .setValue(profile).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful()){
                                                Toast.makeText(RegisterUser.this,"Registered Successfully!",Toast.LENGTH_LONG).show();
                                                ld.dismissDialog();
                                            }else{
                                                Toast.makeText(RegisterUser.this,"Failed to Register!",Toast.LENGTH_LONG).show();
                                                ld.dismissDialog();
                                            }
                                        }
                                    });

                                }else{
                                    Toast.makeText(RegisterUser.this,"Failed to Register!",Toast.LENGTH_LONG).show();
                                    ld.dismissDialog();
                                }
                            }
                        });
                //Close
            }
        });
    }
}