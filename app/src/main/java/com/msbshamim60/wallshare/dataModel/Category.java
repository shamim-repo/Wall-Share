package com.msbshamim60.wallshare.dataModel;

import java.util.Objects;

public class Category {
    private String category;
    private String url;

    public Category() {
    }

    public Category(String category, String url) {
        this.category = category;
        this.url = url;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Category category1 = (Category) o;
        return Objects.equals(category, category1.category) && Objects.equals(url, category1.url);
    }

    @Override
    public int hashCode() {
        return Objects.hash(category, url);
    }
}
