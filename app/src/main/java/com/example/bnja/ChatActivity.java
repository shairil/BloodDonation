package com.example.bnja;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.bnja.Modals.MessageModal;
import com.example.bnja.databinding.ActivityChatBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ChatActivity extends AppCompatActivity {
    ActivityChatBinding binding;
    private final String url = "https://fcm.googleapis.com/fcm/send";
    private final String key = "AAAAGzbhca0:APA91bHHlDiHE1yBhMIXc7B5YhrCRs5hrpbeVGrAfJo8y47G9saMKNCdZwIjk_MwoRooco6fH_4uTeCH2KGqvfsmRy-7TCOokWqHDsJnJRsjbye9wdXgCbo1yFQmhCowvvQ2Kbbga-Sf";
    private FirebaseAuth auth = FirebaseAuth.getInstance();
    String senderId;
    String receiverId;
    String userName;
    private final FirebaseFirestore database = FirebaseFirestore.getInstance();
    RequestQueue requestQueue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestQueue = Volley.newRequestQueue(this);
        binding = ActivityChatBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().hide();
        senderId = auth.getUid();

        final String id = getIntent().getStringExtra("userId");
        receiverId = id;
        final String name2 = getIntent().getStringExtra("userName");
        userName = name2;

        database.collection("Users").document(receiverId)
                .addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                binding.status.setText(value.get("status").toString().trim());
            }
        });

        binding.receiverUserName.setText(userName);
        binding.backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        final ArrayList<MessageModal> msgList = new ArrayList<>();
        final ChatAdapter adpater = new ChatAdapter(msgList, this);
        binding.chatRecyclerList.setAdapter(adpater);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        binding.chatRecyclerList.setLayoutManager(linearLayoutManager);

        database.collection("Users").document("chats")
                .collection(senderId+receiverId).orderBy("timeStamp")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if(!value.isEmpty()){
                    //msgList.clear();
                    for(DocumentChange doc: value.getDocumentChanges()){
                        switch(doc.getType()){
                            case ADDED:
                                MessageModal modal = doc.getDocument().toObject(MessageModal.class);
                                msgList.add(modal);
                                break;
                        }
                    }
                    adpater.notifyItemInserted(msgList.size() - 1);
                    binding.chatRecyclerList.smoothScrollToPosition(msgList.size()-1);
                }
            }
        });

        binding.sendbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg = binding.textmsg.getText().toString().trim();
                binding.textmsg.setText("");
                final MessageModal messageModal = new MessageModal(senderId, msg);
                messageModal.setTimeStamp(new Date().getTime());
                database.collection("Users").document("chats")
                        .collection(senderId+receiverId)
                        .add(messageModal).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        sendNotification(msg);
                        database.collection("Users").document("chats")
                                .collection(receiverId+senderId)
                                .add(messageModal).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                //Toast.makeText(ChatActivity.this, "hii..", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });

            }
        });


    }

    private void sendNotification(String msg){
        final String[] name = new String[1];
        database.collection("Users")
                .document(senderId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                name[0] = task.getResult().get("userName").toString();
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("to", "/topics/"+receiverId);
                    JSONObject title = new JSONObject();
                    title.put("title", name[0]);
                    title.put("body", msg);
                    title.put("userID", senderId);
                    jsonObject.put("notification", title);
                    JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url,
                            jsonObject, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.d("Mummy", "onErrorResponse: " + error.getMessage());
                        }
                    }){
                        @Override
                        public Map<String, String> getHeaders() throws AuthFailureError {
                            Map<String, String> map = new HashMap<>();
                            map.put("content-type", "application/json");
                            map.put("authorization", "key="+key);
                            return map;
                        }
                    };
                    requestQueue.add(jsonObjectRequest);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    private void Status(String status){
        database.collection("Users").document(senderId).update("status", status)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("TAG", "onSuccess: Status1"+status);
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
}