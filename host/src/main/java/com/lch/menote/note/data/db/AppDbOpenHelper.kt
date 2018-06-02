package com.lch.menote.note.data.db

import android.content.Context
import android.content.ContextWrapper
import android.database.DatabaseErrorHandler
import android.database.sqlite.SQLiteDatabase
import android.util.Log
import com.apkfuns.logutils.LogUtils
import com.lch.menote.note.data.db.gen.DaoMaster
import com.lch.menote.note.helper.ConstantUtil
import com.lch.netkit.common.tool.ExtFileUtils
import org.apache.commons.io.FileUtils
import org.greenrobot.greendao.database.Database
import java.io.File

class AppDbOpenHelper : DaoMaster.DevOpenHelper {


    constructor(context: Context, name: String, factory: SQLiteDatabase.CursorFactory, isSdcardDatabase: Boolean = false) : super(chooseContext(context, isSdcardDatabase), name, factory)

    constructor(context: Context, name: String, isSdcardDatabase: Boolean = false) : super(chooseContext(context, isSdcardDatabase), name)

    /**
     * in production environment,you can Override this to impl your needs.
     *
     *
     * note:when upgrade you must modify DaoMaster#SCHEMA_VERSION.
     *
     * @param db
     * @param oldVersion
     * @param newVersion
     */

    override fun onUpgrade(db: Database?, oldVersion: Int, newVersion: Int) {

        Log.e("greenDAO", "Upgrading schema from version $oldVersion to $newVersion by dropping all tables")

        if (oldVersion == 1) {
            db!!.execSQL("ALTER TABLE note ADD CATEGORY integer")
        } else {
            DaoMaster.dropAllTables(db, true)
            onCreate(db)
        }

    }

    companion object {


        private fun chooseContext(context: Context, isSdcardDatabase: Boolean): Context {

            return if (!isSdcardDatabase) {

                context

            } else object : ContextWrapper(context) {

                override fun getDatabasePath(name: String): File {

                    try {
                        FileUtils.forceMkdir(File(ConstantUtil.DB_DIR))
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }

                    val noteDb = File(ConstantUtil.DB_DIR, name)

                    LogUtils.e("DB_DIR exists:" + File(ConstantUtil.DB_DIR).exists())

                    ExtFileUtils.makeFile(noteDb.absolutePath)

                    LogUtils.e("db:" + noteDb.absolutePath)

                    return noteDb

                }


                override fun openOrCreateDatabase(name: String, mode: Int, factory: SQLiteDatabase.CursorFactory?): SQLiteDatabase {

                    return SQLiteDatabase.openOrCreateDatabase(getDatabasePath(name), factory)

                }


                override fun openOrCreateDatabase(name: String, mode: Int, factory: SQLiteDatabase.CursorFactory?, errorHandler: DatabaseErrorHandler?): SQLiteDatabase {

                    return SQLiteDatabase.openOrCreateDatabase(getDatabasePath(name).path, factory, errorHandler)

                }


            }

        }
    }

}