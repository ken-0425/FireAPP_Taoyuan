package tw.com.mygis.fireapp_taoyuan;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.apache.http.NameValuePair;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by choey on 2015/10/15.
 */
public class Verification extends Activity {

    //全域變數
    private GlobalVariable gv;
    private String url, Version;
    private ProgressBar pb_Version;
    private TextView tv_Version;
    private int progress;

    private static final String savePath = Environment.getExternalStorageDirectory() + "/FireAPP/";
    private static final String saveFileName = "e-PCR.apk";

    private static final int DOWN_UPDATE = 1;
    private static final int DOWN_OVER = 2;

    private boolean interceptFlag = false;

    private FileService fileService = new FileService(Verification.this);
    /**
     * 等待視窗
     */
    private ProgressDialog PDialog = null;
    private AlertDialog.Builder dialog = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.verification);

        controls();
        requestPermission();
    }

    /**
     *  控制項設定
     */
    private void controls() {
        gv = (GlobalVariable) getApplicationContext();
        url = this.getString(R.string.web);
    }


    private void Verification() {
        PDialog = ProgressDialog.show(Verification.this, "驗證", "驗證中...", true);
      //有驗證
      //   new Thread(R_Verification).start();
       // 驗證拿掉測試
       new Thread(take_off).start();
    }

    private void Version() {
        Version = "1.0";
        try {
            PackageInfo pInfo = this.getPackageManager().getPackageInfo(Verification.this.getPackageName(), 0);
            Version = pInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        PDialog = ProgressDialog.show(Verification.this, "版本", "版本驗證中...", true);

        new Thread(R_Version).start();
    }

    private void Notice(){
        dialog = new AlertDialog.Builder(Verification.this);
        dialog.setTitle("版本更新"); //設定dialog 的title顯示內容
        dialog.setMessage("有新的版本，是否進行下載？");
        dialog.setCancelable(false); //關閉 Android 系統的主要功能鍵(menu,home等...)
        dialog.setPositiveButton("下載", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Download();
            }
        });
        dialog.setNegativeButton("關閉程式", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        dialog.show();
    }

    private void Download(){
        dialog = new AlertDialog.Builder(Verification.this);
        dialog.setTitle("版本更新"); //設定dialog 的title顯示內容

        //建立路徑
        if (!fileService.createDir(savePath)) {
            Log.e("log_tag", "[Error]");
        }

        final LayoutInflater inflater = LayoutInflater.from(Verification.this);
        View v = inflater.inflate(R.layout.version, null);
        pb_Version = (ProgressBar) v.findViewById(R.id.pb_Version);
        tv_Version = (TextView) v.findViewById(R.id.tv_Version);

        dialog.setView(v);
        dialog.setCancelable(false); //關閉 Android 系統的主要功能鍵(menu,home等...)
        dialog.show();

        new Thread(R_DownloadApk).start();
    }


    Runnable take_off = new Runnable(){
        @Override
        public void run() {
            // TODO: http request.

            PDialog.dismiss();


            H_VError.sendEmptyMessage(0);


        }
    };

    Runnable R_Verification = new Runnable(){
        @Override
        public void run() {
            // TODO: http request.
            Message msg = new Message();
            Bundle data = new Bundle();

            HttpPost httppost = new HttpPost( url + "APP_Verification.aspx" ) ;
            List<NameValuePair> params = new ArrayList<NameValuePair>( ) ; //宣告參數清單
            params.add(new BasicNameValuePair("AndroidID", android.os.Build.SERIAL)) ;
            String str = gv.GetResponse(httppost, params);

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
    };

    Runnable R_Version = new Runnable(){
        @Override
        public void run() {
            // TODO: http request.
            Message msg = new Message();
            Bundle data = new Bundle();

            HttpPost httppost = new HttpPost( url + "APP_Version.aspx" ) ;
            List<NameValuePair> params = new ArrayList<NameValuePair>( ) ; //宣告參數清單
            params.add(new BasicNameValuePair("Version", Version)) ;
            String str = gv.GetResponse(httppost, params);

            PDialog.dismiss();

            if(str.equals("OK")){
                H_VOK.sendEmptyMessage(0);
            }
            else if(str.equals("Overtime")){
                H_VOverTime.sendEmptyMessage(0);
            }
            else{
                H_VError.sendEmptyMessage(0);
            }
        }
    };

    Handler H_OK = new Handler()
    {
        @Override
        public void handleMessage(Message msg) {
            Version();
        }
    };

    Handler H_Error = new Handler()
    {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle data = msg.getData();
            String str = data.getString("value");
            dialog = new AlertDialog.Builder(Verification.this);
            dialog.setTitle("提示"); //設定dialog 的title顯示內容
            dialog.setMessage(str);
            dialog.setCancelable(false); //關閉 Android 系統的主要功能鍵(menu,home等...)
            dialog.setPositiveButton("確定", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    PDialog = ProgressDialog.show(Verification.this, "驗證", "驗證中...", true);
                    new Thread(R_Verification).start();
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
            Bundle data = msg.getData();
            String str = data.getString("value");
            dialog = new AlertDialog.Builder(Verification.this);
            dialog.setTitle("錯誤"); //設定dialog 的title顯示內容
            dialog.setMessage("網路異常，連線逾時！");
            dialog.setCancelable(false); //關閉 Android 系統的主要功能鍵(menu,home等...)
            dialog.setPositiveButton("確定", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    PDialog = ProgressDialog.show(Verification.this, "驗證", "驗證中...", true);
                    new Thread(R_Verification).start();
                }
            });
            dialog.show();
        }
    };

    Handler H_VOK = new Handler()
    {
        @Override
        public void handleMessage(Message msg) {
            Notice();
        }
    };

    Handler H_VError = new Handler()
    {
        @Override
        public void handleMessage(Message msg) {
            Intent intent = new Intent();
            intent.setClass(Verification.this, Login.class);
            startActivity(intent);
            finish();
        }
    };

    Handler H_VOverTime = new Handler()
    {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle data = msg.getData();
            String str = data.getString("value");
            dialog = new AlertDialog.Builder(Verification.this);
            dialog.setTitle("錯誤"); //設定dialog 的title顯示內容
            dialog.setMessage("網路異常，連線逾時！");
            dialog.setCancelable(false); //關閉 Android 系統的主要功能鍵(menu,home等...)
            dialog.setPositiveButton("重試", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    PDialog = ProgressDialog.show(Verification.this, "版本", "版本驗證中...", true);
                    new Thread(R_Version).start();
                }
            });
            dialog.show();
        }
    };

    Runnable R_DownloadApk = new Runnable() {
        @Override
        public void run() {
            Message msg = new Message();
            Bundle data = new Bundle();
            try {
                URL apkurl = new URL(url + saveFileName);

                HttpURLConnection conn = (HttpURLConnection)apkurl.openConnection();
                conn.connect();
                int length = conn.getContentLength();
                InputStream is = conn.getInputStream();

                File ApkFile = new File(savePath, saveFileName);
                FileOutputStream fos = new FileOutputStream(ApkFile);

                int count = 0;
                byte buf[] = new byte[1024];

                do{
                    int numread = is.read(buf);
                    count += numread;
                    progress =(int)(((float)count / length) * 100);
                    Log.e("log_tag", "[Error]" + String.valueOf(progress));
                    //更新進度
                    H_Schedule.sendEmptyMessage(DOWN_UPDATE);
                    if(numread <= 0){
                        //完成通知安裝
                        H_Schedule.sendEmptyMessage(DOWN_OVER);
                        break;
                    }
                    fos.write(buf,0,numread);
                }
                while(!interceptFlag);//取消下載.

                fos.close();
                is.close();
            } catch (MalformedURLException e) {
                data.putString("value", e.toString());
                msg.setData(data);
                H_DownloadError.sendMessage(msg);
            } catch(IOException e){
                data.putString("value", e.toString());
                msg.setData(data);
                H_DownloadError.sendMessage(msg);
            }
        }
    };

    Handler H_Schedule = new Handler(){
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case DOWN_UPDATE:
                    pb_Version.setProgress(progress);
                    tv_Version.setText(String.valueOf(progress) + "%");
                    break;
                case DOWN_OVER:
                    File apkfile = new File(savePath, saveFileName);
                    if (!apkfile.exists()) {
                        dialog = new AlertDialog.Builder(Verification.this);
                        dialog.setTitle("安裝"); //設定dialog 的title顯示內容
                        dialog.setMessage("安裝失敗找不到檔案！");
                        dialog.setCancelable(false); //關閉 Android 系統的主要功能鍵(menu,home等...)
                        dialog.setPositiveButton("確定", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        });
                        dialog.show();
                        return;
                    }
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setDataAndType(Uri.parse("file://" + apkfile.toString()), "application/vnd.android.package-archive");
                    startActivity(intent);
                    break;
                default:
                    break;
            }
        };
    };

    Handler H_DownloadError = new Handler()
    {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle data = msg.getData();
            String str = data.getString("value");
            dialog = new AlertDialog.Builder(Verification.this);
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

    /**
     * 簽名權限
     * */

    public static final int EXTERNAL_STORAGE_REQ_CODE = 0 ;

    private void requestPermission() {
        //判斷是否取得權限
        boolean req_code = true;
        String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.ACCESS_FINE_LOCATION};
        for (int i = 0; i < permissions.length; i++) {
            if (ContextCompat.checkSelfPermission(this,permissions[i]) != PackageManager.PERMISSION_GRANTED) {
                req_code = false;
            }
        }
        if(!req_code){
            ActivityCompat.requestPermissions(this, permissions, EXTERNAL_STORAGE_REQ_CODE);
        }
        else{
            Verification();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case EXTERNAL_STORAGE_REQ_CODE: {
                boolean req_code = true;
                for (int i = 0; i < grantResults.length; i++) {
                    if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                        req_code = false;
                    }
                }
                if(!req_code){
                    finish();
                }
                else{
                    Verification();
                }
                return;
            }
        }
    }
}
