package tw.com.mygis.fireapp_taoyuan;

import android.Manifest;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Parcelable;
import android.os.StrictMode;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.client.methods.HttpPost;
import org.json.JSONException;
import org.json.JSONObject;

import net.gotev.uploadservice.MultipartUploadRequest;
import net.gotev.uploadservice.UploadNotificationConfig;

import java.io.ByteArrayOutputStream;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import pub.devrel.easypermissions.EasyPermissions;

/**
 * Tab頁面滑動效果
 */
public class MainActivity extends Activity implements OnClickListener,OnCheckedChangeListener,OnFocusChangeListener, EasyPermissions.PermissionCallbacks {
    /**
     * 共用參數
     */
    private SimpleDateFormat formatter_Age = new SimpleDateFormat("yyyyMMdd");
    private Date curDate_Age = new Date(System.currentTimeMillis()) ; // 獲取當前時間
    private ArrayList<HashMap<String,String>> listItem = new ArrayList<HashMap<String,String>>();
    private boolean Definition_tab1 = true, Definition_tab2 = true, Definition_tab3 = true, Definition_tab4 = true, Definition_tab5 = true;

    private String url;

    /**
     * 側頁
     */
    private DrawerLayout dl_Drawer;
    private ActionBarDrawerToggle abdt_Drawer;

    private LinearLayout ll_Drawer;
    private ListView lv_Drawer_Subno, lv_Drawer_Item;
    // 記錄被選擇的選單指標用

    /**
     * main_tab1
     */
    private Bundle bundle;
    private LinearLayout ll_Custody_F, ll_Custody_T;
    private EditText et_Date, et_AttendanceUnit, et_LocationHappen, et_AssistanceUnit, et_HospitalAddress, et_Time1, et_Time2, et_Time3, et_Time4, et_Time5, et_Time6, et_Name, et_Age, et_ID, et_Address, et_RCustody, et_Custody;
    private TextView tv_FNumber, tv_MHospital, tv_Time1, tv_Time2, tv_Time3, tv_Time4, tv_Time5, tv_Time6, tv_SCustody;
    private RadioGroup rg_AcceptedUnit;
    private RadioButton rb_AcceptedUnit1, rb_AcceptedUnit2;
    private Button btn_Upload, btn_MHospital, btn_Address;
    private Spinner sp_PDF, sp_HospitalAddress, sp_UploadHospital, sp_Area, sp_RCustody;
    private ImageView iv_SCustody;
    private CheckBox cb_Critical, cb_AgeM, cb_AgeA;
    private CheckBox[] cb_HospitalAddress_Name = new CheckBox[3];
    private int[] cb_HospitalAddress_ID = new int[]{R.id.cb_HospitalAddress1, R.id.cb_HospitalAddress2, R.id.cb_HospitalAddress3};
    private CheckBox[] cb_NoHospital_Name = new CheckBox[8];
    private int[] cb_NoHospital_ID = new int[]{R.id.cb_NoHospital1, R.id.cb_NoHospital2, R.id.cb_NoHospital3, R.id.cb_NoHospital4, R.id.cb_NoHospital5, R.id.cb_NoHospital6, R.id.cb_NoHospital7, R.id.cb_NoHospital8};
    private CheckBox[] cb_Sex_Name = new CheckBox[2];
    private int[] cb_Sex_ID = new int[]{R.id.cb_Sex1, R.id.cb_Sex2};
    private CheckBox[] cb_Property_Name = new CheckBox[2];
    private int[] cb_Property_ID = new int[]{R.id.cb_Property1, R.id.cb_Property2};

    /**
     * main_tab2
     */
    private FrameLayout fl_Trauma_T, fl_Trauma_F, fl_OHCA;
    private LinearLayout ll_Trauma1_T, ll_Trauma2_T, ll_NTrauma6;
    private TextView tv_Trauma_T, tv_Trauma_F, tv_OHCA;
    private EditText et_Trauma3, et_Trauma9, et_Traffic5, et_NTrauma15, et_ROSC2, et_LTime;
    private CheckBox cb_Smile, cb_LArm, cb_Speech;
    private CheckBox[] cb_Trauma_Switch_Name = new CheckBox[2];
    private int[] cb_Trauma_Switch_ID = new int[]{R.id.cb_Trauma_T, R.id.cb_Trauma_F};
    private CheckBox[] cb_Trauma_Name = new CheckBox[9];
    private int[] cb_Trauma_ID = new int[]{R.id.cb_Trauma1, R.id.cb_Trauma2, R.id.cb_Trauma3, R.id.cb_Trauma4, R.id.cb_Trauma5, R.id.cb_Trauma6, R.id.cb_Trauma7, R.id.cb_Trauma8, R.id.cb_Trauma9};
    private CheckBox[] cb_Trauma1_Name = new CheckBox[5];
    private int[] cb_Trauma1_ID = new int[]{R.id.cb_Trauma1_1, R.id.cb_Trauma1_2, R.id.cb_Trauma1_3, R.id.cb_Trauma1_4, R.id.cb_Trauma1_5};
    private CheckBox[] cb_Traffic_Name = new CheckBox[5];
    private int[] cb_Traffic_ID = new int[]{R.id.cb_Traffic1, R.id.cb_Traffic2, R.id.cb_Traffic3, R.id.cb_Traffic4, R.id.cb_Traffic5};
    private CheckBox[] cb_NTrauma_Name = new CheckBox[15];
    private int[] cb_NTrauma_ID = new int[]{R.id.cb_NTrauma1, R.id.cb_NTrauma2, R.id.cb_NTrauma3, R.id.cb_NTrauma4, R.id.cb_NTrauma5, R.id.cb_NTrauma6, R.id.cb_NTrauma7, R.id.cb_NTrauma8, R.id.cb_NTrauma9, R.id.cb_NTrauma10, R.id.cb_NTrauma11, R.id.cb_NTrauma12, R.id.cb_NTrauma13, R.id.cb_NTrauma14, R.id.cb_NTrauma15};
    private CheckBox[] cb_NTrauma6_Name = new CheckBox[4];
    private int[] cb_NTrauma6_ID = new int[]{R.id.cb_NTrauma6_1, R.id.cb_NTrauma6_2, R.id.cb_NTrauma6_3, R.id.cb_NTrauma6_4};
    private CheckBox[] cb_Witnesses_Name = new CheckBox[2];
    private int[] cb_Witnesses_ID = new int[]{R.id.cb_Witnesses1, R.id.cb_Witnesses2};
    private CheckBox[] cb_CPR_Name = new CheckBox[2];
    private int[] cb_CPR_ID = new int[]{R.id.cb_CPR1, R.id.cb_CPR2};
    private CheckBox[] cb_PAD_Name = new CheckBox[2];
    private int[] cb_PAD_ID = new int[]{R.id.cb_PAD1, R.id.cb_PAD2};
    private CheckBox[] cb_ROSC_Name = new CheckBox[2];
    private int[] cb_ROSC_ID = new int[]{R.id.cb_ROSC1, R.id.cb_ROSC2};
    private CheckBox[] cb_PSpecies_Name = new CheckBox[11];
    private int[] cb_PSpecies_ID = new int[]{R.id.cb_PSpecies1, R.id.cb_PSpecies2, R.id.cb_PSpecies3, R.id.cb_PSpecies4, R.id.cb_PSpecies5, R.id.cb_PSpecies6, R.id.cb_PSpecies7, R.id.cb_PSpecies8, R.id.cb_PSpecies9, R.id.cb_PSpecies10, R.id.cb_PSpecies11};
    private CheckBox[] cb_Stroke_Name = new CheckBox[2];
    private int[] cb_Stroke_ID = new int[]{R.id.cb_Stroke1, R.id.cb_Stroke2};

    /**
     * main_tab3
     */
    private boolean ll_SBreathing_b = true, ll_STDisposal_b = true, ll_SCPRDisposal_b = true, ll_SDDisposal_b = true, ll_SODisposal_b = true, ll_SALSDisposal_b = true;
    private LinearLayout ll_Breathing, ll_TDisposal, ll_CPRDisposal, ll_CPRDisposal3, ll_DDisposal, ll_DDisposal1, ll_ODisposal, ll_ALSDisposal;
    private LinearLayout ll_SBreathing, ll_STDisposal, ll_SCPRDisposal, ll_SDDisposal, ll_SODisposal, ll_SALSDisposal;
    private EditText et_Breathing5, et_Breathing6, et_Breathing7, et_Breathing10;
    private EditText et_TDisposal8;
    private EditText et_CPRDisposal1, et_CPRDisposal1_Time, et_CPRDisposal3_Time, et_CPRDisposal3_1;
    private EditText et_DDisposal1, et_DDisposal1_1, et_DDisposal1_2, et_DDisposal1_3, et_DDisposal3, et_DDisposal4;
    private EditText et_ODisposal4, et_ODisposal5;
    private EditText et_ALSDisposal1_1, et_ALSDisposal1_2, et_ALSDisposal2_1, et_ALSDisposal2_2;
    private EditText et_Posture;
    private EditText et_DrugUseTime1, et_DrugUseTime2, et_DrugUseTime3, et_DrugUseTime4, et_DrugUser1, et_DrugUser2, et_DrugUser3, et_DrugUser4;
    private ImageView iv_Breathing, iv_TDisposal, iv_CPRDisposal, iv_DDisposal, iv_ODisposal, iv_ALSDisposal;
    private Spinner sp_ODisposal4, sp_Posture;
    private Spinner sp_DrugUseName1, sp_DrugUseName2, sp_DrugUseName3, sp_DrugUseName4;
    private Spinner sp_DrugUseDose1, sp_DrugUseDose2, sp_DrugUseDose3, sp_DrugUseDose4;
    private CheckBox[] cb_Breathing_Name = new CheckBox[10];
    private int[] cb_Breathing_ID = new int[]{R.id.cb_Breathing1, R.id.cb_Breathing2, R.id.cb_Breathing3, R.id.cb_Breathing4, R.id.cb_Breathing5, R.id.cb_Breathing6, R.id.cb_Breathing7, R.id.cb_Breathing8, R.id.cb_Breathing9, R.id.cb_Breathing10};
    private CheckBox[] cb_TDisposal_Name = new CheckBox[8];
    private int[] cb_TDisposal_ID = new int[]{R.id.cb_TDisposal1, R.id.cb_TDisposal2, R.id.cb_TDisposal3, R.id.cb_TDisposal4, R.id.cb_TDisposal5, R.id.cb_TDisposal6, R.id.cb_TDisposal7, R.id.cb_TDisposal8};
    private CheckBox[] cb_CPRDisposal_Name = new CheckBox[3];
    private int[] cb_CPRDisposal_ID = new int[]{R.id.cb_CPRDisposal1, R.id.cb_CPRDisposal2, R.id.cb_CPRDisposal3};
    private CheckBox[] cb_CPRDisposal3_Name = new CheckBox[2];
    private int[] cb_CPRDisposal3_ID = new int[]{R.id.cb_CPRDisposal3_1, R.id.cb_CPRDisposal3_2};
    private CheckBox[] cb_DDisposal_Name = new CheckBox[4];
    private int[] cb_DDisposal_ID = new int[]{R.id.cb_DDisposal1, R.id.cb_DDisposal2, R.id.cb_DDisposal3, R.id.cb_DDisposal4};
    private CheckBox[] cb_DDisposal1_Name = new CheckBox[3];
    private int[] cb_DDisposal1_ID = new int[]{R.id.cb_DDisposal1_1, R.id.cb_DDisposal1_2, R.id.cb_DDisposal1_3};
    private CheckBox[] cb_ODisposal_Name = new CheckBox[5];
    private int[] cb_ODisposal_ID = new int[]{R.id.cb_ODisposal1, R.id.cb_ODisposal2, R.id.cb_ODisposal3, R.id.cb_ODisposal4, R.id.cb_ODisposal5};
    private CheckBox[] cb_ALSDisposal_Name = new CheckBox[2];
    private int[] cb_ALSDisposal_ID = new int[]{R.id.cb_ALSDisposal1, R.id.cb_ALSDisposal2};

    /**
     * main_tab4
     */
    private boolean ll_SMHistory_b = true, ll_SAllergies_b = true;
    private LinearLayout ll_MHistory, ll_Allergies, ll_People;
    private LinearLayout ll_SMHistory, ll_SAllergies;
    private EditText et_Q1, et_Q2, et_Q3, et_Q4, et_Q5, et_MHistory12, et_Allergies1, et_Allergies2, et_Allergies3, et_People;
    private Spinner sp_Q1, sp_Q2, sp_Q3, sp_Q4, sp_People;
    private ImageView iv_MHistory, iv_Allergies;
    private CheckBox[] cb_MHistory_Name = new CheckBox[13];
    private int[] cb_MHistory_ID = new int[]{R.id.cb_MHistory1, R.id.cb_MHistory2, R.id.cb_MHistory3, R.id.cb_MHistory4, R.id.cb_MHistory5, R.id.cb_MHistory6, R.id.cb_MHistory7, R.id.cb_MHistory8, R.id.cb_MHistory9, R.id.cb_MHistory10, R.id.cb_MHistory11, R.id.cb_MHistory12, R.id.cb_MHistory13};
    private CheckBox[] cb_Allergies_Name = new CheckBox[4];
    private int[] cb_Allergies_ID = new int[]{R.id.cb_Allergies1, R.id.cb_Allergies2, R.id.cb_Allergies3, R.id.cb_Allergies4};
    private ImageView[] iv_People_Name = new ImageView[23];
    private int[] iv_People_ID = new int[]{R.id.iv_People1, R.id.iv_People2, R.id.iv_People3, R.id.iv_People4, R.id.iv_People5, R.id.iv_People6, R.id.iv_People7, R.id.iv_People8, R.id.iv_People9, R.id.iv_People10, R.id.iv_People11, R.id.iv_People12, R.id.iv_People13, R.id.iv_People14, R.id.iv_People15, R.id.iv_People16, R.id.iv_People17, R.id.iv_People18, R.id.iv_People19, R.id.iv_People20, R.id.iv_People21, R.id.iv_People22, R.id.iv_People23};
    private int[] T_People_ID = new int[]{R.drawable.people_highlight_01, R.drawable.people_highlight_02, R.drawable.people_highlight_03, R.drawable.people_highlight_04, R.drawable.people_highlight_05, R.drawable.people_highlight_06, R.drawable.people_highlight_07, R.drawable.people_highlight_08, R.drawable.people_highlight_09, R.drawable.people_highlight_10, R.drawable.people_highlight_11, R.drawable.people_highlight_12, R.drawable.people_highlight_13, R.drawable.people_highlight_14, R.drawable.people_highlight_15, R.drawable.people_highlight_16, R.drawable.people_highlight_17, R.drawable.people_highlight_18, R.drawable.people_highlight_19, R.drawable.people_highlight_20, R.drawable.people_highlight_21, R.drawable.people_highlight_22, R.drawable.people_highlight_23};
    private int[] F_People_ID = new int[]{R.drawable.people_01, R.drawable.people_02, R.drawable.people_03, R.drawable.people_04, R.drawable.people_05, R.drawable.people_06, R.drawable.people_07, R.drawable.people_08, R.drawable.people_09, R.drawable.people_10, R.drawable.people_11, R.drawable.people_12, R.drawable.people_13, R.drawable.people_14, R.drawable.people_15, R.drawable.people_16, R.drawable.people_17, R.drawable.people_18, R.drawable.people_19, R.drawable.people_20, R.drawable.people_21, R.drawable.people_22, R.drawable.people_23};

  /**
     * main_tab5
     */
    private TextView tv_LifeTime;
    private EditText et_LifeTime1, et_LifeBreathing1, et_Pulse1, et_GCS1_E, et_GCS1_V, et_GCS1_M, et_SBP1, et_DBP1, et_SpO21, et_Temperature1;
    private Spinner sp_Consciousness1, sp_LifeBreathing1, sp_Pulse1, sp_GCS1_E, sp_GCS1_V, sp_GCS1_M, sp_SBP1,sp_DBP1, sp_BPressure1_A, sp_SpO21, sp_Temperature1;
    private EditText et_LifeTime2, et_LifeBreathing2, et_Pulse2, et_GCS2_E, et_GCS2_V, et_GCS2_M, et_SBP2, et_DBP2, et_SpO22, et_Temperature2;
    private Spinner sp_Consciousness2, sp_LifeBreathing2, sp_Pulse2, sp_GCS2_E, sp_GCS2_V, sp_GCS2_M, sp_SBP2,sp_DBP2, sp_BPressure2_A, sp_SpO22, sp_Temperature2;
    private EditText et_LifeBreathing3, et_Pulse3, et_GCS3_E, et_GCS3_V, et_GCS3_M, et_SBP3, et_DBP3, et_SpO23, et_Temperature3;
    private Spinner sp_Consciousness3, sp_LifeBreathing3, sp_Pulse3, sp_GCS3_E, sp_GCS3_V, sp_GCS3_M, sp_SBP3,sp_DBP3, sp_BPressure3_A, sp_SpO23, sp_Temperature3;
    private LinearLayout ll_BP1, ll_BP2, ll_BP3;
    private RadioGroup rg_BP1, rg_BP2, rg_BP3;
    private RadioButton rb_BP1_1, rb_BP1_2, rb_BP2_1, rb_BP2_2, rb_BP3_1, rb_BP3_2;
    private Button bt_ecg_write ,bt_ecg_read;

    /**
     * 等待視窗
     */
    private ProgressDialog PDialog = null;

    /**
     * 滑頁
     */
    private ViewPager mPager;// 頁面內容
    private List<View> listViews; // Tab頁面類表
    private ImageView cursor;// 動畫圖片
    private ImageView t1, t2, t3, t4, t5;// 標頭
    private int offset = 0;// 動畫圖片偏移量
    private int currIndex = 0;// 當頁編號
    private int bmpW;// 動畫圖片寬度
    //手寫功能
    private Bitmap mSignBitmap;//手寫文件
    private String signPath;//手寫簽名路徑
    private FileService fileService = new FileService(MainActivity.this);
    /**
     * 下拉式
     */
    private ArrayAdapter<String> adapter_PDF, adapter_HospitalAddress, adapter_Area, adapter_UploadHospital, adapter_Relationship, adapter_Posture, adapter_ODisposal4, adapter_DrugUseName, adapter_DrugUseDose, adapter_Glucose, adapter_Amiodarone, adapter_Epinephrine, adapter_Q1, adapter_Q2, adapter_Q3, adapter_Q4;//下拉是選單
    private ArrayAdapter[] array_DrugUseDose;
    private ArrayAdapter<String> adapter_People;
    private ArrayAdapter<String> adapter_Exception, adapter_Consciousness, adapter_BPressure, adapter_GCSE, adapter_GCSV, adapter_GCSM;
    //全域變數
    private GlobalVariable gv;

    private Uri filePath;

    private AlertDialog.Builder dialog = null;

    /**
     * BlE
     */
    public static final String EXTRAS_DEVICE_NAME = "DEVICE_NAME";
    public static final String EXTRAS_DEVICE_ADDRESS = "DEVICE_ADDRESS";
    public static final String ECGWS12 = "00:0E:EA:CF:28:95";
    private static final long SCAN_PERIOD = 10000;
    private static final int REQUEST_ENABLE_BT = 1;

    private long exitTime = 0;
    private BluetoothAdapter mBluetoothAdapter;
    //宣告Service程式
    private ServiceBluetoothSearch searchService;
    private final BluetoothReceiver receiver = new BluetoothReceiver();

