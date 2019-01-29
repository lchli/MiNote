package com.lch.menote.note.data.response;

import com.lch.menote.model.BaseResponse;
import com.lch.menote.note.model.NoteModel;

import java.util.List;

public class QueryNoteResponse extends BaseResponse {

    public List<NoteModel> data;
}