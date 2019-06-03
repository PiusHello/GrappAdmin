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

import com.example.grappadmin.Prevalent.Prevalent;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import io.paperdb.Paper;
public class LoginAdminActivity extends AppCompatActivity {
    private EditText userEmail,userPassword;
    private Button signInManager;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference myDatabase;
    private ProgressDialog progressDialog;
    private CheckBox checkBox;
    private TextView createAccount;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_admin);
        checkBox = (CheckBox) findViewById(R.id.remember_me);
        userEmail = (EditText) findViewById(R.id.user_email);
        userPassword = (EditText) findViewById(R.id.user_password);
        signInManager = (Button) findViewById(R.id.sign_in_button);
        createAccount = (TextView) findViewById(R.id.createAnAccount);
        progressDialog = new ProgressDialog(this);
        Paper.init(this);
        firebaseAuth = FirebaseAuth.getInstance();
        myDatabase = FirebaseDatabase.getInstance().getReference().child("Managers");

        createAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginAdminActivity.this,RegisterManagerActivity.class);
                startActivity(intent);
            }
        });
        signInManager.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                allowAccess();
            }
        });


    }

    private void allowAccess()
    {
        final String email = userEmail.getText().toString().trim();
        final String password = userPassword.getText().toString().trim();

        if (TextUtils.isEmpty(email))
        {
            userEmail.setError("Email is required");
            userEmail.requestFocus();
            return;
        }

        else if (TextUtils.isEmpty(password))
        {
            userEmail.setError("Password is required");
            userEmail.requestFocus();
            return;
        }

        else
        {
            progressDialog.setTitle("Welcome Back");
            progressDialog.setMessage("Please wait...");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();

            firebaseAuth.signInWithEmailAndPassword(email,password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if(task.isSuccessful())
                            {
                                if(checkBox.isChecked())
                                {
                                    Paper.book().write(Prevalent.UserEmailKey,email);
                                    Paper.book().write(Prevalent.UserPasswordKey,password);
                                }
                                final String user_id = firebaseAuth.getCurrentUser().getUid();
                                myDatabase.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        if(dataSnapshot.hasChild(user_id))
                                        {
                                            progressDialog.dismiss();

                                            Intent homeIntent = new Intent(LoginAdminActivity.this,HomeActivity.class);
                                            homeIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                            Prevalent.currentOnLineUser = user_id;
                                            startActivity(homeIntent);
                                            finish();

                                            Toast.makeText(LoginAdminActivity.this,"You Have Successfully Logged In",Toast.LENGTH_SHORT).show();

                                        }



                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });

                            }
                            else
                            {
                                String errorMessage = task.getException().getMessage();
                                Toast.makeText(LoginAdminActivity.this,"You must registered before you login",Toast.LENGTH_LONG).show();

                                Intent loginIntent = new Intent(LoginAdminActivity.this,LoginAdminActivity.class);
                                loginIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(loginIntent);
                            }
                        }
                    });

        }
    }




    private void SendMeToRegisterActivity()
    {
        Intent registerIntent = new Intent(LoginAdminActivity.this,RegisterManagerActivity.class);
        startActivity(registerIntent);
    }


}
