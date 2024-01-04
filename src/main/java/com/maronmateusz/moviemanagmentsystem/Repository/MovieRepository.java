package com.maronmateusz.moviemanagmentsystem.Repository;

import com.maronmateusz.moviemanagmentsystem.Entity.Movie;
import org.springframework.data.repository.CrudRepository;

public interface MovieRepository extends CrudRepository<Movie, Long> {
    Movie findByTitle(String title);
}
