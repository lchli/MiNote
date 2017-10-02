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

@location("/register")
class Register

fun Route.register(dao: DAOFacade) {

    post<Register> {

        val registration = call.receive<ValuesMap>()
        val userId = System.currentTimeMillis().toString()
        val userName = registration["userName"]
        val userPwd = registration["userPwd"]

        if (userName != null && userPwd != null) {
            try {
                val old = dao.queryByUserName(userName)
                if (old != null) {
                    call.respond(RegisterResponse(-1, "user exist"))
                } else {
                    dao.saveUser(User(userName, userPwd, userId))
                    call.respond(RegisterResponse(0, "success"))
                }
            } catch (e: Throwable) {
                e.printStackTrace()
                call.respond(RegisterResponse(-1, e.message))
            }
        } else {
            call.respond(RegisterResponse(-1, "user name or pwd can not be null"))
        }


    }

    get<Register>{
        call.respond(FreeMarkerContent("register.ftl", mapOf("userId" to "uid", "error" to "err"), ""))
    }
}