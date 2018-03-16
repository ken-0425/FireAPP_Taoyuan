package library;

import android.util.Log;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by HSS-sysb1 on 2018/2/2.
 */



public class JSONPost {


    static JSONObject jObj = null;
    static String jsons = "";


    //取得大隊資料
    public JSONObject R_SPCAR2() {
        try {

            URL url = new URL("http://192.168.0.107:3000/phone/load_brigade");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            JSONObject json = new JSONObject();

            Date date = new Date();


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

    //取的案件資料
    public JSONObject R_GetCase(String Brigade,String GroupType) {
        try {

            URL url = new URL("http://192.168.0.107:3000/phone/load_case");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            JSONObject json = new JSONObject();

            Date date = new Date();


            //  JSONArray arrayvalue = new JSONArray();
            //  arrayvalue.put(value);


            json.put("Brigade", Brigade);
            json.put("GroupType", GroupType);


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


    //上傳資料
    public JSONObject registerAddress(String id, String password, String email, String address) {
        try {


            URL url = new URL("http://192.168.0.107:8080/phone/addmac");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            JSONObject json = new JSONObject();



            json.put("Account", id);
            json.put("Password", password);
            json.put("Email", email);
            json.put("Address", address);

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


    //登入
    public JSONObject login(String id, String password) {
        try {

            URL url = new URL("http://192.168.0.107:8080/phone/login");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            JSONObject json = new JSONObject();


            json.put("Account", id);
            json.put("Password", password);


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


    //取得設備服務權限
    public JSONObject loginService(String id) {
        try {

            URL url = new URL("http://192.168.0.107:8080/phone/getservices");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            JSONObject json = new JSONObject();


            json.put("Account", id);


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


    //設備驗證
    public JSONObject getBleAddress(String id, String address) {
        try {

            URL url = new URL("http://192.168.0.107:8080/phone/checkdevice");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            JSONObject json = new JSONObject();


            json.put("Account", id);
            json.put("Address", address);


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

    //上傳血糖資料
    public JSONObject uploadBG(String id, String value, String longitude, String latitude, String eatinfo) {
        try {

            URL url = new URL("http://192.168.0.107:8080/phone/uploadmeasurement");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            JSONObject json = new JSONObject();


            json.put("Account", id);

            JSONArray arrayvalue = new JSONArray();
            arrayvalue.put(value);

            json.put("Upload", arrayvalue);
            json.put("Category", "BG");
            json.put("Class", eatinfo);
            json.put("Longitude", longitude);
            json.put("Latitude", latitude);

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


    //讀取血糖資料
    public JSONObject readBG(String id) {
        try {

            URL url = new URL("http://192.168.0.107:8080/phone/loadmeasurement");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            JSONObject json = new JSONObject();


            json.put("Account", id);
            json.put("Category", "BG");


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


    //上傳體溫資料
    public JSONObject uploadBT(String id, String value, String longitude, String latitude, String info) {
        try {

            URL url = new URL("http://192.168.0.107:8080/phone/uploadmeasurement");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            JSONObject json = new JSONObject();


            json.put("Account", id);

            JSONArray arrayvalue = new JSONArray();
            arrayvalue.put(value);

            json.put("Upload", arrayvalue);
            json.put("Category", "Temp");
            json.put("Class", info);
            json.put("Longitude", longitude);
            json.put("Latitude", latitude);

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


    //讀取體溫資料
    public JSONObject readTemp(String id) {
        try {

            URL url = new URL("http://192.168.0.107:8080/phone/loadmeasurement");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            JSONObject json = new JSONObject();


            json.put("Account", id);
            json.put("Category", "Temp");


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


    //上傳血壓資料
    public JSONObject uploadBP(String id, String value1, String value2, String value3, String longitude, String latitude, String info) {
        try {

            URL url = new URL("http://192.168.0.107:8080/phone/uploadmeasurement");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            JSONObject json = new JSONObject();


            json.put("Account", id);

            JSONArray arrayvalue = new JSONArray();
            arrayvalue.put(value1);
            arrayvalue.put(value2);
            arrayvalue.put(value3);

            json.put("Upload", arrayvalue);
            json.put("Category", "BP");
            json.put("Class", info);
            json.put("Longitude", longitude);
            json.put("Latitude", latitude);

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


    //讀取血壓資料
    public JSONObject readBP(String id) {
        try {

            URL url = new URL("http://192.168.0.107:8080/phone/loadmeasurement");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            JSONObject json = new JSONObject();


            json.put("Account", id);
            json.put("Category", "BP");


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


    //上傳血氧資料
    public JSONObject uploadSPO2(String id, String value01, String value02, String longitude, String latitude, String info) {
        try {

            URL url = new URL("http://192.168.0.107:8080/phone/uploadmeasurement");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            JSONObject json = new JSONObject();


            json.put("Account", id);

            JSONArray arrayvalue = new JSONArray();
            arrayvalue.put(value01);
            arrayvalue.put(value02);

            json.put("Upload", arrayvalue);
            json.put("Category", "SPO2");
            json.put("Class", info);
            json.put("Longitude", longitude);
            json.put("Latitude", latitude);

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


    //讀取血氧資料
    public JSONObject readSPO2(String id) {
        try {

            URL url = new URL("http://192.168.0.107:8080/phone/loadmeasurement");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            JSONObject json = new JSONObject();


            json.put("Account", id);
            json.put("Category", "SPO2");


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


    //上傳體重資料
    public JSONObject uploadBW(String id, String value01, String value02, String longitude, String latitude, String info) {
        try {


            URL url = new URL("http://192.168.0.107:8080/phone/uploadmeasurement");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            JSONObject json = new JSONObject();


            json.put("Account", id);

            JSONArray arrayvalue = new JSONArray();
            arrayvalue.put(value01);
            arrayvalue.put(value02);

            json.put("Upload", arrayvalue);
            json.put("Category", "BW");
            json.put("Class", info);
            json.put("Longitude", longitude);
            json.put("Latitude", latitude);

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


    //讀取體重資料
    public JSONObject readBW(String id) {
        try {

            URL url = new URL("http://192.168.0.107:8080/phone/loadmeasurement");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            JSONObject json = new JSONObject();


            json.put("Account", id);
            json.put("Category", "BW");


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


    //上傳ECG資料
    public JSONObject uploadECG(String id,
                                ArrayList<String> value1, ArrayList<String> value2, ArrayList<String> value3,
                                ArrayList<String> value4, ArrayList<String> value5, ArrayList<String> value6, String info)

    {
        try {

            URL url = new URL("http://192.168.0.107:8080/firework/uploadmeasurement");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            JSONObject json = new JSONObject();


            JSONArray arrayvalue = new JSONArray();
            JSONArray arrayvalue2 = new JSONArray();
            JSONArray arrayvalue3 = new JSONArray();
            JSONArray arrayvalue4 = new JSONArray();
            JSONArray arrayvalue5 = new JSONArray();
            JSONArray arrayvalue6 = new JSONArray();
            for (int a = 0; a < value1.size(); a++) {
                arrayvalue.put(value1.get(a));
                arrayvalue2.put(value2.get(a));
                arrayvalue3.put(value3.get(a));
                arrayvalue4.put(value4.get(a));
                arrayvalue5.put(value5.get(a));
                arrayvalue6.put(value6.get(a));

            }

            JSONArray arrayvalueFish = new JSONArray();
            arrayvalueFish.put(arrayvalue);
            arrayvalueFish.put(arrayvalue2);
            arrayvalueFish.put(arrayvalue3);
            arrayvalueFish.put(arrayvalue4);
            arrayvalueFish.put(arrayvalue5);
            arrayvalueFish.put(arrayvalue6);


            json.put("CaseNum", id);

            json.put("Upload", arrayvalueFish);

            json.put("Category", "ECG");
            json.put("Class", info);



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







    //上傳尿酸
    public JSONObject uploadUA(String id, String value, String longitude, String latitude, String info) {
        try {

            URL url = new URL("http://192.168.0.107:8080/phone/uploadmeasurement");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            JSONObject json = new JSONObject();


            json.put("Account", id);

            JSONArray arrayvalue = new JSONArray();
            arrayvalue.put(value);

            json.put("Upload", arrayvalue);
            json.put("Category", "UA");
            json.put("Class", info);
            json.put("Longitude", longitude);
            json.put("Latitude", latitude);

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


    //讀取尿酸資料
    public JSONObject readUA(String id) {
        try {

            URL url = new URL("http://192.168.0.107:8080/phone/loadmeasurement");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            JSONObject json = new JSONObject();


            json.put("Account", id);
            json.put("Category", "UA");


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


    //上傳膽固醇
    public JSONObject uploadCHOL(String id, String value, String longitude, String latitude, String info) {
        try {

            URL url = new URL("http://192.168.0.107:8080/phone/uploadmeasurement");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            JSONObject json = new JSONObject();


            json.put("Account", id);

            JSONArray arrayvalue = new JSONArray();
            arrayvalue.put(value);

            json.put("Upload", arrayvalue);
            json.put("Category", "CHOL");
            json.put("Class", info);
            json.put("Longitude", longitude);
            json.put("Latitude", latitude);

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


    //讀取尿酸資料
    public JSONObject readCHOL(String id) {
        try {

            URL url = new URL("http://192.168.0.107:8080/phone/loadmeasurement");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            JSONObject json = new JSONObject();


            json.put("Account", id);
            json.put("Category", "CHOL");


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


    //上傳訊息
    public JSONObject sendMessage(String id, String personnel, String title, String message, String longitude, String latitude) {
        try {


            URL url = new URL("http://192.168.0.107:8080/phone/uploadmeasurement");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            JSONObject json = new JSONObject();


            json.put("Account", id);

            json.put("Upload", personnel);
            json.put("Category",title);
            json.put("Class", message);
            json.put("Longitude", longitude);
            json.put("Latitude", latitude);

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

}
