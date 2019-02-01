package com.lch.menote.note.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ListView;

import com.blankj.utilcode.util.ToastUtils;
import com.lch.menote.R;
import com.lch.menote.note.events.CloudNoteListChangedEvent;
import com.lch.menote.note.model.CloudNoteModel;
import com.lch.menote.note.presenter.MyCloudNoteListPresenter;
import com.lch.menote.utils.MvpUtils;
import com.lchli.utils.base.BaseCompatActivity;
import com.lchli.utils.tool.EventBusUtils;
import com.lchli.utils.widget.CommonEmptyView;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

/**
 * Created by lichenghang on 2018/5/20.
 */

public class MyCloudNoteUi extends BaseCompatActivity implements MyCloudNoteListPresenter.MvpView {

    private CloudNoteListAdapter notesAdp;
    private ListView moduleListRecyclerView;
    private MyCloudNoteListPresenter cloudNotePresenter;
    private SmartRefreshLayout refreshLayout;
    private CommonEmptyView empty_widget;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBusUtils.register(this);

        setContentView(R.layout.fragment_cloud_note_list);
        moduleListRecyclerView = f(R.id.moduleListRecyclerView);
        empty_widget = f(R.id.empty_widget);
        refreshLayout = f(R.id.refreshLayout);


        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                loadmore();
            }
        });

        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                queryNotesAsync();
            }

        });
        refreshLayout.setRefreshHeader(new ClassicsHeader(this));
        refreshLayout.setRefreshFooter(new ClassicsFooter(this));

        empty_widget.addEmptyText("no data");


        cloudNotePresenter = new MyCloudNoteListPresenter(this, MvpUtils.newUiThreadWeakProxy(this));
        notesAdp = new CloudNoteListAdapter(this, cloudNotePresenter);
        moduleListRecyclerView.setAdapter(notesAdp);


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
    public void showListNotes(List<CloudNoteModel> datas) {
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
        refreshLayout.finishLoadMore();
        refreshLayout.finishRefresh();

    }

    @Override
    public void showNoMore() {
        ToastUtils.showShort("没有更多数据了");
    }

    @Override
    public void showEmpty(boolean b) {
        empty_widget.setVisibility(b ? View.VISIBLE : View.GONE);
    }

}
