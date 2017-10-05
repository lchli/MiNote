package com.lch.minote.server

import org.jetbrains.exposed.sql.Table
import org.jetbrains.ktor.locations.location

/**
 * Created by lichenghang on 2017/10/1.
 */
object NoteTable : Table() {
    val noteId = varchar("note_id", 64).primaryKey()
    val userId = varchar("user_id", 64)
    val title = varchar("title", 256)
    val imageDir = varchar("image_dir", 256)
    val lastModifyTime = varchar("last_modify_time", 64)
    val type = varchar("type", 64)
    val thumbnail = varchar("thumbnail", 256)
    val shareUrl = varchar("share_url", 256)
    val content = text("content")

}

@location("/uploadNote")
data class Note(var noteId: String? = null, var userId: String? = null, var title: String? = "", var imageDir: String = "",
                var lastModifyTime: String? =System.currentTimeMillis().toString(), var type: String? = "",
                var thumbnail: String? = "", var content: String? = "", var shareUrl: String= "")
