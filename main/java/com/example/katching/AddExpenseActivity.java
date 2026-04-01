package com.example.katching;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.katching.util.DatePickerUtil;
import com.example.katching.util.ImageMap;

public class AddExpenseActivity extends AppCompatActivity {

    Spinner type_input;
    Button btnAdd,btnDate;
    EditText value, name;

    String formattedDate = DatePickerUtil.getTodayFormattedDate();
    KatchingDatabase myDB = new KatchingDatabase(AddExpenseActivity.this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_expense);

        String[] expenseTypes ={"Food / Beverages", "Housing / Utilities", "Transportation", "Shopping","Health / Wellness","Entertainment / Leisure","Education","Communication","Pets","Savings / Investments","Gifts / Donations","Personal / Miscellaneous"};

        type_input = findViewById(R.id.expenseTypeSpinner);
        value = findViewById(R.id.expense_values_input);
        name = findViewById(R.id.expense_name_input);
        btnAdd = findViewById(R.id.btnAddExpense);
        btnDate = findViewById(R.id.expense_date_input);

        ArrayAdapter adapter = new ArrayAdapter(getApplicationContext(),android.R.layout.simple_spinner_item,expenseTypes);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        type_input.setAdapter(adapter);



        btnDate.setText(DatePickerUtil.getTodayDateString());

        btnDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerUtil.showDatePicker(AddExpenseActivity.this, (display, formatted) -> {
                    btnDate.setText(display);
                    formattedDate = formatted; // save this to insert into DB
                });
            }
        });

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String expenseType = type_input.getSelectedItem().toString();
                int imageExpenseType = ImageMap.getImageResource(expenseType);
                String strValue = value.getText().toString().trim();
                String expenseName = name.getText().toString().trim();
                String expenseDate = formattedDate;
                if(strValue.isEmpty() || expenseName.isEmpty()) {
                    Toast.makeText(AddExpenseActivity.this, "Please fill in all of the value", Toast.LENGTH_SHORT).show();
                    return;
                }
                try {
                    double expenseValue = Double.parseDouble(strValue);
                    if(expenseValue >= 0.01 && expenseValue <= 1000000000){
                        expenseValue = Math.round(expenseValue * 100.0) / 100.0;


                        boolean success = myDB.addExpense(expenseType,expenseName,expenseValue,imageExpenseType,expenseDate);
                        if (success) {
                            Intent i = new Intent(AddExpenseActivity.this, ExpenseActivity.class);
                            startActivity(i);
                        }
                    }
                    else{
                        Toast.makeText(AddExpenseActivity.this, "Value must be bigger than 0 & less than 1000,000,000", Toast.LENGTH_SHORT).show();
                    }

                } catch (NumberFormatException e) {
                    Toast.makeText(AddExpenseActivity.this, "Value must be a number", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}