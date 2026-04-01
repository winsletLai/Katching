package com.example.katching.util;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.katching.R;

import java.util.ArrayList;

public class MonthlyExpenseCustomAdapter extends RecyclerView.Adapter<MonthlyExpenseCustomAdapter.MyViewHolder> {

    private Context context;
    private ArrayList expense_type, expense_value, expense_image;


    public MonthlyExpenseCustomAdapter(Context context, ArrayList expense_type, ArrayList expense_value, ArrayList expense_image){
        this.context = context;
        this.expense_type = expense_type;
        this.expense_value = expense_value;
        this.expense_image = expense_image;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.monthly_expense_row, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        holder.txt_expense_type.setText(String.valueOf(expense_type.get(position)));
        holder.txt_expense_value.setText(String.valueOf(expense_value.get(position)));
        holder.img_expense_image.setImageResource((Integer) expense_image.get(position));
    }

    @Override
    public int getItemCount() {
        return expense_type.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView txt_expense_type, txt_expense_value;
        ImageView img_expense_image;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            txt_expense_type = itemView.findViewById(R.id.txt_mexpense_type);
            txt_expense_value = itemView.findViewById(R.id.txt_mexpense_value);
            img_expense_image = itemView.findViewById(R.id.img_mexpense);
        }
    }
}

