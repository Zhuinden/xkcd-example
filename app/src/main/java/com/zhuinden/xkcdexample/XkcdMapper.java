package com.zhuinden.xkcdexample;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by Zhuinden on 2017.04.11..
 */

@Singleton
public class XkcdMapper {
    @Inject
    XkcdMapper() {
    }

    public XkcdComic from(XkcdResponse xkcdResponse) {
        XkcdComic xkcdComic = new XkcdComic();
        xkcdComic.setMonth(xkcdResponse.getMonth());
        xkcdComic.setNum(xkcdResponse.getNum());
        xkcdComic.setLink(xkcdResponse.getLink());
        xkcdComic.setYear(xkcdResponse.getYear());
        xkcdComic.setNews(xkcdResponse.getNews());
        xkcdComic.setSafeTitle(xkcdResponse.getSafeTitle());
        xkcdComic.setTranscript(xkcdResponse.getTranscript());
        xkcdComic.setAlt(xkcdResponse.getAlt());
        xkcdComic.setImg(xkcdResponse.getImg());
        xkcdComic.setTitle(xkcdResponse.getTitle());
        xkcdComic.setDay(xkcdResponse.getDay());
        return xkcdComic;
    }
}
