package com.lch.menote.note.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lch.menote.common.ContextExtKt;
import com.lch.menote.common.base.BaseFragment;
import com.lch.menote.common.util.EventBusUtils;
import com.lch.menote.common.util.Navigator;
import com.lch.menote.common.widget.CommonEmptyView;
import com.lch.menote.note.R;
import com.lch.menote.note.TaskExecutor;
import com.lch.menote.note.domain.LocalNoteListChangedEvent;
import com.lch.menote.note.usercase.GetLocalNotesWithCatCase;
import com.lch.netkit.common.mvc.ControllerCallback;
import com.lch.netkit.common.mvc.ResponseValue;
import com.lch.netkit.common.tool.VF;
import com.lchli.pinedrecyclerlistview.library.pinnedRecyclerView.PinnedRecyclerView;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.OnItemClickListener;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Arrays;
import java.util.List;

/**
 * Created by lichenghang on 2018/5/20.
 */

public class LocalNoteUi extends BaseFragment {

    private LocalNoteListAdapter notesAdp = new LocalNoteListAdapter();
    private CommonEmptyView empty_widget;
    private PinnedRecyclerView moduleListRecyclerView;
    private FloatingActionButton fab;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBusUtils.register(this);
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
        fab = VF.f(view, R.id.fab);

        empty_widget.addEmptyText("no data");

        moduleListRecyclerView.setHasFixedSize(true);
        moduleListRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        moduleListRecyclerView.addItemDecoration(new GridDividerDecoration(getActivity(), GridDividerDecoration.VERTICAL_LIST));
        moduleListRecyclerView.setPinnedAdapter(notesAdp);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContextExtKt.showListDialog(getActivity(), new OnItemClickListener() {
                    @Override
                    public void onItemClick(DialogPlus dialog, Object item, View view, int position) {
                        switch (position) {
                            case 0:
                                dialog.dismiss();

                                Navigator.launchActivity(getActivity(), EditNoteUi.class);
                                break;
                            case 1:
                                dialog.dismiss();

                                Navigator.launchActivity(getActivity(), MusicActivity.class);
                                break;
                        }

                    }
                }, false, Arrays.asList("创建笔记", "创建音乐"));
            }
        });


        queryNotesAsync();

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(LocalNoteListChangedEvent e) {
        queryNotesAsync();
    }


    private void queryNotesAsync() {
        TaskExecutor.execute(new GetLocalNotesWithCatCase(new ControllerCallback<List<Object>>() {
            @Override
            public void onComplete(@NonNull ResponseValue<List<Object>> responseValue) {
                if (responseValue.hasError()) {
                    com.blankj.utilcode.util.ToastUtils.showShort(responseValue.errMsg());
                } else {
                    notesAdp.refresh(responseValue.data);
                }

            }
        }, null, null, true, null, getActivity()));

    }
}
