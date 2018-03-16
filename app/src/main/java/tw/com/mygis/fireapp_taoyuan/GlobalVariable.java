package tw.com.mygis.fireapp_taoyuan;

import android.app.Application;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
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

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import library.JSONParser;

/**
 * Created by choey on 2014/4/9.
 */



public class GlobalVariable extends Application {


    static JSONObject jObj = null;
    static String jsons = "";

    private JSONParser jsonParser;

    // constructor
    public GlobalVariable() {
        jsonParser = new JSONParser();
    }


    public int screenWidth, screenHeight;
    public boolean Modify_Tab1, Modify_Tab2, Modify_Tab3, Modify_Tab4, Modify_Tab5;
    public int main_currIndex;//目前頁面
    public int DrawerItemPosition;//目前表單
    public String DrawerItemID;//目前表單
    public String PreviewMain;
    public String CarID01;//隊
    public String CarID;//分隊
    public String Coordinate;//座標

    /**
     * 登入
     */

    /**
     * 基本資料
     */
    //通報內容
    public int PHospitalAddress, PArea, PRCustody;
    public String FNumber, LoadPDF, Date, AttendanceUnit, AcceptedUnit, LocationHappen, AssistanceUnit, HospitalAddress, ReasonHospital, MHospital,Car_Index;
    //時間填寫
    public String Time1, Time2, Time3, Time4, Time5, Time6;
    //病患資料
    public String Name, Age, Sex, ID, Area, Address, Property, RCustody, Custody, SCustody;
    public boolean Critical, AgeM, AgeA;
    public Bitmap I_SCustody;
    public String[] Hospital;

    /**
     * 現場狀況
     */
    public String[] Trauma, Trauma1, Traffic, NTrauma, NTrauma6;
    public String Trauma_Switch, Witnesses, CPR, PAD, ROSC, PSpecies, Stroke, LTime;
    public boolean Smile, LArm, Speech;

    /**
     * 急救處置
     */
    //急救處置
    public int PPosture;
    public String[] Breathing, TDisposal, CPRDisposal, CPRDisposal3, DDisposal, DDisposal1, ODisposal, ALSDisposal;
    public String Posture;
    //給藥
    public int PDrugUseName1 = 0, PDrugUseDose1 = 0;
    public String DrugUseTime1, DrugUseName1, DrugUseDose1, DrugUser1;
    public int PDrugUseName2 = 0, PDrugUseDose2 = 0;
    public String DrugUseTime2, DrugUseName2, DrugUseDose2, DrugUser2;
    public int PDrugUseName3 = 0, PDrugUseDose3 = 0;
    public String DrugUseTime3, DrugUseName3, DrugUseDose3, DrugUser3;
    public int PDrugUseName4 = 0, PDrugUseDose4 = 0;
    public String DrugUseTime4, DrugUseName4, DrugUseDose4, DrugUser4;

    /**
     * 病患相關
     */
    //病患主訴
    public String Q1, Q2, Q3, Q4, Q5;
    public String[] MHistory, Allergies;
    //補述
    public String People, SPeople;
    public boolean[] Switch_People;

    /**
     * 生命徵象
     */
    //生命徵象1
    public int PConsciousness1, PBPressure1_A;
    public String LifeTime1, Consciousness1, LifeBreathing1, Pulse1, GCS1_E, GCS1_V, GCS1_M, SBP1, DBP1, BPressure1_A, SpO21, Temperature1;
    //生命徵象2
    public int PConsciousness2, PBPressure2_A;
    public String LifeTime2, Consciousness2, LifeBreathing2, Pulse2, GCS2_E, GCS2_V, GCS2_M, SBP2, DBP2, BPressure2_A, SpO22, Temperature2;
    //生命徵象3
    public int PConsciousness3, PBPressure3_A;
    public String Consciousness3, LifeBreathing3, Pulse3, GCS3_E, GCS3_V, GCS3_M, SBP3, DBP3, BPressure3_A, SpO23, Temperature3;

    /**
     * 手寫簽名
     */
    public int PRRelationship, PRelationship;
    public String AStaffNO1, AStaffNO2, AStaffNO3, AStaffNO4, AStaffNO5, AStaffNO6, IClassification, RHospital, RRelationship, Relationship, Phone;
    public String SNote, SAStaff1, SAStaff2, SAStaff3, SAStaff4, SAStaff5, SAStaff6, SHStaff, SRRelationship, SRelationship;
    public Bitmap I_SNote, I_SAStaff1, I_SAStaff2, I_SAStaff3, I_SAStaff4, I_SAStaff5, I_SAStaff6, I_SHStaff, I_SRRelationship, I_SRelationship;
    public boolean cb_RHospital;

    public boolean Initialization() {

        screenWidth = 0; screenHeight = 0;
        Modify_Tab1 = false;Modify_Tab2 = false;Modify_Tab3 = false;Modify_Tab4 = false;Modify_Tab5 = false;
        main_currIndex = 0;//目前頁面
        DrawerItemPosition = 0;
        DrawerItemID = "";
        PreviewMain = "";

        /**
         * 登入
         */

        /**
         * 基本資料
         */
        //通報內容
        PHospitalAddress = 0;PArea = 0;PRCustody = 0;
        FNumber = "";LoadPDF = "";Date = "";AttendanceUnit = "";AcceptedUnit = "";LocationHappen = "";AssistanceUnit = "";HospitalAddress = "";ReasonHospital = "";MHospital = "";
        //時間填寫
        Time1 = "";Time2 = "";Time3 = "";Time4 = "";Time5 = "";Time6 = "";
        //病患資料
        Name = "";Age = "";Sex = "";ID = "";Area = "";Address = "";Property = "";RCustody = "";Custody = "";SCustody = "";
        Critical = false;AgeM = false; AgeA = false;
        I_SCustody = null;

        /**
         * 現場狀況
         */
        Trauma = new String[] { "", "", "", "", "", "", "", "", "" };
        Trauma1 = new String[] { "", "", "", "", "" };
        Traffic = new String[] { "", "", "", "", "" };
        NTrauma = new String[] { "", "", "", "", "", "", "", "", "", "", "", "", "", "", "" };
        NTrauma6 = new String[] { "", "", "", "" };
        Trauma_Switch = "";Witnesses = "";CPR = "";PAD = "";ROSC = "";PSpecies = "";Stroke = "";LTime = "";
        Smile = false;LArm = false;Speech = false;

        /**
         * 急救處置
         */
        //急救處置
        PPosture = 0;
        Breathing = new String[] { "", "", "", "", "", "", "", "", "", "" };
        TDisposal = new String[] { "", "", "", "", "", "", "", "" };
        CPRDisposal = new String[] { "", "", "" };
        CPRDisposal3 = new String[] { "", "" };
        DDisposal = new String[] { "", "", "", "" };
        DDisposal1 = new String[] { "", "", "" };
        ODisposal = new String[] { "", "", "", "", "" };
        ALSDisposal = new String[] { "", "" };
        Posture = "";
        //給藥
        PDrugUseName1 = 0; PDrugUseDose1 = 0;
        DrugUseTime1 = "";DrugUseName1 = "";DrugUseDose1 = "";DrugUser1 = "";
        PDrugUseName2 = 0; PDrugUseDose2 = 0;
        DrugUseTime2 = "";DrugUseName2 = "";DrugUseDose2 = "";DrugUser2 = "";
        PDrugUseName3 = 0; PDrugUseDose3 = 0;
        DrugUseTime3 = "";DrugUseName3 = "";DrugUseDose3 = "";DrugUser3 = "";
        PDrugUseName4 = 0; PDrugUseDose4 = 0;
        DrugUseTime4 = "";DrugUseName4 = "";DrugUseDose4 = "";DrugUser4 = "";

        /**
         * 病患相關
         */
        //病患主訴
        Q1 = "";Q2 = "";Q3 = "";Q4 = "";Q5 = "";
        MHistory = new String[] { "", "", "", "", "", "", "", "", "", "", "", "", "" };
        Allergies = new String[] { "", "", "", "" };
        //補述
        People = "";SPeople = "";
        Switch_People = new boolean[] { false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false};

        /**
         * 生命徵象
         */
        //生命徵象1
        PConsciousness1 = 0; PBPressure1_A = 0;
        LifeTime1 = "";Consciousness1 = "";LifeBreathing1 = "";Pulse1 = "";GCS1_E = "";GCS1_V = "";GCS1_M = "";SBP1 = "";DBP1 = "";BPressure1_A = "";SpO21 = "";Temperature1 = "";
        //生命徵象2
        PConsciousness2 = 0; PBPressure2_A = 0;
        LifeTime2 = "";Consciousness2 = "";LifeBreathing2 = "";Pulse2 = "";GCS2_E = "";GCS2_V = "";GCS2_M = "";SBP2 = "";DBP2 = "";BPressure2_A = "";SpO22 = "";Temperature2 = "";
        //生命徵象3
        PConsciousness3 = 0; PBPressure3_A = 0;
        Consciousness3 = "";LifeBreathing3 = "";Pulse3 = "";GCS3_E = "";GCS3_V = "";GCS3_M = "";SBP3 = "";DBP3 = "";BPressure3_A = "";SpO23 = "";Temperature3 = "";

        /**
         * 手寫簽名
         */
        PRRelationship = 0;PRelationship = 0;
        AStaffNO1 = "";AStaffNO2 = "";AStaffNO3 = "";AStaffNO4 = "";AStaffNO5 = "";AStaffNO6 = "";IClassification = "";RHospital = "";RRelationship = "";Relationship = "";Phone = "";
        SNote = "";SAStaff1 = "";SAStaff2 = "";SAStaff3 = "";SAStaff4 = "";SAStaff5 = "";SAStaff6 = "";SHStaff = "";SRRelationship = "";SRelationship = "";
        I_SNote = null;I_SAStaff1 = null;I_SAStaff2 = null;I_SAStaff3 = null;I_SAStaff4 = null;I_SAStaff5 = null;I_SAStaff6 = null;I_SHStaff = null;I_SRRelationship = null;I_SRelationship = null;
        cb_RHospital = false;

        return true;
    }

