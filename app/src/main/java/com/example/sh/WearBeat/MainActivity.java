package com.example.sh.WearBeat;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sh.WearBeat.AppInfo.AppFilter;
import com.example.sh.databasetest3.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by SH on 2015-07-29.
 * @author 이상희
 * @version 1.0
 * 이 클래스는 메인 클래스로 액티비티를 구성하는데, 어플별 진동패턴설정과 사람별진동 패턴 설정으로 연결되어 있음.
 */
public class MainActivity extends Activity implements OnClickListener {
    private static final String TAG = MainActivity.class.getSimpleName();

    private final int MENU_ALL = 0;
    private final int MENU_DOWNLOAD = 1;

    private int MENU_MODE = MENU_DOWNLOAD;

    private PackageManager pm;

    private View mLoadingContainer;
    private ListView mListView = null;
    private IAAdapter mAdapter = null;

    MySQLiteOpenHelper myhelper;
    Database db;

    VibrationPattern vib = new VibrationPattern();
    private  CharSequence[] vibrationModel = vib._vibrationModel;


   //rivate TextView txtView;
    private NotificationReceiver nReceiver;
    /** Called when the activity is first created. */

    /**
     * 액티비티 생성시에 나타나는 화면
     * @param savedInstanceState
     */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_info);


       // getActionBar().setDisplayHomeAsUpEnabled(true); //뒤로가기 버튼
        //패키지 아이템 리스트를 가져오기위한 초기값 설정
        mLoadingContainer = findViewById(R.id.loading_container);
        mListView = (ListView) findViewById(R.id.listView1);
        mAdapter = new IAAdapter(this);
        mListView.setAdapter(mAdapter);
        myhelper =  new MySQLiteOpenHelper(MainActivity.this, "package.db",null, 1);
        db = new Database(myhelper);


        //패키지 아이템 리스트를 클리시 설정 가능
        mListView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> av, View view, int position,
                                    long id) {
                // TODO Auto-generated method stub
                String app_name = ((TextView) view.findViewById(R.id.app_name)).getText().toString();
                String package_name = ((TextView) view.findViewById(R.id.app_package)).getText().toString();
                int vib =db.selectVibration(package_name);
                db.selectALL();

                Toast.makeText(MainActivity.this, "현재 진동 방법 : " + vibrationModel[vib],Toast.LENGTH_SHORT).show();
                DialogRadio(package_name);
            }
        });



        //Notification Listenr 등록
        nReceiver = new NotificationReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction("com.kpbird.nlsexample.NOTIFICATION_LISTENER_EXAMPLE");
        registerReceiver(nReceiver, filter);

        //MyPersonActivity로 링크
        Button btn_intent;
        btn_intent = (Button)findViewById(R.id.btn_intent);
        btn_intent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,MyPersonActivity.class);
                startActivity(intent);
            }
        });


    }

    /**
     * 버튼 클릭시에 사람별 진동 수를 만드는 MyPersonActivity 클래스를 생성
     * @param v 클릭되면 사람별로 실행되는 액티비티 실행
     */
    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        Intent intent = new Intent(this, MyPersonActivity.class);
        startActivity(intent);
        finish();
    }

    /**
     * 진동 패턴을 설정하기위한 라디오 다이얼버튼
     * @param pkgName 패키지이름
     */
    private void DialogRadio(final String pkgName) {
        final CharSequence[] vibModel = vibrationModel;
        AlertDialog.Builder alt_bld = new AlertDialog.Builder(this);
        alt_bld.setTitle("진동을 선택하세요");
        alt_bld.setSingleChoiceItems(vibModel, -1,
                new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int item) {
                        Toast.makeText(getApplicationContext(), "바뀐 진동방법 = " + vibModel[item], Toast.LENGTH_SHORT).show();
                        db.PkgSetVibration(pkgName, item, "OK");
                        dialog.cancel();
                    }
                });
       AlertDialog alert = alt_bld.create();
        alert.show();
       }

    /**
     * 액티비티를 지울 때
     */
    @Override
         protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(nReceiver);
    }

    /**
     * 액티비티를 재실행
     */
    @Override
    protected void onResume() {
        super.onResume();
    }


    /**
     * 앱태스크 실행
     */
    private void startTask() {
        new AppTask().execute();
    }

    /**
     * 로딩하는 화면을 구성
     * @param isView
     */
    private void setLoadingView(boolean isView) {
        if (isView) {

            mLoadingContainer.setVisibility(View.VISIBLE);
            mListView.setVisibility(View.GONE);
        } else {

            mListView.setVisibility(View.VISIBLE);
            mLoadingContainer.setVisibility(View.GONE);
        }
    }


    /**
     * 아이템, 이름, 패키지 별로 뷰를 구성함
     */
    private class ViewHolder {

        public ImageView mIcon;

        public TextView mName;
        // App Package Name
        public TextView mPacakge;
    }


    /**
     *  아이템 리스트를 구성하기위한 클래스
     */
    private class IAAdapter extends BaseAdapter {
        private Context mContext = null;

        private List<ApplicationInfo> mAppList = null;
        private ArrayList<AppInfo> mListData = new ArrayList<AppInfo>();

        public IAAdapter(Context mContext) {
            super();
            this.mContext = mContext;
        }

        public int getCount() {
            return mListData.size();
        }

        public Object getItem(int arg0) {
            return null;
        }

        public long getItemId(int arg0) {
            return 0;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;

            if (convertView == null) {
                holder = new ViewHolder();

                LayoutInflater inflater = (LayoutInflater) mContext
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.list_item_layout, null);

                holder.mIcon = (ImageView) convertView
                        .findViewById(R.id.app_icon);
                holder.mName = (TextView) convertView
                        .findViewById(R.id.app_name);
                holder.mPacakge = (TextView) convertView
                        .findViewById(R.id.app_package);

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            AppInfo data = mListData.get(position);

            if (data.mIcon != null) {
                holder.mIcon.setImageDrawable(data.mIcon);
            }

            holder.mName.setText(data.mAppNaem);
            holder.mPacakge.setText(data.mAppPackge);

            return convertView;
        }


        public void rebuild() {
            if (mAppList == null) {

                Log.d(TAG, "Is Empty Application List");

                pm = MainActivity.this.getPackageManager();


                mAppList = pm
                        .getInstalledApplications(PackageManager.GET_UNINSTALLED_PACKAGES
                                | PackageManager.GET_DISABLED_COMPONENTS);
            }

            AppFilter filter;
            switch (MENU_MODE) {
                case MENU_DOWNLOAD:
                    filter = AppInfo.THIRD_PARTY_FILTER;
                    break;
                default:
                    filter = null;
                    break;
            }

            if (filter != null) {
                filter.init();
            }


            mListData.clear();

            AppInfo addInfo = null;
            ApplicationInfo info = null;
            for (ApplicationInfo app : mAppList) {
                info = app;

                if (filter == null || filter.filterApp(info)) {

                    addInfo = new AppInfo();
                    // App Icon
                    addInfo.mIcon = app.loadIcon(pm);
                    // App Name
                    addInfo.mAppNaem = app.loadLabel(pm).toString();
                    // App Package Name
                    addInfo.mAppPackge = app.packageName;
                    mListData.add(addInfo);
                }
            }


            Collections.sort(mListData, AppInfo.ALPHA_COMPARATOR);
        }
    }

    /**
     * 앱태스크 클래스
     * @author 앱태스
     */
    private class AppTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {

            setLoadingView(true);
        }

        @Override
        protected Void doInBackground(Void... params) {

            mAdapter.rebuild();

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {

            mAdapter.notifyDataSetChanged();


            setLoadingView(false);
        }

    };

    /**
     * 메뉴설정
     * @param menu
     * @return 메뉴 설정되면 TRUE
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(0, MENU_DOWNLOAD, 1, "어플별 진동 설정"); //R.string.menu_download
        menu.add(0, MENU_ALL, 2, R.string.menu_all);

        return true;
    }

    /**
     *  메뉴를 활성 또는 비활성
     * @param menu
     * @return 변경되면 TRUE
     */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (MENU_MODE == MENU_DOWNLOAD) {
            menu.findItem(MENU_DOWNLOAD).setVisible(true);
            menu.findItem(MENU_ALL).setVisible(false);
        } else {
            menu.findItem(MENU_DOWNLOAD).setVisible(false);
            menu.findItem(MENU_ALL).setVisible(false);//전체기능 false
        }

        return true;
    }



    /**
     * 메뉴 선택하여 태스크 시작
     * @param item 선택한 아이템
     * @return 선택 되면 TRUE
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int menuId = item.getItemId();

        if (menuId == MENU_DOWNLOAD) {
            MENU_MODE = MENU_DOWNLOAD;
        } else {
            MENU_MODE = MENU_ALL;
        }

        startTask();

        return true;
    }

    /**
     * NLService로 부터 Notification Reciever를 받는 클래스
     */
    class NotificationReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {


          //  String temp = intent.getStringExtra("notification_event") + "\n" ;


        }
    }
}
