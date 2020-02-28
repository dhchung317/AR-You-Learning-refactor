package com.hyunki.aryoulearning2.ui.main.ar;

import android.Manifest;
import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.lifecycle.ViewModelProviders;

import com.airbnb.lottie.LottieAnimationView;
import com.google.ar.core.Anchor;
import com.google.ar.core.Frame;
import com.google.ar.core.HitResult;
import com.google.ar.core.Plane;
import com.google.ar.core.Trackable;
import com.google.ar.core.TrackingState;
import com.google.ar.sceneform.AnchorNode;
import com.google.ar.sceneform.HitTestResult;
import com.google.ar.sceneform.Node;
import com.google.ar.sceneform.Scene;
import com.google.ar.sceneform.rendering.ModelRenderable;
import com.google.ar.sceneform.ux.ArFragment;
import com.hyunki.aryoulearning2.R;
import com.hyunki.aryoulearning2.animation.Animations;
import com.hyunki.aryoulearning2.animation.LottieHelper;
import com.hyunki.aryoulearning2.model.Model;
import com.hyunki.aryoulearning2.ui.main.controller.NavListener;
import com.hyunki.aryoulearning2.ui.main.needsrefactor.ResultsFragment;
import com.hyunki.aryoulearning2.util.audio.PronunciationUtil;
import com.hyunki.aryoulearning2.viewmodel.ViewModelProviderFactory;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import javax.inject.Inject;

import dagger.android.support.AndroidSupportInjection;
import dagger.android.support.DaggerFragment;

public class ARHostFragmentX extends DaggerFragment implements GameCommandListener {
    private static final int RC_PERMISSIONS = 0x123;
    public static final String MODEL_LIST = "MODEL_LIST";
    GameManager gameManager;


    @Inject
    ViewModelProviderFactory viewModelProviderFactory;

    @Inject
    LottieHelper lottieHelper;

    private ArViewModel arViewModel;

    private ArFragment arFragment;

    private NavListener listener;
    private GestureDetector gestureDetector;
    private PronunciationUtil pronunciationUtil;
    private MediaPlayer playBalloonPop;

    private SharedPreferences prefs;

    private boolean hasFinishedLoadingModels = false;
    private boolean hasFinishedLoadingLetters = false;
    private boolean hasPlacedGame = false;
    private boolean placedAnimation;

    private FrameLayout f;

    private LinearLayout wordContainer;
    private View wordValidatorLayout;
    private CardView wordCardView;
    private CardView wordValidatorCv;
    private TextView wordValidator;
    private TextView validatorWord;
    private TextView validatorWrongWord;
    private TextView validatorWrongPrompt;
    private ImageView validatorImage;
    private ImageView validatorBackgroudImage;
    private Button validatorOkButton;
    private ImageButton undo;

    private LottieAnimationView tapAnimation;

    private View exitMenu;
    private ImageButton exit;
    private Button exitYes;
    private Button exitNo;

    private ObjectAnimator fadeIn;
    private ObjectAnimator fadeOut;

    private List<Model> categoryList = new ArrayList<>();
    private List<HashMap<String, ModelRenderable>> modelMapList = new ArrayList<>();
    private HashMap<String, ModelRenderable> letterMap = new HashMap<>();
    private String letters = "";
    private String currentWord = "";

    private Set<String> correctAnswerSet = new HashSet<>();
    private List<String> wrongAnswerList = new ArrayList<>();

    private int roundCounter = 0;
    private int roundLimit = 5;

//    private Random r = new Random();
//    private Set<Vector3> collisionSet = new HashSet<>();

    private TextToSpeech textToSpeech;

    private Node base;
    private Anchor mainAnchor;
    private AnchorNode mainAnchorNode;
    private HitResult mainHit;

