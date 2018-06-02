package com.lch.menote.note.ui;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.SizeUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.lch.menote.common.util.AppListItemAnimatorUtils;
import com.lch.menote.common.util.ContextProvider;
import com.lch.menote.common.util.DialogUtils;
import com.lch.menote.common.util.EventBusUtils;
import com.lch.menote.note.R;
import com.lch.menote.note.controller.NoteController;
import com.lch.menote.note.domain.CloudNoteListChangedEvent;
import com.lch.menote.note.domain.HeadData;
import com.lch.menote.note.domain.LocalNoteListChangedEvent;
import com.lch.menote.note.domain.Note;
import com.lch.menote.note.domain.NoteModel;
import com.lch.menote.note.domain.NotePinedData;
import com.lch.menote.note.helper.ConstantUtil;
import com.lch.netkit.common.mvc.ControllerCallback;
import com.lch.netkit.common.mvc.ResponseValue;
import com.lch.netkit.common.tool.VF;
import com.lchli.pinedrecyclerlistview.library.ListSectionData;
import com.lchli.pinedrecyclerlistview.library.pinnedRecyclerView.PinnedRecyclerAdapter;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.OnItemClickListener;

/**
 * Created by lichenghang on 2018/5/27.
 */

public class LocalNoteListAdp extends PinnedRecyclerAdapter {

    private final Bitmap def = BitmapFactory.decodeResource(ContextProvider.context().getResources(), R.drawable.ic_add_note);
    private Activity activity;
    private NoteController noteController;


    public LocalNoteListAdp(Activity activity, NoteController noteController) {
        this.activity = activity;
        this.noteController = noteController;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;

        switch (viewType) {

            case ConstantUtil.VIEW_TYPE_HEADER:
                view = LayoutInflater.from(parent.getContext())

                        .inflate(R.layout.local_note_list_header, parent, false);

                return new HeaderViewHolder(view);

            case ConstantUtil.VIEW_TYPE_PINED:

                view = LayoutInflater.from(parent.getContext())

                        .inflate(R.layout.local_note_list_pined_item, parent, false);

                return new PinedViewHolder(view);

            default:
                view = LayoutInflater.from(parent.getContext())

                        .inflate(R.layout.local_note_list_item, parent, false);

                return new ViewHolder(view);

        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder h, int position) {
        int viewtype = getItemViewType(position);

        Object o = getItem(position);


        if (viewtype == ConstantUtil.VIEW_TYPE_PINED) {

            PinedViewHolder holder = (PinedViewHolder) h;

            NotePinedData pinedData = (NotePinedData) o;

            holder.pinedHeader.setText(pinedData.getNoteType());

            return;
        }

        if (viewtype == ConstantUtil.VIEW_TYPE_HEADER) {

            HeaderViewHolder holder = (HeaderViewHolder) h;
            holder.imageView.setImageResource(R.drawable.ic_add_note);
            return;
        }


        ViewHolder holder = (ViewHolder) h;

        final NoteModel data = (NoteModel) o;

        final Context context = holder.itemView.getContext();

        holder.couse_title_textView.setText(data.title);

        holder.course_time_textView.setText(data.lastModifyTime);

        if (!isScrolling) {

            if (data.category == Note.CAT_MUSIC) {

                Glide.with(context).load(R.drawable.music).apply(RequestOptions
                        .overrideOf(SizeUtils.dp2px(100f), SizeUtils.dp2px(100f))).into(holder.course_thumb_imageView);
            } else {
                Glide.with(context).load(R.drawable.app_logo).apply(RequestOptions
                        .overrideOf(SizeUtils.dp2px(100f), SizeUtils.dp2px(100f))).into(holder.course_thumb_imageView);
            }


        } else {
            holder.course_thumb_imageView.setImageBitmap(def);
        }


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (data.category == Note.CAT_MUSIC) {
                    MusicActivity.Companion.launch(context, data);
                } else {
                    LocalNoteDetailUi.launchFromLocal(context, data);
                }
            }
        });


        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                DialogUtils.showTextListDialogPlus(activity, new OnItemClickListener() {
                    @Override
                    public void onItemClick(DialogPlus dialog, Object item, View view, int position) {
                        dialog.dismiss();

                        noteController.deleteLocalNote(data, new ControllerCallback<Void>() {
                            @Override
                            public void onComplete(@NonNull ResponseValue<Void> responseValue) {
                                if (responseValue.hasError()) {
                                    ToastUtils.showShort(responseValue.errMsg());
                                    return;
                                }

                                EventBusUtils.post(new LocalNoteListChangedEvent());
                            }
                        });


                    }
                }, "删除");


                return true;
            }
        });

        holder.course_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                noteController.saveNoteToNet(data, new ControllerCallback<Void>() {
                    @Override
                    public void onComplete(@NonNull ResponseValue<Void> responseValue) {
                        if (responseValue.hasError()) {
                            ToastUtils.showShort(responseValue.errMsg()+"");
                            return;
                        }

                        ToastUtils.showShort(context.getString(R.string.upload_note_success));

                        EventBusUtils.post(new CloudNoteListChangedEvent());
                    }
                });
            }
        });


        AppListItemAnimatorUtils.startAnim(holder.itemView);
    }


    @Override
    public int getItemViewType(int position) {
        Object data = getItem(position);

        if (data instanceof ListSectionData) {
            return ((ListSectionData) data).sectionViewType;
        }

        if (data instanceof HeadData) {
            return ConstantUtil.VIEW_TYPE_HEADER;
        }

        return ConstantUtil.VIEW_TYPE_ITEM;
    }

    private class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView course_thumb_imageView;
        private TextView couse_title_textView;
        private TextView course_time_textView;
        private TextView course_upload;


        public ViewHolder(View itemView) {
            super(itemView);
            course_thumb_imageView = VF.f(itemView, R.id.course_thumb_imageView);
            couse_title_textView = VF.f(itemView, R.id.couse_title_textView);
            course_time_textView = VF.f(itemView, R.id.course_time_textView);
            course_upload = VF.f(itemView, R.id.course_upload);
        }
    }


    private class HeaderViewHolder extends RecyclerView.ViewHolder {

        private ImageView imageView;

        public HeaderViewHolder(View itemView) {
            super(itemView);
            imageView = VF.f(itemView, R.id.imageView);
        }
    }


    private class PinedViewHolder extends RecyclerView.ViewHolder {

        private TextView pinedHeader;

        public PinedViewHolder(View itemView) {
            super(itemView);
            pinedHeader = VF.f(itemView, R.id.pinedHeader);
        }
    }

}
