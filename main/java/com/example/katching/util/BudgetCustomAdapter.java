package com.example.katching.util;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.katching.R;
import com.example.katching.UpdateBudgetActivity;

import java.util.ArrayList;

public class BudgetCustomAdapter extends RecyclerView.Adapter<BudgetCustomAdapter.MyViewHolder> {

    private Context context;
    private ArrayList budget_id,budget_type, budget_value, budget_image, budget_valueReal;

    public BudgetCustomAdapter(Context context, ArrayList budget_id, ArrayList budget_type, ArrayList budget_value, ArrayList budget_image, ArrayList budget_valueReal){
        this.context = context;
        this.budget_id = budget_id;
        this.budget_type = budget_type;
        this.budget_value = budget_value;
        this.budget_image = budget_image;
        this.budget_valueReal = budget_valueReal;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.budget_row, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        holder.txt_budget_type.setText(String.valueOf(budget_type.get(position)));
        holder.txt_budget_value.setText(String.valueOf(budget_value.get(position)));
        holder.txt_budget_valueReal.setText(String.valueOf(budget_valueReal.get(position)));
        holder.img_budget_image.setImageResource((Integer) budget_image.get(position));
        holder.budgetLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, UpdateBudgetActivity.class);
                intent.putExtra("id",String.valueOf(budget_id.get(position)));
                intent.putExtra("type",String.valueOf(budget_type.get(position)));
                intent.putExtra("value",String.valueOf(budget_value.get(position)));
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return budget_id.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView txt_budget_type, txt_budget_value, txt_budget_valueReal;
        ImageView img_budget_image;
        LinearLayout budgetLayout;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            txt_budget_type = itemView.findViewById(R.id.txt_budget_type);
            txt_budget_value = itemView.findViewById(R.id.txt_budget_value);
            txt_budget_valueReal = itemView.findViewById(R.id.txt_budget_valueReal);
            img_budget_image = itemView.findViewById(R.id.img_budget_type);
            budgetLayout = itemView.findViewById(R.id.budgetRowLayout);
        }
    }
}
