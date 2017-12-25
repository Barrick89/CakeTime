package com.mahausch.caketime;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.mahausch.caketime.Utils.JsonUtils;
import com.mahausch.caketime.Utils.RecipeAdapter;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipeOverviewActivity extends AppCompatActivity implements RecipeAdapter.RecipeAdapterOnClickHandler {

    @BindView(R.id.recycler_view)
    RecyclerView recycler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_overview);

        ButterKnife.bind(this);

        ArrayList<Recipe> list = JsonUtils.getRecipesFromJson(this);

        LinearLayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recycler.setLayoutManager(manager);

        RecipeAdapter adapter = new RecipeAdapter(this);
        adapter.setRecipesList(list);
        recycler.setAdapter(adapter);

    }

    @Override
    public void onClick(Recipe recipeData) {

        Intent intent = new Intent(this, RecipeDetailActivity.class);
        intent.putExtra("recipe", recipeData);
        startActivity(intent);
    }
}
