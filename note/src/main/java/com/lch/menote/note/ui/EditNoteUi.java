package com.lch.menote.note.ui;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bilibili.boxing.Boxing;
import com.bilibili.boxing.model.config.BoxingConfig;
import com.bilibili.boxing.model.entity.BaseMedia;
import com.bilibili.boxing_impl.ui.BoxingActivity;
import com.blankj.utilcode.util.ToastUtils;
import com.lch.audio_player.AudioPlayer;
import com.lch.audio_player.LchAudioPlayer;
import com.lch.menote.common.util.AliJsonHelper;
import com.lch.menote.common.util.DialogUtils;
import com.lch.menote.common.util.EventBusUtils;
import com.lch.menote.common.util.ListUtils;
import com.lch.menote.common.util.TimeUtils;
import com.lch.menote.common.util.UUIDUtils;
import com.lch.menote.note.R;
import com.lch.menote.note.controller.NoteController;
import com.lch.menote.note.controller.NoteElementController;
import com.lch.menote.note.controller.NoteTagController;
import com.lch.menote.note.domain.LocalNoteListChangedEvent;
import com.lch.menote.note.domain.NoteElement;
import com.lch.menote.note.domain.NoteModel;
import com.lch.menote.note.helper.NoteUtils;
import com.lch.netkit.common.base.BaseCompatActivity;
import com.lch.netkit.common.mvc.ControllerCallback;
import com.lch.netkit.common.mvc.ResponseValue;
import com.lch.netkit.common.tool.VF;
import com.lch.video_player.LchVideoPlayer;
import com.lch.video_player.VideoPlayer;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.OnItemClickListener;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import cn.finalteam.toolsfinal.io.FileUtils;

/**
 * Created by lichenghang on 2018/5/20.
 */

public class EditNoteUi extends BaseCompatActivity {
    private static final int SELECT_IMG_RQUEST = 1;
    private static final int SELECT_VIDEO_RQUEST = 2;
    private static final int SELECT_AUDIO_RQUEST = 3;

    private ListView noteElementListView;
    private View bt_more;
    private View bt_save;
    private ImageView ivBackward;
    private TextView tv_note_category;
    private EditText et_note_title;
    private NoteElementAdapter noteElementAdapter;
    private NoteElementController noteElementController = new NoteElementController();
    private NoteController noteController;
    private NoteModel oldNote;
    private String courseUUID;
    private String courseDir;
    private int mPositionToModify = 0;
    private AudioPlayer audioPlayer = LchAudioPlayer.newAudioPlayer();
    private VideoPlayer videoPlayer;
    private String currentTag = "默认";
    private NoteTagController mNoteTagController = new NoteTagController();

    public static void launch(Context context, NoteModel note) {
        Intent it = new Intent(context, EditNoteUi.class);
        it.putExtra("note", note);
        it.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(it);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        videoPlayer = LchVideoPlayer.newPlayer(getApplicationContext());

        setContentView(R.layout.activity_edit_note);
        noteElementListView = VF.f(this, R.id.imageEditText_content);
        bt_more = VF.f(this, R.id.bt_more);
        bt_save = VF.f(this, R.id.bt_save);
        tv_note_category = VF.f(this, R.id.tv_note_category);
        et_note_title = VF.f(this, R.id.et_note_title);
        ivBackward = VF.f(this, R.id.ivBackward);

        ivBackward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        noteController = new NoteController(this);
        noteElementAdapter = new NoteElementAdapter(new NoteElementAdapter.Callback() {
            @Override
            public void showOperation(int position, boolean isPlayingVideo, boolean isPlayingAudio) {
                operation(position, true, isPlayingVideo, isPlayingAudio);
            }
        }, this, audioPlayer, videoPlayer);

        noteElementListView.setAdapter(noteElementAdapter);

        oldNote = (NoteModel) getIntent().getSerializableExtra("note");

        if (oldNote != null) {
            courseUUID = oldNote.uid;
            courseDir = oldNote.imagesDir;

            tv_note_category.setText(oldNote.type);
            et_note_title.setText(oldNote.title);
            List<NoteElement> oldElements = AliJsonHelper.parseArray(oldNote.content, NoteElement.class);
            if (oldElements != null) {
                noteElementController.setElements(oldElements);
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
                operation(-1, false, false, false);
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
                List<NoteElement> content = noteElementController.getElements().data;
                if (ListUtils.isEmpty(content)) {
                    ToastUtils.showShort(R.string.note_content_cannot_empty);
                    return;
                }

                final NoteModel note = new NoteModel();
                note.uid = courseUUID;
                note.imagesDir = courseDir;
                note.type = currentTag;
                note.title = title;
                note.lastModifyTime = TimeUtils.getTime(System.currentTimeMillis());
                note.content = AliJsonHelper.toJSONString(content);

                noteController.saveLocalNote(note, new ControllerCallback<Void>() {
                    @Override
                    public void onComplete(@NonNull ResponseValue<Void> responseValue) {
                        if (responseValue.hasError()) {
                            ToastUtils.showShort(responseValue.errMsg());
                            return;
                        }
                        EventBusUtils.post(new LocalNoteListChangedEvent());

                        finish();
                    }
                });

            }
        });

