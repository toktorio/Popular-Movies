package com.timotiusoktorio.popularmovies.ui.moviedetails;

import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.timotiusoktorio.popularmovies.R;
import com.timotiusoktorio.popularmovies.data.model.Review;
import com.timotiusoktorio.popularmovies.data.source.remote.MovieApiContract;
import com.timotiusoktorio.popularmovies.util.NavigationHelper;

public class DetailsActivity extends AppCompatActivity implements DetailsFragment.DetailsFragmentListener {

    public static final int INTENT_REQUEST_CODE = 200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        if (savedInstanceState == null) {
            long movieId = getIntent().getLongExtra(NavigationHelper.INTENT_EXTRA_MOVIE_ID, -1);
            DetailsFragment detailsFragment = DetailsFragment.newInstance(movieId);
            NavigationHelper.addFragmentToActivity(getSupportFragmentManager(), detailsFragment, R.id.details_fragment_container, null);
        }
    }

    @Override
    public void onFavoriteToggled() {
        setResult(RESULT_OK);
    }

    @Override
    public void onTrailerClicked(String key) {
        Uri trailerUri = Uri.parse(MovieApiContract.YOUTUBE_VIDEO_URL + key);
        NavigationHelper.startActionViewIntent(this, trailerUri);
    }

    @Override
    public void onReviewClicked(Review review) {
        NavigationHelper.startReviewActivity(this, review);
    }
}