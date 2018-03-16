package tw.com.mygis.fireapp_taoyuan;


import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import pub.devrel.easypermissions.EasyPermissions;
import serial.jni.BluetoothConnect;
import serial.jni.DataUtils;
import serial.jni.GLView;
import serial.jni.NativeCallBack;
import serial.jni.UsbHidConnect;

public class BleECG extends AppCompatActivity implements  EasyPermissions.PermissionCallbacks{
	private GLView glView;
	private DataUtils data;
	private Context mContext;
	private static UsbManager mUsbManager;
	private static UsbDevice mUsbDevice;
	private String strCase;

	public static final String EXTRAS_DEVICE_NAME = "DEVICE_NAME";
	public static final String EXTRAS_DEVICE_ADDRESS = "DEVICE_ADDRESS";

	private Dialog dialog;

	boolean getStart=true;

	private BluetoothAdapter mBluetoothAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		setContentView(R.layout.lay_ecg);
		mContext = this;

		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		toolbar.setLogo(R.drawable.toolbar_ic);

		GlobalVariable globalVariable = (GlobalVariable)getApplicationContext();
		String number = globalVariable.FNumber;
		toolbar.setTitle(number);
		setSupportActionBar(toolbar);


		DisplayMetrics dm = new DisplayMetrics();
		// 取得窗口属性
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		int width = dm.widthPixels;// 屏幕宽度
		int height = dm.heightPixels;// 屏幕高度
		Log.e("Activity WxH", width + "x" + height);
		Log.e("Density", "" + dm.densityDpi);

		//要求權限
		readequipment();

		// Use this check to determine whether BLE is supported on the device.  Then you can
		// selectively disable BLE-related features.
		if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
			Toast.makeText(this, "不支持", Toast.LENGTH_SHORT).show();
			finish();
		}

		// 初始化獲得一個bluetoothManager

		final BluetoothManager bluetoothManager =
				(BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
		mBluetoothAdapter = bluetoothManager.getAdapter();

		// 确认设备支持蓝牙并且已经启用. 如果没有,
		// 显示一个对话框要求用户授权启用蓝牙.
		//並檢測設備是否支持藍牙

		if (mBluetoothAdapter == null) {
			Toast.makeText(this, "藍芽不支持", Toast.LENGTH_SHORT).show();
			finish();
			return;
		}



		/*
		Button b1 = (Button) this.findViewById(R.id.btn01);
		Button b2 = (Button) this.findViewById(R.id.btn02);
		Button b3 = (Button) this.findViewById(R.id.btn03);

		Button b7 = (Button) this.findViewById(R.id.btn07);
		Button b8 = (Button) this.findViewById(R.id.btn08);
		b1.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// 分析数据文件，结果存储为xml
				int ret = data.ecgAnalyzeToXml(
						Environment.getExternalStorageDirectory() + "/"
								+ strCase,
						Environment.getExternalStorageDirectory()
								+ "/BECG_advice.xml",
						Environment.getExternalStorageDirectory()
								+ "/conclusion.cn");
				Log.e("ANA", "ecgAnalyzeToXml ret = " + ret);
			}
		});
		b2.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// data.setGain(DataUtils.DISPLAY_GAIN__20);
				// 数据文件转换成aecg格式病例
				int ret = data.ecgDataToAECG(
						Environment.getExternalStorageDirectory() + "/"
								+ strCase + ".c8k",
						Environment.getExternalStorageDirectory() + "/BECG.xml");
				Log.e("aecg", "ecgDataToAECG ret = " + ret);
			}
		});
		b3.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				data.setSpeed(DataUtils.DISPLAY_SPEED_50);
			}
		});
		b7.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				data.saveCase(Environment.getExternalStorageDirectory() + "/",
						strCase, 20);// 存储文件 参数为路径，文件名，存储秒数

			}
		});
		b8.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				data.cancelCase();// 取消正在保存的文件
			}
		});
		// data对象包含所有心电采集相关操作
		// glView负责显示
		// 蓝牙采集

		*/


		Intent para = null;
		para = getIntent();
		if (para != null) {
			String address = para.getExtras().getString("DEVICE_ADDRESS");
			data = new DataUtils(mContext, address, mHandler);
		}
		// 演示文件采集
//		 data = new DataUtils(Environment.getExternalStorageDirectory().getPath()+"/demo.ecg");
		// USB 8000G 设备支持
//		mUsbManager = (UsbManager) getSystemService(Context.USB_SERVICE);
//		data = new DataUtils(mUsbManager, mHandler);
		// 以下关于glView操作为必要操作，请不要更改
		glView = (GLView) this.findViewById(R.id.GLWave);
		glView.setBackground(Color.TRANSPARENT, Color.rgb(111, 110, 110));
		glView.setGather(data);
		glView.setMsg(mHandler);
		glView.setZOrderOnTop(true);
		glView.getHolder().setFormat(PixelFormat.TRANSLUCENT);

		//textHR = (TextView) this.findViewById(R.id.textHR);

		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss",
				Locale.getDefault());
		Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
		strCase = formatter.format(curDate);
	}





	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		data.gatherEnd();
	}

	@Override
	protected void onPause() {
		super.onPause();
		glView.onPause();
		data.gatherEnd();
	}

	@Override
	protected void onResume() {
		super.onResume();
		glView.onResume();
		data.gatherStart(new nativeMsg());

		//跳出提示視窗
		dialog = ProgressDialog.show(BleECG.this,
				"讀取中", "當波型穩定變為綠色才開始記錄", true);
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					Thread.sleep(5000);
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					dialog.dismiss();
				}
			}
		}).start();

	}

