package com.example.reneewu.codepathassignment1;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;

public class DetailActivity extends AppCompatActivity {
    private MovieObject item;
    @BindView(R.id.text_title) TextView tvTitle;
    @BindView(R.id.text_overview)TextView tvOverview;
    @BindView(R.id.image_poster)ImageView ivBasicImage;
    @BindView(R.id.rating)RatingBar rating;

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
            String imageUri = "https://image.tmdb.org/t/p/w500" + item.backdrop_path;

            Picasso.with(this).load(imageUri)
                    .resize(this.getResources().getDisplayMetrics().widthPixels, 0)
                    .transform(new RoundedCornersTransformation(10, 10))
                    .placeholder(R.drawable.placeholder)
                    .error(R.drawable.error_placeholder)
                    .into(ivBasicImage);

            rating.setRating(item.vote_average);

            ivBasicImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    launchYoutubeView(item.id);
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
}
