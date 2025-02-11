package com.atulkumar.bro.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.atulkumar.bro.R;
import com.atulkumar.bro.activity.HomeActivity;
import com.atulkumar.bro.model.BookModel;
import com.atulkumar.bro.model.HomeBookAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class HomeFragment extends Fragment {

   private FloatingActionButton fab;
    private ArrayList<BookModel> homeBookLists =new ArrayList<>();
    private SearchView searchView;
    private ImageView imglocation;
    private TextView textlocation;
    private ProgressBar progressBar;

    RecyclerView recyclerView;
    private HomeBookAdapter bookAdapter;
    private DatabaseReference booksRef;




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view=inflater.inflate(R.layout.fragment_home, container, false);


        fab=view.findViewById(R.id.fab);
        imglocation=view.findViewById(R.id.img_location);
        textlocation=view.findViewById(R.id.location);
        progressBar=view.findViewById(R.id.progress_home);
        recyclerView=view.findViewById(R.id.book_recycler);
        searchView=view.findViewById(R.id.search_view);


        recyclerView.setLayoutManager(new GridLayoutManager(getContext(),2));
        homeBookLists = new ArrayList<>();
        bookAdapter = new HomeBookAdapter(getContext(), homeBookLists);
        recyclerView.setAdapter(bookAdapter);

        loadAllBooks();
        setupSearchView();

        fab.setOnClickListener(v -> {
            getParentFragmentManager().beginTransaction()
                    .setCustomAnimations(R.anim.form_open,R.anim.anim_out)
                    .replace(R.id.main_container,new AddBookFragment())
                    .addToBackStack(null).commit();
          });
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        ((HomeActivity) getActivity()).showBottomNavigation();
    }

    private void setupSearchView() {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                filterBooks(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
//                filterBooks(newText);

//                if (!newText.isEmpty()){
//                    filterBooks(newText);
//                    ((HomeActivity) getActivity()).hideBottomNavigation();
//                }
//                else {
//                    ((HomeActivity) getActivity()).showBottomNavigation();
//                }
                return true;
            }
        });
    }

    private void filterBooks(String query) {
        ArrayList<BookModel> filteredList = new ArrayList<>();
        if (!TextUtils.isEmpty(query)) {
            for (BookModel book : homeBookLists) {
                if (book.getBookName().toLowerCase().contains(query.toLowerCase()) ||
                        book.getPublication().toLowerCase().contains(query.toLowerCase())) {
                    filteredList.add(book);
                }
            }
        } else {
            filteredList = new ArrayList<>(homeBookLists);
        }
        bookAdapter.updateList(filteredList);
    }

    private void loadAllBooks() {

        progressBar.setVisibility(View.VISIBLE);
        DatabaseReference booksRef = FirebaseDatabase.getInstance().getReference().child("User");

        booksRef.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                homeBookLists.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    for (DataSnapshot bookSnapshot : postSnapshot.child("books").getChildren()){
                    BookModel book = bookSnapshot.getValue(BookModel.class);
                    homeBookLists.add(book);
                }
            }
                progressBar.setVisibility(View.GONE);
                bookAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("home", "DatabaseError: " + databaseError.getMessage());
                Toast.makeText(getContext(), "Failed to load books", Toast.LENGTH_SHORT).show();
            }
        });
    }
}