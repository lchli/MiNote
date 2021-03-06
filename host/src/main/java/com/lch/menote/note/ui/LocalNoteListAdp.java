package com.lch.menote.note.ui;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.SizeUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.lch.menote.R;
import com.lch.menote.note.data.entity.Note;
import com.lch.menote.note.model.NotePinedData;
import com.lch.menote.note.presenter.LocalNoteAdpPresenter;
import com.lch.menote.utils.DialogTool;
import com.lch.menote.utils.MvpUtils;
import com.lchli.pinedrecyclerlistview.library.ListSectionData;
import com.lchli.pinedrecyclerlistview.library.pinnedRecyclerView.PinnedRecyclerAdapter;
import com.lchli.utils.tool.AppListItemAnimatorUtils;
import com.lchli.utils.tool.ContextProvider;
import com.lchli.utils.tool.DialogUtils;
import com.lchli.utils.tool.TimeUtils;
import com.lchli.utils.tool.VF;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.OnItemClickListener;

/**
 * Created by lichenghang on 2018/5/27.
 */

public class LocalNoteListAdp extends PinnedRecyclerAdapter implements LocalNoteAdpPresenter.MvpView {
    private static final int VIEW_TYPE_ITEM = 0;
    public static final int VIEW_TYPE_PINED = 1;

    private final Bitmap def = BitmapFactory.decodeResource(ContextProvider.context().getResources(), R.drawable.ic_add_note);
    private final Dialog loading;
    private Activity activity;
    private LocalNoteAdpPresenter localNoteAdpPresenter;


    public LocalNoteListAdp(Activity activity) {
        this.activity = activity;
        localNoteAdpPresenter = new LocalNoteAdpPresenter(activity.getApplicationContext(), MvpUtils.newUiThreadWeakProxy(this));
        loading = DialogTool.createLoadingDialog(activity);
        loading.setCancelable(false);

    }

    @Override
    public void showFail(String msg) {
        if (TextUtils.isEmpty(msg)) {
            msg = "上传失败";
        }
        ToastUtils.showShort(msg);
    }

    @Override
    public void showLoading() {
        loading.show();
    }

    @Override
    public void dismissLoading() {
        loading.dismiss();
    }

    @Override
    public void toast(String msg) {
        ToastUtils.showShort(msg);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view;

        switch (viewType) {

            case VIEW_TYPE_PINED:

                view = LayoutInflater.from(parent.getContext())

                        .inflate(R.layout.local_note_list_pined_item, parent, false);

                return new PinedViewHolder(view);


            case VIEW_TYPE_ITEM:
                view = LayoutInflater.from(parent.getContext())

                        .inflate(R.layout.local_note_list_item, parent, false);

                return new ViewHolder(view);

            default:
                return null;

        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder h, int position) {
        if (h == null) {
            return;
        }

        Object o = getItem(position);

        if (h instanceof PinedViewHolder) {

            PinedViewHolder holder = (PinedViewHolder) h;

            NotePinedData pinedData = (NotePinedData) o;

            holder.pinedHeader.setText(pinedData.getNoteType());

            return;
        }


        if (!(h instanceof ViewHolder)) {
            return;
        }


        ViewHolder holder = (ViewHolder) h;

        final Note data = (Note) o;

        final Context context = holder.itemView.getContext();

        holder.couse_title_textView.setText(data.title);

        holder.course_time_textView.setText(TimeUtils.getTime(data.lastModifyTime));

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

                        localNoteAdpPresenter.onDeleteLocalNote(data.uid);

                    }
                }, "删除");


                return true;
            }
        });

        holder.course_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                localNoteAdpPresenter.onUploadNote(data);
            }
        });


        AppListItemAnimatorUtils.startAnim(holder.itemView);
    }


    @Override
    public int getItemViewType(int position) {
        Object data = getItem(position);

        if (data instanceof ListSectionData) {
            return VIEW_TYPE_PINED;
        }

        return VIEW_TYPE_ITEM;
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


    private class PinedViewHolder extends RecyclerView.ViewHolder {

        private TextView pinedHeader;

        public PinedViewHolder(View itemView) {
            super(itemView);
            pinedHeader = VF.f(itemView, R.id.pinedHeader);
        }
    }

}
