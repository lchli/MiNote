package com.lch.menote.note.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.blankj.utilcode.util.ToastUtils;
import com.lch.menote.note.R;
import com.lch.menote.note.controller.NoteElementController;
import com.lch.menote.note.domain.NoteElement;
import com.lch.netkit.common.base.BaseCompatActivity;
import com.lch.netkit.common.tool.VF;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.OnItemClickListener;

import java.util.Arrays;

/**
 * Created by lichenghang on 2018/5/20.
 */

public class EditNoteUi extends BaseCompatActivity {

    private ListView imageEditText_content;
    private View bt_more;
    private NoteElementAdapter noteElementAdapter;
    private NoteElementController controller=new NoteElementController();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_note);
        noteElementAdapter = new NoteElementAdapter(controller, new NoteElementAdapter.Callback() {
            @Override
            public void showOperation(NoteElement e) {
                operation(e);
            }
        });

        imageEditText_content = VF.f(this, R.id.imageEditText_content);
        bt_more = VF.f(this, R.id.bt_more);

        imageEditText_content.setAdapter(noteElementAdapter);
        imageEditText_content.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ToastUtils.showShort("ck="+position);
            }
        });

        bt_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                operation(null);

            }
        });
    }


    private void operation(NoteElement e){
        final TextOptionItemAdapter adapter = new TextOptionItemAdapter();
        adapter.refresh(Arrays.asList("插入文本", "插入图片", "插入音频", "插入视频"));

        DialogPlus.newDialog(EditNoteUi.this)
                .setAdapter(adapter)
                .setOnItemClickListener(new OnItemClickListener() {
                    @Override
                    public void onItemClick(DialogPlus dialog, Object item, View view, int position) {
                        switch (position) {
                            case 0: {
                                controller.insertText();

                                noteElementAdapter.refresh(controller.getElements());
                            }
                            break;
                            case 1: {
                                controller.insertImg("");

                                noteElementAdapter.refresh(controller.getElements());
                            }
                            break;
                        }

                    }
                }).create().show();
    }

}
