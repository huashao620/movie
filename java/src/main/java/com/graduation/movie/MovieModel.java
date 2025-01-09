package com.graduation.movie;

public class MovieModel {

    //主键
    private Integer id;

    //电影名称
    private String name;

    //简介
    private String introduction;

    //海报
    private String imgUrl;

    //导演
    private String director;

    //演员
    private String actor;

    //上映日期
    private String upDate;

    //评分
    private Double score;

    //分类
    private String type;

    //是否收藏
    private boolean ifCollect;

    public boolean isIfCollect() {
        return ifCollect;
    }

    public void setIfCollect(boolean ifCollect) {
        this.ifCollect = ifCollect;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public String getActor() {
        return actor;
    }

    public void setActor(String actor) {
        this.actor = actor;
    }

    public String getUpDate() {
        return upDate;
    }

    public void setUpDate(String upDate) {
        this.upDate = upDate;
    }

    public Double getScore() {
        return score;
    }

    public void setScore(Double score) {
        this.score = score;
    }
}
