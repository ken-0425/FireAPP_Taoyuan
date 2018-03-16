/*
 * Copyright (C) 2012 TaiDoc Technology Corporation. All rights reserved.
 */

package tw.com.mygis.fireapp_taoyuan;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.taidoc.pclinklibrary.android.bluetooth.util.BluetoothUtil;
import com.taidoc.pclinklibrary.connection.AndroidBluetoothConnection;
import com.taidoc.pclinklibrary.connection.AndroidBluetoothConnection.LeConnectedListener;
import com.taidoc.pclinklibrary.connection.util.ConnectionManager;
import com.taidoc.pclinklibrary.constant.PCLinkLibraryConstant;
import com.taidoc.pclinklibrary.constant.PCLinkLibraryEnum.User;
import com.taidoc.pclinklibrary.exceptions.CommunicationTimeoutException;
import com.taidoc.pclinklibrary.exceptions.ExceedRetryTimesException;
import com.taidoc.pclinklibrary.exceptions.NotConnectSerialPortException;
import com.taidoc.pclinklibrary.exceptions.NotSupportMeterException;
import com.taidoc.pclinklibrary.meter.AbstractMeter;
import com.taidoc.pclinklibrary.meter.record.AbstractRecord;
import com.taidoc.pclinklibrary.meter.record.TemperatureRecord;
import com.taidoc.pclinklibrary.meter.util.MeterManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import library.JSONPost;

/**
 * PCLinkLibrary command test activity
 *
 * @author Jay Lee
 */
public class BleTemperature extends AppCompatActivity {
    // Message types sent from the meterCommuHandler Handler
    public static final int MESSAGE_STATE_CONNECTING = 1;
    public static final int MESSAGE_STATE_CONNECT_FAIL = 2;
    public static final int MESSAGE_STATE_CONNECT_DONE = 3;
    public static final int MESSAGE_STATE_CONNECT_NONE = 4;
    public static final int MESSAGE_STATE_CONNECT_METER_SUCCESS = 5;

    public static final int MESSAGE_STATE_CHECK_METER_BT_DISTENCE = 7;
    public static final int MESSAGE_STATE_CHECK_METER_BT_DISTENCE_FAIL = 8;
    public static final int MESSAGE_STATE_NOT_SUPPORT_METER = 9;
    public static final int MESSAGE_STATE_NOT_CONNECT_SERIAL_PORT = 10;
    public static final int MESSAGE_STATE_SCANED_DEVICE = 11;

    // Tag and Debug flag
    private static final boolean DEBUG = true;
    private static final String TAG = "BleTemperature";
    private TextView data ;
    private String tem="null";
    // Views
    private ProgressDialog mProcessDialog = null;
    private String number;

    //座標
    private String Longitude ;
    private String Latitude ;



    /**
     * BT Transfer type, Type I, Type II or PL2303
     */
    private String mBtTransferType;

    /**
     * Android BT connection
     */
    private AndroidBluetoothConnection mConnection;




    /**
     * 控制Meter連通時以UI互動的Handler
     */
    private final Handler meterCommuHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MESSAGE_STATE_CONNECTING:
                    mProcessDialog = ProgressDialog.show(BleTemperature.this, null,
                            "搜尋裝置", true);
                    mProcessDialog.setCancelable(false);
                    break;
                case MESSAGE_STATE_SCANED_DEVICE:
                    // 取得Bluetooth Device資訊
                    final BluetoothDevice device = BluetoothUtil.getPairedDevice(mConnection.getConnectedDeviceAddress());
                    // Attempt to connect to the device
                    mConnection.LeConnect(getApplicationContext(), device);
                    // 在mLeConnectedListener會收
                    break;
                case MESSAGE_STATE_CONNECT_DONE:
                    dimissProcessDialog();

                    break;
                case MESSAGE_STATE_CONNECT_FAIL:
                    dimissProcessDialog();
                    Toast.makeText(getBaseContext(), "設備連線失敗", Toast.LENGTH_SHORT).show();

                    break;
                case MESSAGE_STATE_CONNECT_NONE:
                    //showAlertDialog(BleTemperature.this, "00000");
                    dimissProcessDialog();


