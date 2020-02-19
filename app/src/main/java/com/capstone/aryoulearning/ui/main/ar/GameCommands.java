package com.capstone.aryoulearning.ui.main.ar;

import android.animation.ObjectAnimator;

import com.capstone.aryoulearning.animation.Animations;
import com.google.ar.core.Anchor;
import com.google.ar.core.HitResult;
import com.google.ar.sceneform.AnchorNode;
import com.google.ar.sceneform.Node;
import com.google.ar.sceneform.math.Vector3;
import com.google.ar.sceneform.rendering.ModelRenderable;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class GameCommands {
    private Anchor mainAnchor;
    private AnchorNode mainAnchorNode;
    private HitResult mainHit;
    private Node base;

    public GameCommands(Anchor mainAnchor, AnchorNode mainAnchorNode, HitResult mainHit, Node base) {
        this.mainAnchor = mainAnchor;
        this.mainAnchorNode = mainAnchorNode;
        this.mainHit = mainHit;
        this.base = base;
    }

//    private Node createGame(
//            Map<String, ModelRenderable> modelMap,
//            HashMap<String, ModelRenderable> letterMap) {
//
//        base = new Node();
//
//        Node mainModel = new Node();
//        mainModel.setParent(base);
//
//        ObjectAnimator rotate = Animations.AR.createRotationAnimator();
//        rotate.setTarget(mainModel);
//        rotate.setDuration(7000);
//        rotate.start();
//
//        for (Map.Entry<String, ModelRenderable> e : modelMap.entrySet()) {
//
//            mainModel.setRenderable(e.getValue());
//
//            mainModel.setLocalPosition(new Vector3(base.getLocalPosition().x,//x
//                    base.getLocalPosition().y,//y
//                    base.getLocalPosition().z));
//            mainModel.setLookDirection(new Vector3(0, 0, 4));
//            mainModel.setLocalScale(new Vector3(1.0f, 1.0f, 1.0f));
//
////            String randomWord = e.getKey() + "abcdefghijklmnopqrstuvwxyz";
////            collisionSet.clear();
////            pronunciationUtil.textToSpeechAnnouncer(e.getKey(), pronunciationUtil.textToSpeech);
//
//            for (int i = 0; i < e.getKey().length(); i++) {
//                createLetter(
//                        Character.toString(e.getKey().charAt(i)),
//                        e.getKey(), base, letterMap.get(
//                                Character.toString(e.getKey().charAt(i))));
//            }
//
//            currentWord = e.getKey();
//        }
//        return base;
//    }
}
