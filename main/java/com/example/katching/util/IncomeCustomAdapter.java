package com.example.katching.util;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.katching.R;
import com.example.katching.UpdateIncomeActivity;

import java.util.ArrayList;

public class IncomeCustomAdapter extends RecyclerView.Adapter<IncomeCustomAdapter.MyViewHolder> {

    private Context context;
    private ArrayList income_id, income_name, income_value, income_date;


    public IncomeCustomAdapter(Context context,ArrayList income_id,ArrayList income_name, ArrayList income_value, ArrayList income_date){
        this.context = context;
        this.income_id = income_id;
        this.income_name = income_name;
        this.income_value = income_value;
        this.income_date = income_date;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.income_row, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        holder.txt_income_name.setText(String.valueOf(income_name.get(position)));
        holder.txt_income_value.setText(String.valueOf(income_value.get(position)));
        holder.txt_income_date.setText(String.valueOf(income_date.get(position)));
        holder.incomeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, UpdateIncomeActivity.class);
                intent.putExtra("id",String.valueOf(income_id.get(position)));
                intent.putExtra("name",String.valueOf(income_name.get(position)));
                intent.putExtra("value",String.valueOf(income_value.get(position)));
                intent.putExtra("date",String.valueOf(income_date.get(position)));
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return income_id.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView txt_income_date, txt_income_name, txt_income_value;
        LinearLayout incomeLayout;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            txt_income_date = itemView.findViewById(R.id.txt_income_date);
            txt_income_name = itemView.findViewById(R.id.txt_income_name);
            txt_income_value = itemView.findViewById(R.id.txt_income_value);
            incomeLayout = itemView.findViewById(R.id.incomeRowLayout);
        }
    }
}

