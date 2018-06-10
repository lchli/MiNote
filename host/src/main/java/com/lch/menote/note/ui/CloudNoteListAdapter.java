package com.lch.menote.note.ui;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.ToastUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.lch.menote.R;
import com.lch.menote.note.controller.CloudNoteController;
import com.lch.menote.note.domain.CloudNoteListChangedEvent;
import com.lch.menote.note.domain.NoteModel;
import com.lch.menote.user.ui.UserInfoActivity;
import com.lch.netkit.common.base.AbsAdapter;
import com.lch.netkit.common.mvc.ControllerCallback;
import com.lch.netkit.common.mvc.ResponseValue;
import com.lch.netkit.common.tool.DialogUtils;
import com.lch.netkit.common.tool.EventBusUtils;
import com.lch.netkit.common.tool.TimeUtils;
import com.lch.netkit.common.tool.VF;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.OnItemClickListener;

/**
 * Created by lichenghang on 2018/6/2.
 */

public class CloudNoteListAdapter extends AbsAdapter<NoteModel> {

    private final CloudNoteController noteController;
    private final Activity activity;

    public CloudNoteListAdapter(CloudNoteController noteController, Activity activity) {
        this.noteController = noteController;
        this.activity = activity;
    }

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
        h.course_time_textView.setText(TimeUtils.getTime(model.updateTime));
        h.course_userName_textView.setText(model.userName);
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

        h.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                DialogUtils.showTextListDialogPlus(activity, new OnItemClickListener() {
                    @Override
                    public void onItemClick(DialogPlus dialog, Object item, View view, int position) {
                        dialog.dismiss();

                        noteController.deleteNetNote(model.uid, new ControllerCallback<Void>() {
                            @Override
                            public void onComplete(@NonNull ResponseValue<Void> responseValue) {
                                if (responseValue.hasError()) {
                                    ToastUtils.showShort(responseValue.errMsg());
                                    return;
                                }

                                EventBusUtils.post(new CloudNoteListChangedEvent());
                            }
                        });


                    }
                }, "删除");


                return true;
            }
        });
    }

    private class H extends AbsAdapter.AbsViewHolder {
        private ImageView course_thumb_imageView;
        private TextView couse_title_textView;
        private TextView course_time_textView;
        private TextView course_userName_textView;
        private View itemView;

        public H(int viewtype, View view) {
            super(viewtype);
            itemView = view;

            course_thumb_imageView = VF.f(itemView, R.id.course_thumb_imageView);
            couse_title_textView = VF.f(itemView, R.id.couse_title_textView);
            course_time_textView = VF.f(itemView, R.id.course_time_textView);
            course_userName_textView = VF.f(itemView, R.id.course_userName_textView);
        }

        @Override
        protected View getItemView() {
            return itemView;
        }
    }
}
