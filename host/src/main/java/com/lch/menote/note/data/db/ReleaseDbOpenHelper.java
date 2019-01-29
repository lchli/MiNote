package com.lch.menote.note.data.db;

import android.content.Context;
import android.content.ContextWrapper;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.apkfuns.logutils.LogUtils;
import com.lch.menote.ConstantUtil;
import com.lch.menote.note.data.db.gen.DaoMaster;
import com.lchli.utils.tool.ExtFileUtils;

import org.apache.commons.io.FileUtils;
import org.greenrobot.greendao.database.Database;

import java.io.File;

public class ReleaseDbOpenHelper extends DaoMaster.DevOpenHelper {


    public ReleaseDbOpenHelper(Context context, String name, boolean isSdcardDatabase) {
        super(chooseContext(context, isSdcardDatabase), name);
    }

    public ReleaseDbOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, boolean isSdcardDatabase) {
        super(chooseContext(context, isSdcardDatabase), name, factory);
    }

    private static Context chooseContext(Context context, boolean isSdcardDatabase) {
        if (!isSdcardDatabase) {
            return context;
        }

        return new ContextWrapper(context) {

            @Override
            public File getDatabasePath(String name) {
                try {
                    FileUtils.forceMkdir(new File(ConstantUtil.DB_DIR));
                } catch (Exception e) {
                    e.printStackTrace();
                }

                File noteDb = new File(ConstantUtil.DB_DIR, name);

                LogUtils.e("DB_DIR exists:" + new File(ConstantUtil.DB_DIR).exists());

                ExtFileUtils.makeFile(noteDb.getAbsolutePath());

                LogUtils.e("db:" + noteDb.getAbsolutePath());

                return noteDb;
            }

            @Override
            public SQLiteDatabase openOrCreateDatabase(String name, int mode, SQLiteDatabase.CursorFactory factory) {
                return SQLiteDatabase.openOrCreateDatabase(getDatabasePath(name), factory);
            }

            @Override
            public SQLiteDatabase openOrCreateDatabase(String name, int mode, SQLiteDatabase.CursorFactory factory, DatabaseErrorHandler errorHandler) {
                return SQLiteDatabase.openOrCreateDatabase(getDatabasePath(name).getPath(), factory, errorHandler);
            }
        };
    }


    @Override
    public void onUpgrade(Database db, int oldVersion, int newVersion) {
        Log.e("greenDAO", "Upgrading schema from version $oldVersion to $newVersion by dropping all tables");

        if (oldVersion == 1) {
            db.execSQL("ALTER TABLE note ADD CATEGORY integer");
        } else {
            DaoMaster.dropAllTables(db, true);
            onCreate(db);
        }

    }
}
