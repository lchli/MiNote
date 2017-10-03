package com.lch.menote.note.data.net;

import com.lch.menote.note.domain.AllNoteResponse;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface NoteService {

    @FormUrlEncoded
    @POST("/queryAllNote")
    Call<AllNoteResponse> queryAllNote(@Field("UserId") String userId, @Field("UserToken") String userToken);
}