package com.mahausch.caketime;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipeDetailFragment extends Fragment implements StepAdapter.StepAdapterOnClickHandler {

    @BindView(R.id.recycler_view_detail)
    RecyclerView recycler;

    OnStepClickListener mCallback;

    public RecipeDetailFragment() {
        // Required empty public constructor
    }

    public interface OnStepClickListener {
        void onStepSelected(RecipeStep step);

        void onIngredientsSelected(ArrayList<Ingredient> ingredients);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            mCallback = (OnStepClickListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement OnStepClickListener");
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.fragment_recipe_detail, container, false);
        ButterKnife.bind(this, rootView);

        Bundle bundle = getArguments();
        Recipe recipe = bundle.getParcelable("recipe");
        ArrayList<RecipeStep> steps = recipe.getRecipeSteps();
        ArrayList<Ingredient> ingredients = recipe.getIngredients();

        recycler.setLayoutManager(new LinearLayoutManager(getContext()));
        StepAdapter adapter = new StepAdapter(this);
        recycler.setAdapter(adapter);
        adapter.setStepsList(steps, ingredients);

        return rootView;
    }

    @Override
    public void onClickIngredient(ArrayList<Ingredient> ingredients) {
        mCallback.onIngredientsSelected(ingredients);
    }

    @Override
    public void onClickStep(RecipeStep step) {
        mCallback.onStepSelected(step);
    }
}
