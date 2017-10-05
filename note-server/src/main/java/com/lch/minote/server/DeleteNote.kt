package com.lch.minote.server

/**
 * Created by lichenghang on 2017/10/1.
 */
import org.jetbrains.ktor.locations.get
import org.jetbrains.ktor.locations.location
import org.jetbrains.ktor.response.respondRedirect
import org.jetbrains.ktor.routing.Route

@location("/deleteNote")
class DeleteNote

fun Route.deleteNote(dao: DAOFacade) {


    get<DeleteNote> {
        try {
            print("deleNote=============")

            dao.deleteNote(call.parameters["noteId"]!!)


            call.respondRedirect("/index")

        } catch (e: Throwable) {
            e.printStackTrace()
            call.respondJson(LoginResponse(-1, e.message))
        }


    }
}