package tw.com.mygis.fireapp_taoyuan;

import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Binder;
import android.os.IBinder;
import android.os.StrictMode;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.taidoc.pclinklibrary.android.bluetooth.util.BluetoothUtil;
import com.taidoc.pclinklibrary.interfaces.BleUtilsListener;
import com.taidoc.pclinklibrary.util.BleUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import library.JSONPost;

public class ServiceBluetoothSearch extends Service {
    private static final long SCAN_PERIOD = 10000;
    public static String ACTION = "equipment_type";
    private final String FORA_IR21 = "C0:26:DF:01:9B:D8";
    private final String FORA_IR40 = "C0:26:DF:01:9E:5A";
    private final String ClinkBlood = "C6:05:04:03:75:B7";

    private final String ClinkBlood2 = "94:E3:6D:86:19:83";
    private final String FORA_GD40 = "C0:26:DF:01:68:30";

 //   private final String FORA_D40 = "C0:26:DF:01:E6:3C";
    private final String FORA_D40 = "C0:26:DF:01:EA:49";

    private final String FORA_D40b = "C0:26:DF:01:63:91";
    private final String TAIDOC_TD8255 = "C0:26:DF:01:A3:9D";
    private final String FORA_W310 = "C0:26:DF:00:7A:7E";
    private BluetoothAdapter mAdapter;
    private List<BluetoothDevice> mSearchedDevices;
    private Map<String, String> mPairedMeterAddrs;
    private BleUtils mBleUtils;
    private String equipment_type;
    //藍芽資料
    private BleUtilsListener mBleUtilsListener = new BleUtilsListener() {

        @Override
        public void onScanned(BluetoothDevice device, int rssi) {

            if (device != null && !TextUtils.isEmpty(device.getName())) {


                if (!mPairedMeterAddrs.containsValue(device.getAddress()) &&
                        !mSearchedDevices.contains(device)) {
                    mSearchedDevices.add(device);
                    String nameValue = device.getName();
                    String addrValue = device.getAddress();

                    mPairedMeterAddrs.put(addrValue, nameValue);


                    Toast.makeText(getBaseContext(), "偵測到設備", Toast.LENGTH_SHORT).show();

                    //單機測試
                    test(addrValue);
                    //檢查網路
                   // checkNetWork(addrValue);
                }
            }
        }

        @Override
        public void onLost(BluetoothDevice device) {
            if (device != null && !TextUtils.isEmpty(device.getName())) {
                if (mSearchedDevices.contains(device)) {
                    mSearchedDevices.remove(device);

                }
            }
        }
    };
    public ServiceBluetoothSearch() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        Toast toast = Toast.makeText(getApplicationContext(), "onBind()", Toast.LENGTH_SHORT);
       // toast.show();

        if (mSearchedDevices.size() > 0) {
            mSearchedDevices.clear();
        }


        if (mBleUtils != null) {
            mBleUtils.scanLeDevice(true);
        }

        return new SearchServiceBinder();
    }


    //Service程式-onCreate()
    @Override
    public void onCreate() {
        super.onCreate();

        mSearchedDevices = new ArrayList<BluetoothDevice>();
        mPairedMeterAddrs = new HashMap<String, String>();

        mAdapter = BluetoothUtil.getBluetoothAdapter();
        //mLeAcceptHandler = new LeAcceptHandler(this);

        mBleUtils = new BleUtils(mAdapter, mBleUtilsListener);

        if (mBleUtils != null) {
            mBleUtils.initScanner();
        }
    }


    //Service程式-onDestroy()
    @Override
    public void onDestroy() {
        super.onDestroy();

    }


    //Service程式-onRebind()
    @Override
    public void onRebind(Intent intent) {

        if (mSearchedDevices.size() > 0) {
            mSearchedDevices.clear();
        }


        if (mBleUtils != null) {
            mBleUtils.scanLeDevice(true);
        }
    }

    //Service程式-onUnbind()
    @Override
    public boolean onUnbind(Intent intent) {

        if (mBleUtils != null) {
            mBleUtils.scanLeDevice(false);
        }

        return true; //當再度自Client接口時，呼叫 onRebind的場合會回覆 true
    }
