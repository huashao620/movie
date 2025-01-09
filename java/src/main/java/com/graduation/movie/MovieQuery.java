package com.graduation.movie;

import java.util.List;

public class MovieQuery {

    // 查询页码
    private Integer current = 1;

    //主键
    private Integer id;

    //电影名称
    private String name;

    //电影分类
    private String type;

    //搜索年份
    private Integer year;

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

    //排序方式
    private Integer sortType;

    private List<Integer> idList;

    private boolean onlyCollect;

    public boolean isOnlyCollect() {
        return onlyCollect;
    }

    public void setOnlyCollect(boolean onlyCollect) {
        this.onlyCollect = onlyCollect;
    }

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

    public Integer getSortType() {
        return sortType;
    }

    public void setSortType(Integer sortType) {
        this.sortType = sortType;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<Integer> getIdList() {
        return idList;
    }

    public void setIdList(List<Integer> idList) {
        this.idList = idList;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }
}
