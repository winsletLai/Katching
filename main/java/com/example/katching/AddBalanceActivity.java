package com.example.katching;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AddBalanceActivity extends AppCompatActivity {

    String TABLE_BALANCE = "balance";
    Button btnAdd;
    EditText value_input;

    String balance_value;

    KatchingDatabase myDB = new KatchingDatabase(AddBalanceActivity.this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_balance);
        value_input = findViewById(R.id.budget_balance_input);
        btnAdd = findViewById(R.id.btnAddBalance);

        storeData();

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String strValue = value_input.getText().toString().trim();
                if(strValue.isEmpty()) {
                    Toast.makeText(AddBalanceActivity.this, "Please fill in the value", Toast.LENGTH_SHORT).show();
                    return;
                }
                try {
                    double balanceValue = Double.parseDouble(strValue);
                    if(balanceValue >= 0){
                        balanceValue = Math.round(balanceValue * 100.0) / 100.0;
                        boolean success = myDB.updateBalanceData(balanceValue);
                        if (success) {
                            Intent i = new Intent(AddBalanceActivity.this, BudgetActivity.class);
                            startActivity(i);
                        }
                    }
                    else{
                        Toast.makeText(AddBalanceActivity.this, "Value must be bigger than 0", Toast.LENGTH_SHORT).show();
                    }

                } catch (NumberFormatException e) {
                    Toast.makeText(AddBalanceActivity.this, "Value must be a number", Toast.LENGTH_SHORT).show();
                }
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
                value_input.setText(balance_value);
            }
        }
    }


}