package tw.com.mygis.fireapp_taoyuan;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationServices;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Bart on 2014/8/23.
 */
public class Drawer_Navigation extends Activity implements OnClickListener, ConnectionCallbacks, OnConnectionFailedListener {

    protected GoogleApiClient mGoogleApiClient;
    protected Location mLastLocation;
    //等待視窗
    private ProgressDialog PDialog = null;
    private AlertDialog.Builder dialog = null;

    private Button btn_Destination, btn_Hospital;
    private WebView wv_GoogleMap;
    private double X = 0, Y = 0;
    private String[] latlng_Hospital = new String[] {"", "25.061592, 121.367554", "24.977249, 121.268265", "25.003802, 121.325756", "24.877446, 121.235665", "24.982497, 121.311964", "25.016277, 121.306441", "24.946423, 121.205109", "24.916959, 121.155825", "24.909080, 121.156948", "24.969633, 121.107014", "24.962623, 121.228954"};
    //全域變數
    private GlobalVariable gv;
    /**
     * 側頁
     */
    private DrawerLayout dl_Drawer;
    private ActionBarDrawerToggle abdt_Drawer;

    private LinearLayout ll_Drawer;
    private ListView lv_Drawer_Subno, lv_Drawer_Item;
    // 記錄被選擇的選單指標用
    private int DrawerItemPosition = 0;
    private ArrayList<HashMap<String,String>> listItem = new ArrayList<HashMap<String,String>>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drawer_navigation);

        gv = (GlobalVariable) getApplicationContext();
        controls();
        setDrawerLayout();

        buildGoogleApiClient();
    }

    /**
     *  控制項設定
     */
    private void controls() {
        btn_Destination = (Button) findViewById(R.id.btn_Destination);
        btn_Hospital = (Button) findViewById(R.id.btn_Hospital);
        btn_Destination.setOnClickListener(Drawer_Navigation.this);
        btn_Hospital.setOnClickListener(Drawer_Navigation.this);
        wv_GoogleMap = (WebView) findViewById(R.id.wv_GoogleMap);
        wv_GoogleMap.getSettings().setSupportZoom(true);//縮放
        wv_GoogleMap.getSettings().setBuiltInZoomControls(true);//縮放
        wv_GoogleMap.getSettings().setJavaScriptEnabled(true);
        wv_GoogleMap.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        wv_GoogleMap.addJavascriptInterface(Drawer_Navigation.this, "APP_Function");
        wv_GoogleMap.loadUrl("file:///android_asset/Navigation.html");

        wv_GoogleMap.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                return true;
            }
        });
    }

    protected synchronized void buildGoogleApiClient()
    {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    @Override
    protected void onStart()
    {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onStop()
    {
        super.onStop();
        if (mGoogleApiClient.isConnected())
        {
            mGoogleApiClient.disconnect();
        }
    }


    // 當 GoogleApiClient 連上 Google Play Service 後要執行的動作
    @Override
    public void onConnected(Bundle connectionHint)
    {
        // 這行指令在 IDE 會出現紅線，不過仍可正常執行，可不予理會
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (mLastLocation != null)
        {
            X = mLastLocation.getLongitude();
            Y = mLastLocation.getLatitude();
            //Toast.makeText(this, String.format("%f *** % f", X, Y), Toast.LENGTH_LONG).show();
        }
        else
        {
            Toast.makeText(this, "偵測不到定位，請確認定位功能已開啟。", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onConnectionFailed(ConnectionResult result)
    {

    }


    @Override
    public void onConnectionSuspended(int cause)
    {
        mGoogleApiClient.connect();
    }

    public void onClick(View v) {
        buildGoogleApiClient();
        if (v == btn_Destination) {
            if (!gv.LocationHappen.equals("")) {
                wv_GoogleMap.loadUrl("javascript:GoogleMap.address('" + gv.LocationHappen + "')");
            } else {
                Toast.makeText(Drawer_Navigation.this, "發生地點欄位空白!!", Toast.LENGTH_LONG).show();
            }
        } else if (v == btn_Hospital) {
            if (!gv.HospitalAddress.equals("")) {
                wv_GoogleMap.loadUrl("javascript:GoogleMap.calcRoute(new google.maps.LatLng(" + Y + ", " + X + "), new google.maps.LatLng(" + latlng_Hospital[gv.PHospitalAddress] + "))");
                //wv_GoogleMap.loadUrl("javascript:GoogleMap.address('" + gv.HospitalAddress + "')");
            } else {
                Toast.makeText(Drawer_Navigation.this, "請選擇送往醫院!!", Toast.LENGTH_LONG).show();
            }
        }
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

        SQLite SQLite = new SQLite(Drawer_Navigation.this);
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
                Drawer_Navigation.this,
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
                Drawer_Navigation.this,
                R.layout.listview_function,
                new int[]{R.drawable.function_add, R.drawable.function_preview, R.drawable.function_signature},
                new String[]{"新增表單", "預覽", "簽名"},
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

            PDialog = ProgressDialog.show(Drawer_Navigation.this, "請稍等...", "切換表單中...", true);

            gv.DrawerItemID = data.get("Subno").toString();
            gv.DrawerItemPosition = position;

            new Thread(R_SwitchTable).start();
        }
    }

    private void selectItem(int position) {
        Intent intent = new Intent();
        switch (position){
            case 0:
                PDialog = ProgressDialog.show(Drawer_Navigation.this, "請稍等...", "建立表單中...", true);
                new Thread(R_NewTable).start();
                break;
            case 1:
                intent.setClass(Drawer_Navigation.this, Drawer_Preview.class);
                startActivity(intent);
                Drawer_Navigation.this.finish();
                break;
            case 2:
                intent.setClass(Drawer_Navigation.this, Drawer_Handwrite.class);
                startActivity(intent);
                Drawer_Navigation.this.finish();
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

    /**
     * 返回上一頁
     *
     * @return
     */
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if ((keyCode == KeyEvent.KEYCODE_BACK)) {   //確定按下退出鍵
            Intent intent = new Intent();
            intent.setClass(Drawer_Navigation.this, MainActivity.class);
            startActivity(intent);
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @JavascriptInterface
    public double X()
    {
        return X;
    }
    @JavascriptInterface
    public double Y()
    {
        return Y;
    }
    @JavascriptInterface
    public boolean NError()
    {
        Toast.makeText(Drawer_Navigation.this, "地址有誤無法導航!", Toast.LENGTH_LONG).show();
        return true;
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
                    Drawer_Navigation.this,
                    listItem,
                    R.layout.listview_subno,
                    gv.DrawerItemPosition,
                    new String[]{"SubnoID"},
                    new int[]{R.id.iv_Select, R.id.tv_SubnoID, R.id.btn_Delete}
            );

            lv_Drawer_Subno.setAdapter(SubnoAdapter);

            // 關掉
            dl_Drawer.closeDrawer(ll_Drawer);
            PDialog.dismiss();
        }
    };

    Runnable R_NewTable = new Runnable() {
        @Override
        public void run() {
            // TODO: http request.
            SQLite SQLite = new SQLite(Drawer_Navigation.this);
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
                    Drawer_Navigation.this,
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
}
