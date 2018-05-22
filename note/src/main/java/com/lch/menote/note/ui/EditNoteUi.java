package com.lch.menote.note.ui;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.blankj.utilcode.util.ToastUtils;
import com.lch.menote.common.util.AliJsonHelper;
import com.lch.menote.common.util.DialogUtils;
import com.lch.menote.common.util.ListUtils;
import com.lch.menote.common.util.TimeUtils;
import com.lch.menote.common.util.UUIDUtils;
import com.lch.menote.note.R;
import com.lch.menote.note.controller.NoteController;
import com.lch.menote.note.controller.NoteElementController;
import com.lch.menote.note.domain.Note;
import com.lch.menote.note.domain.NoteElement;
import com.lch.menote.note.helper.NoteUtils;
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

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import cn.finalteam.galleryfinal.GalleryFinal;
import cn.finalteam.galleryfinal.model.PhotoInfo;
import cn.finalteam.toolsfinal.io.FileUtils;

/**
 * Created by lichenghang on 2018/5/20.
 */

public class EditNoteUi extends BaseCompatActivity {

    private ListView imageEditText_content;
    private View bt_more;
    private View bt_save;
    private TextView tv_note_category;
    private EditText et_note_title;
    private NoteElementAdapter noteElementAdapter;
    private NoteElementController controller = new NoteElementController();
    private NoteController noteController;
    private Note oldNote;
    private String courseUUID;
    private String courseDir;

    public static void launch(Context context, Note note) {
        Intent it = new Intent(context, EditNoteUi.class);
        it.putExtra("note", note);
        it.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(it);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_note);
        imageEditText_content = VF.f(this, R.id.imageEditText_content);
        bt_more = VF.f(this, R.id.bt_more);
        bt_save = VF.f(this, R.id.bt_save);
        tv_note_category = VF.f(this, R.id.tv_note_category);
        et_note_title = VF.f(this, R.id.et_note_title);

        noteController = new NoteController(this);
        noteElementAdapter = new NoteElementAdapter(controller, new NoteElementAdapter.Callback() {
            @Override
            public void showOperation(int position) {
                operation(position, true);
            }
        }, this);
        imageEditText_content.setAdapter(noteElementAdapter);

        oldNote = (Note) getIntent().getSerializableExtra("note");

        if (oldNote != null) {
            courseUUID = oldNote.getUid();
            courseDir = oldNote.getImagesDir();

            tv_note_category.setText(oldNote.type);
            et_note_title.setText(oldNote.title);
            List<NoteElement> oldElements = AliJsonHelper.parseArray(oldNote.content, NoteElement.class);
            if (oldElements != null) {
                controller.setElements(oldElements);
                noteElementAdapter.refresh(oldElements);
            }
        } else {
            courseUUID = UUIDUtils.uuid();
            courseDir = NoteUtils.INSTANCE.buildNoteDir(courseUUID);
            FileUtils.mkdirs(new File(courseDir));
        }


        bt_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                operation(-1, false);
            }
        });
        bt_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = et_note_title.getText().toString();
                if (TextUtils.isEmpty(title)) {
                    ToastUtils.showShort(R.string.note_title_cannot_empty);
                    return;
                }
                List<NoteElement> content = controller.getElements().data;
                if (ListUtils.isEmpty(content)) {
                    ToastUtils.showShort(R.string.note_content_cannot_empty);
                    return;
                }

                Note note = new Note();
                note.uid = courseUUID;
                note.imagesDir = courseDir;
                note.type = tv_note_category.getText().toString();
                note.title = title;
                note.lastModifyTime = TimeUtils.getTime(System.currentTimeMillis());
                note.content = AliJsonHelper.toJSONString(content);

                noteController.saveNote(note, new ControllerCallback<Void>() {
                    @Override
                    public void onComplete(@NonNull ResponseValue<Void> responseValue) {
                        if (responseValue.hasError()) {
                            ToastUtils.showShort(responseValue.errMsg());
                            return;
                        }

                        finish();
                    }
                });

            }
        });
    }


    private void operation(final int positionToModify, boolean isShowDelete) {

        List<String> ops = new ArrayList<>();
        ops.add("插入文本");
        ops.add("插入图片");
        ops.add("插入音频");
        ops.add("插入视频");
        if (isShowDelete) {
            ops.add("删除");
        }

        DialogUtils.showTextListDialogPlus(this, new OnItemClickListener() {
            @Override
            public void onItemClick(DialogPlus dialog, Object item, View view, int position) {
                dialog.dismiss();

                switch (position) {
                    case 0:
                        insertTextNoteCase(positionToModify);
                        break;
                    case 1:
                        showImgSourceDialog(positionToModify);
                        break;
                    case 4:
                        deleteNoteElementCase(positionToModify);
                        break;
                }
            }
        }, ops);

    }

    private void showImgSourceDialog(final int positionToModify) {
        DialogUtils.showTextListDialog(this, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        GalleryFinal.openGallerySingle(0, new GalleryFinal.OnHanlderResultCallback() {
                            @Override
                            public void onHanlderSuccess(int reqeustCode, List<PhotoInfo> resultList) {
                                if (!ListUtils.isEmpty(resultList)) {
                                    insertImgNoteCase(positionToModify, resultList.get(0).getPhotoPath());
                                }
                            }

                            @Override
                            public void onHanlderFailure(int requestCode, String errorMsg) {
                                ToastUtils.showShort(errorMsg);
                            }
                        });
                        break;
                    case 1:
                        break;
                }

            }
        }, "相册", "照相机");
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

    private void insertImgNoteCase(int position, String imgPath) {
        new InsertImgNoteCase(new ControllerCallback<Void>() {
            @Override
            public void onComplete(@NonNull ResponseValue<Void> responseValue) {
                if (responseValue.hasError()) {
                    ToastUtils.showShort(responseValue.errMsg());
                    return;
                }

                getNoteElementsCase();
            }
        }, imgPath, position).run();
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
