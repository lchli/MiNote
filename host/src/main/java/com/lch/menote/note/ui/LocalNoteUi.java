package com.lch.menote.note.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.blankj.utilcode.util.ToastUtils;
import com.lch.menote.R;
import com.lch.menote.note.events.LocalNoteListChangedEvent;
import com.lch.menote.note.presenter.LocalNoteListPresenter;
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

public class LocalNoteUi extends BaseFragment implements LocalNoteListPresenter.MvpView {

    private LocalNoteListAdp notesAdp;
    private CommonEmptyView empty_widget;
    private PinnedRecyclerView moduleListRecyclerView;
    private LocalNoteListPresenter localNoteListPresenter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBusUtils.register(this);
        notesAdp = new LocalNoteListAdp(getActivity());
        localNoteListPresenter = new LocalNoteListPresenter(this);
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
        localNoteListPresenter.getLocalNotesWithCat();
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

    }

    @Override
    public void showEmpty() {

    }
}
