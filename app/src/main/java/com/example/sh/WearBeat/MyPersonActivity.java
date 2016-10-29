package com.example.sh.WearBeat;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sh.databasetest3.R;

/**
 * Created by SH on 2015-08-04.
 * @author 이상희
 * @version 1.0
 * @since 2015-08-04
 * 사람별로 패턴을 다르게 주기위한 소스로 추가/수정 및 삭제 가능하다.
 */
public class MyPersonActivity extends Activity  {



    private static final String TAG = MainActivity.class.getSimpleName();
    private View mLoadingContainer;
    private ListView mListView = null;
    private PackageManager pm;


    private Button btnClosePopup;
    private Button btnCreatePopup;
    private PopupWindow pwindo;
    private int mWidthPixels, mHeightPixels;

    MySQLiteOpenHelperPerson myhelper;
    Database_person db;


    /** Called when the activity is first created. */
    VibrationPattern vib = new VibrationPattern();
    private  CharSequence[] vibrationModel = vib._vibrationModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_person);
        Intent intent=new Intent(this.getIntent());
         mLoadingContainer = findViewById(R.id.loading_container);
        mListView = (ListView) findViewById(R.id.listView1);


        myhelper =  new MySQLiteOpenHelperPerson(MyPersonActivity.this, "person.db",null, 1);
        db = new Database_person(myhelper);

        TextView txtvew = (TextView)findViewById(R.id.addedPersonandVib);
        txtvew.setText(db.selectALL());



        Button btn_intent;
        btn_intent = (Button)findViewById(R.id.friend_btn);
        btn_intent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyPersonActivity.this, MyFriendActivity.class);
                startActivity(intent);
            }
        });
        Button btn_intent2;
        btn_intent2 = (Button)findViewById(R.id.family_btn);
        btn_intent2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyPersonActivity.this, MyFamilyActivity.class);
                startActivity(intent);
            }
        });
        Button btn_intent3;
        btn_intent3 = (Button)findViewById(R.id.job_btn);
        btn_intent3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyPersonActivity.this, MyJobActivity.class);
                startActivity(intent);
            }
        });

    }

    /**
     * 사람이름별로 진동패턴을 받기위해 패턴설정을 위한 라디오
     * @param personName 사람이름
     */
    private void DialogRadio(final String personName) {

        final CharSequence[] vibModel = vibrationModel;

        if(personName.equals("")) //공백이면 경고창
        {
            AlertDialog.Builder alert = new AlertDialog.Builder(MyPersonActivity.this);
            alert.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();     //닫기
                }
            });
            alert.setMessage("이름을 입력해주세요");
            alert.show();

        }else{


        AlertDialog.Builder alt_bld = new AlertDialog.Builder(this);

        alt_bld.setTitle("진동을 선택하세요");

        alt_bld.setSingleChoiceItems(vibModel, -1,
                new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int item) {
                        Toast.makeText(getApplicationContext(), personName + " 님이 추가되었습니다.", Toast.LENGTH_SHORT).show();
                        Toast.makeText(getApplicationContext(), "바뀐 진동방법 = " + vibModel[item], Toast.LENGTH_SHORT).show();
                        db.PersonSetVibration(personName, item, "OK");
                        dialog.cancel();
                        reflesh();
                    }
                });

        AlertDialog alert = alt_bld.create();
        alert.show();
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
    public void addPerson(View view){

        EditText text = (EditText) findViewById(R.id.EditName);
        DialogRadio(text.getText().toString());


    }

    /**
     * 사람별 설정된 진동패턴을 지움
     * @param view
     */
    public void removePerson(View view)
    {

        EditText text = (EditText) findViewById(R.id.EditName);
        if(text.getText().toString().equals(""))
        {


            AlertDialog.Builder alert = new AlertDialog.Builder(MyPersonActivity.this);
            alert.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();     //닫기
                }
            });
            alert.setMessage("이름을 입력해주세요");
            alert.show();
        }else {
            String deltedName = db.delete(text.getText().toString());
            Toast.makeText(getApplicationContext(), deltedName + " 님이 삭제되었습니다.", Toast.LENGTH_SHORT).show();
            TextView txtvew = (TextView) findViewById(R.id.addedPersonandVib);
            txtvew.setText(db.selectALL());
        }
    }
    public void reflesh(){
        TextView txtvew = (TextView)findViewById(R.id.addedPersonandVib);
        txtvew.setText(db.selectALL());
    }


}
