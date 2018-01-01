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
    private Context mContext;

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
            holder.stepName.setText(position + ". " + step.getmShortDescription());
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

    public void setStepsList(ArrayList<RecipeStep> steps) {
        mSteps = steps;
        notifyDataSetChanged();
    }


    public class StepHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.step_name)
        TextView stepName;

        public StepHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
