package com.lch.minote.server

/**
 * Created by lichenghang on 2017/10/1.
 */
import org.jetbrains.ktor.locations.get
import org.jetbrains.ktor.locations.location
import org.jetbrains.ktor.locations.post
import org.jetbrains.ktor.request.receive
import org.jetbrains.ktor.response.respond
import org.jetbrains.ktor.routing.Route
import org.jetbrains.ktor.util.ValuesMap

@location("/queryAllNote")
class AllNote

fun Route.queryAllNote(dao: DAOFacade) {

    post<AllNote> {

        val registration = call.receive<ValuesMap>()
        val UserId = registration["UserId"]


        if (UserId != null) {
            try {

                val notes = dao.queryAllNote(UserId)

                call.respond(AllNoteResponse(0, "success", notes))

            } catch (e: Throwable) {
                e.printStackTrace()
                call.respond(RegisterResponse(-1, e.message))
            }
        } else {
            call.respond(RegisterResponse(-1, "UserId can not be null"))
        }


    }

    get<AllNote> {


        try {

            val notes = dao.queryAllNote()

            call.respond(AllNoteResponse(0, "success", notes))

        } catch (e: Throwable) {
            e.printStackTrace()
            call.respond(RegisterResponse(-1, e.message))
        }


    }
}