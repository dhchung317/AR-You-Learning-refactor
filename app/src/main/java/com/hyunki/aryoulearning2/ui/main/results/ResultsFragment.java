package com.hyunki.aryoulearning2.ui.main.results;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.speech.tts.TextToSpeech;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.hyunki.aryoulearning2.BaseApplication;
import com.hyunki.aryoulearning2.R;
import com.hyunki.aryoulearning2.ui.main.MainViewModel;
import com.hyunki.aryoulearning2.ui.main.State;
import com.hyunki.aryoulearning2.ui.main.ar.util.CurrentWord;
import com.hyunki.aryoulearning2.ui.main.results.rv.ResultsAdapter;
import com.hyunki.aryoulearning2.util.audio.PronunciationUtil;
import com.hyunki.aryoulearning2.model.Model;
import com.hyunki.aryoulearning2.viewmodel.ViewModelProviderFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.inject.Inject;

//TODO- refactor resultsfragment
public class ResultsFragment extends Fragment {
    private static final int REQUEST_CODE = 1;
    private RatingBar rainbowRatingBar;
    private TextView categoryTextView;
    private Map<String,Model> modelMap = new HashMap<>();
    FloatingActionButton shareFAB;
    FloatingActionButton backFAB;
    private RecyclerView resultRV;
    private PronunciationUtil pronunciationUtil;
    private TextToSpeech textToSpeech;
    private MainViewModel viewModel;
    private ProgressBar progressBar;

    private ViewModelProviderFactory viewModelProviderFactory;

    @Inject
    public ResultsFragment(ViewModelProviderFactory viewModelProviderFactory) {
        this.viewModelProviderFactory = viewModelProviderFactory;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        ((BaseApplication) getActivity().getApplication()).getAppComponent().inject(this);
        super.onAttach(context);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        pronunciationUtil = new PronunciationUtil();
//        textToSpeech = pronunciationUtil.getTTS(getContext());
    }

    private void initializeViews(@NonNull final View view) {
        rainbowRatingBar = view.findViewById(R.id.rainbow_correctword_ratingbar);
        shareFAB = view.findViewById(R.id.share_info);
        backFAB = view.findViewById(R.id.back_btn);
        resultRV = view.findViewById(R.id.result_recyclerview);
        categoryTextView = view.findViewById(R.id.results_category);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_results, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = ViewModelProviders.of(getActivity(),viewModelProviderFactory).get(MainViewModel.class);
        progressBar = getActivity().findViewById(R.id.progress_bar);
        initializeViews(view);
        setViews();
        renderModelList(viewModel.getModelLiveData().getValue());
    }

    public void setViews(){
        displayRatingBarAttempts();
//        categoryTextView.setText(MainActivityX.currentCategory);
        shareFAB.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.share_button_color)));
        backFABClick();
        shareFABClick();
    }

    private void setResultRV() {
        resultRV.setAdapter(new ResultsAdapter(viewModel.getWordHistory(), modelMap, pronunciationUtil, textToSpeech));
        resultRV.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));

    }

    public void shareFABClick() {
        shareFAB.setOnClickListener(v -> {
            v = Objects.requireNonNull(ResultsFragment.this.getActivity()).getWindow().getDecorView().getRootView();
            if (ContextCompat.checkSelfPermission(v.getContext(), Manifest.permission.READ_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(v.getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(ResultsFragment.this.getActivity(),
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE);
                ResultsFragment.this.takeScreenshotAndShare(v);
            } else {
                ResultsFragment.this.takeScreenshotAndShare(v);
            }
        });
    }

    public void backFABClick(){
        backFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Objects.requireNonNull(getActivity()).onBackPressed();
            }
        });
    }

    public void allowOnFileUriExposed() {
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_CODE) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                return;
            }
        }
    }

    public void takeScreenshotAndShare(final View view) {
        allowOnFileUriExposed();
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache(true);
        Bitmap b = Bitmap.createBitmap(view.getDrawingCache());
        view.setDrawingCacheEnabled(true);
        saveBitmap(b);
    }

    private void shareIt(final File imagePath) {
        Uri uri = Uri.fromFile(imagePath);
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.setType("image/*");

        intent.putExtra(android.content.Intent.EXTRA_SUBJECT, "");
        intent.putExtra(android.content.Intent.EXTRA_TEXT, "");
        intent.putExtra(Intent.EXTRA_STREAM, uri);
        try {
            startActivity(Intent.createChooser(intent, "Share Screenshot"));
        } catch (ActivityNotFoundException e) {
            Toast.makeText(getContext(), "No App Available", Toast.LENGTH_SHORT).show();
        }
    }

    public void saveBitmap(final Bitmap bitmap) {
        Date now = new Date();
        android.text.format.DateFormat.format("yyyy-MM-dd_hh:mm:ss", now);

        try {
            String mPath = Environment.getExternalStorageDirectory().toString() + "/" + now + ".jpg";

            File imageFile = new File(mPath);

            FileOutputStream outputStream = new FileOutputStream(imageFile);
            int quality = 100;
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream);
            outputStream.flush();
            outputStream.close();

            shareIt(imageFile);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    private void displayRatingBarAttempts() {

        rainbowRatingBar.setNumStars(viewModel.getWordHistory().size());
        rainbowRatingBar.setStepSize(1);
        rainbowRatingBar.setRating(
                getCorrectAnswerCount(viewModel.getWordHistory())
        );
        rainbowRatingBar.setIsIndicator(true);
    }

    private void renderModelList(State state){
        Log.d("results", "renderModelList: " + state.getClass());
        if (state == State.Loading.INSTANCE) {
            progressBar.bringToFront();
            showProgressBar(true);

        } else if (state == State.Error.INSTANCE) {
            showProgressBar(false);

        } else if (state.getClass() == State.Success.OnModelsLoaded.class) {
            showProgressBar(false);
            State.Success.OnModelsLoaded s = (State.Success.OnModelsLoaded) state;
            for (int i = 0; i < s.getModels().size(); i++) {
                modelMap.put(s.getModels().get(i).getName(),s.getModels().get(i));
            }
            Log.d("resultsAdapter", "renderModelList: " + s.getModels().size());
            setResultRV();
        }
    }

    private void showProgressBar(boolean isVisible) {
        if (isVisible) {
            progressBar.setVisibility(View.VISIBLE);
        } else {
            progressBar.setVisibility(View.GONE);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        textToSpeech.shutdown();
        pronunciationUtil = null;
    }

    public int getCorrectAnswerCount(List<CurrentWord> wordHistory){
        int count = 0;

        for (int i = 0; i < wordHistory.size(); i++) {
            if(wordHistory.get(i).getAttempts().size() < 1){
                count++;
            }
        }
        return count;
    }
}
