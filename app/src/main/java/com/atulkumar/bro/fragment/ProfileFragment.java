package com.atulkumar.bro.fragment;

import static android.content.Context.MODE_PRIVATE;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.atulkumar.bro.R;
import com.atulkumar.bro.activity.HomeActivity;
import com.atulkumar.bro.activity.SignInActivity;
import com.atulkumar.bro.model.BookModel;
import com.atulkumar.bro.model.UserBookAdapter;
import com.atulkumar.bro.model.UserModel;
import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileFragment extends Fragment {

private RecyclerView recyclerView;
    private UserBookAdapter bookAdapter;
    private ArrayList<BookModel> bookList;
    private UserModel user;
    private FirebaseAuth mAuth;
    private Button logout,sellShare,update;
   private TextView name,email;
   private String userId,username,emailid,imageurl;
   FirebaseUser firebaseUser;
   private CircleImageView circleImageView;

    private static final String TAG = "User";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_profile, container, false);
         recyclerView=view.findViewById(R.id.book_recycler);
         name=view.findViewById(R.id.user_name);
         email=view.findViewById(R.id.profile_email);
         logout=view.findViewById(R.id.logout);
         update=view.findViewById(R.id.update_profile);
         sellShare=view.findViewById(R.id.sell_share);
         circleImageView=view.findViewById(R.id.profileImg);
         mAuth=FirebaseAuth.getInstance();

         logout.setOnClickListener(v -> {
             logout();
         });
        displyUserBook();
        showUser();

        sellShare.setOnClickListener(v -> {
            getParentFragmentManager().beginTransaction()
                    .setCustomAnimations(R.anim.form_open,R.anim.anim_out)
                    .replace(R.id.main_container,new AddBookFragment())
                    .addToBackStack(null).commit();
        });

        update.setOnClickListener(v -> {
            UpdateProfileFragment fragment=new UpdateProfileFragment();
            Bundle bundle=new Bundle();
            bundle.putString("USER",username);
            bundle.putString("EMAIL",emailid);
            fragment.setArguments(bundle);
            getParentFragmentManager().beginTransaction()
                    .replace(R.id.main_container,fragment)
                    .addToBackStack(null).commit();
        });
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        ((HomeActivity) getActivity()).showBottomNavigation();
    }

    private void showUser() {
        DatabaseReference userRef= FirebaseDatabase.getInstance().getReference().child("User").child(userId);
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                     username = snapshot.child("name").getValue(String.class);
                     emailid = snapshot.child("email").getValue(String.class);
                     imageurl=snapshot.child("userImage").getValue(String.class);
                    Glide.with(getActivity()).load(imageurl).into(circleImageView);
                    name.setText(username);
                    email.setText(emailid);
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Failed to load user data"+ error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void logout() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View dialogView=inflater.inflate(R.layout.dialog_box, null);
        builder.setView(dialogView);
        AlertDialog dialog = builder.create();


        TextView message=dialogView.findViewById(R.id.message);
        TextView title=dialogView.findViewById(R.id.title);
        Button positive=dialogView.findViewById(R.id.btnPositive);
        Button negative=dialogView.findViewById(R.id.btnNegative);
        title.setText(R.string.logout);
        message.setText(R.string.are_you_sure_you_want_to_logout);

        positive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearLoginState();
                startActivity(new Intent(getActivity(), SignInActivity.class));
                getActivity().finish();
            }
        });

        negative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();


    }

    private void clearLoginState() {
        SharedPreferences prefs;
        prefs = requireActivity().getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean("isLoggedIn", false);
        editor.apply();
    }

    private void displyUserBook() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        firebaseUser=mAuth.getCurrentUser();
        assert firebaseUser != null;
        userId=firebaseUser.getUid();
        DatabaseReference bookref = FirebaseDatabase.getInstance().getReference().child("User").child(userId).child("books");
        bookref.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                bookList=new ArrayList<>();
                for (DataSnapshot booksnap:snapshot.getChildren()){
                    BookModel bookModel=booksnap.getValue(BookModel.class);
                  bookList.add(bookModel);
//                    Log.d(TAG, "Book Retrieved: " + bookModel.getBookName());
                }
                bookAdapter = new UserBookAdapter(bookList);
                recyclerView.setAdapter(bookAdapter);
                bookAdapter.notifyDataSetChanged();
//                Log.d(TAG, "Total Books Retrieved: " + bookList.size());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d(TAG, "Error retrieving books: " +error.getMessage());
            }
        });

    }
}