package com.busglo.globus.rest;

import com.busglo.globus.domain.user.User;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Header;

public interface IRewardGatewayApi {

    @GET("/list")
    Observable<List<User>> fetchUsers(@Header("Authorization") String authHeader);

}
