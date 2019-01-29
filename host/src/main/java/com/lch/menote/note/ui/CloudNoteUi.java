package com.lch.menote.note.ui;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.blankj.utilcode.util.ToastUtils;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.lch.menote.R;
import com.lch.menote.note.controller.CloudNoteController;
import com.lch.menote.note.data.net.NetNoteRepo;
import com.lch.menote.note.domain.CloudNoteListChangedEvent;
import com.lch.menote.note.domain.NoteModel;
import com.lchli.arch.clean.ControllerCallback;
import com.lchli.arch.clean.ResponseValue;
import com.lchli.utils.base.BaseFragment;
import com.lchli.utils.tool.EventBusUtils;
import com.lchli.utils.tool.VF;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

/**
 * Created by lichenghang on 2018/5/20.
 */

public class CloudNoteUi extends BaseFragment {

    private CloudNoteListAdapter notesAdp;
    private PullToRefreshListView moduleListRecyclerView;
    private CloudNoteController noteController;
    private Handler handler = new Handler();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBusUtils.register(this);

        noteController = new CloudNoteController(getActivity());

        notesAdp = new CloudNoteListAdapter(noteController,getActivity());

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

        NetNoteRepo.NetNoteQuery query = NetNoteRepo.NetNoteQuery.newInstance()
                .addSort("updateTime",NetNoteRepo.NetNoteQuery.DIRECTION_ASC);


        noteController.refresh(query, new ControllerCallback<List<NoteModel>>() {
            @Override
            public void onComplete(@NonNull ResponseValue<List<NoteModel>> responseValue) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        moduleListRecyclerView.onRefreshComplete();
                    }
                });

                if (responseValue.hasError()) {
                    ToastUtils.showShort(responseValue.errMsg());
                    return;
                }

                notesAdp.refresh(responseValue.data);


            }
        });
    }

    private void loadmore() {
        noteController.loadMore(new ControllerCallback<List<NoteModel>>() {
            @Override
            public void onComplete(@NonNull ResponseValue<List<NoteModel>> responseValue) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        moduleListRecyclerView.onRefreshComplete();

                    }
                });

                if (responseValue.hasError()) {
                    ToastUtils.showShort(responseValue.errMsg());
                    return;
                }

                notesAdp.refresh(responseValue.data);


            }
        });
    }
}
