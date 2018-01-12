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
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

//Class to create java objects from JSON file
public class JsonUtils {

    private static final String RECIPE_URL =
            "https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/baking.json";

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

        URL url = new URL(RECIPE_URL);
        HttpURLConnection urlConnection = null;
        String jsonData = "";

        try {
            urlConnection = (HttpURLConnection) url.openConnection();

            InputStream input = urlConnection.getInputStream();

            Scanner scanner = new Scanner(input);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                jsonData = scanner.next();
            } else {
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            urlConnection.disconnect();
        }
        return jsonData;
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
