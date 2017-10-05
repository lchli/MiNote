package com.lch.minote.server

/**
 * Created by lichenghang on 2017/10/1.
 */
import org.jetbrains.ktor.freemarker.FreeMarkerContent
import org.jetbrains.ktor.locations.get
import org.jetbrains.ktor.locations.location
import org.jetbrains.ktor.response.respond
import org.jetbrains.ktor.routing.Route

@location("/writeNote")
class WriteNote

fun Route.writeNote(dao: DAOFacade) {


    get<WriteNote> {
        try {
//            val note = dao.queryNote(call.parameters["Uid"]!!)
//            if (note != null) {
//                call.respond(FreeMarkerContent("noteDetail.ftl", mapOf("note" to note), ""))
//            } else {
//                call.respondJson(LoginResponse(-1, "note not found"))
//            }

            call.respond(FreeMarkerContent("writeNote.ftl", mapOf("noteId" to "id-123","UserId" to "user-123"), ""))

        } catch (e: Throwable) {
            e.printStackTrace()
            call.respondJson(LoginResponse(-1, e.message))
        }


    }
}