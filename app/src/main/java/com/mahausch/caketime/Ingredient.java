package com.mahausch.caketime;


import android.os.Parcel;
import android.os.Parcelable;

public class Ingredient implements Parcelable {

    private int mQuantity;
    private String mMeasure;
    private String mIngredient;

    public Ingredient(int quantity, String measure, String ingredient) {
        mQuantity = quantity;
        mMeasure = measure;
        mIngredient = ingredient;
    }

    public Ingredient(Parcel in) {
        readFromParcel(in);
    }

    public int getQuantity() {
        return mQuantity;
    }

    public String getMeasure() {
        return mMeasure;
    }

    public String getIngredient() {
        return mIngredient;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeInt(mQuantity);
        parcel.writeString(mMeasure);
        parcel.writeString(mIngredient);
    }

    public void readFromParcel(Parcel in) {
        mQuantity = in.readInt();
        mMeasure = in.readString();
        mIngredient = in.readString();

    }

    public static final Parcelable.Creator<Ingredient> CREATOR = new Creator<Ingredient>() {

        @Override
        public Ingredient createFromParcel(Parcel source) {
            return new Ingredient(source);
        }

        @Override
        public Ingredient[] newArray(int size) {
            return new Ingredient[size];
        }
    };
}
