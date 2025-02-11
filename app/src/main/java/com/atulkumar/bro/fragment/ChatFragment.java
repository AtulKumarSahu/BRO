package com.atulkumar.bro.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.atulkumar.bro.R;
import com.atulkumar.bro.activity.HomeActivity;
import com.atulkumar.bro.model.ChatAdapter;
import com.atulkumar.bro.model.MessageModel;

import com.atulkumar.bro.model.Partcipents;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ChatFragment extends Fragment {
    ArrayList<MessageModel> messageModels=new ArrayList<>();
  private TextView userName;
  private DatabaseReference userRef,chatRef;
  private  String bookId,userId,sellerid,chatroomId,reciverid,imageurl, buyerid,message;
  private ImageButton backbtn,sendbtn;
  private EditText messageinput;
  private RecyclerView chatRecycler;
  private ChatAdapter chatAdapter;


  public ChatFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_chat, container, false);

        userName=view.findViewById(R.id.UserName);
        backbtn=view.findViewById(R.id.backbtn);
        sendbtn=view.findViewById(R.id.send);
        messageinput=view.findViewById(R.id.edtmessage);
        chatRecycler=view.findViewById(R.id.chat_recycler);

        chatAdapter=new ChatAdapter(messageModels,getContext());
        chatRecycler.setAdapter(chatAdapter);
        chatRecycler.setLayoutManager(new LinearLayoutManager(getContext()));

        if (getArguments() != null) {
            bookId = getArguments().getString("BOOK_ID");
            chatroomId = getArguments().getString("CHAT_ROOM_ID");
            buyerid = getArguments().getString("BUYERID");
            sellerid = getArguments().getString("SELLERID");
            imageurl=getArguments().getString("IMAGEURL");
        }
        userId= FirebaseAuth.getInstance().getCurrentUser().getUid();
        reciverid=sellerid.equals(userId)?buyerid:sellerid;

        chatRef= FirebaseDatabase.getInstance().getReference().child("Chats");

        sendbtn.setOnClickListener(v -> {

            message=messageinput.getText().toString();
            if (message.isEmpty()){
                return;
            }
            MessageModel messageModel=new MessageModel(message,userId,System.currentTimeMillis());
            chatRef.child(chatroomId).child("Messages").push().setValue(messageModel).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    if (buyerid.isEmpty()&&sellerid.isEmpty()&&bookId.isEmpty()&&imageurl.isEmpty()){
                        return;
                    }
                    messageinput.setText("");
                    Partcipents partcipents=new Partcipents(buyerid,sellerid,bookId,imageurl,message);
                    chatRef.child(chatroomId).child("Partcipents").setValue(partcipents);
                }
            });
        });

        showChat();
        loadReciverName();
        backbtn.setOnClickListener(v -> {
            getParentFragmentManager().popBackStack();
        });

        ((HomeActivity) getActivity()).hideBottomNavigation();
        return view;
    }

    private void showChat() {

        chatRef.child(chatroomId).child("Messages").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                messageModels.clear();
                for (DataSnapshot a:snapshot.getChildren()){
                    MessageModel messageModel=a.getValue(MessageModel.class);
                    messageModels.add(messageModel);
                }
                chatAdapter.notifyDataSetChanged();
                chatRecycler.scrollToPosition(messageModels.size());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void loadReciverName() {

     userRef= FirebaseDatabase.getInstance().getReference().child("User").child(reciverid);
     userRef.addValueEventListener(new ValueEventListener() {
         @Override
         public void onDataChange(@NonNull DataSnapshot snapshot) {
             if(snapshot.exists()){
              String name=snapshot.child("name").getValue(String.class);
              userName.setText(name);
             }
         }

         @Override
         public void onCancelled(@NonNull DatabaseError error) {

         }
     });
    }

//    public void onDestroyView() {
//        super.onDestroyView();
//        // Show BottomNavigationView when this fragment is destroyed
//        ((HomeActivity) getActivity()).showBottomNavigation();
//    }
}