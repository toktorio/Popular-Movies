package com.timotiusoktorio.popularmovies.ui.review;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import com.timotiusoktorio.popularmovies.R;
import com.timotiusoktorio.popularmovies.data.model.Review;
import com.timotiusoktorio.popularmovies.util.NavigationHelper;

public class ReviewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        Review review = getIntent().getParcelableExtra(NavigationHelper.INTENT_EXTRA_REVIEW);
        TextView titleTextView = findViewById(R.id.title_text_view);
        titleTextView.setText(review.getMovieTitle());

        TextView reviewAuthorTextView = findViewById(R.id.review_author_text_view);
        reviewAuthorTextView.setText(getString(R.string.string_format_review_author, review.getAuthor()));

        TextView reviewContentTextView = findViewById(R.id.review_content_text_view);
        reviewContentTextView.setText(review.getContent());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}