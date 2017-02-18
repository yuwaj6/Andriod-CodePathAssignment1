package com.example.reneewu.codepathassignment1;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;

/**
 * Created by reneewu on 2/13/2017.
 */

public class MovieListAdapter extends BaseAdapter {
    private Context mContext;
    private MovieDbObject mData;
    private int screenWidth;

    static class ViewHolder {
        @Nullable
        @BindView(R.id.text_title) TextView tvTitle;

        @Nullable
        @BindView(R.id.text_overview)TextView tvOverview;

        @BindView(R.id.image_poster)ImageView ivBasicImage;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    public MovieListAdapter(Context context, MovieDbObject data) {
        mContext = context;
        mData = data;
        screenWidth = mContext.getResources().getDisplayMetrics().widthPixels;
    }

    @Override
    public int getCount() {
        return mData.results.size();
    }

    @Override
    public Object getItem(int position) {
        return mData.results.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public int getItemViewType(int position) {

        int orientation = mContext.getResources().getConfiguration().orientation;

        if (mData.results.get(position).vote_average < 5.0f) {
            return 0; //產生類型0的layout
        } else if(orientation == Configuration.ORIENTATION_PORTRAIT){
            return 1; //產生類型1的layout
        } else{
            return 2;
        }
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        MovieObject item = (MovieObject) getItem(position);

        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag
        int type = getItemViewType(position);

        if (convertView == null) {
            //convertView = LayoutInflater.from(mContext).inflate(R.layout.list_item_type1, parent, false);
            //產生不同的item layout
            if (type == 0) {
                convertView = LayoutInflater.from(mContext).inflate(R.layout.list_item_type1, parent, false);
            } else if(type==1){
                convertView = LayoutInflater.from(mContext).inflate(R.layout.list_item_type2, parent, false);
            } else{

                convertView = LayoutInflater.from(mContext).inflate(R.layout.list_item_popular_landscape, parent, false);
            }

            viewHolder = new ViewHolder(convertView);

            // Cache the viewHolder object inside the fresh view
            convertView.setTag(viewHolder);

        } else {
            // View is being recycled, retrieve the viewHolder object from tag
            viewHolder = (ViewHolder) convertView.getTag();
        }

        if(type==0){
            // Lookup view for data population
            viewHolder.tvTitle.setText(item.title);
            viewHolder.tvOverview.setText(item.overview);

            // https://developers.themoviedb.org/3/getting-started/images
            String imagePath = "";
            int orientation = mContext.getResources().getConfiguration().orientation;
            if (orientation == Configuration.ORIENTATION_PORTRAIT) {
                imagePath = item.poster_path;
            } else if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
                imagePath = item.backdrop_path;
            }
            String imageUri = "https://image.tmdb.org/t/p/w500" + imagePath;

            Picasso.with(mContext).load(imageUri)/*.fit().centerCrop()*/
                    .transform(new RoundedCornersTransformation(10, 10))
                    .placeholder(R.drawable.placeholder)
                    .error(R.drawable.error_placeholder)
                    .into(viewHolder.ivBasicImage);

            //set this item click listener
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    launchDetailsView(position);
                }
            });
        }
        else{
            String imageUri = "https://image.tmdb.org/t/p/w500" + item.backdrop_path;

            if(type==1){
                Picasso.with(mContext).load(imageUri)
                        //.fit()
                        .resize(screenWidth, 0)
                        .transform(new RoundedCornersTransformation(10, 10))
                        .placeholder(R.drawable.placeholder)
                        .error(R.drawable.error_placeholder)
                        .into(viewHolder.ivBasicImage);
            } else {
                viewHolder.tvTitle.setText(item.title);
                viewHolder.tvOverview.setText(item.overview);

                Picasso.with(mContext).load(imageUri)
                        .transform(new RoundedCornersTransformation(10, 10))
                        .placeholder(R.drawable.placeholder)
                        .error(R.drawable.error_placeholder)
                        .into(viewHolder.ivBasicImage);
            }

            //set this item click listener
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    launchYoutubeView(position);
                }
            });

        }

        // Return the completed view to render on screen
        return convertView;

    }

    @Override
    public int getViewTypeCount() {
        return 3; //這個ListView有兩種類型的item layout
    }

    private void launchYoutubeView(int position) {
        Intent i = new Intent(mContext, PlayYoutubeActivity.class);
        i.putExtra("MovieId", mData.results.get(position).id);
        // brings up the youtube activity
        mContext.startActivity(i);
    }

    private void launchDetailsView(int position) {
        Intent i = new Intent(mContext, DetailActivity.class);
        i.putExtra("MovieObject", mData.results.get(position));
        mContext.startActivity(i);
    }
}