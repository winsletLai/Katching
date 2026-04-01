package com.example.katching;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class UpdateBudgetActivity extends AppCompatActivity {

    String type, value, id;
    String TABLE_NAME = "budget", COLUMN_BUDGET_ID = "b_id", COLUMN_BUDGET_VALUE = "budget_value";
    TextView type_input;
    EditText value_input;
    Button btnUpdate, btnDelete;
    ArrayList<Double> budget_value;
    double originalValue;

    KatchingDatabase myDB = new KatchingDatabase(UpdateBudgetActivity.this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_budget);

        type_input = findViewById(R.id.budgetType);
        value_input = findViewById(R.id.budget_values_input2);
        btnUpdate = findViewById(R.id.btnUpdateBudget);
        btnDelete = findViewById(R.id.btnDeleteBudget);

        budget_value = new ArrayList<>();

        storeData();

        double totalBudget = calculateTotalBudget(budget_value);

        getAndSetIntentData();

        //set action bar title
        ActionBar ab = getSupportActionBar();
        if(ab != null){
            ab.setTitle(type);
        }

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String strValue = value_input.getText().toString().trim();
                if(strValue.isEmpty()) {
                    Toast.makeText(UpdateBudgetActivity.this, "Please fill in the value", Toast.LENGTH_SHORT).show();
                    return;
                }
                try {
                    double budgetValue = Double.parseDouble(strValue);
                    double maxBudget = (100 + originalValue) - totalBudget;
                    if(budgetValue >= 0 && budgetValue <= maxBudget){
                        budgetValue = Math.round(budgetValue * 100.0) / 100.0;
                        boolean success = myDB.updateBudgetData(id,budgetValue);
                        if (success) {
                            Intent i = new Intent(UpdateBudgetActivity.this, BudgetActivity.class);
                            startActivity(i);
                        }
                    }
                    else{
                        Toast.makeText(UpdateBudgetActivity.this, "Value must be bigger than 0, lesser than " + maxBudget, Toast.LENGTH_SHORT).show();
                    }

                } catch (NumberFormatException e) {
                    Toast.makeText(UpdateBudgetActivity.this, "Value must be a number", Toast.LENGTH_SHORT).show();
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
        if(getIntent().hasExtra("id") && getIntent().hasExtra("type") && getIntent().hasExtra("value")){
            //Getting data from intent from the budgetCustomAdapter
            id = getIntent().getStringExtra("id");
            type = getIntent().getStringExtra("type");
            value = getIntent().getStringExtra("value");

            //Setting intent data
            type_input.setText(type);
            value_input.setText(value);

            originalValue = Double.parseDouble(value);

        }else{
            Toast.makeText(this, "No data.", Toast.LENGTH_SHORT).show();
        }
    }

    void confirmDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete " + type + " ?");
        builder.setMessage("Are you sure you want to delete " + type + " ?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                boolean success = myDB.deleteSingleData(TABLE_NAME, COLUMN_BUDGET_ID, id);
                if(success){
                    Intent intent = new Intent(UpdateBudgetActivity.this, BudgetActivity.class);
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

    void storeData(){
        Cursor cursor = myDB.readSelectedBudgetData(TABLE_NAME);
        if (cursor.getCount() == 0){
            Toast.makeText(this, "No data.", Toast.LENGTH_SHORT).show();
        } else {
            while(cursor.moveToNext()){
                budget_value.add(cursor.getDouble(2));
            }
        }
    }

    Double calculateTotalBudget(ArrayList<Double> budget_value){
        double totalBudget = 0.0;
        for(int i = 0; i < budget_value.size(); i++){
            double budget = budget_value.get(i);
            totalBudget += budget;
        }
        return totalBudget;
    }
}