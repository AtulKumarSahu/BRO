package com.atulkumar.bro.model;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.atulkumar.bro.R;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class UserBookAdapter extends RecyclerView.Adapter<UserBookAdapter.BookViewHolder> {

    private ArrayList<BookModel> bookModels;

    public UserBookAdapter(ArrayList<BookModel> bookModels) {
        this.bookModels = bookModels;
    }

    @NonNull
    @Override
    public BookViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.book_recycler,parent,false);

        return new  BookViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookViewHolder holder, int position) {

  BookModel book=bookModels.get(position);
     holder.bookName.setText(book.getBookName());
     holder.publication.setText(book.getPublication());
     holder.yearOrSemester.setText(book.getYearOrSemesterStr());
     Glide.with(holder.itemView.getContext()).load(book.getImageUrl()).into(holder.bookImage);
    }

    @Override
    public int getItemCount() {
        return bookModels.size();
    }

    public static class BookViewHolder extends RecyclerView.ViewHolder{
        ImageView bookImage;
        TextView bookName, publication, yearOrSemester;
        public BookViewHolder(@NonNull View itemView) {
            super(itemView);
            bookImage = itemView.findViewById(R.id.book_Image);
            bookName = itemView.findViewById(R.id.book_Name);
            publication = itemView.findViewById(R.id.publication_book);
            yearOrSemester = itemView.findViewById(R.id.year_Semester);
        }
    }
}
