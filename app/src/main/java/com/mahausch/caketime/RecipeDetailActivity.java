package com.mahausch.caketime;


import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

public class RecipeDetailActivity extends AppCompatActivity {

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
}
