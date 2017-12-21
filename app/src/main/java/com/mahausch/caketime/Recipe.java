package com.mahausch.caketime;


import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class Recipe implements Parcelable {

    private ArrayList<Ingredient> mIngredients;
    private ArrayList<RecipeStep> mRecipeSteps;

    public Recipe(ArrayList<Ingredient> ingredients, ArrayList<RecipeStep> recipeSteps) {
        mIngredients = ingredients;
        mRecipeSteps = recipeSteps;
    }

    public ArrayList<Ingredient> getIngredients() {
        return mIngredients;
    }

    public ArrayList<RecipeStep> getRecipeSteps() {
        return mRecipeSteps;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeTypedList(mIngredients);
        parcel.writeTypedList(mRecipeSteps);
    }

    public void readFromParcel(Parcel in) {
        in.readTypedList(mIngredients, Ingredient.CREATOR);
        in.readTypedList(mRecipeSteps, RecipeStep.CREATOR);
    }

    public static final Parcelable.Creator<Recipe> CREATOR = new Creator<Recipe>() {

        @Override
        public Recipe createFromParcel(Parcel source) {
            return new Recipe(source);
        }

        @Override
        public Recipe[] newArray(int size) {
            return new Recipe[size];
        }
    };
}
