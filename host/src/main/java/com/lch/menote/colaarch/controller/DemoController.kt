package com.lch.menote.colaarch.controller

import com.lch.menote.colaarch.client.DemoService
import com.lch.menote.colaarch.client.LoginDto
import com.lch.menote.colaarch.client.LoginResponse

object DemoController {
    var service: DemoService?=null//inject.

    fun login(name:String,pwd:String): LoginResponse? {
        if(name==null||pwd==null){
           return null
        }
        val v=LoginDto()
        v.name=name;
        v.pwd=pwd;

       return service.login(v)
    }

    fun register(){

    }
}