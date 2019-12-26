package com.privatecarforpublic.util;

import com.privatecarforpublic.response.PollingResult;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;

public interface IGetRequest {
    @GET("Route/id")
    Observable<PollingResult> getRoute(@Header("token") String token, @Query("id") Long routeId);
}