    public boolean SwitchingForm() {

        byte[] buf;

        SQLite SQLite = new SQLite(GlobalVariable.this);
        SQLiteDatabase DB = SQLite.getWritableDatabase();
        String[] colum = { "CaseID", "LoadPDF", "Critical", "Date", "AttendanceUnit", "AcceptedUnit", "LocationHappen", "AssistanceUnit", "HospitalAddress", "PHospitalAddress", "ReasonHospital", "MHospital", "Time1", "Time2", "Time3", "Time4", "Time5", "Time6", "Name", "AgeM", "AgeA", "Age", "Sex", "Identity", "Area", "PArea", "Address", "Property", "RCustody", "PRCustody", "Custody", "SCustody", "I_SCustody" };
        String[] parameter = {FNumber, DrawerItemID};
        Cursor c = DB.query("MainTab1", colum, "CaseID=? AND Subno=?", parameter, null, null, "id ASC");
        c.moveToFirst();
        FNumber = c.getString(0);
        LoadPDF = c.getString(1);
        Critical = (c.getInt(2) == 1);
        Date = c.getString(3);
        AttendanceUnit = c.getString(4);
        AcceptedUnit = c.getString(5);
        LocationHappen = c.getString(6);
        AssistanceUnit = c.getString(7);
        HospitalAddress = c.getString(8);
        PHospitalAddress = c.getInt(9);
        ReasonHospital = c.getString(10);
        MHospital = c.getString(11);
        Time1 = c.getString(12);
        Time2 = c.getString(13);
        Time3 = c.getString(14);
        Time4 = c.getString(15);
        Time5 = c.getString(16);
        Time6 = c.getString(17);
        Name = c.getString(18);
        AgeM = (c.getInt(19) == 1);
        AgeA = (c.getInt(20) == 1);
        Age = c.getString(21);
        Sex = c.getString(22);
        ID = c.getString(23);
        Area = c.getString(24);
        PArea = c.getInt(25);
        Address = c.getString(26);
        Property = c.getString(27);
        RCustody = c.getString(28);
        PRCustody = c.getInt(29);
        Custody = c.getString(30);
        SCustody = c.getString(31);
        if(c.getBlob(32) != null) {
            buf = c.getBlob(32);
            I_SCustody = BitmapFactory.decodeByteArray(buf, 0, buf.length);
        }
        else{
            I_SCustody = null;
        }

        colum = new String[]{ "Trauma_Switch", "Trauma", "Trauma1", "Traffic", "NTrauma", "NTrauma6", "Witnesses", "CPR", "PAD", "ROSC", "PSpecies", "Stroke", "LTime", "Smile", "LArm", "Speech" };
        c = DB.query("MainTab2", colum, "CaseID=? AND Subno=?", parameter, null, null, "id ASC");
        c.moveToFirst();
        Trauma_Switch = c.getString(0);

        Trauma = new String[]{"", "", "", "", "", "", "", "", ""};
        if(c.getString(1) != null) {
            String[] str = c.getString(1).split("－");
            for(int i = 0; i < str.length; i++) {
                Trauma[i] = str[i];
            }
        }

        Trauma1 = new String[] { "", "", "", "", "" };
        if(c.getString(2) != null) {
            String[] str = c.getString(2).split("－");
            for(int i = 0; i < str.length; i++) {
                Trauma1[i] = str[i];
            }
        }

        Traffic = new String[] { "", "", "", "", "" };
        if(c.getString(3) != null) {
            String[] str = c.getString(3).split("－");
            for(int i = 0; i < str.length; i++) {
                Traffic[i] = str[i];
            }
        }

        NTrauma = new String[] { "", "", "", "", "", "", "", "", "", "", "", "", "", "", "" };
        if(c.getString(4) != null) {
            String[] str = c.getString(4).split("－");
            for(int i = 0; i < str.length; i++) {
                NTrauma[i] = str[i];
            }
        }

        NTrauma6 = new String[] { "", "", "", "" };
        if(c.getString(5) != null) {
            String[] str = c.getString(5).split("－");
            for(int i = 0; i < str.length; i++) {
                NTrauma6[i] = str[i];
            }
        }

        Witnesses = c.getString(6);
        CPR = c.getString(7);
        PAD = c.getString(8);
        ROSC = c.getString(9);
        PSpecies = c.getString(10);
        Stroke = c.getString(11);
        LTime = c.getString(12);
        Smile = (c.getInt(13) == 1);
        LArm = (c.getInt(14) == 1);
        Speech = (c.getInt(15) == 1);

        colum = new String[]{ "Breathing", "TDisposal", "CPRDisposal", "CPRDisposal3", "DDisposal", "DDisposal1", "ODisposal", "ALSDisposal", "Posture", "PPosture", "DrugUseTime1", "DrugUseName1", "PDrugUseName1", "DrugUseDose1", "PDrugUseDose1", "DrugUser1", "DrugUseTime2", "DrugUseName2", "PDrugUseName2", "DrugUseDose2", "PDrugUseDose2", "DrugUser2", "DrugUseTime3", "DrugUseName3", "PDrugUseName3", "DrugUseDose3", "PDrugUseDose3", "DrugUser3", "DrugUseTime4", "DrugUseName4", "PDrugUseName4", "DrugUseDose4", "PDrugUseDose4", "DrugUser4" };
        c = DB.query("MainTab3", colum, "CaseID=? AND Subno=?", parameter, null, null, "id ASC");
        c.moveToFirst();
        Breathing = new String[] { "", "", "", "", "", "", "", "", "", "" };
        if(c.getString(0) != null) {
            String[] str = c.getString(0).split("－");
            for(int i = 0; i < str.length; i++) {
                Breathing[i] = str[i];
            }
        }

        TDisposal = new String[] { "", "", "", "", "", "", "", "" };
        if(c.getString(1) != null) {
            String[] str = c.getString(1).split("－");
            for(int i = 0; i < str.length; i++) {
                TDisposal[i] = str[i];
            }
        }

        CPRDisposal = new String[] { "", "", "" };
        if(c.getString(2) != null) {
            String[] str = c.getString(2).split("－");
            for(int i = 0; i < str.length; i++) {
                CPRDisposal[i] = str[i];
            }
        }

        CPRDisposal3 = new String[] { "", "" };
        if(c.getString(3) != null) {
            String[] str = c.getString(3).split("－");
            for(int i = 0; i < str.length; i++) {
                CPRDisposal3[i] = str[i];
            }
        }

        DDisposal = new String[] { "", "", "", "" };
        if(c.getString(4) != null) {
            String[] str = c.getString(4).split("－");
            for(int i = 0; i < str.length; i++) {
                DDisposal[i] = str[i];
            }
        }

        DDisposal1 = new String[] { "", "", "" };
        if(c.getString(5) != null) {
            String[] str = c.getString(5).split("－");
            for(int i = 0; i < str.length; i++) {
                DDisposal1[i] = str[i];
            }
        }

        ODisposal = new String[] { "", "", "", "", "" };
        if(c.getString(6) != null) {
            String[] str = c.getString(6).split("－");
            for(int i = 0; i < str.length; i++) {
                ODisposal[i] = str[i];
            }
        }

        ALSDisposal = new String[] { "", "" };
        if(c.getString(7) != null) {
            String[] str = c.getString(7).split("－");
            for(int i = 0; i < str.length; i++) {
                ALSDisposal[i] = str[i];
            }
        }

        Posture = c.getString(8);
        PPosture = c.getInt(9);

        DrugUseTime1 = c.getString(10);
        DrugUseName1 = c.getString(11);
        PDrugUseName1 = c.getInt(12);
        DrugUseDose1 = c.getString(13);
        PDrugUseDose1 = c.getInt(14);
        DrugUser1 = c.getString(15);

        DrugUseTime2 = c.getString(16);
        DrugUseName2 = c.getString(17);
        PDrugUseName2 = c.getInt(18);
        DrugUseDose2 = c.getString(19);
        PDrugUseDose2 = c.getInt(20);
        DrugUser2 = c.getString(21);

        DrugUseTime3 = c.getString(22);
        DrugUseName3 = c.getString(23);
        PDrugUseName3 = c.getInt(24);
        DrugUseDose3 = c.getString(25);
        PDrugUseDose3 = c.getInt(26);
        DrugUser3 = c.getString(27);

        DrugUseTime4 = c.getString(28);
        DrugUseName4 = c.getString(29);
        PDrugUseName4 = c.getInt(30);
        DrugUseDose4 = c.getString(31);
        PDrugUseDose4 = c.getInt(32);
        DrugUser4 = c.getString(33);

        colum = new String[]{ "Q1", "Q2", "Q3", "Q4", "Q5", "MHistory", "Allergies", "People", "SPeople", "Switch_People" };
        c = DB.query("MainTab4", colum, "CaseID=? AND Subno=?", parameter, null, null, "id ASC");
        c.moveToFirst();
        Q1 = c.getString(0);
        Q2 = c.getString(1);
        Q3 = c.getString(2);
        Q4 = c.getString(3);
        Q5 = c.getString(4);

        MHistory = new String[] { "", "", "", "", "", "", "", "", "", "", "", "", "" };
        if(c.getString(5) != null) {
            String[] str = c.getString(5).split("－");
            for(int i = 0; i < str.length; i++) {
                MHistory[i] = str[i];
            }
        }

        Allergies = new String[] { "", "", "", "" };
        if(c.getString(6) != null) {
            String[] str = c.getString(6).split("－");
            for(int i = 0; i < str.length; i++) {
                Allergies[i] = str[i];
            }
        }

        People = c.getString(7);
        SPeople = c.getString(8);

        Switch_People = new boolean[] { false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false};
        if(c.getString(9) != null) {
            String[] str = c.getString(9).split("－");
            for(int i = 0;i < str.length;i++){
                Switch_People[i] = (str[i].equals("true"));
            }
        }

        colum = new String[]{ "LifeTime1", "Consciousness1", "PConsciousness1", "LifeBreathing1", "Pulse1", "GCS1_E", "GCS1_V", "GCS1_M", "SBP1", "DBP1", "BPressure1_A", "PBPressure1_A", "SpO21", "Temperature1", "LifeTime2", "Consciousness2", "PConsciousness2", "LifeBreathing2", "Pulse2", "GCS2_E", "GCS2_V", "GCS2_M", "SBP2", "DBP2", "BPressure2_A", "PBPressure2_A", "SpO22", "Temperature2", "Consciousness3", "PConsciousness3", "LifeBreathing3", "Pulse3", "GCS3_E", "GCS3_V", "GCS3_M", "SBP3", "DBP3", "BPressure3_A", "PBPressure3_A", "SpO23", "Temperature3" };
        c = DB.query("MainTab5", colum, "CaseID=? AND Subno=?", parameter, null, null, "id ASC");
        c.moveToFirst();
        LifeTime1 = c.getString(0);
        Consciousness1 = c.getString(1);
        PConsciousness1 = c.getInt(2);
        LifeBreathing1 = c.getString(3);
        Pulse1 = c.getString(4);
        GCS1_E = c.getString(5);
        GCS1_V = c.getString(6);
        GCS1_M = c.getString(7);
        SBP1 = c.getString(8);
        DBP1 = c.getString(9);
        BPressure1_A = c.getString(10);
        PBPressure1_A = c.getInt(11);
        SpO21 = c.getString(12);
        Temperature1 = c.getString(13);
        LifeTime2 = c.getString(14);
        Consciousness2 = c.getString(15);
        PConsciousness2 = c.getInt(16);
        LifeBreathing2 = c.getString(17);
        Pulse2 = c.getString(18);
        GCS2_E = c.getString(19);
        GCS2_V = c.getString(20);
        GCS2_M = c.getString(21);
        SBP2 = c.getString(22);
        DBP2 = c.getString(23);
        BPressure2_A = c.getString(24);
        PBPressure2_A = c.getInt(25);
        SpO22 = c.getString(26);
        Temperature2 = c.getString(27);
        Consciousness3 = c.getString(28);
        PConsciousness3 = c.getInt(29);
        LifeBreathing3 = c.getString(30);
        Pulse3 = c.getString(31);
        GCS3_E = c.getString(32);
        GCS3_V = c.getString(33);
        GCS3_M = c.getString(34);
        SBP3 = c.getString(35);
        DBP3 = c.getString(36);
        BPressure3_A = c.getString(37);
        PBPressure3_A = c.getInt(38);
        SpO23 = c.getString(39);
        Temperature3 = c.getString(40);

        colum = new String[]{ "SNote", "I_SNote", "AStaffNO1", "SAStaff1", "I_SAStaff1", "AStaffNO2", "SAStaff2", "I_SAStaff2", "AStaffNO3", "SAStaff3", "I_SAStaff3", "AStaffNO4", "SAStaff4", "I_SAStaff4", "AStaffNO5", "SAStaff5", "I_SAStaff5", "AStaffNO6", "SAStaff6", "I_SAStaff6", "IClassification", "SHStaff", "I_SHStaff", "cb_RHospital", "RHospital", "RRelationship", "PRRelationship", "SRRelationship", "I_SRRelationship", "Relationship", "PRelationship", "Phone", "SRelationship", "I_SRelationship" };
        c = DB.query("MainHandWrite", colum, "CaseID=? AND Subno=?", parameter, null, null, "id ASC");
        c.moveToFirst();
        SNote = c.getString(0);

        I_SNote = null;
        if(c.getBlob(1) != null) {
            buf = c.getBlob(1);
            I_SNote = BitmapFactory.decodeByteArray(buf, 0, buf.length);
        }

        AStaffNO1 = c.getString(2);
        SAStaff1 = c.getString(3);

        I_SAStaff1 = null;
        if(c.getBlob(4) != null) {
            buf = c.getBlob(4);
            I_SAStaff1 = BitmapFactory.decodeByteArray(buf, 0, buf.length);
        }

        AStaffNO2 = c.getString(5);
        SAStaff2 = c.getString(6);

        I_SAStaff2 = null;
        if(c.getBlob(7) != null) {
            buf = c.getBlob(7);
            I_SAStaff2 = BitmapFactory.decodeByteArray(buf, 0, buf.length);
        }

        AStaffNO3 = c.getString(8);
        SAStaff3 = c.getString(9);

        I_SAStaff3 = null;
        if(c.getBlob(10) != null) {
            buf = c.getBlob(10);
            I_SAStaff3 = BitmapFactory.decodeByteArray(buf, 0, buf.length);
        }

        AStaffNO4 = c.getString(11);
        SAStaff4 = c.getString(12);

        I_SAStaff4 = null;
        if(c.getBlob(13) != null) {
            buf = c.getBlob(13);
            I_SAStaff4 = BitmapFactory.decodeByteArray(buf, 0, buf.length);
        }

        AStaffNO5 = c.getString(14);
        SAStaff5 = c.getString(15);

        I_SAStaff5 = null;
        if(c.getBlob(16) != null) {
            buf = c.getBlob(16);
            I_SAStaff5 = BitmapFactory.decodeByteArray(buf, 0, buf.length);
        }

        AStaffNO6 = c.getString(17);
        SAStaff6 = c.getString(18);

        I_SAStaff6 = null;
        if(c.getBlob(19) != null) {
            buf = c.getBlob(19);
            I_SAStaff6 = BitmapFactory.decodeByteArray(buf, 0, buf.length);
        }

        IClassification = c.getString(20);
        SHStaff = c.getString(21);

        I_SHStaff = null;
        if(c.getBlob(22) != null) {
            buf = c.getBlob(22);
            I_SHStaff = BitmapFactory.decodeByteArray(buf, 0, buf.length);
        }

        cb_RHospital = (c.getInt(23) == 1);
        RHospital = c.getString(24);
        RRelationship = c.getString(25);
        PRRelationship = c.getInt(26);
        SRRelationship = c.getString(27);

        I_SRRelationship = null;
        if(c.getBlob(28) != null) {
            buf = c.getBlob(28);
            I_SRRelationship = BitmapFactory.decodeByteArray(buf, 0, buf.length);
        }
        Relationship = c.getString(29);
        PRelationship = c.getInt(30);
        Phone = c.getString(31);
        SRelationship = c.getString(32);

        I_SRelationship = null;
        if(c.getBlob(33) != null) {
            buf = c.getBlob(33);
            I_SRelationship = BitmapFactory.decodeByteArray(buf, 0, buf.length);
        }

        SQLite.close();
        DB.close();
        c.close();

        return true;
    }

