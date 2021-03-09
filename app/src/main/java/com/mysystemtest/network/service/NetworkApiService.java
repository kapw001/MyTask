package com.mysystemtest.network.service;


import com.mysystemtest.network.response.UserDetailsResponse;
import com.mysystemtest.network.response.UserResponse;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Path;

public interface NetworkApiService {

    @Headers({"Content-Type: application/json;charset=UTF-8"})
    @GET("users?per_page=20")
    Observable<UserResponse> fetchUser();

    @Headers({"Content-Type: application/json;charset=UTF-8"})
    @GET("users/{id}")
    Observable<UserDetailsResponse> fetchUserDetails(@Path("id") int id);

}
