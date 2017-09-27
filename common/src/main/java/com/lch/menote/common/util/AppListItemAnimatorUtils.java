package com.lch.menote.common.util;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by lchli on 2016/9/18.
 */

public class AppListItemAnimatorUtils {

    public static void startAnim(View view) {
        view.clearAnimation();
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(view, "scaleY", 0.2f, 1f);
        scaleY.setDuration(300);
        scaleY.start();
    }

    public static void startHeightAnim(final View view, int from, int to) {
        view.clearAnimation();

        ValueAnimator anim = ValueAnimator.ofInt(from, to);
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                int val = (Integer) valueAnimator.getAnimatedValue();
                ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
                layoutParams.height = val;
                view.setLayoutParams(layoutParams);
            }
        });
        anim.setDuration(300);
        anim.start();
    }

}
