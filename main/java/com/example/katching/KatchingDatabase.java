package com.example.katching;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

class KatchingDatabase extends SQLiteOpenHelper {
    private Context context;
    private static final String DATABASE_NAME = "Katching.db" ;
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_BUDGET = "budget", TABLE_EXPENSE = "expense", TABLE_BALANCE = "balance" , TABLE_INCOME = "income";
    private static final String COLUMN_BUDGET_ID = "b_id", COLUMN_BUDGET_TYPE = "budget_type", COLUMN_BUDGET_VALUE = "budget_value", COLUMN_BUDGET_IMG = "budget_image";
    private static final String COLUMN_EXPENSE_ID = "e_id", COLUMN_EXPENSE_TYPE = "expense_type", COLUMN_EXPENSE_NAME = "expense_name", COLUMN_EXPENSE_VALUE = "expense_value", COLUMN_EXPENSE_DATE = "expense_date", COLUMN_EXPENSE_IMG = "expense_image";
    private static final String COLUMN_BALANCE_ID = "ba_id", COLUMN_BALANCE_VALUE = "balance_value";
    private static final String COLUMN_INCOME_ID = "i_id", COLUMN_INCOME_NAME = "income_name", COLUMN_INCOME_VALUE = "income_value", COLUMN_INCOME_DATE = "income_date";

