package com.example.bnja;

import android.content.Context;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bnja.Modals.MessageModal;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import java.util.SimpleTimeZone;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.SendRecViewHolder> {
    ArrayList<MessageModal> msgList;
    Context context;
    String senderId = FirebaseAuth.getInstance().getUid();

    public ChatAdapter(ArrayList<MessageModal> msgList, Context context) {
        this.msgList = msgList;
        this.context = context;
    }

    @Override
    public int getItemViewType(int position) {
        if(msgList.get(position).getId().equals(senderId)){
            return 1;
        }
        else{
            return 0;
        }
    }

    @NonNull
    @Override
    public SendRecViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if(viewType == 1){
            view = LayoutInflater.from(context).inflate(R.layout.magsender, parent, false);
        }
        else{
            view = LayoutInflater.from(context).inflate(R.layout.msgreceiver, parent, false);
        }
        return new SendRecViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SendRecViewHolder holder, int position) {
        MessageModal msg = msgList.get(position);
        if(msgList.get(position).getId().equals(senderId)){
            holder.senderMsg.setText(msg.getMsg());
            Long timestamp = msg.getTimeStamp();
            Calendar cal = Calendar.getInstance(Locale.ENGLISH);
            cal.setTimeInMillis(timestamp);
            holder.senderDateTime.setText(DateFormat.format("hh:mm a", cal).toString());
        }
        else{
            holder.recMsg.setText(msg.getMsg());
            Long timestamp = msg.getTimeStamp();
            Calendar cal = Calendar.getInstance(Locale.ENGLISH);
            cal.setTimeInMillis(timestamp);
            holder.recDateTime.setText(DateFormat.format("hh:mm a", cal).toString());
        }

    }

    @Override
    public int getItemCount() {
        return msgList.size();
    }

    public class SendRecViewHolder extends RecyclerView.ViewHolder{
        TextView senderMsg, recMsg, senderDateTime, recDateTime;
        public SendRecViewHolder(@NonNull View itemView) {
            super(itemView);
            senderMsg = itemView.findViewById(R.id.sendmsg);
            recMsg = itemView.findViewById(R.id.msg);
            senderDateTime = itemView.findViewById(R.id.senderDatetime);
            recDateTime = itemView.findViewById(R.id.datetime);
        }
    }
}
