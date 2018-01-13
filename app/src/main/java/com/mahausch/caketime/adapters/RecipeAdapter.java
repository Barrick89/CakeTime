package com.mahausch.caketime.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mahausch.caketime.R;
import com.mahausch.caketime.Recipe;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecipeHolder> {

    private ArrayList<Recipe> mRecipesList;
    private final RecipeAdapterOnClickHandler mOnClickHandler;
    private Context mContext;

    public RecipeAdapter(RecipeAdapterOnClickHandler onClickHandler) {
        mOnClickHandler = onClickHandler;
    }

    @Override
    public RecipeAdapter.RecipeHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        mContext = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(mContext);
        int layout = R.layout.recipe_list_item;

        View view = inflater.inflate(layout, parent, false);
        return new RecipeHolder(view);
    }

    @Override
    public void onBindViewHolder(RecipeAdapter.RecipeHolder holder, int position) {

        Recipe recipe = mRecipesList.get(position);
        String name = recipe.getName();
        holder.mName.setText(name);
        String image = recipe.getImage();

        if (image != null && !image.isEmpty()) {
            Glide.with(mContext).load(image).into(holder.mImage);
        } else {
            switch (position) {
                case 0:
                    Glide.with(mContext).load(R.drawable.placeholder_1).into(holder.mImage);
                    break;
                case 1:
                    Glide.with(mContext).load(R.drawable.placeholder_2).into(holder.mImage);
                    break;
                case 2:
                    Glide.with(mContext).load(R.drawable.placeholder_3).into(holder.mImage);
                    break;
                case 3:
                    Glide.with(mContext).load(R.drawable.placeholder_4).into(holder.mImage);
            }
        }
    }

    @Override
    public int getItemCount() {

        if (mRecipesList != null) {
            return mRecipesList.size();
        } else {
            return 0;
        }
    }

    public void setRecipesList(ArrayList<Recipe> recipesList) {
        mRecipesList = recipesList;
        notifyDataSetChanged();
    }

    public interface RecipeAdapterOnClickHandler {
        public void onClick(Recipe recipeData);
    }

    public class RecipeHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.name)
        TextView mName;
        @BindView(R.id.image)
        ImageView mImage;

        public RecipeHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            Recipe recipe = mRecipesList.get(position);
            mOnClickHandler.onClick(recipe);
        }
    }
}
