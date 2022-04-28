package com.veselin.sprintcompare.acitvities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.veselin.sprintcompare.R;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {

    //Widgets
    EditText usernameET, passwordET, emailET;
    Button registerBtn;
    TextView loginBtn;

    //Firebase
    FirebaseAuth firebaseAuth;
    DatabaseReference myRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        firebaseAuth = FirebaseAuth.getInstance();
        initializeViews();
    }

    private void initializeViews(){
        usernameET = findViewById(R.id.username_input);
        passwordET = findViewById(R.id.password_input);
        emailET = findViewById(R.id.email_input);
        loginBtn = findViewById(R.id.login_btn);
        loginBtn.setOnClickListener(view -> startActivity(new Intent(RegisterActivity.this, LoginActivity.class)));
        registerBtn = findViewById(R.id.sign_up_btn);
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = usernameET.getText().toString();
                String email = emailET.getText().toString();
                String password = passwordET.getText().toString();
                //TODO add data verification
                register(username, email, password);
            }
        });
    }

    private void register(final String username, String email, String password){
        firebaseAuth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                            String userId = firebaseUser.getUid();
                            myRef = FirebaseDatabase.getInstance()
                                    .getReference("Users")
                                    .child(userId);

                            HashMap<String, String> hashMap = new HashMap<>();
                            hashMap.put("id", userId);
                            hashMap.put("username", username);
                            hashMap.put("imageURL", "URL");

                            //Starting MainActivity after successful login
                            myRef.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        startActivity(new Intent(RegisterActivity.this, MainActivity.class)
                                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
                                        finish();
                                    }else{
                                        Toast.makeText(RegisterActivity.this, "Error registering", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

                        }else{
                            Toast.makeText(RegisterActivity.this, "Invalid email or password", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}