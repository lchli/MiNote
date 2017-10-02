package com.lch.minote.server

/**
 * Created by lichenghang on 2017/10/1.
 */
import org.jetbrains.ktor.locations.post
import org.jetbrains.ktor.request.PartData
import org.jetbrains.ktor.request.receiveMultipart
import org.jetbrains.ktor.response.respond
import org.jetbrains.ktor.response.respondText
import org.jetbrains.ktor.routing.Route
import java.io.File


fun Route.uploadNote(dao: DAOFacade) {

    post<Note> {

        val multipart = call.receiveMultipart()
        var videoFile: File? = null
        val note = Note()

        multipart.parts.forEach {
            if (it is PartData.FormItem) {

                when (it.partName) {

                    "Uid" -> {
                        note.noteId = it.value
                    }
                    "UserId" -> {
                        note.userId = it.value
                    }
                    "Title" -> {
                        note.title = it.value
                    }

                    "LastModifyTime" -> {
                        note.lastModifyTime = it.value
                    }


                    "Type" -> {
                        note.type = it.value
                    }

                    "ThumbNail" -> {
                        note.thumbnail = it.value
                    }

                    "Content" -> {
                        note.content = it.value
                    }

                    "ImagesDir" -> {
                        note.imageDir = it.value
                    }

                }

                //it.dispose()

            }
           else if (it is PartData.FileItem) {

                val ext = File(it.originalFileName).extension
                val dir = File("noteImages/${note.noteId}/")
                if (!dir.exists()) {
                    dir.mkdirs()
                }

                val file = File(dir, it.originalFileName)
                it.streamProvider().use { its -> file.outputStream().buffered().use { its.copyTo(it) } }
                videoFile = file

            }

            it.dispose()

        }


//        multipart.parts.forEach {
//
//            if (it is PartData.FileItem) {
//
//                val ext = File(it.originalFileName).extension
//                val dir = File("resources/${note.noteId}/")
//                if (!dir.exists()) {
//                    dir.mkdirs()
//                }
//
//                val file = File(dir, it.originalFileName)
//                it.streamProvider().use { its -> file.outputStream().buffered().use { its.copyTo(it) } }
//                videoFile = file
//
//            }
//
//            it.dispose()
//        }

        try {

            val old = dao.queryNote(note.noteId!!)
            if (old != null) {
                dao.updateNote(note)
            } else {
                dao.insertNote(note)
            }

            call.respondText("123")
        } catch (e: Throwable) {
            e.printStackTrace()
            call.respond(BaseResponse(-1, e.message))
        }

    }
}