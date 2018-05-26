package com.lch.menote.note.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.lch.audio_player.AudioPlayer;
import com.lch.audio_player.LchAudioPlayer;
import com.lch.menote.common.base.BaseAppCompatActivity;
import com.lch.menote.common.util.AliJsonHelper;
import com.lch.menote.common.util.ListUtils;
import com.lch.menote.note.R;
import com.lch.menote.note.domain.Note;
import com.lch.menote.note.domain.NoteElement;
import com.lch.netkit.common.tool.VF;
import com.lch.video_player.LchVideoPlayer;
import com.lch.video_player.VideoPlayer;

import java.util.List;

public class LocalNoteDetailUi extends BaseAppCompatActivity {

    private ListView imageEditText_content;
    private Note note;
    private AudioPlayer audioPlayer = LchAudioPlayer.newAudioPlayer();
    private VideoPlayer videoPlayer;

    public static void launch(Context context, Note note) {
        Intent it = new Intent(context, LocalNoteDetailUi.class);
        it.putExtra("note", note);
        it.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(it);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        videoPlayer = LchVideoPlayer.newPlayer(getApplicationContext());
        note = (Note) getIntent().getSerializableExtra("note");

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
        String type = note.type;
        int maxTypeLen = 5;
        if (type.length() >= maxTypeLen) {
            type = type.substring(0, maxTypeLen) + "...";
        }
        menu.findItem(R.id.action_note_type).setTitle(String.format("[%s]", type));
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_edit_note) {
            EditNoteUi.launch(this, note);
            finish();
        } else if (item.getItemId() == android.R.id.home) {
            finish();
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
