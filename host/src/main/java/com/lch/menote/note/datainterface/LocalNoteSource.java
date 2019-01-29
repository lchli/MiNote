package com.lch.menote.note.datainterface;

import com.lch.menote.note.domain.NoteModel;
import com.lchli.arch.clean.ResponseValue;

import java.util.List;

/**
 * Created by Administrator on 2019/1/29.
 */

public interface LocalNoteSource {

    class LocalNoteQuery {
        public static final String SORT_ASC = "asc";
        public static final String SORT_DESC = "desc";
        public String sortDiretion = SORT_ASC;
        public String tag;
        public String title;

        public static LocalNoteQuery newInstance() {
            return new LocalNoteQuery();
        }

        public LocalNoteQuery setSortDiretion(String sortDiretion) {
            this.sortDiretion = sortDiretion;
            return this;
        }


        public LocalNoteQuery setTag(String tag) {
            this.tag = tag;
            return this;
        }

        public LocalNoteQuery setTitle(String title) {
            this.title = title;
            return this;
        }

    }


    ResponseValue<List<NoteModel>> queryNotes(LocalNoteQuery query);

    ResponseValue<Void> save(NoteModel note);

    ResponseValue<Void> delete(String noteId);
}
