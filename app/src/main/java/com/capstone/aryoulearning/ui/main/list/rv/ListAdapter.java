package com.capstone.aryoulearning.ui.main.list.rv;

import android.content.Context;
import android.os.Vibrator;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.capstone.aryoulearning.R;
import com.capstone.aryoulearning.controller.NavListenerX;
import com.capstone.aryoulearning.db.model.Category;
import com.capstone.aryoulearning.model.Model;
import com.capstone.aryoulearning.ui.main.MainActivity;
import com.capstone.aryoulearning.ui.main.MainViewModel;
import com.capstone.aryoulearning.ui.main.controller.NavListener;
import com.capstone.aryoulearning.viewmodel.ViewModelProviderFactory;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.CategoryViewHolder> {
//    private List<List<Model>> categoryList;
//    private List<String> categoryName;
//    private List<String> categoryImages;
    private List<Category> categories = new ArrayList<>();
    private NavListener listener;

//    @Inject
//    ViewModelProviderFactory viewModelProviderFactory;
//    private MainViewModel mainViewModel;


//    public ListAdapter() {
//        this.categoryList = categoryList;
//        this.categoryName = categoryName;
//        this.categoryImages = categoryImages;
//    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).
                inflate(R.layout.category_item, viewGroup, false);

        Context context = viewGroup.getContext();
        if (context instanceof NavListener) {
            listener = (NavListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + "must implement FragmentInteractionListener");
        }

        return new CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        holder.onBind(categories.get(position), listener);
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    public void setLists(
            List<Category> categories
    ){


        this.categories = categories;
//        this.categoryList = categoryList;
//        this.categoryName = categoryName;
//        this.categoryImages = categoryImages;

        notifyDataSetChanged();
    }

    class CategoryViewHolder extends RecyclerView.ViewHolder {
        private CardView categoryCard;
        private TextView categoryName;
        private ImageView categoryImage;

        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            categoryCard = itemView.findViewById(R.id.category_card);
            categoryName = itemView.findViewById(R.id.category_name);
            categoryImage = itemView.findViewById(R.id.category_image);
        }

        public void onBind(final Category category, final NavListener listener) {
            categoryName.setText(category.getName());
            Picasso.get()
                    .load(category.getImage())
                    .into(categoryImage);

            Log.d("TAG", category.getImage());
            categoryCard.setOnClickListener(v -> {
                Log.d("list adapter", "onBind: " + category.getName());

                listener.moveToHintFragment(category.getName());
                makeVibration();
            });
        }

        private void makeVibration() {
            Vibrator categoryVibrate = (Vibrator) itemView.getContext().getSystemService(Context.VIBRATOR_SERVICE);
            categoryVibrate.vibrate(100);
        }
    }
}
