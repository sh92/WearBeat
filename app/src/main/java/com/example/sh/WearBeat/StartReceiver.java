package com.example.sh.WearBeat;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by SH on 2015-07-30.
 * @author 이상희
 * @version 1.0
 * 이 클래스는 스마트폰이 부팅할 시에 AppInfoActivity가 자동으로 백그라운드에서 실행되도록 하는 기능을 가진다.
 */
public class StartReceiver extends BroadcastReceiver
{
    @Override
    public void onReceive(Context context, Intent intent){
        String action = intent.getAction();
        if(action.equals("android.intetn.action.BOOT_COMPLETE")){
            Intent i = new Intent(context,MainActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(i);
        }
    }
}
