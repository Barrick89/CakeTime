package com.mahausch.caketime.Utils;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mahausch.caketime.R;
import com.mahausch.caketime.Recipe;

import java.util.ArrayList;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecipeHolder> {

    private ArrayList<Recipe> mRecipesList;
    private final RecipeAdapterOnClickHandler mOnClickHandler;

    public RecipeAdapter(RecipeAdapterOnClickHandler onClickHandler) {
        mOnClickHandler = onClickHandler;
    }

    @Override
    public RecipeAdapter.RecipeHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        int layout = R.layout.recipe_list_item;

        View view = inflater.inflate(layout, parent, false);
        return new RecipeHolder(view);
    }

    @Override
    public void onBindViewHolder(RecipeAdapter.RecipeHolder holder, int position) {

        Recipe recipe = mRecipesList.get(position);
        String name = recipe.getName();
        holder.mName.setText(name);
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

        public final TextView mName;

        public RecipeHolder(View itemView) {
            super(itemView);
            mName = (TextView) itemView.findViewById(R.id.name);
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