    @Inject
    public ARHostFragmentX(PronunciationUtil pronunciationUtil) {
        this.pronunciationUtil = pronunciationUtil;
        this.textToSpeech = pronunciationUtil.textToSpeech;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof NavListener) {
            listener = (NavListener) context;
        }
        AndroidSupportInjection.inject(this);
//        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_arfragment_host, container, false);
        arFragment = (ArFragment) getChildFragmentManager().findFragmentById(R.id.ux_fragment);
        return rootView;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
//        playBalloonPop = MediaPlayer.create(getContext(), R.raw.pop_effect);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        f = view.findViewById(R.id.frame_layout);
        arViewModel = ViewModelProviders.of(this, viewModelProviderFactory).get(ArViewModel.class);
        arViewModel.loadModels();

        arViewModel.getModelLiveData().observe(getViewLifecycleOwner(), models -> {
            categoryList = models;
            arViewModel.setListMapsOfFutureModels(models);
        });

        arViewModel.getFutureModelMapList().observe(getViewLifecycleOwner(), hashMaps -> {
            arViewModel.setModelRenderables(hashMaps);
            arViewModel.setMapOfFutureLetters(hashMaps);
        });

        arViewModel.getFutureLetterMap().observe(getViewLifecycleOwner(), map -> {
            arViewModel.setLetterRenderables(map);
        });

        arViewModel.getModelMapList().observe(getViewLifecycleOwner(), hashMaps -> {
            modelMapList = hashMaps;
            Log.d("getmodelmaplist", "onViewCreated: " + hashMaps.size());

            for (int i = 0; i < hashMaps.size(); i++) {
                Log.d("getmodelkeyset", "onViewCreated: " + hashMaps.get(i).keySet());

//                for(Map.Entry<String,ModelRenderable> e : hashMaps.get(i).entrySet()){
//                    Log.d("getmodelloop", "onViewCreated: " + e.getKey());
//                }
            }

            hasFinishedLoadingModels = true;
            List<String> list = getKeysFromModelMapList(hashMaps);
//            Log.d("getmodelmaplist", "onViewCreated: " + list.get(0));
//            gameManager = new GameManager(getKeysFromModelMapList(hashMaps),this);
        });

        arViewModel.getLetterMap().observe(getViewLifecycleOwner(), returnMap -> {
            letterMap = returnMap;
            hasFinishedLoadingLetters = true;
        });

        setUpViews(view);

        gestureDetector = getGestureDetector();

