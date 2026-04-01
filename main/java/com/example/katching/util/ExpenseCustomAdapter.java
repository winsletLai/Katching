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
import com.example.katching.UpdateExpenseActivity;

import java.util.ArrayList;

public class ExpenseCustomAdapter extends RecyclerView.Adapter<ExpenseCustomAdapter.MyViewHolder> {

    private Context context;
    private ArrayList expense_id,expense_type, expense_name, expense_value, expense_image, expense_date;


    public ExpenseCustomAdapter(Context context,ArrayList expense_id, ArrayList expense_type,ArrayList expense_name, ArrayList expense_value, ArrayList expense_image, ArrayList expense_date){
        this.context = context;
        this.expense_id = expense_id;
        this.expense_type = expense_type;
        this.expense_name = expense_name;
        this.expense_value = expense_value;
        this.expense_image = expense_image;
        this.expense_date = expense_date;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.expense_row, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        holder.txt_expense_type.setText(String.valueOf(expense_type.get(position)));
        holder.txt_expense_name.setText(String.valueOf(expense_name.get(position)));
        holder.txt_expense_value.setText(String.valueOf(expense_value.get(position)));
        holder.img_expense_image.setImageResource((Integer) expense_image.get(position));
        holder.expenseLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, UpdateExpenseActivity.class);
                intent.putExtra("id",String.valueOf(expense_id.get(position)));
                intent.putExtra("name",String.valueOf(expense_name.get(position)));
                intent.putExtra("value",String.valueOf(expense_value.get(position)));
                intent.putExtra("date",String.valueOf(expense_date.get(position)));
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return expense_id.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView txt_expense_type, txt_expense_name, txt_expense_value;
        ImageView img_expense_image;
        LinearLayout expenseLayout;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            txt_expense_type = itemView.findViewById(R.id.txt_expense_type);
            txt_expense_name = itemView.findViewById(R.id.txt_expense_name);
            txt_expense_value = itemView.findViewById(R.id.txt_expense_value);
            img_expense_image = itemView.findViewById(R.id.img_expense_type);
            expenseLayout = itemView.findViewById(R.id.expenseRowLayout);
        }
    }
}

