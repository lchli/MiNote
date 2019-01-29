package com.lch.menote.note.ui;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.lch.menote.R;
import com.lch.menote.kotlinext.ContextExtKt;
import com.lch.menote.note.controller.HotNoteController;
import com.lch.menote.note.events.CloudNoteListChangedEvent;
import com.lch.netkit.common.base.BaseFragment;
import com.lch.netkit.common.mvc.ControllerCallback;
import com.lch.netkit.common.mvc.ResponseValue;
import com.lch.netkit.common.tool.EventBusUtils;
import com.lch.netkit.common.tool.Navigator;
import com.lch.netkit.common.tool.VF;
import com.lch.netkit.common.widget.CommonEmptyView;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.OnItemClickListener;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Arrays;
import java.util.List;

/**
 * Created by lichenghang on 2018/5/20.
 */

public class HotNoteUi extends BaseFragment {

    private HotNoteListAdp notesAdp;
    private CommonEmptyView empty_widget;
    private PullToRefreshListView moduleListRecyclerView;
    private FloatingActionButton fab;
    private HotNoteController noteController;
    private Handler handler = new Handler();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBusUtils.register(this);
        noteController = new HotNoteController(getActivity());
        notesAdp = new HotNoteListAdp();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBusUtils.unregister(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_hot_note_list, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        empty_widget = VF.f(view, R.id.empty_widget);
        moduleListRecyclerView = VF.f(view, R.id.moduleListRecyclerView);
        fab = VF.f(view, R.id.fab);

        empty_widget.addEmptyText("no data");

        moduleListRecyclerView.setMode(PullToRefreshBase.Mode.BOTH);
        moduleListRecyclerView.setAdapter(notesAdp);
        moduleListRecyclerView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                queryNotesAsync();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                loadmore();
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigator.launchActivity(getActivity(), EditNoteUi.class);

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
    public void onEvent(CloudNoteListChangedEvent e) {
        queryNotesAsync();
    }


    private void queryNotesAsync() {
        noteController.refresh(new ControllerCallback<List<Object>>() {
            @Override
            public void onComplete(@NonNull ResponseValue<List<Object>> responseValue) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        moduleListRecyclerView.onRefreshComplete();
                    }
                });

                if (responseValue.hasError()) {
                    com.blankj.utilcode.util.ToastUtils.showShort(responseValue.errMsg());
                } else {
                    notesAdp.refresh(responseValue.data);
                }
            }
        });

    }

    private void loadmore() {
        noteController.loadMore(new ControllerCallback<List<Object>>() {
            @Override
            public void onComplete(@NonNull ResponseValue<List<Object>> responseValue) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        moduleListRecyclerView.onRefreshComplete();
                    }
                });

                if (responseValue.hasError()) {
                    com.blankj.utilcode.util.ToastUtils.showShort(responseValue.errMsg());
                } else {
                    notesAdp.refresh(responseValue.data);
                }
            }
        });
    }
}
