package com.timotiusoktorio.popularmovies.ui.moviedetails.adapters;

import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.timotiusoktorio.popularmovies.R;
import com.timotiusoktorio.popularmovies.data.model.Review;
import com.timotiusoktorio.popularmovies.ui.moviedetails.DetailsFragment;

import java.util.List;

public class ReviewsAdapter extends RecyclerView.Adapter<ReviewsAdapter.ViewHolder> {

    private List<Review> mReviews;
    private DetailsFragment.DetailsFragmentListener mFragmentListener;

    public ReviewsAdapter(@NonNull List<Review> reviews, @NonNull DetailsFragment.DetailsFragmentListener fragmentListener) {
        mReviews = reviews;
        mFragmentListener = fragmentListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_reviews, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Review review = mReviews.get(position);

        String reviewText = "<b>" + review.getAuthor() + "</b>" + " - " + review.getContent();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            holder.reviewTextView.setText(Html.fromHtml(reviewText, Html.FROM_HTML_MODE_LEGACY));
        } else {
            holder.reviewTextView.setText(Html.fromHtml(reviewText));
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mFragmentListener.onReviewClicked(review);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mReviews.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView reviewTextView;

        ViewHolder(View itemView) {
            super(itemView);
            reviewTextView = itemView.findViewById(R.id.review_text_view);
        }
    }
}