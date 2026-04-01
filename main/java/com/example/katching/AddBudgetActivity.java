package com.example.katching;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.katching.util.ImageMap;

import java.util.ArrayList;
import java.util.Arrays;

public class AddBudgetActivity extends AppCompatActivity {

    String TABLE_BUDGET = "budget";

    Spinner spinner;
    Button btnAdd;
    EditText value;

    KatchingDatabase myDB = new KatchingDatabase(AddBudgetActivity.this);

    ArrayList<Double> budget_value;
    ArrayList<String> budget_type, budgetTypes;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_budget);

        budget_value = new ArrayList<>();
        budget_type = new ArrayList<>();
        budgetTypes = new ArrayList<>(Arrays.asList("Food / Beverages", "Housing / Utilities", "Transportation", "Shopping","Health / Wellness","Entertainment / Leisure","Education","Communication","Pets","Savings / Investments","Gifts / Donations","Personal / Miscellaneous"));

        storeData();
        budgetTypes.removeAll(budget_type);

        spinner = findViewById(R.id.budgetTypeSpinner);
        value = findViewById(R.id.budget_values_input);
        btnAdd = findViewById(R.id.btnAddBudget);



        ArrayAdapter adapter = new ArrayAdapter(getApplicationContext(),android.R.layout.simple_spinner_item,budgetTypes);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(adapter);

        double totalBudget = calculateTotalBudget(budget_value);


        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String budgetType = spinner.getSelectedItem().toString();
                int imageBudgetType = ImageMap.getImageResource(budgetType);
                String strValue = value.getText().toString().trim();
                if(strValue.isEmpty()) {
                    Toast.makeText(AddBudgetActivity.this, "Please fill in the value", Toast.LENGTH_SHORT).show();
                    return;
                }
                try {
                    double budgetValue = Double.parseDouble(strValue);
                    double maxBudget = 100 - totalBudget;
                    if(budgetValue >= 0 && budgetValue <= maxBudget){
                        budgetValue = Math.round(budgetValue * 100.0) / 100.0;

                        boolean success = myDB.addBudget(budgetType,budgetValue,imageBudgetType);
                        if (success) {
                            Intent i = new Intent(AddBudgetActivity.this, BudgetActivity.class);
                            startActivity(i);
                        }
                    }
                    else{
                        Toast.makeText(AddBudgetActivity.this, "Value must be bigger than 0, lesser than " + maxBudget, Toast.LENGTH_SHORT).show();
                    }

                } catch (NumberFormatException e) {
                    Toast.makeText(AddBudgetActivity.this, "Value must be a number", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    void storeData(){
        Cursor cursor = myDB.readSelectedBudgetData(TABLE_BUDGET);
        if (cursor.getCount() == 0){
            Toast.makeText(this, "No data.", Toast.LENGTH_SHORT).show();
        } else {
            while(cursor.moveToNext()){
                budget_type.add(cursor.getString(1));
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