        setUpARScene(arFragment);

//        gestureDetector =
//                new GestureDetector(
//                        getActivity(),
//                        new GestureDetector.SimpleOnGestureListener() {
//                            @Override
//                            public boolean onSingleTapUp(MotionEvent e) {
//                                onSingleTap(e);
//                                return true;
//                            }
//
//                            @Override
//                            public boolean onDown(MotionEvent e) {
//                                return true;
//                            }
//                        });
//
//        arFragment.getArSceneView()
//                .getScene()
//                .setOnTouchListener(
//                        (HitTestResult hitTestResult, MotionEvent event) -> {
//                            // If the solar system hasn't been placed yet, detect a tap and then check to see if
//                            // the tap occurred on an ARCore plane to place the solar system.
//                            if (!hasPlacedGame) {
//                                return gestureDetector.onTouchEvent(event);
//                            }
//                            // Otherwise return false so that the touch event can propagate to the scene.
//                            return false;
//                        });
//
//        arFragment.getArSceneView()
//                .getScene()
//                .addOnUpdateListener(
//                        frameTime -> {
//                            Frame frame = arFragment.getArSceneView().getArFrame();
//
//                            if (frame == null) {
//                                return;
//                            }
//                            if (frame.getCamera().getTrackingState() != TrackingState.TRACKING) {
//                                return;
//                            }
//                            if (!hasPlacedGame) {
//                                for (Plane plane : frame.getUpdatedTrackables(Plane.class)) {
//                                    if (!placedAnimation && plane.getTrackingState() == TrackingState.TRACKING) {
//                                        placedAnimation = true;
//                                        tapAnimation = lottieHelper.getAnimationView(getContext(), LottieHelper.AnimationType.TAP);
//                                        lottieHelper.addTapAnimationToScreen(tapAnimation, getActivity(), f);
//                                    }
//                                }
//                            }
//                        });
//         Lastly request CAMERA permission which is required by ARCore.
        requestCameraPermission(getActivity(), RC_PERMISSIONS);
    }

    private List<String> getKeysFromModelMapList(List<HashMap<String,ModelRenderable>> mapList){
        List<String> keys = new ArrayList<>();

        for (int i = 0; i < mapList.size(); i++) {
            for(Map.Entry<String,ModelRenderable> e : mapList.get(i).entrySet()){
                keys.add(e.getKey());
            }
        }

        return keys;
    }

    private void setUpViews(View view) {
        initViews(view);
        setListeners();
//        setAnimations();
    }

    private GestureDetector getGestureDetector() {
        return new GestureDetector(
                getActivity(),
                new GestureDetector.SimpleOnGestureListener() {
                    @Override
                    public boolean onSingleTapUp(MotionEvent e) {
                        onSingleTap(e);
                        return true;
                    }

                    @Override
                    public boolean onDown(MotionEvent e) {
                        return true;
                    }
                });
    }

    private void setUpARScene(ArFragment arFragment) {
        Scene scene = arFragment.getArSceneView()
                .getScene();
        Frame frame = arFragment.getArSceneView().getArFrame();

        setOnTouchListener(scene);
        setAddOnUpdateListener(scene, frame);
    }

    private void setOnTouchListener(Scene scene) {
        scene.setOnTouchListener(
                (HitTestResult hitTestResult, MotionEvent event) -> {
                    if (!hasPlacedGame) {
                        return gestureDetector.onTouchEvent(event);
                    }
                    return false;
                });
    }

    private void setAddOnUpdateListener(Scene scene, Frame frame) {
        scene.addOnUpdateListener(
                frameTime -> {

                    if (frame == null) {
                        return;
                    }
                    if (frame.getCamera().getTrackingState() != TrackingState.TRACKING) {
                        return;
                    }
                    if (!hasPlacedGame) {
                        for (Plane plane : frame.getUpdatedTrackables(Plane.class)) {
                            if (!placedAnimation && plane.getTrackingState() == TrackingState.TRACKING) {
                                placedAnimation = true;
                                tapAnimation = lottieHelper.getAnimationView(getContext(), LottieHelper.AnimationType.TAP);
                                lottieHelper.addTapAnimationToScreen(tapAnimation, getActivity(), f);
                            }
                        }
                    }
                });
    }

    private void initViews(View view) {
        wordCardView = view.findViewById(R.id.card_wordContainer);
        wordContainer = view.findViewById(R.id.word_container);
        wordValidatorLayout = getLayoutInflater().inflate(R.layout.validator_card, f, false);
//        wordValidatorCv = wordValidatorLayout.findViewById(R.id.word_validator_cv);
        wordValidator = wordValidatorLayout.findViewById(R.id.word_validator);
        validatorImage = wordValidatorLayout.findViewById(R.id.validator_imageView);
        validatorBackgroudImage = wordValidatorLayout.findViewById(R.id.correct_star_imageView);
        validatorWord = wordValidatorLayout.findViewById(R.id.validator_word);
        validatorWrongPrompt = wordValidatorLayout.findViewById(R.id.validator_incorrect_prompt);
        validatorWrongWord = wordValidatorLayout.findViewById(R.id.validator_wrong_word);
        validatorOkButton = wordValidatorLayout.findViewById(R.id.button_validator_ok);
//        wordValidatorCv.setVisibility(View.INVISIBLE);
        exitMenu = getLayoutInflater().inflate(R.layout.exit_menu_card, f, false);
        exit = view.findViewById(R.id.exit_imageButton);
        exitYes = exitMenu.findViewById(R.id.exit_button_yes);
        exitNo = exitMenu.findViewById(R.id.exit_button_no);
        undo = view.findViewById(R.id.button_undo);
    }

    private void setListeners() {
        exit.setOnClickListener(v -> f.addView(exitMenu));
        exitYes.setOnClickListener(v -> {
                    listener.moveToListFragment();
                }
        );
        exitNo.setOnClickListener(v -> f.removeView(exitMenu));
//        undo.setOnClickListener(v -> recreateErasedLetter(eraseLastLetter(letters)));
    }
    //TODO - refactor animations to separate class
