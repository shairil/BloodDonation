package com.example.bnja;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.nfc.Tag;
import static android.content.ContentValues.TAG;

import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.bnja.Modals.Users;
import com.example.bnja.databinding.ActivityRegisterBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class Register extends AppCompatActivity {

    ActivityRegisterBinding binding;
    private FirebaseAuth auth;
    private FirebaseFirestore database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        super.onCreate(savedInstanceState);
        setContentView(binding.getRoot());
        getSupportActionBar().hide();
        auth = FirebaseAuth.getInstance();
        database = FirebaseFirestore.getInstance();

        binding.singIn.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Register.this, SignIn.class);
                startActivity(intent);
                finish();
            }
        });


        binding.signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String bloodGroup = binding.bloodgroup.getText().toString().trim().toUpperCase();
                String email = binding.email.getText().toString().trim();
                String password = binding.password.getText().toString().trim();
                String userName = binding.name.getText().toString().trim();
                binding.progressBar.setVisibility(View.VISIBLE);
                if(TextUtils.isEmpty(email)){
                    binding.email.setError("Email can't be empty");
                    binding.progressBar.setVisibility(View.GONE);
                    return;
                }
                if(TextUtils.isEmpty(password)){
                    binding.password.setError("Password can't be empty");
                    binding.progressBar.setVisibility(View.GONE);
                    return;
                }
                if(TextUtils.isEmpty(bloodGroup)){
                    binding.bloodgroup.setError("bloodGroup can't be empty");
                    binding.progressBar.setVisibility(View.GONE);
                    return;
                }
                if(TextUtils.isEmpty(userName)){
                    binding.email.setError("userName can't be empty");
                    binding.progressBar.setVisibility(View.GONE);
                    return;
                }
                auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(
                        new OnCompleteListener<AuthResult>() {
                            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                binding.progressBar.setVisibility(View.GONE);
                                if(task.isSuccessful()){
                                    Users user = new Users(userName, bloodGroup, email, password);
                                    String id = task.getResult().getUser().getUid();
                                    user.setUserId(id);
                                    database.collection("Users").document(id).set(user)
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Toast.makeText(Register.this, "SuccesFull", Toast.LENGTH_SHORT).show();
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(Register.this, e.toString(), Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                    Toast.makeText(Register.this, "User Created", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(Register.this, MainActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                                else{
                                    Toast.makeText(Register.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                );
            }
        });
    }
}