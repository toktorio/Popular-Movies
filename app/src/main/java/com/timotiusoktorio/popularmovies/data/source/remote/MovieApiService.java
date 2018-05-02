package com.timotiusoktorio.popularmovies.data.source.remote;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;

import com.timotiusoktorio.popularmovies.R;
import com.timotiusoktorio.popularmovies.data.model.Movie;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.List;

import static com.timotiusoktorio.popularmovies.data.source.remote.MovieApiContract.TMDB_CREDITS_URL_SUFFIX;
import static com.timotiusoktorio.popularmovies.data.source.remote.MovieApiContract.TMDB_MOVIE_URL;
import static com.timotiusoktorio.popularmovies.data.source.remote.MovieApiContract.TMDB_PARAM_API_KEY;
import static com.timotiusoktorio.popularmovies.data.source.remote.MovieApiContract.TMDB_REVIEWS_URL_SUFFIX;
import static com.timotiusoktorio.popularmovies.data.source.remote.MovieApiContract.TMDB_VIDEOS_URL_SUFFIX;

public class MovieApiService {

    private static final String TAG = MovieApiService.class.getSimpleName();
    private static final int READ_TIMEOUT = 10000;
    private static final int CONNECT_TIMEOUT = 15000;

    private static MovieApiService sInstance;
    private String mApiKey;

    public static synchronized MovieApiService getInstance(@NonNull Context context) {
        if (sInstance == null) {
            sInstance = new MovieApiService(context);
        }
        return sInstance;
    }

    private MovieApiService(@NonNull Context context) {
        mApiKey = context.getString(R.string.tmdb_api_key);
    }

    public List<Movie> getMovies(String movieUrl) {
        List<Movie> movies = null;
        String rawMovieData = getRawMovieData(movieUrl);
        if (TextUtils.isEmpty(rawMovieData)) {
            return null;
        }
        try {
            movies = JsonHelper.extractMoviesFromJsonString(rawMovieData);
        } catch (JSONException e) {
            Log.e(TAG, e.getMessage(), e);
        }
        return movies;
    }

    public Movie getMovieDetails(long movieId) {
        Movie movie = null;
        String rawMovieDetailsData = getRawMovieData(TMDB_MOVIE_URL + movieId);
        String rawMovieCreditsData = getRawMovieData(TMDB_MOVIE_URL + movieId + TMDB_CREDITS_URL_SUFFIX);
        String rawMovieTrailersData = getRawMovieData(TMDB_MOVIE_URL + movieId + TMDB_VIDEOS_URL_SUFFIX);
        String rawMovieReviewsData = getRawMovieData(TMDB_MOVIE_URL + movieId + TMDB_REVIEWS_URL_SUFFIX);
        if (TextUtils.isEmpty(rawMovieDetailsData)) {
            return null;
        }
        try {
            movie = JsonHelper.extractMovieDetailsFromJsonString(rawMovieDetailsData);
            movie.setCasts(JsonHelper.extractCastsFromJsonString(rawMovieCreditsData));
            movie.setTrailers(JsonHelper.extractTrailersFromJsonString(rawMovieTrailersData));
            movie.setReviews(JsonHelper.extractReviewsFromJsonString(rawMovieReviewsData, movie.getTitle()));
        } catch (JSONException e) {
            Log.e(TAG, e.getMessage(), e);
        }
        return movie;
    }

    private String getRawMovieData(String movieUrl) {
        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        BufferedReader reader = null;

        try {
            Uri uri = Uri.parse(movieUrl).buildUpon().appendQueryParameter(TMDB_PARAM_API_KEY, mApiKey).build();
            URL url = new URL(uri.toString());
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setReadTimeout(READ_TIMEOUT);
            urlConnection.setConnectTimeout(CONNECT_TIMEOUT);
            urlConnection.connect();

            int responseCode = urlConnection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                inputStream = urlConnection.getInputStream();
                reader = new BufferedReader(new InputStreamReader(inputStream, Charset.forName("UTF-8")));

                String line;
                StringBuilder builder = new StringBuilder();
                while ((line = reader.readLine()) != null) {
                    builder.append(line).append("\n");
                }

                if (builder.length() == 0) return null;
                return builder.toString();
            }
        } catch (IOException e) {
            Log.e(TAG, e.getMessage(), e);
        } finally {
            try {
                if (urlConnection != null) urlConnection.disconnect();
                if (inputStream != null) inputStream.close();
                if (reader != null) reader.close();
            } catch (IOException e) {
                Log.e(TAG, e.getMessage(), e);
            }
        }
        return null;
    }
}