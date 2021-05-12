package com.example.startproject2;


import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class SearchFragment extends Fragment {

    MovieAdapter adapter;
    final String clientId = "naver clientId";//애플리케이션 클라이언트 아이디값";
    final String clientSecret = "naver clientScret";//애플리케이션 클라이언트 비밀번호
    String urlStr = "https://openapi.naver.com/v1/search/movie.json?query=";
    RecyclerView recyclerView;
    String uriString = "content://com.example.sampleproject2.movieprovider/movie";
    MovieList movieList;
    Movie movie;
    Movie movie2;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_search, container, false);

        adapter = new MovieAdapter();

        recyclerView = rootView.findViewById(R.id.recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        recyclerView.setAdapter(adapter);


        SearchView searchView = rootView.findViewById(R.id.searchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                makeRequest(query);

                adapter.notifyDataSetChanged();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        movie = new Movie();

        movie.title = "movie title";

        movie.director = "director";
        movie.actor = "person1, person2, person3";
        movie.userRating = 5;

        return rootView;
    }

    public void makeRequest(final String query) {
        StringRequest request = new StringRequest(Request.Method.GET, urlStr + query, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                processResponse(response);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("X-Naver-Client-Id", clientId);
                params.put("X-Naver-Client-Secret", clientSecret);

                return params;
            }
        };

        request.setShouldCache(false);
        MainActivity.requestQueue.add(request);


    }

    private void processResponse(String response) {
        Gson gson = new Gson();
        //adapter.clearItems();
        String n = stripHtml(response);

        movieList = gson.fromJson(n, MovieList.class);
//        for (int i =0; i< movieList.items.size(); i++) {
//            Movie movie = movieList.items.get(i);
//            adapter.addItem(movie);
//        }


        clearMovie();
        insertMovie(movieList);
        adapter.setItems(movieList.items);

        adapter.notifyDataSetChanged();

    }

    private void clearMovie() {
        Uri uri = new Uri.Builder().build().parse(uriString);
        int count = getActivity().getContentResolver().delete(uri, null, null);
    }

    public String stripHtml(String html) {
        return Html.fromHtml(html).toString();
    }

    private void insertMovie(MovieList movieList) {
        Uri uri = new Uri.Builder().build().parse(uriString);
        Cursor cursor = getActivity().getContentResolver().query(uri, null, null,
                null, null);
        if (movieList.items.size() != 0) {
            for (int i = 0; i < movieList.items.size(); i++) {
                Movie movie = movieList.items.get(i);
                ContentValues values = new ContentValues();
                values.put("title", movie.title);
                values.put("director", movie.director);
                values.put("actor", movie.actor);
                values.put("link", movie.link);
                values.put("rating", movie.userRating);
                values.put("image", movie.image);
                values.put("pubDate", movie.pubDate);

                uri = getActivity().getContentResolver().insert(uri, values);
            }

        }
    }
    public void onPause() {
        super.onPause();

    }
    public void onResume() {
        super.onResume();
        queryPerson();



        }

    private void queryPerson() { Uri uri = new Uri.Builder().build(). parse (uriString);

    String[] columns = new String[] {"title", "director", "actor","rating","image","pubDate"};
    Cursor cursor = getActivity().getContentResolver().query(uri, columns, null, null, null);

        while (cursor.moveToNext())
        {
            movie2 =new Movie();
            movie2.title =cursor.getString(cursor.getColumnIndex(columns[0]));
            movie2.director = cursor.getString(cursor.getColumnIndex(columns[1]));
            movie2.actor = cursor.getString(cursor.getColumnIndex(columns[2]));
            movie2.userRating = cursor.getFloat(cursor.getColumnIndex(columns[3]));
            movie2.image = cursor.getString(cursor.getColumnIndex(columns[4]));
            movie2.pubDate = cursor.getString(cursor.getColumnIndex(columns[5]));
            adapter.addItem(movie2);
            adapter.notifyDataSetChanged();



        }  }
}

