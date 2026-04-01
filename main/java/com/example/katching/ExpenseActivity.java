package com.example.katching;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.katching.util.ExpenseCustomAdapter;
import com.example.katching.util.DatePickerUtil;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class ExpenseActivity extends AppCompatActivity {

    String TABLE_BALANCE = "balance";
    double balance_value;

    RecyclerView recyclerView;
    FloatingActionButton btnAdd;
    Button btnDate;
    String formattedDate = DatePickerUtil.getTodayFormattedDate();
    TextView txtDailyExpense,txtDailyBalance,txtDailyRemainder;
    ImageView empty_image;

    KatchingDatabase myDB;
    ArrayList<String> expense_id,expense_type,expense_name, expense_value, expense_date;
    ArrayList<Integer> expense_img;
    ExpenseCustomAdapter customAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expense);
        recyclerView = findViewById(R.id.recyclerViewExpense);
        btnAdd = findViewById(R.id.btnAddExpenseMain);
        btnDate = findViewById(R.id.expense_date);
        txtDailyExpense = findViewById(R.id.txtDailyExpenseValue);
        txtDailyBalance = findViewById(R.id.txtDailyBalanceValue);
        txtDailyRemainder = findViewById(R.id.txtDailyLeftoverValue);
        empty_image =findViewById(R.id.imgExpenseEmpty);

        myDB = new KatchingDatabase(ExpenseActivity.this);
        expense_id = new ArrayList<>();
        expense_type = new ArrayList<>();
        expense_name = new ArrayList<>();
        expense_value = new ArrayList<>();
        expense_img = new ArrayList<>();
        expense_date = new ArrayList<>();

        storeData(formattedDate);
        storeBalanceData();

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ExpenseActivity.this, AddExpenseActivity.class);
                startActivity(intent);
            }
        });

        btnDate.setText(DatePickerUtil.getTodayDateString());

        btnDate.setOnClickListener(view -> {
            DatePickerUtil.showDatePicker(ExpenseActivity.this, (display, formatted) -> {
                btnDate.setText(display);
                formattedDate = formatted;

                expense_id.clear();
                expense_type.clear();
                expense_name.clear();
                expense_img.clear();
                expense_value.clear();
                expense_date.clear();

                storeData(formattedDate);
                customAdapter.notifyDataSetChanged();
                calculateTotalExpense(expense_value);
                calculateRemainder(expense_value);

            });
        });

        customAdapter = new ExpenseCustomAdapter(ExpenseActivity.this,expense_id, expense_type, expense_name,expense_value,expense_img,expense_date);
        recyclerView.setAdapter(customAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(ExpenseActivity.this));

        calculateTotalExpense(expense_value);
        calculateRemainder(expense_value);
    }

    void storeData(String formattedDate){
        Cursor cursor = myDB.readSelectedExpenseData(formattedDate);
        if (cursor.getCount() == 0){
            empty_image.setVisibility(View.VISIBLE);
            Toast.makeText(this, "No data.", Toast.LENGTH_SHORT).show();
        } else {
            while(cursor.moveToNext()){
                expense_id.add(cursor.getString(0));
                expense_name.add(cursor.getString(1));
                expense_type.add(cursor.getString(2));
                expense_value.add(cursor.getString(3));
                expense_img.add(cursor.getInt(4));
                expense_date.add(cursor.getString(5));
            }
            empty_image.setVisibility(View.GONE);
        }
    }

    Double calculateTotalExpense(ArrayList<String> expense_value){
        double totalExpense = 0.0;
        for(int i = 0; i < expense_value.size(); i++){
            double expense = Double.parseDouble(expense_value.get(i));
            totalExpense += expense;
        }

        txtDailyExpense.setText("RM " + totalExpense);
        return totalExpense;
    }

    void storeBalanceData(){
        Cursor cursor = myDB.readSelectedBudgetData(TABLE_BALANCE);
        if (cursor.getCount() == 0){
            Toast.makeText(this, "No data.", Toast.LENGTH_SHORT).show();
        } else {
            while(cursor.moveToNext()){
                balance_value = cursor.getDouble(1);
            }
            txtDailyBalance.setText("RM " + balance_value);
        }
    }

    double calculateRemainder(ArrayList<String> expense_value){
        double remainder = balance_value - calculateTotalExpense(expense_value);
        remainder = Math.round(remainder * 100.0) / 100.0;
        txtDailyRemainder.setText("RM "+ remainder);
        return remainder;
    }

}