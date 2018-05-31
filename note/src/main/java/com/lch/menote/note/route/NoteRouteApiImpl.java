package com.lch.menote.note.route;

import android.content.Context;
import android.support.v4.app.Fragment;

import com.lch.menote.common.util.EventBusUtils;
import com.lch.menote.note.data.db.DaoSessionManager;
import com.lch.menote.note.domain.CloudNoteListChangedEvent;
import com.lch.menote.note.helper.ConstantUtil;
import com.lch.menote.note.helper.GlideImageLoader;
import com.lch.menote.note.ui.CloudNoteUi;
import com.lch.menote.note.ui.LocalNoteUi;
import com.lch.menote.noteapi.NoteRouteApi;
import com.lch.route.noaop.lib.RouteService;
import com.lch.route.noaop.lib.Router;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.util.Map;

import cn.finalteam.galleryfinal.CoreConfig;
import cn.finalteam.galleryfinal.FunctionConfig;
import cn.finalteam.galleryfinal.GalleryFinal;
import cn.finalteam.galleryfinal.ThemeConfig;

/**
 * Created by lichenghang on 2018/5/19.
 */
@RouteService(NoteRouteApi.MODULE_NAME)
public class NoteRouteApiImpl implements NoteRouteApi, Router {
    public static final String SP = "note-sp";

    @Override
    public void init(Context context) {

        FunctionConfig functionConfig = new FunctionConfig.Builder()
                .setEnableCamera(true)
                .setEnableEdit(true)
                .setEnableCrop(true)
                .setEnableRotate(true)
                .setCropSquare(true)
                .setEnablePreview(true)
                .build();

        CoreConfig b = new CoreConfig.Builder(context, new GlideImageLoader(), ThemeConfig.DEFAULT)
                .setFunctionConfig(functionConfig)
                .build();
        GalleryFinal.init(b);

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
