package com.timotiusoktorio.popularmovies.ui.movies;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.timotiusoktorio.popularmovies.R;
import com.timotiusoktorio.popularmovies.data.model.Review;
import com.timotiusoktorio.popularmovies.data.source.local.PreferencesHelper;
import com.timotiusoktorio.popularmovies.data.source.remote.MovieApiContract;
import com.timotiusoktorio.popularmovies.ui.moviedetails.DetailsActivity;
import com.timotiusoktorio.popularmovies.ui.moviedetails.DetailsFragment;
import com.timotiusoktorio.popularmovies.util.NavigationHelper;

public class MoviesActivity extends AppCompatActivity implements SortByDialogFragment.SortByDialogFragmentListener,
        MoviesFragment.MoviesFragmentListener, DetailsFragment.DetailsFragmentListener {

    private long mSelectedMovieId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movies);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        int selectedSectionId = PreferencesHelper.getInstance(this).getSelectedSection();
        MoviesSection selectedSection = MoviesSection.fromId(selectedSectionId);
        setActionBarTitle(selectedSection.getTitleResId());
        startMoviesFragment(selectedSection);
    }

    private void setActionBarTitle(int titleResId) {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(titleResId);
        }
    }

    private void startMoviesFragment(MoviesSection section) {
        MoviesFragment moviesFragment = MoviesFragment.newInstance(section);
        NavigationHelper.replaceFragmentInActivity(getSupportFragmentManager(), moviesFragment,
                R.id.movies_fragment_container, "movies_fragment");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_sort_by: {
                showSortByDialog();
                return true;
            }
            case R.id.action_about: {
                NavigationHelper.startAboutActivity(this);
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private void showSortByDialog() {
        SortByDialogFragment sortByDialogFragment = new SortByDialogFragment();
        sortByDialogFragment.show(getFragmentManager(), "sort_by_dialog");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == DetailsActivity.INTENT_REQUEST_CODE && resultCode == RESULT_OK) {
            refreshFavoritesFragment();
        }
    }

    private void refreshFavoritesFragment() {
        int selectedSectionId = PreferencesHelper.getInstance(this).getSelectedSection();
        if (selectedSectionId == MoviesSection.FAVORITES.getId()) {
            MoviesFragment moviesFragment = (MoviesFragment) getSupportFragmentManager().findFragmentByTag("movies_fragment");
            if (moviesFragment != null) {
                moviesFragment.refresh();
            }
            removeDetailsFragment();
            mSelectedMovieId = -1;
        }
    }

    private void removeDetailsFragment() {
        DetailsFragment detailsFragment = (DetailsFragment) getSupportFragmentManager().findFragmentByTag("details_fragment");
        if (detailsFragment != null) {
            NavigationHelper.removeFragmentFromActivity(getSupportFragmentManager(), detailsFragment);
        }
    }

    @Override
    public void onSectionSelected(int sectionId) {
        int selectedSectionId = PreferencesHelper.getInstance(this).getSelectedSection();
        if (selectedSectionId == sectionId) {
            return;
        }

        PreferencesHelper.getInstance(this).setSelectedSection(sectionId);
        MoviesSection section = MoviesSection.fromId(sectionId);
        setActionBarTitle(section.getTitleResId());
        startMoviesFragment(section);

        removeDetailsFragment();
        mSelectedMovieId = -1;
    }

    @Override
    public void onMovieClicked(long movieId) {
        boolean isTwoPane = getResources().getBoolean(R.bool.is_two_pane);
        if (!isTwoPane) {
            NavigationHelper.startDetailsActivity(this, movieId);
            return;
        }

        if (movieId != mSelectedMovieId) {
            mSelectedMovieId = movieId;
            DetailsFragment detailsFragment = DetailsFragment.newInstance(movieId);
            NavigationHelper.replaceFragmentInActivity(getSupportFragmentManager(), detailsFragment,
                    R.id.details_fragment_container, "details_fragment");
        }
    }

    @Override
    public void onFavoriteToggled() {
        refreshFavoritesFragment();
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