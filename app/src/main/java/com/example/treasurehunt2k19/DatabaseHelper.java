package com.example.treasurehunt2k19;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String Database_name = "TreasureHunt.db";
    public static final String Table_name = "Clues_table";
    public static final String COL1 = "ID";
    public static final String COL2 = "hints";
    public static final String COL3 = "time";

    public DatabaseHelper(Context context) {
        super(context, Database_name, null, 1);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + Table_name + "(ID TEXT PRIMARY KEY ,hints TEXT,time TEXT)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + Table_name);

        onCreate(db);
    }

    public boolean insertData(String ID, String hint) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentvalues = new ContentValues();
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
        String time = format.format(calendar.getTime());
        contentvalues.put(COL1, ID);
        contentvalues.put(COL2, hint);
        contentvalues.put(COL3,time);
        long error = db.insert(Table_name, null, contentvalues);
        if (error == -1) return false;
        else return true;
    }

    public void reset_game(){

        SQLiteDatabase db = this.getWritableDatabase();

            ContentValues contentvalues = new ContentValues();
            Calendar calendar = Calendar.getInstance();
            SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
            String time = format.format(calendar.getTime());
            contentvalues.put(COL1, "E10");
            contentvalues.put(COL2, "Your Game is Over, You have used all your attempts");
            contentvalues.put(COL3, time);
            long error = db.insert(Table_name, null, contentvalues);



    }

    public Cursor getData() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("Select * from " + Table_name, null);
        //Cursor res = db.rawQuery("Select * from tableA",null);
        return res;

    }

    public boolean check_data(String ID) {

        boolean correct_data = false;
        SQLiteDatabase db = this.getWritableDatabase();
        char table = ID.charAt(0);
        int number = Character.getNumericValue(ID.charAt(1));
        Cursor res = db.rawQuery("Select * from "+Table_name, null);
        if (res.getCount() == 0) {
            if (number == 1) return true;
            else return false;
        }
        else {
            res.moveToLast();
            char main_entry = res.getString(0).charAt(0);
            int main_entry_number = Character.getNumericValue(res.getString(0).charAt(1));
            if (number == main_entry_number+1 && number !=6)
            {
                if(table == main_entry) return true;
            }
            // New Code
            else if(number==6 && main_entry_number == 5){
                return true;

            }
            return correct_data;
        }

    }
}
