package com.graduation.remark;

import com.graduation.movie.MovieModel;
import com.graduation.user.UserModel;

import java.util.ArrayList;
import java.util.List;

public class MovieRemarkModel {

    //主键
    private Integer id;

    //评论内容
    private String content;

    //评论用户
    private Integer userId;

    // 回复的用户
    private Integer toUserId;

    //回复的用户
    private UserModel toUser;

    // 回复的消息
    private Integer parentId;

    //评论用户
    private UserModel user;

    //评论电影
    private Integer movieId;

    //评论电影
    private MovieModel movie;

    //评论时间
    private String createTime;

    private List<MovieRemarkModel> children = new ArrayList<>();

    public List<MovieRemarkModel> getChildren() {
        return children;
    }

    public void setChildren(List<MovieRemarkModel> children) {
        this.children = children;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
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

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public Integer getToUserId() {
        return toUserId;
    }

    public void setToUserId(Integer toUserId) {
        this.toUserId = toUserId;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public UserModel getToUser() {
        return toUser;
    }

    public void setToUser(UserModel toUser) {
        this.toUser = toUser;
    }
}
