package com.lch.menote.note.data.response;

import com.lch.menote.model.BaseResponse;
import com.lch.menote.note.model.CloudNoteModel;

import java.util.List;

public class QueryNoteResponse extends BaseResponse {

    public List<CloudNoteModel> data;
}