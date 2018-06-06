package com.lch.menote.note.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.Switch;

import com.lch.menote.R;
import com.lch.netkit.common.base.BaseFragment;
import com.lch.netkit.common.tool.VF;

/**
 * Created by lichenghang on 2018/6/3.
 */

public class NoteFragmentContainer extends BaseFragment {
    private FrameLayout viewpager;
    private Switch swNoteLocation;
    private LocalNoteUi localNoteUi = new LocalNoteUi();
    private CloudNoteUi cloudNoteUi = new CloudNoteUi();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.note_frament_container, container, false);

        viewpager = VF.f(v, R.id.viewpager);
        swNoteLocation = VF.f(v, R.id.swNoteLocation);

        swNoteLocation.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    showCloudNote();
                } else {
                    showLocalNote();
                }
            }
        });


        showLocalNote();

        return v;
    }

    private void showLocalNote() {

        FragmentTransaction tans = getChildFragmentManager().beginTransaction();
        tans.replace(R.id.viewpager, localNoteUi);
        tans.commitAllowingStateLoss();
    }

    private void showCloudNote() {

        FragmentTransaction tans = getChildFragmentManager().beginTransaction();
        tans.replace(R.id.viewpager, cloudNoteUi);
        tans.commitAllowingStateLoss();
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        //Override and do not save.
    }
}
