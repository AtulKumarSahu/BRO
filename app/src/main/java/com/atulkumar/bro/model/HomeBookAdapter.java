package com.atulkumar.bro.model;

import android.annotation.SuppressLint;
import android.content.Context;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.atulkumar.bro.fragment.BookDetailsFragment;
import com.bumptech.glide.Glide;

import com.atulkumar.bro.R;

import java.util.ArrayList;


public class HomeBookAdapter extends RecyclerView.Adapter<HomeBookAdapter.ViewHolder> {
    private final Context mCtx;

    private ArrayList<BookModel> itemlist;


    public HomeBookAdapter(Context mCtx, ArrayList<BookModel> item) {
        this.mCtx = mCtx;
        this.itemlist = item;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=  LayoutInflater.from(parent.getContext()).inflate(R.layout.home_recycler_view,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        BookModel book = itemlist.get(position);

        holder.name.setText(book.getBookName());
        holder.publication.setText(book.getPublication());
        Glide.with(holder.itemView.getContext()).load(book.getImageUrl()).placeholder(R.drawable.logo)
        .into(holder.image);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BookDetailsFragment fragment=new BookDetailsFragment();
                Bundle bundle=new Bundle();
                bundle.putSerializable("book",book);
                fragment.setArguments(bundle);
                AppCompatActivity activity=(AppCompatActivity)v.getContext();
                activity.getSupportFragmentManager()
                        .beginTransaction().replace(R.id.main_container,fragment)
                        .addToBackStack(null).commit();
            }
        });
    }

    @Override
    public int getItemCount() {
        return itemlist.size();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void updateList(ArrayList<BookModel> newList) {
        itemlist=newList;
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private final ImageView image;
        private final TextView name;
        private final TextView publication;
       

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image=itemView.findViewById(R.id.book_image);
            name=itemView.findViewById(R.id.book_name);
            publication=itemView.findViewById(R.id.book_publication);

        }
    }
}


