package com.example.sh.WearBeat;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.RemoteViews;

import com.example.sh.databasetest3.R;

import java.text.DateFormat;
import java.util.Date;

/**
 * @author 이상희
 * @version 1.0
 * 이 클래스는 NotificationListener를 이용하여 노티가 들어오면 패키지별, 사람, 감정, 즐겨찾기등의 우선순위를 고려하여 진동패턴이 다르게 설정한다.
 *  우선순위 : 사람별 > 즐겨찾기 이모티콘 및 감정 > 패키지별 설정 > 디폴트값
 */

public class NLService extends NotificationListenerService {


    VibrationPattern vib = new VibrationPattern();
    private String TAG = this.getClass().getSimpleName();
    private NLServiceReceiver nlservicereciver;

    MySQLiteOpenHelper myhelper;
    Database db;

    MySQLiteOpenHelperPerson myhelperperson;
    Database_person db_person;

    @Override
    public void onCreate() {
        //Notification 인텐트를 받아 리시버에게 전달.
        super.onCreate();
        nlservicereciver = new NLServiceReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction("com.kpbird.nlsexample.NOTIFICATION_LISTENER_SERVICE_EXAMPLE");
        registerReceiver(nlservicereciver, filter);

        //데이터베이스 형성

        myhelper =  new MySQLiteOpenHelper(NLService.this, "package.db",null, 1);
        db = new Database(myhelper);

        myhelperperson =new MySQLiteOpenHelperPerson(NLService.this,"person.db",null,1);
        db_person =new Database_person(myhelperperson);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(nlservicereciver);
    }

