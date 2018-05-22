package com.lch.menote.note.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;

import com.lch.menote.common.util.AliJsonHelper;
import com.lch.menote.note.R;
import com.lch.menote.note.domain.Note;
import com.lch.menote.note.domain.NoteElement;
import com.lch.netkit.common.base.BaseCompatActivity;
import com.lch.netkit.common.tool.VF;

public class LocalNoteDetailUi extends BaseCompatActivity {

    private ListView imageEditText_content;

    public static void launch(Context context, Note note) {
        Intent it = new Intent(context, LocalNoteDetailUi.class);
        it.putExtra("note", note);
        it.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(it);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_local_note_detail_ui);
        imageEditText_content = VF.f(this, R.id.imageEditText_content);

        Note note = (Note) getIntent().getSerializableExtra("note");
        if (note != null) {
            NoteElementNotEditAdapter adapter = new NoteElementNotEditAdapter(this);
            adapter.refresh(AliJsonHelper.parseArray(note.content, NoteElement.class));

            imageEditText_content.setAdapter(adapter);
        }

    }
}
