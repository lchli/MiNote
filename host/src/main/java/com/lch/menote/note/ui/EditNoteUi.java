package com.lch.menote.note.ui;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
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
import com.lch.menote.R;
import com.lch.menote.note.model.NoteElement;
import com.lch.menote.note.model.NoteModel;
import com.lch.menote.note.presenter.EditNotePresenter;
import com.lch.menote.utils.DialogTool;
import com.lch.menote.utils.MvpUtils;
import com.lch.video_player.LchVideoPlayer;
import com.lch.video_player.VideoPlayer;
import com.lchli.utils.base.BaseCompatActivity;
import com.lchli.utils.tool.DialogUtils;
import com.lchli.utils.tool.ListUtils;
import com.lchli.utils.tool.VF;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.OnItemClickListener;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by lichenghang on 2018/5/20.
 */

public class EditNoteUi extends BaseCompatActivity implements EditNotePresenter.MvpView {
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
    private int mPositionToModify = 0;
    private AudioPlayer audioPlayer = LchAudioPlayer.newAudioPlayer();
    private VideoPlayer videoPlayer;
    private EditNotePresenter editNotePresenter;

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
        editNotePresenter = new EditNotePresenter(this, MvpUtils.newUiThreadWeakProxy(this));

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

        noteElementAdapter = new NoteElementAdapter(new NoteElementAdapter.Callback() {
            @Override
            public void showOperation(int position, boolean isPlayingVideo, boolean isPlayingAudio) {
                operation(position, true, isPlayingVideo, isPlayingAudio);
            }
        }, this, audioPlayer, videoPlayer);
        noteElementListView.setAdapter(noteElementAdapter);

        bt_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                operation(-1, false, false, false);
            }
        });
        bt_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editNotePresenter.onSaveLocalNote(et_note_title.getText().toString());
            }
        });

        tv_note_category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editNotePresenter.onGetAllTag();
            }
        });


        editNotePresenter.onInit(getIntent());
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
    public void finishUi() {
        finish();
    }

    @Override
    public void showCategory(String text) {
        tv_note_category.setText(text);
    }

    @Override
    public void showTitle(String text) {
        et_note_title.setText(text);
    }

    @Override
    public void showContent(List<NoteElement> datas) {
        noteElementAdapter.refresh(datas);
    }

    @Override
    public void showTags(final List<String> datas) {
        DialogUtils.showTextListDialogPlus(EditNoteUi.this, new OnItemClickListener() {
            @Override
            public void onItemClick(DialogPlus dialog, Object item, View view, int position) {
                dialog.dismiss();

                if (position != 0) {
                    editNotePresenter.onTagClick(datas.get(position));
                } else {
                    showAddTagDialog();
                }

            }
        }, datas);
    }

    @Override
    public void toast(String msg) {
        ToastUtils.showShort(msg);
    }

    private void showAddTagDialog() {
        DialogTool.showInputDialog(this, "添加标签", new DialogTool.InputDialogListener() {
            @Override
            public void onConfirm(final Dialog dialog, String inputText) {
                if (TextUtils.isEmpty(inputText)) {
                    return;
                }
                dialog.dismiss();

                editNotePresenter.onAddTag(inputText);
            }
        });

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
                editNotePresenter.onInsertVideo(medias.get(0).getPath(), mPositionToModify);
            }
        } else if (requestCode == SELECT_AUDIO_RQUEST && resultCode == Activity.RESULT_OK) {
            List<BaseMedia> medias = Boxing.getResult(data);
            if (!ListUtils.isEmpty(medias)) {
                editNotePresenter.onInsertAudio(medias.get(0).getPath(), mPositionToModify);
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
        editNotePresenter.onInsertText(position);
    }

    private void insertImgNoteCase(int position, String imgPath) {
        editNotePresenter.onInsertImg(imgPath, position);
    }


    private void deleteNoteElementCase(int position, boolean isPlayingVideo, boolean isPlayingAudio) {
        if (isPlayingVideo) {
            videoPlayer.reset();
        }
        if (isPlayingAudio) {
            audioPlayer.reset();
        }

        editNotePresenter.onDeleteElement(position);
    }

}
