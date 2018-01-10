package com.mahausch.caketime.fragments;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mahausch.caketime.Ingredient;
import com.mahausch.caketime.R;
import com.mahausch.caketime.Recipe;
import com.mahausch.caketime.RecipeStep;
import com.mahausch.caketime.adapters.StepAdapter;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

//Fragment that creates the list of Recipe steps
public class RecipeDetailFragment extends Fragment implements StepAdapter.StepAdapterOnClickHandler {

    @BindView(R.id.recycler_view_detail)
    RecyclerView recycler;

    LinearLayoutManager mManager;
    OnStepClickListener mCallback;
    static Parcelable mListState;

    public RecipeDetailFragment() {
    }

    public interface OnStepClickListener {
        void onStepSelected(RecipeStep step);

        void onIngredientsSelected(ArrayList<Ingredient> ingredients);
    }

    //Check of Activity implements the callback interface
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

        mManager = new LinearLayoutManager(getContext());
        recycler.setLayoutManager(mManager);
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


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        mListState = mManager.onSaveInstanceState();
        outState.putParcelable("listState", mListState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null)
            mListState = savedInstanceState.getParcelable("listState");
    }
}
