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

import com.example.katching.util.BudgetCustomAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class BudgetActivity extends AppCompatActivity {

    String TABLE_BUDGET = "budget",TABLE_BALANCE = "balance";
    RecyclerView recyclerView;
    FloatingActionButton btnAdd;
    Button btnAddBalance;
    ImageView empty_image;
    TextView unbudget_balance_percentage, unbudget_balance_value;
    KatchingDatabase myDB;
    ArrayList<String> budget_type, budget_value, budget_id, budget_valueReal;
    ArrayList<Integer> budget_image;
    BudgetCustomAdapter customAdapter;

    String balance_value;

    double totalBalance;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_budget);

        recyclerView = findViewById(R.id.recyclerViewBudget);
        btnAdd = findViewById(R.id.btnAddBudgetMain);
        btnAddBalance = findViewById(R.id.btnBudgetBalance);
        empty_image =findViewById(R.id.imgBudgetEmpty);
        unbudget_balance_percentage=findViewById(R.id.txtUnbudgetedBalancePercent);
        unbudget_balance_value = findViewById(R.id.txtUnbudgetedBalanceValue);

        myDB = new KatchingDatabase(BudgetActivity.this);
        budget_id = new ArrayList<>();
        budget_type = new ArrayList<>();
        budget_value = new ArrayList<>();
        budget_image = new ArrayList<>();
        budget_valueReal = new ArrayList<>();

        storeData();
        storeBalanceData();


        double totalBudget = calculateTotalBudget(budget_value);
        calculateEachBudgetValue(budget_value);

        double percent_balance_unbudget = 100 - totalBudget;
        double value_balance_unbudget = totalBalance * (percent_balance_unbudget /100);
        value_balance_unbudget = Math.round(value_balance_unbudget * 100.0) / 100.0;

        unbudget_balance_percentage.setText(percent_balance_unbudget + "%");
        if (budget_value.size() == 0) {
            // No budget entries yet
            unbudget_balance_value.setText("RM " + balance_value);
        } else if (value_balance_unbudget <= 0.01) {
            unbudget_balance_value.setText("RM 0.00");
        } else {
            unbudget_balance_value.setText("RM " + value_balance_unbudget);
        }


        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (totalBudget < 100){
                    Intent intent = new Intent(BudgetActivity.this, AddBudgetActivity.class);
                    startActivity(intent);
                }
                else{
                    Toast.makeText(BudgetActivity.this, "Please update the value of other budget, Total budget can't be more than 100", Toast.LENGTH_SHORT).show();
                }
            }
        });

        customAdapter = new BudgetCustomAdapter(BudgetActivity.this, budget_id, budget_type, budget_value, budget_image, budget_valueReal);
        recyclerView.setAdapter(customAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(BudgetActivity.this));

        btnAddBalance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(BudgetActivity.this, AddBalanceActivity.class);
                startActivity(i);
            }
        });

    }

    void storeData(){
        Cursor cursor = myDB.readSelectedBudgetData(TABLE_BUDGET);
        if (cursor.getCount() == 0){
            empty_image.setVisibility(View.VISIBLE);
            Toast.makeText(this, "No data.", Toast.LENGTH_SHORT).show();
        } else {
            while(cursor.moveToNext()){
                budget_id.add(cursor.getString(0));
                budget_type.add(cursor.getString(1));
                budget_value.add(cursor.getString(2));
                budget_image.add(cursor.getInt(3));
            }
            empty_image.setVisibility(View.GONE);
        }
    }

    void storeBalanceData(){
        Cursor cursor = myDB.readSelectedBudgetData(TABLE_BALANCE);
        if (cursor.getCount() == 0){
            balance_value = "0.0";
            Toast.makeText(this, "No data.", Toast.LENGTH_SHORT).show();
        } else {
            while(cursor.moveToNext()){
                balance_value = cursor.getString(1);
            }
            btnAddBalance.setText("Today's Balance: RM " + balance_value);
        }
    }

    Double calculateTotalBudget(ArrayList<String> budget_value){
        double totalBudget = 0.0;
        for(int i = 0; i < budget_value.size(); i++){
            double budget = Double.parseDouble(budget_value.get(i));
            totalBudget += budget;
        }
        return totalBudget;
    }

    void calculateEachBudgetValue(ArrayList<String> budget_value){

        for(int i = 0; i < budget_value.size(); i++){
            double budget = Double.parseDouble(budget_value.get(i));
            totalBalance = Double.parseDouble(balance_value);
            double budgetReal = (totalBalance * (budget / 100));
            budgetReal = Math.round(budgetReal * 100.0) / 100.0;
            budget_valueReal.add("" + budgetReal);

        }
    }
}