package com.mahausch.caketime;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipeDetailFragment extends Fragment {

    @BindView(R.id.recycler_view_detail)
    RecyclerView recycler;

    public RecipeDetailFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.fragment_recipe_detail, container, false);
        ButterKnife.bind(this, rootView);

        Bundle bundle = getArguments();
        Recipe recipe = bundle.getParcelable("recipe");
        ArrayList<RecipeStep> steps = recipe.getRecipeSteps();

        recycler.setLayoutManager(new LinearLayoutManager(getContext()));
        StepAdapter adapter = new StepAdapter();
        recycler.setAdapter(adapter);
        adapter.setStepsList(steps);

        return rootView;
    }
}
