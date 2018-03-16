package tw.com.mygis.fireapp_taoyuan;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import library.JSONPost;

/**
 * Created by Bart on 2014/8/23.
 */
public class Login extends AppCompatActivity implements OnClickListener {

    //全域變數
    private GlobalVariable gv;
    private AlertDialog.Builder dialog;
    private Bundle bundle = new Bundle();

    //網址
    private String url;

    private boolean SCarID = true;

    private String ExistCase;
    private Button btn_CDelete, btn_Modify, btn_CarID, btn_Get, btn_Try;
    private TextView L_Title, tv_CDelete;
    private FrameLayout fl_CarID, fl_Case;
    private Spinner sp_CarID1, sp_CarID2;
    private ListView lv_Case;
    private ArrayList<HashMap<String,String>> listItem = new ArrayList<HashMap<String,String>>();

    private String[] sp_CarIDNull_item = new String[]{""};
    //private String[] sp_Car1_item = new String[]{"","第一大隊","第二大隊","第三大隊","第四大隊"};
    private String[] sp_Car1_item;
    private String[] sp_Car2_item;


    private  ArrayList<ArrayList<String>> items = new ArrayList<ArrayList<String>>();

    private ArrayList<String[]> Case =new ArrayList<String[]>();

    private ArrayAdapter<String> adapter_CarID2;//下拉是選單

