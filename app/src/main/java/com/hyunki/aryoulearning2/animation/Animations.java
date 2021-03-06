package com.hyunki.aryoulearning2.animation;

import android.animation.Animator;
import android.animation.FloatEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.BounceInterpolator;
import android.view.animation.LinearInterpolator;

import androidx.cardview.widget.CardView;

import com.google.ar.sceneform.Node;
import com.google.ar.sceneform.Scene;
import com.google.ar.sceneform.math.Vector3Evaluator;
import com.hyunki.aryoulearning2.R;
import com.google.ar.sceneform.math.Quaternion;
import com.google.ar.sceneform.math.QuaternionEvaluator;
import com.google.ar.sceneform.math.Vector3;

public class Animations {

    public static class AR {

        public static ObjectAnimator createRotationAnimator() {
            Quaternion orientation1 = Quaternion.axisAngle(new Vector3(0.0f, 1.0f, 0.0f), 0);
            Quaternion orientation2 = Quaternion.axisAngle(new Vector3(0.0f, 1.0f, 0.0f), 120);
            Quaternion orientation3 = Quaternion.axisAngle(new Vector3(0.0f, 1.0f, 0.0f), 240);
            Quaternion orientation4 = Quaternion.axisAngle(new Vector3(0.0f, 1.0f, 0.0f), 360);

            ObjectAnimator orbitAnimation = new ObjectAnimator();
            orbitAnimation.setObjectValues(orientation1, orientation2, orientation3, orientation4);
            orbitAnimation.setPropertyName("localRotation");
            orbitAnimation.setEvaluator(new QuaternionEvaluator());

            orbitAnimation.setRepeatCount(ObjectAnimator.INFINITE);
            orbitAnimation.setRepeatMode(ObjectAnimator.RESTART);
            orbitAnimation.setInterpolator(new LinearInterpolator());
            orbitAnimation.setAutoCancel(true);

            return orbitAnimation;
        }

        public static ObjectAnimator createFloatAnimator(Node animatedNode) {
            ObjectAnimator floater = ObjectAnimator.ofObject(
                    animatedNode,
                    "localPosition",
                    new Vector3Evaluator(),
                    animatedNode.getLocalPosition(),
                    new Vector3(
                            animatedNode.getLocalPosition().x,
                            animatedNode.getLocalPosition().y + .5f,
                            animatedNode.getLocalPosition().z));

            floater.setRepeatCount(ObjectAnimator.INFINITE);
            floater.setRepeatMode(ObjectAnimator.REVERSE);
            return floater;
        }
    }


    public static class Normal {
        public static ObjectAnimator setCardFadeInAnimator(CardView cv) {
            cv.setAlpha(0);
            cv.setVisibility(View.VISIBLE);
            ObjectAnimator fadeAnimation = ObjectAnimator.ofFloat(cv, "alpha", 0f, 1.0f);
            fadeAnimation.setDuration(1000);
            fadeAnimation.setStartDelay(500);
            return fadeAnimation;
        }

        public static ObjectAnimator setCardFadeOutAnimator(CardView cv) {
            ObjectAnimator fadeAnimation = ObjectAnimator.ofFloat(cv, "alpha", 1.0f, 0f);
            fadeAnimation.setDuration(1000);
            return fadeAnimation;
        }

        public static Animation getVibrator(View itemView) {
            return AnimationUtils.loadAnimation(itemView.getContext(), R.anim.vibrate);
        }

    }

}
