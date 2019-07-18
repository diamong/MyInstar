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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {

    EditText userOtherName,userName, userEmail, userPassword;
    Button buttonRegister;
    TextView textViewLogin;

    FirebaseAuth mAuth;
    DatabaseReference reference;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        userOtherName=findViewById(R.id.register_edittext_othername);
        userName = findViewById(R.id.register_edittext_username);
        userEmail = findViewById(R.id.register_edittext_email);
        userPassword = findViewById(R.id.register_edittext_password);
        buttonRegister = findViewById(R.id.register_button_login);
        textViewLogin = findViewById(R.id.register_textview_need_email);

        mAuth = FirebaseAuth.getInstance();

        textViewLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
            }
        });

        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                progressDialog = new ProgressDialog(RegisterActivity.this);
                progressDialog.setMessage("잠시만 기다려 주세요....");
                progressDialog.show();


                String stringUserOtherName=userOtherName.getText().toString();
                String stringUserName = userName.getText().toString();
                String stringUserEmail = userEmail.getText().toString();
                String stringUserPassword = userPassword.getText().toString();

                if (TextUtils.isEmpty(stringUserOtherName) ||TextUtils.isEmpty(stringUserName) || TextUtils.isEmpty(stringUserEmail) || TextUtils.isEmpty(stringUserPassword)) {
                    Toast.makeText(RegisterActivity.this, "빈칸을 모두 입력해 주세요", Toast.LENGTH_SHORT).show();

                } else if (stringUserPassword.length() < 6) {
                    Toast.makeText(RegisterActivity.this, "비밀번호는 여섯자리 이상 입력하세요", Toast.LENGTH_SHORT).show();
                } else {
                    register(stringUserOtherName,stringUserName, stringUserEmail, stringUserPassword);
                }
            }
        });
    }

    private void register(final String stringUserOtherName, final String string_userName, String string_userEmail, String string_userpassword) {
        mAuth.createUserWithEmailAndPassword(string_userEmail, string_userpassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    FirebaseUser firebaseUser = mAuth.getCurrentUser();
                    String userId = firebaseUser.getUid();

                    reference = FirebaseDatabase.getInstance().getReference().child("Users").child(userId);

                    HashMap<String, Object> hashMap = new HashMap<>();
                    hashMap.put("nickname", stringUserOtherName);
                    hashMap.put("id", userId);
                    hashMap.put("username", string_userName.toLowerCase());
                    hashMap.put("bio", "");
                    hashMap.put("imageurl", "https://firebasestorage.googleapis.com/v0/b/myinstar-5c088.appspot.com/o/ic_person.png?alt=media&token=d65cb519-c986-496a-8e53-cf8dee2574dd");

                    reference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                progressDialog.dismiss();
                                Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                            }
                        }
                    });
                } else {
                    progressDialog.dismiss();
                    String errorMessage = task.getException().toString();
                    Toast.makeText(RegisterActivity.this, "Error:  " + errorMessage, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
