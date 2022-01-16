package com.CPE001_2021.capstoneproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {
    private TextView register_,forgotpassword_;
    private EditText editTextEmail,editTextPassword;
    private Button signIn;
    private LoadingDialog ld;

    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();
        ld = new LoadingDialog(LoginActivity.this,"Logging In");
        signIn = (Button) findViewById(R.id.signIn);
        editTextEmail = (EditText) findViewById(R.id.loginEmail);
        editTextPassword = (EditText) findViewById(R.id.loginPassword);
        register_ = (TextView) findViewById(R.id.register_user);
        forgotpassword_ = (TextView) findViewById(R.id.ForgotPassword);

        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = editTextEmail.getText().toString().trim();
                String password = editTextPassword.getText().toString().trim();

                if(email.isEmpty()){
                    editTextEmail.setError("Email is Required!");
                    editTextEmail.requestFocus();
                    return;
                }

                if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                    editTextEmail.setError("Invalid Email!");
                    editTextEmail.requestFocus();
                    return;
                }

                if(password.isEmpty()){
                    editTextPassword.setError("Password is Required!");
                    editTextPassword.requestFocus();
                    return;
                }

                if (password.length() < 6){
                    editTextPassword.setError("Minimum of 6 Characters is Required!");
                    editTextPassword.requestFocus();
                    return;
                }

                ld.startLoadingDialog();

                mAuth.signInWithEmailAndPassword(email,password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                if(user.isEmailVerified()){
                                    DatabaseSystemFunctions.getInstance();
                                    startActivity(new Intent(LoginActivity.this,MainActivity.class));
                                }else{
                                    user.sendEmailVerification();
                                    Toast.makeText(LoginActivity.this,"Check your email to verify your account!",Toast.LENGTH_LONG).show();
                                }
                                ld.dismissDialog();

                            }else{
                                Toast.makeText(LoginActivity.this,"Failed to Login! Check your Credentials.",Toast.LENGTH_LONG).show();
                                ld.dismissDialog();
                            }
                        }
                    });
            }
        });
        register_.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(v.getContext(), RegisterUser.class));
            }
        });
        forgotpassword_.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this,ForgotPassword.class));
            }
        });
    }
}