    public boolean NewTable(int Subno) {

        SQLite SQLite = new SQLite(GlobalVariable.this);
        SQLiteDatabase DB = SQLite.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("CaseID", FNumber);
        cv.put("Subno", Subno);
        cv.put("FNumber", FNumber);
        cv.put("LoadPDF", LoadPDF);
        cv.put("Critical", 0);
        cv.put("Date", Date);
        cv.put("AttendanceUnit", AttendanceUnit);
        cv.put("AcceptedUnit", AcceptedUnit);
        cv.put("LocationHappen", LocationHappen);
        cv.put("AssistanceUnit", AssistanceUnit);
        cv.put("HospitalAddress", HospitalAddress);
        cv.put("PHospitalAddress", PHospitalAddress);
        cv.put("ReasonHospital", ReasonHospital);
        cv.put("MHospital", MHospital);
        cv.put("Time1", Time1);
        cv.put("Time2", Time2);
        cv.put("Time3", Time3);
        cv.put("Time4", Time4);
        cv.put("Time5", Time5);
        cv.put("Time6", Time6);
        cv.put("Name", "");
        cv.put("AgeM", 0);
        cv.put("AgeA", 0);
        cv.put("Age", "");
        cv.put("Sex", "");
        cv.put("Identity", "");
        cv.put("Area", "");
        cv.put("PArea", 0);
        cv.put("Address", "");
        cv.put("Property", "");
        cv.put("RCustody", "");
        cv.put("PRCustody", 0);
        cv.put("Custody", "");
        cv.put("SCustody", "");
        cv.put("I_SCustody", "");
        DB.insert("MainTab1", null, cv);

        cv = new ContentValues();
        cv.put("CaseID", FNumber);
        cv.put("Subno", Subno);
        cv.put("Trauma_Switch", "");
        cv.put("Trauma", "");
        cv.put("Trauma1", "");
        cv.put("Traffic", "");
        cv.put("NTrauma", "");
        cv.put("NTrauma6", "");
        cv.put("Witnesses", "");
        cv.put("CPR", "");
        cv.put("PAD", "");
        cv.put("ROSC", "");
        cv.put("PSpecies", "");
        cv.put("Stroke", "");
        cv.put("LTime", "");
        cv.put("Smile", 0);
        cv.put("LArm", 0);
        cv.put("Speech", 0);
        DB.insert("MainTab2", null, cv);

        cv = new ContentValues();
        cv.put("CaseID", FNumber);
        cv.put("Subno", Subno);
        cv.put("Breathing", "");
        cv.put("TDisposal", "");
        cv.put("CPRDisposal", "");
        cv.put("CPRDisposal3", "");
        cv.put("DDisposal", "");
        cv.put("DDisposal1", "");
        cv.put("ODisposal", "");
        cv.put("ALSDisposal", "");
        cv.put("Posture", "");
        cv.put("PPosture", 0);
        cv.put("DrugUseTime1", "");
        cv.put("DrugUseName1", "");
        cv.put("PDrugUseName1", 0);
        cv.put("DrugUseDose1", "");
        cv.put("PDrugUseDose1", 0);
        cv.put("DrugUser1", "");
        cv.put("DrugUseTime2", "");
        cv.put("DrugUseName2", "");
        cv.put("PDrugUseName2", 0);
        cv.put("DrugUseDose2", "");
        cv.put("PDrugUseDose2", 0);
        cv.put("DrugUser2", "");
        cv.put("DrugUseTime3", "");
        cv.put("DrugUseName3", "");
        cv.put("PDrugUseName3", 0);
        cv.put("DrugUseDose3", "");
        cv.put("PDrugUseDose3", 0);
        cv.put("DrugUser3", "");
        cv.put("DrugUseTime4", "");
        cv.put("DrugUseName4", "");
        cv.put("PDrugUseName4", 0);
        cv.put("DrugUseDose4", "");
        cv.put("PDrugUseDose4", 0);
        cv.put("DrugUser4", "");
        DB.insert("MainTab3", null, cv);

        cv = new ContentValues();
        cv.put("CaseID", FNumber);
        cv.put("Subno", Subno);
        cv.put("Q1", "");
        cv.put("Q2", "");
        cv.put("Q3", "");
        cv.put("Q4", "");
        cv.put("Q5", "");
        cv.put("MHistory", "");
        cv.put("Allergies", "");
        cv.put("People", "");
        cv.put("SPeople", "");
        cv.put("Switch_People", "");
        DB.insert("MainTab4", null, cv);

        cv = new ContentValues();
        cv.put("CaseID", FNumber);
        cv.put("Subno", Subno);
        cv.put("LifeTime1", "");
        cv.put("Consciousness1", "");
        cv.put("PConsciousness1", 0);
        cv.put("LifeBreathing1", "");
        cv.put("Pulse1", "");
        cv.put("GCS1_E", "");
        cv.put("GCS1_V", "");
        cv.put("GCS1_M", "");
        cv.put("SBP1", "");
        cv.put("DBP1", "");
        cv.put("BPressure1_A", "");
        cv.put("PBPressure1_A", 0);
        cv.put("SpO21", "");
        cv.put("Temperature1", "");
        cv.put("LifeTime2", "");
        cv.put("Consciousness2", "");
        cv.put("PConsciousness2", 0);
        cv.put("LifeBreathing2", "");
        cv.put("Pulse2", "");
        cv.put("GCS2_E", "");
        cv.put("GCS2_V", "");
        cv.put("GCS2_M", "");
        cv.put("SBP2", "");
        cv.put("DBP2", "");
        cv.put("BPressure2_A", "");
        cv.put("PBPressure2_A", 0);
        cv.put("SpO22", "");
        cv.put("Temperature2", "");
        cv.put("Consciousness3", "");
        cv.put("PConsciousness3", 0);
        cv.put("LifeBreathing3", "");
        cv.put("Pulse3", "");
        cv.put("GCS3_E", "");
        cv.put("GCS3_V", "");
        cv.put("GCS3_M", "");
        cv.put("SBP3", "");
        cv.put("DBP3", "");
        cv.put("BPressure3_A", "");
        cv.put("PBPressure3_A", "");
        cv.put("SpO23", "");
        cv.put("Temperature3", "");
        DB.insert("MainTab5", null, cv);

        cv = new ContentValues();
        cv.put("CaseID", FNumber);
        cv.put("Subno", Subno);
        cv.put("SNote", "");
        cv.put("I_SNote", "");
        cv.put("AStaffNO1", "");
        cv.put("SAStaff1", "");
        cv.put("I_SAStaff1", "");
        cv.put("AStaffNO2", "");
        cv.put("SAStaff2", "");
        cv.put("I_SAStaff2", "");
        cv.put("AStaffNO3", "");
        cv.put("SAStaff3", "");
        cv.put("I_SAStaff3", "");
        cv.put("AStaffNO4", "");
        cv.put("SAStaff4", "");
        cv.put("I_SAStaff4", "");
        cv.put("AStaffNO5", "");
        cv.put("SAStaff5", "");
        cv.put("I_SAStaff5", "");
        cv.put("AStaffNO6", "");
        cv.put("SAStaff6", "");
        cv.put("I_SAStaff6", "");
        cv.put("IClassification", "");
        cv.put("SHStaff", "");
        cv.put("I_SHStaff", "");
        cv.put("cb_RHospital", 0);
        cv.put("RHospital", "");
        cv.put("RRelationship", "");
        cv.put("PRRelationship", "");
        cv.put("SRRelationship", "");
        cv.put("I_SRRelationship", "");
        cv.put("Relationship", "");
        cv.put("PRelationship", "");
        cv.put("Phone", "");
        cv.put("SRelationship", "");
        cv.put("I_SRelationship", "");
        DB.insert("MainHandWrite", null, cv);

        return true;
    }

