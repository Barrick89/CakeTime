package com.mahausch.caketime;


import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;

public class RecipeDetailActivity extends AppCompatActivity implements RecipeDetailFragment.OnStepClickListener {

    private Recipe mRecipe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);

        Bundle intentBundle = getIntent().getExtras();
        mRecipe = intentBundle.getParcelable("recipe");

        Bundle bundle = new Bundle();
        bundle.putParcelable("recipe", mRecipe);

        RecipeDetailFragment detailFragment = new RecipeDetailFragment();
        detailFragment.setArguments(bundle);

        FragmentManager manager = getSupportFragmentManager();
        manager.beginTransaction().add(R.id.recipe_detail_fragment, detailFragment).commit();


    }

    @Override
    public void onStepSelected(RecipeStep step) {

    }

    @Override
    public void onIngredientsSelected(ArrayList<Ingredient> ingredients) {

        IngredientsFragment ingredientsFragment = new IngredientsFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList("ingredients", ingredients);
        ingredientsFragment.setArguments(bundle);

        FragmentManager manager = getSupportFragmentManager();
        manager.beginTransaction().replace(R.id.recipe_detail_fragment, ingredientsFragment)
                .addToBackStack(null)
                .commit();
    }
}
