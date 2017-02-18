package com.example.reneewu.codepathassignment1;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {
    MovieDbObject m_data;
    //ArrayList<MovieObject> items;
    @BindView(R.id.list_movie) ListView lvItems;
    MovieListAdapter movieListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        //lvItems = (ListView) findViewById(R.id.list_movie);

        String url = "https://api.themoviedb.org/3/movie/now_playing?api_key=a07e22bc18f5cb106bfe4cc1f83ad8ed";
        /*
        // Use AsyncHttpClient
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(url, new TextHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                Gson gson = new GsonBuilder().create();
                //Define Response class to correspond to the JSON response returned
                m_data = gson.fromJson(responseString, MovieDbObject.class);
                movieListAdapter = new MovieListAdapter(getBaseContext(), m_data);
                lvItems.setAdapter(movieListAdapter);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String res, Throwable t) {
            }
        });
        */

        //OKHttp3
        OkHttpClient client = new OkHttpClient();

        okhttp3.Request request = new okhttp3.Request.Builder()
                .url(url)
                .build();

        // Get a handler that can be used to post to the main thread
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("ERROR", e.toString());
            }

            // Parse response using gson deserializer
            @Override
            public void onResponse(okhttp3.Call call, final Response response) throws IOException {
                Gson gson = new GsonBuilder().create();
                //Define Response class to correspond to the JSON response returned
                m_data = gson.fromJson(response.body().charStream(), MovieDbObject.class);

                runOnUiThread(new Runnable(){
                    @Override
                    public void run(){
                        setList();
                    }
                });
            }
        });
    }

    private void setList(){
        movieListAdapter = new MovieListAdapter(getBaseContext(), m_data);
        lvItems.setAdapter(movieListAdapter);
    }
}
