package com.lch.minote.server

import org.jetbrains.ktor.content.resolveResource
import org.jetbrains.ktor.locations.get
import org.jetbrains.ktor.locations.location
import org.jetbrains.ktor.response.respond
import org.jetbrains.ktor.routing.Route

/**
 * Created by lichenghang on 2017/10/3.
 */
@location("/styles/note-detail.css")
class NoteDetailCss

fun Route.styles() {

    get<NoteDetailCss> {
        call.respond(call.resolveResource("note-detail.css")!!)
    }
}