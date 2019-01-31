package com.lch.menote.note.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.blankj.utilcode.util.ToastUtils;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.lch.menote.R;
import com.lch.menote.kotlinext.ContextExtKt;
import com.lch.menote.note.events.CloudNoteListChangedEvent;
import com.lch.menote.note.model.NoteModel;
import com.lch.menote.note.presenter.HotNoteListPresenter;
import com.lch.menote.utils.MvpUtils;
import com.lchli.utils.base.BaseFragment;
import com.lchli.utils.tool.EventBusUtils;
import com.lchli.utils.tool.Navigator;
import com.lchli.utils.tool.VF;
import com.lchli.utils.widget.CommonEmptyView;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.OnItemClickListener;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by lichenghang on 2018/5/20.
 */

public class HotNoteUi extends BaseFragment implements HotNoteListPresenter.MvpView {

    private HotNoteListAdp notesAdp;
    private CommonEmptyView empty_widget;
    private PullToRefreshListView moduleListRecyclerView;
    private FloatingActionButton fab;
    private HotNoteListPresenter cloudNotePresenter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBusUtils.register(this);
        notesAdp = new HotNoteListAdp();
        cloudNotePresenter = new HotNoteListPresenter(getActivity(), MvpUtils.newUiThreadWeakProxy(this));
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

    @Override
    public void showListNotes(List<NoteModel> datas) {
        List<Object> total = new ArrayList<>();
        total.addAll(datas);
        notesAdp.refresh(total);
    }

    @Override
    public void showFail(String msg) {
        ToastUtils.showShort(msg);
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void dismissLoading() {

    }

    @Override
    public void showNoMore() {

    }

    @Override
    public void showEmpty() {

    }

    private void queryNotesAsync() {
        cloudNotePresenter.refresh();
    }

    private void loadmore() {
        cloudNotePresenter.loadMore();
    }


}
