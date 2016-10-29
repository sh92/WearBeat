package com.example.sh.WearBeat;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by SH on 2015-08-04.
 * @author 이상희
 * @version 1.0
 * @since 2015-08-04
 * 이 프로그램은 sqlite를 이용하여 사람별 진동패턴을 주기위해 만들어진 소스로 데이터베이스 생성을 하고 기존에 데이터베이스가 존재할경우 업그레이드를 한다.
 */
public class MySQLiteOpenHelperPerson extends SQLiteOpenHelper {
    public MySQLiteOpenHelperPerson(Context context, String name, SQLiteDatabase.CursorFactory factory, int version)
    {
        super(context, name,factory,version);

    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        String sql= "create table myPerson("+
                "_id integer primary key autoincrement , "+
                "name text unique, "+
                "vibPattern integer, "+
                "isAssigned text);";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String sql="drop table if  exists myPerson";
        db.execSQL(sql);
        onCreate(db);
    }


}
