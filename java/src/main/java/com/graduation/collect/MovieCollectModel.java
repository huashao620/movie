package com.graduation.collect;

import com.graduation.movie.MovieModel;
import com.graduation.user.UserModel;

public class MovieCollectModel {

    //主键
    private Integer id;

    //收藏用户
    private Integer userId;

    //收藏用户
    private UserModel user;

    //收藏电影
    private Integer movieId;

    //收藏电影
    private MovieModel movie;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public UserModel getUser() {
        return user;
    }

    public void setUser(UserModel user) {
        this.user = user;
    }

    public Integer getMovieId() {
        return movieId;
    }

    public void setMovieId(Integer movieId) {
        this.movieId = movieId;
    }

    public MovieModel getMovie() {
        return movie;
    }

    public void setMovie(MovieModel movie) {
        this.movie = movie;
    }

}
