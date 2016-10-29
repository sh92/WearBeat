package com.example.sh.WearBeat;

/**
 * Created by SH on 2015-07-29.
 * @author 이상희
 * @version 1.0
 * 이 클래스는 패키지별 데이터를 가져오기 위해 어플리케이션의 정보를 구성하기 위한 인터페이스와 변수를 구성하는 구조체같은 역할을 한다..
 */
import android.content.pm.ApplicationInfo;
import android.graphics.drawable.Drawable;

import java.text.Collator;
import java.util.Comparator;

public class AppInfo {

    /**
     * App을 구별하기 위한 interface
     */
    public static interface AppFilter {
        public void init();
        public boolean filterApp(ApplicationInfo info);
    }


    public Drawable mIcon = null;

    public String mAppNaem = null;

    public String mAppPackge = null;



    public static final AppFilter THIRD_PARTY_FILTER = new AppFilter() {
        public void init() {
        }

        /**
         * 어플리케이션 정볼르 가지고 옴
         * @param info 어플리케이션 정보
         * @return 어플리케이션이 업데이트 되면 true
         */
        @Override
        public boolean filterApp(ApplicationInfo info) {
            if ((info.flags & ApplicationInfo.FLAG_UPDATED_SYSTEM_APP) != 0) {
                return true;
            } else if ((info.flags & ApplicationInfo.FLAG_SYSTEM) == 0) {
                return true;
            }
            return false;
        }
    };



    public static final Comparator<AppInfo> ALPHA_COMPARATOR = new Comparator<AppInfo>() {
        private final Collator sCollator = Collator.getInstance();

        /**
         * 문자를 비교하여 반환함
         * @param object1 비교할 앱1
         * @param object2 비교할 앱2
         * @return 문자를 비교하여  크면 양수 같으면 0 작으면 음수를 리턴
         * */
        @Override
        public int compare(AppInfo object1, AppInfo object2) {
            return sCollator.compare(object1.mAppNaem, object2.mAppNaem);
        }
    };
}
