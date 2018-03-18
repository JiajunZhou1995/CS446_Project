package com.mtr.codetrip.codetrip.Utility;

import android.database.Cursor;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by catrina on 2018-03-18.
 */

public class DataBaseUtility {

    public static List<String> getArrayFromDB(Cursor c, String columnName){
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

}
