package tw.com.mygis.fireapp_taoyuan;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Bart on 2014/8/23.
 */
public class Drawer_Handwrite extends Activity implements OnClickListener,CompoundButton.OnCheckedChangeListener {

    //等待視窗
    private ProgressDialog PDialog = null;
    private AlertDialog.Builder dialog = null;
    /**
     * 側頁
     */
    private DrawerLayout dl_Drawer;
    private ActionBarDrawerToggle abdt_Drawer;

    private LinearLayout ll_Drawer;
    private ListView lv_Drawer_Subno, lv_Drawer_Item;
    //手寫功能
    private Bitmap mSignBitmap = null;//手寫文件
    private String signPath;//手寫簽名路徑
    private FileService fileService = new FileService(Drawer_Handwrite.this);
    //全域變數
    private GlobalVariable gv;
    // 記錄被選擇的選單指標用
    private ArrayList<HashMap<String,String>> listItem = new ArrayList<HashMap<String,String>>();

    private TextView tv_SNote, tv_SAStaff1, tv_SAStaff2, tv_SAStaff3, tv_SAStaff4, tv_SAStaff5, tv_SAStaff6, tv_SHStaff, tv_SRRelationship, tv_SRelationship;
    private EditText et_AStaffNO1, et_AStaffNO2, et_AStaffNO3, et_AStaffNO4, et_AStaffNO5, et_AStaffNO6, et_RRelationship, et_Relationship, et_Phone;
    private ImageView iv_SNote, iv_SAStaff1, iv_SAStaff2, iv_SAStaff3, iv_SAStaff4, iv_SAStaff5, iv_SAStaff6, iv_SHStaff, iv_SRRelationship, iv_SRelationship;
    private CheckBox cb_RHospital;
    private Spinner sp_RRelationship, sp_Relationship;
    private CheckBox[] cb_IClassification_Name = new CheckBox[5];
    private int[] cb_IClassification_ID = new int[]{R.id.cb_IClassification1, R.id.cb_IClassification2, R.id.cb_IClassification3, R.id.cb_IClassification4, R.id.cb_IClassification5};
    private CheckBox[] cb_RHospital_Name = new CheckBox[2];
    private int[] cb_RHospital_ID = new int[]{R.id.cb_RHospital1, R.id.cb_RHospital2};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drawer_handwrite);

        controls();
        Click();
        Modify();
        setDrawerLayout();
    }

    /**
     *  控制項設定
     */
    private void controls() {
        gv= (GlobalVariable) getApplicationContext();

        tv_SNote = (TextView) findViewById(R.id.tv_SNote);
        tv_SAStaff1 = (TextView) findViewById(R.id.tv_SAStaff1);
        tv_SAStaff2 = (TextView) findViewById(R.id.tv_SAStaff2);
        tv_SAStaff3 = (TextView) findViewById(R.id.tv_SAStaff3);
        tv_SAStaff4 = (TextView) findViewById(R.id.tv_SAStaff4);
        tv_SAStaff5 = (TextView) findViewById(R.id.tv_SAStaff5);
        tv_SAStaff6 = (TextView) findViewById(R.id.tv_SAStaff6);
        tv_SHStaff = (TextView) findViewById(R.id.tv_SHStaff);
        tv_SRRelationship = (TextView) findViewById(R.id.tv_SRRelationship);
        tv_SRelationship = (TextView) findViewById(R.id.tv_SRelationship);
        et_AStaffNO1 = (EditText) findViewById(R.id.et_AStaffNO1);
        et_AStaffNO2 = (EditText) findViewById(R.id.et_AStaffNO2);
        et_AStaffNO3 = (EditText) findViewById(R.id.et_AStaffNO3);
        et_AStaffNO4 = (EditText) findViewById(R.id.et_AStaffNO4);
        et_AStaffNO5 = (EditText) findViewById(R.id.et_AStaffNO5);
        et_AStaffNO6 = (EditText) findViewById(R.id.et_AStaffNO6);
        et_RRelationship = (EditText) findViewById(R.id.et_RRelationship);
        et_Relationship = (EditText) findViewById(R.id.et_Relationship);
        et_Phone = (EditText) findViewById(R.id.et_Phone);
        iv_SNote = (ImageView) findViewById(R.id.iv_SNote);
        iv_SAStaff1 = (ImageView) findViewById(R.id.iv_SAStaff1);
        iv_SAStaff2 = (ImageView) findViewById(R.id.iv_SAStaff2);
        iv_SAStaff3 = (ImageView) findViewById(R.id.iv_SAStaff3);
        iv_SAStaff4 = (ImageView) findViewById(R.id.iv_SAStaff4);
        iv_SAStaff5 = (ImageView) findViewById(R.id.iv_SAStaff5);
        iv_SAStaff6 = (ImageView) findViewById(R.id.iv_SAStaff6);
        iv_SHStaff = (ImageView) findViewById(R.id.iv_SHStaff);
        iv_SRRelationship = (ImageView) findViewById(R.id.iv_SRRelationship);
        iv_SRelationship = (ImageView) findViewById(R.id.iv_SRelationship);
        cb_RHospital = (CheckBox) findViewById(R.id.cb_RHospital);

        String[] sp_Relationship_item = new String[]{"請選擇","父親","母親","夫妻","兒子","女兒","兄弟","姊妹","同事","友人","其他"};
        ArrayAdapter<String> adapter_Relationship = new ArrayAdapter<String>(this,R.layout.spinner_style,R.id.txtvwSpinner,sp_Relationship_item);
        adapter_Relationship.setDropDownViewResource(R.layout.spinner_dropdown_style);
        sp_RRelationship = (Spinner) findViewById(R.id.sp_RRelationship);
        sp_RRelationship.setAdapter(adapter_Relationship);
        sp_Relationship = (Spinner) findViewById(R.id.sp_Relationship);
        sp_Relationship.setAdapter(adapter_Relationship);

        for(int i = 0; i < cb_IClassification_Name.length; i++){
            cb_IClassification_Name[i] = (CheckBox) findViewById(cb_IClassification_ID[i]);
        }
        for(int i = 0; i < cb_RHospital_Name.length; i++){
            cb_RHospital_Name[i] = (CheckBox) findViewById(cb_RHospital_ID[i]);
        }
    }

    /**
     *控制項定義
     */
    private CheckBox[] CheckBoxCD(int[] ID) {
        CheckBox[] Name = new CheckBox[ID.length];
        for(int i = 0; i < Name.length; i++){
            Name[i] = (CheckBox) findViewById(ID[i]);
        }
        return Name;
    }

    /**
     * 事件定義
     */
    private void Click() {
        tv_SNote.setOnClickListener(Drawer_Handwrite.this);
        tv_SAStaff1.setOnClickListener(Drawer_Handwrite.this);
        tv_SAStaff2.setOnClickListener(Drawer_Handwrite.this);
        tv_SAStaff3.setOnClickListener(Drawer_Handwrite.this);
        tv_SAStaff4.setOnClickListener(Drawer_Handwrite.this);
        tv_SAStaff5.setOnClickListener(Drawer_Handwrite.this);
        tv_SAStaff6.setOnClickListener(Drawer_Handwrite.this);
        tv_SHStaff.setOnClickListener(Drawer_Handwrite.this);
        tv_SRRelationship.setOnClickListener(Drawer_Handwrite.this);
        tv_SRelationship.setOnClickListener(Drawer_Handwrite.this);

        iv_SNote.setOnClickListener(Drawer_Handwrite.this);
        iv_SAStaff1.setOnClickListener(Drawer_Handwrite.this);
        iv_SAStaff2.setOnClickListener(Drawer_Handwrite.this);
        iv_SAStaff3.setOnClickListener(Drawer_Handwrite.this);
        iv_SAStaff4.setOnClickListener(Drawer_Handwrite.this);
        iv_SAStaff5.setOnClickListener(Drawer_Handwrite.this);
        iv_SAStaff6.setOnClickListener(Drawer_Handwrite.this);
        iv_SHStaff.setOnClickListener(Drawer_Handwrite.this);
        iv_SRRelationship.setOnClickListener(Drawer_Handwrite.this);
        iv_SRelationship.setOnClickListener(Drawer_Handwrite.this);

        for(int i = 0; i < cb_IClassification_Name.length; i++){
            cb_IClassification_Name[i].setOnCheckedChangeListener(Drawer_Handwrite.this);
        }
        for(int i = 0; i < cb_RHospital_Name.length; i++){
            cb_RHospital_Name[i].setOnCheckedChangeListener(Drawer_Handwrite.this);
        }

        sp_RRelationship.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            public void onItemSelected(AdapterView adapterView, View view, int position, long id) {
                if (adapterView.getSelectedItemPosition() != 0 & adapterView.getSelectedItem().equals("其他")) {
                    sp_RRelationship.setVisibility(View.GONE);
                    et_RRelationship.setVisibility(View.VISIBLE);
                }
            }

            public void onNothingSelected(AdapterView arg0) {

            }
        });

        et_RRelationship.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                sp_RRelationship.setVisibility(View.VISIBLE);
                et_RRelationship.setVisibility(View.GONE);
                return true;
            }
        });

        sp_Relationship.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            public void onItemSelected(AdapterView adapterView, View view, int position, long id) {
                if (adapterView.getSelectedItemPosition() != 0 & adapterView.getSelectedItem().equals("其他")) {
                    sp_Relationship.setVisibility(View.GONE);
                    et_Relationship.setVisibility(View.VISIBLE);
                }
            }

            public void onNothingSelected(AdapterView arg0) {

            }
        });

        et_Relationship.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                sp_Relationship.setVisibility(View.VISIBLE);
                et_Relationship.setVisibility(View.GONE);
                return true;
            }
        });
    }

    /**
     * 參數帶入
     */
    private void Modify() {
        et_AStaffNO1.setText(gv.AStaffNO1);
        et_AStaffNO2.setText(gv.AStaffNO2);
        et_AStaffNO3.setText(gv.AStaffNO3);
        et_AStaffNO4.setText(gv.AStaffNO4);
        et_AStaffNO5.setText(gv.AStaffNO5);
        et_AStaffNO6.setText(gv.AStaffNO6);


        for (int i = 0; i < cb_IClassification_Name.length; i++) {
            cb_IClassification_Name[i].setChecked(false);
            if(!gv.IClassification.equals("")) {
                if (cb_IClassification_Name[i].getText().toString().equals(gv.IClassification)) {
                    cb_IClassification_Name[i].setChecked(true);
                }
            }
        }

        cb_RHospital.setChecked(gv.cb_RHospital);

        for (int i = 0; i < cb_RHospital_Name.length; i++) {
            cb_RHospital_Name[i].setChecked(false);
            if(!gv.RHospital.equals("")) {
                if (cb_RHospital_Name[i].getText().toString().equals(gv.RHospital)) {
                    cb_RHospital_Name[i].setChecked(true);
                }
            }
        }

        sp_RRelationship.setVisibility(View.VISIBLE);
        et_RRelationship.setVisibility(View.GONE);
        sp_RRelationship.setSelection(gv.PRRelationship);
        if(sp_RRelationship.getSelectedItem().equals("其他")) {
            et_RRelationship.setText(gv.RRelationship);
        }

        sp_Relationship.setVisibility(View.VISIBLE);
        et_Relationship.setVisibility(View.GONE);
        sp_Relationship.setSelection(gv.PRelationship);
        if(sp_Relationship.getSelectedItem().equals("其他")) {
            et_Relationship.setText(gv.Relationship);
        }

        et_Phone.setText(gv.Phone);

        if(gv.I_SNote != null){
            iv_SNote.setImageBitmap(gv.I_SNote);
            iv_SNote.setVisibility(View.VISIBLE);
            tv_SNote.setVisibility(View.GONE);
            ImageViewSize(iv_SNote, 625, 250);
        }
        else{
            iv_SNote.setVisibility(View.GONE);
            tv_SNote.setVisibility(View.VISIBLE);
        }

        if(gv.I_SAStaff1 != null){
            iv_SAStaff1.setImageBitmap(gv.I_SAStaff1);
            iv_SAStaff1.setVisibility(View.VISIBLE);
            tv_SAStaff1.setVisibility(View.GONE);
            ImageViewSize(iv_SAStaff1, 625, 250);
        }
        else{
            iv_SAStaff1.setVisibility(View.GONE);
            tv_SAStaff1.setVisibility(View.VISIBLE);
        }

        if(gv.I_SAStaff2 != null){
            iv_SAStaff2.setImageBitmap(gv.I_SAStaff2);
            iv_SAStaff2.setVisibility(View.VISIBLE);
            tv_SAStaff2.setVisibility(View.GONE);
            ImageViewSize(iv_SAStaff2, 625, 250);
        }
        else{
            iv_SAStaff2.setVisibility(View.GONE);
            tv_SAStaff2.setVisibility(View.VISIBLE);
        }

        if(gv.I_SAStaff3 != null){
            iv_SAStaff3.setImageBitmap(gv.I_SAStaff3);
            iv_SAStaff3.setVisibility(View.VISIBLE);
            tv_SAStaff3.setVisibility(View.GONE);
            ImageViewSize(iv_SAStaff3, 625, 250);
        }
        else{
            iv_SAStaff3.setVisibility(View.GONE);
            tv_SAStaff3.setVisibility(View.VISIBLE);
        }

        if(gv.I_SAStaff4 != null){
            iv_SAStaff4.setImageBitmap(gv.I_SAStaff4);
            iv_SAStaff4.setVisibility(View.VISIBLE);
            tv_SAStaff4.setVisibility(View.GONE);
            ImageViewSize(iv_SAStaff4, 625, 250);
        }
        else{
            iv_SAStaff4.setVisibility(View.GONE);
            tv_SAStaff4.setVisibility(View.VISIBLE);
        }

        if(gv.I_SAStaff5 != null){
            iv_SAStaff5.setImageBitmap(gv.I_SAStaff5);
            iv_SAStaff5.setVisibility(View.VISIBLE);
            tv_SAStaff5.setVisibility(View.GONE);
            ImageViewSize(iv_SAStaff5, 625, 250);
        }
        else{
            iv_SAStaff5.setVisibility(View.GONE);
            tv_SAStaff5.setVisibility(View.VISIBLE);
        }

        if(gv.I_SAStaff6 != null){
            iv_SAStaff6.setImageBitmap(gv.I_SAStaff6);
            iv_SAStaff6.setVisibility(View.VISIBLE);
            tv_SAStaff6.setVisibility(View.GONE);
            ImageViewSize(iv_SAStaff6, 625, 250);
        }
        else{
            iv_SAStaff6.setVisibility(View.GONE);
            tv_SAStaff6.setVisibility(View.VISIBLE);
        }

        if(gv.I_SHStaff != null){
            iv_SHStaff.setImageBitmap(gv.I_SHStaff);
            iv_SHStaff.setVisibility(View.VISIBLE);
            tv_SHStaff.setVisibility(View.GONE);
            ImageViewSize(iv_SHStaff, 625, 250);
        }
        else{
            iv_SHStaff.setVisibility(View.GONE);
            tv_SHStaff.setVisibility(View.VISIBLE);
        }

        if(gv.I_SRRelationship != null){
            iv_SRRelationship.setImageBitmap(gv.I_SRRelationship);
            iv_SRRelationship.setVisibility(View.VISIBLE);
            tv_SRRelationship.setVisibility(View.GONE);
            ImageViewSize(iv_SRRelationship, 625, 250);
        }
        else{
            iv_SRRelationship.setVisibility(View.GONE);
            tv_SRRelationship.setVisibility(View.VISIBLE);
        }

        if(gv.I_SRelationship != null){
            iv_SRelationship.setImageBitmap(gv.I_SRelationship);
            iv_SRelationship.setVisibility(View.VISIBLE);
            tv_SRelationship.setVisibility(View.GONE);
            ImageViewSize(iv_SRelationship, 625, 250);
        }
        else{
            iv_SRelationship.setVisibility(View.GONE);
            tv_SRelationship.setVisibility(View.VISIBLE);
        }
    }

    /**
     *  參數儲存
     */
    private void Save() {
        gv.AStaffNO1 = et_AStaffNO1.getText().toString();
        gv.AStaffNO2 = et_AStaffNO2.getText().toString();
        gv.AStaffNO3 = et_AStaffNO3.getText().toString();
        gv.AStaffNO4 = et_AStaffNO4.getText().toString();
        gv.AStaffNO5 = et_AStaffNO5.getText().toString();
        gv.AStaffNO6 = et_AStaffNO6.getText().toString();

        gv.IClassification = "";
        for (int i = 0; i < cb_IClassification_Name.length; i++) {
            if (cb_IClassification_Name[i].isChecked()) {
                gv.IClassification = cb_IClassification_Name[i].getText().toString();
            }
        }

        gv.cb_RHospital = false;
        if (cb_RHospital.isChecked()) {
            gv.cb_RHospital = true;
        }

        gv.RHospital = "";
        for (int i = 0; i < cb_RHospital_Name.length; i++) {
            if (cb_RHospital_Name[i].isChecked()) {
                gv.RHospital = cb_RHospital_Name[i].getText().toString();
            }
        }

        gv.PRRelationship = sp_RRelationship.getSelectedItemPosition();
        gv.RRelationship = "";
        if (sp_RRelationship.getSelectedItemPosition() != 0) {
            if (sp_RRelationship.getSelectedItem().equals("其他")) {
                gv.RRelationship = et_RRelationship.getText().toString();
            } else {
                gv.RRelationship = sp_RRelationship.getSelectedItem().toString();
            }
        }

        gv.PRelationship = sp_Relationship.getSelectedItemPosition();
        gv.Relationship = "";
        if (sp_Relationship.getSelectedItemPosition() != 0) {
            if (sp_Relationship.getSelectedItem().equals("其他")) {
                gv.Relationship = et_Relationship.getText().toString();
            } else {
                gv.Relationship = sp_Relationship.getSelectedItem().toString();
            }
        }

        gv.Phone = et_Phone.getText().toString();
    }

    /**
     *  參數儲存
     */
    private void DBSave() {
        SQLite SQLite = new SQLite(Drawer_Handwrite.this);
        SQLiteDatabase DB = SQLite.getWritableDatabase();
        ContentValues cv  = new ContentValues();
        ByteArrayOutputStream out;
        cv.put("SNote", gv.SNote);
        if(gv.I_SNote != null) {
            out = new ByteArrayOutputStream();
            gv.I_SNote.compress(Bitmap.CompressFormat.JPEG, 50, out);
            cv.put("I_SNote", out.toByteArray());
        }
        cv.put("AStaffNO1", gv.AStaffNO1);
        cv.put("SAStaff1", gv.SAStaff1);
        if(gv.I_SAStaff1 != null) {
            out = new ByteArrayOutputStream();
            gv.I_SAStaff1.compress(Bitmap.CompressFormat.JPEG, 50, out);
            cv.put("I_SAStaff1", out.toByteArray());
        }
        cv.put("AStaffNO2", gv.AStaffNO2);
        cv.put("SAStaff2", gv.SAStaff2);
        if(gv.I_SAStaff2 != null) {
            out = new ByteArrayOutputStream();
            gv.I_SAStaff2.compress(Bitmap.CompressFormat.JPEG, 50, out);
            cv.put("I_SAStaff2", out.toByteArray());
        }
        cv.put("AStaffNO3", gv.AStaffNO3);
        cv.put("SAStaff3", gv.SAStaff3);
        if(gv.I_SAStaff3 != null) {
            out = new ByteArrayOutputStream();
            gv.I_SAStaff3.compress(Bitmap.CompressFormat.JPEG, 50, out);
            cv.put("I_SAStaff3", out.toByteArray());
        }
        cv.put("AStaffNO4", gv.AStaffNO4);
        cv.put("SAStaff4", gv.SAStaff4);
        if(gv.I_SAStaff4 != null) {
            out = new ByteArrayOutputStream();
            gv.I_SAStaff4.compress(Bitmap.CompressFormat.JPEG, 50, out);
            cv.put("I_SAStaff4", out.toByteArray());
        }
        cv.put("AStaffNO5", gv.AStaffNO5);
        cv.put("SAStaff5", gv.SAStaff5);
        if(gv.I_SAStaff5 != null) {
            out = new ByteArrayOutputStream();
            gv.I_SAStaff5.compress(Bitmap.CompressFormat.JPEG, 50, out);
            cv.put("I_SAStaff5", out.toByteArray());
        }
        cv.put("AStaffNO6", gv.AStaffNO6);
        cv.put("SAStaff6", gv.SAStaff6);
        if(gv.I_SAStaff6 != null) {
            out = new ByteArrayOutputStream();
            gv.I_SAStaff6.compress(Bitmap.CompressFormat.JPEG, 50, out);
            cv.put("I_SAStaff6", out.toByteArray());
        }
        cv.put("IClassification", gv.IClassification);
        cv.put("SHStaff", gv.SHStaff);
        if(gv.I_SHStaff != null) {
            out = new ByteArrayOutputStream();
            gv.I_SHStaff.compress(Bitmap.CompressFormat.JPEG, 50, out);
            cv.put("I_SHStaff", out.toByteArray());
        }
        cv.put("cb_RHospital", gv.cb_RHospital);
        cv.put("RHospital", gv.RHospital);
        cv.put("RRelationship", gv.RRelationship);
        cv.put("PRRelationship", gv.PRRelationship);
        cv.put("SRRelationship", gv.SRRelationship);
        if(gv.I_SRRelationship != null) {
            out = new ByteArrayOutputStream();
            gv.I_SRRelationship.compress(Bitmap.CompressFormat.JPEG, 50, out);
            cv.put("I_SRRelationship", out.toByteArray());
        }
        cv.put("Relationship", gv.Relationship);
        cv.put("PRelationship", gv.PRelationship);
        cv.put("Phone", gv.Phone);
        cv.put("SRelationship", gv.SRelationship);
        if(gv.I_SRelationship != null) {
            out = new ByteArrayOutputStream();
            gv.I_SRelationship.compress(Bitmap.CompressFormat.JPEG, 50, out);
            cv.put("I_SRelationship", out.toByteArray());
        }
        DB.update("MainHandWrite", cv, "CaseID=? AND Subno=?", new String[]{gv.FNumber, gv.DrawerItemID});
        SQLite.close();
        DB.close();
    }

    public void onClick(View v) {
        if (v == tv_SNote || v == iv_SNote) {
            WritePadDialog writeTabletDialog = new WritePadDialog(Drawer_Handwrite.this, gv.screenWidth, gv.screenHeight, new WritePadDialog.DialogListener() {
                @Override
                public void refreshActivity(Object object) {
                    mSignBitmap = (Bitmap) object;
                    signPath = fileService.saveBitmapToSDCard(FileName() + "_main_SNote.jpg", mSignBitmap, "HandWrite");
                    iv_SNote.setImageBitmap(mSignBitmap);
                    iv_SNote.setVisibility(View.VISIBLE);
                    tv_SNote.setVisibility(View.GONE);
                    String html = "<img style=\"width:50pt;height:20pt;margin-bottom:-8px;\" src=\"" + "file://" + signPath + "\">";
                    gv.SNote = html;
                    gv.I_SNote = mSignBitmap;
                    ImageViewSize(iv_SNote, 625, 250);
                }
            });
            writeTabletDialog.show();
        }
        else if (v == tv_SAStaff1 || v == iv_SAStaff1) {
            WritePadDialog writeTabletDialog = new WritePadDialog(Drawer_Handwrite.this, gv.screenWidth, gv.screenHeight, new WritePadDialog.DialogListener() {
                @Override
                public void refreshActivity(Object object) {
                    mSignBitmap = (Bitmap) object;
                    signPath = fileService.saveBitmapToSDCard(FileName() + "_main_SAStaff1.jpg", mSignBitmap, "HandWrite");
                    iv_SAStaff1.setImageBitmap(mSignBitmap);
                    iv_SAStaff1.setVisibility(View.VISIBLE);
                    tv_SAStaff1.setVisibility(View.GONE);
                    String html = "<img style=\"width:40pt;height:16pt;\" src=\"" + "file://" + signPath + "\">";
                    gv.SAStaff1 = html;
                    gv.I_SAStaff1 = mSignBitmap;
                    ImageViewSize(iv_SAStaff1, 625, 250);
                }
            });
            writeTabletDialog.show();
        }
        else if (v == tv_SAStaff2 || v == iv_SAStaff2) {
            WritePadDialog writeTabletDialog = new WritePadDialog(Drawer_Handwrite.this, gv.screenWidth, gv.screenHeight, new WritePadDialog.DialogListener() {
                @Override
                public void refreshActivity(Object object) {
                    mSignBitmap = (Bitmap) object;
                    signPath = fileService.saveBitmapToSDCard(FileName() + "_main_SAStaff2.jpg", mSignBitmap, "HandWrite");
                    iv_SAStaff2.setImageBitmap(mSignBitmap);
                    iv_SAStaff2.setVisibility(View.VISIBLE);
                    tv_SAStaff2.setVisibility(View.GONE);
                    String html = "<img style=\"width:40pt;height:16pt;\" src=\"" + "file://" + signPath + "\">";
                    gv.SAStaff2 = html;
                    gv.I_SAStaff2 = mSignBitmap;
                    ImageViewSize(iv_SAStaff2, 625, 250);
                }
            });
            writeTabletDialog.show();
        }
        else if (v == tv_SAStaff3 || v == iv_SAStaff3) {
            WritePadDialog writeTabletDialog = new WritePadDialog(Drawer_Handwrite.this, gv.screenWidth, gv.screenHeight, new WritePadDialog.DialogListener() {
                @Override
                public void refreshActivity(Object object) {
                    mSignBitmap = (Bitmap) object;
                    signPath = fileService.saveBitmapToSDCard(FileName() + "_main_SAStaff3.jpg", mSignBitmap, "HandWrite");
                    iv_SAStaff3.setImageBitmap(mSignBitmap);
                    iv_SAStaff3.setVisibility(View.VISIBLE);
                    tv_SAStaff3.setVisibility(View.GONE);
                    String html = "<img style=\"width:40pt;height:16pt;\" src=\"" + "file://" + signPath + "\">";
                    gv.SAStaff3 = html;
                    gv.I_SAStaff3 = mSignBitmap;
                    ImageViewSize(iv_SAStaff3, 625, 250);
                }
            });
            writeTabletDialog.show();
        }
        else if (v == tv_SAStaff4 || v == iv_SAStaff4) {
            WritePadDialog writeTabletDialog = new WritePadDialog(Drawer_Handwrite.this, gv.screenWidth, gv.screenHeight, new WritePadDialog.DialogListener() {
                @Override
                public void refreshActivity(Object object) {
                    mSignBitmap = (Bitmap) object;
                    signPath = fileService.saveBitmapToSDCard(FileName() + "_main_SAStaff4.jpg", mSignBitmap, "HandWrite");
                    iv_SAStaff4.setImageBitmap(mSignBitmap);
                    iv_SAStaff4.setVisibility(View.VISIBLE);
                    tv_SAStaff4.setVisibility(View.GONE);
                    String html = "<img style=\"width:40pt;height:16pt;\" src=\"" + "file://" + signPath + "\">";
                    gv.SAStaff4 = html;
                    gv.I_SAStaff4 = mSignBitmap;
                    ImageViewSize(iv_SAStaff4, 625, 250);
                }
            });
            writeTabletDialog.show();
        }
        else if (v == tv_SAStaff5 || v == iv_SAStaff5) {
            WritePadDialog writeTabletDialog = new WritePadDialog(Drawer_Handwrite.this, gv.screenWidth, gv.screenHeight, new WritePadDialog.DialogListener() {
                @Override
                public void refreshActivity(Object object) {
                    mSignBitmap = (Bitmap) object;
                    signPath = fileService.saveBitmapToSDCard(FileName() + "_main_SAStaff5.jpg", mSignBitmap, "HandWrite");
                    iv_SAStaff5.setImageBitmap(mSignBitmap);
                    iv_SAStaff5.setVisibility(View.VISIBLE);
                    tv_SAStaff5.setVisibility(View.GONE);
                    String html = "<img style=\"width:40pt;height:16pt;\" src=\"" + "file://" + signPath + "\">";
                    gv.SAStaff5 = html;
                    gv.I_SAStaff5 = mSignBitmap;
                    ImageViewSize(iv_SAStaff5, 625, 250);
                }
            });
            writeTabletDialog.show();
        }
        else if (v == tv_SAStaff6 || v == iv_SAStaff6) {
            WritePadDialog writeTabletDialog = new WritePadDialog(Drawer_Handwrite.this, gv.screenWidth, gv.screenHeight, new WritePadDialog.DialogListener() {
                @Override
                public void refreshActivity(Object object) {
                    mSignBitmap = (Bitmap) object;
                    signPath = fileService.saveBitmapToSDCard(FileName() + "_main_SAStaff6.jpg", mSignBitmap, "HandWrite");
                    iv_SAStaff6.setImageBitmap(mSignBitmap);
                    iv_SAStaff6.setVisibility(View.VISIBLE);
                    tv_SAStaff6.setVisibility(View.GONE);
                    String html = "<img style=\"width:40pt;height:16pt;\" src=\"" + "file://" + signPath + "\">";
                    gv.SAStaff6 = html;
                    gv.I_SAStaff6 = mSignBitmap;
                    ImageViewSize(iv_SAStaff6, 625, 250);
                }
            });
            writeTabletDialog.show();
        }
        else if (v == tv_SHStaff || v == iv_SHStaff) {
            WritePadDialog writeTabletDialog = new WritePadDialog(Drawer_Handwrite.this, gv.screenWidth, gv.screenHeight, new WritePadDialog.DialogListener() {
                @Override
                public void refreshActivity(Object object) {
                    mSignBitmap = (Bitmap) object;
                    signPath = fileService.saveBitmapToSDCard(FileName() + "_main_SHStaff.jpg", mSignBitmap, "HandWrite");
                    iv_SHStaff.setImageBitmap(mSignBitmap);
                    iv_SHStaff.setVisibility(View.VISIBLE);
                    tv_SHStaff.setVisibility(View.GONE);
                    String html = "<img style=\"width:50pt;height:20pt;margin-bottom:-8px;\" src=\"" + "file://" + signPath + "\">";
                    gv.SHStaff = html;
                    gv.I_SHStaff = mSignBitmap;
                    ImageViewSize(iv_SHStaff, 625, 250);
                }
            });
            writeTabletDialog.show();
        }
        else if (v == tv_SRRelationship || v == iv_SRRelationship) {
            WritePadDialog writeTabletDialog = new WritePadDialog(Drawer_Handwrite.this, gv.screenWidth, gv.screenHeight, new WritePadDialog.DialogListener() {
                @Override
                public void refreshActivity(Object object) {
                    mSignBitmap = (Bitmap) object;
                    signPath = fileService.saveBitmapToSDCard(FileName() + "_main_SRRelationship.jpg", mSignBitmap, "HandWrite");
                    iv_SRRelationship.setImageBitmap(mSignBitmap);
                    iv_SRRelationship.setVisibility(View.VISIBLE);
                    tv_SRRelationship.setVisibility(View.GONE);
                    String html = "<img style=\"width:50pt;height:20pt;margin-bottom:-8px;\" src=\"" + "file://" + signPath + "\">";
                    gv.SRRelationship = html;
                    gv.I_SRRelationship = mSignBitmap;
                    ImageViewSize(iv_SRRelationship, 625, 250);
                }
            });
            writeTabletDialog.show();
        }
        else if (v == tv_SRelationship || v == iv_SRelationship) {
            WritePadDialog writeTabletDialog = new WritePadDialog(Drawer_Handwrite.this, gv.screenWidth, gv.screenHeight, new WritePadDialog.DialogListener() {
                @Override
                public void refreshActivity(Object object) {
                    mSignBitmap = (Bitmap) object;
                    signPath = fileService.saveBitmapToSDCard(FileName() + "_main_SRelationship.jpg", mSignBitmap, "HandWrite");
                    iv_SRelationship.setImageBitmap(mSignBitmap);
                    iv_SRelationship.setVisibility(View.VISIBLE);
                    tv_SRelationship.setVisibility(View.GONE);
                    String html = "<img style=\"width:50pt;height:20pt;margin-bottom:-8px;\" src=\"" + "file://" + signPath + "\">";
                    gv.SRelationship = html;
                    gv.I_SRelationship = mSignBitmap;
                    ImageViewSize(iv_SRelationship, 625, 250);
                }
            });
            writeTabletDialog.show();
        }
    }

    public void onCheckedChanged(CompoundButton v, boolean isChecked) {
        for(int i = 0; i < cb_IClassification_Name.length; i++) {
            if (v == cb_IClassification_Name[i]) {
                for (int j = 0; j < cb_IClassification_Name.length; j++) {
                    cb_IClassification_Name[j].setChecked(false);
                }
                if (isChecked) {
                    v.setChecked(true);
                } else {
                    v.setChecked(false);
                }
                break;
            }
        }
        for(int i = 0; i < cb_RHospital_Name.length; i++) {
            if (v == cb_RHospital_Name[i]) {
                for (int j = 0; j < cb_RHospital_Name.length; j++) {
                    cb_RHospital_Name[j].setChecked(false);
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

        SQLite SQLite = new SQLite(Drawer_Handwrite.this);
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
                Drawer_Handwrite.this,
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
                Drawer_Handwrite.this,
                R.layout.listview_function,
                new int[]{R.drawable.function_add, R.drawable.function_navigation, R.drawable.function_preview},
                new String[]{"新增表單", "導航", "預覽"},
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

            PDialog = ProgressDialog.show(Drawer_Handwrite.this, "請稍等...", "切換表單中...", true);

            Save();
            DBSave();

            gv.DrawerItemID = data.get("Subno").toString();
            gv.DrawerItemPosition = position;

            new Thread(R_SwitchTable).start();
        }
    }

    private void selectItem(int position) {
        Intent intent = new Intent();
        switch (position){
            case 0:
                PDialog = ProgressDialog.show(Drawer_Handwrite.this, "請稍等...", "建立表單中...", true);
                new Thread(R_NewTable).start();
                break;
            case 1:
                Save();
                DBSave();

                int sdk = android.os.Build.VERSION.SDK_INT;
                if(sdk < android.os.Build.VERSION_CODES.HONEYCOMB) {
                    android.text.ClipboardManager clipboard = (android.text.ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                    clipboard.setText(gv.Coordinate);
                } else {
                    android.content.ClipboardManager clipboard = (android.content.ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                    android.content.ClipData clip = android.content.ClipData.newPlainText("Coordinate",gv.Coordinate);
                    clipboard.setPrimaryClip(clip);
                }
                intent.setClass(Drawer_Handwrite.this, Drawer_Navigation.class);
                startActivity(intent);
                Drawer_Handwrite.this.finish();
                break;
            case 2:
                Save();
                DBSave();

                intent.setClass(Drawer_Handwrite.this, Drawer_Preview.class);
                startActivity(intent);
                Drawer_Handwrite.this.finish();
                break;
            default:
                break;
        }
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

    public String FileName() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        Date curDate = new Date(System.currentTimeMillis()); // 獲取當前時間
        return formatter.format(curDate);
    }
    /**
     * 返回上一頁
     *
     * @return
     */
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            Save();
            DBSave();
            Intent intent = new Intent();
            intent.setClass(Drawer_Handwrite.this, MainActivity.class);
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
                    Drawer_Handwrite.this,
                    listItem,
                    R.layout.listview_subno,
                    gv.DrawerItemPosition,
                    new String[]{"SubnoID"},
                    new int[]{R.id.iv_Select, R.id.tv_SubnoID, R.id.btn_Delete}
            );

            lv_Drawer_Subno.setAdapter(SubnoAdapter);

            Modify();

            // 關掉
            dl_Drawer.closeDrawer(ll_Drawer);
            PDialog.dismiss();
        }
    };

    Runnable R_NewTable = new Runnable() {
        @Override
        public void run() {
            // TODO: http request.
            SQLite SQLite = new SQLite(Drawer_Handwrite.this);
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
                    Drawer_Handwrite.this,
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
