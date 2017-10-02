package com.lch.minote.server

import org.jetbrains.exposed.sql.Table

/**
 * Created by lichenghang on 2017/10/1.
 */
object UserTable : Table() {
    val userId = varchar("user_id", 64).primaryKey()
    val userName = varchar("user_name", 256)
    val password = varchar("password", 64)
}

data class User(val userName: String, val password: String, val userId: String)