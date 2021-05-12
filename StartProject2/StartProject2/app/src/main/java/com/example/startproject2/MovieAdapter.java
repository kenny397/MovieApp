package com.example.startproject2;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.io.InputStream;
import java.util.ArrayList;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder>{

    ArrayList<Movie> items = new ArrayList<Movie>();


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull final ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.movie_item, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        Movie item = items.get(position);
        Log.d("[DEBUG]", "onBindViewHolder Called");
        holder.setItem(item);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void addItem(Movie item){
        items.add(item);
    }

    public void setItems(ArrayList<Movie> items){
        this.items = items;
    }

    public Movie getItem(int position) {
        return items.get(position);
    }

    public void clearItems() {
        this.items.clear();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView, textView2, textView3, textView4;
        ImageView imageView;
        RatingBar ratingBar;

        public final View layout;

        Bitmap bitmap;



        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            textView = itemView.findViewById(R.id.textView);
            textView2 = itemView.findViewById(R.id.textView2);
            textView3 = itemView.findViewById(R.id.textView3);
            textView4 = itemView.findViewById(R.id.textView4);
            imageView = itemView.findViewById(R.id.imageView);
            ratingBar = itemView.findViewById(R.id.ratingBar);

            layout = itemView;
        }

        public void setItem(final Movie item) {
            Glide.with(layout).load(item.image).into(imageView);
            textView.setText(item.title);
            String q1= item.director;
            if(q1.length()==0){
                textView2.setText("감독: " +q1);
            }
            else {
                q1 = q1.replaceAll("\\|", ",");
                q1=q1.substring(0,q1.length()-1);
                textView2.setText("감독: " + q1);
            }

            String q= item.actor;
            if(q.length()<=0){
                textView3.setText("출연: " + q);
            }
            else {
                q = q.replaceAll("\\|", ",");

                q=q.substring(0,q.length()-1);
                textView3.setText("출연: " + q);
            }
           layout.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                   Intent intent= new Intent(Intent.ACTION_VIEW, Uri.parse(item.link));
                   layout.getContext().startActivity(intent);
               }
           });




            if(item.userRating != 0) {
                textView4.setText(item.userRating + " ");
                ratingBar.setVisibility(View.VISIBLE);
                ratingBar.setRating(item.userRating / 2);
            } else {
                textView4.setText("평점 없음");
                ratingBar.setVisibility(View.GONE);
            }

        }
    }
}
