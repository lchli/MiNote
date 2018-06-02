package com.lch.menote.note.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.blankj.utilcode.util.ToastUtils;
import com.lch.menote.R;
import com.lch.menote.note.controller.NoteController;
import com.lch.menote.note.domain.CloudNoteListChangedEvent;
import com.lch.menote.note.domain.NoteModel;
import com.lch.netkit.common.base.BaseFragment;
import com.lch.netkit.common.mvc.ControllerCallback;
import com.lch.netkit.common.mvc.ResponseValue;
import com.lch.netkit.common.tool.EventBusUtils;
import com.lch.netkit.common.tool.VF;
import com.lchli.pinedrecyclerlistview.library.pinnedRecyclerView.PinnedRecyclerView;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

/**
 * Created by lichenghang on 2018/5/20.
 */

public class CloudNoteUi extends BaseFragment {

    private CloudNoteListAdapter notesAdp;
    private PinnedRecyclerView moduleListRecyclerView;
    private NoteController noteController;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBusUtils.register(this);

        notesAdp = new CloudNoteListAdapter();
        noteController = new NoteController(getActivity());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBusUtils.unregister(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_cloud_note_list, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        moduleListRecyclerView = VF.f(view, R.id.moduleListRecyclerView);

        moduleListRecyclerView.setHasFixedSize(true);
        moduleListRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        moduleListRecyclerView.addItemDecoration(new GridDividerDecoration(getActivity(), GridDividerDecoration.VERTICAL_LIST));
        moduleListRecyclerView.setPinnedAdapter(notesAdp);

        queryNotesAsync();

    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(CloudNoteListChangedEvent e) {
        queryNotesAsync();
    }


    private void queryNotesAsync() {
        noteController.getCloudNotes(null, null, true, "", new ControllerCallback<List<NoteModel>>() {
            @Override
            public void onComplete(@NonNull ResponseValue<List<NoteModel>> responseValue) {
                if (responseValue.hasError()) {
                    ToastUtils.showShort(responseValue.errMsg());
                    return;
                }

                notesAdp.refresh(responseValue.data);


            }
        });
    }
}
