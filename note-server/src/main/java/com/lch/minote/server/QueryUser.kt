package com.lch.minote.server

/**
 * Created by lichenghang on 2017/10/1.
 */
import org.jetbrains.ktor.freemarker.FreeMarkerContent
import org.jetbrains.ktor.locations.get
import org.jetbrains.ktor.locations.location
import org.jetbrains.ktor.locations.post
import org.jetbrains.ktor.request.receive
import org.jetbrains.ktor.response.respond
import org.jetbrains.ktor.routing.Route
import org.jetbrains.ktor.util.ValuesMap

@location("/queryUser")
class QueryUser

fun Route.queryUser(dao: DAOFacade) {

    post<QueryUser> {

        val registration = call.receive<ValuesMap>()
        val userId = registration["userId"]

        if (userId != null) {
            try {

                val user = dao.queryByUserId(userId)
                if (user != null) {

                    call.respondJson(LoginResponse(0, "success", user))

                } else {
                    call.respondJson(LoginResponse(-1, "user not exist"))
                }

            } catch (e: Throwable) {
                e.printStackTrace()
                call.respondJson(LoginResponse(-1, e.message))
            }
        } else {
            call.respondJson(LoginResponse(-1, "userName or pwd can not be null"))
        }


    }

    get<QueryUser> {

        call.respond(FreeMarkerContent("login.ftl", mapOf("userId" to "uid", "error" to "err"), ""))

    }
}