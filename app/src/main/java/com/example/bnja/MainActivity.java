package com.example.bnja;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.bnja.Modals.Users;
import com.example.bnja.databinding.ActivityMainBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    
    ActivityMainBinding binding;
    private FirebaseAuth auth = FirebaseAuth.getInstance();

    private String id;
    private String reqBloodGroup;
    private FirebaseFirestore database = FirebaseFirestore.getInstance();

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        super.onCreate(savedInstanceState);
        setContentView(binding.getRoot());
        final String[] name = new String[1];
        id = auth.getUid();
        if(id!=null){
            FirebaseMessaging.getInstance().subscribeToTopic(id);
            database.collection("Users").document(id).get()
                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            name[0] = Objects.requireNonNull(task.getResult().get("userName")).toString();
                            Objects.requireNonNull(getSupportActionBar()).setTitle(name[0]);
                            Toast.makeText(MainActivity.this, name[0], Toast.LENGTH_SHORT).show();
                        }
                    });
        }

        if(auth.getCurrentUser() == null){
            Intent intent = new Intent(MainActivity.this,SignIn.class);
            startActivity(intent);
            finish();
        }
        binding.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder request = new AlertDialog.Builder(MainActivity.this);
                request.setTitle("Blood Group Type: ");
                final EditText groupInput = new EditText(MainActivity.this);
                request.setView(groupInput);
                request.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        reqBloodGroup = groupInput.getText().toString().trim();
                        Intent intent = new Intent(MainActivity.this, SearchView.class);
                        intent.putExtra("reqGroup", reqBloodGroup);
                        startActivity(intent);
                    }
                });
                request.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                request.show();
            }
        });


    }


    private void Status(String status){
        database.collection("Users").document(id).update("status", status)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("TAG", "onSuccess: Status");
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("TAG", "onFailure: " + e.getMessage());
            }
        });
    }

    protected void onResume() {
        super.onResume();
        String status = "ONLINE";
        Status(status);
    }
    protected void onPause(){
        super.onPause();
        String status = "OFFLINE";
        Status(status);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.setting:
                break;

            case R.id.logout:
                auth.signOut();
                Intent intent = new Intent(MainActivity.this, SignIn.class);
                startActivity(intent);
                finish();
                break;

        }
        return super.onOptionsItemSelected(item);
    }
}