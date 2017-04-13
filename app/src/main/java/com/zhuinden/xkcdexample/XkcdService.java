package com.zhuinden.xkcdexample;

import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by Zhuinden on 2017.04.11..
 */

public interface XkcdService {
    @GET("{number}/info.0.json")
    Single<XkcdResponse> getNumber(@Path("number") int number);

    @GET("info.0.json")
    Single<XkcdResponse> getDefault();
}
