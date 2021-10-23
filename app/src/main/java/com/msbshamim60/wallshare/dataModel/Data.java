package com.msbshamim60.wallshare.dataModel;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Objects;

public class Data {
    private String title;
    @SerializedName("url")
    @Expose
    private String url;
    @SerializedName("display_url")
    @Expose
    private String displayUrl;
    @SerializedName("size")
    @Expose
    private Long size;
    @SerializedName("time")
    @Expose
    private String time;

    public Data() {
    }
    public Data(String title, String url, String displayUrl, Long size, String time) {
        super();
        this.title = title;
        this.url = url;
        this.displayUrl = displayUrl;
        this.size = size;
        this.time = time;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Data withTitle(String title) {
        this.title = title;
        return this;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Data withUrl(String url) {
        this.url = url;
        return this;
    }

    public String getDisplayUrl() {
        return displayUrl;
    }

    public void setDisplayUrl(String displayUrl) {
        this.displayUrl = displayUrl;
    }

    public Data withDisplayUrl(String displayUrl) {
        this.displayUrl = displayUrl;
        return this;
    }

    public Long getSize() {
        return size;
    }

    public void setSize(Long size) {
        this.size = size;
    }

    public Data withSize(Long size) {
        this.size = size;
        return this;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Data withTime(String time) {
        this.time = time;
        return this;
    }

    @Override
    public int hashCode() {
        int result = 1;
        result = ((result* 31)+((this.displayUrl == null)? 0 :this.displayUrl.hashCode()));
        result = ((result* 31)+((this.size == null)? 0 :this.size.hashCode()));
        result = ((result* 31)+((this.time == null)? 0 :this.time.hashCode()));
        result = ((result* 31)+((this.title == null)? 0 :this.title.hashCode()));
        result = ((result* 31)+((this.url == null)? 0 :this.url.hashCode()));
        return result;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Data) == false) {
            return false;
        }
        Data rhs = ((Data) other);
        return ((((((Objects.equals(this.displayUrl, rhs.displayUrl))&&(Objects.equals(this.size, rhs.size)))&&(Objects.equals(this.time, rhs.time)))&&(Objects.equals(this.title, rhs.title)))&&(Objects.equals(this.url, rhs.url))));
    }

}