    //宣告ServiceConnection程式serviceConnection
    private ServiceConnection serviceConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName className, IBinder service) {
            searchService = ((ServiceBluetoothSearch.SearchServiceBinder) service).getService();
        }
        public void onServiceDisconnected(ComponentName className) {
            searchService = null;
        }
    };

    //接收廣播
    private class BluetoothReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {

            String message = intent.getStringExtra("type");
            IntentBle(message);

        }
    }


    /** 建立UI Thread使用的Handler，來接收其他Thread來的訊息 */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        gv = (GlobalVariable) getApplicationContext();
        url = this.getString(R.string.web);

        InitImageView();
        InitTextView();
        InitViewPager();
        ArrayAdapter();
        setDrawerLayout();
        bluetooth();
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

        SQLite SQLite = new SQLite(MainActivity.this);
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
                MainActivity.this,
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
                MainActivity.this,
                R.layout.listview_function,
                new int[]{R.drawable.function_add, R.drawable.function_navigation, R.drawable.function_preview, R.drawable.function_signature},
                new String[]{"新增表單", "導航", "預覽", "簽名"},
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

            PDialog = ProgressDialog.show(MainActivity.this, "請稍等...", "切換表單中...", true);

            SaveData();

            gv.DrawerItemID = data.get("Subno").toString();
            gv.DrawerItemPosition = position;

            new Thread(R_SwitchTable).start();
        }
    }

    private void selectItem(int position) {
        SaveData();

        Intent intent = new Intent();
        switch (position){
            case 0:
                PDialog = ProgressDialog.show(MainActivity.this, "請稍等...", "建立表單中...", true);
                new Thread(R_NewTable).start();
                break;
            case 1:
                SaveCurrIndex();
                int sdk = android.os.Build.VERSION.SDK_INT;
                if(sdk < android.os.Build.VERSION_CODES.HONEYCOMB) {
                    android.text.ClipboardManager clipboard = (android.text.ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                    clipboard.setText(gv.Coordinate);
                } else {
                    android.content.ClipboardManager clipboard = (android.content.ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                    android.content.ClipData clip = android.content.ClipData.newPlainText("Coordinate",gv.Coordinate);
                    clipboard.setPrimaryClip(clip);
                }
                intent.setClass(MainActivity.this, Drawer_Navigation.class);
                startActivity(intent);
                MainActivity.this.finish();
                break;
            case 2:
                SaveCurrIndex();

                intent.setClass(MainActivity.this, Drawer_Preview.class);
                startActivity(intent);
                MainActivity.this.finish();
                break;
            case 3:
                SaveCurrIndex();

                intent.setClass(MainActivity.this, Drawer_Handwrite.class);
                startActivity(intent);
                MainActivity.this.finish();
                break;
            default:
                break;
        }
    }

    private void SaveCurrIndex() {
        gv.main_currIndex = currIndex;

        gv.Modify_Tab1 = true;
        gv.Modify_Tab2 = true;
        gv.Modify_Tab3 = true;
        gv.Modify_Tab4 = true;
        gv.Modify_Tab5 = true;
    }

    private void SaveData() {

        switch (currIndex) {
            case 0:
                main_tab1_Save();
                main_tab1_DBSave();
                break;
            case 1:
                main_tab2_Save();
                main_tab2_DBSave();
                break;
            case 2:
                main_tab3_Save();
                main_tab3_DBSave();
                break;
            case 3:
                main_tab4_Save();
                main_tab4_DBSave();
                break;
            case 4:
                main_tab5_Save();
                main_tab5_DBSave();
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
     * 初始動畫
     */
    private void InitImageView() {
        cursor = (ImageView) findViewById(R.id.cursor);
        bmpW = BitmapFactory.decodeResource(getResources(), R.drawable.header_position).getWidth();// 取寬
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int screenW = dm.widthPixels;// 取分辨率寬度
        offset = (screenW / 5 - bmpW) / 2;// 偏移量計算
        Matrix matrix = new Matrix();
        matrix.postTranslate(offset, 0);
        cursor.setImageMatrix(matrix);// 動畫起始位子設置
    }

    /**
     * 起始標頭
     */
    private void InitTextView() {
        t1 = (ImageView) findViewById(R.id.text1);
        t2 = (ImageView) findViewById(R.id.text2);
        t3 = (ImageView) findViewById(R.id.text3);
        t4 = (ImageView) findViewById(R.id.text4);
        t5 = (ImageView) findViewById(R.id.text5);

        t1.setOnClickListener(new MyOnClickListener(0));
        t2.setOnClickListener(new MyOnClickListener(1));
        t3.setOnClickListener(new MyOnClickListener(2));
        t4.setOnClickListener(new MyOnClickListener(3));
        t5.setOnClickListener(new MyOnClickListener(4));
    }

    /**
     * 初始化ViewPager
     */
    private void InitViewPager() {
        mPager = (ViewPager) findViewById(R.id.vPager);
        listViews = new ArrayList<View>();
        LayoutInflater mInflater = getLayoutInflater();
        listViews.add(mInflater.inflate(R.layout.main_tab1, null));
        listViews.add(mInflater.inflate(R.layout.main_tab2, null));
        listViews.add(mInflater.inflate(R.layout.main_tab3, null));
        listViews.add(mInflater.inflate(R.layout.main_tab4, null));
        listViews.add(mInflater.inflate(R.layout.main_tab5, null));
        mPager.setAdapter(new MyPagerAdapter(listViews));
        mPager.setCurrentItem(gv.main_currIndex);
        currIndex = gv.main_currIndex;
        mPager.setOnPageChangeListener(new MyOnPageChangeListener());
        Animation animation = null;
        switch (currIndex) {
            case 0:
                animation = new TranslateAnimation(0, 0, 0, 0);
                break;
            case 1:
                animation = new TranslateAnimation(offset * 2 + bmpW, offset * 2 + bmpW, 0, 0);
                break;
            case 2:
                animation = new TranslateAnimation((offset * 2 + bmpW) * 2, (offset * 2 + bmpW) * 2, 0, 0);
                break;
            case 3:
                animation = new TranslateAnimation((offset * 2 + bmpW) * 3, (offset * 2 + bmpW) * 3, 0, 0);
                break;
            case 4:
                animation = new TranslateAnimation((offset * 2 + bmpW) * 4, (offset * 2 + bmpW) * 4, 0, 0);
                break;
        }
        animation.setFillAfter(true);// True:停在動畫结束位置
        animation.setDuration(300);
        cursor.startAnimation(animation);
    }

    /**
     * 下拉是選單
     */
    private void ArrayAdapter() {
        /**
         * tab1
         */

        String[] sp_PDF_item = new String[]{ "案由:" + gv.LoadPDF, "通用救護流程", "非創傷救護流程", "創傷救護流程", "救護車救護流程", "氣管內管插管緊急救護規範", "非創傷心臟停止救護流程", "創傷心臟停止救護流程", "呼吸道異物哽塞流程", "呼吸窘迫救護流程", "低血糖救護流程", "致命性過敏救護流程", "疑似腦中風救護原則", "頭部外傷救護原則", "胸部外傷處置原則", "肢體外傷處置原則", "創傷性休克處置原則", "創傷性截肢處置原則", "張力性氣胸處置原則", "燒燙傷處置原則", "急產處置原則", "懷孕婦女的運送原則", "子癇前症／子癇症處置原則", "不施行心肺復甦術處置原則", "現場死亡處置原則", "執行CO中毒救護案件相關注意事項"};
        adapter_PDF = new ArrayAdapter<String>(this,R.layout.spinner_style,R.id.txtvwSpinner,sp_PDF_item);
        adapter_PDF.setDropDownViewResource(R.layout.spinner_dropdown_style);
      //  String[] sp_HospitalAddress_item = new String[]{"請選擇","林口長庚", "桃園醫院", "榮總桃園分院", "國軍桃園總醫院", "聖保祿醫院", "敏盛綜合醫院", "壢新醫院", "怡仁綜合醫院", "天成醫院(楊梅)", "新屋分院", "天晟醫院(中壢)", "恩主公醫院", "樂生療養院(迴龍院區)", "亞東紀念醫院", "桃園療養院", "中壢長榮", "宋俊宏婦幼醫院", "宏其婦幼醫院", "秉坤婦幼醫院", "龍潭敏盛醫院", "復興鄉華陵整合醫療站", "居善醫院", "其他"};
        String[] sp_HospitalAddress_item = gv.Hospital;
        adapter_HospitalAddress = new ArrayAdapter<String>(this,R.layout.spinner_style,R.id.txtvwSpinner,sp_HospitalAddress_item);
        adapter_HospitalAddress.setDropDownViewResource(R.layout.spinner_dropdown_style);
        String[] sp_UploadHospital_item = new String[]{"請選擇","OHCA","急性腦中風","急性心肌哽塞","嚴重創傷"};
        adapter_UploadHospital = new ArrayAdapter<String>(this,R.layout.spinner_style,R.id.txtvwSpinner,sp_UploadHospital_item);
        adapter_UploadHospital.setDropDownViewResource(R.layout.spinner_dropdown_style);
        String[] sp_Area_item = new String[]{"請選擇","桃園區","中壢區","大溪區","楊梅區","蘆竹區","大園區","龜山區","八德區","龍潭區","平鎮區","新屋區","觀音區","復興區"};
        adapter_Area = new ArrayAdapter<String>(this,R.layout.spinner_style,R.id.txtvwSpinner,sp_Area_item);
        adapter_Area.setDropDownViewResource(R.layout.spinner_dropdown_style);
        String[] sp_Relationship_item = new String[]{"請選擇","父親","母親","夫妻","兒子","女兒","兄弟","姊妹","同事","友人","其他"};
        adapter_Relationship = new ArrayAdapter<String>(this,R.layout.spinner_style,R.id.txtvwSpinner,sp_Relationship_item);
        adapter_Relationship.setDropDownViewResource(R.layout.spinner_dropdown_style);

        String[] sp_Posture_item = new String[]{"請選擇","半坐臥","平躺","坐姿","左側躺","其他"};
        adapter_Posture = new ArrayAdapter<String>(this,R.layout.spinner_style,R.id.txtvwSpinner,sp_Posture_item);
        adapter_Posture.setDropDownViewResource(R.layout.spinner_dropdown_style);
        String[] sp_ODisposal4_item = new String[]{"請選擇","無法測量","Low","High"};
        adapter_ODisposal4 = new ArrayAdapter<String>(this,R.layout.spinner_style,R.id.txtvwSpinner,sp_ODisposal4_item);
        adapter_ODisposal4.setDropDownViewResource(R.layout.spinner_dropdown_style);
        //給藥
        String[] sp_DrugUseName_item = new String[]{"請選擇","Epinephrine","Amiodarone","50%Glucose"};
        adapter_DrugUseName = new ArrayAdapter<String>(this,R.layout.spinner_style,R.id.txtvwSpinner,sp_DrugUseName_item);
        adapter_DrugUseName.setDropDownViewResource(R.layout.spinner_dropdown_style);
        String[] sp_DrugUseDose_item = new String[]{"請先選取藥名"};
        adapter_DrugUseDose = new ArrayAdapter<String>(this,R.layout.spinner_style,R.id.txtvwSpinner,sp_DrugUseDose_item);
        adapter_DrugUseDose.setDropDownViewResource(R.layout.spinner_dropdown_style);
        String[] sp_Epinephrine = new String[]{"請選擇","IV,1mg","IM,0.3mg","IM,0.5mg"};
        adapter_Epinephrine = new ArrayAdapter<String>(this,R.layout.spinner_style,R.id.txtvwSpinner,sp_Epinephrine);
        adapter_Epinephrine.setDropDownViewResource(R.layout.spinner_dropdown_style);
        String[] sp_Amiodarone_item = new String[]{"請選擇","IV,150mg","IV,300mg"};
        adapter_Amiodarone = new ArrayAdapter<String>(this,R.layout.spinner_style,R.id.txtvwSpinner,sp_Amiodarone_item);
        adapter_Amiodarone.setDropDownViewResource(R.layout.spinner_dropdown_style);
        String[] sp_Glucose_item = new String[]{"請選擇","IV,2amp","IV,4amp"};
        adapter_Glucose = new ArrayAdapter<String>(this,R.layout.spinner_style,R.id.txtvwSpinner,sp_Glucose_item);
        adapter_Glucose.setDropDownViewResource(R.layout.spinner_dropdown_style);
        array_DrugUseDose = new ArrayAdapter[]{adapter_DrugUseDose, adapter_Epinephrine, adapter_Amiodarone, adapter_Glucose};
        //病患主訴
        String[] sp_Q1_item = new String[]{"添加常用詞", "頭部", "胸部", "腹部", "肢體疼痛", "OHCA", "意識昏迷", "昏厥", "意識不清", "呼吸困難", "異物哽塞", "肢體無力", "抽搐", "發燒", "嘔吐", "行為急症/精神異常", "疑似毒藥物中毒", "孕婦急症", "路倒", "家屬代述", "旁人代述"};
        adapter_Q1 = new ArrayAdapter<String>(this,R.layout.spinner_style,R.id.txtvwSpinner,sp_Q1_item);
        adapter_Q1.setDropDownViewResource(R.layout.spinner_dropdown_style);
        String[] sp_Q2_item = new String[]{"添加常用詞", "疼痛", "暈", "悶", "肢體無力", "OHCA", "意識昏迷", "昏厥", "意識不清", "呼吸喘", "異物哽塞", "呼吸急促", "抽搐", "發燒", "嘔吐", "情緒激動", "行為急症/精神異常", "疑似毒藥物中毒", "路倒", "家屬代述", "旁人代述"};
        adapter_Q2 = new ArrayAdapter<String>(this,R.layout.spinner_style,R.id.txtvwSpinner,sp_Q2_item);
        adapter_Q2.setDropDownViewResource(R.layout.spinner_dropdown_style);
        String[] sp_Q3_item = new String[]{"添加常用詞", "約五分鐘", "約十分鐘", "約十五分鐘", "約三十分鐘", "約一小時", "約二小時", "約一天", "不清楚", "車禍後", "報案前", "家屬代述", "旁人代述"};
        adapter_Q3 = new ArrayAdapter<String>(this,R.layout.spinner_style,R.id.txtvwSpinner,sp_Q3_item);
        adapter_Q3.setDropDownViewResource(R.layout.spinner_dropdown_style);
        String[] sp_Q4_item = new String[]{"添加常用詞", "沒有", "無法表達", "不清楚", "有外傷傷口", "手腳痲", "低血糖", "低血壓", "膚色蒼白", "冒冷汗", "噁心", "腹瀉", "嗜睡", "發冷", "家屬代述", "旁人代述"};
        adapter_Q4 = new ArrayAdapter<String>(this,R.layout.spinner_style,R.id.txtvwSpinner,sp_Q4_item);
        adapter_Q4.setDropDownViewResource(R.layout.spinner_dropdown_style);
        String[] sp_People_item = new String[]{"添加常用詞", "現場無人員受傷", "目擊者表示患者已自離", "勤區表示患者已自離", "患者為長期臥床病人", "患者為氣切病人", "患者長期臥床，意識與平時無異", "患者身上疑似有酒味", "警察人員隨同至醫院", "醫護人員隨同至醫院", "患者牙關緊閉無法上LMA", "現場環境狹窄，搬運困難", "現場環境狹窄，搬運途中無法CPR", "患者、家屬堅持指定醫院送醫", "患者為無意識、無呼吸、無脈搏之情形", "患者達到屍腐、屍僵、屍體焦黑、無首、內臟外溢或軀幹斷體的狀態"};
        adapter_People = new ArrayAdapter<String>(this,R.layout.spinner_style,R.id.txtvwSpinner,sp_People_item);
        adapter_People.setDropDownViewResource(R.layout.spinner_dropdown_style);
        //生命徵象
        String[] sp_Exception_item = new String[]{"請選擇","無法測量","未測量"};
        adapter_Exception = new ArrayAdapter<String>(this,R.layout.spinner_style,R.id.txtvwSpinner,sp_Exception_item);
        adapter_Exception.setDropDownViewResource(R.layout.spinner_dropdown_style);
        String[] sp_Consciousness_item = new String[]{"請選擇","清","聲","痛","否"};
        adapter_Consciousness = new ArrayAdapter<String>(this,R.layout.spinner_style,R.id.txtvwSpinner,sp_Consciousness_item);
        adapter_Consciousness.setDropDownViewResource(R.layout.spinner_dropdown_style);
        String[] sp_BPressure_item = new String[]{"請選擇","無","頸動脈","肱/股動脈","橈動脈"};
        adapter_BPressure = new ArrayAdapter<String>(this,R.layout.spinner_style,R.id.txtvwSpinner,sp_BPressure_item);
        adapter_BPressure.setDropDownViewResource(R.layout.spinner_dropdown_style);
        String[] sp_GCSE_item = new String[]{"","1","2","3","4"};
        adapter_GCSE = new ArrayAdapter<String>(this,R.layout.spinner_style,R.id.txtvwSpinner,sp_GCSE_item);
        adapter_GCSE.setDropDownViewResource(R.layout.spinner_dropdown_style);
        String[] sp_GCSV_item = new String[]{"","1","2","3","4","5","T","A","E"};
        adapter_GCSV = new ArrayAdapter<String>(this,R.layout.spinner_style,R.id.txtvwSpinner,sp_GCSV_item);
        adapter_GCSV.setDropDownViewResource(R.layout.spinner_dropdown_style);
        String[] sp_GCSM_item = new String[]{"","1","2","3","4","5","6"};
        adapter_GCSM = new ArrayAdapter<String>(this,R.layout.spinner_style,R.id.txtvwSpinner,sp_GCSM_item);
        adapter_GCSM.setDropDownViewResource(R.layout.spinner_dropdown_style);
    }

    /**
     *
     * ViewPager適配器
     */
    public class MyPagerAdapter extends PagerAdapter  {
        public List<View> mListViews;


        public MyPagerAdapter(List<View> mListViews) {
            this.mListViews = mListViews;
        }

        @Override
        public void destroyItem(View arg0, int arg1, Object arg2) {
            ((ViewPager) arg0).removeView(mListViews.get(arg1));
        }

        @Override
        public void finishUpdate(View arg0) {
        }

        @Override
        public int getCount() {
            return mListViews.size();
        }

        @Override
        public Object instantiateItem(View arg0, int arg1) {

            if (arg1 < 5) {
                ((ViewPager) arg0).addView(mListViews.get(arg1 % 5), 0);
            }
            switch (arg1){
                case 0:
                    if(Definition_tab1){
                        main_tab1_controls();
                        main_tab1_Click();
                        Definition_tab1 = false;
                    }
                    if(gv.Modify_Tab1){
                        main_tab1_Modify();
                    }
                    break;
                case 1:
                    if(Definition_tab2) {
                        main_tab2_controls();
                        main_tab2_Click();
                        Definition_tab2 = false;
                    }
                    if(gv.Modify_Tab2){
                        main_tab2_Modify();
                    }
                    break;
                case 2:
                    if(Definition_tab3) {
                        main_tab3_controls();
                        main_tab3_Click();
                        Definition_tab3 = false;
                    }
                    if(gv.Modify_Tab3){
                        main_tab3_Modify();
                    }
                    break;
                case 3:
                    if(Definition_tab4) {
                        main_tab4_controls();
                        main_tab4_Click();
                        Definition_tab4 = false;
                    }
                    if (gv.Modify_Tab4) {
                        main_tab4_Modify();
                    }

                    break;
                case 4:
                    if(Definition_tab5) {
                        main_tab5_controls();
                        main_tab5_Click();
                        Definition_tab5 = false;
                    }
                    if(gv.Modify_Tab5){
                        main_tab5_Modify();
                    }
                    break;
            }

            return mListViews.get(arg1 % 5);
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == (arg1);
        }

        @Override
        public void restoreState(Parcelable arg0, ClassLoader arg1) {
        }

        @Override
        public Parcelable saveState() {
            return null;
        }

        @Override
        public void startUpdate(View arg0) {
        }
    }

    /**
     * 控制項設定
     */
    //基本資料main_tab1
    private void main_tab1_controls() {
        bundle = getIntent().getExtras();
        ll_Custody_F = (LinearLayout) findViewById(R.id.ll_Custody_F);
        ll_Custody_T = (LinearLayout) findViewById(R.id.ll_Custody_T);
        tv_FNumber = (TextView) findViewById(R.id.tv_FNumber);
        tv_MHospital = (TextView) findViewById(R.id.tv_MHospital);
        tv_Time1 = (TextView) findViewById(R.id.tv_Time1);
        tv_Time2 = (TextView) findViewById(R.id.tv_Time2);
        tv_Time3 = (TextView) findViewById(R.id.tv_Time3);
        tv_Time4 = (TextView) findViewById(R.id.tv_Time4);
        tv_Time5 = (TextView) findViewById(R.id.tv_Time5);
        tv_Time6 = (TextView) findViewById(R.id.tv_Time6);
        tv_SCustody = (TextView) findViewById(R.id.tv_SCustody);
        et_Date = (EditText) findViewById(R.id.et_Date);
        et_AttendanceUnit = (EditText) findViewById(R.id.et_AttendanceUnit);
        et_LocationHappen = (EditText) findViewById(R.id.et_LocationHappen);
        et_AssistanceUnit = (EditText) findViewById(R.id.et_AssistanceUnit);
        et_HospitalAddress = (EditText) findViewById(R.id.et_HospitalAddress);
        et_Time1 = (EditText) findViewById(R.id.et_Time1);
        et_Time2 = (EditText) findViewById(R.id.et_Time2);
        et_Time3 = (EditText) findViewById(R.id.et_Time3);
        et_Time4 = (EditText) findViewById(R.id.et_Time4);
        et_Time5 = (EditText) findViewById(R.id.et_Time5);
        et_Time6 = (EditText) findViewById(R.id.et_Time6);
        et_Name = (EditText) findViewById(R.id.et_Name);
        et_Age = (EditText) findViewById(R.id.et_Age);
        et_ID = (EditText) findViewById(R.id.et_ID);
        et_Address = (EditText) findViewById(R.id.et_Address);
        et_RCustody = (EditText) findViewById(R.id.et_RCustody);
        et_Custody = (EditText) findViewById(R.id.et_Custody);
        rg_AcceptedUnit = (RadioGroup) findViewById(R.id.rg_AcceptedUnit);
        rb_AcceptedUnit1 = (RadioButton) findViewById(R.id.rb_AcceptedUnit1);
        rb_AcceptedUnit2 = (RadioButton) findViewById(R.id.rb_AcceptedUnit2);
        btn_Upload = (Button) findViewById(R.id.btn_Upload);
        btn_MHospital = (Button) findViewById(R.id.btn_MHospital);
        btn_Address = (Button) findViewById(R.id.btn_Address);
        iv_SCustody = (ImageView) findViewById(R.id.iv_SCustody);
        cb_Critical = (CheckBox) findViewById(R.id.cb_Critical);
        cb_AgeM = (CheckBox) findViewById(R.id.cb_AgeM);
        cb_AgeA = (CheckBox) findViewById(R.id.cb_AgeA);
        sp_PDF = (Spinner) findViewById(R.id.sp_PDF);
        sp_PDF.setAdapter(adapter_PDF);
        sp_HospitalAddress = (Spinner) findViewById(R.id.sp_HospitalAddress);
        sp_HospitalAddress.setAdapter(adapter_HospitalAddress);
        sp_UploadHospital = (Spinner) findViewById(R.id.sp_UploadHospital);
        sp_UploadHospital.setAdapter(adapter_UploadHospital);
        sp_Area = (Spinner) findViewById(R.id.sp_Area);
        sp_Area.setAdapter(adapter_Area);
        sp_RCustody = (Spinner) findViewById(R.id.sp_RCustody);
        sp_RCustody.setAdapter(adapter_Relationship);

        for(int i = 0; i < cb_HospitalAddress_Name.length; i++){
            cb_HospitalAddress_Name[i] = (CheckBox) findViewById(cb_HospitalAddress_ID[i]);
        }
        for(int i = 0; i < cb_NoHospital_Name.length; i++){
            cb_NoHospital_Name[i] = (CheckBox) findViewById(cb_NoHospital_ID[i]);
        }
        for(int i = 0; i <cb_Sex_Name.length; i++){
            cb_Sex_Name[i] = (CheckBox) findViewById(cb_Sex_ID[i]);
        }
        for(int i = 0; i < cb_Property_Name.length; i++){
            cb_Property_Name[i] = (CheckBox) findViewById(cb_Property_ID[i]);
        }
    }

    //現場狀況main_tab2
    private void main_tab2_controls() {
        fl_Trauma_T = (FrameLayout) findViewById(R.id.fl_Trauma_T);
        fl_Trauma_F = (FrameLayout) findViewById(R.id.fl_Trauma_F);
        fl_OHCA = (FrameLayout) findViewById(R.id.fl_OHCA);
        ll_Trauma1_T = (LinearLayout) findViewById(R.id.ll_Trauma1_T);
        ll_Trauma2_T = (LinearLayout) findViewById(R.id.ll_Trauma2_T);
        ll_NTrauma6 = (LinearLayout) findViewById(R.id.ll_NTrauma6);
        tv_Trauma_T = (TextView) findViewById(R.id.tv_Trauma_T);
        tv_Trauma_F = (TextView) findViewById(R.id.tv_Trauma_F);
        tv_OHCA = (TextView) findViewById(R.id.tv_OHCA);
        et_Trauma3 = (EditText) findViewById(R.id.et_Trauma3);
        et_Trauma9 = (EditText) findViewById(R.id.et_Trauma9);
        et_Traffic5 = (EditText) findViewById(R.id.et_Traffic5);
        et_NTrauma15 = (EditText) findViewById(R.id.et_NTrauma15);
        et_ROSC2 = (EditText) findViewById(R.id.et_ROSC2);
        et_LTime = (EditText) findViewById(R.id.et_LTime);
        cb_Smile = (CheckBox) findViewById(R.id.cb_Smile);
        cb_LArm = (CheckBox) findViewById(R.id.cb_LArm);
        cb_Speech = (CheckBox) findViewById(R.id.cb_Speech);

        for(int i = 0; i < cb_Trauma_Switch_Name.length; i++){
            cb_Trauma_Switch_Name[i] = (CheckBox) findViewById(cb_Trauma_Switch_ID[i]);
        }
        for(int i = 0; i < cb_Trauma_Name.length; i++){
            cb_Trauma_Name[i] = (CheckBox) findViewById(cb_Trauma_ID[i]);
        }
        for(int i = 0; i < cb_Trauma1_Name.length; i++){
            cb_Trauma1_Name[i] = (CheckBox) findViewById(cb_Trauma1_ID[i]);
        }
        for(int i = 0; i < cb_Traffic_Name.length; i++){
            cb_Traffic_Name[i] = (CheckBox) findViewById(cb_Traffic_ID[i]);
        }
        for(int i = 0; i < cb_NTrauma_Name.length; i++){
            cb_NTrauma_Name[i] = (CheckBox) findViewById(cb_NTrauma_ID[i]);
        }
        for(int i = 0; i < cb_NTrauma6_Name.length; i++){
            cb_NTrauma6_Name[i] = (CheckBox) findViewById(cb_NTrauma6_ID[i]);
        }
        for(int i = 0; i < cb_Witnesses_Name.length; i++){
            cb_Witnesses_Name[i] = (CheckBox) findViewById(cb_Witnesses_ID[i]);
        }
        for(int i = 0; i < cb_CPR_Name.length; i++){
            cb_CPR_Name[i] = (CheckBox) findViewById(cb_CPR_ID[i]);
        }
        for(int i = 0; i < cb_PAD_Name.length; i++){
            cb_PAD_Name[i] = (CheckBox) findViewById(cb_PAD_ID[i]);
        }
        for(int i = 0; i < cb_ROSC_Name.length; i++){
            cb_ROSC_Name[i] = (CheckBox) findViewById(cb_ROSC_ID[i]);
        }
        for(int i = 0; i < cb_PSpecies_Name.length; i++){
            cb_PSpecies_Name[i] = (CheckBox) findViewById(cb_PSpecies_ID[i]);
        }
        for(int i = 0; i < cb_Stroke_Name.length; i++){
            cb_Stroke_Name[i] = (CheckBox) findViewById(cb_Stroke_ID[i]);
        }
    }

    //急救處置main_tab3
    private void main_tab3_controls() {
        ll_Breathing = (LinearLayout) findViewById(R.id.ll_Breathing);
        ll_TDisposal = (LinearLayout) findViewById(R.id.ll_TDisposal);
        ll_CPRDisposal = (LinearLayout) findViewById(R.id.ll_CPRDisposal);
        ll_CPRDisposal3 = (LinearLayout) findViewById(R.id.ll_CPRDisposal3);
        ll_DDisposal = (LinearLayout) findViewById(R.id.ll_DDisposal);
        ll_DDisposal1 = (LinearLayout) findViewById(R.id.ll_DDisposal1);
        ll_ODisposal = (LinearLayout) findViewById(R.id.ll_ODisposal);
        ll_ALSDisposal = (LinearLayout) findViewById(R.id.ll_ALSDisposal);
        ll_SBreathing = (LinearLayout) findViewById(R.id.ll_SBreathing);
        ll_STDisposal = (LinearLayout) findViewById(R.id.ll_STDisposal);
        ll_SCPRDisposal = (LinearLayout) findViewById(R.id.ll_SCPRDisposal);
        ll_SDDisposal = (LinearLayout) findViewById(R.id.ll_SDDisposal);
        ll_SODisposal = (LinearLayout) findViewById(R.id.ll_SODisposal);
        ll_SALSDisposal = (LinearLayout) findViewById(R.id.ll_SALSDisposal);
        et_Breathing5 = (EditText) findViewById(R.id.et_Breathing5);
        et_Breathing6 = (EditText) findViewById(R.id.et_Breathing6);
        et_Breathing7 = (EditText) findViewById(R.id.et_Breathing7);
        et_Breathing10 = (EditText) findViewById(R.id.et_Breathing10);
        et_TDisposal8 = (EditText) findViewById(R.id.et_TDisposal8);
        et_CPRDisposal1 = (EditText) findViewById(R.id.et_CPRDisposal1);
        et_CPRDisposal1_Time = (EditText) findViewById(R.id.et_CPRDisposal1_Time);
        et_CPRDisposal3_Time = (EditText) findViewById(R.id.et_CPRDisposal3_Time);
        et_CPRDisposal3_1 = (EditText) findViewById(R.id.et_CPRDisposal3_1);
        et_DDisposal1 = (EditText) findViewById(R.id.et_DDisposal1);
        et_DDisposal1_1 = (EditText) findViewById(R.id.et_DDisposal1_1);
        et_DDisposal1_2 = (EditText) findViewById(R.id.et_DDisposal1_2);
        et_DDisposal1_3 = (EditText) findViewById(R.id.et_DDisposal1_3);
        et_DDisposal3 = (EditText) findViewById(R.id.et_DDisposal3);
        et_DDisposal4 = (EditText) findViewById(R.id.et_DDisposal4);
        et_ODisposal4 = (EditText) findViewById(R.id.et_ODisposal4);
        et_ODisposal5 = (EditText) findViewById(R.id.et_ODisposal5);
        et_ALSDisposal1_1 = (EditText) findViewById(R.id.et_ALSDisposal1_1);
        et_ALSDisposal1_2 = (EditText) findViewById(R.id.et_ALSDisposal1_2);
        et_ALSDisposal2_1 = (EditText) findViewById(R.id.et_ALSDisposal2_1);
        et_ALSDisposal2_2 = (EditText) findViewById(R.id.et_ALSDisposal2_2);
        et_Posture = (EditText) findViewById(R.id.et_Posture);
        et_DrugUseTime1 = (EditText) findViewById(R.id.et_DrugUseTime1);
        et_DrugUseTime2 = (EditText) findViewById(R.id.et_DrugUseTime2);
        et_DrugUseTime3 = (EditText) findViewById(R.id.et_DrugUseTime3);
        et_DrugUseTime4 = (EditText) findViewById(R.id.et_DrugUseTime4);
        et_DrugUser1 = (EditText) findViewById(R.id.et_DrugUser1);
        et_DrugUser2 = (EditText) findViewById(R.id.et_DrugUser2);
        et_DrugUser3 = (EditText) findViewById(R.id.et_DrugUser3);
        et_DrugUser4 = (EditText) findViewById(R.id.et_DrugUser4);

        iv_Breathing = (ImageView) findViewById(R.id.iv_Breathing);
        iv_Breathing.setBackgroundResource(R.drawable.menu_icon_f);
        iv_TDisposal = (ImageView) findViewById(R.id.iv_TDisposal);
        iv_TDisposal.setBackgroundResource(R.drawable.menu_icon_f);
        iv_CPRDisposal = (ImageView) findViewById(R.id.iv_CPRDisposal);
        iv_CPRDisposal.setBackgroundResource(R.drawable.menu_icon_f);
        iv_DDisposal = (ImageView) findViewById(R.id.iv_DDisposal);
        iv_DDisposal.setBackgroundResource(R.drawable.menu_icon_f);
        iv_ODisposal = (ImageView) findViewById(R.id.iv_ODisposal);
        iv_ODisposal.setBackgroundResource(R.drawable.menu_icon_f);
        iv_ALSDisposal = (ImageView) findViewById(R.id.iv_ALSDisposal);
        iv_ALSDisposal.setBackgroundResource(R.drawable.menu_icon_f);

        sp_Posture = (Spinner) findViewById(R.id.sp_Posture);//下拉選單樣式
        sp_Posture.setAdapter(adapter_Posture);
        sp_ODisposal4 = (Spinner) findViewById(R.id.sp_ODisposal4);//下拉選單樣式
        sp_ODisposal4.setAdapter(adapter_ODisposal4);
        sp_DrugUseName1 = (Spinner) findViewById(R.id.sp_DrugUseName1);//下拉選單樣式
        sp_DrugUseName1.setAdapter(adapter_DrugUseName);
        sp_DrugUseName2 = (Spinner) findViewById(R.id.sp_DrugUseName2);//下拉選單樣式
        sp_DrugUseName2.setAdapter(adapter_DrugUseName);
        sp_DrugUseName3 = (Spinner) findViewById(R.id.sp_DrugUseName3);//下拉選單樣式
        sp_DrugUseName3.setAdapter(adapter_DrugUseName);
        sp_DrugUseName4 = (Spinner) findViewById(R.id.sp_DrugUseName4);//下拉選單樣式
        sp_DrugUseName4.setAdapter(adapter_DrugUseName);
        sp_DrugUseDose1 = (Spinner) findViewById(R.id.sp_DrugUseDose1);//下拉選單樣式
        sp_DrugUseDose1.setAdapter(adapter_DrugUseDose);
        sp_DrugUseDose2 = (Spinner) findViewById(R.id.sp_DrugUseDose2);//下拉選單樣式
        sp_DrugUseDose2.setAdapter(adapter_DrugUseDose);
        sp_DrugUseDose3 = (Spinner) findViewById(R.id.sp_DrugUseDose3);//下拉選單樣式
        sp_DrugUseDose3.setAdapter(adapter_DrugUseDose);
        sp_DrugUseDose4 = (Spinner) findViewById(R.id.sp_DrugUseDose4);//下拉選單樣式
        sp_DrugUseDose4.setAdapter(adapter_DrugUseDose);

        for(int i = 0; i < cb_Breathing_Name.length; i++){
            cb_Breathing_Name[i] = (CheckBox) findViewById(cb_Breathing_ID[i]);
        }
        for(int i = 0; i < cb_TDisposal_Name.length; i++){
            cb_TDisposal_Name[i] = (CheckBox) findViewById(cb_TDisposal_ID[i]);
        }
        for(int i = 0; i < cb_CPRDisposal_Name.length; i++){
            cb_CPRDisposal_Name[i] = (CheckBox) findViewById(cb_CPRDisposal_ID[i]);
        }
        for(int i = 0; i < cb_CPRDisposal3_Name.length; i++){
            cb_CPRDisposal3_Name[i] = (CheckBox) findViewById(cb_CPRDisposal3_ID[i]);
        }
        for(int i = 0; i < cb_DDisposal_Name.length; i++){
            cb_DDisposal_Name[i] = (CheckBox) findViewById(cb_DDisposal_ID[i]);
        }
        for(int i = 0; i < cb_DDisposal1_Name.length; i++){
            cb_DDisposal1_Name[i] = (CheckBox) findViewById(cb_DDisposal1_ID[i]);
        }
        for(int i = 0; i < cb_ODisposal_Name.length; i++){
            cb_ODisposal_Name[i] = (CheckBox) findViewById(cb_ODisposal_ID[i]);
        }
        for(int i = 0; i < cb_ALSDisposal_Name.length; i++){
            cb_ALSDisposal_Name[i] = (CheckBox) findViewById(cb_ALSDisposal_ID[i]);
        }
    }

    //病患相關main_tab4
    private void main_tab4_controls() {
        ll_MHistory = (LinearLayout) findViewById(R.id.ll_MHistory);
        ll_Allergies = (LinearLayout) findViewById(R.id.ll_Allergies);
        ll_People = (LinearLayout) findViewById(R.id.ll_People);
        ll_SMHistory = (LinearLayout) findViewById(R.id.ll_SMHistory);
        ll_SAllergies = (LinearLayout) findViewById(R.id.ll_SAllergies);
        et_Q1 = (EditText) findViewById(R.id.et_Q1);
        et_Q2 = (EditText) findViewById(R.id.et_Q2);
        et_Q3 = (EditText) findViewById(R.id.et_Q3);
        et_Q4 = (EditText) findViewById(R.id.et_Q4);
        et_Q5 = (EditText) findViewById(R.id.et_Q5);
        et_MHistory12 = (EditText) findViewById(R.id.et_MHistory12);
        et_Allergies1 = (EditText) findViewById(R.id.et_Allergies1);
        et_Allergies2 = (EditText) findViewById(R.id.et_Allergies2);
        et_Allergies3 = (EditText) findViewById(R.id.et_Allergies3);
        et_People = (EditText) findViewById(R.id.et_People);

        iv_MHistory = (ImageView) findViewById(R.id.iv_MHistory);
        iv_MHistory.setBackgroundResource(R.drawable.menu_icon_f);
        iv_Allergies = (ImageView) findViewById(R.id.iv_Allergies);
        iv_Allergies.setBackgroundResource(R.drawable.menu_icon_f);

        sp_Q1 = (Spinner) findViewById(R.id.sp_Q1);//下拉選單樣式
        sp_Q1.setAdapter(adapter_Q1);
        sp_Q2 = (Spinner) findViewById(R.id.sp_Q2);//下拉選單樣式
        sp_Q2.setAdapter(adapter_Q2);
        sp_Q3 = (Spinner) findViewById(R.id.sp_Q3);//下拉選單樣式
        sp_Q3.setAdapter(adapter_Q3);
        sp_Q4 = (Spinner) findViewById(R.id.sp_Q4);//下拉選單樣式
        sp_Q4.setAdapter(adapter_Q4);
        sp_People = (Spinner) findViewById(R.id.sp_People);//下拉選單樣式
        sp_People.setAdapter(adapter_People);

        for(int i = 0; i < cb_MHistory_Name.length; i++){
            cb_MHistory_Name[i] = (CheckBox) findViewById(cb_MHistory_ID[i]);
        }
        for(int i = 0; i < cb_Allergies_Name.length; i++){
            cb_Allergies_Name[i] = (CheckBox) findViewById(cb_Allergies_ID[i]);
        }
        for(int i = 0; i < iv_People_Name.length; i++){
            iv_People_Name[i] = (ImageView) findViewById(iv_People_ID[i]);
        }
    }

    //生命徵象main_tab5
    private void main_tab5_controls() {
        ll_BP1 = (LinearLayout) findViewById(R.id.ll_BP1);
        ll_BP2 = (LinearLayout) findViewById(R.id.ll_BP2);
        ll_BP3 = (LinearLayout) findViewById(R.id.ll_BP3);
        tv_LifeTime = (TextView) findViewById(R.id.tv_LifeTime);
        et_LifeTime1 = (EditText) findViewById(R.id.et_LifeTime1);
        et_LifeBreathing1 = (EditText) findViewById(R.id.et_LifeBreathing1);
        et_Pulse1 = (EditText) findViewById(R.id.et_Pulse1);
        et_GCS1_E = (EditText) findViewById(R.id.et_GCS1_E);
        et_GCS1_V = (EditText) findViewById(R.id.et_GCS1_V);
        et_GCS1_M = (EditText) findViewById(R.id.et_GCS1_M);
        et_SBP1 = (EditText) findViewById(R.id.et_SBP1);
        et_DBP1 = (EditText) findViewById(R.id.et_DBP1);
        et_SpO21 = (EditText) findViewById(R.id.et_SpO21);
        et_Temperature1 = (EditText) findViewById(R.id.et_Temperature1);
        et_LifeTime2 = (EditText) findViewById(R.id.et_LifeTime2);
        et_LifeBreathing2 = (EditText) findViewById(R.id.et_LifeBreathing2);
        et_Pulse2 = (EditText) findViewById(R.id.et_Pulse2);
        et_GCS2_E = (EditText) findViewById(R.id.et_GCS2_E);
        et_GCS2_V = (EditText) findViewById(R.id.et_GCS2_V);
        et_GCS2_M = (EditText) findViewById(R.id.et_GCS2_M);
        et_SBP2 = (EditText) findViewById(R.id.et_SBP2);
        et_DBP2 = (EditText) findViewById(R.id.et_DBP2);
        et_SpO22 = (EditText) findViewById(R.id.et_SpO22);
        et_Temperature2 = (EditText) findViewById(R.id.et_Temperature2);
        et_LifeBreathing3 = (EditText) findViewById(R.id.et_LifeBreathing3);
        et_Pulse3 = (EditText) findViewById(R.id.et_Pulse3);
        et_GCS3_E = (EditText) findViewById(R.id.et_GCS3_E);
        et_GCS3_V = (EditText) findViewById(R.id.et_GCS3_V);
        et_GCS3_M = (EditText) findViewById(R.id.et_GCS3_M);
        et_SBP3 = (EditText) findViewById(R.id.et_SBP3);
        et_DBP3 = (EditText) findViewById(R.id.et_DBP3);
        et_SpO23 = (EditText) findViewById(R.id.et_SpO23);
        et_Temperature3 = (EditText) findViewById(R.id.et_Temperature3);
        rg_BP1 = (RadioGroup) findViewById(R.id.rg_BP1);
        rb_BP1_1 = (RadioButton) findViewById(R.id.rb_BP1_1);
        rb_BP1_2 = (RadioButton) findViewById(R.id.rb_BP1_2);
        rg_BP2 = (RadioGroup) findViewById(R.id.rg_BP2);
        rb_BP2_1 = (RadioButton) findViewById(R.id.rb_BP2_1);
        rb_BP2_2 = (RadioButton) findViewById(R.id.rb_BP2_2);
        rg_BP3 = (RadioGroup) findViewById(R.id.rg_BP3);
        rb_BP3_1 = (RadioButton) findViewById(R.id.rb_BP3_1);
        rb_BP3_2 = (RadioButton) findViewById(R.id.rb_BP3_2);
        //心電圖
        bt_ecg_write = (Button) findViewById(R.id.bt_ecg_write);
        bt_ecg_read = (Button) findViewById(R.id.bt_ecg_read);

        sp_Consciousness1 = (Spinner) findViewById(R.id.sp_Consciousness1);//下拉選單樣式
        sp_Consciousness1.setAdapter(adapter_Consciousness);
        sp_Consciousness2 = (Spinner) findViewById(R.id.sp_Consciousness2);//下拉選單樣式
        sp_Consciousness2.setAdapter(adapter_Consciousness);
        sp_Consciousness3 = (Spinner) findViewById(R.id.sp_Consciousness3);//下拉選單樣式
        sp_Consciousness3.setAdapter(adapter_Consciousness);
        sp_LifeBreathing1 = (Spinner) findViewById(R.id.sp_LifeBreathing1);//下拉選單樣式
        sp_LifeBreathing1.setAdapter(adapter_Exception);
        sp_LifeBreathing2 = (Spinner) findViewById(R.id.sp_LifeBreathing2);//下拉選單樣式
        sp_LifeBreathing2.setAdapter(adapter_Exception);
        sp_LifeBreathing3 = (Spinner) findViewById(R.id.sp_LifeBreathing3);//下拉選單樣式
        sp_LifeBreathing3.setAdapter(adapter_Exception);
        sp_Pulse1 = (Spinner) findViewById(R.id.sp_Pulse1);//下拉選單樣式
        sp_Pulse1.setAdapter(adapter_Exception);
        sp_Pulse2 = (Spinner) findViewById(R.id.sp_Pulse2);//下拉選單樣式
        sp_Pulse2.setAdapter(adapter_Exception);
        sp_Pulse3 = (Spinner) findViewById(R.id.sp_Pulse3);//下拉選單樣式
        sp_Pulse3.setAdapter(adapter_Exception);
        sp_GCS1_E = (Spinner) findViewById(R.id.sp_GCS1_E);//下拉選單樣式
        sp_GCS1_E.setAdapter(adapter_GCSE);
        sp_GCS1_V = (Spinner) findViewById(R.id.sp_GCS1_V);//下拉選單樣式
        sp_GCS1_V.setAdapter(adapter_GCSV);
        sp_GCS1_M = (Spinner) findViewById(R.id.sp_GCS1_M);//下拉選單樣式
        sp_GCS1_M.setAdapter(adapter_GCSM);
        sp_GCS2_E = (Spinner) findViewById(R.id.sp_GCS2_E);//下拉選單樣式
        sp_GCS2_E.setAdapter(adapter_GCSE);
        sp_GCS2_V = (Spinner) findViewById(R.id.sp_GCS2_V);//下拉選單樣式
        sp_GCS2_V.setAdapter(adapter_GCSV);
        sp_GCS2_M = (Spinner) findViewById(R.id.sp_GCS2_M);//下拉選單樣式
        sp_GCS2_M.setAdapter(adapter_GCSM);
        sp_GCS3_E = (Spinner) findViewById(R.id.sp_GCS3_E);//下拉選單樣式
        sp_GCS3_E.setAdapter(adapter_GCSE);
        sp_GCS3_V = (Spinner) findViewById(R.id.sp_GCS3_V);//下拉選單樣式
        sp_GCS3_V.setAdapter(adapter_GCSV);
        sp_GCS3_M = (Spinner) findViewById(R.id.sp_GCS3_M);//下拉選單樣式
        sp_GCS3_M.setAdapter(adapter_GCSM);
        sp_SBP1 = (Spinner) findViewById(R.id.sp_SBP1);//下拉選單樣式
        sp_SBP1.setAdapter(adapter_Exception);
        sp_SBP2 = (Spinner) findViewById(R.id.sp_SBP2);//下拉選單樣式
        sp_SBP2.setAdapter(adapter_Exception);
        sp_SBP3 = (Spinner) findViewById(R.id.sp_SBP3);//下拉選單樣式
        sp_SBP3.setAdapter(adapter_Exception);
        sp_DBP1 = (Spinner) findViewById(R.id.sp_DBP1);//下拉選單樣式
        sp_DBP1.setAdapter(adapter_Exception);
        sp_DBP2 = (Spinner) findViewById(R.id.sp_DBP2);//下拉選單樣式
        sp_DBP2.setAdapter(adapter_Exception);
        sp_DBP3 = (Spinner) findViewById(R.id.sp_DBP3);//下拉選單樣式
        sp_DBP3.setAdapter(adapter_Exception);
        sp_BPressure1_A = (Spinner) findViewById(R.id.sp_BPressure1_A);//下拉選單樣式
        sp_BPressure1_A.setAdapter(adapter_BPressure);
        sp_BPressure2_A = (Spinner) findViewById(R.id.sp_BPressure2_A);//下拉選單樣式
        sp_BPressure2_A.setAdapter(adapter_BPressure);
        sp_BPressure3_A = (Spinner) findViewById(R.id.sp_BPressure3_A);//下拉選單樣式
        sp_BPressure3_A.setAdapter(adapter_BPressure);
        sp_SpO21 = (Spinner) findViewById(R.id.sp_SpO21);//下拉選單樣式
        sp_SpO21.setAdapter(adapter_Exception);
        sp_SpO22 = (Spinner) findViewById(R.id.sp_SpO22);//下拉選單樣式
        sp_SpO22.setAdapter(adapter_Exception);
        sp_SpO23 = (Spinner) findViewById(R.id.sp_SpO23);//下拉選單樣式
        sp_SpO23.setAdapter(adapter_Exception);
        sp_Temperature1 = (Spinner) findViewById(R.id.sp_Temperature1);//下拉選單樣式
        sp_Temperature1.setAdapter(adapter_Exception);
        sp_Temperature2 = (Spinner) findViewById(R.id.sp_Temperature2);//下拉選單樣式
        sp_Temperature2.setAdapter(adapter_Exception);
        sp_Temperature3 = (Spinner) findViewById(R.id.sp_Temperature3);//下拉選單樣式
        sp_Temperature3.setAdapter(adapter_Exception);
    }

    /**
     *控制項定義
     */
    private LinearLayout[] LinearLayoutCD(int[] ID) {
        LinearLayout[] Name = new LinearLayout[ID.length];
        for(int i = 0; i < Name.length; i++){
            Name[i] = (LinearLayout) findViewById(ID[i]);
        }
        return Name;
    }
    private Button[] ButtonCD(int[] ID) {
        Button[] Name = new Button[ID.length];
        for(int i = 0; i < Name.length; i++){
            Name[i] = (Button) findViewById(ID[i]);
        }
        return Name;
    }
    private CheckBox[] CheckBoxCD(int[] ID) {
        CheckBox[] Name = new CheckBox[ID.length];
        for(int i = 0; i < Name.length; i++){
            Name[i] = (CheckBox) findViewById(ID[i]);
        }
        return Name;
    }
    private TextView[] TextViewCD(int[] ID) {
        TextView[] Name = new TextView[ID.length];
        for(int i = 0; i < Name.length; i++){
            Name[i] = (TextView) findViewById(ID[i]);
        }
        return Name;
    }
    private EditText[] EditTextCD(int[] ID) {
        EditText[] Name = new EditText[ID.length];
        for(int i = 0; i < Name.length; i++){
            Name[i] = (EditText) findViewById(ID[i]);
        }
        return Name;
    }

    /**
     * 事件定義
     */
    //基本資料main_tab1
    private void main_tab1_Click() {
        //帶入資料
        if(bundle!=null) {
            try //因Java的規定，所以從這開始要用try..catch
            {
                tv_FNumber.setText(Html.fromHtml("<u>" + bundle.getString("CaseID").trim() + "</u>"));
                try //因Java的規定，所以從這開始要用try..catch
                {
                    et_Date.setText(bundle.getString("DispatchTime").trim().substring(0,10));
                } catch (Exception e) {
                    Log.e("log_tag", "時間 " + e.toString());
                }
                et_AttendanceUnit.setText(bundle.getString("DispVehicleID").trim());
                et_LocationHappen.setText(bundle.getString("ReportPlace").trim());
                et_LocationHappen.setText(bundle.getString("ReportPlace").trim());

                et_Name.setText(bundle.getString("Name").trim());
                et_ID.setText(bundle.getString("Personal_ID").trim());
                et_Age.setText(bundle.getString("Ages").trim());

                gv.Modify_Tab4 = true;

                for (int i = 0; i < cb_Sex_Name.length; i++) {
                    if (cb_Sex_Name[i].getText().toString().equals(bundle.getString("Gender"))) {
                        cb_Sex_Name[i].setChecked(true);
                    }
                    else{
                        cb_Sex_Name[i].setChecked(false);
                    }
                }



            }
            catch(Exception e)
            {
                Log.e("log_tag", "Error：" + e.toString());
            }
        }
        //Click事件設定
        btn_Upload.setOnClickListener(MainActivity.this);
        btn_MHospital.setOnClickListener(MainActivity.this);
        btn_Address.setOnClickListener(MainActivity.this);
        tv_Time1.setOnClickListener(MainActivity.this);
        tv_Time2.setOnClickListener(MainActivity.this);
        tv_Time3.setOnClickListener(MainActivity.this);
        tv_Time4.setOnClickListener(MainActivity.this);
        tv_Time5.setOnClickListener(MainActivity.this);
        tv_Time6.setOnClickListener(MainActivity.this);
        et_Time1.setOnFocusChangeListener(MainActivity.this);
        et_Time2.setOnFocusChangeListener(MainActivity.this);
        et_Time3.setOnFocusChangeListener(MainActivity.this);
        et_Time4.setOnFocusChangeListener(MainActivity.this);
        et_Time5.setOnFocusChangeListener(MainActivity.this);
        et_Time6.setOnFocusChangeListener(MainActivity.this);
        iv_SCustody.setOnClickListener(MainActivity.this);
        tv_SCustody.setOnClickListener(MainActivity.this);
        //監聽事件設定
        for(int i = 0; i < cb_HospitalAddress_Name.length; i++){
            cb_HospitalAddress_Name[i].setOnCheckedChangeListener(MainActivity.this);
        }
        for(int i = 0; i < cb_NoHospital_Name.length; i++){
            cb_NoHospital_Name[i].setOnCheckedChangeListener(MainActivity.this);
        }
        for(int i = 0; i < cb_Sex_Name.length; i++){
            cb_Sex_Name[i].setOnCheckedChangeListener(MainActivity.this);
        }
        for(int i = 0; i < cb_Property_Name.length; i++){
            cb_Property_Name[i].setOnCheckedChangeListener(MainActivity.this);
        }

        sp_PDF.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            public void onItemSelected(AdapterView adapterView, View view, int position, long id) {
                if (adapterView.getSelectedItemPosition() != 0) {
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_VIEW);
                    intent.setDataAndType(Uri.parse("file://" + Environment.getExternalStorageDirectory() + "/FirePDF/" + sp_PDF.getSelectedItem() + ".pdf"), "application/pdf");
                    startActivity(intent);

                    sp_PDF.setSelection(0);
                }
            }

            public void onNothingSelected(AdapterView arg0) {

            }
        });

        sp_HospitalAddress.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            public void onItemSelected(AdapterView adapterView, View view, int position, long id) {
                if (adapterView.getSelectedItemPosition() != 0 & adapterView.getSelectedItem().equals("其他")) {
                    sp_HospitalAddress.setVisibility(View.GONE);
                    sp_UploadHospital.setVisibility(View.GONE);
                    et_HospitalAddress.setVisibility(View.VISIBLE);
                }
            }

            public void onNothingSelected(AdapterView arg0) {

            }
        });

        sp_UploadHospital.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            public void onItemSelected(AdapterView adapterView, View view, int position, long id) {
                if (adapterView.getSelectedItemPosition() != 0) {
                    if (sp_HospitalAddress.getSelectedItemPosition() != 0) {
                        gv.PHospitalAddress = sp_HospitalAddress.getSelectedItemPosition();
                        PDialog = ProgressDialog.show(MainActivity.this, "請稍等...", "通知醫院中...", true);
                        new Thread(R_UploadHospital).start();
                    } else {
                        dialog = new AlertDialog.Builder(MainActivity.this);
                        dialog.setTitle("錯誤"); //設定dialog 的title顯示內容
                        dialog.setMessage("請選擇醫院!!");
                        dialog.setCancelable(false); //關閉 Android 系統的主要功能鍵(menu,home等...)
                        dialog.setPositiveButton("確定", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        });
                        dialog.show();
                    }
                }
            }

            public void onNothingSelected(AdapterView arg0) {

            }
        });

        et_Date.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    String str = et_Date.getText().toString();
                    if (str.length() == 8) {
                        Pattern pattern = Pattern.compile("[0-9]*");
                        Matcher isNum = pattern.matcher(str);
                        if (isNum.matches()) {
                            et_Date.setText(str.substring(0, 4) + "-" + str.substring(4, 6) + "-" + str.substring(6));
                        } else {
                            et_Date.setText("");
                            Toast.makeText(MainActivity.this, "輸入格式錯誤!!", Toast.LENGTH_LONG).show();
                        }
                    } else if (str.length() == 10) {
                        String[] Date = str.split("-");
                        if (Date.length == 3) {
                            Pattern pattern = Pattern.compile("[0-9]*");
                            Matcher isNum1 = pattern.matcher(Date[0]);
                            Matcher isNum2 = pattern.matcher(Date[1]);
                            Matcher isNum3 = pattern.matcher(Date[2]);
                            if (isNum1.matches() & isNum2.matches() & isNum3.matches() & Date[0].length() == 4 & Date[1].length() == 2 & Date[2].length() == 2) {
                                et_Date.setText(Date[0] + "-" + Date[1] + "-" + Date[2]);
                            } else {
                                et_Date.setText("");
                                Toast.makeText(MainActivity.this, "輸入格式錯誤!!", Toast.LENGTH_LONG).show();
                            }
                        } else {
                            et_Date.setText("");
                            Toast.makeText(MainActivity.this, "輸入格式錯誤!!", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        et_Date.setText("");
                        Toast.makeText(MainActivity.this, "輸入格式錯誤!!", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

        et_HospitalAddress.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                sp_HospitalAddress.setVisibility(View.VISIBLE);
                sp_UploadHospital.setVisibility(View.VISIBLE);
                et_HospitalAddress.setVisibility(View.GONE);
                return true;
            }
        });

        et_Age.setOnFocusChangeListener(new android.view.View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    String str = et_Age.getText().toString();
                    if (str.length() == 6 || str.length() == 7) {
                        try {
                            String Now_Time = formatter_Age.format(curDate_Age);
                            int year = Integer.parseInt(Now_Time) - 19110000;
                            year = (year - Integer.parseInt(str)) / 10000;
                            et_Age.setText(year + "");
                        } catch (Exception e) {
                            Toast.makeText(MainActivity.this, "輸入格式錯誤!!", Toast.LENGTH_LONG).show();
                            et_Age.setText("");
                        }
                    } else if (str.length() == 8) {
                        try {
                            String Now_Time = formatter_Age.format(curDate_Age);
                            int year = Integer.parseInt(Now_Time);
                            year = (year - Integer.parseInt(str)) / 10000;
                            et_Age.setText(year + "");
                        } catch (Exception e) {
                            Toast.makeText(MainActivity.this, "輸入格式錯誤!!", Toast.LENGTH_LONG).show();
                            et_Age.setText("");
                        }
                    }
                }
            }
        });

        sp_RCustody.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            public void onItemSelected(AdapterView adapterView, View view, int position, long id) {
                if (adapterView.getSelectedItemPosition() != 0 & adapterView.getSelectedItem().equals("其他")) {
                    sp_RCustody.setVisibility(View.GONE);
                    et_RCustody.setVisibility(View.VISIBLE);
                }
            }

            public void onNothingSelected(AdapterView arg0) {

            }
        });

        et_RCustody.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                sp_RCustody.setVisibility(View.VISIBLE);
                et_RCustody.setVisibility(View.GONE);
                return true;
            }
        });

    }

    //現場狀況main_tab2
    private void main_tab2_Click() {
        for(int i = 0; i < cb_Trauma_Switch_Name.length; i++){
            cb_Trauma_Switch_Name[i].setOnCheckedChangeListener(MainActivity.this);
        }
        for(int i = 0; i < cb_Traffic_Name.length; i++){
            cb_Traffic_Name[i].setOnCheckedChangeListener(MainActivity.this);
        }
        for(int i = 0; i < cb_Trauma_Name.length; i++){
            cb_Trauma_Name[i].setOnCheckedChangeListener(MainActivity.this);
        }
        for(int i = 0; i < cb_NTrauma_Name.length; i++){
            cb_NTrauma_Name[i].setOnCheckedChangeListener(MainActivity.this);
        }
        for(int i = 0; i < cb_Witnesses_Name.length; i++){
            cb_Witnesses_Name[i].setOnCheckedChangeListener(MainActivity.this);
        }
        for(int i = 0; i < cb_CPR_Name.length; i++){
            cb_CPR_Name[i].setOnCheckedChangeListener(MainActivity.this);
        }
        for(int i = 0; i < cb_PAD_Name.length; i++){
            cb_PAD_Name[i].setOnCheckedChangeListener(MainActivity.this);
        }
        for(int i = 0; i < cb_ROSC_Name.length; i++){
            cb_ROSC_Name[i].setOnCheckedChangeListener(MainActivity.this);
        }
        et_ROSC2.setOnFocusChangeListener(MainActivity.this);
        for(int i = 0; i < cb_PSpecies_Name.length; i++){
            cb_PSpecies_Name[i].setOnCheckedChangeListener(MainActivity.this);
        }
        for(int i = 0; i < cb_Stroke_Name.length; i++){
            cb_Stroke_Name[i].setOnCheckedChangeListener(MainActivity.this);
        }
        et_LTime.setOnFocusChangeListener(MainActivity.this);
    }

    //急救處置main_tab3
    private void main_tab3_Click() {
        ll_SBreathing.setOnClickListener(MainActivity.this);
        ll_STDisposal.setOnClickListener(MainActivity.this);
        ll_SCPRDisposal.setOnClickListener(MainActivity.this);
        ll_SDDisposal.setOnClickListener(MainActivity.this);
        ll_SODisposal.setOnClickListener(MainActivity.this);
        ll_SALSDisposal.setOnClickListener(MainActivity.this);
        cb_CPRDisposal_Name[2].setOnCheckedChangeListener(MainActivity.this);
        et_CPRDisposal1_Time.setOnFocusChangeListener(MainActivity.this);
        et_CPRDisposal3_Time.setOnFocusChangeListener(MainActivity.this);
        cb_DDisposal_Name[0].setOnCheckedChangeListener(MainActivity.this);
        et_DrugUseTime1.setOnFocusChangeListener(MainActivity.this);
        et_DrugUseTime2.setOnFocusChangeListener(MainActivity.this);
        et_DrugUseTime3.setOnFocusChangeListener(MainActivity.this);
        et_DrugUseTime4.setOnFocusChangeListener(MainActivity.this);
        //給藥

        sp_ODisposal4.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            public void onItemSelected(AdapterView adapterView, View view, int position, long id) {
                if (adapterView.getSelectedItemPosition() != 0) {
                    sp_ODisposal4.setVisibility(View.GONE);
                    et_ODisposal4.setVisibility(View.VISIBLE);
                    et_ODisposal4.setText(adapterView.getSelectedItem().toString());
                    adapterView.setSelection(0);
                }
            }

            public void onNothingSelected(AdapterView arg0) {

            }
        });

        et_ODisposal4.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                sp_ODisposal4.setVisibility(View.VISIBLE);
                et_ODisposal4.setVisibility(View.GONE);
                return true;
            }
        });

        sp_Posture.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            public void onItemSelected(AdapterView adapterView, View view, int position, long id) {
                if (adapterView.getSelectedItemPosition() != 0 & adapterView.getSelectedItem().equals("其他")) {
                    sp_Posture.setVisibility(View.GONE);
                    et_Posture.setVisibility(View.VISIBLE);
                }
            }

            public void onNothingSelected(AdapterView arg0) {

            }
        });

        et_Posture.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                sp_Posture.setVisibility(View.VISIBLE);
                et_Posture.setVisibility(View.GONE);
                return true;
            }
        });

        sp_DrugUseName1.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            public void onItemSelected(AdapterView adapterView, View view, int position, long id) {
                sp_DrugUseDose1.setAdapter(array_DrugUseDose[adapterView.getSelectedItemPosition()]);
                sp_DrugUseDose1.setSelection(gv.PDrugUseDose1);
            }

            public void onNothingSelected(AdapterView arg0) {

            }
        });
        sp_DrugUseName2.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            public void onItemSelected(AdapterView adapterView, View view, int position, long id) {
                sp_DrugUseDose2.setAdapter(array_DrugUseDose[adapterView.getSelectedItemPosition()]);
                sp_DrugUseDose2.setSelection(gv.PDrugUseDose2);
            }

            public void onNothingSelected(AdapterView arg0) {

            }
        });
        sp_DrugUseName3.setOnItemSelectedListener(new Spinner.OnItemSelectedListener(){
            public void onItemSelected(AdapterView adapterView, View view, int position, long id) {
                sp_DrugUseDose3.setAdapter(array_DrugUseDose[adapterView.getSelectedItemPosition()]);
                sp_DrugUseDose3.setSelection(gv.PDrugUseDose3);
            }
            public void onNothingSelected(AdapterView arg0) {

            }
        });
        sp_DrugUseName4.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            public void onItemSelected(AdapterView adapterView, View view, int position, long id) {
                sp_DrugUseDose4.setAdapter(array_DrugUseDose[adapterView.getSelectedItemPosition()]);
                sp_DrugUseDose4.setSelection(gv.PDrugUseDose4);
            }

            public void onNothingSelected(AdapterView arg0) {

            }
        });
    }

    //病患相關main_tab4
    private void main_tab4_Click() {

        ll_SMHistory.setOnClickListener(MainActivity.this);
        ll_SAllergies.setOnClickListener(MainActivity.this);
        ll_People.setOnClickListener(MainActivity.this);
        cb_MHistory_Name[4].setOnCheckedChangeListener(MainActivity.this);

        sp_Q1.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            public void onItemSelected(AdapterView adapterView, View view, int position, long id) {
                if (adapterView.getSelectedItemPosition() != 0) {
                    if (et_Q1.getText().toString().equals("")) {
                        et_Q1.setText(adapterView.getSelectedItem().toString());
                    } else {
                        et_Q1.setText(et_Q1.getText() + "," + adapterView.getSelectedItem());
                    }
                    adapterView.setSelection(0);
                }
            }

            public void onNothingSelected(AdapterView arg0) {

            }
        });
        sp_Q2.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            public void onItemSelected(AdapterView adapterView, View view, int position, long id) {
                if (adapterView.getSelectedItemPosition() != 0) {
                    if (et_Q2.getText().toString().equals("")) {
                        et_Q2.setText(adapterView.getSelectedItem().toString());
                    } else {
                        et_Q2.setText(et_Q2.getText() + "," + adapterView.getSelectedItem());
                    }
                    adapterView.setSelection(0);
                }
            }

            public void onNothingSelected(AdapterView arg0) {

            }
        });
        sp_Q3.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            public void onItemSelected(AdapterView adapterView, View view, int position, long id) {
                if (adapterView.getSelectedItemPosition() != 0) {
                    if (et_Q3.getText().toString().equals("")) {
                        et_Q3.setText(adapterView.getSelectedItem().toString());
                    } else {
                        et_Q3.setText(et_Q3.getText() + "," + adapterView.getSelectedItem());
                    }
                    adapterView.setSelection(0);
                }
            }

            public void onNothingSelected(AdapterView arg0) {

            }
        });
        sp_Q4.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            public void onItemSelected(AdapterView adapterView, View view, int position, long id) {
                if (adapterView.getSelectedItemPosition() != 0) {
                    if (et_Q4.getText().toString().equals("")) {
                        et_Q4.setText(adapterView.getSelectedItem().toString());
                    } else {
                        et_Q4.setText(et_Q4.getText() + "," + adapterView.getSelectedItem());
                    }
                    adapterView.setSelection(0);
                }
            }
            public void onNothingSelected(AdapterView arg0) {

            }
        });
        sp_People.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            public void onItemSelected(AdapterView adapterView, View view, int position, long id) {
                if (adapterView.getSelectedItemPosition() != 0) {
                    if (et_People.getText().toString().equals("")) {
                        et_People.setText(adapterView.getSelectedItem().toString());
                    } else {
                        et_People.setText(et_People.getText() + "\n" + adapterView.getSelectedItem());
                    }
                    adapterView.setSelection(0);
                }
            }

            public void onNothingSelected(AdapterView arg0) {

            }
        });
    }

    //生命徵象main_tab5
    private void main_tab5_Click() {

        tv_LifeTime.setOnClickListener(MainActivity.this);
        et_LifeTime1.setOnFocusChangeListener(MainActivity.this);
        et_LifeTime2.setOnFocusChangeListener(MainActivity.this);

        rg_BP1.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // TODO Auto-generated method stub
                switch (checkedId) {
                    case R.id.rb_BP1_1:
                        ll_BP1.setVisibility(View.VISIBLE);
                        sp_BPressure1_A.setVisibility(View.GONE);
                        break;
                    case R.id.rb_BP1_2:
                        ll_BP1.setVisibility(View.GONE);
                        sp_BPressure1_A.setVisibility(View.VISIBLE);
                        break;
                }

            }

        });
        rg_BP1.check(R.id.rb_BP1_1);

        rg_BP2.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // TODO Auto-generated method stub
                switch (checkedId) {
                    case R.id.rb_BP2_1:
                        ll_BP2.setVisibility(View.VISIBLE);
                        sp_BPressure2_A.setVisibility(View.GONE);
                        break;
                    case R.id.rb_BP2_2:
                        ll_BP2.setVisibility(View.GONE);
                        sp_BPressure2_A.setVisibility(View.VISIBLE);
                        break;
                }

            }

        });
        rg_BP2.check(R.id.rb_BP2_1);

        rg_BP3.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // TODO Auto-generated method stub
                switch (checkedId) {
                    case R.id.rb_BP3_1:
                        ll_BP3.setVisibility(View.VISIBLE);
                        sp_BPressure3_A.setVisibility(View.GONE);
                        break;
                    case R.id.rb_BP3_2:
                        ll_BP3.setVisibility(View.GONE);
                        sp_BPressure3_A.setVisibility(View.VISIBLE);
                        break;
                }

            }

        });
        rg_BP3.check(R.id.rb_BP3_1);

        sp_Consciousness1.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            public void onItemSelected(AdapterView adapterView, View view, int position, long id) {
                if(gv.GCS1_E.equals("") && gv.GCS1_V.equals("") && gv.GCS1_M.equals("")) {
                    if (adapterView.getSelectedItemPosition() == 1) {
                        et_GCS1_E.setText("4");
                        et_GCS1_V.setText("5");
                        et_GCS1_M.setText("6");
                        et_GCS1_E.setVisibility(View.VISIBLE);
                        et_GCS1_V.setVisibility(View.VISIBLE);
                        et_GCS1_M.setVisibility(View.VISIBLE);
                        sp_GCS1_E.setVisibility(View.GONE);
                        sp_GCS1_V.setVisibility(View.GONE);
                        sp_GCS1_M.setVisibility(View.GONE);
                    } else if (adapterView.getSelectedItemPosition() == 4) {
                        et_GCS1_E.setText("1");
                        et_GCS1_V.setText("1");
                        et_GCS1_M.setText("1");
                        et_GCS1_E.setVisibility(View.VISIBLE);
                        et_GCS1_V.setVisibility(View.VISIBLE);
                        et_GCS1_M.setVisibility(View.VISIBLE);
                        sp_GCS1_E.setVisibility(View.GONE);
                        sp_GCS1_V.setVisibility(View.GONE);
                        sp_GCS1_M.setVisibility(View.GONE);
                    }
                }
            }

            public void onNothingSelected(AdapterView arg0) {
                et_GCS1_E.setVisibility(View.VISIBLE);
                et_GCS1_V.setVisibility(View.VISIBLE);
                et_GCS1_M.setVisibility(View.VISIBLE);
                sp_GCS1_E.setVisibility(View.GONE);
                sp_GCS1_V.setVisibility(View.GONE);
                sp_GCS1_M.setVisibility(View.GONE);
            }
        });

        sp_Consciousness2.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            public void onItemSelected(AdapterView adapterView, View view, int position, long id) {
                if(gv.GCS2_E.equals("") && gv.GCS2_V.equals("") && gv.GCS2_M.equals("")) {
                    if (adapterView.getSelectedItemPosition() == 1) {
                        et_GCS2_E.setText("4");
                        et_GCS2_V.setText("5");
                        et_GCS2_M.setText("6");
                        et_GCS2_E.setVisibility(View.VISIBLE);
                        et_GCS2_V.setVisibility(View.VISIBLE);
                        et_GCS2_M.setVisibility(View.VISIBLE);
                        sp_GCS2_E.setVisibility(View.GONE);
                        sp_GCS2_V.setVisibility(View.GONE);
                        sp_GCS2_M.setVisibility(View.GONE);
                    } else if (adapterView.getSelectedItemPosition() == 4) {
                        et_GCS2_E.setText("1");
                        et_GCS2_V.setText("1");
                        et_GCS2_M.setText("1");
                        et_GCS2_E.setVisibility(View.VISIBLE);
                        et_GCS2_V.setVisibility(View.VISIBLE);
                        et_GCS2_M.setVisibility(View.VISIBLE);
                        sp_GCS2_E.setVisibility(View.GONE);
                        sp_GCS2_V.setVisibility(View.GONE);
                        sp_GCS2_M.setVisibility(View.GONE);
                    }
                }
            }

            public void onNothingSelected(AdapterView arg0) {
                et_GCS2_E.setVisibility(View.VISIBLE);
                et_GCS2_V.setVisibility(View.VISIBLE);
                et_GCS2_M.setVisibility(View.VISIBLE);
                sp_GCS2_E.setVisibility(View.GONE);
                sp_GCS2_V.setVisibility(View.GONE);
                sp_GCS2_M.setVisibility(View.GONE);
            }
        });

        sp_Consciousness3.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            public void onItemSelected(AdapterView adapterView, View view, int position, long id) {
                if(gv.GCS3_E.equals("") && gv.GCS3_V.equals("") && gv.GCS3_M.equals("")) {
                    if (adapterView.getSelectedItemPosition() == 1) {
                        et_GCS3_E.setText("4");
                        et_GCS3_V.setText("5");
                        et_GCS3_M.setText("6");
                        et_GCS3_E.setVisibility(View.VISIBLE);
                        et_GCS3_V.setVisibility(View.VISIBLE);
                        et_GCS3_M.setVisibility(View.VISIBLE);
                        sp_GCS3_E.setVisibility(View.GONE);
                        sp_GCS3_V.setVisibility(View.GONE);
                        sp_GCS3_M.setVisibility(View.GONE);
                    } else if (adapterView.getSelectedItemPosition() == 4) {
                        et_GCS3_E.setText("1");
                        et_GCS3_V.setText("1");
                        et_GCS3_M.setText("1");
                        et_GCS3_E.setVisibility(View.VISIBLE);
                        et_GCS3_V.setVisibility(View.VISIBLE);
                        et_GCS3_M.setVisibility(View.VISIBLE);
                        sp_GCS3_E.setVisibility(View.GONE);
                        sp_GCS3_V.setVisibility(View.GONE);
                        sp_GCS3_M.setVisibility(View.GONE);
                    }
                }
            }

            public void onNothingSelected(AdapterView arg0) {
                et_GCS3_E.setVisibility(View.VISIBLE);
                et_GCS3_V.setVisibility(View.VISIBLE);
                et_GCS3_M.setVisibility(View.VISIBLE);
                sp_GCS3_E.setVisibility(View.GONE);
                sp_GCS3_V.setVisibility(View.GONE);
                sp_GCS3_M.setVisibility(View.GONE);
            }
        });

        et_LifeBreathing1.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                et_LifeBreathing1.setVisibility(View.GONE);
                sp_LifeBreathing1.setVisibility(View.VISIBLE);
                return true;
            }
        });

        sp_LifeBreathing1.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            public void onItemSelected(AdapterView adapterView, View view, int position, long id) {
                if (adapterView.getSelectedItemPosition() != 0) {
                    et_LifeBreathing1.setText(adapterView.getSelectedItem().toString());
                    et_LifeBreathing1.setVisibility(View.VISIBLE);
                    sp_LifeBreathing1.setVisibility(View.GONE);
                }
            }

            public void onNothingSelected(AdapterView arg0) {
                et_LifeBreathing1.setVisibility(View.VISIBLE);
                sp_LifeBreathing1.setVisibility(View.GONE);
            }
        });

        et_LifeBreathing2.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                et_LifeBreathing2.setVisibility(View.GONE);
                sp_LifeBreathing2.setVisibility(View.VISIBLE);
                return true;
            }
        });

        sp_LifeBreathing2.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            public void onItemSelected(AdapterView adapterView, View view, int position, long id) {
                if (adapterView.getSelectedItemPosition() != 0) {
                    et_LifeBreathing2.setText(adapterView.getSelectedItem().toString());
                    et_LifeBreathing2.setVisibility(View.VISIBLE);
                    sp_LifeBreathing2.setVisibility(View.GONE);
                }
            }

            public void onNothingSelected(AdapterView arg0) {
                et_LifeBreathing2.setVisibility(View.VISIBLE);
                sp_LifeBreathing2.setVisibility(View.GONE);
            }
        });

        et_LifeBreathing3.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                et_LifeBreathing3.setVisibility(View.GONE);
                sp_LifeBreathing3.setVisibility(View.VISIBLE);
                return true;
            }
        });

        sp_LifeBreathing3.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            public void onItemSelected(AdapterView adapterView, View view, int position, long id) {
                if (adapterView.getSelectedItemPosition() != 0) {
                    et_LifeBreathing3.setText(adapterView.getSelectedItem().toString());
                    et_LifeBreathing3.setVisibility(View.VISIBLE);
                    sp_LifeBreathing3.setVisibility(View.GONE);
                }
            }

            public void onNothingSelected(AdapterView arg0) {
                et_LifeBreathing3.setVisibility(View.VISIBLE);
                sp_LifeBreathing3.setVisibility(View.GONE);
            }
        });

        et_Pulse1.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                et_Pulse1.setVisibility(View.GONE);
                sp_Pulse1.setVisibility(View.VISIBLE);
                return true;
            }
        });

        sp_Pulse1.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            public void onItemSelected(AdapterView adapterView, View view, int position, long id) {
                if (adapterView.getSelectedItemPosition() != 0) {
                    et_Pulse1.setText(adapterView.getSelectedItem().toString());
                    et_Pulse1.setVisibility(View.VISIBLE);
                    sp_Pulse1.setVisibility(View.GONE);
                }
            }

            public void onNothingSelected(AdapterView arg0) {
                et_Pulse1.setVisibility(View.VISIBLE);
                sp_Pulse1.setVisibility(View.GONE);
            }
        });

        et_Pulse2.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                et_Pulse2.setVisibility(View.GONE);
                sp_Pulse2.setVisibility(View.VISIBLE);
                return true;
            }
        });

        sp_Pulse2.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            public void onItemSelected(AdapterView adapterView, View view, int position, long id) {
                if (adapterView.getSelectedItemPosition() != 0) {
                    et_Pulse2.setText(adapterView.getSelectedItem().toString());
                    et_Pulse2.setVisibility(View.VISIBLE);
                    sp_Pulse2.setVisibility(View.GONE);
                }
            }

            public void onNothingSelected(AdapterView arg0) {
                et_Pulse2.setVisibility(View.VISIBLE);
                sp_Pulse2.setVisibility(View.GONE);
            }
        });

        et_Pulse3.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                et_Pulse3.setVisibility(View.GONE);
                sp_Pulse3.setVisibility(View.VISIBLE);
                return true;
            }
        });

        sp_Pulse3.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            public void onItemSelected(AdapterView adapterView, View view, int position, long id) {
                if (adapterView.getSelectedItemPosition() != 0) {
                    et_Pulse3.setText(adapterView.getSelectedItem().toString());
                    et_Pulse3.setVisibility(View.VISIBLE);
                    sp_Pulse3.setVisibility(View.GONE);
                }
            }

            public void onNothingSelected(AdapterView arg0) {
                et_Pulse3.setVisibility(View.VISIBLE);
                sp_Pulse3.setVisibility(View.GONE);
            }
        });

        et_GCS1_E.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                et_GCS1_E.setVisibility(View.GONE);
                sp_GCS1_E.setVisibility(View.VISIBLE);
                return true;
            }
        });

        sp_GCS1_E.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            public void onItemSelected(AdapterView adapterView, View view, int position, long id) {
                if (adapterView.getSelectedItemPosition() != 0) {
                    et_GCS1_E.setText(adapterView.getSelectedItem().toString());
                    et_GCS1_E.setVisibility(View.VISIBLE);
                    sp_GCS1_E.setVisibility(View.GONE);
                }
            }

            public void onNothingSelected(AdapterView arg0) {
                et_GCS1_E.setVisibility(View.VISIBLE);
                sp_GCS1_E.setVisibility(View.GONE);
            }
        });

        et_GCS1_V.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                et_GCS1_V.setVisibility(View.GONE);
                sp_GCS1_V.setVisibility(View.VISIBLE);
                return true;
            }
        });

        sp_GCS1_V.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            public void onItemSelected(AdapterView adapterView, View view, int position, long id) {
                if (adapterView.getSelectedItemPosition() != 0) {
                    et_GCS1_V.setText(adapterView.getSelectedItem().toString());
                    et_GCS1_V.setVisibility(View.VISIBLE);
                    sp_GCS1_V.setVisibility(View.GONE);
                }
            }

            public void onNothingSelected(AdapterView arg0) {
                et_GCS1_V.setVisibility(View.VISIBLE);
                sp_GCS1_V.setVisibility(View.GONE);
            }
        });

        et_GCS1_M.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                et_GCS1_M.setVisibility(View.GONE);
                sp_GCS1_M.setVisibility(View.VISIBLE);
                return true;
            }
        });

        sp_GCS1_M.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            public void onItemSelected(AdapterView adapterView, View view, int position, long id) {
                if (adapterView.getSelectedItemPosition() != 0) {
                    et_GCS1_M.setText(adapterView.getSelectedItem().toString());
                    et_GCS1_M.setVisibility(View.VISIBLE);
                    sp_GCS1_M.setVisibility(View.GONE);
                }
            }

            public void onNothingSelected(AdapterView arg0) {
                et_GCS1_M.setVisibility(View.VISIBLE);
                sp_GCS1_M.setVisibility(View.GONE);
            }
        });

        et_GCS2_E.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                et_GCS2_E.setVisibility(View.GONE);
                sp_GCS2_E.setVisibility(View.VISIBLE);
                return true;
            }
        });

        sp_GCS2_E.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            public void onItemSelected(AdapterView adapterView, View view, int position, long id) {
                if (adapterView.getSelectedItemPosition() != 0) {
                    et_GCS2_E.setText(adapterView.getSelectedItem().toString());
                    et_GCS2_E.setVisibility(View.VISIBLE);
                    sp_GCS2_E.setVisibility(View.GONE);
                }
            }

            public void onNothingSelected(AdapterView arg0) {
                et_GCS2_E.setVisibility(View.VISIBLE);
                sp_GCS2_E.setVisibility(View.GONE);
            }
        });

        et_GCS2_V.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                et_GCS2_V.setVisibility(View.GONE);
                sp_GCS2_V.setVisibility(View.VISIBLE);
                return true;
            }
        });

        sp_GCS2_V.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            public void onItemSelected(AdapterView adapterView, View view, int position, long id) {
                if (adapterView.getSelectedItemPosition() != 0) {
                    et_GCS2_V.setText(adapterView.getSelectedItem().toString());
                    et_GCS2_V.setVisibility(View.VISIBLE);
                    sp_GCS2_V.setVisibility(View.GONE);
                }
            }

            public void onNothingSelected(AdapterView arg0) {
                et_GCS2_V.setVisibility(View.VISIBLE);
                sp_GCS2_V.setVisibility(View.GONE);
            }
        });

        et_GCS2_M.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                et_GCS2_M.setVisibility(View.GONE);
                sp_GCS2_M.setVisibility(View.VISIBLE);
                return true;
            }
        });

        sp_GCS2_M.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            public void onItemSelected(AdapterView adapterView, View view, int position, long id) {
                if (adapterView.getSelectedItemPosition() != 0) {
                    et_GCS2_M.setText(adapterView.getSelectedItem().toString());
                    et_GCS2_M.setVisibility(View.VISIBLE);
                    sp_GCS2_M.setVisibility(View.GONE);
                }
            }

            public void onNothingSelected(AdapterView arg0) {
                et_GCS2_M.setVisibility(View.VISIBLE);
                sp_GCS2_M.setVisibility(View.GONE);
            }
        });

        et_GCS3_E.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                et_GCS3_E.setVisibility(View.GONE);
                sp_GCS3_E.setVisibility(View.VISIBLE);
                return true;
            }
        });

        sp_GCS3_E.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            public void onItemSelected(AdapterView adapterView, View view, int position, long id) {
                if (adapterView.getSelectedItemPosition() != 0) {
                    et_GCS3_E.setText(adapterView.getSelectedItem().toString());
                    et_GCS3_E.setVisibility(View.VISIBLE);
                    sp_GCS3_E.setVisibility(View.GONE);
                }
            }

            public void onNothingSelected(AdapterView arg0) {
                et_GCS3_E.setVisibility(View.VISIBLE);
                sp_GCS3_E.setVisibility(View.GONE);
            }
        });

        et_GCS3_V.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                et_GCS3_V.setVisibility(View.GONE);
                sp_GCS3_V.setVisibility(View.VISIBLE);
                return true;
            }
        });

        sp_GCS3_V.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            public void onItemSelected(AdapterView adapterView, View view, int position, long id) {
                if (adapterView.getSelectedItemPosition() != 0) {
                    et_GCS3_V.setText(adapterView.getSelectedItem().toString());
                    et_GCS3_V.setVisibility(View.VISIBLE);
                    sp_GCS3_V.setVisibility(View.GONE);
                }
            }

            public void onNothingSelected(AdapterView arg0) {
                et_GCS3_V.setVisibility(View.VISIBLE);
                sp_GCS3_V.setVisibility(View.GONE);
            }
        });

        et_GCS3_M.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                et_GCS3_M.setVisibility(View.GONE);
                sp_GCS3_M.setVisibility(View.VISIBLE);
                return true;
            }
        });

        sp_GCS3_M.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            public void onItemSelected(AdapterView adapterView, View view, int position, long id) {
                if (adapterView.getSelectedItemPosition() != 0) {
                    et_GCS3_M.setText(adapterView.getSelectedItem().toString());
                    et_GCS3_M.setVisibility(View.VISIBLE);
                    sp_GCS3_M.setVisibility(View.GONE);
                }
            }

            public void onNothingSelected(AdapterView arg0) {
                et_GCS3_M.setVisibility(View.VISIBLE);
                sp_GCS3_M.setVisibility(View.GONE);
            }
        });

        et_SBP1.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                et_SBP1.setVisibility(View.GONE);
                sp_SBP1.setVisibility(View.VISIBLE);
                return true;
            }
        });

        sp_SBP1.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            public void onItemSelected(AdapterView adapterView, View view, int position, long id) {
                if (adapterView.getSelectedItemPosition() != 0) {
                    et_SBP1.setText(adapterView.getSelectedItem().toString());
                    et_SBP1.setVisibility(View.VISIBLE);
                    sp_SBP1.setVisibility(View.GONE);
                }
            }

            public void onNothingSelected(AdapterView arg0) {
                et_SBP1.setVisibility(View.VISIBLE);
                sp_SBP1.setVisibility(View.GONE);
            }
        });

        et_SBP2.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                et_SBP2.setVisibility(View.GONE);
                sp_SBP2.setVisibility(View.VISIBLE);
                return true;
            }
        });

        sp_SBP2.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            public void onItemSelected(AdapterView adapterView, View view, int position, long id) {
                if (adapterView.getSelectedItemPosition() != 0) {
                    et_SBP2.setText(adapterView.getSelectedItem().toString());
                    et_SBP2.setVisibility(View.VISIBLE);
                    sp_SBP2.setVisibility(View.GONE);
                }
            }

            public void onNothingSelected(AdapterView arg0) {
                et_SBP2.setVisibility(View.VISIBLE);
                sp_SBP2.setVisibility(View.GONE);
            }
        });

        et_SBP3.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                et_SBP3.setVisibility(View.GONE);
                sp_SBP3.setVisibility(View.VISIBLE);
                return true;
            }
        });

        sp_SBP3.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            public void onItemSelected(AdapterView adapterView, View view, int position, long id) {
                if (adapterView.getSelectedItemPosition() != 0) {
                    et_SBP3.setText(adapterView.getSelectedItem().toString());
                    et_SBP3.setVisibility(View.VISIBLE);
                    sp_SBP3.setVisibility(View.GONE);
                }
            }

            public void onNothingSelected(AdapterView arg0) {
                et_SBP3.setVisibility(View.VISIBLE);
                sp_SBP3.setVisibility(View.GONE);
            }
        });

        et_DBP1.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                et_DBP1.setVisibility(View.GONE);
                sp_DBP1.setVisibility(View.VISIBLE);
                return true;
            }
        });

        sp_DBP1.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            public void onItemSelected(AdapterView adapterView, View view, int position, long id) {
                if (adapterView.getSelectedItemPosition() != 0) {
                    et_DBP1.setText(adapterView.getSelectedItem().toString());
                    et_DBP1.setVisibility(View.VISIBLE);
                    sp_DBP1.setVisibility(View.GONE);
                }
            }

            public void onNothingSelected(AdapterView arg0) {
                et_DBP1.setVisibility(View.VISIBLE);
                sp_DBP1.setVisibility(View.GONE);
            }
        });

        et_DBP2.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                et_DBP2.setVisibility(View.GONE);
                sp_DBP2.setVisibility(View.VISIBLE);
                return true;
            }
        });

        sp_DBP2.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            public void onItemSelected(AdapterView adapterView, View view, int position, long id) {
                if (adapterView.getSelectedItemPosition() != 0) {
                    et_DBP2.setText(adapterView.getSelectedItem().toString());
                    et_DBP2.setVisibility(View.VISIBLE);
                    sp_DBP2.setVisibility(View.GONE);
                }
            }

            public void onNothingSelected(AdapterView arg0) {
                et_DBP2.setVisibility(View.VISIBLE);
                sp_DBP2.setVisibility(View.GONE);
            }
        });

        et_DBP3.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                et_DBP3.setVisibility(View.GONE);
                sp_DBP3.setVisibility(View.VISIBLE);
                return true;
            }
        });

        sp_DBP3.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            public void onItemSelected(AdapterView adapterView, View view, int position, long id) {
                if (adapterView.getSelectedItemPosition() != 0) {
                    et_DBP3.setText(adapterView.getSelectedItem().toString());
                    et_DBP3.setVisibility(View.VISIBLE);
                    sp_DBP3.setVisibility(View.GONE);
                }
            }

            public void onNothingSelected(AdapterView arg0) {
                et_DBP3.setVisibility(View.VISIBLE);
                sp_DBP3.setVisibility(View.GONE);
            }
        });

        et_SpO21.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                et_SpO21.setVisibility(View.GONE);
                sp_SpO21.setVisibility(View.VISIBLE);
                return true;
            }
        });

        sp_SpO21.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            public void onItemSelected(AdapterView adapterView, View view, int position, long id) {
                if (adapterView.getSelectedItemPosition() != 0) {
                    et_SpO21.setText(adapterView.getSelectedItem().toString());
                    et_SpO21.setVisibility(View.VISIBLE);
                    sp_SpO21.setVisibility(View.GONE);
                }
            }

            public void onNothingSelected(AdapterView arg0) {
                et_SpO21.setVisibility(View.VISIBLE);
                sp_SpO21.setVisibility(View.GONE);
            }
        });

        et_SpO22.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                et_SpO22.setVisibility(View.GONE);
                sp_SpO22.setVisibility(View.VISIBLE);
                return true;
            }
        });

        sp_SpO22.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            public void onItemSelected(AdapterView adapterView, View view, int position, long id) {
                if (adapterView.getSelectedItemPosition() != 0) {
                    et_SpO22.setText(adapterView.getSelectedItem().toString());
                    et_SpO22.setVisibility(View.VISIBLE);
                    sp_SpO22.setVisibility(View.GONE);
                }
            }

            public void onNothingSelected(AdapterView arg0) {
                et_SpO22.setVisibility(View.VISIBLE);
                sp_SpO22.setVisibility(View.GONE);
            }
        });

        et_SpO23.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                et_SpO23.setVisibility(View.GONE);
                sp_SpO23.setVisibility(View.VISIBLE);
                return true;
            }
        });

        sp_SpO23.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            public void onItemSelected(AdapterView adapterView, View view, int position, long id) {
                if (adapterView.getSelectedItemPosition() != 0) {
                    et_SpO23.setText(adapterView.getSelectedItem().toString());
                    et_SpO23.setVisibility(View.VISIBLE);
                    sp_SpO23.setVisibility(View.GONE);
                }
            }

            public void onNothingSelected(AdapterView arg0) {
                et_SpO23.setVisibility(View.VISIBLE);
                sp_SpO23.setVisibility(View.GONE);
            }
        });

        et_Temperature1.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                et_Temperature1.setVisibility(View.GONE);
                sp_Temperature1.setVisibility(View.VISIBLE);
                return true;
            }
        });

        sp_Temperature1.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            public void onItemSelected(AdapterView adapterView, View view, int position, long id) {
                if (adapterView.getSelectedItemPosition() != 0) {
                    et_Temperature1.setText(adapterView.getSelectedItem().toString());
                    et_Temperature1.setVisibility(View.VISIBLE);
                    sp_Temperature1.setVisibility(View.GONE);
                }
            }

            public void onNothingSelected(AdapterView arg0) {
                et_Temperature1.setVisibility(View.VISIBLE);
                sp_Temperature1.setVisibility(View.GONE);
            }
        });

        et_Temperature1.setOnFocusChangeListener(new android.view.View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    EditText ET = (EditText) findViewById(v.getId());
                    String str = ET.getText().toString();
                    if (str.length() == 3) {
                        Pattern pattern = Pattern.compile("[0-9]*");
                        Matcher isNum = pattern.matcher(str);
                        if (isNum.matches()) {
                            ET.setText(str.substring(0, 2) + "." + str.substring(2, 3));
                        }
                    }
                }
            }
        });

        et_Temperature2.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                et_Temperature2.setVisibility(View.GONE);
                sp_Temperature2.setVisibility(View.VISIBLE);
                return true;
            }
        });

        sp_Temperature2.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            public void onItemSelected(AdapterView adapterView, View view, int position, long id) {
                if (adapterView.getSelectedItemPosition() != 0) {
                    et_Temperature2.setText(adapterView.getSelectedItem().toString());
                    et_Temperature2.setVisibility(View.VISIBLE);
                    sp_Temperature2.setVisibility(View.GONE);
                }
            }

            public void onNothingSelected(AdapterView arg0) {
                et_Temperature2.setVisibility(View.VISIBLE);
                sp_Temperature2.setVisibility(View.GONE);
            }
        });

        et_Temperature2.setOnFocusChangeListener(new android.view.View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    EditText ET = (EditText) findViewById(v.getId());
                    String str = ET.getText().toString();
                    if (str.length() == 3) {
                        Pattern pattern = Pattern.compile("[0-9]*");
                        Matcher isNum = pattern.matcher(str);
                        if (isNum.matches()) {
                            ET.setText(str.substring(0, 2) + "." + str.substring(2, 3));
                        }
                    }
                }
            }
        });

        et_Temperature3.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                et_Temperature3.setVisibility(View.GONE);
                sp_Temperature3.setVisibility(View.VISIBLE);
                return true;
            }
        });

        sp_Temperature3.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            public void onItemSelected(AdapterView adapterView, View view, int position, long id) {
                if (adapterView.getSelectedItemPosition() != 0) {
                    et_Temperature3.setText(adapterView.getSelectedItem().toString());
                    et_Temperature3.setVisibility(View.VISIBLE);
                    sp_Temperature3.setVisibility(View.GONE);
                }
            }

            public void onNothingSelected(AdapterView arg0) {
                et_Temperature3.setVisibility(View.VISIBLE);
                sp_Temperature3.setVisibility(View.GONE);
            }
        });

        et_Temperature3.setOnFocusChangeListener(new android.view.View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    EditText ET = (EditText) findViewById(v.getId());
                    String str = ET.getText().toString();
                    if (str.length() == 3) {
                        Pattern pattern = Pattern.compile("[0-9]*");
                        Matcher isNum = pattern.matcher(str);
                        if (isNum.matches()) {
                            ET.setText(str.substring(0, 2) + "." + str.substring(2, 3));
                        }
                    }
                }
            }
        });

        bt_ecg_write.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                SaveData();
                SaveCurrIndex();
              Intent  intent = new Intent(MainActivity.this,BleECG.class);
                intent.putExtra("DEVICE_ADDRESS", ECGWS12);
                startActivity(intent);
                unbindService(serviceConnection);
                searchService.stopSelf();    //Stop Service//Stop Service
                unregisterReceiver(receiver);
                finish();



            }
        });

        bt_ecg_read.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                SaveData();
                SaveCurrIndex();
             Intent   intent = new Intent(MainActivity.this, ECGImageView.class);


                startActivity(intent);
                unbindService(serviceConnection);
                searchService.stopSelf();    //Stop Service//Stop Service
                unregisterReceiver(receiver);
                finish();

            }
        });

    }

    /**
     * 參數帶入
     */
    //基本資料main_tab1
    private void main_tab1_Modify() {
        gv.Modify_Tab1 = false;

        tv_FNumber.setText(gv.FNumber);
        cb_Critical.setChecked(gv.Critical);
        et_Date.setText(gv.Date);
        et_AttendanceUnit.setText(gv.AttendanceUnit);
        rg_AcceptedUnit.clearCheck();
        if(rb_AcceptedUnit1.getText().toString().equals(gv.AcceptedUnit)){
            rb_AcceptedUnit1.setChecked(true);
        }
        else if(rb_AcceptedUnit2.getText().toString().equals(gv.AcceptedUnit)){
            rb_AcceptedUnit2.setChecked(true);
        }
        et_LocationHappen.setText(gv.LocationHappen);
        et_AssistanceUnit.setText(gv.AssistanceUnit);
        sp_HospitalAddress.setVisibility(View.VISIBLE);
        sp_UploadHospital.setVisibility(View.VISIBLE);
        et_HospitalAddress.setVisibility(View.GONE);
        sp_HospitalAddress.setSelection(gv.PHospitalAddress);
        if(sp_HospitalAddress.getSelectedItem().equals("其他")){
            sp_HospitalAddress.setVisibility(View.GONE);
            sp_UploadHospital.setVisibility(View.GONE);
            et_HospitalAddress.setVisibility(View.VISIBLE);
            et_HospitalAddress.setText(gv.HospitalAddress);
        }
        for (int i = 0; i < cb_HospitalAddress_Name.length; i++) {
            if (cb_HospitalAddress_Name[i].getText().toString().equals(gv.ReasonHospital)) {
                cb_HospitalAddress_Name[i].setChecked(true);
            }
            else{
                cb_HospitalAddress_Name[i].setChecked(false);
            }
        }
        for (int i = 0; i < cb_NoHospital_Name.length; i++) {
            if (cb_NoHospital_Name[i].getText().toString().equals(gv.ReasonHospital)) {
                cb_NoHospital_Name[i].setChecked(true);
            }
            else{
                cb_NoHospital_Name[i].setChecked(false);
            }
        }
        tv_MHospital.setText(gv.MHospital);
        et_Time1.setText(gv.Time1);
        et_Time2.setText(gv.Time2);
        et_Time3.setText(gv.Time3);
        et_Time4.setText(gv.Time4);
        et_Time5.setText(gv.Time5);
        et_Time6.setText(gv.Time6);
        et_Name.setText(gv.Name);
        cb_AgeM.setChecked(gv.AgeM);
        cb_AgeA.setChecked(gv.AgeA);
        et_Age.setText(gv.Age);
        for (int i = 0; i < cb_Sex_Name.length; i++) {
            if (cb_Sex_Name[i].getText().toString().equals(gv.Sex)) {
                cb_Sex_Name[i].setChecked(true);
            }
            else{
                cb_Sex_Name[i].setChecked(false);
            }
        }
        et_ID.setText(gv.ID);
        sp_Area.setSelection(gv.PArea);
        et_Address.setText(gv.Address);
        for (int i = 0; i < cb_Property_Name.length; i++) {
            if (cb_Property_Name[i].getText().toString().equals(gv.Property)) {
                cb_Property_Name[i].setChecked(true);
                if(i == 1){
                    ll_Custody_T.setVisibility(View.VISIBLE);
                    ll_Custody_F.setVisibility(View.GONE);
                }
            }
            else{
                cb_Property_Name[i].setChecked(false);
            }
        }
        sp_RCustody.setVisibility(View.VISIBLE);
        et_RCustody.setVisibility(View.GONE);
        sp_RCustody.setSelection(gv.PRCustody);
        if(sp_RCustody.getSelectedItem().equals("其他")) {
            et_RCustody.setText(gv.RCustody);
        }
        et_Custody.setText(gv.Custody);
        if(gv.I_SCustody != null){
            iv_SCustody.setImageBitmap(gv.I_SCustody);
            iv_SCustody.setVisibility(View.VISIBLE);
            tv_SCustody.setVisibility(View.GONE);
            ImageViewSize(iv_SCustody, 625, 250);
        }
        else{
            iv_SCustody.setVisibility(View.GONE);
            tv_SCustody.setVisibility(View.VISIBLE);
        }
    }

    //現場狀況main_tab2
    private void main_tab2_Modify() {
        gv.Modify_Tab2 = false;

        for (int i = 0; i < cb_Trauma_Switch_Name.length; i++) {
            cb_Trauma_Switch_Name[i].setChecked(false);
            if (!gv.Trauma_Switch.equals("")) {
                if (cb_Trauma_Switch_Name[i].getText().toString().equals(gv.Trauma_Switch)) {
                    cb_Trauma_Switch_Name[i].setChecked(true);
                }
            }
        }

        et_Trauma3.setText("");
        et_Trauma9.setText("");
        for (int i = 0; i < cb_Trauma_Name.length; i++) {
            cb_Trauma_Name[i].setChecked(false);
            if(!gv.Trauma[i].equals("")) {
                if (cb_Trauma_Name[i].getText().toString().equals(gv.Trauma[i])) {
                    cb_Trauma_Name[i].setChecked(true);
                } else if (gv.Trauma[i].contains("@")) {
                    String[] array = gv.Trauma[i].split("@");
                    cb_Trauma_Name[i].setChecked(true);
                    if(array.length > 1) {
                        switch (i) {
                            case 2:
                                et_Trauma3.setText(array[1]);
                                break;
                            case 8:
                                et_Trauma9.setText(array[1]);
                                break;
                        }
                    }
                }
            }
        }

        for (int i = 0; i < cb_Trauma1_Name.length; i++) {
            cb_Trauma1_Name[i].setChecked(false);
            if(!gv.Trauma1[i].equals("")) {
                if (cb_Trauma1_Name[i].getText().toString().equals(gv.Trauma1[i])) {
                    cb_Trauma1_Name[i].setChecked(true);
                }
            }
        }

        et_Traffic5.setText("");
        for (int i = 0; i < cb_Traffic_Name.length; i++) {
            cb_Traffic_Name[i].setChecked(false);
            if(!gv.Traffic[i].equals("")) {
                if (cb_Traffic_Name[i].getText().toString().equals(gv.Traffic[i])) {
                    cb_Traffic_Name[i].setChecked(true);
                }
                else if (gv.Traffic[i].contains("@")) {
                    String[] array = gv.Traffic[i].split("@");
                    cb_Traffic_Name[i].setChecked(true);
                    if(array.length > 1) {
                        switch (i) {
                            case 4:
                                et_Traffic5.setText(array[1]);
                                break;
                        }
                    }
                }
            }
        }

        et_NTrauma15.setText("");
        for (int i = 0; i < cb_NTrauma_Name.length; i++) {
            cb_NTrauma_Name[i].setChecked(false);
            if(!gv.NTrauma[i].equals("")) {
                if (cb_NTrauma_Name[i].getText().toString().equals(gv.NTrauma[i])) {
                    cb_NTrauma_Name[i].setChecked(true);
                }
                else if (gv.NTrauma[i].contains("@")) {
                    String[] array = gv.NTrauma[i].split("@");
                    cb_NTrauma_Name[i].setChecked(true);
                    if(array.length > 1) {
                        switch (i) {
                            case 14:
                                et_NTrauma15.setText(array[1]);
                                break;
                        }
                    }
                }
            }
        }

        for (int i = 0; i < cb_NTrauma6_Name.length; i++) {
            cb_NTrauma6_Name[i].setChecked(false);
            if(!gv.NTrauma6[i].equals("")) {
                if (cb_NTrauma6_Name[i].getText().toString().equals(gv.NTrauma6[i])) {
                    cb_NTrauma6_Name[i].setChecked(true);
                }
            }
        }

        for (int i = 0; i < cb_Witnesses_Name.length; i++) {
            cb_Witnesses_Name[i].setChecked(false);
            if(!gv.Witnesses.equals("")) {
                if (cb_Witnesses_Name[i].getText().toString().equals(gv.Witnesses)) {
                    cb_Witnesses_Name[i].setChecked(true);
                }
            }
        }

        for (int i = 0; i < cb_CPR_Name.length; i++) {
            cb_CPR_Name[i].setChecked(false);
            if(!gv.CPR.equals("")) {
                if (cb_CPR_Name[i].getText().toString().equals(gv.CPR)) {
                    cb_CPR_Name[i].setChecked(true);
                }
            }
        }

        for (int i = 0; i < cb_PAD_Name.length; i++) {
            cb_PAD_Name[i].setChecked(false);
            if(!gv.PAD.equals("")) {
                if (cb_PAD_Name[i].getText().toString().equals(gv.PAD)) {
                    cb_PAD_Name[i].setChecked(true);
                }
            }
        }

        et_ROSC2.setText("");
        for (int i = 0; i < cb_ROSC_Name.length; i++) {
            cb_ROSC_Name[i].setChecked(false);
            if(!gv.ROSC.equals("")) {
                if (cb_ROSC_Name[i].getText().toString().equals(gv.ROSC)) {
                    cb_ROSC_Name[i].setChecked(true);
                } else if (gv.ROSC.contains("@")) {
                    String[] array = gv.ROSC.split("@");
                    cb_ROSC_Name[i].setChecked(true);
                    if(array.length > 1) {
                        switch (i) {
                            case 1:
                                et_ROSC2.setText(array[1]);
                                break;
                        }
                    }
                }
            }
        }

        for (int i = 0; i < cb_PSpecies_Name.length; i++) {
            cb_PSpecies_Name[i].setChecked(false);
            if(!gv.PSpecies.equals("")) {
                if (cb_PSpecies_Name[i].getText().toString().equals(gv.PSpecies)) {
                    cb_PSpecies_Name[i].setChecked(true);
                }
            }
        }

        for (int i = 0; i < cb_Stroke_Name.length; i++) {
            cb_Stroke_Name[i].setChecked(false);
            if(!gv.Stroke.equals("")) {
                if (cb_Stroke_Name[i].getText().toString().equals(gv.Stroke)) {
                    cb_Stroke_Name[i].setChecked(true);
                }
            }
        }

        et_LTime.setText(gv.LTime);

        cb_Smile.setChecked(gv.Smile);

        cb_LArm.setChecked(gv.LArm);

        cb_Speech.setChecked(gv.Speech);
    }

    //急救處置main_tab3
    private void main_tab3_Modify() {
        gv.Modify_Tab3 = false;

        et_Breathing5.setText("");
        et_Breathing6.setText("");
        et_Breathing7.setText("");
        et_Breathing10.setText("");
        for (int i = 0; i < cb_Breathing_Name.length; i++) {
            cb_Breathing_Name[i].setChecked(false);
            if(!gv.Breathing[i].equals("")) {
                if (cb_Breathing_Name[i].getText().toString().equals(gv.Breathing[i])) {
                    cb_Breathing_Name[i].setChecked(true);
                } else if (gv.Breathing[i].contains("@")) {
                    String[] array = gv.Breathing[i].split("@");
                    cb_Breathing_Name[i].setChecked(true);
                    if(array.length > 1) {
                        switch (i) {
                            case 4:
                                et_Breathing5.setText(array[1]);
                                break;
                            case 5:
                                et_Breathing6.setText(array[1]);
                                break;
                            case 6:
                                et_Breathing7.setText(array[1]);
                            case 9:
                                et_Breathing10.setText(array[1]);
                                break;
                        }
                    }
                }
            }
        }

        et_TDisposal8.setText("");
        for (int i = 0; i < cb_TDisposal_Name.length; i++) {
            cb_TDisposal_Name[i].setChecked(false);
            if(!gv.TDisposal[i].equals("")) {
                if (cb_TDisposal_Name[i].getText().toString().equals(gv.TDisposal[i])) {
                    cb_TDisposal_Name[i].setChecked(true);
                } else if (gv.TDisposal[i].contains("@")) {
                    String[] array = gv.TDisposal[i].split("@");
                    cb_TDisposal_Name[i].setChecked(true);
                    if(array.length > 1) {
                        switch (i) {
                            case 7:
                                et_TDisposal8.setText(array[1]);
                                break;
                        }
                    }
                }
            }
        }

        et_CPRDisposal1.setText("");
        et_CPRDisposal1_Time.setText("");
        et_CPRDisposal3_Time.setText("");
        for (int i = 0; i < cb_CPRDisposal_Name.length; i++) {
            cb_CPRDisposal_Name[i].setChecked(false);
            if(!gv.CPRDisposal[i].equals("")) {
                if (cb_CPRDisposal_Name[i].getText().toString().equals(gv.CPRDisposal[i])) {
                    cb_CPRDisposal_Name[i].setChecked(true);
                } else if (gv.CPRDisposal[i].contains("@")) {
                    String[] array = gv.CPRDisposal[i].split("@");
                    cb_CPRDisposal_Name[i].setChecked(true);
                    if(array.length > 1) {
                        switch (i) {
                            case 0:
                                et_CPRDisposal1.setText(array[1]);
                                et_CPRDisposal1_Time.setText(array.length > 2 ?array[2]:"");
                                break;
                            case 2:
                                et_CPRDisposal3_Time.setText(array[1]);
                                break;
                        }
                    }
                }
            }
        }

        et_CPRDisposal3_1.setText("");
        for (int i = 0; i < cb_CPRDisposal3_Name.length; i++) {
            cb_CPRDisposal3_Name[i].setChecked(false);
            if(!gv.CPRDisposal3[i].equals("")) {
                if (cb_CPRDisposal3_Name[i].getText().toString().equals(gv.CPRDisposal3[i])) {
                    cb_CPRDisposal3_Name[i].setChecked(true);
                } else if (gv.CPRDisposal3[i].contains("@")) {
                    String[] array = gv.CPRDisposal3[i].split("@");
                    cb_CPRDisposal3_Name[i].setChecked(true);
                    if(array.length > 1) {
                        switch (i) {
                            case 0:
                                et_CPRDisposal3_1.setText(array[1]);
                                break;
                        }
                    }
                }
            }
        }

        et_DDisposal1.setText("");
        et_DDisposal3.setText("");
        et_DDisposal4.setText("");
        for (int i = 0; i < cb_DDisposal_Name.length; i++) {
            cb_DDisposal_Name[i].setChecked(false);
            if(!gv.DDisposal[i].equals("")) {
                if (cb_DDisposal_Name[i].getText().toString().equals(gv.DDisposal[i])) {
                    cb_DDisposal_Name[i].setChecked(true);
                } else if (gv.DDisposal[i].contains("@")) {
                    String[] array = gv.DDisposal[i].split("@");
                    cb_DDisposal_Name[i].setChecked(true);
                    if(array.length > 1) {
                        if (array.length > 1) {
                            switch (i) {
                                case 0:
                                    et_DDisposal1.setText(array[1]);
                                    break;
                                case 2:
                                    et_DDisposal3.setText(array[1]);
                                    break;
                                case 3:
                                    et_DDisposal4.setText(array[1]);
                                    break;
                            }
                        }
                    }
                }
            }
        }

        et_DDisposal1_1.setText("");
        et_DDisposal1_2.setText("");
        et_DDisposal1_3.setText("");
        for (int i = 0; i < cb_DDisposal1_Name.length; i++) {
            cb_DDisposal1_Name[i].setChecked(false);
            if(!gv.DDisposal1[i].equals("")) {
                if (cb_DDisposal1_Name[i].getText().toString().equals(gv.DDisposal1[i])) {
                    cb_DDisposal1_Name[i].setChecked(true);
                } else if (gv.DDisposal1[i].contains("@")) {
                    String[] array = gv.DDisposal1[i].split("@");
                    cb_DDisposal1_Name[i].setChecked(true);
                    if(array.length > 1) {
                        switch (i) {
                            case 0:
                                et_DDisposal1_1.setText(array[1]);
                                break;
                            case 1:
                                et_DDisposal1_2.setText(array[1]);
                                break;
                            case 2:
                                et_DDisposal1_3.setText(array[1]);
                                break;
                        }
                    }
                }
            }
        }

        et_ODisposal4.setText("");
        et_ODisposal5.setText("");
        for (int i = 0; i < cb_ODisposal_Name.length; i++) {
            cb_ODisposal_Name[i].setChecked(false);
            if(!gv.ODisposal[i].equals("")) {
                if (cb_ODisposal_Name[i].getText().toString().equals(gv.ODisposal[i])) {
                    cb_ODisposal_Name[i].setChecked(true);
                } else if (gv.ODisposal[i].contains("@")) {
                    String[] array = gv.ODisposal[i].split("@");
                    cb_ODisposal_Name[i].setChecked(true);
                    if(array.length > 1) {
                        switch (i) {
                            case 3:
                                et_ODisposal4.setText(array[1]);
                                break;
                            case 4:
                                et_ODisposal5.setText(array[1]);
                                break;
                        }
                    }
                }
            }
        }

        et_ALSDisposal1_1.setText("");
        et_ALSDisposal1_2.setText("");
        et_ALSDisposal2_1.setText("");
        et_ALSDisposal2_2.setText("");
        for (int i = 0; i < cb_ALSDisposal_Name.length; i++) {
            cb_ALSDisposal_Name[i].setChecked(false);
            if(!gv.ALSDisposal[i].equals("")) {
                if (cb_ALSDisposal_Name[i].getText().toString().equals(gv.ALSDisposal[i])) {
                    cb_ALSDisposal_Name[i].setChecked(true);
                } else if (gv.ALSDisposal[i].contains("@")) {
                    String[] array = gv.ALSDisposal[i].split("@");
                    cb_ALSDisposal_Name[i].setChecked(true);
                    if(array.length > 2) {
                        switch (i) {
                            case 0:
                                et_ALSDisposal1_1.setText(array[1]);
                                et_ALSDisposal1_2.setText(array[2]);
                                break;
                            case 1:
                                et_ALSDisposal2_1.setText(array[1]);
                                et_ALSDisposal2_2.setText(array[2]);
                                break;
                        }
                    }
                    else if(array.length > 1){
                        switch (i) {
                            case 0:
                                et_ALSDisposal1_1.setText(array[1]);
                                break;
                            case 1:
                                et_ALSDisposal2_1.setText(array[1]);
                                break;
                        }
                    }
                }
            }
        }

        et_Posture.setText("");
        sp_Posture.setVisibility(View.VISIBLE);
        et_Posture.setVisibility(View.GONE);
        sp_Posture.setSelection(gv.PPosture);
        if(sp_Posture.getSelectedItem().equals("其他")) {
            sp_Posture.setVisibility(View.GONE);
            et_Posture.setVisibility(View.VISIBLE);
            et_Posture.setText(gv.Posture);
        }

        et_DrugUseTime1.setText(gv.DrugUseTime1);
        sp_DrugUseName1.setSelection(gv.PDrugUseName1);
        et_DrugUser1.setText(gv.DrugUser1);
        et_DrugUseTime2.setText(gv.DrugUseTime2);
        sp_DrugUseName2.setSelection(gv.PDrugUseName2);
        et_DrugUser2.setText(gv.DrugUser2);
        et_DrugUseTime3.setText(gv.DrugUseTime3);
        sp_DrugUseName3.setSelection(gv.PDrugUseName3);
        et_DrugUser3.setText(gv.DrugUser3);
        et_DrugUseTime4.setText(gv.DrugUseTime4);
        sp_DrugUseName4.setSelection(gv.PDrugUseName4);
        et_DrugUser4.setText(gv.DrugUser4);
    }

    //病患相關main_tab4
    private void main_tab4_Modify() {
        gv.Modify_Tab4 = false;

        et_Q1.setText(gv.Q1);
        et_Q2.setText(gv.Q2);
        et_Q3.setText(gv.Q3);
        et_Q4.setText(gv.Q4);
        et_Q5.setText(gv.Q5);

        et_MHistory12.setText("");
        for (int i = 0; i < cb_MHistory_Name.length; i++) {
            cb_MHistory_Name[i].setChecked(false);
            if(!gv.MHistory[i].equals("")) {
                if (cb_MHistory_Name[i].getText().toString().equals(gv.MHistory[i])) {
                    cb_MHistory_Name[i].setChecked(true);
                } else if (gv.MHistory[i].contains("@")) {
                    String[] array = gv.MHistory[i].split("@");
                    cb_MHistory_Name[i].setChecked(true);
                    if(array.length > 1) {
                        et_MHistory12.setText(array[1]);
                    }
                }
            }
        }

        et_Allergies1.setText("");
        et_Allergies2.setText("");
        et_Allergies3.setText("");
        for (int i = 0; i < cb_Allergies_Name.length; i++) {
            cb_Allergies_Name[i].setChecked(false);
            if(!gv.Allergies[i].equals("")) {
                if (cb_Allergies_Name[i].getText().toString().equals(gv.Allergies[i])) {
                    cb_Allergies_Name[i].setChecked(true);
                } else if (gv.Allergies[i].contains("@")) {
                    String[] array = gv.Allergies[i].split("@");
                    cb_Allergies_Name[i].setChecked(true);
                    if(array.length > 1) {
                        switch (i) {
                            case 0:
                                et_Allergies1.setText(array[1]);
                                break;
                            case 1:
                                et_Allergies2.setText(array[1]);
                                break;
                            case 2:
                                et_Allergies3.setText(array[1]);
                                break;
                        }
                    }
                }
            }
        }

        if(gv.Switch_People != null) {
            Resources iv_Resources = MainActivity.this.getResources();
            Drawable iv_Drawable;
            for (int i = 0; i < gv.Switch_People.length; i++) {
                if (gv.Switch_People[i]) {
                    iv_Drawable = iv_Resources.getDrawable(T_People_ID[i]);
                } else {
                    iv_Drawable = iv_Resources.getDrawable(F_People_ID[i]);
                }
                iv_People_Name[i].setBackground(iv_Drawable);
            }
        }

        et_People.setText(gv.People);
    }

    //生命徵象main_tab5
    private void main_tab5_Modify() {
        gv.Modify_Tab5 = false;
        //生命徵象1
        et_LifeTime1.setText(gv.LifeTime1);
        sp_Consciousness1.setSelection(gv.PConsciousness1);
        et_LifeBreathing1.setText(gv.LifeBreathing1);
        et_Pulse1.setText(gv.Pulse1);
        if(!gv.GCS1_E.equals("")) {
            sp_GCS1_E.setVisibility(View.GONE);
            et_GCS1_E.setVisibility(View.VISIBLE);
            et_GCS1_E.setText(gv.GCS1_E);
        }
        else{
            sp_GCS1_E.setVisibility(View.VISIBLE);
            et_GCS1_E.setVisibility(View.GONE);
            et_GCS1_E.setText("");
        }
        if(!gv.GCS1_V.equals("")) {
            sp_GCS1_V.setVisibility(View.GONE);
            et_GCS1_V.setVisibility(View.VISIBLE);
            et_GCS1_V.setText(gv.GCS1_V);
        }
        else{
            sp_GCS1_V.setVisibility(View.VISIBLE);
            et_GCS1_V.setVisibility(View.GONE);
            et_GCS1_V.setText("");
        }
        if(!gv.GCS1_M.equals("")) {
            sp_GCS1_M.setVisibility(View.GONE);
            et_GCS1_M.setVisibility(View.VISIBLE);
            et_GCS1_M.setText(gv.GCS1_M);
        }
        else{
            sp_GCS1_M.setVisibility(View.VISIBLE);
            et_GCS1_M.setVisibility(View.GONE);
            et_GCS1_M.setText("");
        }
        if(gv.PBPressure1_A == 0){
            rg_BP1.check(R.id.rb_BP1_1);
        }
        else{
            rg_BP1.check(R.id.rb_BP1_2);
        }
        et_SBP1.setText(gv.SBP1);
        et_DBP1.setText(gv.DBP1);
        sp_BPressure1_A.setSelection(gv.PBPressure1_A);
        et_SpO21.setText(gv.SpO21);
        et_Temperature1.setText(gv.Temperature1);

        //生命徵象2
        et_LifeTime2.setText(gv.LifeTime2);
        sp_Consciousness2.setSelection(gv.PConsciousness2);
        et_LifeBreathing2.setText(gv.LifeBreathing2);
        et_Pulse2.setText(gv.Pulse2);
        if(!gv.GCS2_E.equals("")) {
            sp_GCS2_E.setVisibility(View.GONE);
            et_GCS2_E.setVisibility(View.VISIBLE);
            et_GCS2_E.setText(gv.GCS2_E);
        }
        else{
            sp_GCS2_E.setVisibility(View.VISIBLE);
            et_GCS2_E.setVisibility(View.GONE);
            et_GCS2_E.setText("");
        }
        if(!gv.GCS2_V.equals("")) {
            sp_GCS2_V.setVisibility(View.GONE);
            et_GCS2_V.setVisibility(View.VISIBLE);
            et_GCS2_V.setText(gv.GCS2_V);
        }
        else{
            sp_GCS2_V.setVisibility(View.VISIBLE);
            et_GCS2_V.setVisibility(View.GONE);
            et_GCS2_V.setText("");
        }
        if(!gv.GCS2_M.equals("")) {
            sp_GCS2_M.setVisibility(View.GONE);
            et_GCS2_M.setVisibility(View.VISIBLE);
            et_GCS2_M.setText(gv.GCS2_M);
        }
        else{
            sp_GCS2_M.setVisibility(View.VISIBLE);
            et_GCS2_M.setVisibility(View.GONE);
            et_GCS2_M.setText("");
        }
        if(gv.PBPressure2_A == 0){
            rg_BP2.check(R.id.rb_BP2_1);
        }
        else{
            rg_BP2.check(R.id.rb_BP2_2);
        }
        et_SBP2.setText(gv.SBP2);
        et_DBP2.setText(gv.DBP2);
        sp_BPressure2_A.setSelection(gv.PBPressure2_A);
        et_SpO22.setText(gv.SpO22);
        et_Temperature2.setText(gv.Temperature2);

        //生命徵象3
        sp_Consciousness3.setSelection(gv.PConsciousness3);
        et_LifeBreathing3.setText(gv.LifeBreathing3);
        et_Pulse3.setText(gv.Pulse3);
        if(!gv.GCS3_E.equals("")) {
            sp_GCS3_E.setVisibility(View.GONE);
            et_GCS3_E.setVisibility(View.VISIBLE);
            et_GCS3_E.setText(gv.GCS3_E);
        }
        else{
            sp_GCS3_E.setVisibility(View.VISIBLE);
            et_GCS3_E.setVisibility(View.GONE);
            et_GCS3_E.setText("");
        }
        if(!gv.GCS3_V.equals("")) {
            sp_GCS3_V.setVisibility(View.GONE);
            et_GCS3_V.setVisibility(View.VISIBLE);
            et_GCS3_V.setText(gv.GCS3_V);
        }
        else{
            sp_GCS3_V.setVisibility(View.VISIBLE);
            et_GCS3_V.setVisibility(View.GONE);
            et_GCS3_V.setText("");
        }
        if(!gv.GCS3_M.equals("")) {
            sp_GCS3_M.setVisibility(View.GONE);
            et_GCS3_M.setVisibility(View.VISIBLE);
            et_GCS3_M.setText(gv.GCS3_M);
        }
        else{
            sp_GCS3_M.setVisibility(View.VISIBLE);
            et_GCS3_M.setVisibility(View.GONE);
            et_GCS3_M.setText("");
        }
        if(gv.PBPressure3_A == 0){
            rg_BP3.check(R.id.rb_BP3_1);
        }
        else{
            rg_BP3.check(R.id.rb_BP3_2);
        }
        et_SBP3.setText(gv.SBP3);
        et_DBP3.setText(gv.DBP3);
        sp_BPressure3_A.setSelection(gv.PBPressure3_A);
        et_SpO23.setText(gv.SpO23);
        et_Temperature3.setText(gv.Temperature3);
    }

    /**
     *  參數儲存
     */
    //基本資料main_tab1
    private void main_tab1_Save() {
        gv.FNumber = tv_FNumber.getText().toString();
        gv.Critical = cb_Critical.isChecked();
        gv.Date = et_Date.getText().toString();
        gv.AttendanceUnit = et_AttendanceUnit.getText().toString();
        if(rg_AcceptedUnit.getCheckedRadioButtonId()!=-1){
            int id= rg_AcceptedUnit.getCheckedRadioButtonId();
            View radioButton = rg_AcceptedUnit.findViewById(id);
            int radioId = rg_AcceptedUnit.indexOfChild(radioButton);
            RadioButton btn = (RadioButton) rg_AcceptedUnit.getChildAt(radioId);
            gv.AcceptedUnit = btn.getText().toString();
        }
        gv.LocationHappen = et_LocationHappen.getText().toString();
        gv.AssistanceUnit = et_AssistanceUnit.getText().toString();
        gv.PHospitalAddress = sp_HospitalAddress.getSelectedItemPosition();
        gv.HospitalAddress = "";
        if(sp_HospitalAddress.getSelectedItemPosition() != 0) {
            if(sp_HospitalAddress.getSelectedItem().equals("其他")){
                gv.HospitalAddress = et_HospitalAddress.getText().toString();
            } else {
                gv.HospitalAddress = sp_HospitalAddress.getSelectedItem().toString();
            }
        }
        gv.ReasonHospital = "";
        for(int i = 0;i< cb_HospitalAddress_Name.length;i++){
            if(cb_HospitalAddress_Name[i].isChecked()) {
                gv.ReasonHospital = cb_HospitalAddress_Name[i].getText().toString();
            }
        }
        for(int i = 0;i< cb_NoHospital_Name.length;i++){
            if(cb_NoHospital_Name[i].isChecked()) {
                gv.ReasonHospital = cb_NoHospital_Name[i].getText().toString();
            }
        }
        gv.MHospital = tv_MHospital.getText().toString();
        gv.Time1 = TimeFocus(et_Time1.getText().toString());
        gv.Time2 = TimeFocus(et_Time2.getText().toString());
        gv.Time3 = TimeFocus(et_Time3.getText().toString());
        gv.Time4 = TimeFocus(et_Time4.getText().toString());
        gv.Time5 = TimeFocus(et_Time5.getText().toString());
        gv.Time6 = TimeFocus(et_Time6.getText().toString());
        gv.Name = et_Name.getText().toString();
        gv.AgeM = cb_AgeM.isChecked();
        gv.AgeA = cb_AgeA.isChecked();
        gv.Age = et_Age.getText().toString();
        gv.Sex = "";
        for(int i = 0;i< cb_Sex_Name.length;i++){
            if(cb_Sex_Name[i].isChecked()) {
                gv.Sex = cb_Sex_Name[i].getText().toString();
            }
        }
        gv.ID = et_ID.getText().toString();
        gv.PArea = sp_Area.getSelectedItemPosition();
        gv.Area = "";
        if(sp_Area.getSelectedItemPosition() != 0) {
            gv.Area = sp_Area.getSelectedItem().toString();
        }
        gv.Address = et_Address.getText().toString();
        gv.Property = "";
        for(int i = 0;i< cb_Property_Name.length;i++){
            if(cb_Property_Name[i].isChecked()) {
                gv.Property = cb_Property_Name[i].getText().toString();
            }
        }
        gv.PRCustody = sp_RCustody.getSelectedItemPosition();
        gv.RCustody = "";
        if (sp_RCustody.getSelectedItemPosition() != 0) {
            if (sp_RCustody.getSelectedItem().equals("其他")) {
                gv.RCustody = et_RCustody.getText().toString();
            } else {
                gv.RCustody = sp_RCustody.getSelectedItem().toString();
            }
        }
        gv.Custody = et_Custody.getText().toString();
    }

    //現場狀況main_tab2
    private void main_tab2_Save() {
        int TraumaTF = -1;
        boolean OHCATF = false;

        gv.Trauma_Switch = "";
        for (int i = 0; i < cb_Trauma_Switch_Name.length; i++) {
            if (cb_Trauma_Switch_Name[i].isChecked()) {
                gv.Trauma_Switch = cb_Trauma_Switch_Name[i].getText().toString();
                TraumaTF = i;
            }
        }
        switch (TraumaTF) {
            case 0:
                for (int j = 0; j < cb_Trauma1_Name.length; j++) {
                    gv.Trauma1[j] = "";
                }
                for (int i = 0; i < cb_Trauma_Name.length; i++) {
                    if (cb_Trauma_Name[i].isChecked()) {
                        gv.Trauma[i] = cb_Trauma_Name[i].getText().toString();
                        switch (i){
                            case 0:
                                for (int j = 0; j < cb_Trauma1_Name.length; j++) {
                                    if (cb_Trauma1_Name[j].isChecked()) {
                                        gv.Trauma1[j] = cb_Trauma1_Name[j].getText().toString();
                                    }
                                    else {
                                        gv.Trauma1[j] = "";
                                    }
                                }
                                break;
                            case 2:
                                gv.Trauma[i] += "@" + et_Trauma3.getText().toString();
                                break;
                            case 7:
                                OHCATF = true;
                                break;
                            case 8:
                                gv.Trauma[i] += "@" + et_Trauma9.getText().toString();
                                break;
                        }
                    }
                    else {
                        gv.Trauma[i] = "";
                    }
                }

                if(cb_Trauma_Name[1].isChecked()) {
                    for (int i = 0; i < cb_Traffic_Name.length; i++) {
                        if (cb_Traffic_Name[i].isChecked()) {
                            gv.Traffic[i] = cb_Traffic_Name[i].getText().toString();
                            switch (i) {
                                case 4:
                                    gv.Traffic[i] += "@" + et_Traffic5.getText().toString();
                                    break;
                            }
                        } else {
                            gv.Traffic[i] = "";
                        }
                    }
                }
                else {
                    for (int i = 0; i < cb_Traffic_Name.length; i++) {
                        gv.Traffic[i] = "";
                    }
                }

                for (int i = 0; i < cb_NTrauma_Name.length; i++) {
                    gv.NTrauma[i] = "";
                }
                for (int j = 0; j < cb_NTrauma6_Name.length; j++) {
                    gv.NTrauma6[j] = "";
                }
                break;
            case 1:
                for (int j = 0; j < cb_NTrauma6_Name.length; j++) {
                    gv.NTrauma6[j] = "";
                }

                for (int i = 0; i < cb_NTrauma_Name.length; i++) {
                    if (cb_NTrauma_Name[i].isChecked()) {
                        gv.NTrauma[i] = cb_NTrauma_Name[i].getText().toString();
                        switch (i){
                            case 5:
                                for (int j = 0; j < cb_NTrauma6_Name.length; j++) {
                                    if (cb_NTrauma6_Name[j].isChecked()) {
                                        gv.NTrauma6[j] = cb_NTrauma6_Name[j].getText().toString();
                                    }
                                    else {
                                        gv.NTrauma6[j] = "";
                                    }
                                }
                                break;
                            case 13:
                                OHCATF = true;
                                break;
                            case 14:
                                gv.NTrauma[i] += "@" + et_NTrauma15.getText().toString();
                                break;
                        }
                    }
                    else {
                        gv.NTrauma[i] = "";
                    }
                }

                for (int i = 0; i < cb_Trauma_Name.length; i++) {
                    gv.Trauma[i] = "";
                }
                for (int j = 0; j < cb_Trauma1_Name.length; j++) {
                    gv.Trauma1[j] = "";
                }
                for (int i = 0; i < cb_Traffic_Name.length; i++) {
                    gv.Traffic[i] = "";
                }
                break;
            default:
                for (int i = 0; i < cb_Trauma_Name.length; i++) {
                    gv.Trauma[i] = "";
                }
                for (int j = 0; j < cb_Trauma1_Name.length; j++) {
                    gv.Trauma1[j] = "";
                }
                for (int i = 0; i < cb_Traffic_Name.length; i++) {
                    gv.Traffic[i] = "";
                }

                for (int i = 0; i < cb_NTrauma_Name.length; i++) {
                    gv.NTrauma[i] = "";
                }
                for (int j = 0; j < cb_NTrauma6_Name.length; j++) {
                    gv.NTrauma6[j] = "";
                }
                break;
        }

        if(OHCATF){
            for (int i = 0; i < cb_Witnesses_Name.length; i++) {
                if (cb_Witnesses_Name[i].isChecked()) {
                    gv.Witnesses = cb_Witnesses_Name[i].getText().toString();
                }
            }

            for (int i = 0; i < cb_CPR_Name.length; i++) {
                if (cb_CPR_Name[i].isChecked()) {
                    gv.CPR = cb_CPR_Name[i].getText().toString();
                }
            }

            for (int i = 0; i < cb_PAD_Name.length; i++) {
                if (cb_PAD_Name[i].isChecked()) {
                    gv.PAD = cb_PAD_Name[i].getText().toString();
                }
            }

            for (int i = 0; i < cb_ROSC_Name.length; i++) {
                if (cb_ROSC_Name[i].isChecked()) {
                    gv.ROSC = cb_ROSC_Name[i].getText().toString();
                    switch (i){
                        case 1:
                            gv.ROSC += "@" + TimeFocus(et_ROSC2.getText().toString());
                            break;
                    }
                }
            }

            for (int i = 0; i < cb_PSpecies_Name.length; i++) {
                if (cb_PSpecies_Name[i].isChecked()) {
                    gv.PSpecies = cb_PSpecies_Name[i].getText().toString();
                }
            }
        }
        else {
            gv.Witnesses = "";
            gv.CPR = "";
            gv.PAD = "";
            gv.ROSC = "";
            gv.PSpecies = "";
        }

        gv.Stroke = "";
        for (int i = 0; i < cb_Stroke_Name.length; i++) {
            if (cb_Stroke_Name[i].isChecked()) {
                gv.Stroke = cb_Stroke_Name[i].getText().toString();
            }
        }

        gv.LTime = TimeFocus(et_LTime.getText().toString());

        gv.Smile = false;
        if (cb_Smile.isChecked()) {
            gv.Smile = true;
        }

        gv.LArm = false;
        if (cb_LArm.isChecked()) {
            gv.LArm = true;
        }

        gv.Speech = false;
        if (cb_Speech.isChecked()) {
            gv.Speech = true;
        }
    }

    //急救處置main_tab3
    private void main_tab3_Save() {
        for (int i = 0; i < cb_Breathing_Name.length; i++) {
            if (cb_Breathing_Name[i].isChecked()) {
                gv.Breathing[i] = cb_Breathing_Name[i].getText().toString();
                switch (i){
                    case 4:
                        gv.Breathing[i] += "@" + et_Breathing5.getText().toString();
                        break;
                    case 5:
                        gv.Breathing[i] += "@" + et_Breathing6.getText().toString();
                        break;
                    case 6:
                        gv.Breathing[i] += "@" + et_Breathing7.getText().toString();
                        break;
                    case 9:
                        gv.Breathing[i] += "@" + et_Breathing10.getText().toString();
                        break;
                }
            }
            else {
                gv.Breathing[i] = "";
            }
        }

        for (int i = 0; i < cb_TDisposal_Name.length; i++) {
            if (cb_TDisposal_Name[i].isChecked()) {
                gv.TDisposal[i] = cb_TDisposal_Name[i].getText().toString();
                switch (i){
                    case 7:
                        gv.TDisposal[i] += "@" + et_TDisposal8.getText().toString();
                        break;
                }
            }
            else {
                gv.TDisposal[i] = "";
            }
        }

        for (int i = 0; i < cb_CPRDisposal_Name.length; i++) {
            if (cb_CPRDisposal_Name[i].isChecked()) {
                gv.CPRDisposal[i] = cb_CPRDisposal_Name[i].getText().toString();
                switch (i){
                    case 0:
                        gv.CPRDisposal[i] += "@" + et_CPRDisposal1.getText().toString() + "@" + TimeFocus(et_CPRDisposal1_Time.getText().toString());
                        break;
                    case 2:
                        gv.CPRDisposal[i] += "@" + TimeFocus(et_CPRDisposal3_Time.getText().toString());
                        for (int j = 0; j < cb_CPRDisposal3_Name.length; j++) {
                            if (cb_CPRDisposal3_Name[j].isChecked()) {
                                gv.CPRDisposal3[j] = cb_CPRDisposal3_Name[j].getText().toString();
                                switch (j){
                                    case 0:
                                        gv.CPRDisposal3[j] += "@" + et_CPRDisposal3_1.getText().toString();
                                        break;
                                }
                            }
                            else {
                                gv.CPRDisposal3[j] = "";
                            }
                        }
                        break;
                }
            }
            else {
                gv.CPRDisposal[i] = "";
                if(i == 1){
                    gv.CPRDisposal3 = new String[] { "", "" };
                }
            }
        }

        for (int i = 0; i < cb_DDisposal_Name.length; i++) {
            if (cb_DDisposal_Name[i].isChecked()) {
                gv.DDisposal[i] = cb_DDisposal_Name[i].getText().toString();
                switch (i){
                    case 0:
                        gv.DDisposal[i] += "@" + et_DDisposal1.getText().toString();
                        for (int j = 0; j < cb_DDisposal1_Name.length; j++) {
                            if (cb_DDisposal1_Name[j].isChecked()) {
                                gv.DDisposal1[j] = cb_DDisposal1_Name[j].getText().toString();
                                switch (j){
                                    case 0:
                                        gv.DDisposal1[j] += "@" + et_DDisposal1_1.getText().toString();
                                        break;
                                    case 1:
                                        gv.DDisposal1[j] += "@" + et_DDisposal1_2.getText().toString();
                                        break;
                                    case 2:
                                        gv.DDisposal1[j] += "@" + et_DDisposal1_3.getText().toString();
                                        break;
                                }
                            }
                            else {
                                gv.DDisposal1[j] = "";
                            }
                        }
                        break;
                    case 2:
                        gv.DDisposal[i] += "@" + et_DDisposal3.getText().toString();
                        break;
                    case 3:
                        gv.DDisposal[i] += "@" + et_DDisposal4.getText().toString();
                        break;
                }
            }
            else {
                gv.DDisposal[i] = "";
                if(i == 0){
                    gv.DDisposal1 = new String[] { "", "", "" };
                }
            }
        }

        for (int i = 0; i < cb_ODisposal_Name.length; i++) {
            if (cb_ODisposal_Name[i].isChecked()) {
                gv.ODisposal[i] = cb_ODisposal_Name[i].getText().toString();
                cb_ODisposal_Name[i].getText().toString();
                switch (i){
                    case 3:
                        gv.ODisposal[i] += "@" + et_ODisposal4.getText().toString();
                        et_ODisposal4.getText().toString();
                        break;
                    case 4:
                        gv.ODisposal[i] += "@" + et_ODisposal5.getText().toString();
                        break;
                }
            }
            else {
                gv.ODisposal[i] = "";
            }
        }

        for (int i = 0; i < cb_ALSDisposal_Name.length; i++) {
            if (cb_ALSDisposal_Name[i].isChecked()) {
                gv.ALSDisposal[i] = cb_ALSDisposal_Name[i].getText().toString();
                switch (i){
                    case 0:
                        gv.ALSDisposal[i] += "@" + et_ALSDisposal1_1.getText().toString() + "@" + et_ALSDisposal1_2.getText().toString();
                        break;
                    case 1:
                        gv.ALSDisposal[i] += "@" + et_ALSDisposal2_1.getText().toString() + "@" + et_ALSDisposal2_2.getText().toString();
                        break;
                }
            }
            else {
                gv.ALSDisposal[i] = "";
            }
        }

        gv.PPosture = sp_Posture.getSelectedItemPosition();
        gv.Posture = "";
        if (sp_Posture.getSelectedItemPosition() != 0) {
            if(sp_Posture.getSelectedItem().equals("其他")){
                gv.Posture = et_Posture.getText().toString();
            } else {
                gv.Posture = sp_Posture.getSelectedItem().toString();
            }
        }
        //給藥1
        gv.DrugUseTime1 = TimeFocus(et_DrugUseTime1.getText().toString());
        gv.PDrugUseName1 = sp_DrugUseName1.getSelectedItemPosition();
        gv.DrugUseName1 = "";
        if (sp_DrugUseName1.getSelectedItemPosition() != 0) {
            gv.DrugUseName1 = sp_DrugUseName1.getSelectedItem().toString();
        }
        gv.PDrugUseDose1 = sp_DrugUseDose1.getSelectedItemPosition();
        gv.DrugUseDose1 = "";
        if (sp_DrugUseDose1.getSelectedItemPosition() != 0) {
            gv.DrugUseDose1 = sp_DrugUseDose1.getSelectedItem().toString();
        }
        gv.DrugUser1 = et_DrugUser1.getText().toString();
        //給藥2
        gv.DrugUseTime2 = TimeFocus(et_DrugUseTime2.getText().toString());
        gv.PDrugUseName2 = sp_DrugUseName2.getSelectedItemPosition();
        gv.DrugUseName2 = "";
        if (sp_DrugUseName2.getSelectedItemPosition() != 0) {
            gv.DrugUseName2 = sp_DrugUseName2.getSelectedItem().toString();
        }
        gv.PDrugUseDose2 = sp_DrugUseDose2.getSelectedItemPosition();
        gv.DrugUseDose2 = "";
        if (sp_DrugUseDose2.getSelectedItemPosition() != 0) {
            gv.DrugUseDose2 = sp_DrugUseDose2.getSelectedItem().toString();
        }
        gv.DrugUser2 = et_DrugUser2.getText().toString();
        //給藥3
        gv.DrugUseTime3 = TimeFocus(et_DrugUseTime3.getText().toString());
        gv.PDrugUseName3 = sp_DrugUseName3.getSelectedItemPosition();
        gv.DrugUseName3 = "";
        if (sp_DrugUseName3.getSelectedItemPosition() != 0) {
            gv.DrugUseName3 = sp_DrugUseName3.getSelectedItem().toString();
        }
        gv.PDrugUseDose3 = sp_DrugUseDose3.getSelectedItemPosition();
        gv.DrugUseDose3 = "";
        if (sp_DrugUseDose3.getSelectedItemPosition() != 0) {
            gv.DrugUseDose3 = sp_DrugUseDose3.getSelectedItem().toString();
        }
        gv.DrugUser3 = et_DrugUser3.getText().toString();
        //給藥4
        gv.DrugUseTime4 = TimeFocus(et_DrugUseTime4.getText().toString());
        gv.PDrugUseName4 = sp_DrugUseName4.getSelectedItemPosition();
        gv.DrugUseName4 = "";
        if (sp_DrugUseName4.getSelectedItemPosition() != 0) {
            gv.DrugUseName4 = sp_DrugUseName4.getSelectedItem().toString();
        }
        gv.PDrugUseDose4 = sp_DrugUseDose4.getSelectedItemPosition();
        gv.DrugUseDose4 = "";
        if (sp_DrugUseDose4.getSelectedItemPosition() != 0) {
            gv.DrugUseDose4 = sp_DrugUseDose4.getSelectedItem().toString();
        }
        gv.DrugUser4 = et_DrugUser4.getText().toString();
    }

    //病患相關main_tab4
    private void main_tab4_Save() {
        gv.Q1 = et_Q1.getText().toString();
        gv.Q2 = et_Q2.getText().toString();
        gv.Q3 = et_Q3.getText().toString();
        gv.Q4 = et_Q4.getText().toString();
        gv.Q5 = et_Q5.getText().toString();

        for (int i = 0; i < cb_MHistory_Name.length; i++) {
            if (cb_MHistory_Name[i].isChecked()) {
                gv.MHistory[i] = cb_MHistory_Name[i].getText().toString();
                if (cb_MHistory_Name[i] == findViewById(R.id.cb_MHistory12)) {
                    gv.MHistory[i] += "@" + et_MHistory12.getText().toString();
                }
            }
            else {
                gv.MHistory[i] = "";
            }
        }

        for (int i = 0; i < cb_Allergies_Name.length; i++) {
            if (cb_Allergies_Name[i].isChecked()) {
                gv.Allergies[i] = cb_Allergies_Name[i].getText().toString();
                if (cb_Allergies_Name[i] == findViewById(R.id.cb_Allergies1)) {
                    gv.Allergies[i] += "@" + et_Allergies1.getText().toString();
                }
                else if (cb_Allergies_Name[i] == findViewById(R.id.cb_Allergies2)){
                    gv.Allergies[i] += "@" + et_Allergies2.getText().toString();
                }
                else if (cb_Allergies_Name[i] == findViewById(R.id.cb_Allergies3)){
                    gv.Allergies[i] += "@" + et_Allergies3.getText().toString();
                }
            }
            else {
                gv.Allergies[i] = "";
            }
        }

        gv.People = et_People.getText().toString();
    }

    //生命徵象main_tab5
    private void main_tab5_Save() {
        //生命徵象1
        gv.LifeTime1 = TimeFocus(et_LifeTime1.getText().toString());
        gv.PConsciousness1 = sp_Consciousness1.getSelectedItemPosition();
        gv.Consciousness1 = "";
        if (sp_Consciousness1.getSelectedItemPosition() != 0) {
            gv.Consciousness1 = sp_Consciousness1.getSelectedItem().toString();
        }
        gv.LifeBreathing1 = et_LifeBreathing1.getText().toString();
        gv.Pulse1 = et_Pulse1.getText().toString();
        gv.GCS1_E = et_GCS1_E.getText().toString();
        gv.GCS1_V = et_GCS1_V.getText().toString();
        gv.GCS1_M = et_GCS1_M.getText().toString();
        gv.SBP1 = "";
        gv.DBP1 = "";
        gv.PBPressure1_A = 0;
        gv.BPressure1_A = "";
        switch (rg_BP1.getCheckedRadioButtonId()){
            case R.id.rb_BP1_1:
                gv.SBP1 = et_SBP1.getText().toString();
                gv.DBP1 = et_DBP1.getText().toString();
                break;
            case R.id.rb_BP1_2:
                gv.PBPressure1_A = sp_BPressure1_A.getSelectedItemPosition();
                if (sp_BPressure1_A.getSelectedItemPosition() != 0) {
                    gv.BPressure1_A = sp_BPressure1_A.getSelectedItem().toString();
                }
                break;
        }
        gv.SpO21 = et_SpO21.getText().toString();
        gv.Temperature1 = et_Temperature1.getText().toString();

        //生命徵象2
        gv.LifeTime2 = TimeFocus(et_LifeTime2.getText().toString());
        gv.PConsciousness2 = sp_Consciousness2.getSelectedItemPosition();
        gv.Consciousness2 = "";
        if (sp_Consciousness2.getSelectedItemPosition() != 0) {
            gv.Consciousness2 = sp_Consciousness2.getSelectedItem().toString();
        }
        gv.LifeBreathing2 = et_LifeBreathing2.getText().toString();
        gv.Pulse2 = et_Pulse2.getText().toString();
        gv.GCS2_E = et_GCS2_E.getText().toString();
        gv.GCS2_V = et_GCS2_V.getText().toString();
        gv.GCS2_M = et_GCS2_M.getText().toString();
        gv.SBP2 = "";
        gv.DBP2 = "";
        gv.PBPressure2_A = 0;
        gv.BPressure2_A = "";
        switch (rg_BP2.getCheckedRadioButtonId()){
            case R.id.rb_BP2_1:
                gv.SBP2 = et_SBP2.getText().toString();
                gv.DBP2 = et_DBP2.getText().toString();
                break;
            case R.id.rb_BP2_2:
                gv.PBPressure2_A = sp_BPressure2_A.getSelectedItemPosition();
                if (sp_BPressure2_A.getSelectedItemPosition() != 0) {
                    gv.BPressure2_A = sp_BPressure2_A.getSelectedItem().toString();
                }
                break;
        }
        gv.SpO22 = et_SpO22.getText().toString();
        gv.Temperature2 = et_Temperature2.getText().toString();

        //生命徵象3
        gv.PConsciousness3 = sp_Consciousness3.getSelectedItemPosition();
        gv.Consciousness3 = "";
        if (sp_Consciousness3.getSelectedItemPosition() != 0) {
            gv.Consciousness3 = sp_Consciousness3.getSelectedItem().toString();
        }
        gv.LifeBreathing3 = et_LifeBreathing3.getText().toString();
        gv.Pulse3 = et_Pulse3.getText().toString();
        gv.GCS3_E = et_GCS3_E.getText().toString();
        gv.GCS3_V = et_GCS3_V.getText().toString();
        gv.GCS3_M = et_GCS3_M.getText().toString();
        gv.SBP3 = "";
        gv.DBP3 = "";
        gv.PBPressure3_A = 0;
        gv.BPressure3_A = "";
        switch (rg_BP3.getCheckedRadioButtonId()){
            case R.id.rb_BP3_1:
                gv.SBP3 = et_SBP3.getText().toString();
                gv.DBP3 = et_DBP3.getText().toString();
                break;
            case R.id.rb_BP3_2:
                gv.PBPressure3_A = sp_BPressure3_A.getSelectedItemPosition();
                if (sp_BPressure3_A.getSelectedItemPosition() != 0) {
                    gv.BPressure3_A = sp_BPressure3_A.getSelectedItem().toString();
                }
                break;
        }
        gv.SpO23 = et_SpO23.getText().toString();
        gv.Temperature3 = et_Temperature3.getText().toString();
    }

    /**
     *  SQLite儲存
     */
    //基本資料main_tab1
    private void main_tab1_DBSave() {
        SQLite SQLite = new SQLite(MainActivity.this);
        SQLiteDatabase DB = SQLite.getWritableDatabase();
        ContentValues cv  = new ContentValues();
        ByteArrayOutputStream out;
        cv.put("FNumber", gv.FNumber);
        cv.put("LoadPDF", gv.LoadPDF);
        cv.put("Date", gv.Date);
        cv.put("Critical", gv.Critical);
        cv.put("AttendanceUnit", gv.AttendanceUnit);
        cv.put("AcceptedUnit", gv.AcceptedUnit);
        cv.put("LocationHappen", gv.LocationHappen);
        cv.put("AssistanceUnit", gv.AssistanceUnit);
        cv.put("HospitalAddress", gv.HospitalAddress);
        cv.put("PHospitalAddress", gv.PHospitalAddress);
        cv.put("ReasonHospital", gv.ReasonHospital);
        cv.put("MHospital", gv.MHospital);
        cv.put("Time1", gv.Time1);
        cv.put("Time2", gv.Time2);
        cv.put("Time3", gv.Time3);
        cv.put("Time4", gv.Time4);
        cv.put("Time5", gv.Time5);
        cv.put("Time6", gv.Time6);
        cv.put("Name", gv.Name);
        cv.put("AgeM", gv.AgeM);
        cv.put("AgeA", gv.AgeA);
        cv.put("Age", gv.Age);
        cv.put("Sex", gv.Sex);
        cv.put("Identity", gv.ID);
        cv.put("Area", gv.Area);
        cv.put("PArea", gv.PArea);
        cv.put("Address", gv.Address);
        cv.put("Property", gv.Property);
        cv.put("RCustody", gv.RCustody);
        cv.put("PRCustody", gv.PRCustody);
        cv.put("Custody", gv.Custody);
        cv.put("SCustody", gv.SCustody);
        if(gv.I_SCustody != null) {
            out = new ByteArrayOutputStream();
            gv.I_SCustody.compress(Bitmap.CompressFormat.JPEG, 50, out);
            cv.put("I_SCustody", out.toByteArray());
        }
        DB.update("MainTab1", cv, "CaseID=? AND Subno=?", new String[]{gv.FNumber, gv.DrawerItemID});
        SQLite.close();
        DB.close();
    }

    //現場狀況main_tab2
    private void main_tab2_DBSave() {
        SQLite SQLite = new SQLite(MainActivity.this);
        SQLiteDatabase DB = SQLite.getWritableDatabase();
        ContentValues cv  = new ContentValues();
        String str;
        cv.put("Trauma_Switch", gv.Trauma_Switch);

        str = "";
        for(String Content: gv.Trauma){
            str += Content + "－";
        }
        if(!str.equals("")) {
            cv.put("Trauma", str);
        }

        str = "";
        for(String Content: gv.Trauma1){
            str += Content + "－";
        }
        if(!str.equals("")) {
            cv.put("Trauma1", str);
        }

        str = "";
        for(String Content: gv.Traffic){
            str += Content + "－";
        }
        if(!str.equals("")) {
            cv.put("Traffic", str);
        }

        str = "";
        for(String Content: gv.NTrauma){
            str += Content + "－";
        }
        if(!str.equals("")) {
            cv.put("NTrauma", str);
        }

        str = "";
        for(String Content: gv.NTrauma6){
            str += Content + "－";
        }
        if(!str.equals("")) {
            cv.put("NTrauma6", str);
        }

        cv.put("Witnesses", gv.Witnesses);
        cv.put("CPR", gv.CPR);
        cv.put("PAD", gv.PAD);
        cv.put("ROSC", gv.ROSC);
        cv.put("PSpecies", gv.PSpecies);
        cv.put("Stroke", gv.Stroke);
        cv.put("LTime", gv.LTime);
        cv.put("Smile", gv.Smile);
        cv.put("LArm", gv.LArm);
        cv.put("Speech", gv.Speech);
        DB.update("MainTab2", cv, "CaseID=? AND Subno=?", new String[]{gv.FNumber, gv.DrawerItemID});
        SQLite.close();
        DB.close();
    }

    //急救處置main_tab3
    private void main_tab3_DBSave() {
        SQLite SQLite = new SQLite(MainActivity.this);
        SQLiteDatabase DB = SQLite.getWritableDatabase();
        ContentValues cv = new ContentValues();
        String str;

        str = "";
        for(String Content: gv.Breathing){
            str += Content + "－";
        }
        if(!str.equals("")) {
            cv.put("Breathing", str);
        }

        str = "";
        for(String Content: gv.TDisposal){
            str += Content + "－";
        }
        if(!str.equals("")) {
            cv.put("TDisposal", str);
        }

        str = "";
        for(String Content: gv.CPRDisposal){
            str += Content + "－";
        }
        if(!str.equals("")) {
            cv.put("CPRDisposal", str);
        }

        str = "";
        for(String Content: gv.CPRDisposal3){
            str += Content + "－";
        }
        if(!str.equals("")) {
            cv.put("CPRDisposal3", str);
        }

        str = "";
        for(String Content: gv.DDisposal){
            str += Content + "－";
        }
        if(!str.equals("")) {
            cv.put("DDisposal", str);
        }

        str = "";
        for(String Content: gv.DDisposal1){
            str += Content + "－";
        }
        if(!str.equals("")) {
            cv.put("DDisposal1", str);
        }

        str = "";
        for(String Content: gv.ODisposal){
            str += Content + "－";
        }
        if(!str.equals("")) {
            cv.put("ODisposal", str);
        }

        str = "";
        for(String Content: gv.ALSDisposal){
            str += Content + "－";
        }
        if(!str.equals("")) {
            cv.put("ALSDisposal", str);
        }

        cv.put("Posture", gv.Posture);
        cv.put("PPosture", gv.PPosture);

        cv.put("DrugUseTime1", gv.DrugUseTime1);
        cv.put("DrugUseName1", gv.DrugUseName1);
        cv.put("PDrugUseName1", gv.PDrugUseName1);
        cv.put("DrugUseDose1", gv.DrugUseDose1);
        cv.put("PDrugUseDose1", gv.PDrugUseDose1);
        cv.put("DrugUser1", gv.DrugUser1);
        cv.put("DrugUseTime2", gv.DrugUseTime2);
        cv.put("DrugUseName2", gv.DrugUseName2);
        cv.put("PDrugUseName2", gv.PDrugUseName2);
        cv.put("DrugUseDose2", gv.DrugUseDose2);
        cv.put("PDrugUseDose2", gv.PDrugUseDose2);
        cv.put("DrugUser2", gv.DrugUser2);
        cv.put("DrugUseTime3", gv.DrugUseTime3);
        cv.put("DrugUseName3", gv.DrugUseName3);
        cv.put("PDrugUseName3", gv.PDrugUseName3);
        cv.put("DrugUseDose3", gv.DrugUseDose3);
        cv.put("PDrugUseDose3", gv.PDrugUseDose3);
        cv.put("DrugUser3", gv.DrugUser3);
        cv.put("DrugUseTime4", gv.DrugUseTime4);
        cv.put("DrugUseName4", gv.DrugUseName4);
        cv.put("PDrugUseName4", gv.PDrugUseName4);
        cv.put("DrugUseDose4", gv.DrugUseDose4);
        cv.put("PDrugUseDose4", gv.PDrugUseDose4);
        cv.put("DrugUser4", gv.DrugUser4);
        DB.update("MainTab3", cv, "CaseID=? AND Subno=?", new String[]{gv.FNumber, gv.DrawerItemID});
        SQLite.close();
        DB.close();
    }

    //病患相關main_tab4
    private void main_tab4_DBSave() {
        SQLite SQLite = new SQLite(MainActivity.this);
        SQLiteDatabase DB = SQLite.getWritableDatabase();
        ContentValues cv  = new ContentValues();
        ByteArrayOutputStream out;
        String str;
        cv.put("Q1", gv.Q1);
        cv.put("Q2", gv.Q2);
        cv.put("Q3", gv.Q3);
        cv.put("Q4", gv.Q4);
        cv.put("Q5", gv.Q5);

        str = "";
        for(String Content: gv.MHistory){
            str += Content + "－";
        }
        if(!str.equals("")) {
            cv.put("MHistory", str);
        }

        str = "";
        for(String Content: gv.Allergies){
            str += Content + "－";
        }
        if(!str.equals("")) {
            cv.put("Allergies", str);
        }

        cv.put("People", gv.People);
        cv.put("SPeople", gv.SPeople);

        str = "";
        if(gv.Switch_People != null) {
            for (boolean Content : gv.Switch_People) {
                str += String.valueOf(Content) + "－";
            }
            cv.put("Switch_People", str);
        }

        DB.update("MainTab4", cv, "CaseID=? AND Subno=?", new String[]{gv.FNumber, gv.DrawerItemID});
        SQLite.close();
        DB.close();
    }

    //生命徵象main_tab5
    private void main_tab5_DBSave() {
        SQLite SQLite = new SQLite(MainActivity.this);
        SQLiteDatabase DB = SQLite.getWritableDatabase();
        ContentValues cv  = new ContentValues();
        cv.put("LifeTime1", gv.LifeTime1);
        cv.put("Consciousness1", gv.Consciousness1);
        cv.put("PConsciousness1", gv.PConsciousness1);
        cv.put("LifeBreathing1", gv.LifeBreathing1);
        cv.put("Pulse1", gv.Pulse1);
        cv.put("GCS1_E", gv.GCS1_E);
        cv.put("GCS1_V", gv.GCS1_V);
        cv.put("GCS1_M", gv.GCS1_M);
        cv.put("SBP1", gv.SBP1);
        cv.put("DBP1", gv.DBP1);
        cv.put("BPressure1_A", gv.BPressure1_A);
        cv.put("PBPressure1_A", gv.PBPressure1_A);
        cv.put("SpO21", gv.SpO21);
        cv.put("Temperature1", gv.Temperature1);
        cv.put("LifeTime2", gv.LifeTime2);
        cv.put("Consciousness2", gv.Consciousness2);
        cv.put("PConsciousness2", gv.PConsciousness2);
        cv.put("LifeBreathing2", gv.LifeBreathing2);
        cv.put("Pulse2", gv.Pulse2);
        cv.put("GCS2_E", gv.GCS2_E);
        cv.put("GCS2_V", gv.GCS2_V);
        cv.put("GCS2_M", gv.GCS2_M);
        cv.put("SBP2", gv.SBP2);
        cv.put("DBP2", gv.DBP2);
        cv.put("BPressure2_A", gv.BPressure2_A);
        cv.put("PBPressure2_A", gv.PBPressure2_A);
        cv.put("SpO22", gv.SpO22);
        cv.put("Temperature2", gv.Temperature2);
        cv.put("Consciousness3", gv.Consciousness3);
        cv.put("PConsciousness3", gv.PConsciousness3);
        cv.put("LifeBreathing3", gv.LifeBreathing3);
        cv.put("Pulse3", gv.Pulse3);
        cv.put("GCS3_E", gv.GCS3_E);
        cv.put("GCS3_V", gv.GCS3_V);
        cv.put("GCS3_M", gv.GCS3_M);
        cv.put("SBP3", gv.SBP3);
        cv.put("DBP3", gv.DBP3);
        cv.put("BPressure3_A", gv.BPressure3_A);
        cv.put("PBPressure3_A", gv.PBPressure3_A);
        cv.put("SpO23", gv.SpO23);
        cv.put("Temperature3", gv.Temperature3);
        DB.update("MainTab5", cv, "CaseID=? AND Subno=?", new String[]{gv.FNumber, gv.DrawerItemID});
        SQLite.close();
        DB.close();
    }

    public void onClick(View v) {
        /**
                * 基本資料main_tab1
                */
        if (v == btn_Upload)
        {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("*/*");
            intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
            Intent destIntent = Intent.createChooser(intent, "select");
            startActivityForResult(destIntent, 9973);
        }
        //發生地點
        else if (v == btn_MHospital)//同上地點
        {
            PDialog = ProgressDialog.show(MainActivity.this, "請稍等...", "接收滿床資訊中...", true);
            new Thread(R_MHospital).start();
        }
        else if (v == btn_Address)//同上地點
        {
            et_Address.setText(et_LocationHappen.getText());
        }
        //時間
        else if (v == tv_Time1)
        {
            SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
            Date curDate = new Date(System.currentTimeMillis()) ; // 獲取當前時間
            et_Time1.setText(formatter.format(curDate));
        }
        else if (v == tv_Time2)
        {
            SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
            Date curDate = new Date(System.currentTimeMillis()) ; // 獲取當前時間
            et_Time2.setText(formatter.format(curDate));
        }
        else if (v == tv_Time3)
        {
            SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
            Date curDate = new Date(System.currentTimeMillis()) ; // 獲取當前時間
            et_Time3.setText(formatter.format(curDate));
        }
        else if (v == tv_Time4)
        {
            SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
            Date curDate = new Date(System.currentTimeMillis()) ; // 獲取當前時間
            et_Time4.setText(formatter.format(curDate));
        }
        else if (v == tv_Time5)
        {
            SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
            Date curDate = new Date(System.currentTimeMillis()) ; // 獲取當前時間
            et_Time5.setText(formatter.format(curDate));
        }
        else if (v == tv_Time6)
        {
            SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
            Date curDate = new Date(System.currentTimeMillis()) ; // 獲取當前時間
            et_Time6.setText(formatter.format(curDate));
        }
        else if(v == tv_SCustody || v == iv_SCustody) {
            if(requestPermission()) {
                WritePadDialog writeTabletDialog = new WritePadDialog(MainActivity.this, gv.screenWidth, gv.screenHeight, new WritePadDialog.DialogListener() {
                    @Override
                    public void refreshActivity(Object object) {

                        mSignBitmap = (Bitmap) object;
                        signPath = fileService.saveBitmapToSDCard(FileName() + "_main_SCustody.jpg", mSignBitmap, "HandWrite");
                        iv_SCustody.setImageBitmap(mSignBitmap);
                        iv_SCustody.setVisibility(View.VISIBLE);
                        tv_SCustody.setVisibility(View.GONE);
                        String html = "<img style=\"width:50pt;height:20pt;margin-bottom:-8px;\" src=\"" + "file://" + signPath + "\">";
                        gv.SCustody = html;
                        gv.I_SCustody = mSignBitmap;
                        ImageViewSize(iv_SCustody, 625, 250);
                    }
                });
                writeTabletDialog.show();
            }
        }
        /**
         * 現場狀況main_tab2
         */

        /**
         * 急救處置main_tab3
         */
        else if (v == ll_SBreathing)//呼吸處置
        {
            if(ll_SBreathing_b){
                ll_SBreathing.setBackgroundResource(R.drawable.menu_switch_f);
                iv_Breathing.setBackgroundResource(R.drawable.menu_icon_f);
                ll_Breathing.setVisibility(View.GONE);
                ll_SBreathing_b = false;
            }
            else{
                ll_SBreathing.setBackgroundResource(R.drawable.menu_switch_t);
                iv_Breathing.setBackgroundResource(R.drawable.menu_icon_t);
                ll_Breathing.setVisibility(View.VISIBLE);
                ll_SBreathing_b = true;
            }
        }
        else if (v == ll_STDisposal)//創傷處置
        {
            if(ll_STDisposal_b){
                ll_STDisposal.setBackgroundResource(0);
                iv_TDisposal.setBackgroundResource(0);
                ll_STDisposal.setBackgroundResource(R.drawable.menu_switch_f);
                iv_TDisposal.setBackgroundResource(R.drawable.menu_icon_f);
                ll_TDisposal.setVisibility(View.GONE);
                ll_STDisposal_b = false;
            }
            else{
                ll_STDisposal.setBackgroundResource(0);
                iv_TDisposal.setBackgroundResource(0);
                ll_STDisposal.setBackgroundResource(R.drawable.menu_switch_t);
                iv_TDisposal.setBackgroundResource(R.drawable.menu_icon_t);
                ll_TDisposal.setVisibility(View.VISIBLE);
                ll_STDisposal_b = true;
            }
        }
        else if (v == ll_SCPRDisposal)//CPR處置
        {
            if(ll_SCPRDisposal_b){
                ll_SCPRDisposal.setBackgroundResource(0);
                iv_CPRDisposal.setBackgroundResource(0);
                ll_SCPRDisposal.setBackgroundResource(R.drawable.menu_switch_f);
                iv_CPRDisposal.setBackgroundResource(R.drawable.menu_icon_f);
                ll_CPRDisposal.setVisibility(View.GONE);
                ll_SCPRDisposal_b = false;
            }
            else{
                ll_SCPRDisposal.setBackgroundResource(0);
                iv_CPRDisposal.setBackgroundResource(0);
                ll_SCPRDisposal.setBackgroundResource(R.drawable.menu_switch_t);
                iv_CPRDisposal.setBackgroundResource(R.drawable.menu_icon_t);
                if(cb_CPRDisposal_Name[2].isChecked() ){
                    ll_CPRDisposal3.setVisibility(View.VISIBLE);
                }
                ll_CPRDisposal.setVisibility(View.VISIBLE);
                ll_SCPRDisposal_b = true;
            }
        }
        else if (v == ll_SDDisposal)//藥物處置
        {
            if(ll_SDDisposal_b){
                ll_SDDisposal.setBackgroundResource(0);
                iv_DDisposal.setBackgroundResource(0);
                ll_SDDisposal.setBackgroundResource(R.drawable.menu_switch_f);
                iv_DDisposal.setBackgroundResource(R.drawable.menu_icon_f);
                ll_DDisposal.setVisibility(View.GONE);
                ll_SDDisposal_b = false;
            }
            else{
                ll_SDDisposal.setBackgroundResource(0);
                iv_DDisposal.setBackgroundResource(0);
                ll_SDDisposal.setBackgroundResource(R.drawable.menu_switch_t);
                iv_DDisposal.setBackgroundResource(R.drawable.menu_icon_t);
                if(cb_DDisposal_Name[0].isChecked() ){
                    ll_DDisposal1.setVisibility(View.VISIBLE);
                }
                ll_DDisposal.setVisibility(View.VISIBLE);
                ll_SDDisposal_b = true;
            }
        }
        else if (v == ll_SODisposal)//其他處置
        {
            if(ll_SODisposal_b){
                ll_SODisposal.setBackgroundResource(0);
                iv_ODisposal.setBackgroundResource(0);
                ll_SODisposal.setBackgroundResource(R.drawable.menu_switch_f);
                iv_ODisposal.setBackgroundResource(R.drawable.menu_icon_f);
                ll_ODisposal.setVisibility(View.GONE);
                ll_SODisposal_b = false;
            }
            else{
                ll_SODisposal.setBackgroundResource(0);
                iv_ODisposal.setBackgroundResource(0);
                ll_SODisposal.setBackgroundResource(R.drawable.menu_switch_t);
                iv_ODisposal.setBackgroundResource(R.drawable.menu_icon_t);
                ll_ODisposal.setVisibility(View.VISIBLE);
                ll_SODisposal_b = true;
            }
        }
        else if (v == ll_SALSDisposal)//其他處置
        {
            if(ll_SALSDisposal_b){
                ll_SALSDisposal.setBackgroundResource(0);
                iv_ALSDisposal.setBackgroundResource(0);
                ll_SALSDisposal.setBackgroundResource(R.drawable.menu_switch_f);
                iv_ALSDisposal.setBackgroundResource(R.drawable.menu_icon_f);
                ll_ALSDisposal.setVisibility(View.GONE);
                ll_SALSDisposal_b = false;
            }
            else{
                ll_SALSDisposal.setBackgroundResource(0);
                iv_ALSDisposal.setBackgroundResource(0);
                ll_SALSDisposal.setBackgroundResource(R.drawable.menu_switch_t);
                iv_ALSDisposal.setBackgroundResource(R.drawable.menu_icon_t);
                ll_ALSDisposal.setVisibility(View.VISIBLE);
                ll_SALSDisposal_b = true;
            }
        }
        /**
         * 病患相關main_tab4
         */
        else if (v == ll_SMHistory)//過去病史
        {
            if(ll_SMHistory_b){
                ll_SMHistory.setBackgroundResource(0);
                iv_MHistory.setBackgroundResource(0);
                ll_SMHistory.setBackgroundResource(R.drawable.menu_switch_f);
                iv_MHistory.setBackgroundResource(R.drawable.menu_icon_f);
                ll_MHistory.setVisibility(View.GONE);
                ll_SMHistory_b = false;
            }
            else{
                ll_SMHistory.setBackgroundResource(0);
                iv_MHistory.setBackgroundResource(0);
                ll_SMHistory.setBackgroundResource(R.drawable.menu_switch_t);
                iv_MHistory.setBackgroundResource(R.drawable.menu_icon_t);
                ll_MHistory.setVisibility(View.VISIBLE);
                ll_SMHistory_b = true;
            }
        }
        else if (v == ll_SAllergies)//過敏史
        {
            if(ll_SAllergies_b){
                ll_SAllergies.setBackgroundResource(0);
                iv_Allergies.setBackgroundResource(0);
                ll_SAllergies.setBackgroundResource(R.drawable.menu_switch_f);
                iv_Allergies.setBackgroundResource(R.drawable.menu_icon_f);
                ll_Allergies.setVisibility(View.GONE);
                ll_SAllergies_b = false;
            }
            else{
                ll_SAllergies.setBackgroundResource(0);
                iv_Allergies.setBackgroundResource(0);
                ll_SAllergies.setBackgroundResource(R.drawable.menu_switch_t);
                iv_Allergies.setBackgroundResource(R.drawable.menu_icon_t);
                ll_Allergies.setVisibility(View.VISIBLE);
                ll_SAllergies_b = true;
            }
        }
        else if (v == ll_People)
        {
            WritePeopleDialog writeTabletDialog = new WritePeopleDialog(MainActivity.this, gv.screenWidth, gv.screenHeight, gv.Switch_People, new WritePeopleDialog.DialogListener() {
                @Override
                public void refreshActivity(boolean[] Select) {
                    gv.Switch_People = Select;
                    Resources iv_Resources = MainActivity.this.getResources();
                    Drawable iv_Drawable;
                    for(int i = 0; i < Select.length; i++) {
                        if (Select[i]) {
                            iv_Drawable = iv_Resources.getDrawable(T_People_ID[i]);
                        }
                        else{
                            iv_Drawable = iv_Resources.getDrawable(F_People_ID[i]);
                        }
                        iv_People_Name[i].setBackground(iv_Drawable);
                    }
                    View People = findViewById(R.id.ll_People);
                    People.setDrawingCacheEnabled(true);
                    People.layout(0, 0, ll_People.getWidth(), ll_People.getHeight());
                    signPath = fileService.saveBitmapToSDCard(FileName() + "_main_People.jpg", People.getDrawingCache(), "People");
                    String html = "<img style=\"width:220pt;height:180pt;margin-bottom:-8px;\" src=\""+ "file://"+ signPath + "\">";
                    gv.SPeople = html;
                }
            });
            writeTabletDialog.show();
        }

        /**
         * 生命徵象main_tab5
         */
        else if (v == tv_LifeTime)
        {
            if(gv.Time2.contains(":") && gv.Time2.length() == 5){
                String[] Time = gv.Time2.split(":");
                int H = Integer.valueOf(Time[0]);
                int M = Integer.valueOf(Time[1]);
                M += 1;
                if(M >= 60){
                    H += 1;
                    M -= 60;
                    if(H >= 24){
                        H -= 24;
                    }
                }
                if(H < 10){
                    Time[0] = "0" + String.valueOf(H);
                }
                else{
                    Time[0] = String.valueOf(H);
                }
                if(M < 10){
                    Time[1] = "0" + String.valueOf(M);
                }
                else{
                    Time[1] = String.valueOf(M);
                }
                et_LifeTime1.setText(Time[0] + ":" + Time[1]);
            }
            if(gv.Time4.contains(":") && gv.Time4.length() == 5){
                String[] Time = gv.Time4.split(":");
                int H = Integer.valueOf(Time[0]);
                int M = Integer.valueOf(Time[1]);
                M -= 3;
                if(M < 0){
                    H -= 1;
                    M += 60;
                    if(H < 0){
                        H += 24;
                    }
                }
                if(H < 10){
                    Time[0] = "0" + String.valueOf(H);
                }
                else{
                    Time[0] = String.valueOf(H);
                }
                if(M < 10){
                    Time[1] = "0" + String.valueOf(M);
                }
                else{
                    Time[1] = String.valueOf(M);
                }
                et_LifeTime2.setText(Time[0] + ":" + Time[1]);
            }
        }

    }

    public void onCheckedChanged(CompoundButton v, boolean isChecked) {
        /**
         * 基本資料main_tab1
         */

        /**
         * 現場狀況main_tab2
         */
        if(v == cb_Trauma_Name[0]){
            if (isChecked) {
                ll_Trauma1_T.setVisibility(View.VISIBLE);
            } else {
                ll_Trauma1_T.setVisibility(View.GONE);
            }
        }
        else if(v == cb_Trauma_Name[1]){
            if (isChecked) {
                ll_Trauma2_T.setVisibility(View.VISIBLE);
            } else {
                ll_Trauma2_T.setVisibility(View.GONE);
            }
        }
        else if(v == cb_Trauma_Name[7]){
            if (isChecked) {
                fl_OHCA.setVisibility(View.VISIBLE);
                tv_OHCA.setVisibility(View.VISIBLE);
            } else {
                fl_OHCA.setVisibility(View.GONE);
                tv_OHCA.setVisibility(View.GONE);
            }
        }
        else if(v == cb_NTrauma_Name[5]){
            if (isChecked) {
                ll_NTrauma6.setVisibility(View.VISIBLE);
            } else {
                ll_NTrauma6.setVisibility(View.GONE);
            }
        }
        else if(v == cb_NTrauma_Name[13]){
            if (isChecked) {
                fl_OHCA.setVisibility(View.VISIBLE);
                tv_OHCA.setVisibility(View.VISIBLE);
            } else {
                fl_OHCA.setVisibility(View.GONE);
                tv_OHCA.setVisibility(View.GONE);
            }
        }

        /**
         * 急救處置main_tab3
         */
        else if(v == cb_CPRDisposal_Name[2]){
            if (isChecked) {
                ll_CPRDisposal3.setVisibility(View.VISIBLE);
            } else {
                ll_CPRDisposal3.setVisibility(View.GONE);
            }
        }
        else if(v == cb_DDisposal_Name[0]){
            if (isChecked) {
                ll_DDisposal1.setVisibility(View.VISIBLE);
            } else {
                ll_DDisposal1.setVisibility(View.GONE);
            }
        }
        /**
         * 病患相關main_tab4
         */

        /**
         * 生命徵象main_tab5
         */

        /**
         * checkbox單選
         */

        for(int i = 0; i < cb_HospitalAddress_Name.length; i++){
            if (v == cb_HospitalAddress_Name[i]) {
                for(int j = 0; j < cb_HospitalAddress_Name.length; j++){
                    cb_HospitalAddress_Name[j].setChecked(false);
                }
                for (int k = 0; k < cb_NoHospital_Name.length; k++) {
                    cb_NoHospital_Name[k].setChecked(false);
                }
                if (isChecked) {
                    v.setChecked(true);
                } else {
                    v.setChecked(false);
                }
                break;
            }
        }
        for(int i = 0; i < cb_NoHospital_Name.length; i++){
            if (v == cb_NoHospital_Name[i]) {
                for(int j = 0; j < cb_HospitalAddress_Name.length; j++){
                    cb_HospitalAddress_Name[j].setChecked(false);
                }
                for (int k = 0; k < cb_NoHospital_Name.length; k++) {
                    cb_NoHospital_Name[k].setChecked(false);
                }
                if (isChecked) {
                    v.setChecked(true);
                } else {
                    v.setChecked(false);
                }
                break;
            }
        }
        for(int i = 0; i < cb_Sex_Name.length; i++) {
            if (v == cb_Sex_Name[i]) {
                for (int j = 0; j < cb_Sex_Name.length; j++) {
                    cb_Sex_Name[j].setChecked(false);
                }
                if (isChecked) {
                    v.setChecked(true);
                } else {
                    v.setChecked(false);
                }
                break;
            }
        }
        for(int i = 0; i < cb_Property_Name.length; i++) {
            if (v == cb_Property_Name[i]) {
                for (int j = 0; j < cb_Property_Name.length; j++) {
                    cb_Property_Name[j].setChecked(false);
                }
                if (isChecked) {
                    v.setChecked(true);
                } else {
                    v.setChecked(false);
                }
                if(cb_Property_Name[1].isChecked()){
                    ll_Custody_T.setVisibility(View.VISIBLE);
                    ll_Custody_F.setVisibility(View.GONE);
                }
                else{
                    ll_Custody_T.setVisibility(View.GONE);
                    ll_Custody_F.setVisibility(View.VISIBLE);
                }
                break;
            }
        }
        for(int i = 0; i < cb_Trauma_Switch_Name.length; i++) {
            if (v == cb_Trauma_Switch_Name[i]) {
                for (int j = 0; j < cb_Trauma_Switch_Name.length; j++) {
                    cb_Trauma_Switch_Name[j].setChecked(false);
                }
                if (isChecked) {
                    v.setChecked(true);
                } else {
                    v.setChecked(false);
                }
                if(cb_Trauma_Switch_Name[0].isChecked()) {
                    fl_Trauma_T.setVisibility(View.VISIBLE);
                    fl_Trauma_F.setVisibility(View.GONE);
                    tv_Trauma_T.setVisibility(View.VISIBLE);
                    tv_Trauma_F.setVisibility(View.GONE);
                    if(cb_Trauma_Name[7].isChecked()){
                        fl_OHCA.setVisibility(View.VISIBLE);
                        tv_OHCA.setVisibility(View.VISIBLE);
                    } else {
                        fl_OHCA.setVisibility(View.GONE);
                        tv_OHCA.setVisibility(View.GONE);
                    }
                }
                else if(cb_Trauma_Switch_Name[1].isChecked()) {
                    fl_Trauma_T.setVisibility(View.GONE);
                    fl_Trauma_F.setVisibility(View.VISIBLE);
                    tv_Trauma_T.setVisibility(View.GONE);
                    tv_Trauma_F.setVisibility(View.VISIBLE);
                    if(cb_NTrauma_Name[13].isChecked()){
                        fl_OHCA.setVisibility(View.VISIBLE);
                        tv_OHCA.setVisibility(View.VISIBLE);
                    } else {
                        fl_OHCA.setVisibility(View.GONE);
                        tv_OHCA.setVisibility(View.GONE);
                    }
                }
                else{
                    fl_Trauma_T.setVisibility(View.GONE);
                    fl_Trauma_F.setVisibility(View.GONE);
                    tv_Trauma_T.setVisibility(View.GONE);
                    tv_Trauma_F.setVisibility(View.GONE);
                    fl_OHCA.setVisibility(View.GONE);
                    tv_OHCA.setVisibility(View.GONE);
                }
                break;
            }
        }

        for(int i = 0; i < cb_Trauma_Name.length; i++) {
            if (v == cb_Trauma_Name[i]) {
                for (int j = 0; j < cb_Trauma_Name.length; j++) {
                    cb_Trauma_Name[j].setChecked(false);
                }
                if (isChecked) {
                    v.setChecked(true);
                } else {
                    v.setChecked(false);
                }
                break;
            }
        }

        for(int i = 0; i < cb_NTrauma_Name.length; i++) {
            if (v == cb_NTrauma_Name[i]) {
                for (int j = 0; j < cb_NTrauma_Name.length; j++) {
                    cb_NTrauma_Name[j].setChecked(false);
                }
                if (isChecked) {
                    v.setChecked(true);
                } else {
                    v.setChecked(false);
                }
                break;
            }
        }

        for(int i = 0; i < cb_Traffic_Name.length; i++) {
            if (v == cb_Traffic_Name[i]) {
                for (int j = 0; j < cb_Traffic_Name.length; j++) {
                    cb_Traffic_Name[j].setChecked(false);
                }
                if (isChecked) {
                    v.setChecked(true);
                } else {
                    v.setChecked(false);
                }
                break;
            }
        }

        for(int i = 0; i < cb_Witnesses_Name.length; i++) {
            if (v == cb_Witnesses_Name[i]) {
                for (int j = 0; j < cb_Witnesses_Name.length; j++) {
                    cb_Witnesses_Name[j].setChecked(false);
                }
                if (isChecked) {
                    v.setChecked(true);
                } else {
                    v.setChecked(false);
                }
                break;
            }
        }
        for(int i = 0; i < cb_CPR_Name.length; i++) {
            if (v == cb_CPR_Name[i]) {
                for (int j = 0; j < cb_CPR_Name.length; j++) {
                    cb_CPR_Name[j].setChecked(false);
                }
                if (isChecked) {
                    v.setChecked(true);
                } else {
                    v.setChecked(false);
                }
                break;
            }
        }
        for(int i = 0; i < cb_PAD_Name.length; i++) {
            if (v == cb_PAD_Name[i]) {
                for (int j = 0; j < cb_PAD_Name.length; j++) {
                    cb_PAD_Name[j].setChecked(false);
                }
                if (isChecked) {
                    v.setChecked(true);
                } else {
                    v.setChecked(false);
                }
                break;
            }
        }
        for(int i = 0; i < cb_ROSC_Name.length; i++) {
            if (v == cb_ROSC_Name[i]) {
                for (int j = 0; j < cb_ROSC_Name.length; j++) {
                    cb_ROSC_Name[j].setChecked(false);
                }
                if (isChecked) {
                    v.setChecked(true);
                } else {
                    v.setChecked(false);
                }
                break;
            }
        }
        for(int i = 0; i < cb_PSpecies_Name.length; i++) {
            if (v == cb_PSpecies_Name[i]) {
                for (int j = 0; j < cb_PSpecies_Name.length; j++) {
                    cb_PSpecies_Name[j].setChecked(false);
                }
                if (isChecked) {
                    v.setChecked(true);
                } else {
                    v.setChecked(false);
                }
                break;
            }
        }
        for(int i = 0; i < cb_Stroke_Name.length; i++) {
            if (v == cb_Stroke_Name[i]) {
                for (int j = 0; j < cb_Stroke_Name.length; j++) {
                    cb_Stroke_Name[j].setChecked(false);
                }
                if (isChecked) {
                    v.setChecked(true);
                } else {
                    v.setChecked(false);
                }
                break;
            }
        }
    }

    /**
     * 簽名權限
     * */

    public static final int EXTERNAL_STORAGE_REQ_CODE = 10 ;

    public boolean requestPermission(){
        //判斷是否取得權限
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, EXTERNAL_STORAGE_REQ_CODE);
            return false;
        }
        else{
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case EXTERNAL_STORAGE_REQ_CODE: {
                // 拒絕grantResults為空值
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //允許
                    dialog = new AlertDialog.Builder(MainActivity.this);
                    dialog.setTitle("訊息"); //設定dialog 的title顯示內容
                    dialog.setMessage("取得權限簽名可正常使用!!");
                    dialog.setCancelable(false); //關閉 Android 系統的主要功能鍵(menu,home等...)
                    dialog.setPositiveButton("確定", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });
                    dialog.show();
                } else {
                    //拒絕
                    dialog = new AlertDialog.Builder(MainActivity.this);
                    dialog.setTitle("錯誤"); //設定dialog 的title顯示內容
                    dialog.setMessage("無法使用簽名!!");
                    dialog.setCancelable(false); //關閉 Android 系統的主要功能鍵(menu,home等...)
                    dialog.setPositiveButton("確定", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });
                    dialog.show();
                }
                return;
            }
        }
    }

    /**
     * 離開焦點
     */
    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (!hasFocus) {
            EditText ET = (EditText) findViewById(v.getId());
            String str = ET.getText().toString();
            if (str.length() == 3 || str.length() == 4  || (str.contains(":") && str.length() == 5)) {
                if(str.length() == 3){
                    str = "0" + str;
                }
                if(str.contains(":") && str.length() == 5){
                    str = str.substring(0, 2) + str.substring(3, 5);
                }
                Pattern pattern = Pattern.compile("[0-9]*");
                Matcher isNum = pattern.matcher(str);
                if (isNum.matches()) {
                    if (Integer.parseInt(str.substring(0, 2)) < 24 & Integer.parseInt(str.substring(2, 4)) < 60) {
                        ET.setText(str.substring(0, 2) + ":" + str.substring(2, 4));
                    }
                    else{
                        ET.setText("");
                        Toast.makeText(MainActivity.this, "請輸入24小時制", Toast.LENGTH_LONG).show();
                    }
                } else {
                    ET.setText("");
                    Toast.makeText(MainActivity.this, "請輸入數字", Toast.LENGTH_LONG).show();
                }
            }
            else {
                ET.setText("");
                Toast.makeText(MainActivity.this, "請輸入四碼數字", Toast.LENGTH_LONG).show();
            }
        }
    }

    private String TimeFocus(String time) {
        String Result = "";
        if (time.length() == 3 || time.length() == 4 || (time.contains(":") && time.length() == 5)) {
            if (time.length() == 3) {
                time = "0" + time;
            }
            if (time.contains(":") && time.length() == 5) {
                time = time.substring(0, 2) + time.substring(3, 5);
            }
            Pattern pattern = Pattern.compile("[0-9]*");
            Matcher isNum = pattern.matcher(time);
            if (isNum.matches()) {
                if (Integer.parseInt(time.substring(0, 2)) < 24 & Integer.parseInt(time.substring(2, 4)) < 60) {
                    Result = time.substring(0, 2) + ":" + time.substring(2, 4);
                }
            }
        }
        return Result;
    }

    /**
     * 圖片大小調整
     */
    private void ImageViewSize(ImageView imgid, int evenWidth, int evenHight) {
        // TODO 自動產生的方法 Stub
        ViewGroup.LayoutParams params = imgid.getLayoutParams();
        params.width = evenWidth;
        params.height = evenHight;
        imgid.setLayoutParams(params);
    }

    /**
     * 標頭點擊監聽
     */
    public class MyOnClickListener implements View.OnClickListener {
        private int index = 0;

        public MyOnClickListener(int i) {
            index = i;
        }

        public void onClick(View v) {
            mPager.setCurrentItem(index);
        }
    }

    /**
     * 頁面切換監聽
     */
    public class MyOnPageChangeListener implements ViewPager.OnPageChangeListener {

        int one = offset * 2 + bmpW;// 1 -> 2 偏移量
        int two = one * 2;// 1 -> 3 偏移量
        int three = one * 3;// 1 -> 4 偏移量
        int four = one * 4;// 1 -> 5 偏移量

        public void onPageSelected(int arg0) {
            Animation animation = null;
            switch (arg0) {
                case 0:
                    if (currIndex == 1) {
                        animation = new TranslateAnimation(one, 0, 0, 0);
                        main_tab2_Save();
                        main_tab2_DBSave();
                    } else if (currIndex == 2) {
                        animation = new TranslateAnimation(two, 0, 0, 0);
                        main_tab3_Save();
                        main_tab3_DBSave();
                    } else if (currIndex == 3) {
                        animation = new TranslateAnimation(three, 0, 0, 0);
                        main_tab4_Save();
                        main_tab4_DBSave();
                    } else if (currIndex == 4) {
                        animation = new TranslateAnimation(four, 0, 0, 0);
                        main_tab5_Save();
                        main_tab5_DBSave();
                    }
                    break;
                case 1:
                    if (currIndex == 0) {
                        animation = new TranslateAnimation(offset, one, 0, 0);
                        main_tab1_Save();
                        main_tab1_DBSave();
                    } else if (currIndex == 2) {
                        animation = new TranslateAnimation(two, one, 0, 0);
                        main_tab3_Save();
                        main_tab3_DBSave();
                    } else if (currIndex == 3) {
                        animation = new TranslateAnimation(three, one, 0, 0);
                        main_tab4_Save();
                        main_tab4_DBSave();
                    } else if (currIndex == 4) {
                        animation = new TranslateAnimation(four, one, 0, 0);
                        main_tab5_Save();
                        main_tab5_DBSave();
                    }
                    break;
                case 2:
                    if (currIndex == 0) {
                        animation = new TranslateAnimation(offset, two, 0, 0);
                        main_tab1_Save();
                        main_tab1_DBSave();
                    } else if (currIndex == 1) {
                        animation = new TranslateAnimation(one, two, 0, 0);
                        main_tab2_Save();
                        main_tab2_DBSave();
                    } else if (currIndex == 3) {
                        animation = new TranslateAnimation(three, two, 0, 0);
                        main_tab4_Save();
                        main_tab4_DBSave();
                    } else if (currIndex == 4) {
                        animation = new TranslateAnimation(four, two, 0, 0);
                        main_tab5_Save();
                        main_tab5_DBSave();
                    }
                    break;
                case 3:
                    if (currIndex == 0) {
                        animation = new TranslateAnimation(offset, three, 0, 0);
                        main_tab1_Save();
                        main_tab1_DBSave();
                    } else if (currIndex == 1) {
                        animation = new TranslateAnimation(one, three, 0, 0);
                        main_tab2_Save();
                        main_tab2_DBSave();
                    } else if (currIndex == 2) {
                        animation = new TranslateAnimation(two, three, 0, 0);
                        main_tab3_Save();
                        main_tab3_DBSave();
                    } else if (currIndex == 4) {
                        animation = new TranslateAnimation(four, three, 0, 0);
                        main_tab5_Save();
                        main_tab5_DBSave();
                    }
                    break;
                case 4:
                    if (currIndex == 0) {
                        animation = new TranslateAnimation(offset, four, 0, 0);
                        main_tab1_Save();
                        main_tab1_DBSave();
                    } else if (currIndex == 1) {
                        animation = new TranslateAnimation(one, four, 0, 0);
                        main_tab2_Save();
                        main_tab2_DBSave();
                    } else if (currIndex == 2) {
                        animation = new TranslateAnimation(two, four, 0, 0);
                        main_tab3_Save();
                        main_tab3_DBSave();
                    } else if (currIndex == 3) {
                        animation = new TranslateAnimation(three, four, 0, 0);
                        main_tab4_Save();
                        main_tab4_DBSave();
                    }
                    break;
            }
            currIndex = arg0;
            animation.setFillAfter(true);// True:停在動畫结束位置
            animation.setDuration(300);
            cursor.startAnimation(animation);
        }

        public void onPageScrolled(int arg0, float arg1, int arg2) {
        }

        public void onPageScrollStateChanged(int arg0) {
        }
    }

    public String FileName() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        Date curDate = new Date(System.currentTimeMillis()); // 獲取當前時間
        return formatter.format(curDate);
    }

    //handling the image chooser activity result
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();
            uploadMultipart();
        }
    }

    public void uploadMultipart() {
        //getting the actual path of the image
        Log.e("[filePath]:", filePath.toString());
        String path = getPath(filePath);
        Log.e("[Path]:", path);
        //Uploading code
        try {
            String uploadId = UUID.randomUUID().toString();

            UploadNotificationConfig config = new UploadNotificationConfig();
            config.setTitle("檔案上傳");
            config.setInProgressMessage("上傳中...");
            config.setCompletedMessage("上傳成功!");
            config.setErrorMessage("上傳失敗!");

            //Creating a multi part request
            new MultipartUploadRequest(this, uploadId, url + "testUpload.aspx")
                    .addFileToUpload(path, "pdf") //Adding file
                    .setNotificationConfig(config)
                    .addParameter("Hospital",sp_HospitalAddress.getSelectedItem().toString())
                    .setUtf8Charset()
                    .startUpload(); //Starting the upload

        } catch (Exception exc) {

            Log.e("log_tag", "[Error]：" + exc.getMessage());
            Toast.makeText(this, exc.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    //method to get the file path from uri
    public String getPath(Uri uri) {
        if (uri == null)
        {
            return null;
        }

        // 判斷是否為Android 4.4之後的版本
        final boolean after44 = Build.VERSION.SDK_INT >= 19;
        if (after44 && DocumentsContract.isDocumentUri(MainActivity.this, uri))
        {
            // 如果是Android 4.4之後的版本，而且屬於文件URI
            final String authority = uri.getAuthority();
            // 判斷Authority是否為本地端檔案所使用的
            if ("com.android.externalstorage.documents".equals(authority))
            {
                // 外部儲存空間
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] divide = docId.split(":");
                final String type = divide[0];
                if ("primary".equals(type))
                {
                    String path = Environment.getExternalStorageDirectory().getAbsolutePath().concat("/").concat(divide[1]);
                    return path;
                }
                else
                {
                    String path = "/storage/".concat(type).concat("/").concat(divide[1]);
                    return path;
                }
            }
            else if ("com.android.providers.downloads.documents".equals(authority))
            {
                // 下載目錄
                final String docId = DocumentsContract.getDocumentId(uri);
                final Uri downloadUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.parseLong(docId));
                String path = queryAbsolutePath(MainActivity.this, downloadUri);
                return path;
            }
            else if ("com.android.providers.media.documents".equals(authority))
            {
                // 圖片、影音檔案
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] divide = docId.split(":");
                final String type = divide[0];
                Uri mediaUri = null;
                if ("image".equals(type))
                {
                    mediaUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                }
                else if ("video".equals(type))
                {
                    mediaUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                }
                else if ("audio".equals(type))
                {
                    mediaUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }
                else
                {
                    return null;
                }
                mediaUri = ContentUris.withAppendedId(mediaUri, Long.parseLong(divide[1]));
                String path = queryAbsolutePath(MainActivity.this, mediaUri);
                return path;
            }
        }
        else
        {
            // 如果是一般的URI
            final String scheme = uri.getScheme();
            String path = null;
            if ("content".equals(scheme))
            {
                // 內容URI
                path = queryAbsolutePath(MainActivity.this, uri);
            }
            else if ("file".equals(scheme))
            {
                // 檔案URI
                path = uri.getPath();
            }
            return path;
        }
        return null;
    }

    public static String queryAbsolutePath(final Context context, final Uri uri)
    {
        final String[] projection = { MediaStore.MediaColumns.DATA};
        Cursor cursor = null;
        try
        {
            cursor = context.getContentResolver().query(uri, projection, null, null, null);
            if (cursor != null && cursor.moveToFirst())
            {
                final int index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
                return cursor.getString(index);
            }
        }
        catch (final Exception ex) {
            ex.printStackTrace();
            if (cursor != null)
            {
                cursor.close();
            }
        }
        return null;
    }
    /**
     * 返回上一頁
     *
     * @return
     */
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if ((keyCode == KeyEvent.KEYCODE_BACK)) {   //確定按下退出鍵
            dialog = new AlertDialog.Builder(MainActivity.this);
            dialog.setTitle("提示"); //設定dialog 的title顯示內容
            dialog.setMessage("是否返回起始頁面?");
            dialog.setCancelable(false); //關閉 Android 系統的主要功能鍵(menu,home等...)
            dialog.setPositiveButton("是", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    SaveData();

                    Intent intent = new Intent();
                    intent.setClass(MainActivity.this, Login.class);
                    startActivity(intent);
                    finish();
                }
            });
            dialog.setNegativeButton("否", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            dialog.show();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    Runnable R_UploadHospital = new Runnable(){
        @Override
        public void run() {
            // TODO: http request.
            Message msg = new Message();
            Bundle data = new Bundle();

            //第一版
         //   String str = gv.HUpload(url, String.valueOf(sp_UploadHospital.getSelectedItem()));
            //第二版
            String str = uploadDataHospital();

            if (!str.equals("OK")) {
                str = "Error";
            }
            else {
                str = "已成功通知'" + sp_HospitalAddress.getSelectedItem().toString() + "'!";
            }
            PDialog.dismiss();

            data.putString("value", str);
            msg.setData(data);
            H_UploadHospital.sendMessage(msg);
        }
    };


    public String uploadDataHospital(){
        String message = null;
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        // check for login response
        try {

            JSONObject json = gv.UploadData(String.valueOf(sp_UploadHospital.getSelectedItem()));



            if (json.getString("KEY_SUCCESS") != null) {

                String res = json.getString("KEY_SUCCESS");
                if (res.equals("true")) {

                    message="OK2";
                } else {
                    // Error in login
                    message="OK";
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
            message="OK";
        }
        return message;
    }

    Handler H_UploadHospital = new Handler()
    {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle data = msg.getData();
            String str = data.getString("value");
            dialog = new AlertDialog.Builder(MainActivity.this);
            dialog.setTitle("提示"); //設定dialog 的title顯示內容
            dialog.setMessage(str);
            dialog.setCancelable(false); //關閉 Android 系統的主要功能鍵(menu,home等...)
            dialog.setPositiveButton("確定", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                }
            });
            dialog.show();
        }
    };

    Runnable R_MHospital = new Runnable(){
        @Override
        public void run() {
            // TODO: http request.
            Message msg = new Message();
            Bundle data = new Bundle();

            HttpPost httppost = new HttpPost(url + "APP_Get_MHospital.aspx"); //宣告要使用的頁面
            List<NameValuePair> params = new ArrayList<NameValuePair>(); //宣告參數清單

            String str = gv.GetResponse(httppost, params);
            if (str.equals("Null")) {
                str = "";
            }

            data.putString("value", str);
            msg.setData(data);
            H_MHospital.sendMessage(msg);
        }
    };

    Handler H_MHospital = new Handler()
    {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle data = msg.getData();
            String str = data.getString("value");
            tv_MHospital.setText(str);

            PDialog.dismiss();
        }
    };

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
                    MainActivity.this,
                    listItem,
                    R.layout.listview_subno,
                    gv.DrawerItemPosition,
                    new String[]{"SubnoID"},
                    new int[]{R.id.iv_Select, R.id.tv_SubnoID, R.id.btn_Delete}
            );

            lv_Drawer_Subno.setAdapter(SubnoAdapter);

            switch (currIndex) {
                case 0:
                    main_tab1_Modify();
                    main_tab2_Modify();
                    gv.Modify_Tab3 = true;
                    gv.Modify_Tab4 = true;
                    gv.Modify_Tab5 = true;
                    break;
                case 1:
                    main_tab1_Modify();
                    main_tab2_Modify();
                    main_tab3_Modify();
                    gv.Modify_Tab4 = true;
                    gv.Modify_Tab5 = true;
                    break;
                case 2:
                    gv.Modify_Tab1 = true;
                    main_tab2_Modify();
                    main_tab3_Modify();
                    main_tab4_Modify();
                    gv.Modify_Tab5 = true;
                    break;
                case 3:
                    gv.Modify_Tab1 = true;
                    gv.Modify_Tab2 = true;
                    main_tab3_Modify();
                    main_tab4_Modify();
                    main_tab5_Modify();
                    break;
                case 4:
                    gv.Modify_Tab1 = true;
                    gv.Modify_Tab2 = true;
                    gv.Modify_Tab3 = true;
                    main_tab4_Modify();
                    main_tab5_Modify();
                    break;
            }

            // 關掉
            dl_Drawer.closeDrawer(ll_Drawer);
            PDialog.dismiss();
        }
    };

    Runnable R_NewTable = new Runnable() {
        @Override
        public void run() {
            // TODO: http request.
            SQLite SQLite = new SQLite(MainActivity.this);
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
                    MainActivity.this,
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






//藍芽設備控制
    protected void bluetooth(){

        // Use this check to determine whether BLE is supported on the device.  Then you can
        // selectively disable BLE-related features.
        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            Toast.makeText(this, "不支持", Toast.LENGTH_SHORT).show();
            finish();
        }

        // 初始化獲得一個bluetoothManager
        // Initializes a Bluetooth adapter.  For API level 18 and above, get a reference to
        // BluetoothAdapter through BluetoothManager.
        final BluetoothManager bluetoothManager =
                (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = bluetoothManager.getAdapter();

        // 确认设备支持蓝牙并且已经启用. 如果没有,
        // 显示一个对话框要求用户授权启用蓝牙.
        //並檢測設備是否支持藍牙
        // Checks if Bluetooth is supported on the device.
        if (mBluetoothAdapter == null) {
            Toast.makeText(this, "藍芽不支持", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        //啓動服務程式ervice
        final Intent intent = new Intent(this, ServiceBluetoothSearch.class);
        startService(intent);

        //綁縛(Bind)服務程式
        bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);

        //註冊廣播接收receiver
        IntentFilter filter = new IntentFilter(ServiceBluetoothSearch.ACTION);
        registerReceiver(receiver, filter);

    }


    public boolean checkService() {
        boolean isRunning = false;
        ActivityManager activityManager = (ActivityManager)
                getBaseContext().getSystemService(Context.ACTIVITY_SERVICE);
        //此处只在前30个中查找，大家根据需要调整
        List<ActivityManager.RunningServiceInfo> serviceList = activityManager.getRunningServices(30);
        if (!(serviceList.size() > 0)) {
            isRunning = false;
        }
        for (int i = 0; i < serviceList.size(); i++) {
            if (serviceList.get(i).service.getClassName().equals("ServiceBluetoothSearch") == true) {
                isRunning = true;

            }
        }

        return isRunning;
    }



    @Override
    protected void onResume() {
        super.onResume();
        Intent intent = new Intent(this, ServiceBluetoothSearch.class);
        bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
        readequipment();

    }
    public void onDestroy() {
        super.onDestroy();
        if(checkService()){
            unbindService(serviceConnection);
            searchService.stopSelf();    //Stop Service//Stop Service
            unregisterReceiver(receiver);
        }

    }


    //權限方法
    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {

    }
    //權限方法
    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {

        Intent intent = new Intent(this, Login.class);
        startActivity(intent);
    }

    public void readequipment() {
        String[] perms = {Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.ACCESS_FINE_LOCATION};
        //要求權限
        if (EasyPermissions.hasPermissions(MainActivity.this, perms)) {

            if (!mBluetoothAdapter.isEnabled()) {
                if (!mBluetoothAdapter.isEnabled()) {
                    Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
                }
            }

        } else {
            EasyPermissions.requestPermissions(MainActivity.this, "藍芽搜尋需要權限",
                    100, perms);
        }

    }



    public void IntentBle(String type){
        Intent intent;
        SaveData();
        SaveCurrIndex();

        if (type.equals("Temp")) {


         intent = new Intent(MainActivity.this, BleTemperature.class);
          startActivity(intent);


            /*
            unbindService(serviceConnection);
            searchService.stopSelf();    //Stop Service//Stop Service
            unregisterReceiver(receiver);
            finish();
            */

        } else if (type.equals("BP")) {
            intent = new Intent(MainActivity.this, BleBloodPressure.class);

            startActivity(intent);

        } else if (type.equals("SPO2")) {
            intent = new Intent(MainActivity.this, BleBloodOxygen.class);

            startActivity(intent);

        }else if(type.equals("ECG")) {
            intent = new Intent(this, BleECG.class);

            intent.putExtra("DEVICE_ADDRESS", ECGWS12);
            startActivity(intent);
            unbindService(serviceConnection);
            searchService.stopSelf();    //Stop Service//Stop Service
            unregisterReceiver(receiver);
            finish();
        }else if(type.equals("BG_PC")) {
            intent = new Intent(this, BleBloodSugar_pc.class);

            startActivity(intent);

        }else if(type.equals("BG_N")) {
            intent = new Intent(this, BleBloodSugar.class);

            startActivity(intent);

        }
    }



    public void onRestart()
    {
        super.onRestart();
        switch (currIndex) {
            case 0:
                main_tab1_Modify();
                main_tab2_Modify();
                gv.Modify_Tab3 = true;
                gv.Modify_Tab4 = true;
                gv.Modify_Tab5 = true;
                break;
            case 1:
                main_tab1_Modify();
                main_tab2_Modify();
                main_tab3_Modify();
                gv.Modify_Tab4 = true;
                gv.Modify_Tab5 = true;
                break;
            case 2:
                gv.Modify_Tab1 = true;
                main_tab2_Modify();
                main_tab3_Modify();
                main_tab4_Modify();
                gv.Modify_Tab5 = true;
                break;
            case 3:
                gv.Modify_Tab1 = true;
                gv.Modify_Tab2 = true;
                main_tab3_Modify();
                main_tab4_Modify();
                main_tab5_Modify();
                break;
            case 4:
                gv.Modify_Tab1 = true;
                gv.Modify_Tab2 = true;
                gv.Modify_Tab3 = true;
                main_tab4_Modify();
                main_tab5_Modify();
                break;
        }
    }


}