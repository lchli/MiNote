package com.lch.menote.note.controller;

import android.content.Context;

import com.lch.menote.note.data.NetNoteRepo;
import com.lch.menote.note.model.HeadData;
import com.lch.menote.note.model.NoteModel;
import com.lch.menote.note.data.response.QueryNoteResponse;
import com.lch.netkit.common.mvc.ControllerCallback;
import com.lch.netkit.common.mvc.ResponseValue;
import com.lch.netkit.common.tool.ListUtils;
import com.lch.netkit.common.tool.TaskExecutor;
import com.lch.netkit.common.tool.UiHandler;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lichenghang on 2018/5/19.
 */

public class HotNoteController {


    private NetNoteRepo netNoteSource;
    private int page;
    private static final int PAGE_SIZE = 20;
    private List<Object> all = new ArrayList<>();
    private boolean isHaveMore = false;
    private NetNoteRepo.NetNoteQuery mQuery;

    public HotNoteController(Context context) {
        netNoteSource = new NetNoteRepo();
    }


    private void getHotNotes(final ControllerCallback<List<Object>> cb) {

        mQuery.setPage(page).setPageSize(PAGE_SIZE).setPublic(true);

        TaskExecutor.execute(new Runnable() {
            @Override
            public void run() {

                final ResponseValue<QueryNoteResponse> r = netNoteSource.queryNotes(mQuery);

                final ResponseValue<List<Object>> ret = new ResponseValue<>();
                if (r.hasError() || r.data == null) {
                    ret.setErrMsg(r.errMsg());
                    UiHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            if (cb != null) {
                                cb.onComplete(ret);
                            }
                        }
                    });
                    return;
                }

                List<NoteModel> notes = r.data.data;
                if (!ListUtils.isEmpty(notes)) {
                    all.addAll(notes);
                }

                if (ListUtils.isEmpty(notes) || notes.size() < PAGE_SIZE) {
                    isHaveMore = false;
                }

                List<Object> result = new ArrayList<>();
                result.add(new HeadData());
                result.addAll(all);

                ret.data = result;

                UiHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (cb != null) {
                            cb.onComplete(ret);
                        }
                    }
                });

            }
        });

    }

    public void refresh(final ControllerCallback<List<Object>> cb) {
        NetNoteRepo.NetNoteQuery query = NetNoteRepo.NetNoteQuery.newInstance();

        isHaveMore = true;
        all.clear();
        page = 0;
        mQuery = query;

        getHotNotes(cb);

    }

    public void loadMore(final ControllerCallback<List<Object>> cb) {
        final ResponseValue<List<Object>> ret = new ResponseValue<>();

        if (!isHaveMore) {
            ret.setErrMsg("已无更多数据");
            UiHandler.post(new Runnable() {
                @Override
                public void run() {
                    if (cb != null) {
                        cb.onComplete(ret);
                    }
                }
            });

            return;
        }

        getHotNotes(cb);

    }


}
