package com.mahausch.caketime.utils;


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

    private static ArrayList<Ingredient> getIngredientsList(JSONArray ingredients) throws JSONException {

        ArrayList<Ingredient> ingredientsList = new ArrayList<>();

        for (int i = 0; i < ingredients.length(); i++) {
            JSONObject ingredientObject = ingredients.getJSONObject(i);

            int quantity = ingredientObject.getInt("quantity");
            String measure = ingredientObject.getString("measure");
            String ingredient = ingredientObject.getString("ingredient");

            Ingredient ing = new Ingredient(quantity, measure, ingredient);
            ingredientsList.add(ing);
        }
        return ingredientsList;
    }

    private static ArrayList<RecipeStep> getStepsList(JSONArray steps) throws JSONException {

        ArrayList<RecipeStep> stepsList = new ArrayList<>();

        for (int i = 0; i < steps.length(); i++) {
            JSONObject stepObject = steps.getJSONObject(i);

            int id = stepObject.getInt("id");
            String shortDescription = stepObject.getString("shortDescription");
            String description = stepObject.getString("description");
            String videoURL = stepObject.getString("videoURL");
            String thumbnailURL = stepObject.getString("thumbnailURL");

            RecipeStep step = new RecipeStep(id, shortDescription,
                    description, videoURL, thumbnailURL);
            stepsList.add(step);
        }
        return stepsList;
    }


}
