/**
 * Author: Ravi Tamada
 * URL: www.androidhive.info
 * twitter: http://twitter.com/ravitamada
 */
package library;

import android.content.Context;
import android.util.Log;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class UserFunctions {

    private static String loginURL = "http://114.34.209.6/~hssg/android/index.php";
    private static String login02URL = "http://114.34.209.6/~hssg/tw/login.php";
    private static String registerURL = "http://192.168.0.106/android/index.php";
    private static String readURL = "http://192.168.0.106/android/index.php";
    private static String writeURL = "http://192.168.0.106/android/index.php";
    private static String readURL2 = "http://192.168.0.106/android/index.php";
    private static String login_tag = "login";
    private static String login_address = "login_address";

    private static String writebloodsugar="writebloodsugar";

    private static String readbloodsugar = "readbloodsugar";
    private static String register_tag = "register";
    private static String readlocation_tag = "readlocation";
    private static String readcustomer_tag = "readcustomer";
    private static String writelocation_tag = "writelocation";
    private static String write = "write";
    private static String write2 = "write2";
    private static String readmysendletter_tag = "readmysendletter";
    private static String changejudgment_tag = "changejudgment";
    private static String readmydispatcher_tag = "readmydispatcher";
    private static String deletemyletter_tag = "deletemyletter";
    private JSONParser jsonParser;


    // constructor
    public UserFunctions() {
        jsonParser = new JSONParser();
    }

    /**
     * function make Login Request
     * @param Number
     * @param Password
     * */
    public JSONObject loginUser(String Number, String Password) {
        // Building Parameters
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("tag", login_tag));
        params.add(new BasicNameValuePair("Number", Number));
        params.add(new BasicNameValuePair("Password", Password));

        JSONObject json = jsonParser.getJSONFromUrl(loginURL, params);
        // return json
        // Log.e("JSON", json.toString());
        return json;
    }

    //測試失敗
    public JSONObject logintest(String ML001, String ML002) {
        // Building Parameters
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("tag", login_tag));
        params.add(new BasicNameValuePair("user_ID", ML001));
        params.add(new BasicNameValuePair("Password", ML002));

        JSONObject json = jsonParser.getJSONFromUrl(login02URL, params);
        // return json
        // Log.e("JSON", json.toString());
        return json;
    }

//address 登入
    public JSONObject loginAddress(String macaddress) {
        // Building Parameters
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("tag", login_address));
        params.add(new BasicNameValuePair(" macaddress",  macaddress));


        JSONObject json = jsonParser.getJSONFromUrl(loginURL, params);
        // return json
        // Log.e("JSON", json.toString());
        return json;
    }


//寫入血糖資料
    public JSONObject bloodsugarwrite(String Number, String Bleaddress, String value, String eatinfo) {
        // Building Parameters
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("tag", writebloodsugar));
        params.add(new BasicNameValuePair("Number", Number));
        params.add(new BasicNameValuePair("Bleaddress", Bleaddress));
        params.add(new BasicNameValuePair("value", value));
        params.add(new BasicNameValuePair("eatinfo", eatinfo));


        // getting JSON Object
        JSONObject json = jsonParser.getJSONFromUrl(loginURL, params);
        // return json
        return json;
    }



