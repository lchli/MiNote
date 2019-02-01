package com.lch.menote.note.presenter;

import android.content.Context;
import android.content.Intent;

import com.lch.menote.R;
import com.lch.menote.note.data.entity.Note;
import com.lch.menote.note.model.NoteElement;
import com.lch.menote.note.ui.EditNoteUi;
import com.lchli.arch.clean.UseCase;
import com.lchli.utils.tool.AliJsonHelper;
import com.lchli.utils.tool.ListUtils;

import java.util.List;

/**
 * Created by lichenghang on 2019/1/29.
 */

public class LocalNoteDetailPresenter {

    public interface MvpView {

        void showFail(String msg);

        void showLoading();

        void dismissLoading();

        void showEmpty();

        void showNoteContent(List<NoteElement> elements);

        void showActionBarTitle(String text);

        void removeMenue(int id);

        void finishUi();
    }

    private Context context;
    private MvpView view;
    private Note model;


    public LocalNoteDetailPresenter(Context context, MvpView view) {
        this.context = context;
        this.view = view;
    }


    public void onOptionsItemSelected(int id) {
        if (id == R.id.action_edit_note) {
            EditNoteUi.launch(context, model);
            view.finishUi();
        } else if (id == android.R.id.home) {
            view.finishUi();
        }
    }

    public void initLoad(final Intent intent) {
        model = (Note) intent.getSerializableExtra("note");

        if (model == null) {
            view.showEmpty();
            return;
        }
        view.showActionBarTitle(model.title);

        view.showLoading();
        UseCase.executeOnDiskIO(new Runnable() {
            @Override
            public void run() {
                List<NoteElement> datas = AliJsonHelper.parseArray(model.content, NoteElement.class);
                if (ListUtils.isEmpty(datas)) {
                    view.showEmpty();
                } else {
                    view.showNoteContent(datas);
                }
                view.dismissLoading();
            }
        });


    }

    public void onCreateOptionsMenu() {
        if (model == null) {
            return;
        }

        view.removeMenue(R.id.action_like_note);
        view.removeMenue(R.id.action_share_note);
        view.removeMenue(R.id.action_public_note);
    }


}
