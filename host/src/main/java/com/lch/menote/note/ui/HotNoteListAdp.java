package com.lch.menote.note.ui;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.lch.menote.R;
import com.lch.menote.note.model.HeadData;
import com.lch.menote.note.data.entity.Note;
import com.lch.menote.note.model.NoteModel;
import com.lch.menote.user.ui.UserInfoActivity;
import com.lch.netkit.common.base.AbsAdapter;
import com.lch.netkit.common.tool.AppListItemAnimatorUtils;
import com.lch.netkit.common.tool.ContextProvider;
import com.lch.netkit.common.tool.TimeUtils;
import com.lch.netkit.common.tool.VF;

/**
 * Created by lichenghang on 2018/5/27.
 */

public class HotNoteListAdp extends AbsAdapter<Object> {
    private static final int VIEW_TYPE_HEADER = 0;
    private static final int VIEW_TYPE_ITEM = 1;

    private final Bitmap def = BitmapFactory.decodeResource(ContextProvider.context().getResources(), R.drawable.ic_add_note);


    public HotNoteListAdp() {
    }


    @Override
    public AbsAdapter.AbsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;

        switch (viewType) {

            case VIEW_TYPE_HEADER:
                view = LayoutInflater.from(parent.getContext())

                        .inflate(R.layout.local_note_list_header, parent, false);

                return new HeaderViewHolder(viewType, view);

            default:
                view = LayoutInflater.from(parent.getContext())

                        .inflate(R.layout.hot_note_list_item, parent, false);

                return new ViewHolder(viewType, view);

        }
    }

    @Override
    public void onBindViewHolder(AbsAdapter.AbsViewHolder h, int position) {
        int viewtype = getItemViewType(position);

        Object o = getItem(position);

        if (viewtype == VIEW_TYPE_HEADER) {

            HeaderViewHolder holder = (HeaderViewHolder) h;
            holder.imageView.setImageResource(R.drawable.note_banner);
            return;
        }


        final ViewHolder holder = (ViewHolder) h;

        final NoteModel data = (NoteModel) o;

        final Context context = holder.itemView.getContext();

        holder.couse_title_textView.setText(data.title);
        int star = data.star != null ? data.star.size() : 0;
        holder.course_star_textView.setText(star + "");
        holder.course_userName_textView.setText(data.userName);

        holder.course_time_textView.setText(TimeUtils.getTime(data.updateTime));
        holder.course_thumb_imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserInfoActivity.launch(data.userId, holder.getItemView().getContext());
            }
        });

        if (!isScrolling) {

            Glide.with(context).load(data.userHeadUrl).apply(RequestOptions
                    .placeholderOf(R.drawable.add_portrait)).into(holder.course_thumb_imageView);

        } else {
            holder.course_thumb_imageView.setImageBitmap(def);
        }


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (data.category == Note.CAT_MUSIC) {
                    MusicActivity.Companion.launch(context, data);
                } else {
                    LocalNoteDetailUi.launchFromCloud(context, data);
                }
            }
        });

        AppListItemAnimatorUtils.startAnim(holder.itemView);
    }


    @Override
    public int getItemViewType(int position) {
        Object data = getItem(position);

        if (data instanceof HeadData) {
            return VIEW_TYPE_HEADER;
        }

        return VIEW_TYPE_ITEM;
    }

    private class ViewHolder extends AbsAdapter.AbsViewHolder {
        private ImageView course_thumb_imageView;
        private TextView couse_title_textView;
        private TextView course_time_textView;
        private TextView course_star_textView;
        private TextView course_userName_textView;
        private View itemView;


        public ViewHolder(int viewtype, View view) {
            super(viewtype);

            itemView = view;
            course_thumb_imageView = VF.f(itemView, R.id.course_thumb_imageView);
            couse_title_textView = VF.f(itemView, R.id.couse_title_textView);
            course_time_textView = VF.f(itemView, R.id.course_time_textView);
            course_star_textView = VF.f(itemView, R.id.course_star_textView);
            course_userName_textView = VF.f(itemView, R.id.course_userName_textView);
        }

        @Override
        protected View getItemView() {
            return itemView;
        }
    }


    private class HeaderViewHolder extends AbsAdapter.AbsViewHolder {

        private ImageView imageView;
        private View itemView;

        public HeaderViewHolder(int viewtype, View view) {
            super(viewtype);
            itemView = view;
            imageView = VF.f(itemView, R.id.imageView);
        }

        @Override
        protected View getItemView() {
            return itemView;
        }
    }

}
