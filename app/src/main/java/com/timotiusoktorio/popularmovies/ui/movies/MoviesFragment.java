package com.timotiusoktorio.popularmovies.ui.movies;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.timotiusoktorio.popularmovies.R;
import com.timotiusoktorio.popularmovies.data.model.Movie;
import com.timotiusoktorio.popularmovies.util.GridDividerItemDecoration;

import java.util.ArrayList;
import java.util.List;

public class MoviesFragment extends Fragment implements MoviesContract.View {

    private static final String TAG = MoviesFragment.class.getSimpleName();
    private static final String ARG_SECTION_ID = "section_id";

    private MoviesFragmentListener mFragmentListener;
    private MoviesContract.Presenter mPresenter;
    private MoviesAdapter mAdapter;

    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView mRecyclerView;
    private View mEmptyView;
    private ProgressBar mProgressBar;

    public static MoviesFragment newInstance(MoviesSection section) {
        Log.d(TAG, "newInstance: section: " + section);
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_ID, section.getId());
        MoviesFragment moviesFragment = new MoviesFragment();
        moviesFragment.setArguments(args);
        return moviesFragment;
    }

    public MoviesFragment() {
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mFragmentListener = (MoviesFragmentListener) context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() == null) {
            throw new IllegalStateException("No arguments sent here");
        }
        int sectionId = getArguments().getInt(ARG_SECTION_ID, 0);
        MoviesSection section = MoviesSection.fromId(sectionId);
        mPresenter = new MoviesPresenter(getContext(), section, getLoaderManager(), this);
        mAdapter = new MoviesAdapter(new ArrayList<Movie>(), mFragmentListener);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_movies, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mSwipeRefreshLayout = view.findViewById(R.id.swipe_refresh_layout);
        mRecyclerView = view.findViewById(R.id.recycler_view);
        mEmptyView = view.findViewById(R.id.empty_view);
        mProgressBar = view.findViewById(R.id.progress_bar);

        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorAccent);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPresenter.refreshData();
            }
        });

        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new GridLayoutManager(requireContext(), getResources().getInteger(R.integer.total_columns)));
        mRecyclerView.addItemDecoration(new GridDividerItemDecoration(requireContext(), R.dimen.movies_rv_grid_item_offset));
        mRecyclerView.setHasFixedSize(true);

        TextView emptyTextView = view.findViewById(R.id.empty_text_view);
        emptyTextView.setText(R.string.text_movies_empty);

        Button refreshButton = view.findViewById(R.id.refresh_button);
        refreshButton.setText(R.string.label_refresh);
        refreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.refreshData();
            }
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mPresenter.initDataLoad();
    }

    @Override
    public void showLoadingIndicator() {
        mProgressBar.setVisibility(View.VISIBLE);
        mRecyclerView.setVisibility(View.GONE);
        mEmptyView.setVisibility(View.GONE);
    }

    @Override
    public void showMovies(List<Movie> movies) {
        mAdapter.clearData();
        mAdapter.addData(movies);
        mRecyclerView.setVisibility(View.VISIBLE);
        mEmptyView.setVisibility(View.GONE);
        mProgressBar.setVisibility(View.GONE);
        mSwipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void showEmptyView() {
        mEmptyView.setVisibility(View.VISIBLE);
        mRecyclerView.setVisibility(View.GONE);
        mProgressBar.setVisibility(View.GONE);
        mSwipeRefreshLayout.setRefreshing(false);
    }

    public void refresh() {
        if (mPresenter != null) {
            mPresenter.refreshData();
        }
    }

    public interface MoviesFragmentListener {

        void onMovieClicked(long movieId);
    }
}