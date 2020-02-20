package com.capstone.aryoulearning.animation;

import android.animation.Animator;
import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.FrameLayout;

import com.airbnb.lottie.LottieAnimationView;

public class LottieHelper {

    public enum AnimationType {
        SPARKLES,
        TAP,
        ERROR
    }


    //instantiates a lottie view
    public LottieAnimationView getAnimationView(Context context, AnimationType type){

        LottieAnimationView lav = new LottieAnimationView(context);
        lav.setVisibility(View.VISIBLE);
        lav.loop(false);
        lav.setAnimation(getType(type));
        lav.setScale(1);
        lav.setSpeed(.8f);
        return lav;
    }

    private static String getType(AnimationType type) {
        switch (type) {
            case SPARKLES:
                return "explosionA.json";
            case TAP:
                return "tap.json";
            case ERROR:
                return "error.json";
        }
        return "error.json";
    }

    //adds a lottie view to the corresposnding x and y coordinates
    private void addAnimationViewOnTopOfLetter(LottieAnimationView lav, int x, int y, FrameLayout f) {
        lav.setX(x);
        lav.setY(y);
        f.addView(lav, 300, 300);
        lav.playAnimation();
        lav.addAnimatorListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                f.removeView(lav);
            }

            @Override
            public void onAnimationCancel(Animator animation) {
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
            }
        });
    }

    public void addTapAnimationToScreen(LottieAnimationView lavTap, Activity activity, FrameLayout f) {
        int width = activity.getWindow().getDecorView().getWidth();
        int height = activity.getWindow().getDecorView().getHeight();
        lavTap.setX(width / 2 - 50);
        lavTap.setY(height / 2 - 50);
        f.addView(lavTap, 500, 500);
        lavTap.playAnimation();
    }
}