    public String HUpload(String url, String NotifyType) {
        String ParamStr = "";
        HttpPost httppost = new HttpPost( url + "APP_Upload_Hospital.aspx" ) ; //宣告要使用的頁面
        List<NameValuePair> params = new ArrayList<NameValuePair>( ) ; //宣告參數清單
        //基本資料
        params.add(new BasicNameValuePair("FNumber", FNumber)) ;
        params.add(new BasicNameValuePair("Subno", String.valueOf(0))) ;
        params.add(new BasicNameValuePair("LoadPDF", LoadPDF)) ;
        params.add(new BasicNameValuePair("Critical", Critical?"是":"否")) ;
        params.add(new BasicNameValuePair("Date", Date)) ;
        params.add(new BasicNameValuePair("AttendanceUnit", AttendanceUnit)) ;
        params.add(new BasicNameValuePair("AcceptedUnit", AcceptedUnit)) ;
        params.add(new BasicNameValuePair("LocationHappen", LocationHappen)) ;
        params.add(new BasicNameValuePair("AssistanceUnit", AssistanceUnit)) ;
        params.add(new BasicNameValuePair("HospitalAddress", HospitalAddress)) ;
        params.add(new BasicNameValuePair("ReasonHospital", ReasonHospital)) ;
        params.add(new BasicNameValuePair("MHospital", MHospital)) ;
        params.add(new BasicNameValuePair("Time1", Time1)) ;
        params.add(new BasicNameValuePair("Time2", Time2)) ;
        params.add(new BasicNameValuePair("Time3", Time3)) ;
        params.add(new BasicNameValuePair("Time4", Time4)) ;
        params.add(new BasicNameValuePair("Time5", Time5)) ;
        params.add(new BasicNameValuePair("Time6", Time6)) ;
        params.add(new BasicNameValuePair("Name", Name)) ;
        params.add(new BasicNameValuePair("AgeM", String.valueOf(AgeM))) ;
        params.add(new BasicNameValuePair("AgeA", String.valueOf(AgeA))) ;
        params.add(new BasicNameValuePair("Age", Age)) ;
        params.add(new BasicNameValuePair("Sex", Sex)) ;
        params.add(new BasicNameValuePair("ID", ID)) ;
        params.add(new BasicNameValuePair("Address", Area + Address)) ;
        params.add(new BasicNameValuePair("Property", Property)) ;
        params.add(new BasicNameValuePair("RCustody" , RCustody)) ;
        params.add(new BasicNameValuePair("Custody" , Custody)) ;

        //現場狀況
        params.add(new BasicNameValuePair("Trauma_TF", Trauma_Switch)) ;

        ParamStr = "";
        for(String Content: Trauma){
            if(!Content.equals("")) {
                ParamStr += Content + "－";
            }
        }
        params.add(new BasicNameValuePair("Trauma_Select", ParamStr)) ;

        ParamStr = "";
        for(String Content: Trauma1){
            if(!Content.equals("")) {
                ParamStr += Content + "－";
            }
        }
        params.add(new BasicNameValuePair("Trauma1", ParamStr)) ;

        ParamStr = "";
        for(String Content: Traffic){
            if(!Content.equals("")) {
                ParamStr += Content + "－";
            }
        }
        params.add(new BasicNameValuePair("Transport", ParamStr)) ;

        ParamStr = "";
        for(String Content: NTrauma){
            if(!Content.equals("")) {
                ParamStr += Content + "－";
            }
        }
        params.add(new BasicNameValuePair("NTrauma_Select", ParamStr)) ;

        ParamStr = "";
        for(String Content: NTrauma6){
            if(!Content.equals("")) {
                ParamStr += Content + "－";
            }
        }
        params.add(new BasicNameValuePair("NTrauma6", ParamStr)) ;

        params.add(new BasicNameValuePair("Witnesses", Witnesses)) ;
        params.add(new BasicNameValuePair("CPR", CPR)) ;
        params.add(new BasicNameValuePair("PAD", PAD)) ;
        params.add(new BasicNameValuePair("ROSC", ROSC)) ;
        params.add(new BasicNameValuePair("PSpecies", PSpecies)) ;
        params.add(new BasicNameValuePair("Stroke", Stroke)) ;
        params.add(new BasicNameValuePair("LTime", LTime)) ;
        params.add(new BasicNameValuePair("Smile", String.valueOf(Smile))) ;
        params.add(new BasicNameValuePair("LArm", String.valueOf(LArm))) ;
        params.add(new BasicNameValuePair("Speech", String.valueOf(Speech))) ;

        //急救處置
        ParamStr = "";
        for(String Content: Breathing){
            if(!Content.equals("")) {
                ParamStr += Content + "－";
            }
        }
        params.add(new BasicNameValuePair("Breathing", ParamStr)) ;

        ParamStr = "";
        for(String Content: TDisposal){
            if(!Content.equals("")) {
                ParamStr += Content + "－";
            }
        }
        params.add(new BasicNameValuePair("TDisposal", ParamStr)) ;

        ParamStr = "";
        for(String Content: CPRDisposal){
            if(!Content.equals("")) {
                ParamStr += Content + "－";
            }
        }
        params.add(new BasicNameValuePair("CPRDisposal", ParamStr)) ;

        ParamStr = "";
        for(String Content: CPRDisposal3){
            if(!Content.equals("")) {
                ParamStr += Content + "－";
            }
        }
        params.add (new BasicNameValuePair("CPRDisposal3" , ParamStr)) ;

        ParamStr = "";
        for(String Content: DDisposal){
            if(!Content.equals("")) {
                ParamStr += Content + "－";
            }
        }
        params.add(new BasicNameValuePair("DDisposal", ParamStr)) ;

        ParamStr = "";
        for(String Content: DDisposal1){
            if(!Content.equals("")) {
                ParamStr += Content + "－";
            }
        }
        params.add(new BasicNameValuePair("DDisposal1", ParamStr)) ;

        ParamStr = "";
        for(String Content: ODisposal){
            if(!Content.equals("")) {
                ParamStr += Content + "－";
            }
        }
        params.add(new BasicNameValuePair("ODisposal", ParamStr)) ;

        ParamStr = "";
        for(String Content: ALSDisposal){
            if(!Content.equals("")) {
                ParamStr += Content + "－";
            }
        }
        params.add(new BasicNameValuePair("ALSDisposal", ParamStr)) ;

        params.add(new BasicNameValuePair("Posture", Posture)) ;

        params.add(new BasicNameValuePair("DrugUseTime1", DrugUseTime1)) ;
        params.add(new BasicNameValuePair("DrugUseName1", DrugUseName1)) ;
        params.add(new BasicNameValuePair("DrugUseDose1", DrugUseDose1)) ;
        params.add(new BasicNameValuePair("DrugUser1", DrugUser1)) ;
        params.add(new BasicNameValuePair("DrugUseTime2", DrugUseTime2)) ;
        params.add(new BasicNameValuePair("DrugUseName2", DrugUseName2)) ;
        params.add(new BasicNameValuePair("DrugUseDose2", DrugUseDose2)) ;
        params.add(new BasicNameValuePair("DrugUser2", DrugUser2)) ;
        params.add(new BasicNameValuePair("DrugUseTime3", DrugUseTime3)) ;
        params.add(new BasicNameValuePair("DrugUseName3", DrugUseName3)) ;
        params.add(new BasicNameValuePair("DrugUseDose3", DrugUseDose3)) ;
        params.add(new BasicNameValuePair("DrugUser3", DrugUser3)) ;
        params.add(new BasicNameValuePair("DrugUseTime4", DrugUseTime4)) ;
        params.add(new BasicNameValuePair("DrugUseName4", DrugUseName4)) ;
        params.add(new BasicNameValuePair("DrugUseDose4", DrugUseDose4)) ;
        params.add(new BasicNameValuePair("DrugUser4", DrugUser4)) ;

        //病患相關
        params.add(new BasicNameValuePair("Q1", Q1)) ;
        params.add(new BasicNameValuePair("Q2", Q2)) ;
        params.add(new BasicNameValuePair("Q3", Q3)) ;
        params.add(new BasicNameValuePair("Q4", Q4)) ;
        params.add(new BasicNameValuePair("Q5", Q5)) ;

        ParamStr = "";
        for(String Content: MHistory){
            if(!Content.equals("")) {
                ParamStr += Content + "－";
            }
        }
        params.add(new BasicNameValuePair("MHistory", ParamStr)) ;

        ParamStr = "";
        for(String Content: Allergies){
            if(!Content.equals("")) {
                ParamStr += Content + "－";
            }
        }
        params.add(new BasicNameValuePair("Allergies", ParamStr)) ;

        params.add(new BasicNameValuePair("People", People)) ;

        //生命徵象
        params.add(new BasicNameValuePair("LifeTime1", LifeTime1)) ;
        params.add(new BasicNameValuePair("Consciousness1", Consciousness1)) ;
        params.add(new BasicNameValuePair("LifeBreathing1", LifeBreathing1)) ;
        params.add(new BasicNameValuePair("Pulse1", Pulse1)) ;
        params.add(new BasicNameValuePair("GCS1_E", GCS1_E)) ;
        params.add(new BasicNameValuePair("GCS1_V", GCS1_V)) ;
        params.add(new BasicNameValuePair("GCS1_M", GCS1_M)) ;
        params.add(new BasicNameValuePair("SBP1", SBP1)) ;
        params.add(new BasicNameValuePair("DBP1", DBP1)) ;
        params.add(new BasicNameValuePair("BPressure1_A", BPressure1_A)) ;
        params.add(new BasicNameValuePair("SpO21", SpO21)) ;
        params.add(new BasicNameValuePair("Temperature1", Temperature1)) ;
        params.add(new BasicNameValuePair("LifeTime2", LifeTime2)) ;
        params.add(new BasicNameValuePair("Consciousness2", Consciousness2)) ;
        params.add(new BasicNameValuePair("LifeBreathing2", LifeBreathing2)) ;
        params.add(new BasicNameValuePair("Pulse2", Pulse2)) ;
        params.add(new BasicNameValuePair("GCS2_E", GCS2_E)) ;
        params.add(new BasicNameValuePair("GCS2_V", GCS2_V)) ;
        params.add(new BasicNameValuePair("GCS2_M", GCS2_M)) ;
        params.add(new BasicNameValuePair("SBP2", SBP2)) ;
        params.add(new BasicNameValuePair("DBP2", DBP2)) ;
        params.add(new BasicNameValuePair("BPressure2_A", BPressure2_A)) ;
        params.add(new BasicNameValuePair("SpO22", SpO22)) ;
        params.add(new BasicNameValuePair("Temperature2", Temperature2)) ;
        params.add(new BasicNameValuePair("LifeTime3", Time4)) ;
        params.add(new BasicNameValuePair("Consciousness3" , Consciousness3)) ;
        params.add(new BasicNameValuePair("LifeBreathing3", LifeBreathing3)) ;
        params.add(new BasicNameValuePair("Pulse3", Pulse3)) ;
        params.add(new BasicNameValuePair("GCS3_E", GCS3_E)) ;
        params.add(new BasicNameValuePair("GCS3_V", GCS3_V)) ;
        params.add(new BasicNameValuePair("GCS3_M", GCS3_M)) ;
        params.add(new BasicNameValuePair("SBP3", SBP3)) ;
        params.add(new BasicNameValuePair("DBP3", DBP3)) ;
        params.add(new BasicNameValuePair("BPressure3_A", BPressure3_A)) ;
        params.add(new BasicNameValuePair("SpO23", SpO23)) ;
        params.add(new BasicNameValuePair("Temperature3", Temperature3)) ;

        //手寫簽名
        params.add(new BasicNameValuePair("AStaffNO1", AStaffNO1)) ;
        params.add(new BasicNameValuePair("AStaffNO2", AStaffNO2)) ;
        params.add(new BasicNameValuePair("AStaffNO3", AStaffNO3)) ;
        params.add(new BasicNameValuePair("AStaffNO4", AStaffNO4)) ;
        params.add(new BasicNameValuePair("AStaffNO5", AStaffNO5)) ;
        params.add(new BasicNameValuePair("AStaffNO6", AStaffNO6)) ;
        params.add(new BasicNameValuePair("IClassification", IClassification)) ;
        params.add(new BasicNameValuePair("cb_RHospital", String.valueOf(cb_RHospital))) ;
        params.add(new BasicNameValuePair("RHospital", RHospital)) ;
        params.add(new BasicNameValuePair("RRelationship", RRelationship)) ;
        params.add(new BasicNameValuePair("Relationship", Relationship)) ;
        params.add(new BasicNameValuePair("Phone", Phone)) ;
        params.add(new BasicNameValuePair("Preview", PreviewMain)) ;
        params.add(new BasicNameValuePair("NotifyType", NotifyType)) ;
        params.add(new BasicNameValuePair("NotifyClass", String.valueOf(PHospitalAddress + 1))) ;

        String str = GetResponse(httppost, params);

        return str;
    }

