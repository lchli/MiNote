package com.lch.menote.note.ui;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.lch.menote.common.base.BsListAdapter;
import com.lch.menote.note.R;
import com.lch.menote.note.controller.NoteElementController;
import com.lch.menote.note.domain.NoteElement;
import com.lch.netkit.common.tool.VF;

import org.jetbrains.annotations.NotNull;

/**
 * Created by lichenghang on 2018/5/20.
 */

public class NoteElementAdapter extends BsListAdapter<NoteElement> {
    private static final int ITEM_TEXT = 0;
    private static final int ITEM_IMG = 1;
    private NoteElementController controller;
    private Callback callback;

    public interface Callback{
        void showOperation(NoteElement e);
    }

    public NoteElementAdapter(NoteElementController controller,Callback cb) {
        this.controller = controller;
        callback=cb;
    }

    @NotNull
    @Override
    public AbsViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        View item;

        switch (viewType) {
            case ITEM_TEXT:
                item = View.inflate(parent.getContext(), R.layout.list_item_note_element_text, null);
                return new HText(item, viewType);
            case ITEM_IMG:
                item = View.inflate(parent.getContext(), R.layout.list_item_note_element_img, null);
                return new HImg(item, viewType);
            default:
                item = View.inflate(parent.getContext(), R.layout.list_item_note_element_text, null);
                return new HText(item, viewType);

        }

    }

    @Override
    public void onBindViewHolder(@NotNull AbsViewHolder holder, int position) {
        int viewType = getItemViewType(position);
        final NoteElement data = getItem(position);
        if (data == null) {
            return;
        }

        switch (viewType) {
            case ITEM_TEXT: {
               final HText h = (HText) holder;
                h.note_edittext.setText(data.text);

                h.note_radioButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                      callback.showOperation(data);

                    }
                });

                h.note_edittext.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        data.text=h.note_edittext.getText().toString();


                    }
                });
            }
            break;
            case ITEM_IMG: {
                HImg h = (HImg) holder;
                h.note_imageview.setImageResource(R.drawable.ic_add_note);
                h.note_radioButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        callback.showOperation(data);
                    }
                });
            }
            break;


        }


    }

    @Override
    public int getItemViewType(int position) {
        final int def = ITEM_TEXT;

        NoteElement data = getItem(position);
        if (data == null || data.type == null) {
            return def;
        }

        switch (data.type) {
            case NoteElement.TYPE_TEXT:
                return ITEM_TEXT;
            case NoteElement.TYPE_IMG:
                return ITEM_IMG;
            default:
                return def;
        }

    }

    private class HText extends AbsViewHolder {
        private EditText note_edittext;
        private Button note_radioButton;

        public HText(@NotNull View itemView, int viewType) {
            super(itemView, viewType);
            note_edittext = VF.f(itemView, R.id.note_edittext);
            note_radioButton = VF.f(itemView, R.id.note_radioButton);

        }
    }

    private class HImg extends AbsViewHolder {
        private ImageView note_imageview;
        private Button note_radioButton;

        public HImg(@NotNull View itemView, int viewType) {
            super(itemView, viewType);
            note_imageview = VF.f(itemView, R.id.note_imageview);
            note_radioButton = VF.f(itemView, R.id.note_radioButton);

        }
    }
}
