package com.maronmateusz.moviemanagmentsystem.Repository;

import com.maronmateusz.moviemanagmentsystem.Entity.Favorite;
import com.maronmateusz.moviemanagmentsystem.Entity.Movie;
import org.springframework.data.repository.CrudRepository;

public interface FavoriteRepository extends CrudRepository<Favorite, Long> {
    Favorite findByMovie(Movie existingMovie);
}