    public KatchingDatabase(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + TABLE_BUDGET +
                " (" + COLUMN_BUDGET_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_BUDGET_TYPE + " VARCHAR(100), " +
                COLUMN_BUDGET_VALUE + " DOUBLE, " +
                COLUMN_BUDGET_IMG + " INTEGER);";

        String query1 = "CREATE TABLE " + TABLE_EXPENSE +
                " (" + COLUMN_EXPENSE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_EXPENSE_NAME + " VARCHAR(100), " +
                COLUMN_EXPENSE_TYPE + " VARCHAR(100), " +
                COLUMN_EXPENSE_VALUE + " DOUBLE, " +
                COLUMN_EXPENSE_IMG + " INTEGER, " +
                COLUMN_EXPENSE_DATE + " DATE);";

        String query2 = "CREATE TABLE " + TABLE_BALANCE +
                " (" + COLUMN_BALANCE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_BALANCE_VALUE + " DOUBLE);";

        String query3 = "CREATE TABLE " + TABLE_INCOME +
                " (" + COLUMN_INCOME_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_INCOME_NAME + " VARCHAR(100), " +
                COLUMN_INCOME_VALUE + " DOUBLE, " +
                COLUMN_INCOME_DATE + " DATE);";

        db.execSQL(query);
        db.execSQL(query1);
        db.execSQL(query2);
        db.execSQL(query3);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_BUDGET);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EXPENSE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_BALANCE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_INCOME);
        onCreate(db);
    }

    boolean addBudget(String type, double value, int image){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_BUDGET_TYPE, type);
        cv.put(COLUMN_BUDGET_VALUE, value);
        cv.put(COLUMN_BUDGET_IMG, image);

        long result = db.insert(TABLE_BUDGET, null, cv);
        if(result == -1){
            Toast.makeText(context, "Failed to add Budget", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            Toast.makeText(context, "Budget added successfully", Toast.LENGTH_SHORT).show();
            return true;
        }
    }

    boolean addExpense(String type, String name, double value, int image, String date){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_EXPENSE_TYPE, type);
        cv.put(COLUMN_EXPENSE_NAME, name);
        cv.put(COLUMN_EXPENSE_VALUE, value);
        cv.put(COLUMN_EXPENSE_IMG, image);
        cv.put(COLUMN_EXPENSE_DATE, date);

        long result = db.insert(TABLE_EXPENSE, null, cv);
        if(result == -1){
            Toast.makeText(context, "Failed to add Expense", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            Toast.makeText(context, "Expense added successfully", Toast.LENGTH_SHORT).show();
            return true;
        }
    }
    boolean addIncome(String name, double value, String date){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_INCOME_NAME, name);
        cv.put(COLUMN_INCOME_VALUE, value);
        cv.put(COLUMN_INCOME_DATE, date);

        long result = db.insert(TABLE_INCOME, null, cv);
        if(result == -1){
            Toast.makeText(context, "Failed to add Income", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            Toast.makeText(context, "Income added successfully", Toast.LENGTH_SHORT).show();
            return true;
        }
    }

    Cursor readSelectedBudgetData(String tableName){
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT * FROM " + tableName;
        Cursor cursor = db.rawQuery(query, null);

        return cursor;
    }

    Cursor readSelectedExpenseData(String date){
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT * FROM " + TABLE_EXPENSE +
                " WHERE " + COLUMN_EXPENSE_DATE + " = ?";

        Cursor cursor = db.rawQuery(query, new String[]{date});

        return cursor;
    }

    Cursor readMonthlyData(String tableName,String columnName, String month, String year){
        SQLiteDatabase db = this.getReadableDatabase();

        String monthYear = year + "-" + month;
        String query;

        if(tableName.equals("income")){
            query = "SELECT * FROM " + tableName +
                " WHERE " + columnName + " LIKE ?";
        }
        else{
            query = "SELECT " + COLUMN_EXPENSE_TYPE + ", SUM(" + COLUMN_EXPENSE_VALUE + ") FROM " + tableName +
                    " WHERE " + columnName + " LIKE ?" +
                    " GROUP BY " + COLUMN_EXPENSE_TYPE;
        }

        Cursor cursor = db.rawQuery(query, new String[]{monthYear + "%"});

        return cursor;
    }

    boolean updateBudgetData(String row_id, double value){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_BUDGET_VALUE,value);

        long result = db.update(TABLE_BUDGET,cv,"b_id=?", new String[]{row_id});
        if (result == -1){
            Toast.makeText(context, "Failed to Update", Toast.LENGTH_SHORT).show();
            return false;
        }
        else{
            Toast.makeText(context, "Update Successful", Toast.LENGTH_SHORT).show();
            return true;
        }
    }

    boolean updateExpenseData(String row_id, String type, String name, double value, int image, String date){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_EXPENSE_TYPE,type);
        cv.put(COLUMN_EXPENSE_NAME,name);
        cv.put(COLUMN_EXPENSE_VALUE,value);
        cv.put(COLUMN_EXPENSE_IMG,image);
        cv.put(COLUMN_EXPENSE_DATE,date);

        long result = db.update(TABLE_EXPENSE,cv,"e_id=?", new String[]{row_id});
        if (result == -1){
            Toast.makeText(context, "Failed to update Expense", Toast.LENGTH_SHORT).show();
            return false;
        }
        else{
            Toast.makeText(context, "Update Successful", Toast.LENGTH_SHORT).show();
            return true;
        }
    }

    boolean updateIncomeData(String row_id, String name, double value, String date){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_INCOME_NAME,name);
        cv.put(COLUMN_INCOME_VALUE,value);
        cv.put(COLUMN_INCOME_DATE,date);

        long result = db.update(TABLE_INCOME,cv,"i_id=?", new String[]{row_id});
        if (result == -1){
            Toast.makeText(context, "Failed to update Income", Toast.LENGTH_SHORT).show();
            return false;
        }
        else{
            Toast.makeText(context, "Update Successful", Toast.LENGTH_SHORT).show();
            return true;
        }
    }

    boolean updateBalanceData(double value){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_BALANCE_VALUE,value);
        long result;

        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_BALANCE + " WHERE ba_id = 1", null);
        if (cursor.getCount() > 0) {
            result = db.update(TABLE_BALANCE, cv, "ba_id = ?", new String[]{"1"});
        } else {
            result = db.insert(TABLE_BALANCE, null, cv);
        }
        if (result == -1){
            Toast.makeText(context, "Failed to Update", Toast.LENGTH_SHORT).show();
            return false;
        }
        else{
            Toast.makeText(context, "Update Successful", Toast.LENGTH_SHORT).show();
            return true;
        }
    }

    boolean deleteSingleData(String tableName, String idName, String row_id){
        SQLiteDatabase db = this.getWritableDatabase();
        long result = db.delete(tableName, idName + "=?", new String[]{row_id});

        if(result == -1){
            Toast.makeText(context, "Error Deleting", Toast.LENGTH_SHORT).show();
            return false;
        }
        else{
            Toast.makeText(context, "Successfully Deleted", Toast.LENGTH_SHORT).show();
            return true;
        }
    }
}
