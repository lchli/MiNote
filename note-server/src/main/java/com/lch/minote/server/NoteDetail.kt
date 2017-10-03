package com.lch.minote.server

/**
 * Created by lichenghang on 2017/10/1.
 */
import org.jetbrains.ktor.freemarker.FreeMarkerContent
import org.jetbrains.ktor.locations.get
import org.jetbrains.ktor.locations.location
import org.jetbrains.ktor.response.respond
import org.jetbrains.ktor.routing.Route

@location("/noteDetail")
class NoteDetail

fun Route.noteDetail(dao: DAOFacade) {


    get<NoteDetail> {
        try {
            val note = dao.queryNote(call.parameters["Uid"]!!)
            if (note != null) {
                call.respond(FreeMarkerContent("noteDetail.ftl", mapOf("note" to note), ""))
            } else {
                call.respondJson(LoginResponse(-1, "note not found"))
            }

        } catch (e: Throwable) {
            e.printStackTrace()
            call.respondJson(LoginResponse(-1, e.message))
        }


    }
}