//	private TextView textHR;
	private static final int MESSAGE_UPDATE_HR = 0;
	private final Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MESSAGE_UPDATE_HR:

		//		textHR.setText(msg.obj.toString() + "bpm");
				break;
			case BluetoothConnect.MESSAGE_CONNECT_SUCCESS:
				break;
			case BluetoothConnect.MESSAGE_CONNECT_INTERRUPTED:
				Log.e("BL", "INT");
				// Intent interrupt = new Intent(BleECG.this,
				// DeviceListActivity.class);
				// startActivity(interrupt);
				finish();
				break;
			case BluetoothConnect.MESSAGE_CONNECT_FAILED:
				Log.e("BL", "IOE");
				Intent intent = new Intent(BleECG.this,
						MainActivity.class);
				startActivity(intent);
				finish();
				break;
			case UsbHidConnect.MESSAGE_USB_CONNECT_FAILED:
				Toast.makeText(mContext, "IOE", Toast.LENGTH_SHORT).show();
				Intent mintent = new Intent(BleECG.this,
						MainActivity.class);
				startActivity(mintent);
				finish();
				break;
			case UsbHidConnect.MESSAGE_USB_CONNECT_START:
				Toast.makeText(mContext, "START", Toast.LENGTH_SHORT).show();
				break;
			case UsbHidConnect.MESSAGE_USB_CONNECT_SUCCESS:
				Toast.makeText(mContext, " SUCCESS", Toast.LENGTH_SHORT).show();
				break;
			case UsbHidConnect.MESSAGE_USB_CONNECT_ERROR_OPEN_DEVICE:
				Toast.makeText(mContext, "OPEN ERR", Toast.LENGTH_SHORT).show();
				break;
			case UsbHidConnect.MESSAGE_USB_CONNECT_INTERRUPTED:
				Toast.makeText(mContext, "INTERRUPTED", Toast.LENGTH_SHORT).show();
				break;
			case UsbHidConnect.MESSAGE_USB_CONNECT_REMOVE_DEVICE:
				Toast.makeText(mContext, "REMOVE", Toast.LENGTH_SHORT).show();
				finish();
				break;
			
			}
		}
	};

	class nativeMsg extends NativeCallBack {

		@Override
		public void callHRMsg(short hr) {// 心率
			mHandler.obtainMessage(MESSAGE_UPDATE_HR, hr).sendToTarget();
		}

		@Override
		public void callLeadOffMsg(String flagOff) {// 导联脱落
			Log.e("LF", flagOff);
		}

		@Override
		public void callProgressMsg(short progress) {// 文件存储进度百分比 progress%
			Log.e("progress", "" + progress);
		}

		@Override
		public void callCaseStateMsg(short state) {
			if (state == 0) {
				Log.e("Save", "start");// 开始存储文件
			} else {
				Log.e("Save", "end");// 存储完成
			}
		}

		@Override
		public void callHBSMsg(short hbs) {// 心率 hbs = 1表示有心跳
		// Log.e("HeartBeat", "Sound"+hbs);
		}

		@Override
		public void callBatteryMsg(short per) {// 采集盒电量
		// Log.e("Battery", ""+per);
		}

		@Override
		public void callCountDownMsg(short per) {// 剩余存储时长
		// Log.e("CountDown", ""+per);
		}

		@Override
		public void callWaveColorMsg(boolean flag) {
			Log.e("WaveColor", "" + flag);
			if (flag) {
				if(getStart){
					getPNG();
				}
				// 波形稳定后颜色变为绿色
				glView.setRendererColor(0, 1.0f, 0, 0);
				// 以下操作可以实现自动开始保存文件
				data.saveCase(Environment.getExternalStorageDirectory() + "/",
						strCase, 20);// 存储文件 参数为路径，文件名，存储秒数
			}
		}
	}

	public void getPNG(){
		getStart=false;
		//延遲3秒開始關機搜尋

		new Thread() {
			public void run() {
				try {
					Thread.sleep(12000);
				} catch (InterruptedException e) {
				}

				runOnUiThread(new Runnable() {

					@Override
					public void run() {

						int ret = data.ecgDataToAECG(
								Environment.getExternalStorageDirectory() + "/"
										+ strCase + ".c8k",
								Environment.getExternalStorageDirectory() + "/BECG.xml");
						Log.e("aecg", "ecgDataToAECG ret = " + ret);


						Intent intent = new Intent(BleECG.this, ECGtoPNG.class);
						startActivity(intent);
						finish();
					}
				});
			}
			// 如果任务已经完成



		}.start();



	}




	public void onBackPressed() {

		startActivity(new Intent(getBaseContext(), MainActivity.class));
		finish();


	}

	public void readequipment() {
		String[] perms = {Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION
				, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};
		//要求權限
		if (EasyPermissions.hasPermissions(BleECG.this, perms)) {



		} else {
			EasyPermissions.requestPermissions(BleECG.this, "藍芽搜尋需要權限",
					100, perms);
		}


	}


	//權限方法
	@Override
	public void onPermissionsGranted(int requestCode, List<String> perms) {

	}

	//權限方法
	@Override
	public void onPermissionsDenied(int requestCode, List<String> perms) {
		Toast.makeText(getBaseContext(),"必須開啟權限才能連結設備", Toast.LENGTH_SHORT).show();
		finish();
	}
}
