package com.lch.menote.note.ui;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lch.menote.common.base.BsListAdapter;
import com.lch.menote.note.R;
import com.lch.netkit.common.tool.VF;

import org.jetbrains.annotations.NotNull;

/**
 * Created by lichenghang on 2018/5/20.
 */

public class TextOptionItemAdapter extends BsListAdapter<String> {



    @NotNull
    @Override
    public AbsViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        View item = View.inflate(parent.getContext(), R.layout.list_item_insert_image_dialog, null);

        return new H(item, viewType);
    }

    @Override
    public void onBindViewHolder(@NotNull AbsViewHolder holder, int position) {
        H h = (H) holder;
        h.text_view.setText(getItem(position));

    }


    private class H extends AbsViewHolder {
        private TextView text_view;

        public H(@NotNull View itemView, int viewType) {
            super(itemView, viewType);
            text_view = VF.f(itemView, R.id.text_view);

        }
    }
}