    private SharedPreferences remdname;//呼號記憶

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        controls();
        gv.Initialization();

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm); //先取得螢幕解析度
        final GlobalVariable gv= (GlobalVariable) getApplicationContext();
        gv.screenWidth = dm.widthPixels;
        gv.screenHeight = dm.heightPixels;

        btn_CDelete.setOnClickListener(this);
        btn_Modify.setOnClickListener(this);
        btn_CarID.setOnClickListener(this);
        btn_Get.setOnClickListener(this);
        btn_Try.setOnClickListener(this);
        tv_CDelete.setOnClickListener(this);


        SharedPreferences settings = getSharedPreferences(
                "remdname", 0);

        gv.CarID = settings.getString("CarID", "");

        if(!gv.CarID.equals("")){
            L_Title.setText(gv.CarID);
            btn_CarID.setText("切換分隊");
            fl_CarID.setVisibility(View.GONE);
            fl_Case.setVisibility(View.VISIBLE);
            SCarID = false;
        }

        SQLite SQLite = new SQLite(Login.this);
        SQLiteDatabase DB = SQLite.getWritableDatabase();
        Cursor c = null;
        String[] colum = { "id", "CaseID","CaseSubType","DispatchTime", "ReportPlace", "DispVehicleID", "Coordinate" };
        c = DB.query("MainCases", colum, null, null, null, null, "id ASC");
        if (c.getCount() > 0) {
            //移動到第一筆
            c.moveToFirst();
            for (int i = 0; i < c.getCount(); i++) {
                HashMap<String,String> item = new HashMap<String,String>();
                item.put("id", c.getString(0));
                item.put("CaseID", c.getString(1));
                item.put("CaseSubType", c.getString(2));
                item.put("DispatchTime", c.getString(3));
                item.put("ReportPlace", c.getString(4));
                item.put("DispVehicleID", c.getString(5));
                item.put("Coordinate", c.getString(6));
                listItem.add( item );
                c.moveToNext();
            }
            AdapterCase Btnadapter = new AdapterCase(
                    Login.this,
                    listItem,
                    R.layout.listview_case,
                    new String[] {"CaseID", "ReportPlace"},
                    new int[] {R.id.tv_CaseID,R.id.tv_Address,R.id.btn_Delete}
            );
            lv_Case.setAdapter(Btnadapter);
        }
        SQLite.close();
        DB.close();
        c.close();

        lv_Case.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                HashMap<String, String> data = (HashMap<String, String>) arg0.getItemAtPosition(arg2);

                bundle.putString("CaseID", data.get("CaseID").toString());
                bundle.putString("CaseSubType", data.get("CaseSubType").toString());
                bundle.putString("DispatchTime", data.get("DispatchTime").toString());
                bundle.putString("ReportPlace", data.get("ReportPlace").toString());
                bundle.putString("DispVehicleID", data.get("DispVehicleID").toString());
                bundle.putString("Coordinate", data.get("Coordinate").toString());
                bundle.putString("Name", data.get("Name").toString());
                bundle.putString("Gender", data.get("Gender").toString());
                bundle.putString("Personal_ID", data.get("Personal_ID").toString());
                bundle.putString("Ages", data.get("Ages").toString());
                bundle.putString("Feel_Uncomfortable", data.get("Feel_Uncomfortable").toString());
                bundle.putString("Uncomfortable_Feeling", data.get("Uncomfortable_Feeling").toString());
                bundle.putString("Uncomfortable_Time", data.get("Uncomfortable_Time").toString());
                bundle.putString("Other_Uncomfortable", data.get("Other_Uncomfortable").toString());




                gv.FNumber = data.get("CaseID").toString();
                gv.LoadPDF = data.get("CaseSubType").toString();
                gv.Date = data.get("DispatchTime").toString().substring(0, 10);
                gv.LocationHappen = data.get("ReportPlace").toString();
                gv.AttendanceUnit = data.get("DispVehicleID").toString();
                gv.Coordinate = data.get("Coordinate").toString();
                gv.Car_Index = data.get("Car_Index").toString();
                gv.Name = data.get("Name").toString();
                gv.Sex = data.get("Gender").toString();
                gv.Phone = data.get("Phone").toString();
                gv.ID = data.get("Personal_ID").toString();
                gv.Age = data.get("Ages").toString();
                gv.Q1 = data.get("Feel_Uncomfortable").toString();
                gv.Q2 = data.get("Uncomfortable_Feeling").toString();
                gv.Q3 = data.get("Uncomfortable_Time").toString();
                gv.Q4 = data.get("Other_Uncomfortable").toString();


                SQLite SQLite = new SQLite(Login.this);
                SQLiteDatabase DB = SQLite.getWritableDatabase();
                String[] colum = {"id"};
                Cursor c = DB.query("MainTab1", colum, "CaseID=?", new String[]{data.get("CaseID").toString()}, null, null, "id ASC");
                if (c.getCount() > 0) {
                    dialog = new AlertDialog.Builder(Login.this);
                    dialog.setTitle("提示"); //設定dialog 的title顯示內容
                    dialog.setMessage("是否繼續未完成的資料?");
                    dialog.setCancelable(false); //關閉 Android 系統的主要功能鍵(menu,home等...)
                    dialog.setPositiveButton("是", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            SQLTrue();

                            Intent intent = new Intent();
                            intent.setClass(Login.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    });
                    dialog.setNegativeButton("否", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            SQLFalse();
                            gv.NewTable(1);

                            Intent intent = new Intent();
                            intent.setClass(Login.this, MainActivity.class);
                            intent.putExtras(bundle);
                            startActivity(intent);
                            finish();
                        }
                    });
                    dialog.show();
                } else {
                    gv.NewTable(1);

                    Intent intent = new Intent();
                    intent.setClass(Login.this, MainActivity.class);
                    intent.putExtras(bundle);
                    startActivity(intent);
                    finish();
                }
                SQLite.close();
                DB.close();
                c.close();
            }
        });

    }

    /**
     *  控制項設定
     */
    private void controls() {
        url = this.getString(R.string.web);
        gv = (GlobalVariable) getApplicationContext();

        TextView tv_Version = (TextView) findViewById(R.id.tv_Version);
        String Version = "1.0";
        try {
            PackageInfo pInfo = this.getPackageManager().getPackageInfo(Login.this.getPackageName(), 0);
            Version = pInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        tv_Version.setText(tv_Version.getText() + " V" + Version);

        btn_CDelete = (Button) findViewById(R.id.btn_CDelete);
        btn_Modify = (Button) findViewById(R.id.btn_Modify);
        btn_CarID = (Button) findViewById(R.id.btn_CarID);
        btn_Get = (Button) findViewById(R.id.btn_Get);
        btn_Try = (Button) findViewById(R.id.btn_Try);
        L_Title = (TextView) findViewById(R.id.L_Title);
        tv_CDelete = (TextView) findViewById(R.id.tv_CDelete);
        fl_CarID = (FrameLayout) findViewById(R.id.fl_CarID);
        fl_Case = (FrameLayout) findViewById(R.id.fl_Case);
        sp_CarID1 = (Spinner) findViewById(R.id.sp_CarID1);
        sp_CarID2 = (Spinner) findViewById(R.id.sp_CarID2);

        lv_Case = (ListView) findViewById(R.id.lv_Case);

        //取得大隊資料
        if(checkNetWork()){
            getTeam();
        }
        ArrayAdapter<String> adapter;
        if(sp_Car1_item!=null){
         adapter = new ArrayAdapter<String>(this,R.layout.spinner_style,R.id.txtvwSpinner,sp_Car1_item);
            adapter.setDropDownViewResource(R.layout.spinner_dropdown_style);
            sp_CarID1.setAdapter(adapter);
        }else {
            adapter = new ArrayAdapter<String>(this,R.layout.spinner_style,R.id.txtvwSpinner,sp_CarIDNull_item);
            adapter.setDropDownViewResource(R.layout.spinner_dropdown_style);
            sp_CarID1.setAdapter(adapter);
        }


        adapter = new ArrayAdapter<String>(this,R.layout.spinner_style,R.id.txtvwSpinner,sp_CarIDNull_item);
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_style);
        sp_CarID2.setAdapter(adapter);

        sp_CarID1.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            public void onItemSelected(AdapterView adapterView, View view, int position, long id) {
                if (adapterView.getSelectedItemPosition() != 0) {
                    sp_CarID2.setAdapter(new ArrayAdapter<String>(Login.this, R.layout.spinner_style, R.id.txtvwSpinner, sp_CarIDNull_item));
                 //   new Thread(R_SPCAR2).start();
                    new Thread(R_SPCAR2_NEW).start();
                }
            }

            public void onNothingSelected(AdapterView arg0) {

            }
        });


    }

    private void GetCase() {
        btn_Get.setText("接收中...");
        btn_Get.setEnabled(false);
        lv_Case.setEnabled(false);
        ExistCase = "";
        SQLite SQLite = new SQLite(Login.this);
        SQLiteDatabase DB = SQLite.getWritableDatabase();
        Cursor c = null;
        String[] colum = { "CaseID"};
        c = DB.query("MainCases", colum, null, null, null, null, "id ASC");
        if (c.getCount() > 0) {
            //移動到第一筆
            c.moveToFirst();
            for (int i = 0; i < c.getCount(); i++) {
                ExistCase += c.getString(0) + ",";
                c.moveToNext();
            }
            ExistCase = ExistCase.substring(0, ExistCase.length() - 1);
        }
        SQLite.close();
        DB.close();
        c.close();
        //第一版
     //   new Thread(R_GetCase).start();
        //第二版
        if(checkNetWork()){
            new Thread(R_GetCase_NEW).start();
        }

    }

    private void SQLTrue() {

        gv.Modify_Tab1 = true;
        gv.Modify_Tab2 = true;
        gv.Modify_Tab3 = true;
        gv.Modify_Tab4 = true;
        gv.Modify_Tab5 = true;

        SQLite SQLite = new SQLite(Login.this);
        SQLiteDatabase DB = SQLite.getWritableDatabase();
        String[] colum = { "Subno" };
        String[] parameter = {gv.FNumber};
        Cursor c = DB.query("MainTab1", colum, "CaseID=?", parameter, null, null, "id ASC");
        if (c.getCount() > 0) {
            //移動到第一筆
            c.moveToFirst();
            gv.DrawerItemID = String.valueOf(c.getInt(0));
        }
        SQLite.close();
        DB.close();
        c.close();

        gv.SwitchingForm();
    }

    private void SQLFalse() {
        SQLite SQLite = new SQLite(Login.this);
        SQLiteDatabase DB = SQLite.getWritableDatabase();
        DB.delete("MainTab1", "CaseID=?", new String[]{gv.FNumber});
        DB.delete("MainTab2", "CaseID=?", new String[]{gv.FNumber});
        DB.delete("MainTab3", "CaseID=?", new String[]{gv.FNumber});
        DB.delete("MainTab4", "CaseID=?", new String[]{gv.FNumber});
        DB.delete("MainTab5", "CaseID=?", new String[]{gv.FNumber});
        DB.delete("MainHandWrite", "CaseID=?", new String[]{gv.FNumber});

        SQLite.close();
        DB.close();
    }


    public void getTeam(){
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        // check for login response
        try {
            JSONPost jsonPost = new JSONPost();
            JSONObject json = jsonPost.R_SPCAR2();

            JSONObject jarray =json.getJSONObject("datas");

            JSONArray  arrayBrigade= jarray.getJSONArray("Brigade");
            int size=arrayBrigade.length()+1;
            sp_Car1_item = new String[size];
            sp_Car1_item[0]="";//前面多放一個空白欄位
            for(int m=1;m<size;m++) {
                sp_Car1_item[m]=  arrayBrigade.get(m-1).toString();
            }

            JSONArray  arrayHospital= jarray.getJSONArray("Hospital");
            gv.Hospital = new String[arrayHospital.length()];
            for(int m=0;m<arrayHospital.length();m++) {
                gv.Hospital[m]=  arrayHospital.get(m).toString();
            }


            JSONArray  json2= jarray.getJSONArray("Team");
            for(int n = 0; n < json2.length(); n++) {
                JSONArray json3 = json2.getJSONArray(n);
                ArrayList<String> TeamPutData =new ArrayList<>();
                for(int m=0;m<json3.length();m++){
                    TeamPutData.add(json3.get(m).toString());
                }
                items.add(TeamPutData);

            }
            if (json.getString("KEY_SUCCESS") != null) {

                String res = json.getString("KEY_SUCCESS");
                if (res.equals("true")) {

                } else {
                    // Error in login
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void onClick(View v) {
        if (v == btn_CDelete || v == tv_CDelete) {
            dialog = new AlertDialog.Builder(Login.this);
            dialog.setTitle("提示"); //設定dialog 的title顯示內容
            dialog.setMessage("是否清空所有案件資料?");
            dialog.setCancelable(false); //關閉 Android 系統的主要功能鍵(menu,home等...)
            dialog.setPositiveButton("是", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    SQLite SQLite = new SQLite(Login.this);
                    SQLiteDatabase DB = SQLite.getWritableDatabase();
                    DB.delete("MainCases", null, null);
                    DB.delete("MainTab1", null, null);
                    DB.delete("MainTab2", null, null);
                    DB.delete("MainTab3", null, null);
                    DB.delete("MainTab4", null, null);
                    DB.delete("MainTab5", null, null);
                    DB.delete("MainHandWrite", null, null);

                    SQLite.close();
                    DB.close();
                    lv_Case.setAdapter(null);
                    listItem = new ArrayList<HashMap<String,String>>();
                }
            });
            dialog.setNegativeButton("否", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            dialog.show();
        }
        else if (v == btn_Modify) {
            if (!sp_CarID1.getSelectedItem().toString().equals("") & !sp_CarID2.getSelectedItem().toString().equals("")) {
                gv.CarID = sp_CarID2.getSelectedItem().toString();
                L_Title.setText(gv.CarID);



                SharedPreferences settings = getSharedPreferences(
                        "remdname", 0);

                settings.edit()
                        .putString("CarID", gv.CarID)
                        .commit();


                btn_CarID.setText("切換分隊");
                fl_CarID.setVisibility(View.GONE);
                fl_Case.setVisibility(View.VISIBLE);
                SCarID = false;
                GetCase();
            }
            else {
                Toast.makeText(this, "請選擇車輛!!", Toast.LENGTH_LONG).show();
            }
        } else if (v == btn_CarID) {
            if(!SCarID) {
                btn_CarID.setText("返回案件");
                fl_CarID.setVisibility(View.VISIBLE);
                fl_Case.setVisibility(View.GONE);
                SCarID = true;
            }
            else{
                btn_CarID.setText("切換分隊");
                fl_CarID.setVisibility(View.GONE);
                fl_Case.setVisibility(View.VISIBLE);
                SCarID = false;
            }
        }
        else if (v == btn_Get) {
            GetCase();
        }
        else if (v == btn_Try) {
            SQLite SQLite = new SQLite(Login.this);
            SQLiteDatabase DB = SQLite.getWritableDatabase();
            String[] colum = {"id"};
            Cursor c = DB.query("MainTab1", colum, "CaseID=?", new String[]{""}, null, null, "id ASC");
            if (c.getCount() > 0) {
                dialog = new AlertDialog.Builder(Login.this);
                dialog.setTitle("提示"); //設定dialog 的title顯示內容
                dialog.setMessage("是否繼續未完成的資料?");
                dialog.setCancelable(false); //關閉 Android 系統的主要功能鍵(menu,home等...)
                dialog.setPositiveButton("是", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        SQLTrue();

                        Intent intent = new Intent();
                        intent.setClass(Login.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                });
                dialog.setNegativeButton("否", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        SQLFalse();
                        gv.NewTable(1);

                        Intent intent = new Intent();
                        intent.setClass(Login.this, MainActivity.class);
                        intent.putExtras(bundle);
                        startActivity(intent);
                        finish();
                    }
                });
                dialog.show();
            } else {
                gv.NewTable(1);

                Intent intent = new Intent();
                intent.setClass(Login.this, MainActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
                finish();
            }
            SQLite.close();
            DB.close();
            c.close();
        }
    }

    Runnable R_SPCAR2 = new Runnable(){
        @Override
        public void run() {
            // TODO: http request.
            Message msg = new Message();
            Bundle data = new Bundle();
            HttpPost httppost = new HttpPost( url + "APP_Car2.aspx" ) ; //宣告要使用的頁面
            List<NameValuePair> params = new ArrayList<NameValuePair>( ) ; //宣告參數清單
            params.add (new BasicNameValuePair( "Team", sp_CarID1.getSelectedItem().toString()));

            String str = gv.GetResponse(httppost, params);


            if(str.equals("Overtime")){
                //btn_text = "連線逾時";
            }
            else if(str.equals("Null")){
                //btn_text = "尚無派遣";
            }
            else if(str.equals("Error")){
                //btn_text = "異常狀態";
            }
            else {
                data.putString("value", str);
                msg.setData(data);
                H_SPCAR2.sendMessage(msg);
            }
        }
    };

        //第二版
    Runnable R_SPCAR2_NEW = new Runnable(){
        @Override
        public void run() {


            gv.CarID01=sp_CarID1.getSelectedItem().toString();
            Message msg = new Message();
            H_SPCAR2.sendMessage(msg);

        }
    };

    Handler H_SPCAR2 = new Handler()
    {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle data = msg.getData();
            String str = data.getString("value");

            /*
            //第一版
           String[] info = str.split(",");
            sp_Car2_item = new String[info.length + 1];
            sp_Car2_item[0] = "";
            for(int i = 0; i < info.length; i++){
                sp_Car2_item[i + 1] = info[i];
            }
        */

            //第二版
           ArrayList<String>  item = items.get(sp_CarID1.getSelectedItemPosition()-1);
            sp_Car2_item = new String[item.size() + 1];
            sp_Car2_item[0] = "";
            for(int i = 0; i < item.size(); i++){
                sp_Car2_item[i + 1] = item.get(i);
            }
           //---------------------------------------------


            adapter_CarID2 = new ArrayAdapter<String>(Login.this,R.layout.spinner_style,R.id.txtvwSpinner,sp_Car2_item);
            adapter_CarID2.setDropDownViewResource(R.layout.spinner_dropdown_style);
            sp_CarID2.setAdapter(adapter_CarID2);
        }
    };


    Runnable R_GetCase = new Runnable(){
        @Override
        public void run() {
            // TODO: http request.
            Message msg = new Message();
            Bundle data = new Bundle();
            HttpPost httppost = new HttpPost( url + "APP_GetCase.aspx" ) ; //宣告要使用的頁面
            List<NameValuePair> params = new ArrayList<NameValuePair>( ) ; //宣告參數清單
            params.add(new BasicNameValuePair("GroupType", L_Title.getText().toString()));
            params.add(new BasicNameValuePair("CS_NO", ExistCase));

            String str = gv.GetResponse(httppost, params);

            data.putString("value", str);
            msg.setData(data);
            H_GetCase.sendMessage(msg);
        }
    };

    //第二版
    Runnable R_GetCase_NEW = new Runnable(){
        @Override
        public void run() {

            String str = null;
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);

            // check for login response
            try {
                JSONPost jsonPost = new JSONPost();
                JSONObject json = jsonPost.R_GetCase(sp_CarID1.getSelectedItem().toString(),L_Title.getText().toString());


                JSONArray jarray =new JSONArray(json.getString("datas"));


                for(int n = 0; n < jarray.length(); n++) {
                    JSONObject json2 = jarray.getJSONObject(n);


                    String jsonArrayTime = json2.getString("Date");
                    Date dt=new Date(Long.parseLong(jsonArrayTime));//UnixTime毫秒
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");


                    String[] case2 = new String[16];
                    String[] Case_Reason2=new String[]{"創傷","非創傷","臨時勤務","路倒","急病","車禍","其他"};
                    String[] Case_Gender=new String[]{"女","男"};

                    case2[0] = json2.getString("Case_Number");
                    int a =Integer.parseInt(json2.getString("Case_Reason"));
                    case2[1] = Case_Reason2[a];
                    case2[2]= sdf.format(dt);
                    case2[3] = json2.getString("Location");
                    case2[4] = json2.getString("Attendance");
                    case2[5]= json2.getString("Coordinate");
                    case2[6]= json2.getString("Car_Index");
                    case2[7]= json2.getString("Name");
                    int b =Integer.parseInt(json2.getString("Gender"));
                    case2[8]= Case_Gender[b];
                    case2[9]= json2.getString("Phone");
                    case2[10]= json2.getString("Personal_ID");
                    case2[11]= json2.getString("Ages");
                    case2[12]= json2.getString("Feel_Uncomfortable");
                    case2[13]= json2.getString("Uncomfortable_Feeling");
                    case2[14]= json2.getString("Uncomfortable_Time");
                    case2[15]= json2.getString("Other_Uncomfortable");


                    Case.add(case2);
                 //   items.add(item);

                }

                if (json.getString("KEY_SUCCESS") != null) {

                    String res = json.getString("KEY_SUCCESS");
                    if (res.equals("true")) {
                        // TODO: http request.

                        str ="OK";
                    } else {
                        // Error in login
                        str = "Error";

                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
                str = "Error";
            }
            Message msg = new Message();
            Bundle data = new Bundle();
            data.putString("value", str);
            msg.setData(data);
            H_GetCase.sendMessage(msg);

        }
    };

    Handler H_GetCase = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle data = msg.getData();
            String str = data.getString("value");


            String btn_text;

            if(str.equals("Overtime")){
                btn_text = "連線逾時";
            }
            else if(str.equals("Null")){
                btn_text = "尚無派遣";
            }
            else if(str.equals("Error")){
                btn_text = "異常狀態";
            }
            else{
                btn_text = "接收完成";
                String[] info = str.split("／");

                SQLite SQLite = new SQLite(Login.this);
                SQLiteDatabase DB = SQLite.getWritableDatabase();
                ContentValues cv = new ContentValues();
/*
            //第一版
                for(int i = 0;i < info.length;i++ ) {
                    String[] Content = info[i].split("@");

                    cv.put("CaseID", Content.length > 0?Content[0]:"");
                    cv.put("CaseSubType", Content.length > 1?Content[1]:"");
                    cv.put("DispatchTime", Content.length > 2?Content[2]:"");
                    cv.put("ReportPlace", Content.length > 3?Content[3]:"");
                    cv.put("DispVehicleID", Content.length > 4?Content[4]:"");
                    cv.put("Coordinate", Content.length > 5?Content[5]:"");
                    DB.insert("MainCases", null, cv);

                    HashMap<String, String> item = new HashMap<String, String>();
                    item.put("CaseID", Content.length > 0?Content[0]:"");
                    item.put("CaseSubType", Content.length > 1?Content[1]:"");
                    item.put("DispatchTime", Content.length > 2?Content[2]:"");
                    item.put("ReportPlace", Content.length > 3?Content[3]:"");
                    item.put("DispVehicleID", Content.length > 4?Content[4]:"");
                    item.put("Coordinate", Content.length > 5?Content[5]:"");
                    listItem.add(item);
                }
*/
                //第二版

                for(int i = 0;i < Case.size();i++ ) {
                    String[] Content = Case.get(i);

                    cv.put("CaseID", Content.length > 0?Content[0]:"");
                    cv.put("CaseSubType", Content.length > 1?Content[1]:"");
                    cv.put("DispatchTime", Content.length > 2?Content[2]:"");
                    cv.put("ReportPlace", Content.length > 3?Content[3]:"");
                    cv.put("DispVehicleID", Content.length > 4?Content[4]:"");
                    cv.put("Coordinate", Content.length > 5?Content[5]:"");
                    cv.put("Car_Index", Content.length > 6?Content[6]:"");
                    cv.put("Name", Content.length > 7?Content[7]:"");
                    cv.put("Sex", Content.length > 8?Content[8]:"");
                    cv.put("Phone", Content.length > 9?Content[9]:"");
                    cv.put("Identity", Content.length > 10?Content[10]:"");
                    cv.put("Age", Content.length > 11?Content[11]:"");

                    DB.insert("MainCases", null, cv);

                    HashMap<String, String> item = new HashMap<String, String>();
                    item.put("CaseID", Content.length > 0?Content[0]:"");
                    item.put("CaseSubType", Content.length > 1?Content[1]:"");
                    item.put("DispatchTime", Content.length > 2?Content[2]:"");
                    item.put("ReportPlace", Content.length > 3?Content[3]:"");
                    item.put("DispVehicleID", Content.length > 4?Content[4]:"");
                    item.put("Coordinate", Content.length > 5?Content[5]:"");
                    item.put("Car_Index", Content.length > 6?Content[6]:"");
                    item.put("Name", Content.length > 7?Content[7]:"");
                    item.put("Gender", Content.length > 8?Content[8]:"");
                    item.put("Phone", Content.length > 9?Content[9]:"");
                    item.put("Personal_ID", Content.length > 10?Content[10]:"");
                    item.put("Ages", Content.length > 11?Content[11]:"");
                    item.put("Feel_Uncomfortable", Content.length > 12?Content[12]:"");
                    item.put("Uncomfortable_Feeling", Content.length > 13?Content[13]:"");
                    item.put("Uncomfortable_Time", Content.length > 14?Content[14]:"");
                    item.put("Other_Uncomfortable", Content.length > 15?Content[15]:"");
                    listItem.add(item);
                }

               // --------------------------
                AdapterCase Btnadapter = new AdapterCase(
                        Login.this,
                        listItem,
                        R.layout.listview_case,
                        new String[] {"CaseID", "ReportPlace"},
                        new int[] {R.id.tv_CaseID,R.id.tv_Address,R.id.btn_Delete}
                );
                lv_Case.setAdapter(Btnadapter);
                SQLite.close();
                DB.close();
            }

            btn_Get.setText(btn_text);
            btn_Get.setEnabled(true);
            lv_Case.setEnabled(true);
        }
    };

    //檢查網路
    private boolean checkNetWork() {
        ConnectivityManager mConnectivityManager =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();

        if (mNetworkInfo != null) {
            //網路是否已連線
            mNetworkInfo.isConnected();

            getTeam();
            return true;
        } else {
            new AlertDialog.Builder(this).setMessage("沒有網路")
                    .setTitle("請開啟網路連線功能")
                    .setCancelable(false)
                    .setPositiveButton("確定",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,int id) {

                                }
                            })
                    .show();

        }
        return false;
    }

}
