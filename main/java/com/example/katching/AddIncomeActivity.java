package com.example.katching;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.katching.util.DatePickerUtil;

public class AddIncomeActivity extends AppCompatActivity {

    EditText name_input, value_input;
    Button btnAdd, btnDate;

    String formattedDate = DatePickerUtil.getTodayFormattedDate();

    KatchingDatabase myDB = new KatchingDatabase(AddIncomeActivity.this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_income);

        value_input= findViewById(R.id.income_values_input);
        name_input = findViewById(R.id.income_name_input);
        btnAdd = findViewById(R.id.btnAddIncome);
        btnDate = findViewById(R.id.income_date_input);

        btnDate.setText(DatePickerUtil.getTodayDateString());

        btnDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerUtil.showDatePicker(AddIncomeActivity.this, (display, formatted) -> {
                    btnDate.setText(display);
                    formattedDate = formatted; // save this to insert into DB
                });
            }
        });

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String strValue = value_input.getText().toString().trim();
                String incomeName = name_input.getText().toString().trim();
                String incomeDate = formattedDate;
                if (strValue.isEmpty() || incomeName.isEmpty()) {
                    Toast.makeText(AddIncomeActivity.this, "Please fill in all of the value", Toast.LENGTH_SHORT).show();
                    return;
                }
                try {
                    double incomeValue = Double.parseDouble(strValue);
                    if(incomeValue >= 0.01 && incomeValue <= 1000000000){
                        incomeValue = Math.round(incomeValue * 100.0) / 100.0;

                        boolean success = myDB.addIncome(incomeName,incomeValue,incomeDate);
                        if (success) {
                            Intent i = new Intent(AddIncomeActivity.this, ReportActivity.class);
                            startActivity(i);
                        }
                    }
                    else{
                        Toast.makeText(AddIncomeActivity.this, "Value must be bigger than 0 & less than 1000,000,000", Toast.LENGTH_SHORT).show();
                    }

                } catch (NumberFormatException e) {
                    Toast.makeText(AddIncomeActivity.this, "Value must be a number", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}