package com.graduation.collect;

public class MovieCollectQuery {

    // 查询页码
    private Integer current = 1;

    //主键
    private Integer id;

    //收藏用户
    private Integer userId;

    //收藏电影
    private Integer movieId;

    public Integer getCurrent() {
        return current;
    }

    public void setCurrent(Integer current) {
        this.current = current;
    }

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

    public Integer getMovieId() {
        return movieId;
    }

    public void setMovieId(Integer movieId) {
        this.movieId = movieId;
    }

}