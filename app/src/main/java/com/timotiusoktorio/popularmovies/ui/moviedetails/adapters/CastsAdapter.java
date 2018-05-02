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
import com.timotiusoktorio.popularmovies.data.model.Cast;
import com.timotiusoktorio.popularmovies.data.source.remote.MovieApiContract;

import java.util.List;

public class CastsAdapter extends RecyclerView.Adapter<CastsAdapter.ViewHolder> {

    private List<Cast> mCasts;

    public CastsAdapter(@NonNull List<Cast> casts) {
        mCasts = casts;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_casts, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        Cast cast = mCasts.get(position);

        String castPosterUrl = MovieApiContract.TMDB_MOVIE_POSTER_URL + cast.getProfilePath();
        Picasso.get().load(castPosterUrl).into(holder.castPosterImageView);

        holder.castNameTextView.setText(cast.getName());
        holder.castCharacterTextView.setText(cast.getCharacter());
    }

    @Override
    public int getItemCount() {
        return mCasts.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView castPosterImageView;
        private TextView castNameTextView;
        private TextView castCharacterTextView;

        ViewHolder(View itemView) {
            super(itemView);
            castPosterImageView = itemView.findViewById(R.id.cast_poster_image_view);
            castNameTextView = itemView.findViewById(R.id.cast_name_text_view);
            castCharacterTextView = itemView.findViewById(R.id.cast_character_text_view);
        }
    }
}