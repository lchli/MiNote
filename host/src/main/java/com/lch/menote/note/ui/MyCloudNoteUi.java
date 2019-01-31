package com.lch.menote.note.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.ListView;

import com.blankj.utilcode.util.ToastUtils;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.lch.menote.R;
import com.lch.menote.note.events.CloudNoteListChangedEvent;
import com.lch.menote.note.model.NoteModel;
import com.lch.menote.note.presenter.MyCloudNoteListPresenter;
import com.lch.menote.utils.MvpUtils;
import com.lchli.utils.base.BaseCompatActivity;
import com.lchli.utils.tool.EventBusUtils;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

/**
 * Created by lichenghang on 2018/5/20.
 */

public class MyCloudNoteUi extends BaseCompatActivity implements MyCloudNoteListPresenter.MvpView {

    private CloudNoteListAdapter notesAdp;
    private PullToRefreshListView moduleListRecyclerView;
    private MyCloudNoteListPresenter cloudNotePresenter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBusUtils.register(this);

        setContentView(R.layout.fragment_cloud_note_list);
        moduleListRecyclerView = f(R.id.moduleListRecyclerView);

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


        cloudNotePresenter = new MyCloudNoteListPresenter(this, MvpUtils.newUiThreadWeakProxy(this));
        notesAdp = new CloudNoteListAdapter(this, cloudNotePresenter);

        queryNotesAsync();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBusUtils.unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(CloudNoteListChangedEvent e) {
        queryNotesAsync();
    }


    private void queryNotesAsync() {
        cloudNotePresenter.refresh();
    }

    private void loadmore() {
        cloudNotePresenter.loadMore();
    }

    @Override
    public void showListNotes(List<NoteModel> datas) {
        notesAdp.refresh(datas);
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
        ToastUtils.showShort("没有更多数据了");
    }

    @Override
    public void showEmpty() {

    }

}
