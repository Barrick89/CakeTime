package com.mahausch.caketime;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StepAdapter extends RecyclerView.Adapter<StepAdapter.StepHolder> {

    private ArrayList<RecipeStep> mSteps;
    private ArrayList<Ingredient> mIngredients;
    private final StepAdapterOnClickHandler mOnClickHandler;
    private Context mContext;

    public StepAdapter(StepAdapterOnClickHandler onClickHandler) {
        mOnClickHandler = onClickHandler;
    }

    @Override
    public StepHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(mContext);
        int layout = R.layout.steps_list_item;

        View view = inflater.inflate(layout, parent, false);
        return new StepHolder(view);
    }

    @Override
    public void onBindViewHolder(StepHolder holder, int position) {

        if (position == 0) {
            holder.stepName.setText(R.string.ingredients);
        } else {
            RecipeStep step = mSteps.get(position);
            holder.stepName.setText(position + ". " + step.getShortDescription());
        }
    }

    @Override
    public int getItemCount() {

        if (mSteps != null) {
            return mSteps.size();
        } else {
            return 0;
        }
    }

    public void setStepsList(ArrayList<RecipeStep> steps, ArrayList<Ingredient> ingredients) {
        mSteps = steps;
        mIngredients = ingredients;
        notifyDataSetChanged();
    }

    public interface StepAdapterOnClickHandler {
        public void onClickIngredient(ArrayList<Ingredient> ingredients);

        public void onClickStep(RecipeStep step);
    }


    public class StepHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.step_name)
        TextView stepName;

        public StepHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();

            if (position == 0) {
                mOnClickHandler.onClickIngredient(mIngredients);
            } else {
                RecipeStep step = mSteps.get(position);
                mOnClickHandler.onClickStep(step);
            }
        }
    }
}
