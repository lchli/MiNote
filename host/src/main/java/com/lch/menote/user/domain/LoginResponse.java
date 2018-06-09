package com.lch.menote.user.domain;


import com.lch.menote.note.domain.BaseResponse;
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
