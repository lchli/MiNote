package com.lch.menote.note.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.blankj.utilcode.util.ToastUtils;
import com.lch.menote.R;
import com.lch.menote.kotlinext.ContextExtKt;
import com.lch.menote.note.events.CloudNoteListChangedEvent;
import com.lch.menote.note.presenter.HotNoteListPresenter;
import com.lch.menote.utils.MvpUtils;
import com.lchli.utils.base.BaseFragment;
import com.lchli.utils.tool.EventBusUtils;
import com.lchli.utils.tool.Navigator;
import com.lchli.utils.tool.VF;
import com.lchli.utils.widget.CommonEmptyView;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.OnItemClickListener;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Arrays;
import java.util.List;

/**
 * Created by lichenghang on 2018/5/20.
 */

public class HotNoteUi extends BaseFragment implements HotNoteListPresenter.MvpView {

    private HotNoteListAdp notesAdp;
    private ListView moduleListRecyclerView;
    private FloatingActionButton fab;
    private HotNoteListPresenter cloudNotePresenter;
    private SmartRefreshLayout refreshLayout;
    private CommonEmptyView empty_widget;

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
        refreshLayout = VF.f(view, R.id.refreshLayout);

        empty_widget.addEmptyText("no data");

        moduleListRecyclerView.setAdapter(notesAdp);

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
        refreshLayout.setRefreshHeader(new ClassicsHeader(view.getContext()));
        refreshLayout.setRefreshFooter(new ClassicsFooter(view.getContext()));


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
    public void showListNotes(List<Object> datas) {
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

    private void queryNotesAsync() {
        cloudNotePresenter.refresh();
    }

    private void loadmore() {
        cloudNotePresenter.loadMore();
    }


}
