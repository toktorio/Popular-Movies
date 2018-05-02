package com.timotiusoktorio.popularmovies.util;

import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.view.View;

import com.timotiusoktorio.popularmovies.R;

public class SnackbarHelper {

    @SuppressWarnings("unused")
    public static void showNetworkUnavailableSnackbar(@NonNull View view) {
        Snackbar.make(view, R.string.message_network_unavailable, Snackbar.LENGTH_SHORT).show();
    }

    public static void showFavoriteToggledSnackbar(boolean isMovieSaved, @NonNull View view) {
        if (isMovieSaved) {
            Snackbar.make(view, R.string.message_saved_to_favorites, Snackbar.LENGTH_SHORT).show();
        } else {
            Snackbar.make(view, R.string.message_removed_from_favorites, Snackbar.LENGTH_SHORT).show();
        }
    }
}