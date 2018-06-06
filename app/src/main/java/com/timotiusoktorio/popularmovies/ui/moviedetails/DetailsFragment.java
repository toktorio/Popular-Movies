package com.timotiusoktorio.popularmovies.ui.moviedetails;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.ShareActionProvider;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.timotiusoktorio.popularmovies.R;
import com.timotiusoktorio.popularmovies.data.model.Cast;
import com.timotiusoktorio.popularmovies.data.model.Movie;
import com.timotiusoktorio.popularmovies.data.model.Review;
import com.timotiusoktorio.popularmovies.data.model.Trailer;
import com.timotiusoktorio.popularmovies.data.source.local.MovieDbHelper;
import com.timotiusoktorio.popularmovies.data.source.remote.MovieApiContract;
import com.timotiusoktorio.popularmovies.ui.moviedetails.adapters.CastsAdapter;
import com.timotiusoktorio.popularmovies.ui.moviedetails.adapters.ReviewsAdapter;
import com.timotiusoktorio.popularmovies.ui.moviedetails.adapters.TrailersAdapter;
import com.timotiusoktorio.popularmovies.util.SnackbarHelper;

import java.util.List;

public class DetailsFragment extends Fragment implements DetailsContract.View, View.OnClickListener {

    private static final String TAG = DetailsFragment.class.getSimpleName();
    private static final String ARG_MOVIE_ID = "movie_id";

    private DetailsFragmentListener mFragmentListener;
    private DetailsContract.Presenter mPresenter;

    private CoordinatorLayout mRootContainer;
    private NestedScrollView mMainContainer;
    private ImageView mBackdropImageView;
    private TextView mTitleTextView;
    private TextView mReleaseDateTextView;
    private TextView mRuntimeTextView;
    private TextView mGenresTextView;
    private RatingBar mRatingBar;
    private TextView mOverviewTextView;
    private CardView mCastsContainer;
    private RecyclerView mCastsRecyclerView;
    private CardView mTrailersContainer;
    private RecyclerView mTrailersRecyclerView;
    private CardView mReviewsContainer;
    private RecyclerView mReviewsRecyclerView;
    private FloatingActionButton mFavoriteFab;
    private ViewGroup mEmptyView;
    private ProgressBar mProgressBar;
    private ShareActionProvider mShareActionProvider;

    public static DetailsFragment newInstance(long movieId) {
        Log.d(TAG, "newInstance: movieId: " + movieId);
        Bundle args = new Bundle();
        args.putLong(ARG_MOVIE_ID, movieId);
        DetailsFragment detailsFragment = new DetailsFragment();
        detailsFragment.setArguments(args);
        return detailsFragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mFragmentListener = (DetailsFragmentListener) context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        if (getArguments() == null) {
            throw new IllegalStateException("No arguments sent here");
        }

        long movieId = getArguments().getLong(ARG_MOVIE_ID, -1);
        MovieDbHelper dbHelper = MovieDbHelper.getInstance(getActivity());
        mPresenter = new DetailsPresenter(getActivity(), movieId, getLoaderManager(), dbHelper, this, mFragmentListener);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_details, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Toolbar toolbar = view.findViewById(R.id.toolbar);
        boolean isTwoPane = getResources().getBoolean(R.bool.is_two_pane);
        if (isTwoPane) {
            toolbar.setVisibility(View.GONE);
        } else {
            ((AppCompatActivity) requireActivity()).setSupportActionBar(toolbar);
            ActionBar actionBar = ((AppCompatActivity) requireActivity()).getSupportActionBar();
            if (actionBar != null) {
                actionBar.setDisplayHomeAsUpEnabled(true);
                actionBar.setDisplayShowTitleEnabled(false);
            }
        }

        mRootContainer = view.findViewById(R.id.root_container);
        mMainContainer = view.findViewById(R.id.main_container);
        mBackdropImageView = view.findViewById(R.id.backdrop_image_view);
        mTitleTextView = view.findViewById(R.id.title_text_view);
        mReleaseDateTextView = view.findViewById(R.id.release_date_text_view);
        mRuntimeTextView = view.findViewById(R.id.runtime_text_view);
        mGenresTextView = view.findViewById(R.id.genres_text_view);
        mRatingBar = view.findViewById(R.id.rating_bar);
        mOverviewTextView = view.findViewById(R.id.overview_text_view);
        mCastsContainer = view.findViewById(R.id.casts_container);
        mCastsRecyclerView = view.findViewById(R.id.casts_recycler_view);
        mTrailersContainer = view.findViewById(R.id.trailers_container);
        mTrailersRecyclerView = view.findViewById(R.id.trailers_recycler_view);
        mReviewsContainer = view.findViewById(R.id.reviews_container);
        mReviewsRecyclerView = view.findViewById(R.id.reviews_recycler_view);
        mEmptyView = view.findViewById(R.id.empty_view);
        mProgressBar = view.findViewById(R.id.progress_bar);

        mFavoriteFab = view.findViewById(R.id.favorite_fab);
        mFavoriteFab.setOnClickListener(this);

        TextView emptyTextView = view.findViewById(R.id.empty_text_view);
        emptyTextView.setText(R.string.text_movie_details_empty);

        Button refreshButton = view.findViewById(R.id.refresh_button);
        refreshButton.setText(R.string.label_try_again);
        refreshButton.setOnClickListener(this);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mPresenter.initDataLoad();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_details, menu);

