package com.capstone.aryoulearning.ui.main.hint.rv;

import android.graphics.Color;
import android.os.CountDownTimer;
import android.speech.tts.TextToSpeech;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.capstone.aryoulearning.R;
import com.capstone.aryoulearning.animation.Animations;
import com.capstone.aryoulearning.db.model.ModelInfo;
import com.capstone.aryoulearning.util.audio.PronunciationUtil;
import com.capstone.aryoulearning.model.Model;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class HintAdapter extends RecyclerView.Adapter<HintAdapter.HintViewHolder> {

    private List<Model> modelList = new ArrayList<>();

    private final PronunciationUtil pronunciationUtil;

    @Inject
    public HintAdapter(PronunciationUtil pronunciationUtil) {
        this.pronunciationUtil = pronunciationUtil;
    }

    @NonNull
    @Override
    public HintViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new HintViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.hint_item_view, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull HintViewHolder hintViewHolder, int i) {
        hintViewHolder.onBind(modelList.get(i));
    }

    @Override
    public int getItemCount() {
        return modelList.size();
    }

    class HintViewHolder extends RecyclerView.ViewHolder {
        public HintViewHolder(@NonNull View itemView) {
            super(itemView);
        }

        public void onBind(Model model) {
            ImageView imageView = itemView.findViewById(R.id.hint_fragment_image_view);
            TextView textView = itemView.findViewById(R.id.hint_fragment_textview);

            textView.setTextColor(Color.DKGRAY);

            Picasso.get().load(model.getImage()).into(imageView);

            textView.setText(model.getName());

            itemView.setOnClickListener(v -> {

                pronunciationUtil.textToSpeechAnnouncer(model.getName(), pronunciationUtil.textToSpeech );
                itemView.startAnimation(Animations.Normal.getVibrator(itemView));
                textView.setTextColor(Color.LTGRAY);
                CountDownTimer timer = new CountDownTimer(1000, 1000) {
                    @Override
                    public void onTick(long millisUntilFinished) {

                    }

                    @Override
                    public void onFinish() {
                        textView.setTextColor(Color.DKGRAY);
                        itemView.clearAnimation();
                    }
                };
                timer.start();

            });
        }
    }

    public void setList(List<Model> modelList) {

        this.modelList = modelList;

        notifyDataSetChanged();
    }
}

