package com.lch.minote.server

/**
 * Created by lichenghang on 2017/10/1.
 */
import org.jetbrains.ktor.locations.location
import org.jetbrains.ktor.routing.Route

@location("/")
class Index


fun Route.index(dao: DAOFacade) {


}