        MenuItem shareMenuItem = menu.findItem(R.id.action_share);
        mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(shareMenuItem);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            requireActivity().finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void showLoadingIndicator() {
        mMainContainer.setVisibility(View.INVISIBLE);
        mFavoriteFab.setVisibility(View.INVISIBLE);
        mEmptyView.setVisibility(View.GONE);
        mProgressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void showMovieDetails(Movie movie) {
        String backdropUrl = MovieApiContract.TMDB_MOVIE_BACKDROP_URL + movie.getBackdropPath();
        Picasso.get().load(backdropUrl).into(mBackdropImageView);

        ActionBar actionBar = ((AppCompatActivity) requireActivity()).getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(movie.getTitle());
            actionBar.setDisplayShowTitleEnabled(true);
        }

        mTitleTextView.setText(movie.getTitle());
        mReleaseDateTextView.setText(movie.getReleaseDate());
        mRuntimeTextView.setText(getString(R.string.string_format_runtime, movie.getRuntime()));
        mGenresTextView.setText(movie.getGenres());
        mRatingBar.setRating((float) movie.getVoteAverage());
        mOverviewTextView.setText(movie.getOverview());

        mMainContainer.setVisibility(View.VISIBLE);
        mFavoriteFab.setVisibility(View.VISIBLE);
        mEmptyView.setVisibility(View.GONE);
        mProgressBar.setVisibility(View.GONE);
    }

    @Override
    public void updateFavoriteFabIcon(int iconResId) {
        mFavoriteFab.setImageResource(iconResId);
    }

    @Override
    public void showMovieCasts(List<Cast> casts) {
        mCastsRecyclerView.setAdapter(new CastsAdapter(casts));
        mCastsRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));
        mCastsRecyclerView.setHasFixedSize(true);
        mCastsRecyclerView.setNestedScrollingEnabled(false);
        mCastsContainer.setVisibility(View.VISIBLE);
    }

    @Override
    public void showMovieTrailers(List<Trailer> trailers) {
        mTrailersRecyclerView.setAdapter(new TrailersAdapter(trailers, mFragmentListener));
        mTrailersRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));
        mTrailersRecyclerView.setHasFixedSize(true);
        mTrailersRecyclerView.setNestedScrollingEnabled(false);
        mTrailersContainer.setVisibility(View.VISIBLE);
    }

    @Override
    public void updateShareIntent(String message) {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, message);
        mShareActionProvider.setShareIntent(shareIntent);
    }

    @Override
    public void showMovieReviews(List<Review> reviews) {
        mReviewsRecyclerView.setAdapter(new ReviewsAdapter(reviews, mFragmentListener));
        mReviewsRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        mReviewsRecyclerView.addItemDecoration(new DividerItemDecoration(requireContext(), LinearLayoutManager.VERTICAL));
        mReviewsContainer.setVisibility(View.VISIBLE);
    }

    @Override
    public void showEmptyView() {
        mMainContainer.setVisibility(View.INVISIBLE);
        mFavoriteFab.setVisibility(View.INVISIBLE);
        mEmptyView.setVisibility(View.VISIBLE);
        mProgressBar.setVisibility(View.GONE);
    }

    @Override
    public void showFavoriteToggledSnackbar(boolean isMovieSaved) {
        SnackbarHelper.showFavoriteToggledSnackbar(isMovieSaved, mRootContainer);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.favorite_fab) {
            mPresenter.toggleFavorite();
        } else if (id == R.id.refresh_button) {
            mPresenter.refreshData();
        }
    }

    public interface DetailsFragmentListener {

        void onFavoriteToggled();

        void onTrailerClicked(String key);

        void onReviewClicked(Review review);
    }
}