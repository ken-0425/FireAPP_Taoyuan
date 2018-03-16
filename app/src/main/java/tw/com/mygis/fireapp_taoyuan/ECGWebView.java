package tw.com.mygis.fireapp_taoyuan;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

/**
 * Created by HSS-sysb1 on 2018/3/7.
 */

public class ECGWebView extends AppCompatActivity {

    //入口2
    private static final String Web_URL = "http://192.168.0.107:8080/Firework";
    private WebView webView;
    private long exitTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.webview);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        String urlString="http://192.168.0.107:8080/Firework";
        Intent intent=new Intent(Intent.ACTION_VIEW, Uri.parse(urlString));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setPackage("com.android.chrome");
/*
        try {
            getBaseContext().startActivity(intent);
        } catch (ActivityNotFoundException ex) {
            // Chrome browser presumably not installed so allow user to choose instead
            intent.setPackage(null);
            getBaseContext().startActivity(intent);

        }
*/



           setupWebView();
    }

    private void setupWebView() {
        webView = (WebView) findViewById(R.id.webview01);

        webView.getSettings().setUseWideViewPort(true);//設定支持viewport
        webView.getSettings().setLoadWithOverviewMode(true);   //自適應屏幕
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setDisplayZoomControls(false);
        webView.getSettings().setSupportZoom(true);//設定支持縮放
        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setDatabaseEnabled(true);


        //   webView.getSettings().supportMultipleWindows();//多窗口
        //   webView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);//关闭webview中缓存
        webView.getSettings().setAllowFileAccess(true);//设置可以访问文件
        webView.getSettings().setNeedInitialFocus(true);//当webview调用requestFocus时为webview设置节点
        webView.getSettings().setLoadsImagesAutomatically(true);//支持自动加载图片


        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);//设置允许js弹出alert对话框


        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
                //return super.onJsAlert(view, url, message, result);
                Toast.makeText(ECGWebView.this, message, Toast.LENGTH_LONG).show();
                return true;
            }
        });


        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl(Web_URL);

    }


    //我們需要重寫回退按鈕的時間,當用戶點擊回退按鈕：
    //1.webView.canGoBack()判斷網頁是否能後退,可以則goback()
    //2.如果不可以連續點擊兩次退出App,否則彈出提示Toast
    @Override
    public void onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack();
        } else {
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                Toast.makeText(getApplicationContext(), "再按一次回到程式",
                        Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                startActivity(new Intent(getBaseContext(), MainActivity.class));
                finish();
            }

        }
    }




}