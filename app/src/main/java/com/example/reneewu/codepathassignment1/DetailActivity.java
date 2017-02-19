package com.example.reneewu.codepathassignment1;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.RatingBar;
import android.widget.TextView;

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

public class DetailActivity extends YouTubeBaseActivity {
    private MovieObject item;
    @BindView(R.id.text_title) TextView tvTitle;
    @BindView(R.id.text_overview)TextView tvOverview;
    //@BindView(R.id.image_poster)ImageView ivBasicImage;
    @BindView(R.id.rating)RatingBar rating;
    @BindView(R.id.player)
    YouTubePlayerView youTubePlayerView;

    private String youtubeApiKey="Your YouTube API KEY";
    private String youtubeKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);

        try {
            Intent intent = getIntent();
            item = (MovieObject) intent.getExtras().getSerializable("MovieObject");
            // Lookup view for data population
            tvTitle.setText(item.title);
            tvOverview.setText(item.overview);
            rating.setRating(item.vote_average);

            /*
            String imageUri = "https://image.tmdb.org/t/p/w500" + item.backdrop_path;

            Picasso.with(this).load(imageUri)
                    .resize(this.getResources().getDisplayMetrics().widthPixels, 0)
                    .transform(new RoundedCornersTransformation(10, 10))
                    .placeholder(R.drawable.placeholder)
                    .error(R.drawable.error_placeholder)
                    .into(ivBasicImage);



            ivBasicImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    launchYoutubeView(item.id);
                }
            });*/

            // get video
            String videoRequest = String.format("https://api.themoviedb.org/3/movie/%d/videos?api_key=a07e22bc18f5cb106bfe4cc1f83ad8ed&language=en-US",item.id);

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
        } catch (Exception e) {

        }
    }

    private void launchYoutubeView(int movieId) {
        Intent i = new Intent(this, PlayYoutubeActivity.class);
        i.putExtra("MovieId", movieId);

        // brings up the youtube activity
        this.startActivity(i);
    }

    private void initialYoutube(final String videoKey){
        youTubePlayerView.initialize(youtubeApiKey,
                new YouTubePlayer.OnInitializedListener() {
                    @Override
                    public void onInitializationSuccess(YouTubePlayer.Provider provider,
                                                        YouTubePlayer youTubePlayer, boolean b) {

                        // do any work here to cue video, play video, etc.
                        youTubePlayer.cueVideo(videoKey);
                    }

                    @Override
                    public void onInitializationFailure(YouTubePlayer.Provider provider,
                                                        YouTubeInitializationResult youTubeInitializationResult) {

                    }
                });
    }
}
