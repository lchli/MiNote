package com.lch.menote.common.util;

import android.app.Activity;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;

import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.OnItemClickListener;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Administrator on 2018/5/22.
 */

public final class DialogUtils {

    public static void showTextListDialogPlus(Activity activity, OnItemClickListener lsn, String... items) {
        showTextListDialogPlus(activity, lsn, Arrays.asList(items));
    }

    public static void showTextListDialogPlus(Activity activity, OnItemClickListener lsn, List<String> items) {
        final TextOptionItemAdapter adapter = new TextOptionItemAdapter();
        adapter.refresh(items);

        DialogPlus.newDialog(activity)
                .setAdapter(adapter)
                .setOnItemClickListener(lsn).create().show();
    }

    public static void showTextListDialog(Activity activity, DialogInterface.OnClickListener lsn, @NonNull List<CharSequence> items) {

        CharSequence[] ret = new CharSequence[items.size()];
        items.toArray(ret);
        new AlertDialog.Builder(activity).setItems(ret, lsn).show();
    }

    public static void showTextListDialog(Activity activity, DialogInterface.OnClickListener lsn, CharSequence... items) {
        new AlertDialog.Builder(activity).setItems(items, lsn).show();
    }
}
