package com.lch.menote.note.domain.response;

import com.lch.menote.model.BaseResponse;
import com.lch.menote.note.domain.NoteModel;

import java.util.List;

public class QueryNoteResponse extends BaseResponse {

    public List<NoteModel> data;
}