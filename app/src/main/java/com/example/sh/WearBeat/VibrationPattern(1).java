package com.example.sh.WearBeat;

/**
 * Created by SH on 2015-07-28.
 * @author 이상희
 * @version 1.0
 * 이 클래스는 진동 패턴을 만들고 분류하는 클래스이다.
 */
public class VibrationPattern {

    public boolean isVib = false;
    public  CharSequence[] _vibrationModel = {"0) 패턴 사용 안함","1) 붉은악마응원", "2) ㅋㄷㅋㄷ", "3) 눈물 떨어지는 소리","4) 땡~큐", "5) ㅇㅋ~","6) I Love you~","7) 노~노~","8) 빵빠레~","9) 컬~","10) 대한민국","11) 카!톡!","12) 진동4번","13) 머리어깨무릎발무릎발","14) 가위바위보","15) 아리랑~","16) 피카츄~","17) 꼬끼오~","18) 337박수","19)  똑닥똑딱","20) 똑딱똑딱(빠르게)","21) 뻔데기","22) 합죽이가 됩시다 합!","23) 두근두근","24) 3번짧게"};

    private  long[] temp;

    /**
     * 진동 패턴을 만들어내는 함수
     * @param viPattern 진동 패턴 넘버
     * @return 진동패턴이 담긴 배열
     */

    public long[]  vibrationPattern(int viPattern){



        int dot = 200; // Length of a Morse Code "dot" in milliseconds
        int short_gap = 200; // Length of Gap Between dots/dashes
        int short_gap2 = 100;
        int very_short = 20;
        int short_medium_gap=350;
        int medium_gap = 500; // Length of Gap Between Letters
        int dot2 = 100;



        //대한민국을 위한 소스
        int Dea=500;
        int han=150;
        int min=300;
        int gook=100;

        int Bam =150;
        int ba = 100;


        long[] pattern0 = {

        };
        long[] pattern1 = { 0, // Start immediately
                0,  // 응원 5박자
                1000,100,1000,100,1000,100,1000,100,1000,100,1000,100,1000,100,1000,100,1000,100,1000,100,1000 //Bam,short_gap,Bam,short_gap,ba,short_gap2,ba,short_gap2,ba,medium_gap,ba,short_gap2,ba,short_gap2,ba,short_gap2,ba,short_medium_gap,ba,short_gap2,Bam,short_medium_gap
        };

        long[] pattern2 = {
                0,  //머신건
                dot2, very_short, dot2, very_short, dot2, very_short, dot2,very_short, dot2, very_short, dot2, very_short, dot2, very_short, dot2,very_short, dot2, very_short, dot2, very_short, dot2, very_short, dot2
        };
        long[] pattern3 = {
                0,  //눈물
                300,300,250,250,220,220,200,200, 180,180,150,150,100,100,75,75,50,50,40,40,30,30,20,20,10,10,5,5,1,1

        };

        long[] pattern4 = {
                0, 600,300, 300,100 //땡 ~ 큐
        };
        long[] pattern5 = {
                0, 200,200,600,600 //ㅇㅋ~
        };
        long[] pattern6 = {
                0, 200, 200, 300,150, 200,100 //알라뷰~
        };
        long[] pattern7 = {
                0, 400, 200 , 400, 200 //노~ 노~
        };
        long[] pattern8={// 빵빠레 축하
                0, 250,100,150,50,150,50,200,600,200,100,200,100,200,100,150,80,150,80,150,80,300,100
        };
        long[] pattern9={
                //컬~
                0,1000,200
        };
        long[] pattern10={
                0,  // 대한민국.
                Dea, short_gap, han, short_gap2, min, short_gap, gook, medium_gap, dot2,short_gap2,dot,short_gap,dot2,short_gap2,dot,short_gap,dot2
        };
        long[] pattern11={
                0,  //카톡
                dot2 , short_gap2,dot, 20
        };
        long[] pattern12={ //진동 4번
                0, 100,200,100,200,100,200,100,200
        };
        long[] pattern13={//머리어꺠무릎 발 무릎 발
                0, 400,400,100,150,150,150,150,200,150,150,200,200,200,250,150,100,150,150,150
        };

        //옵션 진동
        long[] pattern14={
                0, 200,150,100,100,150,150,100,100,300,300 //가위바위보
        };

        long[] pattern15={ //아리랑
                0, 500, 300, 100,100,500, 200, 200, 400, 250,200,100,100,200,150,300,300
        };



        long[] pattern16={
            //피카츄~~
                0, 250,200,250,200,1200,1200
        };
        long[] pattern17={
                0, 200,200,150,100,500,100 //꼬끼오~
        };
        long[] pattern18={ //337박수
                0, 150,150,150,150,150,400, 150,150,150,150,150,400,150,150,150,150,150,150,150,150,150,150,150,150,150,150
        };
        long[] pattern19={ //똑딱똑딱
                0,200,800,200,800,200,800,200,800
        };
        long[] pattern20={ //똑딱똑딱 빠르게
                0,250,250,250,250,250,250,250,250
        };

        long[] pattern21={ //뻔데기
                0, 300,200,150,150,150,200, 300,200,150,150,150,200,  200,200,200,250, 100,100,100,100,100,100,100,100
        };
        long[] pattern22={ //합죽이가 됩시다 합!
                0, 150,60,150,60, 150,60,150,60, 150,60,150,60,150,150, 100,60
        };
        long[] pattern23={//두근 두근
                0, 150, 200, 200, 400,150,200,200,400
        };
        long[] pattern24={ //짧게 3번
                0, 250, 250, 250, 250, 250, 250
        };







        switch (viPattern) {
            case 0:
                temp = pattern0;
                isVib=true;
                break;
            case 1:
                temp = pattern1;
                isVib=true;
                break;
            case 2:
                temp = pattern2;
                isVib=true;
                break;
            case 3:
                temp = pattern3;
                isVib=true;
                break;
            case 4:
                temp = pattern4;
                isVib=true;
                break;
            case 5:
                temp = pattern5;
                isVib=true;
                break;
            case 6:
                temp = pattern6;
                isVib=true;
                break;
            case 7:
                temp = pattern7;
                isVib=true;
                break;
            case 8:
                temp = pattern8;
                isVib=true;
                break;
            case 9:
                temp = pattern9;
                isVib =true;
                break;
            case 10:
                temp = pattern10;
                isVib =true;
                break;
            case 11:
                temp = pattern11;
                isVib =true;
                break;
            case 12:
                temp = pattern12;
                isVib =true;
                break;
            case 13:
                temp = pattern13;
                isVib =true;
                break;
            case 14:
                temp = pattern14;
                isVib =true;
                break;
            case 15:
                temp = pattern15;
                isVib =true;
                break;
            case 16:
                temp = pattern16;
                isVib =true;
                break;
            case 17:
                temp = pattern17;
                isVib =true;
                break;
            case 18:
                temp = pattern18;
                isVib =true;
                break;
            case 19:
                temp = pattern19;
                isVib =true;
                break;
            case 20:
                temp = pattern20;
                isVib =true;
                break;
            case 21:
                temp = pattern21;
                isVib =true;
                break;
            case 22:
                temp = pattern22;
                isVib =true;
                break;
            case 23:
                temp = pattern23;
                isVib =true;
                break;
            case 24:
                temp = pattern24;
                isVib =true;
                break;

            default:
                temp = new long[]{0,0,0,0};
                isVib=false;
                System.out.println("Input is not right!");
                break;
        }
        return  temp;
    }
}


