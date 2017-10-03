package com.lch.menote.user.data.net;

import com.lch.menote.user.domain.LoginResponse;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface UserApi {

    @FormUrlEncoded
    @POST("/register")
    Call<LoginResponse> register(@Field("userName") String userName, @Field("userPwd") String userPwd);

    @FormUrlEncoded
    @POST("/login")
    Call<LoginResponse> login(@Field("userName") String userName, @Field("userPwd") String userPwd);

    @FormUrlEncoded
    @POST("/queryUser")
    Call<LoginResponse> queryUser(@Field("userId") String userId);
}