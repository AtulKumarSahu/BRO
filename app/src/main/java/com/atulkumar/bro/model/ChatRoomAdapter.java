package com.atulkumar.bro.model;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.atulkumar.bro.R;
import com.atulkumar.bro.fragment.ChatFragment;
import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;


import java.util.List;

public class ChatRoomAdapter extends RecyclerView.Adapter<ChatRoomAdapter.ChatRoomViewHolder> {

    private List<ChatRoom> chatRooms;
    private Context context;
    String userId= FirebaseAuth.getInstance().getCurrentUser().getUid();
    private static final String TAG = "ChatRoomAdapter";

    public ChatRoomAdapter(List<ChatRoom> chatRooms,Context context) {
        this.chatRooms = chatRooms;
        this.context=context;

    }

    @NonNull
    @Override
    public ChatRoomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_list_recycler, parent, false);
        return new ChatRoomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatRoomViewHolder holder, int position) {
        ChatRoom chatRoom = chatRooms.get(position);
        holder.username.setText(chatRoom.getRecivername());
        holder.lastmessage.setText(chatRoom.getLastmessage());
        Glide.with(holder.itemView.getContext()).load(chatRoom.getImageurl()).into(holder.imageView);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Log.d(TAG, "ChatRoom clicked: " + chatRoom.getChatRoomId());

                    ChatFragment chatFragment = new ChatFragment();
                    Bundle args = new Bundle();
                    args.putString("BOOK_ID", chatRoom.getBookId());
                    args.putString("CHAT_ROOM_ID", chatRoom.getChatRoomId());
                    args.putString("SELLERID", chatRoom.getSellerId());
                    args.putString("BUYERID", chatRoom.getBuyerId());
                    args.putString("IMAGEURL", chatRoom.getImageurl());

                    chatFragment.setArguments(args);
                    AppCompatActivity activity=(AppCompatActivity)v.getContext();
                    activity.getSupportFragmentManager()
                            .beginTransaction().replace(R.id.main_container,chatFragment)
                            .addToBackStack(null).commit();
                }catch (Exception e){
                    Log.e(TAG, e.getMessage(), e);
                }
            }
        });

    }


    @Override
    public int getItemCount() {
        return chatRooms.size();
    }

    public static class ChatRoomViewHolder extends RecyclerView.ViewHolder {
        private TextView username,lastmessage;
        private ImageView imageView;


        public ChatRoomViewHolder(@NonNull View itemView) {
            super(itemView);
            username = itemView.findViewById(R.id.chats_user);
            imageView=itemView.findViewById(R.id.book_pic);
            lastmessage=itemView.findViewById(R.id.last_message);
        }


    }

}
