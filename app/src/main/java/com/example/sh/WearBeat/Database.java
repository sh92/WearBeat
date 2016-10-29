package com.example.sh.WearBeat;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * Created by SH on 2015-08-01.
 * @author 이상희
 * @version 1.0
 * sqlite를 이용하여 패키지별진동을 설정하기 위한 데이터 베이스이다.
 */
public class Database {
    SQLiteDatabase db;
    MySQLiteOpenHelper helper;
    String[] columns = { "_id","name","vibPattern","isAssigned"};


    /**
     * 생성자
     * @param myhelper 데이터베이스 생성을 도와주는 객체를 파라미터로 받음
     */
    public Database(MySQLiteOpenHelper myhelper){
        helper=myhelper;
    }

    /**
     * 데이터베이스에 데이터를 삽입
     * @param _name 패키지이름
     * @param _vib 진동패턴
     * @param _isAssigned 할당되었는가를 확인하여  업데이트를 하기위해 필요한 변수
     */
    public void insert(String _name, int _vib, String _isAssigned)
    {
        db = helper.getWritableDatabase();
        ContentValues values =new ContentValues();
        values.put("name",_name);
        values.put("vibPattern", _vib);
        values.put("isAssigned",_isAssigned);
        db.insert("myPkg", null, values);
    }

    /**
     * 진동패턴을 업데이트 함
     * @param _name 패키지이름
     * @param _vib 진동패턴
     */
    public void update(String _name, int _vib)
    {
        ContentValues values = new ContentValues();
        values.put("vibPattern", _vib);
        db.update("myPkg", values, "name=?", new String[]{_name});

    }

    /**
     * 해당 패키지명으로 되어있는 것을 진동패턴을 삭제함
     * @param _name 패키지명
     */
    public void delete(String _name){
       db = helper.getWritableDatabase();
        db.delete("myPkg", "name=?", new String[]{_name});
        Log.i("db", _name + "정상적으로 삭제되었습니다");
    }

    /**
     *  데이터베이스의 값을 로그에 모두 출력(DB의 값을 확인용도)

     */
    public void selectALL()
    {
        db = helper.getReadableDatabase();
        Cursor c=db.query("myPkg", null, null, null, null, null, null);
        while (c.moveToNext()){
            int _id = c.getInt(c.getColumnIndex("_id"));
            String name = c.getString(c.getColumnIndex("name"));
            int vibPattern = c.getInt(c.getColumnIndex("vibPattern"));
            String isAssigned = c.getString(c.getColumnIndex("isAssigned"));
            Log.i("db","id : " + _id +", name : " + name + " , vibPattern :" + vibPattern + ", isAssigned : " +isAssigned);
        }

    }

    /**
     * 패키지별로 진동패턴을 설정하는데. 진동패턴이 할당되지 않았으면 삽입, 할당되어있으면 업데이트, 진동패턴 0으로 설정되면 식제를 한다.
     * @param _name 패키지명
     * @param _vib 진동 패턴
     * @param _isAssigned 할당되었는가를 확인하여  업데이트를 하기위해 필요한 변수
     */
    public void PkgSetVibration(String _name, int _vib, String _isAssigned){
        if(selectVibration(_name) == 0){
            insert(_name,_vib,_isAssigned);
        }else{
            if(_vib==0){
                delete(_name);
            }else {
                update(_name, _vib);
            }
        }
    }


    /**
     *  해당 패키지의 진동패턴값을 가져옴
     * @param pkg 패키지명
     * @return 진동패턴 값
     */
    public int selectVibration(String pkg){
        db =helper.getReadableDatabase();
        Cursor c = db.query("myPkg",columns,"name = ?" ,new String[]{pkg},null,null,null);
        while (c.moveToNext()){
            int _id = c.getInt(c.getColumnIndex("_id"));
            String name = c.getString(c.getColumnIndex("name"));
            int vibPattern = c.getInt(c.getColumnIndex("vibPattern"));
            String isAssigned = c.getString(c.getColumnIndex("isAssigned"));
            if(isAssigned.equals("OK")){
                return vibPattern;
            }
            Log.i("db","id : " + _id +", name : " + name + " , vibPattern :" + vibPattern + ", isAssigned : " +isAssigned);

        }
        return  0;
    }
}
