package com.example.katching;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.katching.util.DatePickerUtil;
import com.example.katching.util.ImageMap;
import com.example.katching.util.IncomeCustomAdapter;
import com.example.katching.util.MonthlyExpenseCustomAdapter;

import java.util.ArrayList;

public class ReportActivity extends AppCompatActivity {

    String TABLE_EXPENSE = "expense", TABLE_INCOME = "income";
    String COLUMN_EXPENSE_DATE = "expense_date", COLUMN_INCOME_DATE = "income_date";
    Spinner monthSpinner,yearSpinner;
    Button btnAdd,btnSearch;
    ImageView empty_expense_image, empty_income_image;
    RecyclerView monthlyIncomeRecyclerView, monthlyExpenseRecyclerView;
    TextView txtMonthlyIncomeValue,txtMonthlyRemainderValue,txtMonthlyExpenseValue;
    ArrayList<String>  expense_type, expense_value;
    ArrayList<String> income_id, income_name, income_value, income_date ;
    ArrayList<Integer> expense_img;

    KatchingDatabase myDB= new KatchingDatabase(ReportActivity.this);
    IncomeCustomAdapter incomeCustomAdapter;
    MonthlyExpenseCustomAdapter monthlyExpenseCustomAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);

       monthSpinner = findViewById(R.id.expenseMonthSpinner);
       yearSpinner = findViewById(R.id.expenseYearSpinner);
       monthlyIncomeRecyclerView = findViewById(R.id.recyclerViewMonthlyIncome);
       monthlyExpenseRecyclerView = findViewById(R.id.recyclerViewMonthlyExpense);
       empty_expense_image = findViewById(R.id.imgMonthlyExpenseEmpty);
       empty_income_image = findViewById(R.id.imgMonthlyIncomeEmpty);
       btnAdd = findViewById(R.id.btnAddIncomeMain);
       btnSearch =findViewById(R.id.searchButton);
       txtMonthlyIncomeValue = findViewById(R.id.txtMonthlyIncomeValue);
       txtMonthlyRemainderValue = findViewById(R.id.txtMonthlyRemainderValue);
       txtMonthlyExpenseValue = findViewById(R.id.txtMonthlyExpenseValue);


       expense_type = new ArrayList<>();
       expense_value = new ArrayList<>();
       expense_img = new ArrayList<>();

       income_id = new ArrayList<>();
       income_name = new ArrayList<>();
       income_value = new ArrayList<>();
       income_date = new ArrayList<>();

       addMonthAndYearIntoSpinner();


       btnAdd.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               Intent intent = new Intent(ReportActivity.this, AddIncomeActivity.class);
               startActivity(intent);
           }
       });

       btnSearch.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               clearData();
               String year = yearSpinner.getSelectedItem().toString();
               String monthName = monthSpinner.getSelectedItem().toString();
               String month = getMonthNumber(monthName);
               storeMonthlyData(TABLE_EXPENSE, COLUMN_EXPENSE_DATE,month,year);
               storeMonthlyData(TABLE_INCOME,COLUMN_INCOME_DATE,month,year);

               incomeCustomAdapter.notifyDataSetChanged();
               monthlyExpenseCustomAdapter.notifyDataSetChanged();

               double totalIncome =calculateTotalValue(income_value);
               txtMonthlyIncomeValue.setText("RM " + totalIncome);

               double totalExpense = calculateTotalValue(expense_value);
               txtMonthlyExpenseValue.setText("RM " + totalExpense);

               calculateTotalRemainder(totalExpense, totalIncome);

           }
       });

       incomeCustomAdapter = new IncomeCustomAdapter(ReportActivity.this,income_id,income_name,income_value,income_date);
       monthlyIncomeRecyclerView.setAdapter(incomeCustomAdapter);
       monthlyIncomeRecyclerView.setLayoutManager(new LinearLayoutManager(ReportActivity.this));

       monthlyExpenseCustomAdapter = new MonthlyExpenseCustomAdapter(ReportActivity.this, expense_type, expense_value, expense_img);
       monthlyExpenseRecyclerView.setAdapter(monthlyExpenseCustomAdapter);
       monthlyExpenseRecyclerView.setLayoutManager(new LinearLayoutManager(ReportActivity.this));

       double totalIncome =calculateTotalValue(income_value);
       txtMonthlyIncomeValue.setText("RM " + totalIncome);

       double totalExpense = calculateTotalValue(expense_value);
       txtMonthlyExpenseValue.setText("RM " + totalExpense);

       calculateTotalRemainder(totalExpense, totalIncome);

    }

    public void addMonthAndYearIntoSpinner(){

        String[] months = {"JAN", "FEB", "MAR", "APR", "MAY", "JUN",
                "JUL", "AUG", "SEP", "OCT", "NOV", "DEC"};

        ArrayAdapter<String> monthAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, months);
        monthAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        monthSpinner.setAdapter(monthAdapter);

        int thisYear = DatePickerUtil.getTodayYear();

        ArrayList<String> years = new ArrayList<>();
        for (int i = 2020; i <= thisYear; i++) {
            years.add(String.valueOf(i));
        }
        ArrayAdapter<String> yearAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, years);
        yearAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        yearSpinner.setAdapter(yearAdapter);

        String year = years.get(0);
        String monthName = months[0];
        String month = getMonthNumber(monthName);
        storeMonthlyData(TABLE_EXPENSE, COLUMN_EXPENSE_DATE,month,year);
        storeMonthlyData(TABLE_INCOME,COLUMN_INCOME_DATE,month,year);
    }

    private String getMonthNumber(String monthName) {
        switch (monthName) {
            case "JAN": return "01";
            case "FEB": return "02";
            case "MAR": return "03";
            case "APR": return "04";
            case "MAY": return "05";
            case "JUN": return "06";
            case "JUL": return "07";
            case "AUG": return "08";
            case "SEP": return "09";
            case "OCT": return "10";
            case "NOV": return "11";
            case "DEC": return "12";
            default: return "01";
        }
    }

    private void storeMonthlyData(String tableName, String columnName, String month, String year){
        Cursor cursor = myDB.readMonthlyData(tableName, columnName, month, year);
        if (cursor.getCount() == 0){
            if(tableName.equals("expense")){
                empty_expense_image.setVisibility(View.VISIBLE);
            }
            else if(tableName.equals("income")){
                empty_income_image.setVisibility(View.VISIBLE);
            }

            Toast.makeText(this, "No data.", Toast.LENGTH_SHORT).show();
        } else {
            if(tableName.equals("expense")){
            while(cursor.moveToNext()){
                expense_type.add(cursor.getString(0));
                expense_value.add(cursor.getString(1));

                String expenseType = cursor.getString(0);
                int imageExpenseType = ImageMap.getImageResource(expenseType);
                expense_img.add(imageExpenseType);
            }
                empty_expense_image.setVisibility(View.GONE);

            }
            else if(tableName.equals("income")){
                while(cursor.moveToNext()){
                    income_id.add(cursor.getString(0));
                    income_name.add(cursor.getString(1));
                    income_value.add(cursor.getString(2));
                    income_date.add(cursor.getString(3));
                }
                empty_income_image.setVisibility(View.GONE);
            }
        }
    }

    private void clearData(){
        expense_type.clear();
        expense_value.clear();
        expense_img.clear();

        income_id.clear();
        income_name.clear();
        income_value.clear();
        income_date.clear();
    }

    private double calculateTotalValue(ArrayList<String> array_value){
        double totalValue = 0.0;
        for(int i = 0; i < array_value.size(); i++){
            double value = Double.parseDouble(array_value.get(i));
            totalValue += value;
        }

        totalValue = Math.round(totalValue * 100.0) / 100.0;
        return totalValue;
    }

    private void calculateTotalRemainder (double expense, double income){
        double remainder = income - expense;
        remainder = Math.round(remainder * 100.0) / 100.0;
        txtMonthlyRemainderValue.setText("RM " + remainder);
    }
}