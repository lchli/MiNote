package com.lch.minote.server

import org.jetbrains.exposed.sql.*
import java.io.Closeable
import java.io.File

/**
 * Created by lichenghang on 2017/10/1.
 */
interface DAOFacade : Closeable {
    fun init()
    fun saveUser(user: User)
    fun queryByUserName(userName: String): User?
    fun queryNote(noteId: String): Note?
    fun insertNote(note: Note)
    fun updateNote(note: Note)
    fun queryAllNote(userId: String): List<Note>?
    fun queryAllNote(): List<Note>?
}

class DAOFacadeDatabase(val db: Database = Database.connect("jdbc:h2:mem:test", driver = "org.h2.Driver")) : DAOFacade {
    constructor(dir: File) : this(Database.connect("jdbc:h2:file:${dir.canonicalFile.absolutePath}", driver = "org.h2.Driver"))

    override fun init() {
        db.transaction {
            create(UserTable, NoteTable)
        }
    }

    override fun close() {

    }


    override fun saveUser(user: User) {
        db.transaction {
            UserTable.insert {
                it[UserTable.userId] = user.userId
                it[UserTable.userName] = user.userName
                it[UserTable.password] = user.password
            }
        }
    }

    override fun queryByUserName(userName: String): User? = db.transaction {

        UserTable.select { UserTable.userName.eq(userName) }
                .mapNotNull {
                    User(it[UserTable.userName], it[UserTable.password], it[UserTable.userId])
                }
                .singleOrNull()
    }


    override fun queryNote(noteId: String): Note? = db.transaction {

        NoteTable.select { NoteTable.noteId.eq(noteId) }
                .mapNotNull {
                    Note(noteId, it[NoteTable.userId], it[NoteTable.title], it[NoteTable.imageDir],
                            it[NoteTable.lastModifyTime], it[NoteTable.type], it[NoteTable.thumbnail],
                            it[NoteTable.content], it[NoteTable.shareUrl])
                }
                .singleOrNull()


    }

    override fun queryAllNote(userId: String): List<Note>? =
            db.transaction {

                NoteTable.select { NoteTable.userId.eq(userId) }
                        .mapNotNull {
                            val ImagesDir = "${WhoApp.DEFAULT_IP}/noteImages/${it[NoteTable.noteId]}"

                            val ShareUrl = "${WhoApp.DEFAULT_IP}/noteDetail?Uid=${it[NoteTable.noteId]}"


                            Note(it[NoteTable.noteId], it[NoteTable.userId], it[NoteTable.title], ImagesDir,
                                    it[NoteTable.lastModifyTime], it[NoteTable.type], it[NoteTable.thumbnail],
                                    it[NoteTable.content], ShareUrl)
                        }


            }

    override fun queryAllNote(): List<Note>? =
            db.transaction {

                NoteTable.selectAll()
                        .mapNotNull {
                            val ImagesDir = "${WhoApp.DEFAULT_IP}/noteImages/${it[NoteTable.noteId]}"

                            val ShareUrl = "${WhoApp.DEFAULT_IP}/noteDetail?Uid=${it[NoteTable.noteId]}"


                            Note(it[NoteTable.noteId], it[NoteTable.userId], it[NoteTable.title], ImagesDir,
                                    it[NoteTable.lastModifyTime], it[NoteTable.type], it[NoteTable.thumbnail],
                                    it[NoteTable.content], ShareUrl)
                        }


            }


    override fun insertNote(note: Note) {
        db.transaction {
            NoteTable.insert {
                it[NoteTable.noteId] = note.noteId
                it[NoteTable.userId] = note.userId
                it[NoteTable.title] = note.title
                it[NoteTable.imageDir] = note.imageDir
                it[NoteTable.lastModifyTime] = note.lastModifyTime
                it[NoteTable.type] = note.type
                it[NoteTable.thumbnail] = note.thumbnail
                it[NoteTable.content] = note.content
                it[NoteTable.shareUrl] = note.shareUrl

            }
        }
    }

    override fun updateNote(note: Note) {
        db.transaction {
            NoteTable.update({ NoteTable.noteId eq note.noteId!! }) {

                it[NoteTable.noteId] = note.noteId
                it[NoteTable.userId] = note.userId
                it[NoteTable.title] = note.title
                it[NoteTable.imageDir] = note.imageDir
                it[NoteTable.lastModifyTime] = note.lastModifyTime
                it[NoteTable.type] = note.type
                it[NoteTable.thumbnail] = note.thumbnail
                it[NoteTable.content] = note.content
                it[NoteTable.shareUrl] = note.shareUrl

            }
        }
    }

}