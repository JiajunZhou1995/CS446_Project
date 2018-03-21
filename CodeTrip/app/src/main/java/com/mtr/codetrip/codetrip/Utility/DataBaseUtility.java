package com.mtr.codetrip.codetrip.Utility;

import android.database.Cursor;

import com.mtr.codetrip.codetrip.MainActivity;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by catrina on 2018-03-18.
 */

public class DataBaseUtility {

    public static List<String> getStrArrayFromDB(Cursor c, String columnName){
        List<String> strings = new ArrayList<>();
        try{
            JSONArray jsArr = new JSONArray(c.getString(c.getColumnIndex(columnName)));
            for(int i = 0; i < jsArr.length(); i++){
                strings.add(jsArr.getString(i));
            }
        }
        catch (JSONException e){
            e.printStackTrace();
        }
        return strings;
    }

//    public static List<Integer> getIntArrayFromDB(Cursor c, String columnName){
//        List<Integer> integers = new ArrayList<>();
//        try{
//            JSONArray jsArr = new JSONArray(c.getInt(c.getColumnIndex(columnName)));
//            for(int i = 0; i < jsArr.length(); i++){
//                integers.add(jsArr.getInt(i));
//            }
//        }
//        catch (JSONException e){
//            e.printStackTrace();
//        }
//        return integers;
//    }

    static List<Integer> getIntArrayFromDB(String constrain, String columnName){
        Cursor cursor = MainActivity.appDB.rawQuery(constrain,null);
        cursor.moveToFirst();
        List<Integer> integers = new ArrayList<>();
        try{
            JSONArray jsArr = new JSONArray(cursor.getInt(cursor.getColumnIndex(columnName)));
            for(int i = 0; i < jsArr.length(); i++){
                integers.add(jsArr.getInt(i));
            }
        }
        catch (JSONException e){
            e.printStackTrace();
        }
        return integers;
    }
}
