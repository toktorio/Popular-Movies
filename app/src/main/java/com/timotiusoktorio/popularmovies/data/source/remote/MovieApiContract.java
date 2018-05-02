package com.timotiusoktorio.popularmovies.data.source.remote;

public class MovieApiContract {

    public static final String TMDB_MOVIE_URL = "http://api.themoviedb.org/3/movie/";
    public static final String TMDB_MOVIE_URL_POPULAR = TMDB_MOVIE_URL + "popular";
    public static final String TMDB_MOVIE_URL_TOP_RATED = TMDB_MOVIE_URL + "top_rated";
    public static final String TMDB_MOVIE_URL_NOW_PLAYING = TMDB_MOVIE_URL + "now_playing";
    public static final String TMDB_MOVIE_URL_UPCOMING = TMDB_MOVIE_URL + "upcoming";
    public static final String TMDB_MOVIE_POSTER_URL = "http://image.tmdb.org/t/p/w342/";
    public static final String TMDB_MOVIE_BACKDROP_URL = "http://image.tmdb.org/t/p/w780/";
    public static final String TMDB_CREDITS_URL_SUFFIX = "/credits";
    public static final String TMDB_VIDEOS_URL_SUFFIX = "/videos";
    public static final String TMDB_REVIEWS_URL_SUFFIX = "/reviews";
    public static final String TMDB_PARAM_API_KEY = "api_key";
    public static final String TMDB_JSON_RESULTS = "results";
    public static final String TMDB_JSON_ID = "id";
    public static final String TMDB_JSON_POSTER_PATH = "poster_path";
    public static final String TMDB_JSON_BACKDROP_PATH = "backdrop_path";
    public static final String TMDB_JSON_TITLE = "title";
    public static final String TMDB_JSON_RELEASE_DATE = "release_date";
    public static final String TMDB_JSON_RUNTIME = "runtime";
    public static final String TMDB_JSON_GENRES = "genres";
    public static final String TMDB_JSON_OVERVIEW = "overview";
    public static final String TMDB_JSON_VOTE_AVERAGE = "vote_average";
    public static final String TMDB_JSON_CAST = "cast";
    public static final String TMDB_JSON_PROFILE_PATH = "profile_path";
    public static final String TMDB_JSON_CHARACTER = "character";
    public static final String TMDB_JSON_KEY = "key";
    public static final String TMDB_JSON_NAME = "name";
    public static final String TMDB_JSON_TYPE = "type";
    public static final String TMDB_JSON_AUTHOR = "author";
    public static final String TMDB_JSON_CONTENT = "content";
    public static final String YOUTUBE_IMAGE_URL_PREFIX = "http://img.youtube.com/vi/";
    public static final String YOUTUBE_IMAGE_URL_SUFFIX = "/0.jpg";
    public static final String YOUTUBE_VIDEO_URL = "https://www.youtube.com/watch?v=";
}