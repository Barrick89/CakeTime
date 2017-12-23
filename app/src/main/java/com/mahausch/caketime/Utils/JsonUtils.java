package com.mahausch.caketime.Utils;


import android.content.Context;

import com.mahausch.caketime.Ingredient;
import com.mahausch.caketime.Recipe;
import com.mahausch.caketime.RecipeStep;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class JsonUtils {


    public static ArrayList<Recipe> getRecipesFromJson(Context context) {
        ArrayList<Recipe> recipeList = new ArrayList<>();
        try {
            String jsonData = readJSONFile(context);
            recipeList = createRecipesFromJsonString(jsonData);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return recipeList;
    }

    private static String readJSONFile(Context context) throws IOException {
        String json = null;
        try {
            InputStream is = context.getAssets().open("baking.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }

    private static ArrayList<Recipe> createRecipesFromJsonString(String jsonData) {

        ArrayList<Recipe> recipeList = new ArrayList<>();

        try {
            JSONArray recipeArray = new JSONArray(jsonData);

            for (int i = 0; i < recipeArray.length(); i++) {
                JSONObject jsonRecipe = recipeArray.getJSONObject(i);

                int id = jsonRecipe.getInt("id");

                String name = jsonRecipe.getString("name");

                JSONArray ingredients = jsonRecipe.getJSONArray("ingredients");
                ArrayList<Ingredient> ingredientsList = getIngredientsList(ingredients);

                JSONArray steps = jsonRecipe.getJSONArray("steps");
                ArrayList<RecipeStep> stepsList = getStepsList(steps);

                Recipe recipe = new Recipe(id, name, ingredientsList, stepsList);
                recipeList.add(recipe);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return recipeList;
    }


}
