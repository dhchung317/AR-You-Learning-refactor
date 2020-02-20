package com.capstone.aryoulearning.ui.main.ar;

import android.animation.ObjectAnimator;

import com.capstone.aryoulearning.animation.Animations;
import com.capstone.aryoulearning.di.ActivityBuildersModule;
import com.google.ar.core.Anchor;
import com.google.ar.core.Pose;
import com.google.ar.core.Session;
import com.google.ar.core.exceptions.CameraNotAvailableException;
import com.google.ar.sceneform.AnchorNode;
import com.google.ar.sceneform.Node;
import com.google.ar.sceneform.math.Vector3;
import com.google.ar.sceneform.rendering.ModelRenderable;
import com.google.ar.sceneform.ux.ArFragment;
import com.google.ar.sceneform.ux.TransformableNode;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class ModelUtil {

    private static Random r = new Random();
    private static Set<Vector3> collisionSet = new HashSet<>();

    static Node getGameAnchor(ModelRenderable model){
        Node base = new Node();
        Node mainModel = new Node();
        mainModel.setParent(base);

        mainModel.setRenderable(model);

        mainModel.setLocalPosition(new Vector3(base.getLocalPosition().x,//x
                base.getLocalPosition().y,//y
                base.getLocalPosition().z));
        mainModel.setLookDirection(new Vector3(0, 0, 4));
        mainModel.setLocalScale(new Vector3(1.0f, 1.0f, 1.0f));

//        for (int i = 0; i < name.length(); i++) {
//            createLetter(
//                    Character.toString(name.charAt(i)),
//                    name, base, model);
//        }

        ObjectAnimator rotate = Animations.AR.createRotationAnimator();
        rotate.setTarget(mainModel);
        rotate.setDuration(7000);
        rotate.start();

        return base;
//        collisionSet.clear(); // should call this outside of this method as it is being called

    }

//    static void placeModel(){
//
//    }

//    static void placeWord(String word){
//
//    }

    static AnchorNode getLetter(Node parent, ModelRenderable renderable, ArFragment arFragment){

        float[] pos = {0,//x
                0,//y
                0};//z
        float[] rotation = {0, 0, 0, 0};

        Session session = arFragment.getArSceneView().getSession();
        Anchor anchor = null;

        if (session != null) {

            try {
                session.resume();

            } catch (CameraNotAvailableException e) {
                e.printStackTrace();
            }
            anchor = session.createAnchor(new Pose(pos, rotation));

        }

        AnchorNode base = new AnchorNode(anchor);
        TransformableNode trNode = new TransformableNode(arFragment.getTransformationSystem());
        trNode.setRenderable(renderable);
        trNode.setParent(base);


        Vector3 coordinates = getRandomCoordinates();

        while (checkDoesLetterCollide(coordinates, parent.getLocalPosition())) {
            coordinates = getRandomCoordinates();
        }

        trNode.setLocalPosition(coordinates);

        return base;
    }

    private static int getRandom(int max, int min) {
        return r.nextInt((max - min)) + min;
    }

    private static boolean checkDoesLetterCollide(Vector3 newV3, Vector3 parentModel) {
        if (collisionSet.isEmpty()) {
            collisionSet.add(newV3);
            return false;
        }

        if ((newV3.x < parentModel.x + 2) && (newV3.x > parentModel.x - 2)
                && (newV3.y < parentModel.y + 2) && (newV3.y > parentModel.y - 2)
                && (newV3.z < parentModel.z + 2) && (newV3.z > parentModel.z - 10)) {
            return true;
        }

        for (Vector3 v : collisionSet) {
            //if the coordinates are within a range of any exisiting coordinates
            if (((newV3.x < v.x + 2 && newV3.x > v.x - 2)
                    && (newV3.y < v.y + 3 && newV3.y > v.y - 3))) {
                return true;
            } else {
                collisionSet.add(newV3);
                return false;
            }
        }
        return true;
    }

    private static Vector3 getRandomCoordinates() {
        return new Vector3(getRandom(5, -5),//x
                getRandom(1, -2),//y
                getRandom(-2, -10));//z
    }

//    private void createLetter(String letter, String word,
//                              Node parent,
//                              ModelRenderable renderable) {
//
//        Session session = arFragment.getArSceneView().getSession();
//
//
//
//        Anchor anchor = null;
//
//        if (session != null) {
//
//            try {
//                session.resume();
//
//            } catch (CameraNotAvailableException e) {
//                e.printStackTrace();
//            }
//            anchor = session.createAnchor(new Pose(pos, rotation));
//        }
//
//        AnchorNode base = new AnchorNode(anchor);
//        arFragment.getArSceneView().getScene().addChild(base);
//        base.setParent(parent);
//        TransformableNode trNode = new TransformableNode(arFragment.getTransformationSystem());
//        // Create the planet and position it relative to the sun.
//        trNode.setParent(base);
//
//        trNode.setRenderable(renderable);
////        trNode.setLocalScale(new Vector3(.1f,.1f,.1f));
//        Vector3 coordinates = getRandomCoordinates();
//
//        while (checkDoesLetterCollide(coordinates, parent.getLocalPosition())) {
//            coordinates = getRandomCoordinates();
//        }
//        trNode.setLocalPosition(coordinates);
}
