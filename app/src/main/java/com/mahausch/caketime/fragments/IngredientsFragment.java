package com.mahausch.caketime.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.mahausch.caketime.Ingredient;
import com.mahausch.caketime.R;
import com.mahausch.caketime.activities.RecipeDetailActivity;
import com.mahausch.caketime.adapters.IngredientsAdapter;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;


public class IngredientsFragment extends Fragment {

    @BindView(R.id.recycler_view_ingredients)
    RecyclerView recycler;

    @BindView(R.id.ingredient_right_arrow)
    ImageView arrow;

    public IngredientsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_ingredients, container, false);
        ButterKnife.bind(this, rootView);

        if (!RecipeDetailActivity.mTwoPane) {
            arrow.setVisibility(View.VISIBLE);
        }

        Bundle bundle = getArguments();
        ArrayList<Ingredient> ingredients = bundle.getParcelableArrayList("ingredients");

        recycler.setLayoutManager(new LinearLayoutManager(getContext()));
        IngredientsAdapter adapter = new IngredientsAdapter();
        recycler.setAdapter(adapter);
        adapter.setIngredientsList(ingredients);

        return rootView;
    }

}
