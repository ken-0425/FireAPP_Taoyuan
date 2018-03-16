package tw.com.mygis.fireapp_taoyuan;


import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.SimpleExpandableListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import library.JSONPost;
import library.UserFunctions;

public class BleBloodSugar extends AppCompatActivity {

    public static final String EXTRAS_DEVICE_NAME = "DEVICE_NAME";
    public static final String EXTRAS_DEVICE_ADDRESS = "DEVICE_ADDRESS";
    private final static String TAG = BleBloodSugar.class.getSimpleName();
    private static String length = "no_of_rows";
    private static String KEY_value = "value";
    private static String KEY_info = "eatinfo";
    private static String KEY_created_at = "created_at";
    private final String LIST_NAME = "NAME";
    private final String LIST_UUID = "UUID";
    String eatinfo = "0";
    List<Map<String, Object>> items = new ArrayList<Map<String, Object>>();
    int sqlLength;
    private String number;

    private String bgdata="null";


    private Button bt1,bt2,bt3;
    private String mDeviceAddress,mDeviceNumber;
    private boolean mConnected = false;
    private ServiceBluetoothLe mBluetoothLeService;


    //宣告Service程式
    private ServiceBluetoothSearch searchService;



    //宣告ServiceConnection程式serviceConnection
    private ServiceConnection serviceConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName className, IBinder service) {
            searchService = ((ServiceBluetoothSearch.SearchServiceBinder)service).getService();
        }

        public void onServiceDisconnected(ComponentName className) {
            searchService = null;

        }
    };

    //註冊廣播
    private final BleBloodSugar.BluetoothReceiver receiver = new BleBloodSugar.BluetoothReceiver();
    //接收廣播
    private class BluetoothReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {


            String message = intent.getStringExtra("type");

            if (message.equals("Temp")) {
                intent = new Intent(BleBloodSugar.this, BleTemperature.class);

                startActivity(intent);
                unbindService(serviceConnection);
                searchService.stopSelf(); 	//Stop Service//Stop Service
                unregisterReceiver(receiver);
                finish();

            }else if(message.equals("BG")){
                intent= new Intent(BleBloodSugar.this, BleBloodSugar.class);

                startActivity(intent);
                unbindService(serviceConnection);
                searchService.stopSelf(); 	//Stop Service//Stop Service
                unregisterReceiver(receiver);
                finish();

            }else if(message.equals("BP")){
                intent = new Intent(BleBloodSugar.this, BleBloodPressure.class);

                startActivity(intent);
                unbindService(serviceConnection);
                searchService.stopSelf(); 	//Stop Service//Stop Service
                unregisterReceiver(receiver);
                finish();
            }else if(message.equals("SPO2")){
                intent = new Intent(BleBloodSugar.this, BleBloodOxygen.class);

                startActivity(intent);
                unbindService(serviceConnection);
                searchService.stopSelf(); 	//Stop Service//Stop Service
                unregisterReceiver(receiver);
                finish();
            }else if(message.equals("BG_PC")) {
                intent = new Intent(getBaseContext(), BleBloodSugar_pc.class);


                startActivity(intent);
                unbindService(serviceConnection);
                searchService.stopSelf();    //Stop Service//Stop Service
                unregisterReceiver(receiver);
                finish();
            }else if(message.equals("BG_N")) {
                intent = new Intent(getBaseContext(), BleBloodSugar.class);


                startActivity(intent);
                unbindService(serviceConnection);
                searchService.stopSelf();    //Stop Service//Stop Service
                unregisterReceiver(receiver);
                finish();
            }
        }
    }



    //管理服務生命週期的代碼。
    // Code to manage Service lifecycle.
    private final ServiceConnection mServiceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName componentName, IBinder service) {
            mBluetoothLeService = ((ServiceBluetoothLe.LocalBinder) service).getService();
            if (!mBluetoothLeService.initialize()) {
                Log.e(TAG, "Unable to initialize Bluetooth");
                finish();
            }
            // Automatically connects to the device upon successful start-up initialization.
            mBluetoothLeService.connect(mDeviceAddress);
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            mBluetoothLeService = null;
        }
    };
    private TextView mDataField;


    //下面讀取數據用
    private ListView mListView;
    private SimpleAdapter simpleAdapter;
    //   private TextView mConnectionState;
    private TextView time;

    private ArrayList<ArrayList<BluetoothGattCharacteristic>> mGattCharacteristics =
            new ArrayList<ArrayList<BluetoothGattCharacteristic>>();
    private BluetoothGattCharacteristic mNotifyCharacteristic;
    // Handles various events fired by the Service.處理服務所激發的各種事件
    // ACTION_GATT_CONNECTED: connected to a GATT server.連接一個GATT服務
    // ACTION_GATT_DISCONNECTED: disconnected from a GATT server.從GATT服務中斷開連接
    // ACTION_GATT_SERVICES_DISCOVERED: discovered GATT services.查找GATT服務
    // ACTION_DATA_AVAILABLE: received data from the device.  This can be a result of read
    //                        or notification operations.從服務中接受數據
    private final BroadcastReceiver mGattUpdateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            if (ServiceBluetoothLe.ACTION_GATT_CONNECTED.equals(action)) {
                mConnected = true;

                invalidateOptionsMenu();


            } else if (ServiceBluetoothLe.ACTION_GATT_DISCONNECTED.equals(action)) {
                mConnected = false;
                invalidateOptionsMenu();
                clearUI();

                //發現有可支持的服務
            } else if (ServiceBluetoothLe.ACTION_GATT_SERVICES_DISCOVERED.equals(action)) {
                // 显示所有支持的service和characteristic。
                // Show all the supported services and characteristics on the user interface.
                displayGattServices(mBluetoothLeService.getSupportedGattServices());
                //顯示數據
            } else if (ServiceBluetoothLe.ACTION_DATA_AVAILABLE.equals(action)) {
                //將數據顯示在mDataField上
                displayData(intent.getStringExtra(ServiceBluetoothLe.EXTRA_DATA));
            }
        }
    };

    private static IntentFilter makeGattUpdateIntentFilter() {
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ServiceBluetoothLe.ACTION_GATT_CONNECTED);
        intentFilter.addAction(ServiceBluetoothLe.ACTION_GATT_DISCONNECTED);
        intentFilter.addAction(ServiceBluetoothLe.ACTION_GATT_SERVICES_DISCOVERED);
        intentFilter.addAction(ServiceBluetoothLe.ACTION_DATA_AVAILABLE);
        return intentFilter;
    }



    private void clearUI() {

        mDataField.setText("沒資料");

        //啓動服務程式ervice
        final Intent intent = new Intent(BleBloodSugar.this, ServiceBluetoothSearch.class);
        startService(intent);

        //綁縛(Bind)服務程式
        bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);


        //註冊廣播接收receiver
        IntentFilter filter = new IntentFilter(ServiceBluetoothSearch.ACTION);
        registerReceiver(receiver, filter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.lay_bloodsugarl);

        GlobalVariable globalVariable = (GlobalVariable) getApplicationContext();
        number = globalVariable.FNumber;


        //標題欄
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setLogo(R.drawable.toolbar_ic);

        toolbar.setTitle(number);
        setSupportActionBar(toolbar);





        SharedPreferences settings = getSharedPreferences(
                GlobalVariable.SHARED_PREFERENCES_NAME, 0);
        mDeviceAddress = settings.getString(GlobalVariable.BLE_PAIRED_BG_ADDREES, "");
        mDeviceNumber=settings.getString(GlobalVariable.BLE_PAIRED_BG_NUMBER, "");




        mDataField = (TextView) findViewById(R.id.data_value_spo2);


        Intent gattServiceIntent = new Intent(this, ServiceBluetoothLe.class);
        bindService(gattServiceIntent, mServiceConnection, BIND_AUTO_CREATE);



    }




