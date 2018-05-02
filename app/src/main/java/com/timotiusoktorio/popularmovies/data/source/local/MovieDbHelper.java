package com.timotiusoktorio.popularmovies.data.source.local;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.timotiusoktorio.popularmovies.data.model.Movie;

import java.util.ArrayList;
import java.util.List;

public class MovieDbHelper extends SQLiteOpenHelper {

    private static final String TAG = MovieDbHelper.class.getSimpleName();
    private static final String DATABASE_NAME = "movie.db";
    private static final int DATABASE_VERSION = 1;

    private static MovieDbHelper sInstance;

    public static synchronized MovieDbHelper getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new MovieDbHelper(context.getApplicationContext());
        }
        return sInstance;
    }

    private MovieDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(MovieDbContract.MovieEntry.CREATE_MOVIE_TABLE_QUERY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(MovieDbContract.MovieEntry.DELETE_MOVIE_TABLE_QUERY);
        onCreate(db);
    }

    public List<Movie> getMovies() {
        List<Movie> movies = null;
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(MovieDbContract.MovieEntry.TABLE_NAME, null, null,
                null, null, null,
                MovieDbContract.MovieEntry._ID + " DESC");

        try {
            if (cursor == null || !cursor.moveToFirst()) {
                return null;
            }
            movies = new ArrayList<>();
            do {
                long id = cursor.getLong(cursor.getColumnIndex(MovieDbContract.MovieEntry.COLUMN_TMDB_ID));
                String posterPath = cursor.getString(cursor.getColumnIndex(MovieDbContract.MovieEntry.COLUMN_POSTER_PATH));
                Movie movie = new Movie(id, posterPath);
                movies.add(movie);
            } while (cursor.moveToNext());
        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
        } finally {
            if (cursor != null) cursor.close();
        }
        return movies;
    }

    public Movie getMovie(long movieId) {
        Movie movie = null;
        String selection = MovieDbContract.MovieEntry.COLUMN_TMDB_ID + " = ?";
        String[] selectionArgs = {String.valueOf(movieId)};
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(MovieDbContract.MovieEntry.TABLE_NAME, null, selection,
                selectionArgs, null, null, MovieDbContract.MovieEntry._ID + " DESC");

        try {
            if (cursor == null || !cursor.moveToFirst()) {
                return null;
            }
            long id = cursor.getLong(cursor.getColumnIndex(MovieDbContract.MovieEntry.COLUMN_TMDB_ID));
            String posterPath = cursor.getString(cursor.getColumnIndex(MovieDbContract.MovieEntry.COLUMN_POSTER_PATH));
            String backdropPath = cursor.getString(cursor.getColumnIndex(MovieDbContract.MovieEntry.COLUMN_BACKDROP_PATH));
            String title = cursor.getString(cursor.getColumnIndex(MovieDbContract.MovieEntry.COLUMN_TITLE));
            String releaseDate = cursor.getString(cursor.getColumnIndex(MovieDbContract.MovieEntry.COLUMN_RELEASE_DATE));
            String runtime = cursor.getString(cursor.getColumnIndex(MovieDbContract.MovieEntry.COLUMN_RUNTIME));
            String genres = cursor.getString(cursor.getColumnIndex(MovieDbContract.MovieEntry.COLUMN_GENRES));
            String overview = cursor.getString(cursor.getColumnIndex(MovieDbContract.MovieEntry.COLUMN_OVERVIEW));
            double rating = cursor.getDouble(cursor.getColumnIndex(MovieDbContract.MovieEntry.COLUMN_VOTE_AVERAGE));
            movie = new Movie(id, posterPath, backdropPath, title, releaseDate, runtime, genres, overview, rating);
        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
        } finally {
            if (cursor != null) cursor.close();
        }
        return movie;
    }

    public void saveMovie(Movie movie) {
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        try {
            ContentValues movieCV = new ContentValues();
            movieCV.put(MovieDbContract.MovieEntry.COLUMN_TMDB_ID, movie.getId());
            movieCV.put(MovieDbContract.MovieEntry.COLUMN_POSTER_PATH, movie.getPosterPath());
            movieCV.put(MovieDbContract.MovieEntry.COLUMN_BACKDROP_PATH, movie.getBackdropPath());
            movieCV.put(MovieDbContract.MovieEntry.COLUMN_TITLE, movie.getTitle());
            movieCV.put(MovieDbContract.MovieEntry.COLUMN_RELEASE_DATE, movie.getReleaseDate());
            movieCV.put(MovieDbContract.MovieEntry.COLUMN_RUNTIME, movie.getRuntime());
            movieCV.put(MovieDbContract.MovieEntry.COLUMN_GENRES, movie.getGenres());
            movieCV.put(MovieDbContract.MovieEntry.COLUMN_OVERVIEW, movie.getOverview());
            movieCV.put(MovieDbContract.MovieEntry.COLUMN_VOTE_AVERAGE, movie.getVoteAverage());
            db.insertOrThrow(MovieDbContract.MovieEntry.TABLE_NAME, null, movieCV);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.d(TAG, e.getMessage(), e);
        } finally {
            db.endTransaction();
        }
    }

    public void deleteMovie(long movieId) {
        String whereClause = MovieDbContract.MovieEntry.COLUMN_TMDB_ID + " = ?";
        String[] whereArgs = new String[]{String.valueOf(movieId)};
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        try {
            db.delete(MovieDbContract.MovieEntry.TABLE_NAME, whereClause, whereArgs);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.d(TAG, e.getMessage(), e);
        } finally {
            db.endTransaction();
        }
    }

    public boolean isMovieSaved(long movieId) {
        boolean isMovieSaved = false;
        String[] columns = new String[]{MovieDbContract.MovieEntry.COLUMN_TMDB_ID};
        String selection = MovieDbContract.MovieEntry.COLUMN_TMDB_ID + " = ?";
        String[] selectionArgs = new String[]{String.valueOf(movieId)};
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(MovieDbContract.MovieEntry.TABLE_NAME, columns, selection, selectionArgs, null, null, null);
        if (cursor == null) {
            return false;
        }
        try {
            isMovieSaved = cursor.moveToFirst();
        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
        } finally {
            cursor.close();
        }
        return isMovieSaved;
    }
}