//
    private void setAnimations() {
        fadeIn = Animations.Normal.setCardFadeInAnimator(wordValidatorCv);

        fadeIn.addListener(new Animator.AnimatorListener() {

            @Override
            public void onAnimationStart(Animator animation) {
                f.addView(wordValidatorLayout);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                validatorOkButton.setOnClickListener(v -> {
                    fadeOut.setStartDelay(500);
                    fadeOut.start();
                });
            }

            @Override
            public void onAnimationCancel(Animator animation) {
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
            }
        });

        fadeOut = Animations.Normal.setCardFadeOutAnimator(wordValidatorCv);
        fadeOut.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                f.removeView(wordValidatorLayout);
//                if (roundCounter < roundLimit && roundCounter < modelMapList.size()) {
//                    createNextGame(modelMapList.get(roundCounter));
//                } else {
//                    moveToReplayFragment();
//                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
            }
        });
    }


//    private void setValidatorCardView(boolean isCorrect) {
//
//        validatorWord.setText(currentWord);
//        validatorWrongWord.setVisibility(View.INVISIBLE);
//        validatorWrongPrompt.setVisibility(View.INVISIBLE);
//
//        if (isCorrect) {
//            validatorBackgroudImage.setImageResource(R.drawable.star);
//            Picasso.get().load(categoryList.get(roundCounter - 1).getImage()).into(validatorImage);
//        } else {
//            validatorBackgroudImage.setImageResource(R.drawable.error);
//            Picasso.get().load(categoryList.get(roundCounter).getImage()).into(validatorImage);
//            validatorWrongWord.setVisibility(View.VISIBLE);
//            validatorWrongPrompt.setVisibility(View.VISIBLE);
//        }
//    }
//
//    private void compareAnswer(String letters, String word) {
//        String validator = "";
//        boolean isCorrect;
//        if (letters.equals(word)) {
//            isCorrect = true;
//            validator = "correct";
//
//            //will run once when correct answer is entered. the method will instantiate, and add all from the current list
//            categoryList.get(roundCounter).setWrongAnswerSet((ArrayList<String>) wrongAnswerList);
//
//            //we will increment once the list is added to correct index
//            roundCounter++;
//
//            //this will remove all, seemed safer than clear, which nulls the object.
//            wrongAnswerList.removeAll(wrongAnswerList);
//
//            pronunciationUtil.textToSpeechAnnouncer(validator, textToSpeech);
//        } else {
//            isCorrect = false;
//            validator = "try again!";
//            validatorWrongWord.setText(letters);
//            correctAnswerSet.add(word);
//            //every wrong answer, until a correct answer will be added here
//            wrongAnswerList.add(letters);
//            categoryList.get(roundCounter).setCorrect(false);
//            pronunciationUtil.textToSpeechAnnouncer("incorrect, please try again", textToSpeech);
//        }
//
//        wordValidator.setText(validator);
//        setValidatorCardView(isCorrect);
//        fadeIn.start();
//
//    }

    public static void requestCameraPermission(Activity activity, int requestCode) {
        ActivityCompat.requestPermissions(
                activity, new String[]{Manifest.permission.CAMERA}, requestCode);
    }

    private void onSingleTap(MotionEvent tap) {
        if (!hasFinishedLoadingModels || !hasFinishedLoadingLetters) {
            // We can't do anything yet.
            return;
        }

        Frame frame = arFragment.getArSceneView().getArFrame();
        if (frame != null) {
            if (!hasPlacedGame && tryPlaceGame(tap, frame)) {
                hasPlacedGame = true;
                f.removeView(tapAnimation);
            }
        }
    }

    private boolean tryPlaceGame(MotionEvent tap, Frame frame) {
        if (tap != null && frame.getCamera().getTrackingState() == TrackingState.TRACKING) {

            mainHit = frame.hitTest(tap).get(0);

            Trackable trackable = mainHit.getTrackable();
            if (trackable instanceof Plane && ((Plane) trackable).isPoseInPolygon(mainHit.getHitPose())) {
                // Create the Anchor.
                if (trackable.getTrackingState() == TrackingState.TRACKING) {
                    mainAnchor = mainHit.createAnchor();
                }

                mainAnchorNode = new AnchorNode(mainAnchor);
                mainAnchorNode.setParent(arFragment.getArSceneView().getScene());
//                    Node gameSystem = createGame(modelMapList.get(0));

                gameManager = new GameManager(getKeysFromModelMapList(modelMapList),this);

                String modelKey = gameManager.getCurrentWordAnswer();

                for (int i = 0; i < modelMapList.size(); i++) {
                    for(Map.Entry<String,ModelRenderable> e : modelMapList.get(i).entrySet()){

                        if(e.getKey().equals(modelKey)){
                            createSingleGame(e.getValue(),e.getKey());
                        }

                    }
                }

//                HashMap<String, ModelRenderable> mainModel = modelMapList.get(0);
//                String s = "";
//                for(Map.Entry<String,ModelRenderable> e: mainModel.entrySet()){
//                    s = e.getKey();
//                }
//                Log.d("arx", "tryPlaceGame: " + modelMapList.get(0).keySet());
//
//                gameManager = new GameManager(getKeysFromModelMapList(modelMapList),this);
//                Log.d("gamemanager", "tryPlaceGame: " + gameManager.getCurrentWordAnswer());
//
//                createSingleGame(mainModel.get(s),s);
                return true;
            }
        }
        return false;
    }



    private void createSingleGame(ModelRenderable mainModel, String name) {

        base = ModelUtil.getGameAnchor(mainModel);
        mainAnchorNode.addChild(base);
        placeLetters(name);

    }


    private void refreshModelResources(){
        letters = "";
        undo.setVisibility(View.INVISIBLE);
        mainAnchorNode.getAnchor().detach();
        mainAnchor = null;
        mainAnchorNode = null;
    }

