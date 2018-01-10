package com.mahausch.caketime.activities;


import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.mahausch.caketime.Ingredient;
import com.mahausch.caketime.R;
import com.mahausch.caketime.Recipe;
import com.mahausch.caketime.RecipeStep;
import com.mahausch.caketime.fragments.IngredientsFragment;
import com.mahausch.caketime.fragments.RecipeDetailFragment;
import com.mahausch.caketime.fragments.StepFragment;

import java.util.ArrayList;

public class RecipeDetailActivity extends AppCompatActivity implements RecipeDetailFragment.OnStepClickListener, StepFragment.OnArrowClickListener {

    private static Recipe mRecipe;
    public static int recipeStepCount;
    public static boolean mTwoPane = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Bundle intentBundle = getIntent().getExtras();
        mRecipe = intentBundle.getParcelable("recipe");

        setTitle(mRecipe.getName());

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);

        if (findViewById(R.id.recipe_step_fragment) == null) {
            mTwoPane = false;
        }

        if (savedInstanceState == null) {

            recipeStepCount = mRecipe.getRecipeSteps().size();

            Bundle bundle = new Bundle();
            bundle.putParcelable("recipe", mRecipe);

            RecipeDetailFragment detailFragment = new RecipeDetailFragment();
            detailFragment.setArguments(bundle);
            FragmentManager manager = getSupportFragmentManager();

            manager.beginTransaction().add(R.id.recipe_detail_fragment, detailFragment).commit();

            if (mTwoPane) {
                IngredientsFragment ingredientsFragment = new IngredientsFragment();
                bundle = new Bundle();
                bundle.putParcelableArrayList("ingredients", mRecipe.getIngredients());
                ingredientsFragment.setArguments(bundle);
                manager.beginTransaction().add(R.id.recipe_step_fragment, ingredientsFragment).commit();
            }
        }

    }

    @Override
    public void onStepSelected(RecipeStep step) {

        StepFragment stepFragment = new StepFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable("step", step);
        stepFragment.setArguments(bundle);

        FragmentManager manager = getSupportFragmentManager();

        int view;
        if (mTwoPane) {
            view = R.id.recipe_step_fragment;
        } else {
            view = R.id.recipe_detail_fragment;
        }

        manager.beginTransaction().replace(view, stepFragment)
                .addToBackStack(null)
                .commit();

    }

    @Override
    public void onIngredientsSelected(ArrayList<Ingredient> ingredients) {

        IngredientsFragment ingredientsFragment = new IngredientsFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList("ingredients", ingredients);
        ingredientsFragment.setArguments(bundle);

        FragmentManager manager = getSupportFragmentManager();

        int view;
        if (mTwoPane) {
            view = R.id.recipe_step_fragment;
        } else {
            view = R.id.recipe_detail_fragment;
        }

        manager.beginTransaction().replace(view, ingredientsFragment)
                .addToBackStack(null)
                .commit();
    }

    public void switchToFirstStep(View view) {
        RecipeStep step = mRecipe.getRecipeSteps().get(0);
        onStepSelected(step);
    }

    @Override
    public void onPreviousSelected(int stepId) {
        if (stepId == 0) {
            onIngredientsSelected(mRecipe.getIngredients());
        } else {
            RecipeStep step = mRecipe.getRecipeSteps().get(stepId - 1);
            onStepSelected(step);
        }
    }

    @Override
    public void onNextSelected(int stepId) {
        RecipeStep step = mRecipe.getRecipeSteps().get(stepId + 1);
        onStepSelected(step);
    }
}
