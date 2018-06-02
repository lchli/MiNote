package com.lch.menote.note.ui;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.ToastUtils;
import com.lch.menote.R;
import com.lch.menote.note.domain.NoteModel;
import com.lch.netkit.common.tool.VF;
import com.lchli.pinedrecyclerlistview.library.pinnedRecyclerView.PinnedRecyclerAdapter;

/**
 * Created by lichenghang on 2018/6/2.
 */

public class CloudNoteListAdapter extends PinnedRecyclerAdapter {

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new H(View.inflate(parent.getContext(), R.layout.cloud_note_list_item, null));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final NoteModel model = (NoteModel) getItem(position);
        if (model == null) {
            return;
        }

        H h = (H) holder;

        h.couse_title_textView.setText(model.title);
        h.course_time_textView.setText(model.lastModifyTime);
        h.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LocalNoteDetailUi.launchFromCloud(v.getContext(), model);
            }
        });
        h.course_thumb_imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtils.showShort("hh");
            }
        });
    }

    private class H extends RecyclerView.ViewHolder {
        private ImageView course_thumb_imageView;
        private TextView couse_title_textView;
        private TextView course_time_textView;

        public H(View itemView) {
            super(itemView);
            course_thumb_imageView = VF.f(itemView, R.id.course_thumb_imageView);
            couse_title_textView = VF.f(itemView, R.id.couse_title_textView);
            course_time_textView = VF.f(itemView, R.id.course_time_textView);
        }
    }
}
