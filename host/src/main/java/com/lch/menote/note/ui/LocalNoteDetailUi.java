package com.lch.menote.note.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.blankj.utilcode.util.ToastUtils;
import com.lch.audio_player.AudioPlayer;
import com.lch.audio_player.LchAudioPlayer;
import com.lch.menote.R;
import com.lch.menote.note.controller.CloudNoteController;
import com.lch.menote.note.events.CloudNoteListChangedEvent;
import com.lch.menote.note.model.NoteElement;
import com.lch.menote.note.model.NoteModel;
import com.lch.menote.note.route.RouteCall;
import com.lch.menote.user.route.User;
import com.lch.menote.user.route.UserRouteApi;
import com.lch.video_player.LchVideoPlayer;
import com.lch.video_player.VideoPlayer;
import com.lchli.arch.clean.ControllerCallback;
import com.lchli.arch.clean.ResponseValue;
import com.lchli.utils.base.BaseCompatActivity;
import com.lchli.utils.tool.AliJsonHelper;
import com.lchli.utils.tool.EventBusUtils;
import com.lchli.utils.tool.ListUtils;
import com.lchli.utils.tool.VF;

import java.util.List;

public class LocalNoteDetailUi extends BaseCompatActivity {

    private static final int LAUNCH_FROM_LOCAL_NOTE = 1;
    private static final int LAUNCH_FROM_CLOUD_NOTE = 2;

    private ListView imageEditText_content;
    private NoteModel note;
    private AudioPlayer audioPlayer = LchAudioPlayer.newAudioPlayer();
    private VideoPlayer videoPlayer;
    private int launchFrom = LAUNCH_FROM_LOCAL_NOTE;
    private CloudNoteController cloudNoteController;
    private CloudNoteController noteController;
    private MenuItem likeMenu;



    public static void launchFromLocal(Context context, NoteModel note) {
        Intent it = new Intent(context, LocalNoteDetailUi.class);
        it.putExtra("note", note);
        it.putExtra("from", LAUNCH_FROM_LOCAL_NOTE);
        it.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(it);
    }

    public static void launchFromCloud(Context context, NoteModel note) {
        Intent it = new Intent(context, LocalNoteDetailUi.class);
        it.putExtra("note", note);
        it.putExtra("from", LAUNCH_FROM_CLOUD_NOTE);
        it.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(it);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        noteController = new CloudNoteController(this);
        videoPlayer = LchVideoPlayer.newPlayer(getApplicationContext());
        note = (NoteModel) getIntent().getSerializableExtra("note");
        launchFrom = getIntent().getIntExtra("from", LAUNCH_FROM_LOCAL_NOTE);
        cloudNoteController = new CloudNoteController(this);

        setContentView(R.layout.activity_local_note_detail_ui);
        Toolbar toolbar = VF.f(this, R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            if (note != null) {
                getSupportActionBar().setTitle(note.title);
            }
        }

        imageEditText_content = VF.f(this, R.id.imageEditText_content);

        if (note != null) {
            List<NoteElement> datas = AliJsonHelper.parseArray(note.content, NoteElement.class);
            if (!ListUtils.isEmpty(datas)) {
                NoteElementNotEditAdapter adapter = new NoteElementNotEditAdapter(this, audioPlayer, videoPlayer);
                adapter.refresh(datas);
                imageEditText_content.setAdapter(adapter);
            }
        }

    }

    private void refreshLikeMenu() {
        if (likeMenu == null||note==null) {
            return;
        }

        UserRouteApi userMod = RouteCall.getUserModule();
        String sessionUid = null;

        if (userMod != null) {
            User session = userMod.userSession();
            if (session != null) {
                sessionUid = session.uid;
            }
        }

        if (note.star != null && note.star.contains(sessionUid)) {
            likeMenu.setIcon(R.drawable.like_thumb_up_selected);

        } else {
            likeMenu.setIcon(R.drawable.like_thumb_up);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.local_note_detail_toolbar_actions, menu);

        UserRouteApi userMod = RouteCall.getUserModule();
        String sessionUid = null;

        if (userMod != null) {
            User session = userMod.userSession();
            if (session != null) {
                sessionUid = session.uid;
            }
        }

        likeMenu = menu.findItem(R.id.action_like_note);

        refreshLikeMenu();


        if (launchFrom == LAUNCH_FROM_LOCAL_NOTE) {
            menu.removeItem(R.id.action_like_note);
        }

        if (launchFrom == LAUNCH_FROM_LOCAL_NOTE) {
            menu.removeItem(R.id.action_share_note);
        }

        if (launchFrom == LAUNCH_FROM_CLOUD_NOTE) {
            menu.removeItem(R.id.action_edit_note);
        }

        if (launchFrom == LAUNCH_FROM_LOCAL_NOTE || sessionUid == null || !sessionUid.equals(note.userId) || note.isPublic()) {
            menu.removeItem(R.id.action_public_note);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_edit_note) {
            EditNoteUi.launch(this, note);
            finish();
        } else if (item.getItemId() == android.R.id.home) {
            finish();
        } else if (item.getItemId() == R.id.action_share_note) {
            try {
                String path = note.ShareUrl;
                Intent imageIntent = new Intent(Intent.ACTION_SEND);
                imageIntent.setType("text/plain");
                imageIntent.putExtra(Intent.EXTRA_TEXT, path);
                startActivity(Intent.createChooser(imageIntent, "分享"));
            } catch (Throwable e) {
                e.printStackTrace();
                ToastUtils.showShort(e.getLocalizedMessage());
            }
        } else if (item.getItemId() == R.id.action_public_note) {

            if (note.isPublic()) {
                ToastUtils.showShort("已经公开!");
                return super.onOptionsItemSelected(item);
            }

            note.isPublic = NoteModel.PUBLIC_TRUE;

            cloudNoteController.publicNetNote(note, new ControllerCallback<Void>() {
                @Override
                public void onComplete(@NonNull ResponseValue<Void> responseValue) {
                    if (responseValue.hasError()) {
                        note.isPublic = NoteModel.PUBLIC_FALSE;
                        ToastUtils.showShort(responseValue.errMsg());
                    } else {
                        EventBusUtils.post(new CloudNoteListChangedEvent());
                        ToastUtils.showShort("操作成功!");
                    }

                }
            });
        } else if (item.getItemId() == R.id.action_like_note) {

            cloudNoteController.likeNetNote(note.uid, new ControllerCallback<Void>() {
                @Override
                public void onComplete(@NonNull ResponseValue<Void> responseValue) {
                    if (responseValue.hasError()) {
                        ToastUtils.showShort(responseValue.errMsg());
                    } else {
                        EventBusUtils.post(new CloudNoteListChangedEvent());

                        reloadData();

                        ToastUtils.showShort("操作成功!");
                    }

                }
            });
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        audioPlayer.release();
        videoPlayer.release();
    }


    private void reloadData() {

        cloudNoteController.getNoteById(note.uid, new ControllerCallback<NoteModel>() {
            @Override
            public void onComplete(@NonNull ResponseValue<NoteModel> responseValue) {
                if (responseValue.hasError()) {
                    ToastUtils.showShort(responseValue.errMsg());
                    return;
                }
                note = responseValue.data;

                refreshLikeMenu();
            }
        });

    }


}
