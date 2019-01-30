package com.lch.menote.note.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.blankj.utilcode.util.ToastUtils;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.lch.menote.R;
import com.lch.menote.note.data.NetNoteRepo;
import com.lch.menote.note.events.CloudNoteListChangedEvent;
import com.lch.menote.note.model.NoteModel;
import com.lch.menote.note.presenter.CloudNotePresenter;
import com.lchli.utils.base.BaseFragment;
import com.lchli.utils.tool.EventBusUtils;
import com.lchli.utils.tool.VF;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.lang.ref.WeakReference;
import java.util.List;

/**
 * Created by lichenghang on 2018/5/20.
 */

public class CloudNoteUi extends BaseFragment implements CloudNotePresenter.MvpView {

    private CloudNoteListAdapter notesAdp;
    private PullToRefreshListView moduleListRecyclerView;
    private CloudNotePresenter cloudNotePresenter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBusUtils.register(this);
        cloudNotePresenter = new CloudNotePresenter(getActivity(), new ViewProxy(this));

        notesAdp = new CloudNoteListAdapter(getActivity(), cloudNotePresenter);

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

        queryNotesAsync();


    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(CloudNoteListChangedEvent e) {
        queryNotesAsync();
    }


    private void queryNotesAsync() {
        cloudNotePresenter.refresh( NetNoteRepo.NetNoteQuery.newInstance()
                .addSort("updateTime", NetNoteRepo.NetNoteQuery.DIRECTION_ASC));
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

    private static class ViewProxy implements CloudNotePresenter.MvpView {

        private final WeakReference<CloudNotePresenter.MvpView> uiRef;

        private ViewProxy(CloudNotePresenter.MvpView activity) {
            this.uiRef = new WeakReference<>(activity);
        }

        @Override
        public void showLoading() {
            final CloudNotePresenter.MvpView ui = uiRef.get();
            if (ui != null) {
                ui.showLoading();
            }
        }

        @Override
        public void dismissLoading() {
            final CloudNotePresenter.MvpView ui = uiRef.get();
            if (ui != null) {
                ui.dismissLoading();
            }
        }

        @Override
        public void showListNotes(List<NoteModel> datas) {
            final CloudNotePresenter.MvpView ui = uiRef.get();
            if (ui != null) {
                ui.showListNotes(datas);
            }
        }

        @Override
        public void showNoMore() {
            final CloudNotePresenter.MvpView ui = uiRef.get();
            if (ui != null) {
                ui.showNoMore();
            }
        }

        @Override
        public void showEmpty() {
            final CloudNotePresenter.MvpView ui = uiRef.get();
            if (ui != null) {
                ui.showEmpty();
            }
        }

        @Override
        public void showFail(String msg) {
            final CloudNotePresenter.MvpView ui = uiRef.get();
            if (ui != null) {
                ui.showFail(msg);
            }
        }

    }
}
