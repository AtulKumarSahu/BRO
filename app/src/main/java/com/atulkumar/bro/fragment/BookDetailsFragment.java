package com.atulkumar.bro.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.atulkumar.bro.R;
import com.atulkumar.bro.activity.HomeActivity;
import com.atulkumar.bro.model.BookModel;
import com.bumptech.glide.Glide;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class BookDetailsFragment extends Fragment {
   private BookModel book;
   private FirebaseUser firebaseUser;
   private FirebaseAuth mAuth;
  private String userId,bookUserId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_book_details, container, false);


        ImageView imageholder=view.findViewById(R.id.image_dls);
        TextView nameholder=view.findViewById(R.id.name_dls);
        TextView publicationholder=view.findViewById(R.id.publication_dls);
        TextView yearholder=view.findViewById(R.id.year_Semester_dls);
        TextView descriptionholder=view.findViewById(R.id.description_details);
        MaterialButton chatbtn=view.findViewById(R.id.btnchat);
        ImageButton backbtn=view.findViewById(R.id.dlsback);


        backbtn.setOnClickListener(v -> {
            getParentFragmentManager().popBackStack();
        });


        if (getArguments() != null) {
            book = (BookModel) getArguments().getSerializable("book");
        }

        nameholder.setText(book.getBookName());
        publicationholder.setText(book.getPublication());
        yearholder.setText(book.getYearOrSemesterStr());
        descriptionholder.setText(book.getDecriptionStr());
        Glide.with(getContext()).load(book.getImageUrl()).into(imageholder);

        chatbtn.setOnClickListener(v -> {
            bookUserId=book.getBook_userid();
            mAuth=FirebaseAuth.getInstance();
            firebaseUser=mAuth.getCurrentUser();
            assert firebaseUser != null;
            userId=firebaseUser.getUid();
            if (bookUserId.equals(userId)){
                Toast.makeText(getContext(), "This book uploaded by self", Toast.LENGTH_SHORT).show();
            }

            else {
                Bundle bundle = new Bundle();
                bundle.putString("BOOK_ID", book.getBookId());
                bundle.putString("BUYERID", userId);
                bundle.putString("SELLERID", book.getBook_userid());
                bundle.putString("CHAT_ROOM_ID", userId+"_"+book.getBookId());
                bundle.putString("IMAGEURL", book.getImageUrl());
                ChatFragment chatFragment = new ChatFragment();
                chatFragment.setArguments(bundle);
                getParentFragmentManager().beginTransaction().replace(R.id.main_container, chatFragment).addToBackStack(null).commit();
            }
            });

        ((HomeActivity) getActivity()).hideBottomNavigation();
        return view;
    }

}