package com.example.reneewu.codepathassignment1;

import android.os.Bundle;
import android.util.Log;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Response;

public class PlayYoutubeActivity extends YouTubeBaseActivity {

    @BindView(R.id.player)
    YouTubePlayerView youTubePlayerView;

    private String youtubeApiKey="Your YouTube API KEY";
    private String youtubeKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_youtube);
        ButterKnife.bind(this);


        // get video key
        // sample query https://api.themoviedb.org/3/movie/329865/videos?api_key=a07e22bc18f5cb106bfe4cc1f83ad8ed&language=en-US
        int movieId = getIntent().getIntExtra("MovieId", 0);
        String videoRequest = String.format("https://api.themoviedb.org/3/movie/%d/videos?api_key=a07e22bc18f5cb106bfe4cc1f83ad8ed&language=en-US",movieId);

        /*
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(videoRequest,new TextHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                Gson gson = new GsonBuilder().create();
                // Define Response class to correspond to the JSON response returned
                youtubeKey = gson.fromJson(responseString, videosObject.class).results.get(0).key;
                initialYoutube(youtubeKey);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String res, Throwable t) {
                // called when response HTTP status is "4XX" (eg. 401, 403, 404)
                Log.e("ERROR", t.toString());
                Toast.makeText(PlayYoutubeActivity.this, t.toString(), Toast.LENGTH_SHORT).show();
            }
        });*/

        //OKHttp3
        OkHttpClient client = new OkHttpClient();

        okhttp3.Request request = new okhttp3.Request.Builder()
                .url(videoRequest)
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
                // Define Response class to correspond to the JSON response returned
                youtubeKey = gson.fromJson(response.body().charStream(), videosObject.class).results.get(0).key;


                runOnUiThread(new Runnable(){
                    @Override
                    public void run(){
                        initialYoutube(youtubeKey);
                    }
                });
            }
        });

    }

    private void initialYoutube(final String videoKey){
        youTubePlayerView.initialize(youtubeApiKey,
                new YouTubePlayer.OnInitializedListener() {
                    @Override
                    public void onInitializationSuccess(YouTubePlayer.Provider provider,
                                                        YouTubePlayer youTubePlayer, boolean b) {

                        // do any work here to cue video, play video, etc.
                        youTubePlayer.loadVideo(videoKey);
                    }

                    @Override
                    public void onInitializationFailure(YouTubePlayer.Provider provider,
                                                        YouTubeInitializationResult youTubeInitializationResult) {

                    }
                });
    }
}
