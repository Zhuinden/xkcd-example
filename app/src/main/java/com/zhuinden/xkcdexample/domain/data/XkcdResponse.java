package com.zhuinden.xkcdexample.domain.data;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;


/**
 * Created by Zhuinden on 2017.04.11..
 */

@JsonObject
public class XkcdResponse {
    @JsonField(name = "month")
    private String month;
    @JsonField(name = "num")
    private Integer num;
    @JsonField(name = "link")
    private String link;
    @JsonField(name = "year")
    private String year;
    @JsonField(name = "news")
    private String news;
    @JsonField(name = "safe_title")
    private String safeTitle;
    @JsonField(name = "transcript")
    private String transcript;
    @JsonField(name = "alt")
    private String alt;
    @JsonField(name = "img")
    private String img;
    @JsonField(name = "title")
    private String title;
    @JsonField(name = "day")
    private String day;

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getNews() {
        return news;
    }

    public void setNews(String news) {
        this.news = news;
    }

    public String getSafeTitle() {
        return safeTitle;
    }

    public void setSafeTitle(String safeTitle) {
        this.safeTitle = safeTitle;
    }

    public String getTranscript() {
        return transcript;
    }

    public void setTranscript(String transcript) {
        this.transcript = transcript;
    }

    public String getAlt() {
        return alt;
    }

    public void setAlt(String alt) {
        this.alt = alt;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }
}
