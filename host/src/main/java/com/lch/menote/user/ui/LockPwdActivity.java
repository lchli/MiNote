package com.lch.menote.user.ui;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.andrognito.patternlockview.PatternLockView;
import com.andrognito.patternlockview.listener.PatternLockViewListener;
import com.blankj.utilcode.util.ToastUtils;
import com.lch.menote.R;
import com.lch.menote.note.NoteApiManager;
import com.lch.menote.user.presenterx.LockPwdPresenter;
import com.lch.menote.utils.DialogTool;
import com.lch.menote.utils.MvpUtils;
import com.lchli.utils.base.BaseCompatActivity;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.OnItemClickListener;

import java.util.Arrays;
import java.util.List;


public class LockPwdActivity extends BaseCompatActivity implements LockPwdPresenter.MvpView {

    private StringBuilder inputPwd = new StringBuilder();
    private LockPwdPresenter mUserController;
    private PatternLockView patternLockView;
    private TextView tvResetPwd;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mUserController = new LockPwdPresenter(this, MvpUtils.newUiThreadWeakProxy(this));

        setContentView(R.layout.activity_pwd);
        tvResetPwd = f(R.id.tvResetPwd);
        patternLockView = f(R.id.patternLockView);

        final int[][] numbers = new int[patternLockView.getDotCount()][patternLockView.getDotCount()];
        int i = 0;
        for (int[] childarr : numbers) {
            for (int index = 0; index < childarr.length; index++) {
                childarr[index] = i++;
            }
        }

        patternLockView.addPatternLockListener(new PatternLockViewListener() {
            @Override
            public void onStarted() {
                inputPwd = new StringBuilder();
            }

            @Override
            public void onProgress(List<PatternLockView.Dot> progressPattern) {

            }

            @Override
            public void onComplete(List<PatternLockView.Dot> pattern) {
                if (pattern != null) {
                    for (PatternLockView.Dot dot : pattern) {
                        inputPwd.append(numbers[dot.getRow()][dot.getColumn()]);
                    }
                }

            }

            @Override
            public void onCleared() {

            }
        });
        tvResetPwd.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                reset();
                return true;
            }
        });
    }


    public void ok(View v) {
        mUserController.onSavePwd(inputPwd.toString());
    }


    private void reset() {

        DialogTool.showListDialog(this, false, Arrays.asList("重置会导致所有数据丢失，是否确认？", "确认"), new OnItemClickListener() {
            @Override
            public void onItemClick(DialogPlus dialog, Object item, View view, int position) {
                dialog.dismiss();

                if (position == 1) {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            showConfirmReset();
                        }
                    },1000);


                }
            }
        });


    }

    private void showConfirmReset() {
        DialogTool.showListDialog(this, false, Arrays.asList("请再次确认?", "确定"), new OnItemClickListener() {
            @Override
            public void onItemClick(DialogPlus dialog, Object item, View view, int position) {
                dialog.dismiss();

                if (position == 1) {
                    NoteApiManager.getINS().clearDB();
                    ToastUtils.showShort("重置成功");
                }
            }
        });
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void dismissLoading() {

    }

    @Override
    public void showFail(String msg) {
        ToastUtils.showShort(msg);
    }

    @Override
    public void finishUi() {
        finish();
    }

}
