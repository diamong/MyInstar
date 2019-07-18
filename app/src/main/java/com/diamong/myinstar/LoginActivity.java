package com.diamong.myinstar;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
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

public class LoginActivity extends AppCompatActivity {

    EditText editTextEmail, editTextPassword;
    Button buttonLogin;
    TextView textViewToRegister;
    ProgressDialog progressDialog;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        editTextEmail = findViewById(R.id.login_edittext_email);
        editTextPassword = findViewById(R.id.login_edittext_password);
        buttonLogin = findViewById(R.id.login_button_login);
        textViewToRegister = findViewById(R.id.login_textview_need_email);
        progressDialog = new ProgressDialog(LoginActivity.this);
        progressDialog.setMessage("로그인 중입니다. 잠시만 기다려 주세요");

        mAuth = FirebaseAuth.getInstance();

        textViewToRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));

            }
        });

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.show();

                String stringEmail = editTextEmail.getText().toString();
                String stringPassword = editTextPassword.getText().toString();


                if (TextUtils.isEmpty(stringEmail) || TextUtils.isEmpty(stringPassword)) {
                    Toast.makeText(LoginActivity.this, "빈칸을 모두 입력하셔야 해요", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                } else {
                    mAuth.signInWithEmailAndPassword(stringEmail, stringPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Users")
                                        .child(mAuth.getCurrentUser().getUid());
                                reference.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        progressDialog.dismiss();
                                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                        finish();
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {
                                        progressDialog.dismiss();
                                    }
                                });
                            } else {
                                progressDialog.dismiss();
                                String errorMessage = task.getException().toString();
                                Toast.makeText(LoginActivity.this, "Error:   " + errorMessage, Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
    }
}
