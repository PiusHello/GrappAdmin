package com.example.grappadmin;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import io.paperdb.Paper;

public class RegisterManagerActivity extends AppCompatActivity {

    private Button registerManager;
    private EditText ManagerEmail,ManagerName,ManagerPassword;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference database;
    private ProgressDialog progressDialog;
    private CheckBox checkBox;
    private TextView hasAccount;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_manager);

        firebaseAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance().getReference().child("Managers");

        ManagerName = (EditText) findViewById(R.id.managerName);
        ManagerEmail = (EditText) findViewById(R.id.managerEmail);
        ManagerPassword = (EditText) findViewById(R.id.managerPassword);
        registerManager = (Button) findViewById(R.id.sign_up_manager_button);
        hasAccount = (TextView) findViewById(R.id.alreadyHasAnAccount);

        progressDialog = new ProgressDialog(this);
        Paper.init(this);

        hasAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterManagerActivity.this,LoginAdminActivity.class);
                startActivity(intent);
            }
        });

        registerManager.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              registerNewManager();
            }
        });
    }

    private void registerNewManager()
    {
        final String name = ManagerName.getText().toString().trim();
        final String email = ManagerEmail.getText().toString().trim();
        String password = ManagerPassword.getText().toString().trim();


        if(TextUtils.isEmpty(name))
        {
            ManagerName.setError("Name is required");
            ManagerName.requestFocus();
            return;
        }

        else if(name.length()<3)
        {
            ManagerName.setError("Minimum length require is 3");
            ManagerName.requestFocus();
            return;
        }

        else if(name.contains("¬!£$%^&&*()_+=-<>:@~/.,0123456789/*-+"))
        {
            ManagerName.setError("Username Is Invalid, Remove Spacial Characters ");
            ManagerName.requestFocus();
            return;

        }

        else if(TextUtils.isEmpty(email))
        {
            ManagerEmail.setError("Email Is Required");
            ManagerEmail.requestFocus();
            return;
        }

        else if(!email.contains("@"))
        {
            ManagerEmail.setError("Email Is Invalid, Add The @ Sign");
            ManagerEmail.requestFocus();
            return;
        }

        else if(TextUtils.isEmpty(password))
        {
            ManagerPassword.setError("Password Is Required");
            ManagerPassword.requestFocus();
            return;
        }

        else if(password.length()<6)
        {
            ManagerPassword.setError("Minimum Password Required Is 6 Characters");
            ManagerPassword.requestFocus();
            return;

        }

        else
        {
            progressDialog.setTitle("Almost there");
            progressDialog.setMessage("Creating account");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();

            firebaseAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {


                    if (task.isSuccessful()) {
                        String user_id = firebaseAuth.getCurrentUser().getUid();
                        DatabaseReference currentUser = database.child(user_id);

                        currentUser.child("Username").setValue(name);
                        currentUser.child("Email").setValue(email);

                        progressDialog.dismiss();
                        Toast.makeText(RegisterManagerActivity.this, "Registration Was Successful", Toast.LENGTH_SHORT).show();


                        Intent homeIntent = new Intent(RegisterManagerActivity.this, HomeActivity.class);
                        homeIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(homeIntent);
                        finish();

                    }

                    else
                    {

                        String errorMessage = task.getException().getMessage();
                        Toast.makeText(RegisterManagerActivity.this," Error Occurred " + errorMessage ,Toast.LENGTH_SHORT).show();

                    }

                }

            });
        }


    }
}
