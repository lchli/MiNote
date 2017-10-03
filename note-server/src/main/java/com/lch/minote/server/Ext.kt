package com.lch.minote.server

import com.google.gson.Gson
import org.jetbrains.ktor.application.ApplicationCall
import org.jetbrains.ktor.http.ContentType
import org.jetbrains.ktor.response.respondText

/**
 * Created by lichenghang on 2017/10/3.
 */
suspend fun ApplicationCall.respondJson(message: Any) {

    respondText(Gson().toJson(message), ContentType.Application.Json)
}