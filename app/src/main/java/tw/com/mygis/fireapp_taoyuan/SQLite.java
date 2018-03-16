package tw.com.mygis.fireapp_taoyuan;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by choey on 2014/4/15.
 */
public class SQLite extends SQLiteOpenHelper {

    private final static int _DBVersion = 1; //<-- 版本
    private final static String _DBName = "FireFighting";
    private final static String _MainCases = "MainCases";
    private final static String _MainTab1 = "MainTab1";
    private final static String _MainTab2 = "MainTab2";
    private final static String _MainTab3 = "MainTab3";
    private final static String _MainTab4 = "MainTab4";
    private final static String _MainTab5 = "MainTab5";
    private final static String _MainHandWrite = "MainHandWrite";

    public SQLite(Context context) {
        super(context, _DBName, null, _DBVersion);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        String CaseCountSQL = "CREATE TABLE IF NOT EXISTS " + _MainCases + "( " +
                "id INTEGER PRIMARY KEY, " +
                "CaseID VARCHAR(30), " +
                "CaseSubType VARCHAR(30), " +
                "DispatchTime VARCHAR(30), " +
                "ReportPlace VARCHAR(60), " +
                "DispVehicleID VARCHAR(15), " +
                "Coordinate VARCHAR(60), " +
                "Car_Index VARCHAR(100), " +
                "Name VARCHAR(30), " +
                "Sex VARCHAR(15), " +
                "Phone VARCHAR(15), " +
                "Identity VARCHAR(15), " +
                "Age VARCHAR(15)" +
                ");";
        db.execSQL(CaseCountSQL);

        CaseCountSQL = "CREATE TABLE IF NOT EXISTS " + _MainTab1 + "( " +
                "id INTEGER PRIMARY KEY, " +
                "CaseID VARCHAR(30), " +
                "Subno INTEGER, " +
                "FNumber VARCHAR(30), " +
                "LoadPDF VARCHAR(15), " +
                "Critical INTEGER, " +
                "Date VARCHAR(30), " +
                "AttendanceUnit VARCHAR(15), " +
                "AcceptedUnit VARCHAR(15), " +
                "LocationHappen VARCHAR(60), " +
                "AssistanceUnit VARCHAR(15), " +
                "HospitalAddress VARCHAR(15), " +
                "PHospitalAddress INTEGER, " +
                "ReasonHospital VARCHAR(15), " +
                "MHospital VARCHAR(60), " +
                "Time1 VARCHAR(10), " +
                "Time2 VARCHAR(10), " +
                "Time3 VARCHAR(10), " +
                "Time4 VARCHAR(10), " +
                "Time5 VARCHAR(10), " +
                "Time6 VARCHAR(10), " +
                "Name VARCHAR(10)," +
                "AgeM INTEGER, " +
                "AgeA INTEGER, " +
                "Age VARCHAR(5), " +
                "Sex VARCHAR(5), " +
                "Identity VARCHAR(15), " +
                "Area VARCHAR(15), " +
                "PArea INTEGER, " +
                "Address VARCHAR(60), " +
                "Property VARCHAR(5), " +
                "RCustody VARCHAR(15), " +
                "PRCustody INTEGER, " +
                "Custody VARCHAR(15), " +
                "SCustody TEXT, " +
                "I_SCustody BLOB " +
                ");";
        db.execSQL(CaseCountSQL);

        CaseCountSQL = "CREATE TABLE IF NOT EXISTS " + _MainTab2 + "( " +
                "id INTEGER PRIMARY KEY, " +
                "CaseID VARCHAR(30), " +
                "Subno INTEGER, " +
                "Trauma_Switch VARCHAR(15), " +
                "Trauma TEXT, " +
                "Trauma1 TEXT, " +
                "Traffic TEXT, " +
                "NTrauma TEXT, " +
                "NTrauma6 TEXT, " +
                "Witnesses VARCHAR(15), " +
                "CPR VARCHAR(15), " +
                "PAD VARCHAR(15), " +
                "ROSC VARCHAR(30), " +
                "PSpecies VARCHAR(15), " +
                "Stroke VARCHAR(15), " +
                "LTime VARCHAR(15), " +
                "Smile INTEGER, " +
                "LArm INTEGER, " +
                "Speech INTEGER " +
                ");";
        db.execSQL(CaseCountSQL);

        CaseCountSQL = "CREATE TABLE IF NOT EXISTS " + _MainTab3 + "( " +
                "id INTEGER PRIMARY KEY, " +
                "CaseID VARCHAR(30), " +
                "Subno INTEGER, " +
                "Breathing TEXT, " +
                "TDisposal TEXT, " +
                "CPRDisposal TEXT, " +
                "CPRDisposal3 TEXT, " +
                "DDisposal TEXT, " +
                "DDisposal1 TEXT, " +
                "ODisposal TEXT, " +
                "ALSDisposal TEXT, " +
                "Posture VARCHAR(15), " +
                "PPosture INTEGER, " +
                "DrugUseTime1 VARCHAR(15), " +
                "DrugUseName1 VARCHAR(15), " +
                "PDrugUseName1 INTEGER, " +
                "DrugUseDose1 VARCHAR(15), " +
                "PDrugUseDose1 INTEGER, " +
                "DrugUser1 VARCHAR(15), " +
                "DrugUseTime2 VARCHAR(15), " +
                "DrugUseName2 VARCHAR(15), " +
                "PDrugUseName2 INTEGER, " +
                "DrugUseDose2 VARCHAR(15), " +
                "PDrugUseDose2 INTEGER, " +
                "DrugUser2 VARCHAR(15), " +
                "DrugUseTime3 VARCHAR(15), " +
                "DrugUseName3 VARCHAR(15), " +
                "PDrugUseName3 INTEGER, " +
                "DrugUseDose3 VARCHAR(15), " +
                "PDrugUseDose3 INTEGER, " +
                "DrugUser3 VARCHAR(15), " +
                "DrugUseTime4 VARCHAR(15), " +
                "DrugUseName4 VARCHAR(15), " +
                "PDrugUseName4 INTEGER, " +
                "DrugUseDose4 VARCHAR(15), " +
                "PDrugUseDose4 INTEGER, " +
                "DrugUser4 VARCHAR(15) " +
                ");";
        db.execSQL(CaseCountSQL);

        CaseCountSQL = "CREATE TABLE IF NOT EXISTS " + _MainTab4 + "( " +
                "id INTEGER PRIMARY KEY, " +
                "CaseID VARCHAR(30), " +
                "Subno INTEGER, " +
                "Q1 TEXT, " +
                "Q2 TEXT, " +
                "Q3 TEXT, " +
                "Q4 TEXT, " +
                "Q5 TEXT, " +
                "MHistory TEXT, " +
                "Allergies TEXT, " +
                "People TEXT, " +
                "SPeople TEXT, " +
                "Switch_People TEXT " +
                ");";
        db.execSQL(CaseCountSQL);

        CaseCountSQL = "CREATE TABLE IF NOT EXISTS " + _MainTab5 + "( " +
                "id INTEGER PRIMARY KEY, " +
                "CaseID VARCHAR(30), " +
                "Subno INTEGER, " +
                "LifeTime1 VARCHAR(10), " +
                "Consciousness1 VARCHAR(5), " +
                "PConsciousness1 INTEGER, " +
                "LifeBreathing1 VARCHAR(10), " +
                "Pulse1 VARCHAR(10), " +
                "GCS1_E VARCHAR(5), " +
                "GCS1_V VARCHAR(5), " +
                "GCS1_M VARCHAR(5), " +
                "SBP1 VARCHAR(10), " +
                "DBP1 VARCHAR(10), " +
                "BPressure1_A VARCHAR(10), " +
                "PBPressure1_A INTEGER, " +
                "SpO21 VARCHAR(10), " +
                "Temperature1 VARCHAR(10), " +
                "LifeTime2 VARCHAR(10), " +
                "Consciousness2 VARCHAR(5), " +
                "PConsciousness2 INTEGER, " +
                "LifeBreathing2 VARCHAR(10), " +
                "Pulse2 VARCHAR(10), " +
                "GCS2_E VARCHAR(5), " +
                "GCS2_V VARCHAR(5), " +
                "GCS2_M VARCHAR(5), " +
                "SBP2 VARCHAR(10), " +
                "DBP2 VARCHAR(10), " +
                "BPressure2_A VARCHAR(10), " +
                "PBPressure2_A INTEGER, " +
                "SpO22 VARCHAR(10), " +
                "Temperature2 VARCHAR(10), " +
                "Consciousness3 VARCHAR(5), " +
                "PConsciousness3 INTEGER, " +
                "LifeBreathing3 VARCHAR(10), " +
                "Pulse3 VARCHAR(10), " +
                "GCS3_E VARCHAR(5), " +
                "GCS3_V VARCHAR(5), " +
                "GCS3_M VARCHAR(5), " +
                "SBP3 VARCHAR(10), " +
                "DBP3 VARCHAR(10), " +
                "BPressure3_A VARCHAR(10), " +
                "PBPressure3_A INTEGER, " +
                "SpO23 VARCHAR(10), " +
                "Temperature3 VARCHAR(10) " +
                ");";
        db.execSQL(CaseCountSQL);

        CaseCountSQL = "CREATE TABLE IF NOT EXISTS " + _MainHandWrite + "( " +
                "id INTEGER PRIMARY KEY, " +
                "CaseID VARCHAR(30), " +
                "Subno INTEGER, " +
                "SNote TEXT, " +
                "I_SNote BLOB, " +
                "AStaffNO1 VARCHAR(20), " +
                "SAStaff1 TEXT, " +
                "I_SAStaff1 BLOB, " +
                "AStaffNO2 VARCHAR(20), " +
                "SAStaff2 TEXT, " +
                "I_SAStaff2 BLOB, " +
                "AStaffNO3 VARCHAR(20), " +
                "SAStaff3 TEXT, " +
                "I_SAStaff3 BLOB, " +
                "AStaffNO4 VARCHAR(20), " +
                "SAStaff4 TEXT, " +
                "I_SAStaff4 BLOB, " +
                "AStaffNO5 VARCHAR(20)," +
                "SAStaff5 TEXT, " +
                "I_SAStaff5 BLOB, " +
                "AStaffNO6 VARCHAR(20), " +
                "SAStaff6 TEXT, " +
                "I_SAStaff6 BLOB, " +
                "IClassification VARCHAR(30), " +
                "SHStaff TEXT, " +
                "I_SHStaff BLOB, " +
                "cb_RHospital INTEGER, " +
                "RHospital VARCHAR(30), " +
                "RRelationship VARCHAR(15), " +
                "PRRelationship INTEGER, " +
                "SRRelationship TEXT, " +
                "I_SRRelationship BLOB, " +
                "Relationship VARCHAR(15), " +
                "PRelationship INTEGER, " +
                "Phone VARCHAR(30), " +
                "SRelationship TEXT, " +
                "I_SRelationship BLOB " +
                ");";
        db.execSQL(CaseCountSQL);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        if (newVersion > oldVersion) {
            db.beginTransaction();//建立交易

            boolean success = false;//判斷參數

            //由之前不用的版本，可做不同的動作
            //switch (oldVersion) {
            //    case 1:
            //        db.execSQL("ALTER TABLE R_DB ADD COLUMN R_2_13 VARCHAR(30)");
            //        oldVersion++;
            //        success = true;
            //        break;
            //}

            if (success) {
                db.setTransactionSuccessful();//正確交易才成功
            }
            db.endTransaction();
        }
        else {
            onCreate(db);
        }
    }
}