//    private void createNextGame(Map<String, ModelRenderable> modelMap) {
//        refreshModelResources();
//
//        Trackable trackable = mainHit.getTrackable();
//        if (trackable instanceof Plane && ((Plane) trackable).isPoseInPolygon(mainHit.getHitPose())) {
//            // Create the Anchor.
//            if (trackable.getTrackingState() == TrackingState.TRACKING) {
//                mainAnchor = mainHit.createAnchor();
//            }
//            mainAnchorNode = new AnchorNode(mainAnchor);
//            mainAnchorNode.setParent(arFragment.getArSceneView().getScene());
////                Node gameSystem = createGame(modelMap);
////                mainAnchorNode.addChild(gameSystem);
//        }
//
//        wordContainer.removeAllViews();
//    }

    //TODO - refactor ui logic for updating wordbox as user spells and erases word letters

//    private void addLetterToWordContainer(String letter) {
//        Typeface ballonTF = ResourcesCompat.getFont(getActivity(), R.font.balloon);
//        TextView t = new TextView(getActivity());
//        t.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
//        t.setTypeface(ballonTF);
//        t.setTextColor(getResources().getColor(R.color.colorWhite));
//        t.setTextSize(100);
//        t.setText(letter);
//        t.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
//        wordContainer.addView(t);
//    }

//    public void moveToReplayFragment() {
//        prefs.edit().putStringSet(ResultsFragment.CORRECT_ANSWER_FOR_USER, correctAnswerSet).apply();
//        prefs.edit().putInt(ResultsFragment.TOTALSIZE, roundLimit).apply();
//
//        //Checks to see which words have an empty wrongAnswerSet and changes the boolean pertaining to that Model to true.
//        for (int i = 0; i < roundLimit; i++) {
//            if (categoryList.get(i).getWrongAnswerSet() == null || categoryList.get(i).getWrongAnswerSet().size() == 0) {
//                categoryList.get(i).setCorrect(true);
//            }
//        }
//        listener.moveToReplayFragment(categoryList, true);
//    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        textToSpeech.shutdown();
        pronunciationUtil = null;
