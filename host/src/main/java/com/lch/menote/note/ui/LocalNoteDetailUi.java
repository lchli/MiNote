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
import com.lch.menote.note.domain.NoteElement;
import com.lch.menote.note.domain.NoteModel;
import com.lch.menote.note.route.RouteCall;
import com.lch.menote.user.route.User;
import com.lch.menote.user.route.UserRouteApi;
import com.lch.netkit.common.base.BaseCompatActivity;
import com.lch.netkit.common.tool.AliJsonHelper;
import com.lch.netkit.common.tool.ListUtils;
import com.lch.netkit.common.tool.VF;
import com.lch.video_player.LchVideoPlayer;
import com.lch.video_player.VideoPlayer;

import java.util.List;

public class LocalNoteDetailUi extends BaseCompatActivity {
    private static final int LAUNCH_FROM_LOCAL_NOTE = 1;
    private static final int LAUNCH_FROM_CLOUD_NOTE = 2;

    private ListView imageEditText_content;
    private NoteModel note;
    private AudioPlayer audioPlayer = LchAudioPlayer.newAudioPlayer();
    private VideoPlayer videoPlayer;
    private int launchFrom = LAUNCH_FROM_LOCAL_NOTE;

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
        videoPlayer = LchVideoPlayer.newPlayer(getApplicationContext());
        note = (NoteModel) getIntent().getSerializableExtra("note");
        launchFrom = getIntent().getIntExtra("from", LAUNCH_FROM_LOCAL_NOTE);

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.local_note_detail_toolbar_actions, menu);
        if (launchFrom == LAUNCH_FROM_LOCAL_NOTE) {
            menu.removeItem(R.id.action_like_note);
            menu.removeItem(R.id.action_share_note);

        } else if (launchFrom == LAUNCH_FROM_CLOUD_NOTE) {

            UserRouteApi userMod = RouteCall.getUserModule();
            if (userMod == null) {
                menu.removeItem(R.id.action_edit_note);
            } else {
                User session = userMod.userSession();
                if (session == null || session.uid == null || !session.uid.equals(note.userId)) {
                    menu.removeItem(R.id.action_edit_note);
                }
            }

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
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        audioPlayer.release();
        videoPlayer.release();
    }
}