//廣播
    public void broadcasting(String addrValue, String type) {
        GlobalVariable globalVariable = (GlobalVariable) getApplicationContext();

        if (globalVariable.BleTemp && type.equals("Temp")) {
            SharedPreferences settings = getSharedPreferences(
                    GlobalVariable.SHARED_PREFERENCES_NAME, 0);

            settings.edit()
                    .putString(GlobalVariable.BLE_PAIRED_BT_ADDREES,
                            addrValue).commit();

            Intent intent = new Intent(ACTION);
            intent.putExtra("type", type);
            sendBroadcast(intent);

        } else if (globalVariable.BleBG && type.equals("BG")) {
            SharedPreferences settings = getSharedPreferences(
                    GlobalVariable.SHARED_PREFERENCES_NAME, 0);

            settings.edit()
                    .putString(GlobalVariable.BLE_PAIRED_BG_ADDREES, addrValue)
                    .putString(GlobalVariable.BLE_PAIRED_BG_NUMBER, "1")
                    .commit();

            Intent intent = new Intent(ACTION);
            intent.putExtra("type", type);
            sendBroadcast(intent);
        } else if (globalVariable.BleBP && type.equals("BP")) {
            SharedPreferences settings = getSharedPreferences(
                    GlobalVariable.SHARED_PREFERENCES_NAME, 0);

            settings.edit()
                    .putString(GlobalVariable.BLE_PAIRED_BP_ADDREES,
                            addrValue).commit();

            Intent intent = new Intent(ACTION);
            intent.putExtra("type", type);
            sendBroadcast(intent);
        } else if (globalVariable.BleSPO2 && type.equals("SPO2")) {
            SharedPreferences settings = getSharedPreferences(
                    GlobalVariable.SHARED_PREFERENCES_NAME, 0);

            settings.edit()
                    .putString(GlobalVariable.BLE_PAIRED_SPO2_ADDREES,
                            addrValue).commit();

            Intent intent = new Intent(ACTION);
            intent.putExtra("type", type);
            sendBroadcast(intent);
        } else if (globalVariable.BleBW && type.equals("BW")) {
            SharedPreferences settings = getSharedPreferences(
                    GlobalVariable.SHARED_PREFERENCES_NAME, 0);

            settings.edit()
                    .putString(GlobalVariable.BLE_PAIRED_BW_ADDREES,
                            addrValue).commit();

            Intent intent = new Intent(ACTION);
            intent.putExtra("type", type);
            sendBroadcast(intent);
        }else if ( type.equals("BCU")) {
            SharedPreferences settings = getSharedPreferences(
                    GlobalVariable.SHARED_PREFERENCES_NAME, 0);

            settings.edit()
                    .putString(GlobalVariable.BLE_PAIRED_BCU_ADDREES,
                            addrValue).commit();

            Intent intent = new Intent(ACTION);
            intent.putExtra("type", type);
            sendBroadcast(intent);
        }else if ( type.equals("BG_PC")) {
            SharedPreferences settings = getSharedPreferences(
                    GlobalVariable.SHARED_PREFERENCES_NAME, 0);

            settings.edit()
                    .putString(GlobalVariable.BLE_PAIRED_BG_ADDREES,
                            addrValue).commit();

            Intent intent = new Intent(ACTION);
            intent.putExtra("type", type);
            sendBroadcast(intent);
        }else if ( type.equals("BG_N")) {
            SharedPreferences settings = getSharedPreferences(
                    GlobalVariable.SHARED_PREFERENCES_NAME, 0);

            settings.edit()
                    .putString(GlobalVariable.BLE_PAIRED_BG_ADDREES, addrValue)
                    .putString(GlobalVariable.BLE_PAIRED_BG_NUMBER, "2")
                    .commit();

            Intent intent = new Intent(ACTION);
            intent.putExtra("type", type);
            sendBroadcast(intent);
        }

    }

    //設備驗證

    public void jsonGetAddress(String address) {

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);


        GlobalVariable globalVariable = (GlobalVariable) getApplicationContext();
        String number = globalVariable.UserID;

        JSONPost jsonPost = new JSONPost();

        Log.d("Button", "Login");

        JSONObject json = jsonPost.getBleAddress(number, address);

        // check for login response
        try {
            if (json.getString("KEY_SUCCESS") != null) {

                String res = json.getString("KEY_SUCCESS");
                if (res.equals("true")) {

                    equipment_type = json.getString("Category");
                    //發送廣播
                    broadcasting(address, equipment_type);

                } else {
                    // Error in login

                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    class SearchServiceBinder extends Binder {
        ServiceBluetoothSearch getService() {
            return ServiceBluetoothSearch.this;
        }
    }


    //檢查網路
    private void checkNetWork(String addrValue) {
        ConnectivityManager mConnectivityManager =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();

        if (mNetworkInfo != null) {
            //網路是否已連線
            mNetworkInfo.isConnected();

            //驗證設備
          //  jsonGetAddress(addrValue);

        } else {
        Toast.makeText(getBaseContext(),"請開啟網路連線功能", Toast.LENGTH_SHORT).show();
        }
    }

    //單機無網路版
    public void test(String address){
        if(address.equals(FORA_D40b)||address.equals(FORA_GD40)){
            broadcasting(address,"BG_PC");
        }else if(address.equals(FORA_D40)){
            broadcasting(address,"BP");
        }else if(address.equals(FORA_IR21)||address.equals(FORA_IR40)){
            broadcasting(address,"Temp");
        }else if(address.equals(FORA_W310 )){
            broadcasting(address,"BW");
        }else if(address.equals(TAIDOC_TD8255)){
            broadcasting(address,"SPO2");
        }else if(address.equals(ClinkBlood)){
            broadcasting(address,"BG");
        }else if(address.equals(ClinkBlood2)){
            broadcasting(address,"BG_N");
        }


    }

}