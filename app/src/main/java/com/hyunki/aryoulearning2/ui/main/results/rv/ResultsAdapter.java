package com.hyunki.aryoulearning2.ui.main.results.rv;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.speech.tts.TextToSpeech;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.hyunki.aryoulearning2.R;
import com.hyunki.aryoulearning2.model.Model;
import com.hyunki.aryoulearning2.ui.main.ar.util.CurrentWord;
import com.hyunki.aryoulearning2.util.audio.PronunciationUtil;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class ResultsAdapter extends RecyclerView.Adapter<ResultsAdapter.ResultsViewHolder> {
    private Map<String,Model> modelMap;
    private List<CurrentWord> wordHistory;
//    private int listSize;
    private PronunciationUtil pronunUtil;
    private TextToSpeech TTS;


    public ResultsAdapter(List<CurrentWord> wordHistory, Map<String,Model> modelMap, PronunciationUtil pronunciationUtil, TextToSpeech TTS) {
        this.wordHistory = wordHistory;
        this.modelMap = modelMap;
        this.pronunUtil = pronunciationUtil;
        this.TTS = TTS;
//        this.listSize = listSize;
    }

    @NonNull
    @Override
    public ResultsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int position) {
        return new ResultsViewHolder(LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.resultmodel_item,
                        viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ResultsViewHolder resultsViewHolder, int position) {
        resultsViewHolder.onBind(wordHistory.get(position),modelMap.get(wordHistory.get(position).getAnswer()), pronunUtil, TTS);
    }


    @Override
    public int getItemCount() {
        return wordHistory.size();
    }

    class ResultsViewHolder extends RecyclerView.ViewHolder {
        private TextView modelTextView;
        private ImageView modelImageview;
        private TextView modelAnswer;
        private ImageView resultImage;
        private TextView promptText;

        ResultsViewHolder(@NonNull View itemView) {
            super(itemView);
            modelTextView = itemView.findViewById(R.id.correctmodel_name);
            promptText = itemView.findViewById(R.id.result_prompt_textView);
            modelImageview = itemView.findViewById(R.id.correctmodel_image);
            modelAnswer = itemView.findViewById(R.id.correctmodel_answer);
            resultImage = itemView.findViewById(R.id.result_imageView);
        }

        @SuppressLint("ResourceAsColor")
        void onBind(final CurrentWord currentWord, final Model model, final PronunciationUtil pronunUtil, final TextToSpeech TTS) {
            String correct = "Correct";

            String wrong = "";
            for (String s : currentWord.getAttempts()) {
                wrong += s + ", ";
            }

            String name = model.getName().toUpperCase().charAt(0) + model.getName().toLowerCase().substring(1);
            CardView cardView = itemView.findViewById(R.id.cardView4);

            modelTextView.setText(name);

            Picasso.get().load(model.getImage()).into(modelImageview);

            if (currentWord.getAttempts().isEmpty()) {
                resultImage.setImageResource(R.drawable.star);
                modelAnswer.setText(correct);
                promptText.setVisibility(View.INVISIBLE);
            } else {
                cardView.setCardBackgroundColor(Color.parseColor("#D81B60"));
                resultImage.setImageResource(R.drawable.error);
                modelAnswer.setText(wrong.substring(0, wrong.length() - 2));
            }
            cardView.setOnClickListener(v -> pronunUtil.textToSpeechAnnouncer(name, TTS));
        }
    }
}
