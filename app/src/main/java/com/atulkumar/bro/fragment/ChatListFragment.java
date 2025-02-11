package com.atulkumar.bro.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.atulkumar.bro.R;
import com.atulkumar.bro.activity.HomeActivity;
import com.atulkumar.bro.model.ChatRoom;
import com.atulkumar.bro.model.ChatRoomAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class ChatListFragment extends Fragment {




    public ChatListFragment() {
    }

     TextView appName;
    private RecyclerView recyclerViewChats;
    private ChatRoomAdapter chatRoomAdapter;
    private List<ChatRoom> chatRoomList;
    DatabaseReference chatsRef,userref;

    String userId,reciverid,chatRoomId,bookId,buyerID,sellerID,lastmessage,imageurl,recivername;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_chat_list, container, false);

        appName = view.findViewById(R.id.app_name);
        recyclerViewChats = view.findViewById(R.id.chatlistrecycler);
        recyclerViewChats.setLayoutManager(new LinearLayoutManager(getContext()));

        chatRoomList = new ArrayList<>();
        chatRoomAdapter = new ChatRoomAdapter(chatRoomList, getActivity());
        recyclerViewChats.setAdapter(chatRoomAdapter);

        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();


        loadChatRooms();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        ((HomeActivity) getActivity()).showBottomNavigation();
    }

    private void loadChatRooms() {
        chatsRef = FirebaseDatabase.getInstance().getReference("Chats");

        chatsRef.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                chatRoomList.clear();
                for (DataSnapshot chatRoomSnapshot : dataSnapshot.getChildren()) {
                    String chatRoomId = chatRoomSnapshot.getKey();
                    String bookId = chatRoomSnapshot.child("bookId").getValue(String.class);
                    String buyerID = chatRoomSnapshot.child("Partcipents").child("buyerId").getValue(String.class);
                    String sellerID = chatRoomSnapshot.child("Partcipents").child("sellerId").getValue(String.class);
                    String imageurl = chatRoomSnapshot.child("Partcipents").child("imageurl").getValue(String.class);
                    String lastmessage = chatRoomSnapshot.child("Partcipents").child("lastmessage").getValue(String.class);
                    String reciverid = sellerID.equals(userId) ? buyerID : sellerID;

                    if (buyerID != null && sellerID != null && (buyerID.equals(userId) || sellerID.equals(userId))) {
                         userref = FirebaseDatabase.getInstance().getReference().child("User").child(reciverid);

                        userref.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                String recivername = snapshot.child("name").getValue(String.class);
                                if (recivername != null) {
                                    ChatRoom chatRoom = new ChatRoom(chatRoomId, bookId, buyerID, sellerID, imageurl, recivername, lastmessage);
                                    chatRoomList.add(chatRoom);
                                    chatRoomAdapter.notifyDataSetChanged(); // Notify after adding the new chat room
                                } else {
                                    Toast.makeText(getContext(), "Receiver name not found", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle possible errors.
            }
        });
    }

}