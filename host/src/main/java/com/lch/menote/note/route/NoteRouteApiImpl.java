package com.lch.menote.note.route;

import android.content.Context;
import android.support.v4.app.Fragment;

import com.lch.menote.ConstantUtil;
import com.lch.menote.note.data.db.DaoSessionManager;
import com.lch.menote.note.events.CloudNoteListChangedEvent;
import com.lch.menote.note.ui.CloudNoteUi;
import com.lch.menote.note.ui.LocalNoteUi;
import com.lch.netkit.common.tool.EventBusUtils;
import com.lch.route.noaop.lib.RouteService;
import com.lch.route.noaop.lib.Router;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.util.Map;

/**
 * Created by lichenghang on 2018/5/19.
 */
@RouteService(NoteRouteApi.MODULE_NAME)
public class NoteRouteApiImpl implements NoteRouteApi, Router {
    public static final String SP = "note-sp";

    @Override
    public void init(Context context) {

    }

    @Override
    public Fragment localFrament(Map<String, String> params) {
        return new LocalNoteUi();
    }

    @Override
    public Fragment cloudFragment(Map<String, String> params) {
        return new CloudNoteUi();
    }

    @Override
    public void clearDB(Map<String, String> params) {
        try {
            FileUtils.deleteDirectory(new File(ConstantUtil.DB_DIR));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onAppBackground(Map<String, String> params) {
        DaoSessionManager.destroy();
    }

    @Override
    public void onUserLogout(Map<String, String> params) {
        EventBusUtils.post(new CloudNoteListChangedEvent());
    }

    @Override
    public void onUserLogin(Map<String, String> params) {

        EventBusUtils.post(new CloudNoteListChangedEvent());

    }
}