//非必要維條

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(mGattUpdateReceiver);
    }

    //非必要維條
    @Override
    protected void onDestroy() {
        super.onDestroy();

        mBluetoothLeService = null;


    }


    //很重要
    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(mGattUpdateReceiver, makeGattUpdateIntentFilter());
        if (mBluetoothLeService != null) {
            final boolean result = mBluetoothLeService.connect(mDeviceAddress);
            Log.d(TAG, "Connect request result=" + result);
        }
    }



    private void displayData(String data) {
        if (data != null) {

            if ("5".equals(data.charAt(12) + "") && data.charAt(13) == '5') {

                mDataField.setText("讀取中");
            } else if (data.charAt(12) != '0' || data.charAt(13) != '0') {

                mDataField.setText("數據過高");
                bgdata="數據過高";
                bgSave();
            } else if (data.charAt(15) == '0' && data.charAt(16) == '0') {
                mDataField.setText("數據過低");
                bgdata="數據過低";
                bgSave();
            } else {

                String date01 = data.substring(15, 17);
                int date02 = Integer.parseInt(date01, 16);
                mDataField.setText(date02 + "");
                bgdata=date02 + "";
                bgSave();
            }

        }
    }

    // 演示如何迭代所支持的GATT Services/Characteristics.
    // 在这个例子中，我们填充绑定到ExpandableListView的数据结构。
    // Demonstrates how to iterate through the supported GATT Services/Characteristics.
    // In this sample, we populate the data structure that is bound to the ExpandableListView
    // on the UI.
    private void displayGattServices(List<BluetoothGattService> gattServices) {
        if (gattServices == null) return;
        String uuid = null;
        String unknownServiceString = getResources().getString(R.string.unknown_service);
        String unknownCharaString = getResources().getString(R.string.unknown_characteristic);
        ArrayList<HashMap<String, String>> gattServiceData = new ArrayList<HashMap<String, String>>();
        ArrayList<ArrayList<HashMap<String, String>>> gattCharacteristicData
                = new ArrayList<ArrayList<HashMap<String, String>>>();
        mGattCharacteristics = new ArrayList<ArrayList<BluetoothGattCharacteristic>>();

        // 循环迭代可访问的GATT Services.
        // Loops through available GATT Services.
        for (BluetoothGattService gattService : gattServices) {
            HashMap<String, String> currentServiceData = new HashMap<String, String>();
            uuid = gattService.getUuid().toString();
            currentServiceData.put(
                    LIST_NAME, SampleGattAttributes.lookup(uuid, unknownServiceString));
            currentServiceData.put(LIST_UUID, uuid);
            gattServiceData.add(currentServiceData);

            ArrayList<HashMap<String, String>> gattCharacteristicGroupData =
                    new ArrayList<HashMap<String, String>>();
            List<BluetoothGattCharacteristic> gattCharacteristics =
                    gattService.getCharacteristics();
            ArrayList<BluetoothGattCharacteristic> charas =
                    new ArrayList<BluetoothGattCharacteristic>();
            // 循环迭代可访问的Characteristics.
            // Loops through available Characteristics.
            for (BluetoothGattCharacteristic gattCharacteristic : gattCharacteristics) {
                charas.add(gattCharacteristic);
                HashMap<String, String> currentCharaData = new HashMap<String, String>();
                uuid = gattCharacteristic.getUuid().toString();
                currentCharaData.put(
                        LIST_NAME, SampleGattAttributes.lookup(uuid, unknownCharaString));
                currentCharaData.put(LIST_UUID, uuid);
                gattCharacteristicGroupData.add(currentCharaData);
            }
            mGattCharacteristics.add(charas);
            gattCharacteristicData.add(gattCharacteristicGroupData);
        }

        SimpleExpandableListAdapter gattServiceAdapter = new SimpleExpandableListAdapter(
                this,
                gattServiceData,
                android.R.layout.simple_expandable_list_item_2,
                new String[]{LIST_NAME, LIST_UUID},
                new int[]{android.R.id.text1, android.R.id.text2},
                gattCharacteristicData,
                android.R.layout.simple_expandable_list_item_2,
                new String[]{LIST_NAME, LIST_UUID},
                new int[]{android.R.id.text1, android.R.id.text2}
        );

        StartNotification();
    }

    //開啟廣播
    private void StartNotification() {


        if (mGattCharacteristics != null) {

            final BluetoothGattCharacteristic characteristic;

            if (mDeviceNumber.equals("2")) {
                characteristic = mGattCharacteristics.get(3).get(3);

            }else {
                characteristic = mGattCharacteristics.get(5).get(1);
            }

            final int charaProp = characteristic.getProperties();
            if ((charaProp | BluetoothGattCharacteristic.PROPERTY_READ) > 0) {
                // If there is an active notification on a characteristic, clear
                // it first so it doesn't update the data field on the user interface.
                if (mNotifyCharacteristic != null) {
                    mBluetoothLeService.setCharacteristicNotification(
                            mNotifyCharacteristic, false);

                    mNotifyCharacteristic = null;
                }
                mBluetoothLeService.readCharacteristic(characteristic);
            }
            if ((charaProp | BluetoothGattCharacteristic.PROPERTY_NOTIFY) > 0) {
                mNotifyCharacteristic = characteristic;
                mBluetoothLeService.setCharacteristicNotification(
                        characteristic, true);

            }

        }


    }







    private void bgSave() {
        GlobalVariable globalVariable = (GlobalVariable) getApplicationContext();
        Intent intent ;


        if(!bgdata.equals("null")) {


            globalVariable.ODisposal[3] = "血糖監測@"+bgdata;

            main_tab3_DBSave();



            new Thread(new Runnable() {
                @Override
                public void run() {
                    //延迟两秒
                    try {
                        Thread.sleep( 2000 );
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            finish();
                        }
                    });

                }
            }).start();

        }

    }



    //急救處置main_tab3
    private void main_tab3_DBSave() {

        GlobalVariable gv = (GlobalVariable) getApplicationContext();
        SQLite SQLite = new SQLite(BleBloodSugar.this);
        SQLiteDatabase DB = SQLite.getWritableDatabase();
        ContentValues cv = new ContentValues();
        String str;


        str = "";
        for(String Content: gv.ODisposal){
            str += Content + "－";
        }
        if(!str.equals("")) {
            cv.put("ODisposal", str);
        }

        DB.update("MainTab3", cv, "CaseID=? AND Subno=?", new String[]{gv.FNumber, gv.DrawerItemID});
        SQLite.close();
        DB.close();
    }
}