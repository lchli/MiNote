package com.lch.menote.note.data.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;


import com.lch.menote.note.data.db.gen.DaoMaster;

import org.greenrobot.greendao.database.Database;

/**
 * Created by Administrator on 2017/5/24.
 */

public class ReleaseDbOpenHelper extends DaoMaster.OpenHelper {

    public ReleaseDbOpenHelper(Context context, String name) {
        super(context, name);
    }

    public ReleaseDbOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory) {
        super(context, name, factory);
    }

    @Override
    public void onCreate(Database db) {
        super.onCreate(db);
    }

    @Override
    public void onUpgrade(Database db, int oldVersion, int newVersion) {
        Log.e("greenDAO-adsdk", "Upgrading schema from version " + oldVersion + " to " + newVersion + " by dropping all tables");
        DaoMaster.dropAllTables(db, true);

        onCreate(db);
    }
}