//讀血糖紀錄
    public JSONObject bloodsugar(String number) {
        // Building Parameters
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("tag", readbloodsugar));
        params.add(new BasicNameValuePair("number", number));

        JSONObject json = jsonParser.getJSONFromUrl(loginURL, params);

        // return json
        // Log.e("JSON", json.toString());
        return json;
    }


    /**
     * function make Login Request
     * @param phone
     * @param name
     * @param gender
     * @param email
     * @param address
     * @param password
     * */
    public JSONObject registerUser(String phone, String name, String gender, String email, String address, String password) {
        // Building Parameters
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("tag", register_tag));
        params.add(new BasicNameValuePair("phone", phone));
        params.add(new BasicNameValuePair("name", name));
        params.add(new BasicNameValuePair("gender", gender));
        params.add(new BasicNameValuePair("email", email));
        params.add(new BasicNameValuePair("address", address));
        params.add(new BasicNameValuePair("password", password));


        // getting JSON Object
        JSONObject json = jsonParser.getJSONFromUrl(registerURL, params);
        // return json
        return json;
    }

    //讀取座標
    public JSONObject readlocation(String phone) {
        // Building Parameters
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("tag", readlocation_tag));
        params.add(new BasicNameValuePair("phone", phone));


        JSONObject json = jsonParser.getJSONFromUrl(loginURL, params);
        // return json
        // Log.e("JSON", json.toString());
        return json;
    }

    //讀取個人資料
    public JSONObject readcustomer(String phone) {
        // Building Parameters
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("tag", readcustomer_tag));
        params.add(new BasicNameValuePair("phone", phone));


        JSONObject json = jsonParser.getJSONFromUrl(readURL, params);
        // return json
        // Log.e("JSON", json.toString());
        return json;
    }

    //寫入座標
    public JSONObject writelocation(String phone, String longitude, String latitude) {
        // Building Parameters
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("tag", writelocation_tag));
        params.add(new BasicNameValuePair("phone", phone));
        params.add(new BasicNameValuePair("longitude", longitude));
        params.add(new BasicNameValuePair("latitude", latitude));

        JSONObject json = jsonParser.getJSONFromUrl(writeURL, params);
        // return json
        // Log.e("JSON", json.toString());
        return json;
    }

    //搶單
    public JSONObject change_judgment(String id, String dispatcher, String Judgment) {
        // Building Parameters
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("tag", changejudgment_tag));
        params.add(new BasicNameValuePair("id", id));
        params.add(new BasicNameValuePair("dispatcher", dispatcher));
        params.add(new BasicNameValuePair("Judgment", Judgment));


        JSONObject json = jsonParser.getJSONFromUrl(writeURL, params);
        // return json
        // Log.e("JSON", json.toString());
        return json;
    }

    //刪除我的寄件單
    public JSONObject delete_myletter(String id, String phone) {
        // Building Parameters
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("tag", deletemyletter_tag));
        params.add(new BasicNameValuePair("id", id));
        params.add(new BasicNameValuePair("phone", phone));


        JSONObject json = jsonParser.getJSONFromUrl(writeURL, params);
        // return json
        // Log.e("JSON", json.toString());
        return json;
    }


    //我的訂單
    public JSONObject readmysendletter(String phone, String Judgment) {
        // Building Parameters
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("tag", readmysendletter_tag));
        params.add(new BasicNameValuePair("phone", phone));
        params.add(new BasicNameValuePair("Judgment", Judgment));

        JSONObject json = jsonParser.getJSONFromUrl(readURL2, params);

        // return json
        // Log.e("JSON", json.toString());
        return json;
    }


    //抓我的貨物的送貨員電話
    public JSONObject readmydispatcher(String phone, String Judgment) {
        // Building Parameters
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("tag", readmydispatcher_tag));
        params.add(new BasicNameValuePair("phone", phone));
        params.add(new BasicNameValuePair("Judgment", Judgment));

        JSONObject json = jsonParser.getJSONFromUrl(readURL, params);

        // return json
        // Log.e("JSON", json.toString());
        return json;
    }


    //顯示訂單可以搶單的
    public JSONObject readdelivery(String Judgment) {
        // Building Parameters
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("tag", write));
        params.add(new BasicNameValuePair("Judgment", Judgment));

        JSONObject json = jsonParser.getJSONFromUrl(readURL2, params);

        // return json
        // Log.e("JSON", json.toString());
        return json;
    }





    //顯示訂單已搶單的
    public JSONObject readdelivery2(String phone, String Judgment) {
        // Building Parameters
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("tag", write2));
        params.add(new BasicNameValuePair("phone", phone));
        params.add(new BasicNameValuePair("Judgment", Judgment));

        JSONObject json = jsonParser.getJSONFromUrl(readURL2, params);

        // return json
        // Log.e("JSON", json.toString());
        return json;
    }


    /**
     * Function get Login status
     * */
    public String getphone(Context context) {
        DatabaseHandler db = new DatabaseHandler(context);
        String phone = db.getuserphone();

        return phone;
    }


    /**
     * Function get Login status
     * */
    public boolean isUserLoggedIn(Context context) {
        DatabaseHandler db = new DatabaseHandler(context);
        int count = db.getRowCount();
        Log.e("test", count + "");
        if (count > 0) {
            // user logged in

            return true;
        }
        return false;
    }

    /**
     * Function to logout user
     * Reset Database
     * */
    public boolean logoutUser(Context context) {
        DatabaseHandler db = new DatabaseHandler(context);
        db.resetTables();
        return true;
    }

}
