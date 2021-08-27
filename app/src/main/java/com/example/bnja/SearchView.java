package com.example.bnja;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.bnja.Modals.Users;
import com.example.bnja.databinding.ActivitySearchViewBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class SearchView extends AppCompatActivity {
    ActivitySearchViewBinding binding;
    private FirebaseFirestore database;
    ArrayList<Users> userList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        binding = ActivitySearchViewBinding.inflate(getLayoutInflater());
        super.onCreate(savedInstanceState);
        setContentView(binding.getRoot());

        getSupportActionBar().setTitle("Available Donor's");

        database = FirebaseFirestore.getInstance();
        UserAdapter userAdapter = new UserAdapter(userList, SearchView.this);
        binding.recyclerView2.setAdapter(userAdapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        binding.recyclerView2.setLayoutManager(layoutManager);
        database.collection("Users")
                .whereEqualTo("bloodGroup", getIntent().getStringExtra("reqGroup").toUpperCase())
                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if(!queryDocumentSnapshots.isEmpty()){
                            userList.clear();
                            for(DocumentSnapshot ds: queryDocumentSnapshots.getDocuments()){
                                Users userTemp = ds.toObject(Users.class);
                                //userTemp.getUserId(ds.getId());
                                userList.add(userTemp);
                                Log.d("TAG", "onSuccess: 1");
                                Toast.makeText(SearchView.this, getIntent().getStringExtra("reqGroup"), Toast.LENGTH_SHORT).show();
                            }
                            userAdapter.notifyDataSetChanged();
                        }
                        else{
                            Log.d("TAG", "fail");
                        }
                    }
                }
        );

    }
}