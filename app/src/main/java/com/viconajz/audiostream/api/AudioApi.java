package com.viconajz.audiostream.api;


import com.viconajz.audiostream.api.pojo.Stream;

import java.util.List;



import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Url;

public interface AudioApi {
    @GET
    Observable<List<Stream>> getStreamList(@Url String url);
}
