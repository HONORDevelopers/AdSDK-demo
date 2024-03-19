package com.hihonor.adsdk.demo.external.splash;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.view.View;

import androidx.interpolator.view.animation.FastOutSlowInInterpolator;

public class AnimatorUtils {

    public static void startScaleAndAlphaAnimator(View view, int duration, AnimatorListener listener) {
        FastOutSlowInInterpolator inInterpolator = new FastOutSlowInInterpolator();
        Animator scaleXAnimator = ObjectAnimator.ofFloat(view, "scaleX", 0.7f, 1f);
        scaleXAnimator.setDuration(duration);
        scaleXAnimator.setInterpolator(inInterpolator);

        Animator scaleYAnimator = ObjectAnimator.ofFloat(view, "scaleY", 0.7f, 1f);
        scaleYAnimator.setDuration(duration);
        scaleYAnimator.setInterpolator(inInterpolator);

        Animator alphaAnimator = ObjectAnimator.ofFloat(view, "alpha", 0f, 1f);
        alphaAnimator.setDuration(duration);
        alphaAnimator.setInterpolator(inInterpolator);

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.play(scaleXAnimator)
                .with(scaleYAnimator)
                .with(alphaAnimator);
        animatorSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                listener.onAnimationEnd();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        animatorSet.start();
    }

    public interface AnimatorListener {
        void onAnimationEnd();
    }

}