        tv_note_category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mNoteTagController.getAllTag(new ControllerCallback<List<String>>() {
                    @Override
                    public void onComplete(@NonNull ResponseValue<List<String>> responseValue) {
                        if (responseValue.hasError()) {
                            ToastUtils.showShort(responseValue.errMsg());
                            return;
                        }

                        final List<String> datas = new ArrayList<>();
                        datas.add("添加新标签");
                        if (responseValue.data != null) {
                            datas.addAll(responseValue.data);
                        }

                        DialogUtils.showTextListDialogPlus(EditNoteUi.this, new OnItemClickListener() {
                            @Override
                            public void onItemClick(DialogPlus dialog, Object item, View view, int position) {
                                dialog.dismiss();

                                if (position != 0) {
                                    currentTag = datas.get(position);
                                    tv_note_category.setText(currentTag);
                                } else {
                                    showAddTagDialog();
                                }

                            }
                        }, datas);
                    }
                });

            }
        });

    }

    private void showAddTagDialog() {
        final Dialog d = new Dialog(this);
        d.setContentView(R.layout.add_new_note_tag_dialog);
        final EditText note_edittext = VF.f(d, R.id.note_edittext);
        Button note_button = VF.f(d, R.id.note_button);

        note_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newtag = note_edittext.getText().toString();
                if (TextUtils.isEmpty(newtag)) {
                    return;
                }
                mNoteTagController.addTag(newtag, new ControllerCallback<Void>() {
                    @Override
                    public void onComplete(@NonNull ResponseValue<Void> responseValue) {
                        if (responseValue.hasError()) {
                            ToastUtils.showShort(responseValue.errMsg());
                        } else {
                            d.dismiss();
                            ToastUtils.showShort("添加成功");
                        }

                    }
                });

            }
        });

        d.show();
    }


    private void operation(final int positionToModify, boolean isShowDelete, final boolean isPlayingVideo, final boolean isPlayingAudio) {

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
                    case 2:
                        showAudioSourceDialog(positionToModify);
                        break;
                    case 3:
                        showVideoSourceDialog(positionToModify);
                        break;
                    case 4:
                        deleteNoteElementCase(positionToModify, isPlayingVideo, isPlayingAudio);
                        break;
                }
            }
        }, ops);

    }

    private void showImgSourceDialog(final int positionToModify) {
        mPositionToModify = positionToModify;

        BoxingConfig config = new BoxingConfig(BoxingConfig.Mode.SINGLE_IMG); // Mode：Mode.SINGLE_IMG, Mode.MULTI_IMG, Mode.VIDEO
        config.needCamera(R.drawable.ic_camera).needGif(); // camera, gif support, set selected images count

        Boxing.of(config).withIntent(EditNoteUi.this, BoxingActivity.class).start(EditNoteUi.this, SELECT_IMG_RQUEST);
    }

    private void showVideoSourceDialog(final int positionToModify) {
        mPositionToModify = positionToModify;

        BoxingConfig config = new BoxingConfig(BoxingConfig.Mode.VIDEO); // Mode：Mode.SINGLE_IMG, Mode.MULTI_IMG, Mode.VIDEO

        Boxing.of(config).withIntent(EditNoteUi.this, BoxingActivity.class).start(EditNoteUi.this, SELECT_VIDEO_RQUEST);
    }

    private void showAudioSourceDialog(final int positionToModify) {
        mPositionToModify = positionToModify;

        BoxingConfig config = new BoxingConfig(BoxingConfig.Mode.AUDIO); // Mode：Mode.SINGLE_IMG, Mode.MULTI_IMG, Mode.VIDEO

        Boxing.of(config).withIntent(EditNoteUi.this, BoxingActivity.class).start(EditNoteUi.this, SELECT_AUDIO_RQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == SELECT_IMG_RQUEST && resultCode == Activity.RESULT_OK) {
            List<BaseMedia> medias = Boxing.getResult(data);
            if (!ListUtils.isEmpty(medias)) {
                insertImgNoteCase(mPositionToModify, medias.get(0).getPath());
            }
        } else if (requestCode == SELECT_VIDEO_RQUEST && resultCode == Activity.RESULT_OK) {
            List<BaseMedia> medias = Boxing.getResult(data);
            if (!ListUtils.isEmpty(medias)) {
                noteElementController.insertVideo(medias.get(0).getPath(), mPositionToModify);

                noteElementAdapter.refresh(noteElementController.getElements().data);
            }
        } else if (requestCode == SELECT_AUDIO_RQUEST && resultCode == Activity.RESULT_OK) {
            List<BaseMedia> medias = Boxing.getResult(data);
            if (!ListUtils.isEmpty(medias)) {
                noteElementController.insertAudio(medias.get(0).getPath(), mPositionToModify);

                noteElementAdapter.refresh(noteElementController.getElements().data);
            }
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        audioPlayer.release();
        videoPlayer.release();
    }

    private void insertTextNoteCase(int position) {
        noteElementController.insertText(position);

        noteElementAdapter.refresh(noteElementController.getElements().data);
    }

    private void insertImgNoteCase(int position, String imgPath) {
        noteElementController.insertImg(imgPath, position);

        noteElementAdapter.refresh(noteElementController.getElements().data);
    }


    private void deleteNoteElementCase(int position, boolean isPlayingVideo, boolean isPlayingAudio) {
        if (isPlayingVideo) {
            videoPlayer.reset();
        }
        if (isPlayingAudio) {
            audioPlayer.reset();
        }

        noteElementController.delete(position);

        noteElementAdapter.refresh(noteElementController.getElements().data);
    }

}