    public String Upload(String url) {

        HttpPost httppost = new HttpPost( url + "APP_Upload_PaperT.aspx" ) ; //宣告要使用的頁面
        List<NameValuePair> params = new ArrayList<NameValuePair>( ) ; //宣告參數清單
        params.add(new BasicNameValuePair("Preview", PreviewMain)) ;

        String Paper = GetResponse(httppost, params);

        if(Paper.equals("Overtime")) {
            return Paper;
        }
        else if(Paper.equals("Error")) {
            return Paper;
        }

        String ParamStr = "";
        httppost = new HttpPost( url + "APP_Upload_DataT.aspx" ) ; //宣告要使用的頁面
        params = new ArrayList<NameValuePair>( ) ; //宣告參數清單
        //基本資料
        params.add(new BasicNameValuePair("PaperName", Paper)) ;
        params.add(new BasicNameValuePair("FNumber", FNumber)) ;
        //params.add(new BasicNameValuePair("Subno", String.valueOf(Integer.valueOf(Subno) + 1))) ;
        params.add(new BasicNameValuePair("LoadPDF", LoadPDF)) ;
        params.add(new BasicNameValuePair("Critical", Critical?"是":"否")) ;
        params.add(new BasicNameValuePair("Date", Date)) ;
        params.add(new BasicNameValuePair("AttendanceUnit", AttendanceUnit)) ;
        params.add(new BasicNameValuePair("AcceptedUnit", AcceptedUnit)) ;
        params.add(new BasicNameValuePair("LocationHappen", LocationHappen)) ;
        params.add(new BasicNameValuePair("AssistanceUnit", AssistanceUnit)) ;
        params.add(new BasicNameValuePair("HospitalAddress", HospitalAddress)) ;
        params.add(new BasicNameValuePair("ReasonHospital", ReasonHospital)) ;
        params.add(new BasicNameValuePair("MHospital", MHospital)) ;
        params.add(new BasicNameValuePair("Time1", Time1)) ;
        params.add(new BasicNameValuePair("Time2", Time2)) ;
        params.add(new BasicNameValuePair("Time3", Time3)) ;
        params.add(new BasicNameValuePair("Time4", Time4)) ;
        params.add(new BasicNameValuePair("Time5", Time5)) ;
        params.add(new BasicNameValuePair("Time6", Time6)) ;
        params.add(new BasicNameValuePair("Name", Name)) ;
        params.add(new BasicNameValuePair("AgeM", String.valueOf(AgeM))) ;
        params.add(new BasicNameValuePair("AgeA", String.valueOf(AgeA))) ;
        params.add(new BasicNameValuePair("Age", Age)) ;
        params.add(new BasicNameValuePair("Sex", Sex)) ;
        params.add(new BasicNameValuePair("ID", ID)) ;
        params.add(new BasicNameValuePair("Address", Area + Address)) ;
        params.add(new BasicNameValuePair("Property", Property)) ;
        params.add(new BasicNameValuePair("RCustody" , RCustody)) ;
        params.add(new BasicNameValuePair("Custody" , Custody)) ;

        //現場狀況
        params.add(new BasicNameValuePair("Trauma_TF", Trauma_Switch)) ;

        ParamStr = "";
        for(String Content: Trauma){
            if(!Content.equals("")) {
                ParamStr += Content + "－";
            }
        }
        params.add(new BasicNameValuePair("Trauma_Select", ParamStr)) ;

        ParamStr = "";
        for(String Content: Trauma1){
            if(!Content.equals("")) {
                ParamStr += Content + "－";
            }
        }
        params.add(new BasicNameValuePair("Trauma1", ParamStr)) ;

        ParamStr = "";
        for(String Content: Traffic){
            if(!Content.equals("")) {
                ParamStr += Content + "－";
            }
        }
        params.add(new BasicNameValuePair("Transport", ParamStr)) ;

        ParamStr = "";
        for(String Content: NTrauma){
            if(!Content.equals("")) {
                ParamStr += Content + "－";
            }
        }
        params.add(new BasicNameValuePair("NTrauma_Select", ParamStr)) ;

        ParamStr = "";
        for(String Content: NTrauma6){
            if(!Content.equals("")) {
                ParamStr += Content + "－";
            }
        }
        params.add(new BasicNameValuePair("NTrauma6", ParamStr)) ;

        params.add(new BasicNameValuePair("Witnesses", Witnesses)) ;
        params.add(new BasicNameValuePair("CPR", CPR)) ;
        params.add(new BasicNameValuePair("PAD", PAD)) ;
        params.add(new BasicNameValuePair("ROSC", ROSC)) ;
        params.add(new BasicNameValuePair("PSpecies", PSpecies)) ;
        params.add(new BasicNameValuePair("Stroke", Stroke)) ;
        params.add(new BasicNameValuePair("LTime", LTime)) ;
        params.add(new BasicNameValuePair("Smile", String.valueOf(Smile))) ;
        params.add(new BasicNameValuePair("LArm", String.valueOf(LArm))) ;
        params.add(new BasicNameValuePair("Speech", String.valueOf(Speech))) ;

        //急救處置
        ParamStr = "";
        for(String Content: Breathing){
            if(!Content.equals("")) {
                ParamStr += Content + "－";
            }
        }
        params.add(new BasicNameValuePair("Breathing", ParamStr)) ;

        ParamStr = "";
        for(String Content: TDisposal){
            if(!Content.equals("")) {
                ParamStr += Content + "－";
            }
        }
        params.add(new BasicNameValuePair("TDisposal", ParamStr)) ;

        ParamStr = "";
        for(String Content: CPRDisposal){
            if(!Content.equals("")) {
                ParamStr += Content + "－";
            }
        }
        params.add(new BasicNameValuePair("CPRDisposal", ParamStr)) ;

        ParamStr = "";
        for(String Content: CPRDisposal3){
            if(!Content.equals("")) {
                ParamStr += Content + "－";
            }
        }
        params.add (new BasicNameValuePair("CPRDisposal3" , ParamStr)) ;

        ParamStr = "";
        for(String Content: DDisposal){
            if(!Content.equals("")) {
                ParamStr += Content + "－";
            }
        }
        params.add(new BasicNameValuePair("DDisposal", ParamStr)) ;

        ParamStr = "";
        for(String Content: DDisposal1){
            if(!Content.equals("")) {
                ParamStr += Content + "－";
            }
        }
        params.add(new BasicNameValuePair("DDisposal1", ParamStr)) ;

        ParamStr = "";
        for(String Content: ODisposal){
            if(!Content.equals("")) {
                ParamStr += Content + "－";
            }
        }
        params.add(new BasicNameValuePair("ODisposal", ParamStr)) ;

        ParamStr = "";
        for(String Content: ALSDisposal){
            if(!Content.equals("")) {
                ParamStr += Content + "－";
            }
        }
        params.add(new BasicNameValuePair("ALSDisposal", ParamStr)) ;

        params.add(new BasicNameValuePair("Posture", Posture)) ;

        params.add(new BasicNameValuePair("DrugUseTime1", DrugUseTime1)) ;
        params.add(new BasicNameValuePair("DrugUseName1", DrugUseName1)) ;
        params.add(new BasicNameValuePair("DrugUseDose1", DrugUseDose1)) ;
        params.add(new BasicNameValuePair("DrugUser1", DrugUser1)) ;
        params.add(new BasicNameValuePair("DrugUseTime2", DrugUseTime2)) ;
        params.add(new BasicNameValuePair("DrugUseName2", DrugUseName2)) ;
        params.add(new BasicNameValuePair("DrugUseDose2", DrugUseDose2)) ;
        params.add(new BasicNameValuePair("DrugUser2", DrugUser2)) ;
        params.add(new BasicNameValuePair("DrugUseTime3", DrugUseTime3)) ;
        params.add(new BasicNameValuePair("DrugUseName3", DrugUseName3)) ;
        params.add(new BasicNameValuePair("DrugUseDose3", DrugUseDose3)) ;
        params.add(new BasicNameValuePair("DrugUser3", DrugUser3)) ;
        params.add(new BasicNameValuePair("DrugUseTime4", DrugUseTime4)) ;
        params.add(new BasicNameValuePair("DrugUseName4", DrugUseName4)) ;
        params.add(new BasicNameValuePair("DrugUseDose4", DrugUseDose4)) ;
        params.add(new BasicNameValuePair("DrugUser4", DrugUser4)) ;

        //病患相關
        params.add(new BasicNameValuePair("Q1", Q1)) ;
        params.add(new BasicNameValuePair("Q2", Q2)) ;
        params.add(new BasicNameValuePair("Q3", Q3)) ;
        params.add(new BasicNameValuePair("Q4", Q4)) ;
        params.add(new BasicNameValuePair("Q5", Q5)) ;

        ParamStr = "";
        for(String Content: MHistory){
            if(!Content.equals("")) {
                ParamStr += Content + "－";
            }
        }
        params.add(new BasicNameValuePair("MHistory", ParamStr)) ;

        ParamStr = "";
        for(String Content: Allergies){
            if(!Content.equals("")) {
                ParamStr += Content + "－";
            }
        }
        params.add(new BasicNameValuePair("Allergies", ParamStr)) ;

        params.add(new BasicNameValuePair("People", People)) ;

        //生命徵象
        params.add(new BasicNameValuePair("LifeTime1", LifeTime1)) ;
        params.add(new BasicNameValuePair("Consciousness1", Consciousness1)) ;
        params.add(new BasicNameValuePair("LifeBreathing1", LifeBreathing1)) ;
        params.add(new BasicNameValuePair("Pulse1", Pulse1)) ;
        params.add(new BasicNameValuePair("GCS1_E", GCS1_E)) ;
        params.add(new BasicNameValuePair("GCS1_V", GCS1_V)) ;
        params.add(new BasicNameValuePair("GCS1_M", GCS1_M)) ;
        params.add(new BasicNameValuePair("SBP1", SBP1)) ;
        params.add(new BasicNameValuePair("DBP1", DBP1)) ;
        params.add(new BasicNameValuePair("BPressure1_A", BPressure1_A)) ;
        params.add(new BasicNameValuePair("SpO21", SpO21)) ;
        params.add(new BasicNameValuePair("Temperature1", Temperature1)) ;
        params.add(new BasicNameValuePair("LifeTime2", LifeTime2)) ;
        params.add(new BasicNameValuePair("Consciousness2", Consciousness2)) ;
        params.add(new BasicNameValuePair("LifeBreathing2", LifeBreathing2)) ;
        params.add(new BasicNameValuePair("Pulse2", Pulse2)) ;
        params.add(new BasicNameValuePair("GCS2_E", GCS2_E)) ;
        params.add(new BasicNameValuePair("GCS2_V", GCS2_V)) ;
        params.add(new BasicNameValuePair("GCS2_M", GCS2_M)) ;
        params.add(new BasicNameValuePair("SBP2", SBP2)) ;
        params.add(new BasicNameValuePair("DBP2", DBP2)) ;
        params.add(new BasicNameValuePair("BPressure2_A", BPressure2_A)) ;
        params.add(new BasicNameValuePair("SpO22", SpO22)) ;
        params.add(new BasicNameValuePair("Temperature2", Temperature2)) ;
        params.add(new BasicNameValuePair("LifeTime3", Time4)) ;
        params.add(new BasicNameValuePair("Consciousness3" , Consciousness3)) ;
        params.add(new BasicNameValuePair("LifeBreathing3", LifeBreathing3)) ;
        params.add(new BasicNameValuePair("Pulse3", Pulse3)) ;
        params.add(new BasicNameValuePair("GCS3_E", GCS3_E)) ;
        params.add(new BasicNameValuePair("GCS3_V", GCS3_V)) ;
        params.add(new BasicNameValuePair("GCS3_M", GCS3_M)) ;
        params.add(new BasicNameValuePair("SBP3", SBP3)) ;
        params.add(new BasicNameValuePair("DBP3", DBP3)) ;
        params.add(new BasicNameValuePair("BPressure3_A", BPressure3_A)) ;
        params.add(new BasicNameValuePair("SpO23", SpO23)) ;
        params.add(new BasicNameValuePair("Temperature3", Temperature3)) ;

        //手寫簽名
        params.add(new BasicNameValuePair("AStaffNO1", AStaffNO1)) ;
        params.add(new BasicNameValuePair("AStaffNO2", AStaffNO2)) ;
        params.add(new BasicNameValuePair("AStaffNO3", AStaffNO3)) ;
        params.add(new BasicNameValuePair("AStaffNO4", AStaffNO4)) ;
        params.add(new BasicNameValuePair("AStaffNO5", AStaffNO5)) ;
        params.add(new BasicNameValuePair("AStaffNO6", AStaffNO6)) ;
        params.add(new BasicNameValuePair("IClassification", IClassification)) ;
        params.add(new BasicNameValuePair("cb_RHospital", String.valueOf(cb_RHospital))) ;
        params.add(new BasicNameValuePair("RHospital", RHospital)) ;
        params.add(new BasicNameValuePair("RRelationship", RRelationship)) ;
        params.add(new BasicNameValuePair("Relationship", Relationship)) ;
        params.add(new BasicNameValuePair("Phone", Phone)) ;

        String str = GetResponse(httppost, params);

        return str;
    }



