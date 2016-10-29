package com.example.sh.WearBeat;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by SH on 2015-08-01.
 * @author 이상희
 * @version 1.0
 * @since 2015-08-01
 * 이 클래스는 어플별로 진동 패턴을 주기 위해 데이터베이스를 형성하고 기존 것이 있으면 업데이트를 한다.
 */
public class MySQLiteOpenHelper extends SQLiteOpenHelper {

    public MySQLiteOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version)
    {
        super(context, name,factory,version);

    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        String sql= "create table myPkg("+
                "_id integer primary key autoincrement , "+
                "name text, "+
                "vibPattern integer, "+
                "isAssigned text);";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String sql="drop table if  exists myPkg";
        db.execSQL(sql);
        onCreate(db);
    }


}
