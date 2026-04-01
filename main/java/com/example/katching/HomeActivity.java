package com.example.katching;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class HomeActivity extends AppCompatActivity {

    KatchingDatabase myDB = new KatchingDatabase(HomeActivity.this);
    String TABLE_BALANCE = "balance", balance_value;

    TextView balanceTxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        balanceTxt = findViewById(R.id.txtBudget);

        storeData();

        Button budgetBalance = (Button)findViewById(R.id.btnBudgetBalanceMain);
        budgetBalance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeActivity.this, AddBalanceActivity.class));
            }
        });

        Button budget = (Button)findViewById(R.id.btnBudget);
        budget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeActivity.this, BudgetActivity.class));
            }
        });

        Button expense = (Button)findViewById(R.id.btnExpense);
        expense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeActivity.this, ExpenseActivity.class));
            }
        });

        Button report = (Button)findViewById(R.id.btnReport);
        report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeActivity.this, ReportActivity.class));
            }
        });
    }

    void storeData(){
        Cursor cursor = myDB.readSelectedBudgetData(TABLE_BALANCE);
        if (cursor.getCount() == 0){
            Toast.makeText(this, "No data.", Toast.LENGTH_SHORT).show();
        } else {
            while(cursor.moveToNext()){
                balance_value = cursor.getString(1);
            }
            balanceTxt.setText("RM " + balance_value);
        }
    }
}