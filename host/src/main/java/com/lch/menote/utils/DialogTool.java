package com.lch.menote.utils;

import android.app.Activity;
import android.app.Dialog;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.lch.menote.R;
import com.lch.netkit.common.tool.VF;

/**
 * Created by lichenghang on 2018/6/8.
 */

public final class DialogTool {

    public interface InputDialogListener {
        void onConfirm(Dialog dialog,String inputText);
    }

    public static Dialog showInputDialog(Activity activity, String title, @NonNull final InputDialogListener confirmClickLsn) {

        final Dialog d = new Dialog(activity);
        d.setContentView(R.layout.input_dialog);

        final EditText etInput = VF.f(d, R.id.etInput);
        final TextView tvTitle = VF.f(d, R.id.tvTitle);
        final Button btOk = VF.f(d, R.id.btOk);

        tvTitle.setText(title);
        btOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmClickLsn.onConfirm(d,etInput.getText().toString());
            }
        });

        d.show();

        return d;
    }
}
