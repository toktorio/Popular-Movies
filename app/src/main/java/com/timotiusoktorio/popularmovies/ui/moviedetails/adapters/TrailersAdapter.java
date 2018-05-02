package com.timotiusoktorio.popularmovies.ui.moviedetails.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.timotiusoktorio.popularmovies.R;
import com.timotiusoktorio.popularmovies.data.model.Trailer;
import com.timotiusoktorio.popularmovies.data.source.remote.MovieApiContract;
import com.timotiusoktorio.popularmovies.ui.moviedetails.DetailsFragment;

import java.util.List;

public class TrailersAdapter extends RecyclerView.Adapter<TrailersAdapter.ViewHolder> {

    private List<Trailer> mTrailers;
    private DetailsFragment.DetailsFragmentListener mFragmentListener;

    public TrailersAdapter(@NonNull List<Trailer> trailers, @NonNull DetailsFragment.DetailsFragmentListener fragmentListener) {
        mTrailers = trailers;
        mFragmentListener = fragmentListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_trailers, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        final Trailer trailer = mTrailers.get(position);
        final String trailerKey = trailer.getKey();

        String trailerPosterUrl = MovieApiContract.YOUTUBE_IMAGE_URL_PREFIX + trailerKey + MovieApiContract.YOUTUBE_IMAGE_URL_SUFFIX;
        Picasso.get().load(trailerPosterUrl).into(holder.trailerPosterImageView);

        holder.trailerLabelTextView.setText(trailer.getName());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mFragmentListener.onTrailerClicked(trailerKey);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mTrailers.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView trailerPosterImageView;
        private TextView trailerLabelTextView;

        ViewHolder(View itemView) {
            super(itemView);
            trailerPosterImageView = itemView.findViewById(R.id.trailer_poster_image_view);
            trailerLabelTextView = itemView.findViewById(R.id.trailer_label_text_view);
        }
    }
}