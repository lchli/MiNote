package com.lch.menote.user.dataimpl.response;


import com.lch.menote.model.BaseResponse;
import com.lch.menote.user.route.User;

/**
 * Created by lichenghang on 2018/5/27.
 */

public class LoginResponse extends BaseResponse{

    public User data;

    public int getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public User getData() {
        return data;
    }
}