                    //??
                    //   GuiUtils.goToPCLinkLibraryHomeActivity(BleTemperature.this);

                    break;
                case MESSAGE_STATE_CONNECT_METER_SUCCESS:
                    Toast.makeText(getBaseContext(), "connect_meter_success", Toast.LENGTH_SHORT).show();


                    break;
                case MESSAGE_STATE_CHECK_METER_BT_DISTENCE:
                    ProgressDialog baCmdDialog = new ProgressDialog(
                            BleTemperature.this);
                    baCmdDialog.setCancelable(false);
                    baCmdDialog.setMessage("send ba command");
                    baCmdDialog.setButton(DialogInterface.BUTTON_POSITIVE, "cancel",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // Use either finish() or return() to either close the activity
                                    // or just
                                    // the dialog
                                    dialog.dismiss();

                                    return;
                                }
                            });
                    baCmdDialog.show();
                    break;
                case MESSAGE_STATE_CHECK_METER_BT_DISTENCE_FAIL:
                    Toast.makeText(getBaseContext(), "check_bt_fail", Toast.LENGTH_SHORT).show();

                    break;
                case MESSAGE_STATE_NOT_SUPPORT_METER:
                    dimissProcessDialog();
                    Toast.makeText(getBaseContext(), "連結失敗", Toast.LENGTH_SHORT).show();

                    break;
                case MESSAGE_STATE_NOT_CONNECT_SERIAL_PORT:
                    Toast.makeText(getBaseContext(), "not_connect_serial_port", Toast.LENGTH_SHORT).show();

                    break;
            } /* end of switch */
        }
    };
    private boolean mBLEMode;
    private String mMacAddress;
    private AbstractMeter mTaiDocMeter = null;
    // Handlers
    // The Handler that gets information back from the android bluetooth connection
    private final Handler mBTConnectionHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            try {
                switch (msg.what) {
                    case PCLinkLibraryConstant.MESSAGE_STATE_CHANGE:
                        if (DEBUG) {
                            Log.i(TAG, "MESSAGE_STATE_CHANGE: " + msg.arg1);
                        } /* end of if */
                        switch (msg.arg1) {
                            case AndroidBluetoothConnection.STATE_CONNECTED_BY_LISTEN_MODE:
                                try {
                                    mTaiDocMeter = MeterManager.detectConnectedMeter(mConnection);
                                } catch (Exception e) {
                                    throw new NotSupportMeterException();
                                }
                                dimissProcessDialog();

                                if (mTaiDocMeter == null) {
                                    throw new NotSupportMeterException();
                                }/* end of if */
                                break;
                            case AndroidBluetoothConnection.STATE_CONNECTING:
                                // 暫無需特別處理的事項
                                break;
                            case AndroidBluetoothConnection.STATE_SCANED_DEVICE:
                                meterCommuHandler.sendEmptyMessage(MESSAGE_STATE_SCANED_DEVICE);
                                break;
                            case AndroidBluetoothConnection.STATE_LISTEN:
                                // 暫無需特別處理的事項
                                break;
                            case AndroidBluetoothConnection.STATE_NONE:
                                // 暫無需特別處理的事項
                                break;
                        } /* end of switch */
                        break;
                    case PCLinkLibraryConstant.MESSAGE_TOAST:
                        // 暫無需特別處理的事項
                        break;
                    default:
                        break;
                } /* end of switch */
            } catch (NotSupportMeterException e) {
                Log.e(TAG, "not support meter", e);
                Toast.makeText(getBaseContext(), "not_support_meter01", Toast.LENGTH_SHORT).show();

            } /* end of try-catch */
        }
    };
    private LeConnectedListener mLeConnectedListener = new LeConnectedListener() {

        @Override
        public void onConnectionTimeout() {
            dimissProcessDialog();

            Toast.makeText(getBaseContext(), "沒連結到設備", Toast.LENGTH_SHORT).show();
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mTaiDocMeter.turnOffMeterOrBluetooth(0);
                    onBackPressed();
                }
            });
        }

        @Override
        public void onConnectionStateChange_Disconnect(BluetoothGatt gatt,
                                                       int status, int newState) {
            dimissProcessDialog();


        }

        @SuppressLint("NewApi")
        @Override
        public void onDescriptorWrite_Complete(BluetoothGatt gatt,
                                               BluetoothGattDescriptor descriptor, int status) {
            mConnection.LeConnected(gatt.getDevice());
        }

        @Override
        public void onCharacteristicChanged_Notify(BluetoothGatt gatt,
                                                   BluetoothGattCharacteristic characteristic) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    Looper.prepare();

                    try {
                        mTaiDocMeter = MeterManager.detectConnectedMeter(mConnection);
                    } catch (Exception e) {

                        meterCommuHandler.sendEmptyMessage(MESSAGE_STATE_NOT_SUPPORT_METER);

                    }

                    BleTemperature.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            dimissProcessDialog();
                            getvalueBT();

                            if (mTaiDocMeter == null) {
                                //throw new NotSupportMeterException();
                                meterCommuHandler.sendEmptyMessage(MESSAGE_STATE_NOT_SUPPORT_METER);
                            }
                        }
                    });

                    Looper.loop();
                }
            }).start();
        }

        @Override
        public void onCharacteristicChanged(BluetoothGatt gatt,
                                            BluetoothGattCharacteristic characteristic) {
            // TODO Auto-generated method stub

        }
    };

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.lay_temperature);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setLogo(R.drawable.toolbar_ic);

        GlobalVariable globalVariable = (GlobalVariable) getApplicationContext();
        number = globalVariable.FNumber;
        toolbar.setTitle(number);
        setSupportActionBar(toolbar);

        data = (TextView) findViewById(R.id.data_value_spo2);


       // jsonReadTemp();


    }



    private void btSave() {
        GlobalVariable globalVariable = (GlobalVariable) getApplicationContext();


        if(!tem.equals("null")) {

            globalVariable.Temperature2 = tem;
            main_tab5_DBSave();



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
                        //    mTaiDocMeter.turnOffMeterOrBluetooth(0);
                    BleTemperature.this.finish();
                        }
                    });

                }
            }).start();

        }

    }

    private void updatePairedList() {
        Map<String, String> addrs = new HashMap<String, String>();
        String addrKey = GlobalVariable.BLE_PAIRED_METER_ADDR_ + String.valueOf(0);
        addrs.put(addrKey, mMacAddress);
        mConnection.updatePairedList(addrs, 1);
    }

    /**
     * Connect Meter
     */
    private void connectMeter() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Looper.prepare();
                try {
                    meterCommuHandler.sendEmptyMessage(MESSAGE_STATE_CONNECTING);


                    if (mBLEMode) {
                        updatePairedList();
                        mConnection.setLeConnectedListener(mLeConnectedListener);

                        if (mConnection.getState() == AndroidBluetoothConnection.STATE_NONE) {
                            // Start the Android Bluetooth connection services to listen mode
                            mConnection.LeListen();

                            if (DEBUG) {
                                Log.i(TAG, "into listen mode");
                            }
                        }

                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                if (mConnection.getState() == AndroidBluetoothConnection.STATE_LISTEN) {
                                    if (mLeConnectedListener != null) {
                                        mLeConnectedListener.onConnectionTimeout();
                                    }
                                }
                            }
                        }, 5000);
                    } else {
                        // Only if the state is STATE_NONE, do we know that we haven't started
                        // already
                        if (mConnection.getState() == AndroidBluetoothConnection.STATE_NONE) {
                            // Start the Android Bluetooth connection services to listen mode
                            mConnection.listen();

                            if (DEBUG) {
                                Log.i(TAG, "into listen mode");
                            }
                        }
                    }


                } catch (CommunicationTimeoutException e) {
                    Log.e(TAG, e.getMessage(), e);
                    meterCommuHandler.sendEmptyMessage(MESSAGE_STATE_CONNECT_FAIL);
                } catch (NotSupportMeterException e) {
                    Log.e(TAG, "not support meter", e);
                    meterCommuHandler.sendEmptyMessage(MESSAGE_STATE_NOT_SUPPORT_METER);
                } catch (NotConnectSerialPortException e) {
                    meterCommuHandler.sendEmptyMessage(MESSAGE_STATE_NOT_CONNECT_SERIAL_PORT);
                } catch (ExceedRetryTimesException e) {

                    meterCommuHandler.sendEmptyMessage(MESSAGE_STATE_NOT_SUPPORT_METER);

                }
                Looper.loop();
            }
        }).start();
    }

    /**
     * // 關閉Process dialog
     */
    private void dimissProcessDialog() {
        if (mProcessDialog != null) {
            mProcessDialog.dismiss();
            mProcessDialog = null;
        } /* end of if */
    }

    /**
     * 關閉Meter
     */
    private void disconnectMeter() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Looper.prepare();
                try {
                    if (mTaiDocMeter != null) {
                        mTaiDocMeter.turnOffMeterOrBluetooth(0);
                    }

                    if (mBLEMode) {
                        mConnection.setLeConnectedListener(null);
                        mConnection.LeDisconnect();
                    } else {
                        mConnection.disconnect();
                        mConnection.LeDisconnect();
                    }
                } catch (Exception e) {
                    Log.e(TAG, e.getMessage(), e);
                } finally {
                }/* end of try-catch-finally */
                Looper.loop();
            }
        }).start();
    }

    /**
     * 取得共用設定值
     */
    private void getSharedPreferencesSettings() {
        SharedPreferences settings = getSharedPreferences(
                GlobalVariable.SHARED_PREFERENCES_NAME, 0);
        mMacAddress = settings.getString(GlobalVariable.BLE_PAIRED_BT_ADDREES, "");
        mBtTransferType = "TypeTwo";

        mBLEMode = true;

    }


    /**
     * 初始化 Android Bluetooth Connection
     */
    private void setupAndroidBluetoothConnection() {
        if (mConnection == null) {
            Log.d(TAG, "setupAndroidBluetoothConnection()");
            // 這裡一定要用一個try-catch, 因為在4.3以前是無法用ble的,會造成runtime error
            try {
                mConnection = ConnectionManager.createAndroidBluetoothConnection(mBTConnectionHandler);
                mConnection.canScanV3KNV(false);
            } catch (Exception ee) {
            }

        } /* end of if */
    }


    @Override
    protected void onStart() {
        super.onStart();

        getSharedPreferencesSettings();


        if ("".equals(mMacAddress)) {
            // 如果是用listen且meter支援ble的話則進入
            if (getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
                setupAndroidBluetoothConnection();
                connectMeter();
            } else {
                Toast.makeText(getBaseContext(), "pair_meter_first", Toast.LENGTH_SHORT).show();

            }
        } else if ("".equals(mBtTransferType)) {
            Toast.makeText(getBaseContext(), "meter_trans_type_fail", Toast.LENGTH_SHORT).show();

        } else if (mTaiDocMeter == null) {
            setupAndroidBluetoothConnection();
            connectMeter();
        }

    }

    @Override
    protected void onStop() {
        super.onStop();

        disconnectMeter();
        dimissProcessDialog();
    }

    //放資料
    private void getvalueBT() {

        try {
        // Get Latest Measurement Record
        AbstractRecord record = mTaiDocMeter.getStorageDataRecord(0,
                User.CurrentUser);
        //   layout = getLatestRecord(inflater, formatterDate, record);
        double thermometerValue = ((TemperatureRecord) record)
                .getObjectTemperatureValue();


        DecimalFormat df = new DecimalFormat("#.##");

         tem = df.format(thermometerValue);

        data.setText(tem);

        btSave();


        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
            meterCommuHandler.sendEmptyMessage(MESSAGE_STATE_CONNECT_FAIL);
            //   onStart();
        }
    }


    private void main_tab5_DBSave() {

        GlobalVariable gv = (GlobalVariable) getApplicationContext();
        SQLite SQLite = new SQLite(BleTemperature.this);
        SQLiteDatabase DB = SQLite.getWritableDatabase();
        ContentValues cv  = new ContentValues();

        cv.put("Temperature2", gv.Temperature2);

        DB.update("MainTab5", cv, "CaseID=? AND Subno=?", new String[]{gv.FNumber, gv.DrawerItemID});
        SQLite.close();
        DB.close();
    }
}
