package com.zhuinden.xkcdexample;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by Zhuinden on 2017.04.11..
 */

public interface XkcdService {
    @GET("{number}/info.0.json")
    Call<XkcdResponse> getNumber(@Path("number") int number);

    @GET("info.0.json")
    Call<XkcdResponse> getDefault();
}