    /**
     *  노티피케이션이 게시될때 함수를 실행하여 진동패턴을 설정
     * @param sbn 노티피케이션 상태
     */
    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {


        String ticker = sbn.getNotification().tickerText.toString();
        Bundle extras = sbn.getNotification().extras;
        String title = extras.getString("android.title"); // 이름이나 메일주소가 적힘
        String text2 = extras.getCharSequence("android.text").toString(); //제목+내용


        Log.i(TAG, "**********  onNotificationPosted");
        Log.i(TAG, "ID :" + sbn.getId() + "\t" + sbn.getNotification().tickerText + "\t" + sbn.getPackageName());
        Intent i = new Intent("com.kpbird.nlsexample.NOTIFICATION_LISTENER_EXAMPLE");


        //해당 패키지가 존재하면 true
        boolean isNaver = sbn.getPackageName().equalsIgnoreCase("com.nhn.android.mail");
        boolean isGmail = sbn.getPackageName().equalsIgnoreCase("com.google.android.gm");
        boolean isKakao = sbn.getPackageName().equalsIgnoreCase("com.kakao.talk");
        boolean isMMS = sbn.getPackageName().equalsIgnoreCase("com.amdroid.mms");
        boolean isAlarm = sbn.getPackageName().equalsIgnoreCase("com.lge.clock");
        boolean isFacebook = sbn.getPackageName().equalsIgnoreCase("com.facebook.katana");
        boolean isFacebookMsg = sbn.getPackageName().equalsIgnoreCase("com.facebook.orca");
        boolean isGoogleCalendar = sbn.getPackageName().equalsIgnoreCase("com.google.android.calendar");
        boolean isGoogleFit = sbn.getPackageName().equalsIgnoreCase("com.google.android.apps.fitness");
        boolean isPhone = sbn.getPackageName().equalsIgnoreCase("com.android.phone");
        boolean isImotecorn = false;


        String output = "post"; //주소록에 없으면 이 값이 디폴트

        if (isNaver || isGmail || isKakao || isMMS || isAlarm || isFacebook || isFacebookMsg || isGoogleCalendar || isGoogleFit) { //이 중 해당되면 TRUE 디폴트 진동을 주기위함.
            NotificationCompat.Builder builder2 = new NotificationCompat.Builder(this);
            // END_INCLUDE(notificationCompat)

            // BEGIN_INCLUDE(intent)
            //Create Intent to launch this Activity again if the notification is clicked.
            Intent i2 = new Intent(this, MainActivity.class);
            i2.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                    | Intent.FLAG_ACTIVITY_CLEAR_TOP
                    | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            PendingIntent intent3 = PendingIntent.getActivity(this, 0, i2,
                    0);
            builder2.setContentIntent(intent3);
            //builder2.setTicker(getResources().getString(R.string.custom_notification));
            // Sets the small icon for the ticker
          //  builder2.setSmallIcon(R.drawable.ic_stat_custom);
            builder2.setSmallIcon(R.drawable.wearbeat_watch);
            Notification notification = builder2.build();


            int vibPattern = 0;


            //디폴트 진동 주기
            if (isGmail) {
            //    vibPattern = 17;
            } else if (isKakao) {
                isImotecorn=true;
                    vibPattern = 11;
            } else if (isNaver) {
              //  vibPattern = 15;
            } else if(isMMS){
          //      vibPattern = 14;
            }else if(isFacebook){
                vibPattern=12;
            }else if(isFacebookMsg){
                    isImotecorn=true;
                 //   vibPattern = 13;
                    vibPattern=12;
            }else if(isGoogleCalendar){
              //  vibPattern=17;
            }else if(isGoogleFit){
           //     vibPattern=8;
            }


            //패키지별 진동값이 존재하면 가져오기
            int db_assignedVibPattern=db.selectVibration(sbn.getPackageName());
            if(db_assignedVibPattern!=0){
                vibPattern=db_assignedVibPattern;
            }


            //사람별 진동값이 존재하면 가져오기
            int db_assignedVibPattern_person=db_person.selectVibration(title);
            if(db_assignedVibPattern_person!=0){
                vibPattern=db_assignedVibPattern_person;

                isImotecorn=false;
            }


          //  output= fetchContacts(title); //주소록에 등록된 사람과, 즐겨찾기 된 사람들의 결과를 가져오기 위함

        /*    if(isImotecorn &&  (output.substring(0,1).equals("#"))){ //#은 주소록에 즐겨찾기 된사람을 의미  &&  이모티콘 사용

                 if (text2.contains("^^") || text2.contains("ㅋㅋ") || text2.contains("ㅎㅎ") || text2.contains(":)") || text2.contains("(씩씩)") || text2.contains("(신나)") || text2.contains("(하하)") || text2.contains("(좋아)")) {
                    vibPattern = 2;
                }else if (text2.contains("ㅠㅠ") || text2.contains("ㅜㅜ") || text2.contains("(흑흑)") || text2.contains("(눈물)")) {
                    vibPattern = 3;
                }else if (text2.contains("ㄳ") || text2.contains("ㄱㅅ") || text2.contains("감사") ||  text2.contains("(감동)") ||  text2.contains("고마워")) {
  //                   vibPattern = 4;
                 }else if (text2.contains("ㅇㅇ") || text2.contains("ㅇㅋ") ) {
         //            vibPattern = 5;
                 }else if (text2.contains("사랑해") || text2.contains("알러뷰") || text2.contains("(사랑뿅)") || text2.contains("(하트)") || text2.contains("(뽀뽀)")) {
  //                   vibPattern = 6;
                 }else if (text2.contains("ㄴㄴ") || text2.contains("ㅡㅡ") || text2.contains("(심각)") || text2.contains("(정색)")) {
      //               vibPattern = 7;
                 }else if (text2.contains("ㅊㅋ") || text2.contains("굿") || text2.contains("(굿)") || text2.contains("추카") || text2.contains("축하")) {
        //            vibPattern = 8;
                 }else if (text2.contains("컬") || text2.contains("헉") || text2.contains("헐") || text2.contains("(헉)")) {
       //             vibPattern = 9;
                }else if (text2.contains("파이팅") || text2.contains("ㅎㅇㅌ") || text2.contains("화이팅") || text2.contains("힘내")  ) {
                   vibPattern = 10;
                }else{
                    vibPattern=1; //즐겨찾기 된사람은 기본진동값이 1번 붉은악마 응원
                }
            }else if(output.equalsIgnoreCase("post") && (isMMS ) ){  //문자메세지서비스 이고 주소록에 추가 되지 않으면 진동 안 줌..
                vibPattern=0;
            }*/



            long[] vibrate = vib.vibrationPattern(vibPattern);//진동 패턴을 가져옴


            //노티피케이션에 진동을 줌
            notification.flags |= Notification.FLAG_AUTO_CANCEL;
            notification.vibrate = vibrate;




            RemoteViews contentView = new RemoteViews(getPackageName(), R.layout.notification);

            // Set text on a TextView in the RemoteViews programmatically.
            final String time = DateFormat.getTimeInstance().format(new Date()).toString();
            final String text = "웨어비트를 즐겨봐요~";//getResources().getString(R.string.collapsed, time);
            final String novibtxt ="웨어비트를 즐겨봐요(진동x)";//getResources().getString(R.string.collapsed_no_vibration, time);

            if (vib.isVib) { //진동
                contentView.setTextViewText(R.id.textView, text);
            } else { //진동
                contentView.setTextViewText(R.id.textView, novibtxt);
            }
            notification.contentView = contentView;


            if (Build.VERSION.SDK_INT >= 16) { //확장된 노티피케이션 이미지를 줄 수 있음.
                // Inflate and set the layout for the expanded notification view
               // RemoteViews expandedView =
                //        new RemoteViews(getPackageName(), R.layout.notification_expanded);
                //notification.bigContentView = expandedView;
            }

            NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

            nm.notify(1, notification);
        }

        i.putExtra("notification_event","");
        //i.putExtra("notification_event", "onNotificationPosted :" + sbn.getPackageName() + " \n #title :  이름 :" + title + " \n #text: " + text2 + "\n" + output.substring(0, 1));
       // i.putExtra("notification_event", " title :  " + title + " text: " +text+"\n");
        sendBroadcast(i);

    }

