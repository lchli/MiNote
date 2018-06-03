package com.lch.menote.note.ui;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.lch.menote.R;
import com.lch.menote.note.domain.NoteModel;
import com.lch.menote.user.ui.UserInfoActivity;
import com.lch.netkit.common.base.AbsAdapter;
import com.lch.netkit.common.tool.VF;

/**
 * Created by lichenghang on 2018/6/2.
 */

public class CloudNoteListAdapter extends AbsAdapter<NoteModel> {

    @Override
    public AbsAdapter.AbsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new H(viewType, View.inflate(parent.getContext(), R.layout.cloud_note_list_item, null));
    }

    @Override
    public void onBindViewHolder(AbsAdapter.AbsViewHolder holder, int position) {
        final NoteModel model = getItem(position);
        if (model == null) {
            return;
        }

        final H h = (H) holder;

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
                UserInfoActivity.launch(model.userId, h.itemView.getContext());
            }
        });

        Glide.with(h.itemView.getContext()).load(model.userHeadUrl).apply(RequestOptions
                .placeholderOf(R.drawable.add_portrait)).into(h.course_thumb_imageView);
    }

    private class H extends AbsAdapter.AbsViewHolder {
        private ImageView course_thumb_imageView;
        private TextView couse_title_textView;
        private TextView course_time_textView;
        private View itemView;

        public H(int viewtype, View view) {
            super(viewtype);
            itemView = view;

            course_thumb_imageView = VF.f(itemView, R.id.course_thumb_imageView);
            couse_title_textView = VF.f(itemView, R.id.couse_title_textView);
            course_time_textView = VF.f(itemView, R.id.course_time_textView);
        }

        @Override
        protected View getItemView() {
            return itemView;
        }
    }
}