//        playBalloonPop.reset();
//        playBalloonPop.release();
    }

//    public String eraseLastLetter(String spelledOutWord) {
//        if (spelledOutWord.length() < 1) {
//            undo.setVisibility(View.INVISIBLE);
//            return spelledOutWord;
//        } else {
//            letters = letters.substring(0, spelledOutWord.length() - 1);
//            wordContainer.removeViewAt(spelledOutWord.length() - 1);
//            return spelledOutWord.substring(spelledOutWord.length() - 1);
//        }
//    }
//
//    public void recreateErasedLetter(String letterToRecreate) {
////        if (!letterToRecreate.equals("")) {
////            createLetter(letterToRecreate, currentWord, base, letterMap.get(letterToRecreate));
////        }
//    }
//
//    private Node.OnTapListener getNodeOnTapListener(AnchorNode letter) {
//
//        return (hitTestResult, motionEvent) -> letter.getAnchor().detach();
//    }

    private void placeLetters(String word) {
        for (int i = 0; i < word.length(); i++) {
            placeSingleLetter(
                    Character.toString(word.charAt(i)));
        }
    }

    private void placeSingleLetter(String letter) {
        AnchorNode letterAnchorNode = ModelUtil.getLetter(base, letterMap.get(letter), arFragment);
        letterAnchorNode.getChildren().get(0).setOnTapListener(getNodeOnTapListener(letter, letterAnchorNode));

        Log.d("arx", "tryPlaceGame: " + letterMap.get(letter));
        connectAnchorToBase(letterAnchorNode);
    }

    private void connectAnchorToBase(AnchorNode anchorNode) {
        arFragment.getArSceneView().getScene().addChild(anchorNode);
        base.addChild(anchorNode);
    }


    public String eraseLastLetter() {
//        if (spelledOutWord.length() < 1) {
//            undo.setVisibility(View.INVISIBLE);
//            return spelledOutWord;
//        } else {
//            letters = letters.substring(0, spelledOutWord.length() - 1);
//            wordContainer.removeViewAt(spelledOutWord.length() - 1);
//            return spelledOutWord.substring(spelledOutWord.length() - 1);
//        }
        return gameManager.subtractLetterFromAttempt();
    }

    private void recreateErasedLetter(String letterToRecreate) {
        if (!letterToRecreate.equals("")) {
            placeSingleLetter(letterToRecreate);
        }
    }

    private void undo(){
        String erasedLetter = eraseLastLetter();
        recreateErasedLetter(erasedLetter);
    }

    private Node.OnTapListener getNodeOnTapListener(String letterString, AnchorNode letterAnchorNode) {

        return (hitTestResult, motionEvent) -> {
            gameManager.addTappedLetterToCurrentWordAttempt(letterString.toLowerCase());
            Objects.requireNonNull(letterAnchorNode.getAnchor()).detach();
        };
    }

    @Override
    public void startNextGame(String modelKey) {
        Log.d("startnextgame:arhostfragment", "startNextGame: condition hit");
        refreshModelResources();

        Trackable trackable = mainHit.getTrackable();
        if (trackable instanceof Plane && ((Plane) trackable).isPoseInPolygon(mainHit.getHitPose())) {
            // Create the Anchor.
            if (trackable.getTrackingState() == TrackingState.TRACKING) {
                mainAnchor = mainHit.createAnchor();
            }

            mainAnchorNode = new AnchorNode(mainAnchor);
            mainAnchorNode.setParent(arFragment.getArSceneView().getScene());

            for (int i = 0; i < modelMapList.size(); i++) {
                for(Map.Entry<String,ModelRenderable> e : modelMapList.get(i).entrySet()){
                    if(e.getKey().equals(modelKey)){
                        createSingleGame(e.getValue(),e.getKey());
                    }
                }
            }

            wordContainer.removeAllViews();
        }
    }

}