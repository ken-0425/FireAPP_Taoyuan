package tw.com.mygis.fireapp_taoyuan;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.StrictMode;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;

import library.JSONPost;

import static android.os.Environment.DIRECTORY_DOWNLOADS;


public class ECGtoPNG extends AppCompatActivity {
    TextView mTextView;
    File mFile = null;
    String FilePath = "BECG.xml";
    private GraphView mGraphView;
    private ArrayList<ArrayList> data00;
    private ArrayList<String> data02;
    private ArrayList<String> data03;
    private ArrayList<String> data04;
    private ArrayList<String> data05;
    private ArrayList<String> data06;
    private ArrayList<String> data07;

    private String pngName;

    private String number;

    //座標
    private String Longitude ;
    private String Latitude ;

    public static final String ECGWS12 = "00:0E:EA:CF:28:95";

    private Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.lay_ecg_png);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setLogo(R.drawable.toolbar_ic);

        GlobalVariable globalVariable = (GlobalVariable) getApplicationContext();
        number = globalVariable.FNumber;
        toolbar.setTitle(number);
        setSupportActionBar(toolbar);

        mGraphView = (GraphView) findViewById(R.id.graph);
        mGraphView.setMaxValue(10); //設定圖形數字最大值
        mTextView = (TextView) findViewById(R.id.value);



        try {
            mFile = new File(Environment.getExternalStorageDirectory() + "/" + FilePath);


            FileInputStream i2 = new FileInputStream(mFile);
            data00 = getData(i2);


            data02 = data00.get(1);
            data03 = data00.get(2);
            data04 = data00.get(3);
            data05 = data00.get(4);
            data06 = data00.get(5);
            data07 = data00.get(6);


        } catch (Exception e) {
            e.printStackTrace();
        }



        checkNetWork();


        //繪圖
        Thread t = new MyThread(); // 產生Thread物件
        t.start();


    }

    // 顯示心跳線
    private void addPoint(final float point, final float point2, final float point3, final float point4, final float point5, final float point6) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mGraphView.addDataPoint(point, point2, point3, point4, point5, point6);
            }
        });
    }

    // 顯示TextView值
    private void setText(final String str) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mTextView.setText(str);
            }
        });
    }

    public ArrayList<ArrayList> getData(InputStream xml) throws Exception {
        //XmlPullParserFactory pullPaser = XmlPullParserFactory.newInstance();
        ArrayList<ArrayList> data00 = new ArrayList<ArrayList>();


        // 创建一个xml解析的工厂
        XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
        // 获得xml解析类的引用
        XmlPullParser parser = factory.newPullParser();
        parser.setInput(xml, "UTF-8");
        // 获得事件的类型
        int eventType = parser.getEventType();
        while (eventType != XmlPullParser.END_DOCUMENT) {
            switch (eventType) {
                case XmlPullParser.START_DOCUMENT:

                    break;
                case XmlPullParser.START_TAG:
                    if ("sequence".equals(parser.getName())) {


                    } else if ("head".equals(parser.getName())) {
                        String time = parser.getAttributeValue(null, "value");
                        Toast.makeText(getApplicationContext(), time, Toast.LENGTH_SHORT).show();

                        ArrayList<String> data01 = new ArrayList<String>();
                        data01.add(time);

                        data00.add(data01);
                    } else if ("digits".equals(parser.getName())) {
                        String value = parser.nextText();// 获取该节点的内容
                        ArrayList<String> data01 = new ArrayList<String>();

                        final String[] splitValue = value.split(" "); // 將字串str2以空白分割,其結果存在splittedStr2字串陣列中

                        for (String s : splitValue) {
                            data01.add(s);
                        }

                        data00.add(data01);
                    }
                    break;
                case XmlPullParser.END_TAG:
                    if ("sequence".equals(parser.getName())) {

                        //  data = null;
                    }
                    break;
            }
            eventType = parser.next();
        }

        return data00;
    }

    class MyThread extends Thread {

        public void run() {
            super.run();
            try {
                /*
                do {
                    java.util.Random rnd = new java.util.Random(
                            System.currentTimeMillis()); // 取亂數
                    float mInput = new Float(rnd.nextInt(1000) + 0) / 100; // 取0~10小數兩位亂數值
                    final float reading = mInput; // mInput偵測到的接收值，本程式使用亂數模擬。
                    addPoint(reading); // 顯示心跳線
                    setText(Float.toString((reading))); // 顯示TextView值
                    sleep(100); // 暫停100ms
                } while (ECGtoPNG.MyThread.interrupted() == false);
                */

                for (int a = 0; a < data02.size(); a++) {

                    java.util.Random rnd = new java.util.Random(
                            System.currentTimeMillis()); // 取亂數
                    float mInput = new Float(rnd.nextInt(1000) + 0) / 100; // 取0~10小數兩位亂數值
                    final float reading = mInput; // mInput偵測到的接收值，本程式使用亂數模擬。


                    float reading2 = Float.parseFloat(data02.get(a));
                    float reading3 = Float.parseFloat(data03.get(a));
                    float reading4 = Float.parseFloat(data04.get(a));
                    float reading5 = Float.parseFloat(data05.get(a));
                    float reading6 = Float.parseFloat(data06.get(a));
                    float reading7 = Float.parseFloat(data07.get(a));
                    reading2 = reading2 / 1000 + 6;
                    reading3 = reading3 / 1000 + 5;
                    reading4 = reading4 / 1000 + 4;
                    reading5 = reading5 / 1000 + 3;
                    reading6 = reading6 / 1000 + 2;
                    reading7 = reading7 / 1000 + 1;
                    addPoint(reading2, reading3, reading4, reading5, reading6, reading7); // 顯示心跳線

                }

                GlobalVariable globalVariable = (GlobalVariable)getApplicationContext();
                String number = globalVariable.FNumber;

                File sdCardDir = Environment.getExternalStoragePublicDirectory(DIRECTORY_DOWNLOADS);//獲取SDCard目錄
                String sdDir = sdCardDir.toString();//轉字串

                String SdDirName= number+".png";
                 if(SdDirName.equals("Null")){

                 }else {

                     File saveFile = new File(sdCardDir, SdDirName);
                     FileOutputStream fos = new FileOutputStream(saveFile);
                     Bitmap bp = mGraphView.getmBitmap();
                     // 將 Bitmap 儲存成 PNG / JPEG 檔案格式
                     bp.compress(Bitmap.CompressFormat.PNG, 100, fos);

                     // 釋放
                     fos.close();
                 }


/*
                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {

                        new AlertDialog.Builder(ECGtoPNG.this)
                                .setTitle("心電圖已結束")
                                .setMessage("是否要繼續使用心電圖")
                                .setPositiveButton("NO", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        startActivity(new Intent(ECGtoPNG.this, MainActivity.class));
                                        finish();
                                    }
                                })

                                .setNeutralButton("YES", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent intent = new Intent(ECGtoPNG.this, BleECG.class);
                                        intent.putExtra("DEVICE_ADDRESS",ECGWS12);
                                        startActivity(intent);
                                        finish();
                                    }
                                })
                                .show();
                    }
                });


*/

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    public String fileIsExists(String sdDir){
        String dirname="Null";
        try{
            boolean exist=true;
            int a=1;

            while (exist){

              dirname ="ecg"+a+".png";

                File f=new File(sdDir+"/"+dirname);
                if(!f.exists()){
                    exist= false;
                }
                a++;
            }


        }catch (Exception e) {
            // TODO: handle exception

        }
        return dirname;
    }

    //上傳部分
    public void jsonValueup(ArrayList<String> value1, ArrayList<String> value2, ArrayList<String> value3,
                            ArrayList<String> value4, ArrayList<String> value5, ArrayList<String> value6) {

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);


        JSONPost jsonPost = new JSONPost();

        Log.d("Button", "Login");
        String info="0";


        JSONObject json = jsonPost.uploadECG(number, value1,value2,value3,value4,value5,value6, info);



        try {

            if (json.getString("KEY_SUCCESS") != null) {
                //        loginErrorMsg.setText("");
                String res = json.getString("KEY_SUCCESS");
                if (res.equals("true")) {


                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {

                            //跳出提示視窗
                            dialog = ProgressDialog.show(ECGtoPNG.this,
                                    "上傳中", "結束後會自動返回", true);
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        Thread.sleep(3000);
                                        Intent   intent = new Intent();
                                        intent.setClass(ECGtoPNG.this, MainActivity.class);
                                        startActivity(intent);
                                        finish();

                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    } finally {
                                        dialog.dismiss();
                                    }
                                }
                            }).start();

                        }
                    });

                } else {
                    // Error in login
                    //   loginErrorMsg.setText("Incorrect username/password");
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    //檢查網路
    private void checkNetWork() {
        ConnectivityManager mConnectivityManager =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();

        if (mNetworkInfo != null) {
            //網路是否已連線
            mNetworkInfo.isConnected();

            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException e) {
                    }

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {


                            //上傳
                            jsonValueup(data02,data03,data04,data05,data06,data07);


                        }
                    });
                }
            }).start();



        } else {
            new android.app.AlertDialog.Builder(this).setMessage("沒有網路")
                    .setTitle("請開啟網路連線功能")
                    .setCancelable(false)
                    .setPositiveButton("確定",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,int id) {
                                    Intent   intent = new Intent();
                                    intent.setClass(ECGtoPNG.this, MainActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                            })
                    .show();

        }
    }




}
