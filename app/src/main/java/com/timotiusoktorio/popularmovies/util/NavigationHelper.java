package com.timotiusoktorio.popularmovies.util;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.timotiusoktorio.popularmovies.data.model.Review;
import com.timotiusoktorio.popularmovies.ui.about.AboutActivity;
import com.timotiusoktorio.popularmovies.ui.moviedetails.DetailsActivity;
import com.timotiusoktorio.popularmovies.ui.review.ReviewActivity;

public class NavigationHelper {

    public static final String INTENT_EXTRA_MOVIE_ID = "movie_id";
    public static final String INTENT_EXTRA_REVIEW = "review";

    public static void addFragmentToActivity(@NonNull FragmentManager fragmentManager, @NonNull Fragment fragment, int frameId, @Nullable String fragmentTag) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(frameId, fragment, fragmentTag);
        transaction.commit();
    }

    public static void replaceFragmentInActivity(@NonNull FragmentManager fragmentManager, @NonNull Fragment fragment, int frameId, @Nullable String fragmentTag) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(frameId, fragment, fragmentTag);
        transaction.commit();
    }

    public static void removeFragmentFromActivity(@NonNull FragmentManager fragmentManager, @NonNull Fragment fragment) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.remove(fragment);
        transaction.commit();
    }

    public static void startDetailsActivity(@NonNull Activity activity, long movieId) {
        Intent intent = new Intent(activity, DetailsActivity.class);
        intent.putExtra(INTENT_EXTRA_MOVIE_ID, movieId);
        activity.startActivityForResult(intent, DetailsActivity.INTENT_REQUEST_CODE);
    }

    public static void startAboutActivity(@NonNull Activity activity) {
        Intent intent = new Intent(activity, AboutActivity.class);
        activity.startActivity(intent);
    }

    public static void startReviewActivity(@NonNull Activity activity, @NonNull Review review) {
        Intent intent = new Intent(activity, ReviewActivity.class);
        intent.putExtra(INTENT_EXTRA_REVIEW, review);
        activity.startActivity(intent);
    }

    public static void startActionViewIntent(@NonNull Activity activity, @NonNull Uri uri) {
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        if (intent.resolveActivity(activity.getPackageManager()) != null) {
            activity.startActivity(intent);
        }
    }
}