package com.lch.menote.user.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.lch.menote.R;
import com.lchli.utils.base.BaseFragment;

/**
 * Created by lchli on 2016/8/10.
 */
public class UserFragmentContainer extends BaseFragment {

    private FrameLayout userFragmentContainer;


    public void toRegister() {
        FragmentTransaction trans = getChildFragmentManager().beginTransaction();
        trans.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left);

        trans.replace(R.id.user_fragment_container, new RegisterFragment());
        trans.commitAllowingStateLoss();
    }

    public void toLogin(boolean isNeedAnim) {
        FragmentTransaction trans = getChildFragmentManager().beginTransaction();
        if (isNeedAnim) {
            trans.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left);
        }
        trans.replace(R.id.user_fragment_container, new LoginFragment());
        trans.commitAllowingStateLoss();
    }

    public void toUserCenter(boolean isNeedAnim) {
        FragmentTransaction trans = getChildFragmentManager().beginTransaction();
        if (isNeedAnim) {
            trans.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left);
        }
        trans.replace(R.id.user_fragment_container, new UserFragmentUi());
        trans.commitAllowingStateLoss();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_container, container, false);
        userFragmentContainer = view.findViewById(R.id.user_fragment_container);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        toUserCenter(false);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        //Override and do not save fragment.
    }


}