package com.lch.menote.note.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.blankj.utilcode.util.ToastUtils;
import com.lch.audio_player.AudioPlayer;
import com.lch.audio_player.LchAudioPlayer;
import com.lch.menote.R;
import com.lch.menote.note.model.NoteElement;
import com.lch.menote.note.model.NoteModel;
import com.lch.menote.note.presenter.NoteDetailPresenter;
import com.lch.menote.utils.MvpUtils;
import com.lch.video_player.LchVideoPlayer;
import com.lch.video_player.VideoPlayer;
import com.lchli.utils.base.BaseCompatActivity;
import com.lchli.utils.tool.VF;

import java.util.List;

public class LocalNoteDetailUi extends BaseCompatActivity implements NoteDetailPresenter.MvpView {

    private ListView imageEditText_content;
    private AudioPlayer audioPlayer = LchAudioPlayer.newAudioPlayer();
    private VideoPlayer videoPlayer;
    private NoteElementNotEditAdapter adapter;
    private Menu menu;
    private NoteDetailPresenter noteDetailPresenter;


    public static void launchFromLocal(Context context, NoteModel note) {
        Intent it = new Intent(context, LocalNoteDetailUi.class);
        it.putExtra("note", note);
        it.putExtra("from", NoteDetailPresenter.LAUNCH_FROM_LOCAL_NOTE);
        it.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(it);
    }

    public static void launchFromCloud(Context context, NoteModel note) {
        Intent it = new Intent(context, LocalNoteDetailUi.class);
        it.putExtra("note", note);
        it.putExtra("from", NoteDetailPresenter.LAUNCH_FROM_CLOUD_NOTE);
        it.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(it);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        videoPlayer = LchVideoPlayer.newPlayer(getApplicationContext());
        noteDetailPresenter = new NoteDetailPresenter(this, MvpUtils.newUiThreadWeakProxy(this));

        setContentView(R.layout.activity_local_note_detail_ui);
        Toolbar toolbar = VF.f(this, R.id.toolbar);
        imageEditText_content = VF.f(this, R.id.imageEditText_content);

        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        adapter = new NoteElementNotEditAdapter(this, audioPlayer, videoPlayer);
        imageEditText_content.setAdapter(adapter);

        noteDetailPresenter.initLoad(getIntent());
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.local_note_detail_toolbar_actions, menu);
        this.menu = menu;

        noteDetailPresenter.onCreateOptionsMenu();

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        noteDetailPresenter.onOptionsItemSelected(item.getItemId());
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        audioPlayer.release();
        videoPlayer.release();
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

    @Override
    public void showNoteContent(List<NoteElement> elements) {
        adapter.refresh(elements);
    }

    @Override
    public void showActionBarTitle(String text) {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(text);
        }
    }

    @Override
    public void showLikeMenuIcon(int resid) {
        if (menu == null) {
            return;
        }
        MenuItem likeMenu = menu.findItem(R.id.action_like_note);
        if (likeMenu != null) {
            likeMenu.setIcon(resid);
        }
    }

    @Override
    public void removeMenue(int id) {
        if (menu != null) {
            menu.removeItem(id);
        }
    }

    @Override
    public void finishUi() {
        finish();
    }

}
