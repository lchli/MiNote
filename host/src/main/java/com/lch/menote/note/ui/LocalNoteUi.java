package com.lch.menote.note.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lch.menote.R;
import com.lch.menote.note.controller.LocalNoteController;
import com.lch.menote.note.data.DatabseNoteRepo;
import com.lch.menote.note.domain.LocalNoteListChangedEvent;
import com.lchli.arch.clean.ControllerCallback;
import com.lchli.arch.clean.ResponseValue;
import com.lchli.pinedrecyclerlistview.library.pinnedRecyclerView.PinnedRecyclerView;
import com.lchli.utils.base.BaseFragment;
import com.lchli.utils.tool.EventBusUtils;
import com.lchli.utils.tool.VF;
import com.lchli.utils.widget.CommonEmptyView;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

/**
 * Created by lichenghang on 2018/5/20.
 */

public class LocalNoteUi extends BaseFragment {

    private LocalNoteListAdp notesAdp;
    private CommonEmptyView empty_widget;
    private PinnedRecyclerView moduleListRecyclerView;
    private LocalNoteController noteController;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBusUtils.register(this);
        noteController = new LocalNoteController(getActivity());
        notesAdp = new LocalNoteListAdp(getActivity(), noteController);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBusUtils.unregister(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_local_note_list, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        empty_widget = VF.f(view, R.id.empty_widget);
        moduleListRecyclerView = VF.f(view, R.id.moduleListRecyclerView);

        empty_widget.addEmptyText("no data");

        moduleListRecyclerView.setHasFixedSize(true);
        moduleListRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        moduleListRecyclerView.addItemDecoration(new GridDividerDecoration(getActivity(), GridDividerDecoration.VERTICAL_LIST));
        moduleListRecyclerView.setPinnedAdapter(notesAdp);

        queryNotesAsync();

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(LocalNoteListChangedEvent e) {
        queryNotesAsync();
    }


    private void queryNotesAsync() {
        DatabseNoteRepo.LocalNoteQuery query = DatabseNoteRepo.LocalNoteQuery.newInstance();

        noteController.getLocalNotesWithCat(query, new ControllerCallback<List<Object>>() {
            @Override
            public void onComplete(@NonNull ResponseValue<List<Object>> responseValue) {
                if (responseValue.hasError()) {
                    com.blankj.utilcode.util.ToastUtils.showShort(responseValue.errMsg());
                } else {
                    notesAdp.refresh(responseValue.data);
                }
            }
        });

    }
}
