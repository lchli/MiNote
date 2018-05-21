package com.lch.menote.note.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ListView;

import com.blankj.utilcode.util.ToastUtils;
import com.lch.menote.note.R;
import com.lch.menote.note.controller.NoteElementController;
import com.lch.menote.note.domain.NoteElement;
import com.lch.menote.note.usercase.DeleteNoteElementCase;
import com.lch.menote.note.usercase.GetNoteElementsCase;
import com.lch.menote.note.usercase.InsertImgNoteCase;
import com.lch.menote.note.usercase.InsertTextNoteCase;
import com.lch.netkit.common.base.BaseCompatActivity;
import com.lch.netkit.common.mvc.ControllerCallback;
import com.lch.netkit.common.mvc.ResponseValue;
import com.lch.netkit.common.tool.VF;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.OnItemClickListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lichenghang on 2018/5/20.
 */

public class EditNoteUi extends BaseCompatActivity {

    private ListView imageEditText_content;
    private View bt_more;
    private NoteElementAdapter noteElementAdapter;
    private NoteElementController controller = new NoteElementController();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_note);
        noteElementAdapter = new NoteElementAdapter(controller, new NoteElementAdapter.Callback() {
            @Override
            public void showOperation(int position) {
                operation(position, true);
            }
        });

        imageEditText_content = VF.f(this, R.id.imageEditText_content);
        bt_more = VF.f(this, R.id.bt_more);

        imageEditText_content.setAdapter(noteElementAdapter);

        bt_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                operation(-1, false);
            }
        });
    }


    private void operation(final int positionToModify, boolean isShowDelete) {
        final TextOptionItemAdapter adapter = new TextOptionItemAdapter();
        List<String> ops = new ArrayList<>();
        ops.add("插入文本");
        ops.add("插入图片");
        ops.add("插入音频");
        ops.add("插入视频");
        if (isShowDelete) {
            ops.add("删除");
        }

        adapter.refresh(ops);

        DialogPlus.newDialog(EditNoteUi.this)
                .setAdapter(adapter)
                .setOnItemClickListener(new OnItemClickListener() {
                    @Override
                    public void onItemClick(DialogPlus dialog, Object item, View view, int position) {
                        dialog.dismiss();

                        switch (position) {
                            case 0:
                                insertTextNoteCase(positionToModify);
                                break;
                            case 1:
                                insertImgNoteCase(positionToModify);
                                break;
                            case 4:
                                deleteNoteElementCase(positionToModify);
                                break;
                        }

                    }
                }).create().show();
    }


    private void insertTextNoteCase(int position) {
        new InsertTextNoteCase(new ControllerCallback<Void>() {
            @Override
            public void onComplete(@NonNull ResponseValue<Void> responseValue) {
                if (responseValue.hasError()) {
                    ToastUtils.showShort(responseValue.errMsg());
                    return;
                }

                getNoteElementsCase();
            }
        }, position).run();

    }

    private void insertImgNoteCase(int position) {
        new InsertImgNoteCase(new ControllerCallback<Void>() {
            @Override
            public void onComplete(@NonNull ResponseValue<Void> responseValue) {
                if (responseValue.hasError()) {
                    ToastUtils.showShort(responseValue.errMsg());
                    return;
                }

                getNoteElementsCase();
            }
        }, "", position).run();
    }

    private void getNoteElementsCase() {
        new GetNoteElementsCase(new ControllerCallback<List<NoteElement>>() {
            @Override
            public void onComplete(@NonNull ResponseValue<List<NoteElement>> responseValue) {
                if (responseValue.hasError()) {
                    ToastUtils.showShort(responseValue.errMsg());
                    return;
                }
                noteElementAdapter.refresh(responseValue.data);
            }
        }).run();
    }

    private void deleteNoteElementCase(int position) {
        new DeleteNoteElementCase(new ControllerCallback<Void>() {
            @Override
            public void onComplete(@NonNull ResponseValue<Void> responseValue) {
                if (responseValue.hasError()) {
                    ToastUtils.showShort(responseValue.errMsg());
                    return;
                }
                getNoteElementsCase();
            }
        }, position).run();

    }

}
