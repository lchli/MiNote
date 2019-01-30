package com.lch.menote.note;

import android.support.v4.app.Fragment;

import com.lch.menote.ConstantUtil;
import com.lch.menote.note.data.db.DaoSessionManager;
import com.lch.menote.note.events.CloudNoteListChangedEvent;
import com.lch.menote.note.ui.CloudNoteUi;
import com.lch.menote.note.ui.LocalNoteUi;
import com.lchli.utils.tool.EventBusUtils;

import org.apache.commons.io.FileUtils;

import java.io.File;

/**
 * Created by lichenghang on 2018/5/19.
 */

public class NoteRouteApiImpl implements NoteApi {
    public static final String SP = "note-sp";


    @Override
    public Fragment localFrament() {
        return new LocalNoteUi();
    }

    @Override
    public Fragment cloudFragment() {
        return new CloudNoteUi();
    }

    @Override
    public void clearDB() {
        try {
            FileUtils.deleteDirectory(new File(ConstantUtil.DB_DIR));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onAppBackground() {
        DaoSessionManager.destroy();
    }

    @Override
    public void onUserLogout() {
        EventBusUtils.post(new CloudNoteListChangedEvent());
    }

    @Override
    public void onUserLogin() {
        EventBusUtils.post(new CloudNoteListChangedEvent());

    }
}
