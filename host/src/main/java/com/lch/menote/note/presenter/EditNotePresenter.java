package com.lch.menote.note.presenter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.lch.menote.R;
import com.lch.menote.note.events.LocalNoteListChangedEvent;
import com.lch.menote.note.util.NoteUtils;
import com.lch.menote.note.model.NoteElement;
import com.lch.menote.note.model.NoteModel;
import com.lch.menote.note.service.LocalNoteService;
import com.lch.menote.note.service.NoteElementService;
import com.lch.menote.note.service.NoteTagService;
import com.lchli.arch.clean.ControllerCallback;
import com.lchli.arch.clean.ResponseValue;
import com.lchli.arch.clean.UseCase;
import com.lchli.utils.tool.AliJsonHelper;
import com.lchli.utils.tool.EventBusUtils;
import com.lchli.utils.tool.ListUtils;
import com.lchli.utils.tool.UUIDUtils;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2019/1/31.
 */

public class EditNotePresenter {

    public interface MvpView {

        void showFail(String msg);

        void showLoading();

        void dismissLoading();

        void finishUi();

        void showCategory(String text);

        void showTitle(String text);

        void showContent(List<NoteElement> datas);

        void showTags(List<String> datas);

        void toast(String msg);

    }

    private NoteModel oldNote;
    private MvpView view;
    private final LocalNoteService localNoteService = new LocalNoteService();
    private final NoteTagService noteTagService = new NoteTagService();
    private NoteElementService noteElementController = new NoteElementService();
    private String courseUUID;
    private String courseDir;
    private String currentTag = "默认";
    private Context context;

    public EditNotePresenter(Context context, MvpView view) {
        this.view = view;
        this.context = context;
    }

    public void onInit(Intent it) {
        oldNote = (NoteModel) it.getSerializableExtra("note");

        if (oldNote != null) {
            courseUUID = oldNote.uid;
            courseDir = oldNote.imagesDir;

            view.showCategory(oldNote.type);
            view.showTitle(oldNote.title);

            new UseCase<Void, Void>() {
                @Override
                protected ResponseValue<Void> execute(Void parameters) {
                    List<NoteElement> oldElements = AliJsonHelper.parseArray(oldNote.content, NoteElement.class);
                    if (oldElements != null) {
                        noteElementController.setElements(oldElements);
                        view.showContent(oldElements);
                    }

                    return null;
                }
            }.invokeAsync(null, null);


        } else {
            courseUUID = UUIDUtils.uuid();
            courseDir = NoteUtils.INSTANCE.buildNoteDir(courseUUID);
            try {
                FileUtils.forceMkdir(new File(courseDir));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    public void onSaveLocalNote(String title) {
        if (TextUtils.isEmpty(title)) {
            view.toast(context.getString(R.string.note_title_cannot_empty));
            return;
        }
        List<NoteElement> content = noteElementController.getElements();
        if (ListUtils.isEmpty(content)) {
            view.toast(context.getString(R.string.note_content_cannot_empty));
            return;
        }

        final NoteModel note = new NoteModel();
        note.uid = courseUUID;
        note.imagesDir = courseDir;
        note.type = currentTag;
        note.title = title;
        note.updateTime = System.currentTimeMillis();
        note.content = AliJsonHelper.toJSONString(content);

        view.showLoading();
        localNoteService.saveLocalNote(note, new ControllerCallback<Void>() {
            @Override
            public void onSuccess(@Nullable Void aVoid) {
                view.dismissLoading();
                EventBusUtils.post(new LocalNoteListChangedEvent());

                view.finishUi();
            }

            @Override
            public void onError(int code, String msg) {
                view.dismissLoading();
                view.showFail(msg);
            }
        });
    }

    public void onGetAllTag() {
        view.showLoading();
        noteTagService.getAllTag(new ControllerCallback<List<String>>() {
            @Override
            public void onSuccess(@Nullable List<String> strings) {
                view.dismissLoading();
                final List<String> datas = new ArrayList<>();
                datas.add("添加新标签");
                if (strings != null) {
                    datas.addAll(strings);
                }

                view.showTags(datas);
            }

            @Override
            public void onError(int code, String msg) {
                view.dismissLoading();
                view.showFail(msg);
            }
        });
    }

    public void onTagClick(String tag) {
        currentTag = tag;
        view.showCategory(tag);
    }

    public void onAddTag(String tag) {

        view.showLoading();
        noteTagService.addTag(tag, new ControllerCallback<Void>() {
            @Override
            public void onSuccess(@Nullable Void aVoid) {
                view.dismissLoading();
                view.toast("添加成功");
            }

            @Override
            public void onError(int code, String msg) {
                view.dismissLoading();
                view.showFail(msg);
            }
        });
    }

    public void onInsertText(int position) {
        noteElementController.insertText(position);

        view.showContent(noteElementController.getElements());
    }

    public void onInsertImg(String path, int position) {
        noteElementController.insertImg(path, position);

        view.showContent(noteElementController.getElements());
    }

    public void onInsertVideo(String path, int position) {
        noteElementController.insertVideo(path, position);

        view.showContent(noteElementController.getElements());
    }

    public void onInsertAudio(String path, int position) {
        noteElementController.insertAudio(path, position);

        view.showContent(noteElementController.getElements());
    }

    public void onDeleteElement(int position) {
        noteElementController.delete(position);

        view.showContent(noteElementController.getElements());
    }
}
