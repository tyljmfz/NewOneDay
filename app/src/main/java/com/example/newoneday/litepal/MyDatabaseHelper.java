
package com.example.newoneday.litepal;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.Context;
import android.widget.Toast;

public class MyDatabaseHelper extends SQLiteOpenHelper {

    public static final String CREATE_USER = "create table User (" +
            "id integer primary key autoincrement," +
            "nickname text," +
            "password text)";

//    public static final String CREATE_GOAL = "create table Goal("+
//            "id integer primary key autoincrement," +
//            "user_id foreign key," +
//            "name text," +
//            "startdate text," +
//            "enddate text)";


    private Context mcontext;
    public MyDatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version){
        super(context,name,factory,version);
        mcontext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        db.execSQL(CREATE_USER);
        Toast.makeText(mcontext, "create succeeded", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){}


}