    public JSONObject UploadData(String NotifyType) {
        try {


            URL url = new URL("http://192.168.0.107:3000/phone/update_case");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            JSONObject json = new JSONObject();


            String LoadPDFn=LoadPDF;
            switch (LoadPDFn){
                case "創傷":
                    LoadPDFn="0";
                    break;
                case "非創傷":
                    LoadPDFn="1";
                    break;
                case "臨時勤務":
                    LoadPDFn="2";
                    break;
                case "路倒":
                    LoadPDFn="3";
                    break;
                case "急病":
                    LoadPDFn="4";
                    break;
                case "車禍":
                    LoadPDFn="5";
                    break;
                case "其他":
                    LoadPDFn="6";
                    break;
            }

            String Sexn=Sex;
            switch (Sexn){
                case "女":
                    Sexn="0";
                    break;
                case "男":
                    Sexn="1";
                    break;

            }

            String ParamStr = "";
            String Paper="未知數";
            //基本資料
            json.put("PaperName", Paper) ;
            json.put("Car_Index", Car_Index) ;
            json.put("Case_Number", FNumber) ;
            //params.add(new BasicNameValuePair("Subno", String.valueOf(Integer.valueOf(Subno) + 1))) ;
            json.put("Case_Reason", LoadPDFn) ;
            json.put("Desperate", Critical?"是":"否") ;//未找到
            json.put("Date", Date) ;
            json.put("Attendance", AttendanceUnit) ;
          //  json.put("Accept", AcceptedUnit) ;
            json.put("Location", LocationHappen) ;
            json.put("Cooperation", AssistanceUnit) ;
            json.put("Send_Location", HospitalAddress) ;
            json.put("Not_Send_Reason", ReasonHospital) ;
            json.put("Full_Bed_Hospital", MHospital) ;
            json.put("Attendance_Time", Time1) ;
            json.put("Arrival_Site_Time", Time2) ;
            json.put("Exit_Site_Time", Time3) ;
            json.put("Arrival_Hospital_Time", Time4) ;
            json.put("Exit_Hosptial_Time", Time5) ;
            json.put("Back_Time", Time6) ;
            json.put("Name", Name) ;
            json.put("Month", String.valueOf(AgeM)) ;
            json.put("Month_About", String.valueOf(AgeA)) ;
            json.put("Ages", Age) ;
            json.put("Gender", Sexn) ;
            json.put("Personal_ID", ID) ;
            json.put("Address", Area + Address) ;
            json.put("Take_Finance", Property) ;
            json.put("Take_Finance_Relationship" , RCustody) ;
            json.put("Finance_Details" , Custody) ;

            //現場狀況
            json.put("Site_Condition", Trauma_Switch) ;

            ParamStr = "";
            for(String Content: Trauma){
                if(!Content.equals("")) {
                    ParamStr += Content + "－";
                }
            }
            json.put("Hurt_Help_Reason", ParamStr) ;

            ParamStr = "";
            for(String Content: Trauma1){
                if(!Content.equals("")) {
                    ParamStr += Content + "－";
                }
            }
            json.put("Trauma_Site", ParamStr) ;

            ParamStr = "";
            for(String Content: Traffic){
                if(!Content.equals("")) {
                    ParamStr += Content + "－";
                }
            }
            json.put("Traffic_Accident_Category", ParamStr) ;

            ParamStr = "";
            for(String Content: NTrauma){
                if(!Content.equals("")) {
                    ParamStr += Content + "－";
                }
            }
            json.put("Not_Hurt_Help_Reason", ParamStr) ;

            ParamStr = "";
            for(String Content: NTrauma6){
                if(!Content.equals("")) {
                    ParamStr += Content + "－";
                }
            }
            json.put("General_Diseases", ParamStr) ;

            json.put("Have_Eyewitness", Witnesses) ;
            json.put("Have_CPR", CPR) ;
            json.put("Have_PAD", PAD) ;
            json.put("Have_ROSC", ROSC) ;
            json.put("Location_Type", PSpecies) ;
            json.put("Cincinnati", Stroke) ;
            json.put("Last_Normal_Time", LTime) ;
            json.put("Smile_Test", String.valueOf(Smile)) ;
            json.put("Lift_Arm_Test", String.valueOf(LArm)) ;
            json.put("Talk_Test", String.valueOf(Speech)) ;

            //急救處置
            ParamStr = "";
            for(String Content: Breathing){
                if(!Content.equals("")) {
                    ParamStr += Content + "－";
                }
            }
            json.put("Basic_Respiratory_Tract_Cure", ParamStr) ;

            ParamStr = "";
            for(String Content: TDisposal){
                if(!Content.equals("")) {
                    ParamStr += Content + "－";
                }
            }
            json.put("Trauma_Cure", ParamStr) ;

            ParamStr = "";
            for(String Content: CPRDisposal){
                if(!Content.equals("")) {
                    ParamStr += Content + "－";
                }
            }
            json.put("OHCA_Cure", ParamStr) ;

            ParamStr = "";
            for(String Content: CPRDisposal3){
                if(!Content.equals("")) {
                    ParamStr += Content + "－";
                }
            }
            json.put("HAVE_AED" , ParamStr) ;

            ParamStr = "";
            for(String Content: DDisposal){
                if(!Content.equals("")) {
                    ParamStr += Content + "－";
                }
            }
            json.put("Drug_Disposal", ParamStr) ;

            ParamStr = "";
            for(String Content: DDisposal1){
                if(!Content.equals("")) {
                    ParamStr += Content + "－";
                }
            }
            json.put("Infusion_Type_And_Dosage", ParamStr) ;

            ParamStr = "";
            for(String Content: ODisposal){
                if(!Content.equals("")) {
                    ParamStr += Content + "－";
                }
            }
            json.put("Other_Disposal", ParamStr) ;

            ParamStr = "";
            for(String Content: ALSDisposal){
                if(!Content.equals("")) {
                    ParamStr += Content + "－";
                }
            }
            json.put("ALS_Disposal", ParamStr) ;

            json.put("Specific_Posture", Posture) ;


            JSONArray arrayMTime = new JSONArray();
            arrayMTime.put(DrugUseTime1);
            arrayMTime.put(DrugUseTime2);
            arrayMTime.put(DrugUseTime3);
            arrayMTime.put(DrugUseTime4);
            json.put("Medication_Time", arrayMTime) ;
            JSONArray arrayMName = new JSONArray();
            arrayMName.put(DrugUseName1);
            arrayMName.put(DrugUseName2);
            arrayMName.put(DrugUseName3);
            arrayMName.put(DrugUseName4);
            json.put("Medication_Name", arrayMName) ;
            JSONArray arrayMDosel = new JSONArray();
            arrayMDosel.put(DrugUseDose1);
            arrayMDosel.put(DrugUseDose2);
            arrayMDosel.put(DrugUseDose3);
            arrayMDosel.put(DrugUseDose4);
            json.put("Medication_Drug_Route_And_Dosage", arrayMDosel) ;
            JSONArray arrayMuser = new JSONArray();
            arrayMuser.put(DrugUser1);
            arrayMuser.put(DrugUser2);
            arrayMuser.put(DrugUser3);
            arrayMuser.put(DrugUser4);
            json.put("Medication_Executor", arrayMuser) ;


            //病患相關
            json.put("Feel_Uncomfortable", Q1) ;
            json.put("Uncomfortable_Feeling", Q2) ;
            json.put("Uncomfortable_Time", Q3) ;
            json.put("Other_Uncomfortable", Q4) ;
            json.put("Remark", Q5) ;

            ParamStr = "";
            for(String Content: MHistory){
                if(!Content.equals("")) {
                    ParamStr += Content + "－";
                }
            }
            json.put("Disease_History", ParamStr) ;

            ParamStr = "";
            for(String Content: Allergies){
                if(!Content.equals("")) {
                    ParamStr += Content + "－";
                }
            }
            json.put("Allergy_History", ParamStr) ;

            json.put("Additional", People) ;

            //生命徵象
            JSONArray arrayLifeTime = new JSONArray();
            arrayLifeTime.put(LifeTime1);
            arrayLifeTime.put(LifeTime2);
            arrayLifeTime.put("");
            json.put("Measuring_Vital_Signs_Time", arrayLifeTime) ;

            JSONArray arrayConsciousness = new JSONArray();
            arrayConsciousness.put(Consciousness1);
            arrayConsciousness.put(Consciousness2);
            arrayConsciousness.put(Consciousness3);
            json.put("Awareness_Status", arrayConsciousness);

            JSONArray arrayLifeBreathing = new JSONArray();
            arrayLifeBreathing.put(LifeBreathing1);
            arrayLifeBreathing.put(LifeBreathing2);
            arrayLifeBreathing.put(LifeBreathing3);
            json.put("Breathe", arrayLifeBreathing);

            JSONArray arrayPulse = new JSONArray();
            arrayPulse.put(Pulse1);
            arrayPulse.put(Pulse2);
            arrayPulse.put(Pulse3);
            json.put("Pulse", arrayPulse);

            JSONArray arrayGCS_E = new JSONArray();
            arrayGCS_E.put(GCS1_E);
            arrayGCS_E.put(GCS2_E);
            arrayGCS_E.put(GCS3_E);
            json.put("GCS_E", arrayGCS_E);

            JSONArray arrayGCS_V = new JSONArray();
            arrayGCS_V.put(GCS1_V);
            arrayGCS_V.put(GCS2_V);
            arrayGCS_V.put(GCS3_V);
            json.put("GCS_V", arrayGCS_V);

            JSONArray arrayGCS_M = new JSONArray();
            arrayGCS_M.put(GCS1_M);
            arrayGCS_M.put(GCS2_M);
            arrayGCS_M.put(GCS3_M);
            json.put("GCS_M", arrayGCS_M);

            JSONArray arraySBP = new JSONArray();
            arraySBP.put(SBP1);
            arraySBP.put(SBP2);
            arraySBP.put(SBP3);
            json.put("Systolic_Pressure", arraySBP);

            JSONArray arrayDBP = new JSONArray();
            arrayDBP.put(DBP1);
            arrayDBP.put(DBP2);
            arrayDBP.put(DBP3);
            json.put("Diastolic_Pressure", arrayDBP);

            JSONArray arraySpO2 = new JSONArray();
            arraySpO2.put(SpO21);
            arraySpO2.put(SpO22);
            arraySpO2.put(SpO23);
            json.put("SPO2", arraySpO2);

            JSONArray arrayBPressure_A = new JSONArray();
            arrayBPressure_A.put(BPressure1_A);
            arrayBPressure_A.put(BPressure2_A);
            arrayBPressure_A.put(BPressure3_A);
            json.put("Arterial_Point", arrayBPressure_A);

            JSONArray arrayTemperature1 = new JSONArray();
            arrayTemperature1.put(Temperature1);
            arrayTemperature1.put(Temperature2);
            arrayTemperature1.put(Temperature3);
            json.put("Temp", arrayTemperature1);


            //手寫簽名

            JSONArray arrayAStaffNO = new JSONArray();
            arrayAStaffNO.put(AStaffNO1);
            arrayAStaffNO.put(AStaffNO2);
            arrayAStaffNO.put(AStaffNO3);
            arrayAStaffNO.put(AStaffNO4);
            arrayAStaffNO.put(AStaffNO5);
            arrayAStaffNO.put(AStaffNO6);
            json.put("Ambulance_Staff", arrayAStaffNO);

            json.put("Medical_StaffInjury_Point", IClassification) ;
            json.put("Is_Refused_To_Send_Hospital_Sign", String.valueOf(cb_RHospital)) ;
            json.put("Refused_Medical_Treatment_Statement", RHospital) ;//沒找到
            json.put("Refused_To_Send_Sign_Relationship", RRelationship) ;
            json.put("Arrival_Hospital_Sign_Relationship", Relationship) ;
            json.put("Phone", Phone);

            if(!NotifyType.equals("Null")) {
                json.put("Preview", PreviewMain);
                json.put("Notify_Type", NotifyType);
                json.put("Notify_Class", String.valueOf(PHospitalAddress + 1));
            }

            String input = json.toString();

            OutputStream os = conn.getOutputStream();
            os.write(input.getBytes());
            os.flush();


            if (conn.getResponseCode() != HttpURLConnection.HTTP_CREATED && conn.getResponseCode() != 200) {
                throw new RuntimeException("Failed : HTTP error code : "
                        + conn.getResponseCode());
            }


            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    (conn.getInputStream())));
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }

            jsons = sb.toString();
            Log.e("JSON", jsons);

            conn.disconnect();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("Buffer Error", "Error converting result " + e.toString());
        } catch (JSONException e) {
            Log.e("JSON Parser", "Error parsing data " + e.toString());
        }


        // try parse the string to a JSON object
        try {
            jObj = new JSONObject(jsons);
        } catch (JSONException e) {
            Log.e("JSON Parser", "Error parsing data " + e.toString());
        }

        // return JSON String
        return jObj;
    }

    public String GetResponse(HttpPost httppost, List<NameValuePair> params) {
        String str = "";
        if(checkNetworkConnected()) {
            try //因Java的規定，所以從這開始要用try..catch
            {
                httppost.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8)); //設定參數和編碼
                HttpParams httpParamseters = new BasicHttpParams();
                HttpConnectionParams.setConnectionTimeout(httpParamseters, 15000);
                HttpConnectionParams.setSoTimeout(httpParamseters, 15000);
                DefaultHttpClient httpClient = new DefaultHttpClient(httpParamseters);
                HttpResponse res = httpClient.execute(httppost); //執行並接收Server回傳的Page值
                //if (res.getStatusLine( ).getStatusCode( ) == 200 ){ //判斷回傳的狀態是不是200
                str = EntityUtils.toString(res.getEntity()); //取出Server回傳的Code:使用者名稱及Email
                //}
            } catch (ConnectTimeoutException e) {
                str = "Overtime";
            } catch (Exception e) {
                str = "Error";
            }
        }
        else {
            str = "Overtime";
        }
        return str;
    }

    private boolean checkNetworkConnected() {
        boolean result = false;
        ConnectivityManager CM = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        if (CM == null) {
            result = false;
        } else {
            NetworkInfo info = CM.getActiveNetworkInfo();
            if (info != null && info.isConnected()) {
                if (!info.isAvailable()) {
                    result = false;
                } else {
                    result = true;
                }
                //Log.e("log_tag", "[目前連線方式]"+info.getTypeName());
                //Log.e("log_tag", "[目前連線狀態]"+info.getState());
                //Log.e("log_tag", "[目前網路是否可使用]"+info.isAvailable());
                //Log.e("log_tag", "[網路是否已連接]"+info.isConnected());
                //Log.e("log_tag", "[網路是否已連接 或 連線中]"+info.isConnectedOrConnecting());
                //Log.e("log_tag", "[網路目前是否有問題 ]"+info.isFailover());
                //Log.e("log_tag", "[網路目前是否在漫遊中]"+info.isRoaming());
            }
        }
        return result;
    }


    public String UserID = "HSSG";

    public boolean BleTemp = true;
    public boolean BleBG = true;
    public boolean BleBP = true;
    public boolean BleSPO2 = true;
    public boolean BleBW =true;
    public boolean BleECG =true;
    public boolean BleUA =true;
    public boolean BleCHOL =true;


    public static final String SHARED_PREFERENCES_NAME = "com.taidoc.pclinklibrary.demo";


    public static final String BLE_PAIRED_METER_ADDR_ = "BLE_PAIRED_METER_ADDR_";
    public static final String BLE_PAIRED_BT_ADDREES = "BLE_PAIRED_BT_METER_ADDR";
    public static final String BLE_PAIRED_BG_ADDREES = "BLE_PAIRED_BG_METER_ADDR";
    public static final String BLE_PAIRED_BG_NUMBER = "BLE_PAIRED_BG_NUMBER";
    public static final String BLE_PAIRED_BP_ADDREES = "BLE_PAIRED_BP_METER_ADDR";
    public static final String BLE_PAIRED_SPO2_ADDREES = "BLE_PAIRED_SPO2_METER_ADDR";
    public static final String BLE_PAIRED_BW_ADDREES = "BLE_PAIRED_BW_METER_ADDR";
    public static final String BLE_PAIRED_BCU_ADDREES = "BLE_PAIRED_BW_METER_ADDR";

    public static final String ECGTIME = "ECGTIME";
}
