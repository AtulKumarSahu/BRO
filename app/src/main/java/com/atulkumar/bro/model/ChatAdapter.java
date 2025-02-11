package com.atulkumar.bro.model;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.atulkumar.bro.R;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class ChatAdapter extends RecyclerView.Adapter{
       ArrayList<MessageModel> messageModels;
       Context context;
    public ChatAdapter(ArrayList<MessageModel> messageModels, Context context) {
        this.messageModels = messageModels;
        this.context = context;
    }


       int VIEW_TYPE_SENT=1;
       int VIEW_TYPE_RECIVED=2;
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType==VIEW_TYPE_SENT){
            View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.sender,parent,false);
            return new SenderViewHolder(view);
        }
        else {
            View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.reciver,parent,false);
            return new ReceiverViewHolder(view);
        }

    }
    @Override
    public int getItemViewType(int position) {
        if (messageModels.get(position).getSenderId().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())){
            return VIEW_TYPE_SENT;
        }
        else {
            return VIEW_TYPE_RECIVED;
        }

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
           MessageModel messageModel=messageModels.get(position);
           if (holder.getClass()==SenderViewHolder.class){
               SenderViewHolder senderViewHolder=(SenderViewHolder) holder;
               senderViewHolder.senderMessage.setText(messageModel.getMessage());
               senderViewHolder.senderTime.setText(android.text.format.DateFormat.format("hh:mm aa",messageModel.getTimestamp()));
        }
           else {
               ReceiverViewHolder receiverViewHolder=(ReceiverViewHolder) holder;
               receiverViewHolder.reciverMessage.setText(messageModel.getMessage());
               receiverViewHolder.reciverTime.setText(android.text.format.DateFormat.format("hh:mm aa",messageModel.getTimestamp()));
           }
       }

    @Override
    public int getItemCount() {
        return messageModels.size() ;
    }

    public class ReceiverViewHolder extends RecyclerView.ViewHolder {
         TextView reciverMessage,reciverTime;

        public ReceiverViewHolder(@NonNull View itemView) {
            super(itemView);
            reciverMessage=itemView.findViewById(R.id.message_recived);
            reciverTime=itemView.findViewById(R.id.recived_time);
        }
    }
     public class SenderViewHolder extends RecyclerView.ViewHolder{
               TextView senderMessage,senderTime;
         public SenderViewHolder(@NonNull View itemView) {
             super(itemView);
             senderMessage=itemView.findViewById(R.id.message_send);
             senderTime=itemView.findViewById(R.id.send_time );
         }
     }
}
