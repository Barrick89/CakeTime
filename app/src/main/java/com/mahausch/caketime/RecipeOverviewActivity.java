package com.mahausch.caketime;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.mahausch.caketime.Utils.JsonUtils;

import java.util.ArrayList;

public class RecipeOverviewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_overview);

        ArrayList<Recipe> list = JsonUtils.getRecipesFromJson(this);
    }
}
