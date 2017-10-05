package com.lch.minote.server

import com.mchange.v2.c3p0.ComboPooledDataSource
import freemarker.cache.ClassTemplateLoader
import org.h2.Driver
import org.jetbrains.exposed.sql.Database
import org.jetbrains.ktor.application.Application
import org.jetbrains.ktor.application.install
import org.jetbrains.ktor.content.files
import org.jetbrains.ktor.content.static
import org.jetbrains.ktor.features.CallLogging
import org.jetbrains.ktor.features.DefaultHeaders
import org.jetbrains.ktor.freemarker.FreeMarker
import org.jetbrains.ktor.gson.GsonSupport
import org.jetbrains.ktor.locations.Locations
import org.jetbrains.ktor.routing.Routing
import java.io.File

/**
 * Created by lichenghang on 2017/10/1.
 */


class WhoApp {

    companion object {
        val DEFAULT_IP = "http://192.168.1.101:8080"
    }


    val dir = File("target/db")

    val pool = ComboPooledDataSource().apply {
        driverClass = Driver::class.java.name
        jdbcUrl = "jdbc:h2:file:${dir.canonicalFile.absolutePath}"
        user = ""
        password = ""
    }

    val dao = DAOFacadeDatabase(Database.connect(pool))

    fun Application.install() {

        dao.init()
        environment.monitor.applicationStopped += { pool.close() }

        install(DefaultHeaders)
        install(CallLogging)
        install(GsonSupport) {
            setPrettyPrinting()
        }
        install(Locations)
        install(FreeMarker) {
            templateLoader = ClassTemplateLoader(WhoApp::class.java.classLoader, "templates")
        }

        install(Routing) {

            styles()

            index(dao)
            register(dao)
            login(dao)
            uploadNote(dao)
            queryAllNote(dao)
            noteDetail(dao)
            queryUser(dao)
            writeNote(dao)
            deleteNote(dao)

            static(Const.UPLOAD_DIR) {
                //staticRootFolder = File(".")
                files(Const.UPLOAD_DIR)
            }

        }
    }
}


