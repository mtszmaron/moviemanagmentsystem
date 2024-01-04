package com.maronmateusz.moviemanagmentsystem.Controller;

import com.maronmateusz.moviemanagmentsystem.Exception.DuplicateMovieException;
import com.maronmateusz.moviemanagmentsystem.Exception.MovieNotFoundException;
import com.maronmateusz.moviemanagmentsystem.Entity.Movie;
import com.maronmateusz.moviemanagmentsystem.Repository.MovieService;
import jakarta.validation.constraints.NotBlank;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/movies")
public class MovieController {
    private final MovieService movieService;

    public MovieController(MovieService movieService) {
        this.movieService = movieService;
    }

    @GetMapping("/search")
    public ResponseEntity<Object> searchMovie(@RequestParam @NotBlank String title) {
        try {
            Movie movie = movieService.searchMovie(title);
            return ResponseEntity.ok(movie);
        } catch (MovieNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(createErrorResponse(e.getMessage()));
        }
    }

    @PostMapping("/favorites")
    public ResponseEntity<Object> addToFavorites(@RequestParam @NotBlank String title) {
        try {
            Movie addedMovie = movieService.addToFavorites(title);
            return ResponseEntity.ok(addedMovie);
        } catch (MovieNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(createErrorResponse(e.getMessage()));
        } catch (DuplicateMovieException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(createErrorResponse(e.getMessage()));
        }
    }

    @GetMapping("/favorites")
    public Iterable<Movie> getFavorites() {
        return movieService.getFavorites();
    }


    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Object> handleValidationExceptions(MethodArgumentNotValidException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(createErrorResponse("Validation error. Check request parameters."));
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Object> handleValidationExceptions(MissingServletRequestParameterException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(createErrorResponse("Validation error. Check request parameters."));
    }

    @ExceptionHandler(MovieNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<Object> handleMovieNotFoundException(MovieNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(createErrorResponse(ex.getMessage()));
    }

    private String createErrorResponse(String errorMessage) {
        return "{\"Response\":\"False\",\"Error\":\"" + errorMessage + "\"}";
    }
}
