package com.example.covash_demo.Modle;

public class Articles {

    private String title, desc, urlToImage, url, content,publishedAt;
    private String status = "0";

    public Articles(String title, String desc, String urlToImage, String url, String content, String publishedAt, String status) {
        this.title = title;
        this.desc = desc;
        this.urlToImage = urlToImage;
        this.url = url;
        this.content = content;
        this.publishedAt=publishedAt;
        this.status=status;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getUrlToImage() {
        return urlToImage;
    }

    public void setUrlToImage(String urlToImage) {
        this.urlToImage = urlToImage;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getPublishedAt() {
        return publishedAt;
    }

    public void setPublishedAt(String publishedAt) {
        this.publishedAt = publishedAt;
    }


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
