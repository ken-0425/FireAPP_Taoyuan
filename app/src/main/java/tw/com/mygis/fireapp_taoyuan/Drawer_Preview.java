package tw.com.mygis.fireapp_taoyuan;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Picture;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;

import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import library.JSONPost;

/**
 * Created by Bart on 2014/8/23.
 */
public class Drawer_Preview extends Activity implements OnClickListener {

    private String url;
    private GlobalVariable gv;
    private WebView wv_Preview;
    private boolean SelfCase = false;

    private ArrayList<String[]> Case =new ArrayList<String[]>();

    /**
     * 側頁
     */
    private DrawerLayout dl_Drawer;
    private ActionBarDrawerToggle abdt_Drawer;

    private LinearLayout ll_Drawer;
    private ListView lv_Drawer_Subno, lv_Drawer_Item;
    // 記錄被選擇的選單指標用
    private ArrayList<HashMap<String,String>> listItem = new ArrayList<HashMap<String,String>>();

    /**
     * 等待視窗
     */
    private ProgressDialog PDialog = null;
    private AlertDialog.Builder dialog = null;
    private FileService fileService = new FileService(Drawer_Preview.this);
    //重新上傳計時
    private boolean OverTimeSecondTF = false;
    private int OverTimeSecond = 5;
    private AlertDialog dialog_overtime = null;
    private AlertDialog dialog_noFNumber = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drawer_preview);

        controls();
        setDrawerLayout();

    }

    /**
     *  控制項設定
     */
    private void controls() {
        url = this.getString(R.string.web);
        gv = (GlobalVariable) getApplicationContext();

        wv_Preview = (WebView) findViewById(R.id.wv_Preview);
        wv_Preview.getSettings().setSupportZoom(true);//縮放
        wv_Preview.getSettings().setBuiltInZoomControls(true);//縮放
        wv_Preview.getSettings().setJavaScriptEnabled(true);
        wv_Preview.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        wv_Preview.addJavascriptInterface(Drawer_Preview.this, "APP_Function");
        //wv_Preview.loadUrl("http://118.163.96.189/FireAPP_Taoyuan/Preview.html");
        wv_Preview.loadUrl("file:///android_asset/Preview.html");
        wv_Preview.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                return true;
            }
        });
    }

    public void onClick(View v) {

    }
    /**
     * 設定側邊欄
     * */
    private void setDrawerLayout() {
        dl_Drawer = (DrawerLayout) findViewById(R.id.dl_Drawer);
        // 設定影子
        dl_Drawer.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
        abdt_Drawer = new ActionBarDrawerToggle(this, dl_Drawer, R.drawable.ic_drawer, 0, 0);

        dl_Drawer.setDrawerListener(abdt_Drawer);

        setDrawerMenu();
    }
    /**
     * 設定側邊欄 - 動作
     * */
    private void setDrawerMenu() {
        // 定義新宣告的兩個物件：選項清單的 ListView 以及 Drawer內容的 LinearLayou
        ll_Drawer = (LinearLayout) findViewById(R.id.ll_Drawer);
        lv_Drawer_Subno = (ListView) findViewById(R.id.lv_Drawer_Subno);
        lv_Drawer_Item = (ListView) findViewById(R.id.lv_Drawer_Item);

        SQLite SQLite = new SQLite(Drawer_Preview.this);
        SQLiteDatabase DB = SQLite.getWritableDatabase();
        String[] colum = { "Subno" };
        String[] parameter = {gv.FNumber};
        Cursor c = DB.query("MainTab1", colum, "CaseID=?", parameter, null, null, "id ASC");
        if (c.getCount() > 0) {
            //移動到第一筆
            c.moveToFirst();
            for (int i = 0; i < c.getCount(); i++) {
                HashMap<String, String> item = new HashMap<String, String>();
                item.put("CaseID", gv.FNumber);
                item.put("SubnoID", "表單" + c.getString(0));
                item.put("Subno", String.valueOf(c.getString(0)));
                listItem.add(item);
                if(i == 0){
                    if(gv.DrawerItemID.equals("")) {
                        gv.DrawerItemID = String.valueOf(c.getString(0));
                    }
                }
                c.moveToNext();
            }
        }
        SQLite.close();
        DB.close();
        c.close();

        AdapterSubno SubnoAdapter = new AdapterSubno(
                Drawer_Preview.this,
                listItem,
                R.layout.listview_subno,
                gv.DrawerItemPosition,
                new String[]{"SubnoID"},
                new int[]{R.id.iv_Select, R.id.tv_SubnoID, R.id.btn_Delete}
        );

        lv_Drawer_Subno.setAdapter(SubnoAdapter);

        // 當清單選項的子物件被點擊時要做的動作
        lv_Drawer_Subno.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                lv_Drawer_Subno.setEnabled(false);
                HashMap<String, String> data = (HashMap<String, String>) parent.getItemAtPosition(position);
                selectSubno(data, position);
                lv_Drawer_Subno.setEnabled(true);
            }
        });

        AdapterFunction FunctionAdapter = new AdapterFunction(
                Drawer_Preview.this,
                R.layout.listview_function,
                new int[]{R.drawable.function_add, R.drawable.function_navigation, R.drawable.function_signature, R.drawable.function_upload},
                new String[]{"新增表單", "導航", "簽名", "上傳"},
                new int[]{R.id.iv_ItemName, R.id.tv_ItemName}
        );

        lv_Drawer_Item.setAdapter(FunctionAdapter);

        // 當清單選項的子物件被點擊時要做的動作
        lv_Drawer_Item.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                lv_Drawer_Item.setEnabled(false);
                selectItem(position);
                lv_Drawer_Item.setEnabled(true);
            }
        });

    }

    /**
     * 設定當側邊欄裡的某個選項被點擊後要做的動作
     * */
    private void selectSubno(HashMap<String, String> data, int position) {
        if(!gv.DrawerItemID.equals(data.get("Subno").toString())) {

            PDialog = ProgressDialog.show(Drawer_Preview.this, "請稍等...", "切換表單中...", true);

            gv.DrawerItemID = data.get("Subno").toString();
            gv.DrawerItemPosition = position;

            new Thread(R_SwitchTable).start();
        }
    }

    private void selectItem(int position) {
        Intent intent = new Intent();
        switch (position){
            case 0:
                PDialog = ProgressDialog.show(Drawer_Preview.this, "請稍等...", "建立表單中...", true);
                new Thread(R_NewTable).start();
                break;
            case 1:
                int sdk = android.os.Build.VERSION.SDK_INT;
                if(sdk < android.os.Build.VERSION_CODES.HONEYCOMB) {
                    android.text.ClipboardManager clipboard = (android.text.ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                    clipboard.setText(gv.Coordinate);
                } else {
                    android.content.ClipboardManager clipboard = (android.content.ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                    android.content.ClipData clip = android.content.ClipData.newPlainText("Coordinate",gv.Coordinate);
                    clipboard.setPrimaryClip(clip);
                }
                intent.setClass(Drawer_Preview.this, Drawer_Navigation.class);
                startActivity(intent);
                Drawer_Preview.this.finish();
                break;
            case 2:
                intent.setClass(Drawer_Preview.this, Drawer_Handwrite.class);
                startActivity(intent);
                Drawer_Preview.this.finish();
                break;
            case 3:
                wv_Preview.setInitialScale(200);
                wv_Preview.setInitialScale(100);
                if(isValidDate(gv.Date)){
                    dialog = new AlertDialog.Builder(Drawer_Preview.this);
                    dialog.setTitle("提示"); //設定dialog 的title顯示內容
                    dialog.setMessage("是否執行上傳動作?");
                    dialog.setCancelable(false); //關閉 Android 系統的主要功能鍵(menu,home等...)
                    dialog.setPositiveButton("是", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            if(gv.FNumber.equals("")){
                                View view = View.inflate(getApplicationContext(), R.layout.dialog_nofnumber, null);
                                dialog_noFNumber = new AlertDialog.Builder(Drawer_Preview.this)
                                        .setView(view)
                                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {

                                            }
                                        }).create();
                                dialog_noFNumber.setCanceledOnTouchOutside(false);
                                dialog_noFNumber.setTitle("請選擇對應案件"); //設定dialog 的title顯示內容
                                dialog_noFNumber.show();

                                PDialog = ProgressDialog.show(Drawer_Preview.this, "提示", "案件接收中...", true);
                             //   new Thread(R_GetCase).start();
                                new Thread(R_GetCase_new).start();
                            }
                            else{
                                PDialog = ProgressDialog.show(Drawer_Preview.this, "請稍等", "上傳資料中...", true);
                                new Thread(R_Upload).start();
                            }
                        }
                    });
                    dialog.setNegativeButton("否", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    dialog.show();
                }
                else {
                    dialog = new AlertDialog.Builder(Drawer_Preview.this);
                    dialog.setTitle("警告"); //設定dialog 的title顯示內容
                    dialog.setMessage("基本資料'日期'格式有誤請重新確認!");
                    dialog.setCancelable(false); //關閉 Android 系統的主要功能鍵(menu,home等...)
                    dialog.setPositiveButton("確定", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    dialog.show();
                }
                break;
            default:
                break;
        }
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        abdt_Drawer.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        abdt_Drawer.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (abdt_Drawer.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private Bitmap captureWebView(WebView webView){
        //Picture snapShot = webView.capturePicture();

        Bitmap bmp = Bitmap.createBitmap(webView.getWidth(), webView.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bmp);
        webView.draw(canvas);
        return bmp;
    }

    public String BitMapToString(Bitmap bitmap){
        ByteArrayOutputStream baos=new  ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte [] b = baos.toByteArray();
        String temp = Base64.encodeToString(b, Base64.DEFAULT);
        return temp;
    }

    private void Dlog(String str) {
        dialog_overtime = new AlertDialog.Builder(Drawer_Preview.this)
                .setPositiveButton("重新接收", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        PDialog = ProgressDialog.show(Drawer_Preview.this, "提示", "案件接收中...", true);
                      //  new Thread(R_GetCase).start();
                        new Thread(R_GetCase_new).start();
                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).create();
        dialog_overtime.setCanceledOnTouchOutside(false);
        dialog_overtime.setTitle(str); //設定dialog 的title顯示內容
        dialog_overtime.show();
    }
    public static boolean isValidDate(String str) {
        boolean convertSuccess=true;
        // 指定日期格式为四位年/两位月份/两位日期，注意yyyy/MM/dd区分大小写；
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        try {
            // 设置lenient为false. 否则SimpleDateFormat会比较宽松地验证日期，比如2007/02/29会被接受，并转换成2007/03/01
            format.setLenient(false);
            format.parse(str);
        } catch (ParseException e) {
            // e.printStackTrace();
            // 如果throw java.text.ParseException或者NullPointerException，就说明格式不对
            convertSuccess = false;
        }
        return convertSuccess;
    }

    public String FileName() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        Date curDate = new Date(System.currentTimeMillis()); // 獲取當前時間
        return formatter.format(curDate);
    }

    private boolean TimeCompare(String time1, String time2) {
        boolean bool = false;

        if (time1.equals("") || time2.equals("")) {
            return bool;
        }

        String timeS = time1.replace(":", "");
        String timeE = time2.replace(":", "");

        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher istimeS = pattern.matcher(timeS);
        Matcher istimeE = pattern.matcher(timeE);

        if (istimeS.matches() && istimeE.matches()) {
            if (Integer.parseInt(timeS) >= Integer.parseInt(timeE)) {
                if (!(Integer.parseInt(timeS) + 100 > 2400 && Integer.parseInt(timeE) < 100)) {
                    bool = true;
                }
            }
        }

        return bool;
    }
    /**
     * 返回上一頁
     *
     * @return
     */
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if ((keyCode == KeyEvent.KEYCODE_BACK)) {   //確定按下退出鍵
            Intent intent = new Intent();
            intent.setClass(Drawer_Preview.this, MainActivity.class);
            startActivity(intent);
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    Runnable R_SwitchTable = new Runnable() {
        @Override
        public void run() {
            // TODO: http request.

            gv.SwitchingForm();

            H_SwitchTable.sendEmptyMessage(0);
        }
    };

    Handler H_SwitchTable = new Handler()
    {
        @Override
        public void handleMessage(Message msg) {
            AdapterSubno SubnoAdapter = new AdapterSubno(
                    Drawer_Preview.this,
                    listItem,
                    R.layout.listview_subno,
                    gv.DrawerItemPosition,
                    new String[]{"SubnoID"},
                    new int[]{R.id.iv_Select, R.id.tv_SubnoID, R.id.btn_Delete}
            );

            lv_Drawer_Subno.setAdapter(SubnoAdapter);

            // 關掉
            wv_Preview.loadUrl("file:///android_asset/Preview.html");
            dl_Drawer.closeDrawer(ll_Drawer);
            PDialog.dismiss();
        }
    };

    Runnable R_NewTable = new Runnable() {
        @Override
        public void run() {
            // TODO: http request.
            SQLite SQLite = new SQLite(Drawer_Preview.this);
            SQLiteDatabase DB = SQLite.getWritableDatabase();
            String[] colum = {"MAX(Subno)"};
            String[] parameter = {gv.FNumber};
            Cursor c = DB.query("MainTab1", colum, "CaseID=?", parameter, null, null, "id ASC");
            c.moveToFirst();

            int count =  c.getInt(0) + 1;

            gv.NewTable(count);

            SQLite.close();
            DB.close();
            c.close();

            HashMap<String, String> item = new HashMap<String, String>();
            item.put("SubnoID", "表單" + count);
            item.put("Subno", String.valueOf(count));
            listItem.add(item);
            H_NewTable.sendEmptyMessage(0);
        }
    };

    Handler H_NewTable = new Handler()
    {
        @Override
        public void handleMessage(Message msg) {
            AdapterSubno SubnoAdapter = new AdapterSubno(
                    Drawer_Preview.this,
                    listItem,
                    R.layout.listview_subno,
                    gv.DrawerItemPosition,
                    new String[]{"SubnoID", "Subno"},
                    new int[]{R.id.iv_Select, R.id.tv_SubnoID, R.id.btn_Delete}
            );

            lv_Drawer_Subno.setAdapter(SubnoAdapter);

            PDialog.dismiss();
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
            params.add(new BasicNameValuePair("GroupType", gv.CarID));
            params.add(new BasicNameValuePair("CS_NO", ""));

            String str = gv.GetResponse(httppost, params);

            data.putString("value", str);
            msg.setData(data);
            PDialog.dismiss();
            H_GetCase.sendMessage(msg);
        }
    };


    Runnable R_GetCase_new = new Runnable(){
        @Override
        public void run() {


            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);

            // check for login response
            try {
                JSONPost jsonPost = new JSONPost();
                JSONObject json = jsonPost.R_GetCase(gv.CarID01,gv.CarID);


                JSONArray jarray =new JSONArray(json.getString("datas"));


                for(int n = 0; n < jarray.length(); n++) {
                    JSONObject json2 = jarray.getJSONObject(n);


                    String jsonArrayTime = json2.getString("Date");
                    java.util.Date dt=new java.util.Date(Long.parseLong(jsonArrayTime));//UnixTime毫秒
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm");


                    String[] case2 = new String[6];
                    String[] Case_Reason2=new String[]{"創傷","非創傷","臨時勤務","路倒","急病","車禍","其他"};

                    case2[0] = json2.getString("Case_Number");
                    int a =Integer.parseInt(json2.getString("Case_Reason"));
                    case2[1] = Case_Reason2[a];
                    case2[2]= sdf.format(dt);
                    case2[3] = json2.getString("Location");
                    case2[4] = json2.getString("Attendance");
                    case2[5]= json2.getString("Coordinate");


                    Case.add(case2);
                    //   items.add(item);

                }

                if (json.getString("KEY_SUCCESS") != null) {

                    String res = json.getString("KEY_SUCCESS");
                    if (res.equals("true")) {
                        // TODO: http request.
                        Message msg = new Message();
                        H_GetCase.sendMessage(msg);

                    } else {
                        // Error in login

                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            PDialog.dismiss();

        }
    };

    Handler H_GetCase = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle data = msg.getData();
            String str = data.getString("value");
            str="第二版";
            if(str.equals("Overtime")){
                Dlog("連線逾時");
            }
            else if(str.equals("Null")){
                Dlog("尚無派遣");
            }
            else if(str.equals("Error")){
                Dlog("異常狀態");
            }
            else{
                String[] info = str.split("／");

                ArrayList<HashMap<String,String>> CaseItem = new ArrayList<HashMap<String,String>>();


                /*
                //第一版
                for(int i = 0;i < info.length;i++ ) {
                    String[] Content = info[i].split("@");

                    HashMap<String, String> item = new HashMap<String, String>();
                    item.put("CaseID", Content[0]);
                    item.put("CaseSubType", Content[1]);
                    item.put("DispatchTime", Content[2]);
                    item.put("ReportPlace", Content[3]);
                    item.put("DispVehicleID", Content[4]);
                    CaseItem.add(item);
                }
                */

                //第二版
                for(int i = 0;i < Case.size();i++ ) {
                    String[] Content = Case.get(i);

                    HashMap<String, String> item = new HashMap<String, String>();
                    item.put("CaseID", Content.length > 0?Content[0]:"");
                    item.put("CaseSubType", Content.length > 1?Content[1]:"");
                    item.put("DispatchTime", Content.length > 2?Content[2]:"");
                    item.put("ReportPlace", Content.length > 3?Content[3]:"");
                    item.put("DispVehicleID", Content.length > 4?Content[4]:"");

                    CaseItem.add(item);
                }


                AdapterCase Btnadapter = new AdapterCase(
                        Drawer_Preview.this,
                        CaseItem,
                        R.layout.listview_case,
                        new String[] {"CaseID", "ReportPlace"},
                        new int[] {R.id.tv_CaseID,R.id.tv_Address,R.id.btn_Delete}
                );
                ListView lv = (ListView) dialog_noFNumber.findViewById(R.id.lv_Drawer_NoFNumber);
                lv.setAdapter(Btnadapter);
                lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                        HashMap<String, String> data = (HashMap<String, String>) arg0.getItemAtPosition(arg2);

                        gv.FNumber = data.get("CaseID").toString();
                        gv.LoadPDF = data.get("CaseSubType").toString();
                        dialog_noFNumber.dismiss();

                        SelfCase = true;
                        PDialog = ProgressDialog.show(Drawer_Preview.this, "請稍等", "上傳資料中...", true);
                        new Thread(R_Upload).start();
                    }
                });
            }
        }
    };

    Runnable R_Upload = new Runnable(){
        @Override
        public void run() {
            // TODO: http request.
            Message msg = new Message();
            Bundle data = new Bundle();

            HttpPost httppost = new HttpPost( url + "APP_DateTime.aspx" ) ; //宣告要使用的頁面
            List<NameValuePair> params = new ArrayList<NameValuePair>( ) ; //宣告參數清單
            params.add(new BasicNameValuePair("Time", gv.Date));

            String VTime = gv.GetResponse(httppost, params);

            if(VTime.equals("OK")){
                Bitmap bmp = captureWebView(wv_Preview);
                fileService.saveBitmapToSDCard(FileName() + "_main.jpg", bmp, "Print");
                gv.PreviewMain = BitMapToString(bmp);
                //UriMain = Uri.parse(fileService.saveBitmapToSDCard(gv.FNumber + gv.DrawerItemPosition + "_main.jpg", bmp, "Print"));

                //第一版
                //  String str = gv.Upload(url);
                //第二版
                String str= uploadData();

                PDialog.dismiss();

                if(str.equals("OK")){
                    H_OK.sendEmptyMessage(0);
                }
                else if(str.equals("Overtime")){
                    H_OverTime.sendMessage(msg);
                }
                else if(str.equals("Error")){
                    data.putString("value", "異常狀態!");
                    msg.setData(data);
                    H_Error.sendMessage(msg);
                }
                else{
                    data.putString("value", str);
                    msg.setData(data);
                    H_Error.sendMessage(msg);
                }
            }
            else if(VTime.equals("Overtime")){
                PDialog.dismiss();
                H_OverTime.sendMessage(msg);
            }
            else if(VTime.equals("Error")){
                PDialog.dismiss();
                data.putString("value", "已超過上傳日期期限無法上傳該案件!");
                msg.setData(data);
                H_Error.sendMessage(msg);
            }
            else{
                PDialog.dismiss();
                data.putString("value", VTime);
                msg.setData(data);
                H_Error.sendMessage(msg);
            }
        }
    };

    Handler H_OK = new Handler()
    {
        @Override
        public void handleMessage(Message msg) {SQLite SQLite = new SQLite(Drawer_Preview.this);
            if(SelfCase){
                SelfCase = false;
                gv.FNumber = "";
            }
            SQLiteDatabase DB = SQLite.getWritableDatabase();
            String[] parameter = {gv.FNumber, gv.DrawerItemID};
            DB.delete("MainTab1", "CaseID=? AND Subno=?", parameter);
            DB.delete("MainTab2", "CaseID=? AND Subno=?", parameter);
            DB.delete("MainTab3", "CaseID=? AND Subno=?", parameter);
            DB.delete("MainTab4", "CaseID=? AND Subno=?", parameter);
            DB.delete("MainTab5", "CaseID=? AND Subno=?", parameter);
            DB.delete("MainHandWrite", "CaseID=? AND Subno=?", parameter);
            SQLite.close();
            DB.close();

            if(lv_Drawer_Subno.getCount() > 1) {
                listItem.remove(gv.DrawerItemPosition);
                gv.DrawerItemID = listItem.get(0).get("Subno");
                gv.DrawerItemPosition = 0;
                gv.SwitchingForm();
                wv_Preview.loadUrl("file:///android_asset/Preview.html");
                AdapterSubno SubnoAdapter = new AdapterSubno(
                        Drawer_Preview.this,
                        listItem,
                        R.layout.listview_subno,
                        gv.DrawerItemPosition,
                        new String[]{"SubnoID"},
                        new int[]{R.id.iv_Select, R.id.tv_SubnoID, R.id.btn_Delete}
                );
                lv_Drawer_Subno.setAdapter(SubnoAdapter);dialog = new AlertDialog.Builder(Drawer_Preview.this);

                dialog = new AlertDialog.Builder(Drawer_Preview.this);
                dialog.setTitle("完成"); //設定dialog 的title顯示內容
                dialog.setMessage("上傳成功!");
                dialog.setCancelable(false); //關閉 Android 系統的主要功能鍵(menu,home等...)
                dialog.setPositiveButton("確定", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                dialog.show();
            }
            else {
                dialog = new AlertDialog.Builder(Drawer_Preview.this);
                dialog.setTitle("完成"); //設定dialog 的title顯示內容
                dialog.setMessage("是否結束案件?");
                dialog.setCancelable(false); //關閉 Android 系統的主要功能鍵(menu,home等...)
                dialog.setPositiveButton("是", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        SQLite SQLite = new SQLite(Drawer_Preview.this);
                        SQLiteDatabase DB = SQLite.getWritableDatabase();
                        DB.delete("MainCases", "CaseID=?", new String[]{gv.FNumber});
                        DB.delete("MainTab1", "CaseID=?", new String[]{gv.FNumber});
                        DB.delete("MainTab2", "CaseID=?", new String[]{gv.FNumber});
                        DB.delete("MainTab3", "CaseID=?", new String[]{gv.FNumber});
                        DB.delete("MainTab4", "CaseID=?", new String[]{gv.FNumber});
                        DB.delete("MainTab5", "CaseID=?", new String[]{gv.FNumber});
                        DB.delete("MainHandWrite", "CaseID=?", new String[]{gv.FNumber});
                        SQLite.close();
                        DB.close();

                        Intent intent = new Intent();
                        intent.setClass(Drawer_Preview.this, Login.class);
                        startActivity(intent);
                        finish();
                    }
                });
                dialog.setNegativeButton("否", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent();
                        intent.setClass(Drawer_Preview.this, Login.class);
                        startActivity(intent);
                        finish();
                    }
                });
                dialog.show();
            }
        }
    };

    Handler H_Error = new Handler()
    {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle data = msg.getData();
            String str = data.getString("value");
            dialog = new AlertDialog.Builder(Drawer_Preview.this);
            dialog.setTitle("錯誤"); //設定dialog 的title顯示內容
            dialog.setMessage(str);
            dialog.setCancelable(false); //關閉 Android 系統的主要功能鍵(menu,home等...)
            dialog.setPositiveButton("確定", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                }
            });
            dialog.show();
        }
    };

    Handler H_OverTime = new Handler()
    {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            View view = View.inflate(getApplicationContext(), R.layout.dialog_overtime, null);

            dialog_overtime = new AlertDialog.Builder(Drawer_Preview.this)
                    .setView(view)
                    .setPositiveButton("重新上傳", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            H_OverTimeMsg.removeMessages(0);
                            dialog_overtime.cancel();
                            OverTimeSecondTF = false;
                            PDialog = ProgressDialog.show(Drawer_Preview.this, "請稍等...", "上傳資料中...", true);
                            new Thread(R_Upload).start();
                        }
                    }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            OverTimeSecondTF = false;
                        }
                    }).create();
            dialog_overtime.setCanceledOnTouchOutside(false);
            dialog_overtime.setTitle("錯誤"); //設定dialog 的title顯示內容
            dialog_overtime.show();
            OverTimeSecond = 5;
            OverTimeSecondTF = true;
            H_OverTimeMsg.sendEmptyMessage(0);
        }
    };

    Handler H_OverTimeMsg = new Handler()
    {
        @Override
        public void handleMessage(Message msg) {
            int what = msg.what;
            if (what == 0) {
                if(OverTimeSecondTF) {
                    if (OverTimeSecond >= 0 ) {
                        TextView tv = (TextView) dialog_overtime.findViewById(R.id.tv_dialog);
                        tv.setText(Integer.toString(OverTimeSecond));
                        OverTimeSecond--;
                        if (dialog_overtime.isShowing()) {
                            H_OverTimeMsg.sendEmptyMessageDelayed(0, 1000);
                        }
                    }
                    else {
                        H_OverTimeMsg.removeMessages(0);
                        dialog_overtime.cancel();
                        PDialog = ProgressDialog.show(Drawer_Preview.this, "請稍等...", "上傳資料中...", true);
                        new Thread(R_Upload).start();
                    }
                }
                else{
                    H_OverTimeMsg.removeMessages(0);
                    dialog_overtime.cancel();
                }
            }
            else {
                H_OverTimeMsg.removeMessages(0);
                dialog_overtime.cancel();
            }
        }
    };

    //基本資料
    @JavascriptInterface
    public String FNumber()
    {
        return gv.FNumber;
    }
    @JavascriptInterface
    public boolean Critical()
    {
        return gv.Critical;
    }
    @JavascriptInterface
    public boolean Critical_T()
    {
        if(gv.Critical){
            return false;
        }
        Pattern pattern = Pattern.compile("[0-9]*");
        if(gv.GCS1_E.length() > 0 && gv.GCS1_V.length() > 0 && gv.GCS1_M.length() > 0){
            Matcher GCS1_E = pattern.matcher(gv.GCS1_E);
            Matcher GCS1_V = pattern.matcher(gv.GCS1_V);
            Matcher GCS1_M = pattern.matcher(gv.GCS1_M);
            if (GCS1_E.matches() && GCS1_V.matches() && GCS1_M.matches()) {
                int sum = Integer.parseInt(gv.GCS1_E) + Integer.parseInt(gv.GCS1_V) + Integer.parseInt(gv.GCS1_M);
                if(sum < 13){
                    return true;
                }
            }
        }
        if(gv.GCS2_E.length() > 0 && gv.GCS2_V.length() > 0 && gv.GCS2_M.length() > 0){
            Matcher GCS2_E = pattern.matcher(gv.GCS2_E);
            Matcher GCS2_V = pattern.matcher(gv.GCS2_V);
            Matcher GCS2_M = pattern.matcher(gv.GCS2_M);
            if (GCS2_E.matches() && GCS2_V.matches() && GCS2_M.matches()) {
                int sum = Integer.parseInt(gv.GCS2_E) + Integer.parseInt(gv.GCS2_V) + Integer.parseInt(gv.GCS2_M);
                if(sum < 13){
                    return true;
                }
            }
        }
        if(gv.GCS3_E.length() > 0 && gv.GCS3_V.length() > 0 && gv.GCS3_M.length() > 0){
            Matcher GCS3_E = pattern.matcher(gv.GCS3_E);
            Matcher GCS3_V = pattern.matcher(gv.GCS3_V);
            Matcher GCS3_M = pattern.matcher(gv.GCS3_M);
            if (GCS3_E.matches() && GCS3_V.matches() && GCS3_M.matches()) {
                int sum = Integer.parseInt(gv.GCS3_E) + Integer.parseInt(gv.GCS3_V) + Integer.parseInt(gv.GCS3_M);
                if(sum < 13){
                    return true;
                }
            }
        }
        if(gv.LifeBreathing1.length() > 0){
            Matcher LifeBreathing1 = pattern.matcher(gv.LifeBreathing1);
            if (LifeBreathing1.matches()) {
                int sum = Integer.parseInt(gv.LifeBreathing1);
                if(sum >= 30 || sum < 10){
                    return true;
                }
            }
        }
        if(gv.LifeBreathing2.length() > 0){
            Matcher LifeBreathing2 = pattern.matcher(gv.LifeBreathing2);
            if (LifeBreathing2.matches()) {
                int sum = Integer.parseInt(gv.LifeBreathing2);
                if(sum >= 30 || sum < 10){
                    return true;
                }
            }
        }
        if(gv.LifeBreathing3.length() > 0){
            Matcher LifeBreathing3 = pattern.matcher(gv.LifeBreathing3);
            if (LifeBreathing3.matches()) {
                int sum = Integer.parseInt(gv.LifeBreathing3);
                if(sum >= 30 || sum < 10){
                    return true;
                }
            }
        }
        if(gv.Pulse1.length() > 0){
            if(gv.Pulse1.equals("無法測量")){
                return true;
            }
            else {
                Matcher Pulse1 = pattern.matcher(gv.Pulse1);
                if (Pulse1.matches()) {
                    int sum = Integer.parseInt(gv.Pulse1);
                    if(sum >= 140 || sum < 50){
                        return true;
                    }
                }
            }
        }
        if(gv.Pulse2.length() > 0){
            if(gv.Pulse2.equals("無法測量")){
                return true;
            }
            else {
                Matcher Pulse2 = pattern.matcher(gv.Pulse2);
                if (Pulse2.matches()) {
                    int sum = Integer.parseInt(gv.Pulse2);
                    if(sum >= 140 || sum < 50){
                        return true;
                    }
                }
            }
        }
        if(gv.Pulse3.length() > 0){
            if(gv.Pulse3.equals("無法測量")){
                return true;
            }
            else {
                Matcher Pulse3 = pattern.matcher(gv.Pulse3);
                if (Pulse3.matches()) {
                    int sum = Integer.parseInt(gv.Pulse3);
                    if(sum >= 140 || sum < 50){
                        return true;
                    }
                }
            }
        }
        if(gv.BPressure1_A.length() > 0){
            if(!gv.BPressure1_A.equals("橈動脈")){
                return true;
            }
        }
        if(gv.SBP2.length() > 0 && gv.DBP2.length() > 0){
            Matcher SBP2 = pattern.matcher(gv.SBP2);
            Matcher DBP2 = pattern.matcher(gv.DBP2);
            if (SBP2.matches() && DBP2.matches()) {
                int sum1 = Integer.parseInt(gv.SBP2);
                if(sum1 >= 220 || sum1 < 90){
                    return true;
                }
                int sum2 = Integer.parseInt(gv.DBP2);
                if(sum2 >= 220 || sum2 < 90){
                    return true;
                }
            }
        }
        if(gv.SBP3.length() > 0 && gv.DBP3.length() > 0){
            Matcher SBP3 = pattern.matcher(gv.SBP3);
            Matcher DBP3 = pattern.matcher(gv.DBP3);
            if (SBP3.matches() && DBP3.matches()) {
                int sum1 = Integer.parseInt(gv.SBP3);
                if(sum1 >= 220 || sum1 < 90){
                    return true;
                }
                int sum2 = Integer.parseInt(gv.DBP3);
                if(sum2 >= 220 || sum2 < 90){
                    return true;
                }
            }
        }
        if(gv.Temperature1.length() > 0){
            Matcher Temperature1 = pattern.matcher(gv.Temperature1);
            if (Temperature1.matches()) {
                int sum = Integer.parseInt(gv.Temperature1);
                if(sum >= 40 || sum < 32){
                    return true;
                }
            }
        }
        if(gv.Temperature2.length() > 0){
            Matcher Temperature2 = pattern.matcher(gv.Temperature2);
            if (Temperature2.matches()) {
                int sum = Integer.parseInt(gv.Temperature2);
                if(sum >= 40 || sum < 32){
                    return true;
                }
            }
        }
        if(gv.Temperature3.length() > 0){
            Matcher Temperature3 = pattern.matcher(gv.Temperature3);
            if (Temperature3.matches()) {
                int sum = Integer.parseInt(gv.Temperature3);
                if(sum >= 40 || sum < 32){
                    return true;
                }
            }
        }
        if(gv.SpO21.length() > 0){
            Matcher SpO21 = pattern.matcher(gv.SpO21);
            if (SpO21.matches()) {
                int sum = Integer.parseInt(gv.SpO21);
                if(sum < 90){
                    return true;
                }
            }
        }
        if(gv.SpO22.length() > 0){
            Matcher SpO22 = pattern.matcher(gv.SpO22);
            if (SpO22.matches()) {
                int sum = Integer.parseInt(gv.SpO22);
                if(sum < 90){
                    return true;
                }
            }
        }
        if(gv.SpO23.length() > 0){
            Matcher SpO23 = pattern.matcher(gv.SpO23);
            if (SpO23.matches()) {
                int sum = Integer.parseInt(gv.SpO23);
                if(sum < 90){
                    return true;
                }
            }
        }
        String[] str_ODisposal = gv.ODisposal[3].split("@");
        if(str_ODisposal.length > 1) {
            if (str_ODisposal[1].equals("Low") || str_ODisposal[1].equals("High")) {
                return true;
            } else {
                Matcher ODisposal4 = pattern.matcher(str_ODisposal[1]);
                if (ODisposal4.matches()) {
                    int sum = Integer.parseInt(str_ODisposal[1]);
                    if (sum > 500 || sum < 60) {
                        return true;
                    }
                }
            }
        }
        String[] str_Trauma3 = gv.Trauma[2].split("@");
        if(str_Trauma3.length > 1) {
            Matcher Trauma3 = pattern.matcher(str_Trauma3[1]);
            if (Trauma3.matches()) {
                int sum = Integer.parseInt(str_Trauma3[1]);
                if(sum >= 6){
                    return true;
                }
            }
        }
        if(gv.Stroke.equals("是")){
            return true;
        }
        if(!gv.NTrauma[3].equals("")){
            return true;
        }
        if(!gv.NTrauma[2].equals("")){
            return true;
        }
        if(!gv.NTrauma[8].equals("")){
            return true;
        }
        if(!gv.NTrauma[6].equals("") || !gv.NTrauma[7].equals("")){
            return true;
        }
        if(!gv.NTrauma[11].equals("")){
            return true;
        }
        if(!gv.Trauma[7].equals("") || !gv.NTrauma[13].equals("")){
            return true;
        }
        return false;
    }


    public String uploadData(){
    String message = null;
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        // check for login response
        try {

            JSONObject json = gv.UploadData("Null");



            if (json.getString("KEY_SUCCESS") != null) {

                String res = json.getString("KEY_SUCCESS");
                if (res.equals("true")) {

                    message="OK";
                } else {
                    // Error in login
                    message="Error";
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
            message="Error";
        }
        return message;
    }

    @JavascriptInterface
    public String Date()
    {
        return gv.Date;
    }
    @JavascriptInterface
    public String AttendanceUnit()
    {
        return gv.AttendanceUnit;
    }
    @JavascriptInterface
    public String AcceptedUnit()
    {
        return gv.AcceptedUnit;
    }
    @JavascriptInterface
    public String LocationHappen()
    {
        return gv.LocationHappen;
    }
    @JavascriptInterface
    public String AssistanceUnit()
    {
        return gv.AssistanceUnit;
    }
    @JavascriptInterface
    public String HospitalAddress()
    {
        return gv.HospitalAddress;
    }
    @JavascriptInterface
    public boolean HospitalAddress_T()
    {
        boolean bool = false;
        String[] NoHospital = {"未發現", "誤報", "中途取消", "出勤待命", "不需要", "拒送", "警察處理", "現場死亡"};
        for (int i = 0; i < NoHospital.length; i++) {
            if (NoHospital[i].equals(gv.ReasonHospital)) {
                bool = true;
                break;
            }
        }
        return (!gv.Time4.equals("") && gv.HospitalAddress.equals("")) || (!gv.HospitalAddress.equals("") && bool);
    }
    @JavascriptInterface
    public String ReasonHospital()
    {
        return gv.ReasonHospital;
    }
    @JavascriptInterface
    public boolean ReasonHospital_T()
    {
        return !gv.HospitalAddress.equals("") && gv.ReasonHospital.equals("");
    }
    @JavascriptInterface
    public String Time1()
    {
        return gv.Time1;
    }
    @JavascriptInterface
    public boolean Time1_T()
    {
        return !gv.HospitalAddress.equals("") && gv.Time1.equals("");
    }
    @JavascriptInterface
    public String Time2()
    {
        return gv.Time2;
    }
    @JavascriptInterface
    public boolean Time2_T()
    {
        return (!gv.HospitalAddress.equals("") && gv.Time2.equals("")) || TimeCompare(gv.Time1, gv.Time2);
    }
    @JavascriptInterface
    public String Time3()
    {
        return gv.Time3;
    }
    @JavascriptInterface
    public boolean Time3_T()
    {
        return (!gv.HospitalAddress.equals("") && gv.Time3.equals("")) || TimeCompare(gv.Time2, gv.Time3);
    }
    @JavascriptInterface
    public String Time4()
    {
        return gv.Time4;
    }
    @JavascriptInterface
    public boolean Time4_T()
    {
        return (!gv.HospitalAddress.equals("") && gv.Time4.equals("")) || TimeCompare(gv.Time3, gv.Time4);
    }
    @JavascriptInterface
    public String Time5()
    {
        return gv.Time5;
    }
    @JavascriptInterface
    public boolean Time5_T()
    {
        return TimeCompare(gv.Time4, gv.Time5);
    }
    @JavascriptInterface
    public String Time6()
    {
        return gv.Time6;
    }
    @JavascriptInterface
    public boolean Time6_T()
    {
        return TimeCompare(gv.Time5, gv.Time6);
    }
    @JavascriptInterface
    public String Name()
    {
        return gv.Name;
    }
    @JavascriptInterface
    public boolean Name_T()
    {
        return !gv.HospitalAddress.equals("") && gv.Name.equals("");
    }
    @JavascriptInterface
    public String Age()
    {
        String str = "";
        if(gv.AgeA){
            str += "約";
        }
        str += gv.Age;
        if(gv.AgeM){
            str += "月";
        }
        return str;
    }
    @JavascriptInterface
    public boolean Age_T()
    {
        return !gv.HospitalAddress.equals("") && gv.Age.equals("");
    }
    @JavascriptInterface
    public String Sex()
    {
        return gv.Sex;
    }
    @JavascriptInterface
    public boolean Sex_T()
    {
        return !gv.HospitalAddress.equals("") && gv.Sex.equals("");
    }
    @JavascriptInterface
    public String ID()
    {
        return gv.ID;
    }
    @JavascriptInterface
    public boolean ID_T()
    {
        return !gv.HospitalAddress.equals("") && gv.ID.equals("");
    }
    @JavascriptInterface
    public String Address()
    {
        return gv.Area + gv.Address;
    }
    @JavascriptInterface
    public boolean Address_T()
    {
        return !gv.HospitalAddress.equals("") && (gv.Area + gv.Address).equals("");
    }
    @JavascriptInterface
    public String Property()
    {
        return gv.Property;
    }
    @JavascriptInterface
    public String RCustody()
    {
        return gv.RCustody;
    }
    @JavascriptInterface
    public boolean RCustody_T()
    {
        return gv.Property.equals("有") && gv.RCustody.equals("");
    }
    @JavascriptInterface
    public String Custody()
    {
        return gv.Custody;
    }
    @JavascriptInterface
    public boolean Custody_T()
    {
        return gv.Property.equals("有") && gv.Custody.equals("");
    }
    @JavascriptInterface
    public String SCustody()
    {
        return gv.SCustody;
    }
    @JavascriptInterface
    public boolean SCustody_T()
    {
        return gv.Property.equals("有") && gv.SCustody.equals("");
    }

    //現場狀況
    @JavascriptInterface
    public String Trauma_Switch()
    {
        return gv.Trauma_Switch;
    }
    @JavascriptInterface
    public boolean Trauma_Switch_T()
    {
        return !gv.HospitalAddress.equals("") && Trauma().replace("－","").equals("") && NTrauma().replace("－", "").equals("");
    }

    @JavascriptInterface
    public String Trauma()
    {
        String str = "";
        for(String text: gv.Trauma){
            str += text + "－";
        }
        return str;
    }
    @JavascriptInterface
    public String Trauma1()
    {
        String str = "";
        for(String text: gv.Trauma1){
            str += text + "－";
        }
        return str;
    }
    @JavascriptInterface
    public boolean Trauma1_T()
    {
        return !gv.Trauma[0].equals("") && Trauma1().replace("－", "").equals("");
    }
    @JavascriptInterface
    public String Traffic()
    {
        String str = "";
        for(String text: gv.Traffic){
            str += text + "－";
        }
        return str;
    }
    @JavascriptInterface
    public boolean Traffic_T()
    {
        return !gv.Trauma[1].equals("") && Traffic().replace("－", "").equals("");
    }
    @JavascriptInterface
    public String NTrauma()
    {
        String str = "";
        for(String text: gv.NTrauma){
            str += text + "－";
        }
        return str;
    }
    @JavascriptInterface
    public String NTrauma6()
    {
        String str = "";
        for(String text: gv.NTrauma6){
            str += text + "－";
        }
        return str;
    }
    @JavascriptInterface
    public boolean NTrauma6_T()
    {
        return !gv.NTrauma[5].equals("") && NTrauma6().replace("－", "").equals("");
    }
    @JavascriptInterface
    public String Witnesses()
    {
        return gv.Witnesses;
    }
    @JavascriptInterface
    public String CPR()
    {
        return gv.CPR;
    }
    @JavascriptInterface
    public String PAD()
    {
        return gv.PAD;
    }
    @JavascriptInterface
    public String ROSC()
    {
        return gv.ROSC;
    }
    @JavascriptInterface
    public boolean OHCA_T()
    {
        return (!gv.Trauma[7].equals("") || !gv.NTrauma[13].equals("")) && (gv.Witnesses.equals("") || gv.CPR.equals("") ||gv.PAD.equals("") ||gv.ROSC.equals(""));
    }
    @JavascriptInterface
    public String PSpecies()
    {
        return gv.PSpecies;
    }
    @JavascriptInterface
    public boolean PSpecies_T()
    {
        return (!gv.Trauma[7].equals("") || !gv.NTrauma[13].equals("")) && gv.PSpecies.equals("");
    }
    @JavascriptInterface
    public boolean Stroke_T()
    {
        return gv.NTrauma6[0].equals("頭痛／頭暈／昏倒／昏厥") && gv.MHistory[1].equals("高血壓") && gv.Stroke.equals("") && gv.LTime.equals("") && !gv.Smile && !gv.LArm && !gv.Speech;
    }
    @JavascriptInterface
    public String Stroke()
    {
        return gv.Stroke;
    }
    @JavascriptInterface
    public String LTime()
    {
        return gv.LTime;
    }
    @JavascriptInterface
    public boolean Smile()
    {
        return gv.Smile;
    }
    @JavascriptInterface
    public boolean LArm()
    {
        return gv.LArm;
    }
    @JavascriptInterface
    public boolean Speech()
    {
        return gv.Speech;
    }

    //急救處置
    @JavascriptInterface
    public String Breathing()
    {
        String str = "";
        for(String text: gv.Breathing){
            str += text + "－";
        }
        return str;
    }
    @JavascriptInterface
    public boolean Breathing_T()
    {
        return (!gv.NTrauma[0].equals("") || !gv.NTrauma[1].equals("") || !gv.NTrauma[2].equals("") || !gv.NTrauma[3].equals("") || !gv.NTrauma[8].equals("")) && Breathing().replace("－", "").equals("");
    }
    @JavascriptInterface
    public boolean Breathing5_T()
    {
        return (!gv.Trauma[7].equals("") || !gv.NTrauma[13].equals("")) && gv.Breathing[4].equals("");
    }
    @JavascriptInterface
    public boolean Breathing9_T()
    {
        return (!gv.Trauma[7].equals("") || !gv.NTrauma[13].equals("")) && gv.Breathing[8].equals("");
    }
    @JavascriptInterface
    public String TDisposal()
    {
        String str = "";
        for(String text: gv.TDisposal){
            str += text + "－";
        }
        return str;
    }
    @JavascriptInterface
    public boolean TDisposal1_T()
    {
        return (!gv.Trauma[2].equals("") || !gv.Trauma[7].equals("")) && gv.TDisposal[0].equals("");
    }
    @JavascriptInterface
    public boolean TDisposal2_T()
    {
        return !gv.Trauma[0].equals("") && gv.TDisposal[1].equals("");
    }
    @JavascriptInterface
    public boolean TDisposal3_T()
    {
        return !gv.Trauma[0].equals("") && gv.TDisposal[2].equals("");
    }
    @JavascriptInterface
    public boolean TDisposal5_T()
    {
        return (!gv.Trauma[2].equals("") || !gv.Trauma[7].equals("")) && gv.TDisposal[4].equals("");
    }
    @JavascriptInterface
    public String CPRDisposal()
    {
        String str = "";
        for(String text: gv.CPRDisposal){
            str += text + "－";
        }
        return str;
    }
    @JavascriptInterface
    public String CPRDisposal3()
    {
        String str = "";
        for(String text: gv.CPRDisposal3){
            str += text + "－";
        }
        return str;
    }
    @JavascriptInterface
    public boolean CPRDisposal1_T()
    {
        return (!gv.Trauma[7].equals("") || !gv.NTrauma[13].equals("")) && gv.CPRDisposal[0].equals("");
    }
    @JavascriptInterface
    public boolean CPRDisposal2_T()
    {
        return (!gv.Trauma[7].equals("") || !gv.NTrauma[13].equals("")) && gv.CPRDisposal[1].equals("");
    }
    @JavascriptInterface
    public boolean CPRDisposal3_T()
    {
        return (!gv.Trauma[7].equals("") || !gv.NTrauma[13].equals("")) && gv.CPRDisposal[2].equals("");
    }
    @JavascriptInterface
    public String DDisposal()
    {
        String str = "";
        for(String text: gv.DDisposal){
            str += text + "－";
        }
        return str;
    }
    @JavascriptInterface
    public String DDisposal1()
    {
        String str = "";
        for(String text: gv.DDisposal1){
            str += text + "－";
        }
        return str;
    }
    @JavascriptInterface
    public String ODisposal()
    {
        String str = "";
        for(String text: gv.ODisposal){
            str += text + "－";
        }
        return str;
    }
    @JavascriptInterface
    public boolean ODisposal4_T()
    {
        Pattern pattern = Pattern.compile("[0-9]*");
        if(gv.GCS1_E.length() > 0 && gv.GCS1_V.length() > 0 && gv.GCS1_M.length() > 0){
            Matcher GCS1_E = pattern.matcher(gv.GCS1_E);
            Matcher GCS1_V = pattern.matcher(gv.GCS1_V);
            Matcher GCS1_M = pattern.matcher(gv.GCS1_M);
            if (GCS1_E.matches() && GCS1_V.matches() && GCS1_M.matches()) {
                int sum = Integer.parseInt(gv.GCS1_E) + Integer.parseInt(gv.GCS1_V) + Integer.parseInt(gv.GCS1_M);
                if(sum < 13){
                    return true;
                }
            }
        }
        return !gv.NTrauma[2].equals("") && gv.ODisposal[3].equals("");
    }
    @JavascriptInterface
    public String ALSDisposal()
    {
        String str = "";
        for(String text: gv.ALSDisposal){
            str += text + "－";
        }
        return str;
    }
    @JavascriptInterface
    public String Posture()
    {
        return gv.Posture;
    }
    @JavascriptInterface
    public boolean Posture_T()
    {
        return !gv.NTrauma[3].equals("") && gv.Posture.equals("");
    }
    @JavascriptInterface
    public String DrugUseTime1()
    {
        return gv.DrugUseTime1;
    }
    @JavascriptInterface
    public String DrugUseName1()
    {
        return gv.DrugUseName1;
    }
    @JavascriptInterface
    public String DrugUseDose1()
    {
        return gv.DrugUseDose1;
    }
    @JavascriptInterface
    public String DrugUser1()
    {
        return gv.DrugUser1;
    }
    @JavascriptInterface
    public String DrugUseTime2()
    {
        return gv.DrugUseTime2;
    }
    @JavascriptInterface
    public String DrugUseName2()
    {
        return gv.DrugUseName2;
    }
    @JavascriptInterface
    public String DrugUseDose2()
    {
        return gv.DrugUseDose2;
    }
    @JavascriptInterface
    public String DrugUser2()
    {
        return gv.DrugUser2;
    }
    @JavascriptInterface
    public String DrugUseTime3()
    {
        return gv.DrugUseTime3;
    }
    @JavascriptInterface
    public String DrugUseName3()
    {
        return gv.DrugUseName3;
    }
    @JavascriptInterface
    public String DrugUseDose3()
    {
        return gv.DrugUseDose3;
    }
    @JavascriptInterface
    public String DrugUser3()
    {
        return gv.DrugUser3;
    }
    @JavascriptInterface
    public String DrugUseTime4()
    {
        return gv.DrugUseTime4;
    }
    @JavascriptInterface
    public String DrugUseName4()
    {
        return gv.DrugUseName4;
    }
    @JavascriptInterface
    public String DrugUseDose4()
    {
        return gv.DrugUseDose4;
    }
    @JavascriptInterface
    public String DrugUser4()
    {
        return gv.DrugUser4;
    }

    //病患相關
    @JavascriptInterface
    public String Q1()
    {
        return gv.Q1;
    }
    @JavascriptInterface
    public String Q2()
    {
        return gv.Q2;
    }
    @JavascriptInterface
    public String Q3()
    {
        return gv.Q3;
    }
    @JavascriptInterface
    public String Q4()
    {
        return gv.Q4;
    }
    @JavascriptInterface
    public String Q5()
    {
        return gv.Q5;
    }
    @JavascriptInterface
    public String MHistory()
    {
        String str = "";
        for(String text: gv.MHistory){
            str += text + "－";
        }
        return str;
    }
    @JavascriptInterface
    public String Allergies()
    {
        String str = "";
        for(String text: gv.Allergies){
            str += text + "－";
        }
        return str;
    }
    @JavascriptInterface
    public String People()
    {
        return gv.People;
    }
    @JavascriptInterface
    public String SPeople()
    {
        return gv.SPeople;
    }

    //生命徵象
    @JavascriptInterface
    public String LifeTime1()
    {
        return gv.LifeTime1;
    }
    @JavascriptInterface
    public String Consciousness1()
    {
        return gv.Consciousness1;
    }
    @JavascriptInterface
    public String LifeBreathing1()
    {
        return gv.LifeBreathing1;
    }
    @JavascriptInterface
    public String Pulse1()
    {
        return gv.Pulse1;
    }
    @JavascriptInterface
    public String GCS1_E()
    {
        return gv.GCS1_E;
    }
    @JavascriptInterface
    public String GCS1_V()
    {
        return gv.GCS1_V;
    }
    @JavascriptInterface
    public String GCS1_M()
    {
        return gv.GCS1_M;
    }
    @JavascriptInterface
    public String BP1()
    {
        String str;
        if(gv.PBPressure1_A == 0){
            str =  gv.SBP1 + "／" + gv.DBP1 + "mmHg";
        }
        else{
            str = gv.BPressure1_A;
        }
        return str;
    }
    //@JavascriptInterface
    //public String SBP1()
    //{
    //    return gv.SBP1;
    //}
    //@JavascriptInterface
    //public String DBP1()
    //{
    //    return gv.DBP1;
    //}
    //@JavascriptInterface
    //public String BPressure1_A()
    //{
    //    return gv.BPressure1_A;
    //}
    @JavascriptInterface
    public String SpO21()
    {
        return gv.SpO21;
    }
    @JavascriptInterface
    public String Temperature1()
    {
        return gv.Temperature1;
    }
    @JavascriptInterface
    public String LifeTime2()
    {
        return gv.LifeTime2;
    }
    @JavascriptInterface
    public String Consciousness2()
    {
        return gv.Consciousness2;
    }
    @JavascriptInterface
    public String LifeBreathing2()
    {
        return gv.LifeBreathing2;
    }
    @JavascriptInterface
    public String Pulse2()
    {
        return gv.Pulse2;
    }
    @JavascriptInterface
    public String GCS2_E()
    {
        return gv.GCS2_E;
    }
    @JavascriptInterface
    public String GCS2_V()
    {
        return gv.GCS2_V;
    }
    @JavascriptInterface
    public String GCS2_M()
    {
        return gv.GCS2_M;
    }
    @JavascriptInterface
    public String BP2()
    {
        String str;
        if(gv.PBPressure2_A == 0){
            str =  gv.SBP2 + "／" + gv.DBP2 + "mmHg";
        }
        else{
            str = gv.BPressure2_A;
        }
        return str;
    }
    //@JavascriptInterface
    //public String SBP2()
    //{
    //    return gv.SBP2;
    //}
    //@JavascriptInterface
    //public String DBP2()
    //{
    //    return gv.DBP2;
    //}
    //@JavascriptInterface
    //public String BPressure2_A()
    //{
    //    return gv.BPressure2_A;
    //}
    @JavascriptInterface
    public String SpO22()
    {
        return gv.SpO22;
    }
    @JavascriptInterface
    public String Temperature2()
    {
        return gv.Temperature2;
    }
    @JavascriptInterface
    public String Consciousness3()
    {
        return gv.Consciousness3;
    }
    @JavascriptInterface
    public String LifeBreathing3()
    {
        return gv.LifeBreathing3;
    }
    @JavascriptInterface
    public String Pulse3()
    {
        return gv.Pulse3;
    }
    @JavascriptInterface
    public String GCS3_E()
    {
        return gv.GCS3_E;
    }
    @JavascriptInterface
    public String GCS3_V()
    {
        return gv.GCS3_V;
    }
    @JavascriptInterface
    public String GCS3_M()
    {
        return gv.GCS3_M;
    }
    @JavascriptInterface
    public String BP3()
    {
        String str;
        if(gv.PBPressure3_A == 0){
            str =  gv.SBP3 + "／" + gv.DBP3 + "mmHg";
        }
        else{
            str = gv.BPressure3_A;
        }
        return str;
    }
    //@JavascriptInterface
    //public String SBP3()
    //{
    //    return gv.SBP3;
    //}
    //@JavascriptInterface
    //public String DBP3()
    //{
    //    return gv.DBP3;
    //}
    //@JavascriptInterface
    //public String BPressure3_A()
    //{
    //    return gv.BPressure3_A;
    //}
    @JavascriptInterface
    public String SpO23()
    {
        return gv.SpO23;
    }
    @JavascriptInterface
    public String Temperature3()
    {
        return gv.Temperature3;
    }
    @JavascriptInterface
    public boolean LifeTime_T()
    {
        return !gv.HospitalAddress.equals("") && (gv.LifeTime1.equals("") || gv.LifeTime2.equals(""));
    }
    @JavascriptInterface
    public boolean Consciousness_T()
    {
        return !gv.HospitalAddress.equals("") && (gv.Consciousness1.equals("") || gv.Consciousness2.equals("") || gv.Consciousness3.equals(""));
    }
    @JavascriptInterface
    public boolean LifeBreathing_T()
    {
        return !gv.HospitalAddress.equals("") && (gv.LifeBreathing1.equals("") || gv.LifeBreathing2.equals("") || gv.LifeBreathing3.equals(""));
    }
    @JavascriptInterface
    public boolean Pulse_T()
    {
        return !gv.HospitalAddress.equals("") && (gv.Pulse1.equals("") || gv.Pulse2.equals("") || gv.Pulse3.equals(""));
    }
    @JavascriptInterface
    public boolean BP_T()
    {
        return !gv.HospitalAddress.equals("") && (gv.BPressure1_A.equals("") || gv.SBP2.equals("") || gv.DBP3.equals("") || gv.SBP3.equals("") || gv.DBP3.equals(""));
    }
    @JavascriptInterface
    public boolean GCS_T()
    {
        return !gv.HospitalAddress.equals("") && (gv.GCS1_E.equals("") || gv.GCS1_V.equals("") || gv.GCS1_M.equals("") || gv.GCS2_E.equals("") || gv.GCS2_V.equals("") || gv.GCS2_M.equals("") || gv.GCS3_E.equals("") || gv.GCS3_V.equals("") || gv.GCS3_M.equals(""));
    }
    @JavascriptInterface
    public boolean SpO2_T()
    {
        return !gv.HospitalAddress.equals("") && (gv.SpO21.equals("") || gv.SpO22.equals("") || gv.SpO23.equals(""));
    }
    @JavascriptInterface
    public boolean Temperature_T()
    {
        return !gv.HospitalAddress.equals("") && (gv.Temperature2.equals("") || gv.Temperature3.equals(""));
    }

    //手寫簽名
    @JavascriptInterface
    public String SNote()
    {
        return gv.SNote;
    }
    @JavascriptInterface
    public String SAStaff1()
    {
        return gv.SAStaff1;
    }
    @JavascriptInterface
    public String SAStaff2()
    {
        return gv.SAStaff2;
    }
    @JavascriptInterface
    public String SAStaff3()
    {
        return gv.SAStaff3;
    }
    @JavascriptInterface
    public String SAStaff4()
    {
        return gv.SAStaff4;
    }
    @JavascriptInterface
    public String SAStaff5()
    {
        return gv.SAStaff5;
    }
    @JavascriptInterface
    public String SAStaff6()
    {
        return gv.SAStaff6;
    }
    @JavascriptInterface
    public boolean SAStaff_T() {
        boolean CaedId = true;

        boolean[] img = {gv.SAStaff1.equals(""), gv.SAStaff2.equals(""), gv.SAStaff3.equals(""), gv.SAStaff4.equals(""), gv.SAStaff5.equals(""), gv.SAStaff6.equals("")};
        String[] value = {gv.AStaffNO1, gv.AStaffNO2, gv.AStaffNO3, gv.AStaffNO4, gv.AStaffNO5, gv.AStaffNO6};

        for (int x = 0; x < img.length; x++) {
            if (!img[x]) {
                String id = value[x];
                if (!id.matches("[a-zA-Z][1-2][0-9]{8}")) {
                    CaedId = false;
                    break;
                }

                String newId = id.toUpperCase();
                //身分證第一碼代表數值
                int[] headNum = new int[]{
                        1, 10, 19, 28, 37,
                        46, 55, 64, 39, 73,
                        82, 2, 11, 20, 48,
                        29, 38, 47, 56, 65,
                        74, 83, 21, 3, 12, 30};

                char[] headCharUpper = new char[]{
                        'A', 'B', 'C', 'D', 'E', 'F', 'G',
                        'H', 'I', 'J', 'K', 'L', 'M', 'N',
                        'O', 'P', 'Q', 'R', 'S', 'T', 'U',
                        'V', 'W', 'X', 'Y', 'Z'
                };

                int index = Arrays.binarySearch(headCharUpper, newId.charAt(0));
                int base = 8;
                int total = 0;
                for (int i = 1; i < 10; i++) {
                    int tmp = Integer.parseInt(Character.toString(newId.charAt(i))) * base;
                    total += tmp;
                    base--;
                }

                total += headNum[index];
                int remain = total % 10;
                int checkNum = (10 - remain) % 10;
                if (Integer.parseInt(Character.toString(newId.charAt(9))) != checkNum) {
                    CaedId = false;
                    break;
                }
            }
        }
        return (!gv.HospitalAddress.equals("") && (gv.SAStaff1.equals("") || gv.SAStaff2.equals(""))) || !CaedId;
    }
    @JavascriptInterface
    public String IClassification()
    {
        return gv.IClassification;
    }
    @JavascriptInterface
    public String SHStaff()
    {
        return gv.SHStaff;
    }
    @JavascriptInterface
    public boolean SHStaff_T()
    {
        return !gv.HospitalAddress.equals("") && (gv.IClassification.equals("") || gv.SHStaff.equals(""));
    }
    @JavascriptInterface
    public boolean cb_RHospital()
    {
        return gv.cb_RHospital;
    }
    @JavascriptInterface
    public String RHospital()
    {
        return gv.RHospital;
    }
    @JavascriptInterface
    public String RRelationship()
    {
        return gv.RRelationship;
    }
    @JavascriptInterface
    public String SRRelationship()
    {
        return gv.SRRelationship;
    }
    @JavascriptInterface
    public String Relationship()
    {
        return gv.Relationship;
    }
    @JavascriptInterface
    public String Phone()
    {
        return gv.Phone;
    }
    @JavascriptInterface
    public String SRelationship()
    {
        return gv.SRelationship;
    }
    @JavascriptInterface
    public boolean SRelationship_T()
    {
        return !gv.HospitalAddress.equals("") && (gv.Relationship.equals("") || gv.Phone.equals("") || gv.SRelationship.equals(""));
    }
}
