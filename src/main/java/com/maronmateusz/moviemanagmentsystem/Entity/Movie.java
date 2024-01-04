package com.maronmateusz.moviemanagmentsystem.Entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.List;


@Entity
@Table(name = "movies")
public class Movie {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private Long id;
    private String title;
    private String plot;
    private String genre;
    private String director;
    private String posterUrl;

    @JsonIgnore
    @OneToMany(mappedBy = "movie")
    private List<Favorite> favorites;

    public Movie(OmdbMovieResponse omdbMovieResponse) {
        this.title = omdbMovieResponse.getTitle();
        this.plot = omdbMovieResponse.getPlot();
        this.genre = omdbMovieResponse.getGenre();
        this.director = omdbMovieResponse.getDirector();
        this.posterUrl = omdbMovieResponse.getPoster();
    }

    public Movie() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPlot() {
        return plot;
    }

    public void setPlot(String plot) {
        this.plot = plot;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public String getPosterUrl() {
        return posterUrl;
    }

    public void setPosterUrl(String posterUrl) {
        this.posterUrl = posterUrl;
    }

    public List<Favorite> getFavorites() {
        return favorites;
    }

    public void setFavorites(List<Favorite> favorites) {
        this.favorites = favorites;
    }
}

