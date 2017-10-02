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

@location("/login")
class Login

fun Route.login(dao: DAOFacade) {

    post<Login> {

        val registration = call.receive<ValuesMap>()
        val userName = registration["userName"]
        val userPwd = registration["userPwd"]

        if (userName != null && userPwd != null) {
            try {

                val user = dao.queryByUserName(userName)
                if (user != null) {

                    if (user.password == userPwd) {
                        call.respond(LoginResponse(0, "success",user))
                    } else {
                        call.respond(RegisterResponse(-1, "pwd error"))
                    }
                } else {
                    call.respond(RegisterResponse(-1, "user not exist"))
                }

            } catch (e: Throwable) {
                e.printStackTrace()
                call.respond(RegisterResponse(-1, e.message))
            }
        } else {
            call.respond(RegisterResponse(-1, "userName or pwd can not be null"))
        }


    }

    get<Login> {

        call.respond(FreeMarkerContent("login.ftl", mapOf("userId" to "uid", "error" to "err"), ""))

    }
}