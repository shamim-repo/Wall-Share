package com.msbshamim60.wallshare.dataModel;

import java.util.Objects;

public class Post {

    private User user;
    private String pid;
    private String title;
    private String category;
    private String location;
    private String url;
    private String thumbnail;
    private Long size;
    private String time;
    private Long download;

    public Post() {
    }

    public Post(User user, String pid, String title, String category, String location, String url, String thumbnail, Long size, String time, Long download) {
        this.user = user;
        this.pid = pid;
        this.title = title;
        this.category = category;
        this.location = location;
        this.url = url;
        this.thumbnail = thumbnail;
        this.size = size;
        this.time = time;
        this.download = download;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public Long getSize() {
        return size;
    }

    public void setSize(Long size) {
        this.size = size;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Long getDownload() {
        return download;
    }

    public void setDownload(Long download) {
        this.download = download;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Post)) return false;
        Post post = (Post) o;
        return Objects.equals(getUser(), post.getUser()) && Objects.equals(getPid(), post.getPid()) && Objects.equals(getTitle(), post.getTitle()) && Objects.equals(getCategory(), post.getCategory()) && Objects.equals(getLocation(), post.getLocation()) && Objects.equals(getUrl(), post.getUrl()) && Objects.equals(getThumbnail(), post.getThumbnail()) && Objects.equals(getSize(), post.getSize()) && Objects.equals(getTime(), post.getTime()) && Objects.equals(getDownload(), post.getDownload());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getUser(), getPid(), getTitle(), getCategory(), getLocation(), getUrl(), getThumbnail(), getSize(), getTime(), getDownload());
    }
}
