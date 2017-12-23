package com.mahausch.caketime;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.mahausch.caketime.Utils.JsonUtils;
import com.mahausch.caketime.Utils.RecipeAdapter;

import java.util.ArrayList;

public class RecipeOverviewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_overview);

        ArrayList<Recipe> list = JsonUtils.getRecipesFromJson(this);
        RecyclerView recycler = (RecyclerView) findViewById(R.id.recycler_view);


        LinearLayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recycler.setLayoutManager(manager);

        RecipeAdapter adapter = new RecipeAdapter();
        adapter.setRecipesList(list);
        recycler.setAdapter(adapter);

    }
}
