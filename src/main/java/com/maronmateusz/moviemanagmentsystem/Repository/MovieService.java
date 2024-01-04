package com.maronmateusz.moviemanagmentsystem.Repository;

import com.maronmateusz.moviemanagmentsystem.Exception.DuplicateMovieException;
import com.maronmateusz.moviemanagmentsystem.Exception.MovieNotFoundException;
import com.maronmateusz.moviemanagmentsystem.Entity.Favorite;
import com.maronmateusz.moviemanagmentsystem.Entity.Movie;
import com.maronmateusz.moviemanagmentsystem.Entity.OmdbMovieResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class MovieService {
    @Value("${omdb.api.key}")
    private String omdbApiKey;

    private final MovieRepository movieRepository;
    private final FavoriteRepository favoriteRepository;
    private final String omdbApiUrl = "http://www.omdbapi.com/?apikey={apiKey}&t={title}";

    public MovieService(MovieRepository movieRepository, FavoriteRepository favoriteRepository) {
        this.movieRepository = movieRepository;
        this.favoriteRepository = favoriteRepository;
    }

    public Movie searchMovie(String title) throws MovieNotFoundException {
        try {
            String encodedTitle = URLEncoder.encode(title, "UTF-8");
            String apiUrl = omdbApiUrl.replace("{apiKey}", omdbApiKey).replace("{title}", encodedTitle);

            ResponseEntity<OmdbMovieResponse> responseEntity = new RestTemplate().getForEntity(apiUrl, OmdbMovieResponse.class);

            if (responseEntity.getStatusCode() == HttpStatus.OK) {
                OmdbMovieResponse omdbMovieResponse = responseEntity.getBody();

                if (omdbMovieResponse != null && omdbMovieResponse.getResponse().equalsIgnoreCase("True")) {
                    return new Movie(omdbMovieResponse);
                } else {
                    throw new MovieNotFoundException("Movie not found for the title: " + title);
                }
            } else {
                throw new MovieNotFoundException("Error while fetching movie details. Status code: " + responseEntity.getStatusCodeValue());
            }
        } catch (UnsupportedEncodingException e) {
            throw new MovieNotFoundException("Error encoding title: " + e.getMessage());
        }
    }
    @Transactional
    public Movie addToFavorites(String title) throws MovieNotFoundException, DuplicateMovieException {
        try {
            Movie movie = searchMovie(title);
            if (movie != null) { // Find if movie exists in table "Movies"
                Movie existingMovie = movieRepository.findByTitle(movie.getTitle());

                if (existingMovie == null) { // If not exists, add it
                    movie = movieRepository.save(movie);
                } else {
                    movie = existingMovie;
                }

                Favorite existingFavorite = favoriteRepository.findByMovie(movie);

                if (existingFavorite == null) { // Find if movie exists in table "Favourites", if not, add it
                    Favorite favorite = new Favorite();
                    favorite.setMovie(movie);
                    favoriteRepository.save(favorite); // Dodaj do ulubionych
                    return movie;
                } else { // If movie already exists in "Favourites", throw an error
                    throw new DuplicateMovieException("Movie " + title + " already in favorites");
                }
            }
            return null;
        } catch (MovieNotFoundException e) {
            throw e;
        }
    }

    public Iterable<Movie> getFavorites() {
        Iterable<Favorite> favorites = favoriteRepository.findAll();
        List<Movie> favoriteMovies = StreamSupport.stream(favorites.spliterator(), false)
                .map(Favorite::getMovie)
                .collect(Collectors.toList());
        return favoriteMovies;
    }
}