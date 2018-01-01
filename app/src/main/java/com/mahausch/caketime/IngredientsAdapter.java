package com.mahausch.caketime;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class IngredientsAdapter extends RecyclerView.Adapter<IngredientsAdapter.IngredientsHolder> {

    private ArrayList<Ingredient> mIngredients;
    private Context mContext;

    @Override
    public IngredientsHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(mContext);
        int layout = R.layout.ingredients_list_item;

        View view = inflater.inflate(layout, parent, false);
        return new IngredientsHolder(view);
    }

    @Override
    public void onBindViewHolder(IngredientsHolder holder, int position) {
        Ingredient ingredient = mIngredients.get(position);

        String ingredientName = ingredient.getIngredient();
        int quantity = ingredient.getQuantity();
        String measure = ingredient.getMeasure();

        holder.ingredient.setText("- " + ingredientName + " (" + quantity + " " + measure + ")");
    }

    @Override
    public int getItemCount() {
        if (mIngredients != null) {
            return mIngredients.size();
        } else {
            return 0;
        }
    }

    public void setIngredientsList(ArrayList<Ingredient> ingredients) {
        mIngredients = ingredients;
        notifyDataSetChanged();
    }

    public class IngredientsHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.ingredient)
        TextView ingredient;

        public IngredientsHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
