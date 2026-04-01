package com.example.katching;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.katching.util.DatePickerUtil;
import com.example.katching.util.ImageMap;

public class UpdateIncomeActivity extends AppCompatActivity {
    String id, name, value, date;
    String formattedDate;
    String TABLE_NAME = "income", COLUMN_INCOME_ID = "i_id";
    EditText name_input,value_input;
    Button date_input,btnUpdate, btnDelete;
    KatchingDatabase myDB = new KatchingDatabase(UpdateIncomeActivity.this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_income);

        value_input = findViewById(R.id.income_values_input2);
        name_input = findViewById(R.id.income_name_input2);
        date_input = findViewById(R.id.income_date_input2);
        btnUpdate = findViewById(R.id.btnUpdateIncome);
        btnDelete = findViewById(R.id.btnDeleteIncome);

        date_input.setText(DatePickerUtil.getTodayDateString());

        getAndSetIntentData();

        ActionBar ab = getSupportActionBar();
        if(ab != null){
            ab.setTitle(name);
        }

        date_input.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerUtil.showDatePicker(UpdateIncomeActivity.this, (display, formatted) -> {
                    date_input.setText(display);
                    formattedDate = formatted;
                });
            }
        });

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String strValue = value_input.getText().toString().trim();
                String incomeName = name_input.getText().toString().trim();
                String incomeDate = formattedDate;
                if(strValue.isEmpty() || incomeName.isEmpty()) {
                    Toast.makeText(UpdateIncomeActivity.this, "Please fill in all of the value", Toast.LENGTH_SHORT).show();
                    return;
                }
                try {
                    double incomeValue = Double.parseDouble(strValue);
                    if(incomeValue >= 0.01 && incomeValue <= 1000000000){
                        incomeValue = Math.round(incomeValue * 100.0) / 100.0;


                        boolean success = myDB.updateIncomeData(id,incomeName,incomeValue,incomeDate);
                        if (success) {
                            Intent i = new Intent(UpdateIncomeActivity.this, ReportActivity.class);
                            startActivity(i);
                        }
                    }
                    else{
                        Toast.makeText(UpdateIncomeActivity.this, "Value must be bigger than 0 & less than 1000,000,000", Toast.LENGTH_SHORT).show();
                    }

                } catch (NumberFormatException e) {
                    Toast.makeText(UpdateIncomeActivity.this, "Value must be a number", Toast.LENGTH_SHORT).show();
                }
            }
        });
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confirmDialog();
            }
        });
    }

    void getAndSetIntentData(){
        if(getIntent().hasExtra("id") && getIntent().hasExtra("name") && getIntent().hasExtra("value") && getIntent().hasExtra("date")){
            //Getting data from intent from the budgetCustomAdapter
            id = getIntent().getStringExtra("id");
            name = getIntent().getStringExtra("name");
            value = getIntent().getStringExtra("value");
            date = getIntent().getStringExtra("date");

            //Setting intent data
            name_input.setText(name);
            value_input.setText(value);

            String day = date.substring(8);
            String monthStr = date.substring(5,7);
            String year = date.substring(0,4);


            int dayNo = Integer.parseInt(day);
            int monthNo = Integer.parseInt(monthStr);
            int yearNo = Integer.parseInt(year);

            formattedDate = DatePickerUtil.formatDateForDatabase(yearNo,monthNo,dayNo);

            monthNo -= 1;

            String month = DatePickerUtil.getMonthFormat(monthNo);

            date_input.setText(month + " " + dayNo + " " + yearNo);

        }else{
            Toast.makeText(this, "No data.", Toast.LENGTH_SHORT).show();
        }
    }
    void confirmDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete " + name + " ?");
        builder.setMessage("Are you sure you want to delete " + name + " ?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                boolean success = myDB.deleteSingleData(TABLE_NAME, COLUMN_INCOME_ID, id);
                if(success){
                    Intent intent = new Intent(UpdateIncomeActivity.this, ReportActivity.class);
                    startActivity(intent);
                }
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });
        builder.create().show();
    }
}