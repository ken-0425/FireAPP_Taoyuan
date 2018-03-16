package tw.com.mygis.fireapp_taoyuan;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PointF;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;

import static android.os.Environment.DIRECTORY_DOWNLOADS;

/**
 * Created by HSS-sysb1 on 2018/3/7.
 */

public class ECGImageView extends AppCompatActivity  {
    private ImageView img;
    private Button btn;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.imageview);



        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setLogo(R.drawable.toolbar_ic);

        GlobalVariable globalVariable = (GlobalVariable) getApplicationContext();
        String   number = globalVariable.FNumber;
        toolbar.setTitle(number);
        setSupportActionBar(toolbar);

        //顯示SDCard圖片的ImageView與進行讀取SDCard圖片的Button
        img = (ImageView) findViewById(R.id.image);




        File sdCardDir = Environment.getExternalStoragePublicDirectory(DIRECTORY_DOWNLOADS);//獲取SDCard目錄
        String sdDir = sdCardDir.toString();//轉字串

       // String SdDirName= sdDir+number+".png";

        String SdDirName= "Download/"+number+".png";

                //確認是否有插入SDCard
                if(checkSDCard())
                {
                    //帶入SDCard內的圖片路徑(SDCard: DCIM資料夾/100MEDIA資料夾/001圖片)
                    img.setImageBitmap(getBitmapFromSDCard(SdDirName));
                }
                else Toast.makeText(ECGImageView.this,
                        "尚未插入SDCard",
                        Toast.LENGTH_SHORT).show();
            }



    //確認是否有插入SDCard
    private static boolean checkSDCard()
    {
        if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
        {
            return true;
        }
        return false;
    }

    //讀取SDCard圖片，型態為Bitmap
    private static Bitmap getBitmapFromSDCard(String file)
    {
        try
        {
            String sd = Environment.getExternalStorageDirectory().toString();
            Bitmap bitmap = BitmapFactory.decodeFile(sd + "/" + file);
            return bitmap;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }

    public void onBackPressed() {

        startActivity(new Intent(getBaseContext(), MainActivity.class));
        finish();


    }
}