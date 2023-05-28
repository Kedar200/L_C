package com.example.laptopcontroller.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class dbhandler extends SQLiteOpenHelper {
    public dbhandler(Context context) {
        super(context, param.DB_NAME, null, param.DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query="CREATE TABLE " + param.TABLE_NAME + "("
                + param.KEY_ID + " INTEGER PRIMARY KEY," + param.KEY_NICKNAME
                + " TEXT, " + param.KEY_ADDRESS + " TEXT" + ")";
        Log.d("dbharry", "Query being run is : "+ query);
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public void addDevice(com.example.laptopcontroller.data.Devices Device){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(param.KEY_NICKNAME, Device.getNick_name());
        values.put(param.KEY_ADDRESS, Device.getAddress());


        db.insert(param.TABLE_NAME, null, values);

        db.close();


    }
    public List<com.example.laptopcontroller.data.Devices> getAll(){
        List<com.example.laptopcontroller.data.Devices> DeviceList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        // Generate the query to read from the database
        String select = "SELECT * FROM " + param.TABLE_NAME;
        Cursor cursor = db.rawQuery(select, null);
        //Loop through now
        if(cursor.moveToFirst()){
            do{
                com.example.laptopcontroller.data.Devices Device = new com.example.laptopcontroller.data.Devices();
                Device.setId(Integer.parseInt(cursor.getString(0)));
                Device.setNick_name(cursor.getString(1));
                Device.setAddress(cursor.getString(2));
                DeviceList.add(Device);
            }while(cursor.moveToNext());
        }
        return DeviceList;
    }

    public void deleteById(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(param.TABLE_NAME, param.KEY_ID +"=?", new String[]{String.valueOf(id)});
        db.close();
    }

}