    @Override
    public void onNotificationRemoved(StatusBarNotification sbn) {
     //노티를 지울 떄 주는 진동..값

    }


    /**
     * 브로드캐스트를 받아 노티를 탐지.
     */
    class NLServiceReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            NotificationCompat.Builder builder = new NotificationCompat.Builder(context);


        }

    }

    /**
     * 해당 사람이 주소록 데이터와 즐겨찾기에 있는지 알아보기위해 데이터를 가져오기 위한 함수
     * @param _target 찾을 사람
     * @return 주소록의 데이터를 가져옴
     */
    public String fetchContacts(String _target) {

        String phoneNumber = null;
        String email = null;
        String favorite = null;

        Uri CONTENT_URI = ContactsContract.Contacts.CONTENT_URI;
        String _ID = ContactsContract.Contacts._ID;
        String DISPLAY_NAME = ContactsContract.Contacts.DISPLAY_NAME;
        String HAS_PHONE_NUMBER = ContactsContract.Contacts.HAS_PHONE_NUMBER;

        Uri PhoneCONTENT_URI = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
        String Phone_CONTACT_ID = ContactsContract.CommonDataKinds.Phone.CONTACT_ID;
        String NUMBER = ContactsContract.CommonDataKinds.Phone.NUMBER;


        Uri EmailCONTENT_URI = ContactsContract.CommonDataKinds.Email.CONTENT_URI;
        String EmailCONTACT_ID = ContactsContract.CommonDataKinds.Email.CONTACT_ID;
        String DATA = ContactsContract.CommonDataKinds.Email.DATA;




        StringBuffer output = new StringBuffer();

        ContentResolver contentResolver = getContentResolver();

        Cursor cursor = contentResolver.query(CONTENT_URI, null, null, null, null);

        // Loop for every contact in the phone
        if (cursor.getCount() > 0) {

            while (cursor.moveToNext()) {

                String contact_id = cursor.getString(cursor.getColumnIndex(_ID));
                String name = cursor.getString(cursor.getColumnIndex(DISPLAY_NAME));

                int hasPhoneNumber = Integer.parseInt(cursor.getString(cursor.getColumnIndex(HAS_PHONE_NUMBER)));



                if (hasPhoneNumber > 0 && (name.equalsIgnoreCase(_target))) {

                    output.append("\n &First Name:" + name);

                    // Query and loop for every phone number of the contact
                    Cursor phoneCursor = contentResolver.query(PhoneCONTENT_URI, null, Phone_CONTACT_ID + " = ?", new String[]{contact_id}, null);

                    while (phoneCursor.moveToNext()) {
                        phoneNumber = phoneCursor.getString(phoneCursor.getColumnIndex(NUMBER));
                        output.append("\n &Phone number:" + phoneNumber);

                    }

                    phoneCursor.close();

                    // Query and loop for every email of the contact
                    Cursor emailCursor = contentResolver.query(EmailCONTENT_URI, null, EmailCONTACT_ID + " = ?", new String[]{contact_id}, null);

                    while (emailCursor.moveToNext()) {

                        email = emailCursor.getString(emailCursor.getColumnIndex(DATA));

                        output.append("\n &Email:" + email);

                    }

                    emailCursor.close();
                    output.append("\n");

                    Cursor Favoritecursor = getContentResolver().query(ContactsContract.Contacts.CONTENT_URI,
                            new String[]{ContactsContract.Contacts._ID, ContactsContract.Contacts.DISPLAY_NAME},
                            ContactsContract.Contacts.STARRED + " = 1", null,
                            ContactsContract.Contacts.DISPLAY_NAME + " ASC");


                    while (Favoritecursor.moveToNext()) {
                        favorite = Favoritecursor.getString(Favoritecursor.getColumnIndex(DISPLAY_NAME));
                        if(favorite.equalsIgnoreCase(_target)){
                            break;
                        }

                        output.append("\nfavorite:" + favorite);

                    }


                    Favoritecursor.close();


                    output.append("\n");



                    if (favorite.equalsIgnoreCase(_target)) {

                        return "#" + output.toString();
                    } else {
                        return output.toString();
                    }

                }


            }


        }

        return "error!!";

    }
}