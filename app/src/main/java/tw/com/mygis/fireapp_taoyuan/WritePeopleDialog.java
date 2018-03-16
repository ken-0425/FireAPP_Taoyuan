package tw.com.mygis.fireapp_taoyuan;

import android.app.Dialog;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;


public class WritePeopleDialog extends Dialog implements OnClickListener {

    Context context;
    LayoutParams p ;
    DialogListener dialogListener;

    private Button btnClear,btnOk,btnCancel;
    public int screenWidth,screenHeight;
    ImageView iv_People1, iv_People2, iv_People3, iv_People4, iv_People5, iv_People6, iv_People7, iv_People8, iv_People9, iv_People10, iv_People11, iv_People12, iv_People13, iv_People14, iv_People15, iv_People16, iv_People17, iv_People18, iv_People19, iv_People20, iv_People21, iv_People22, iv_People23;
    private ImageView[] iv_People_Name = new ImageView[] {iv_People1, iv_People2, iv_People3, iv_People4, iv_People5, iv_People6, iv_People7, iv_People8, iv_People9, iv_People10, iv_People11, iv_People12, iv_People13, iv_People14, iv_People15, iv_People16, iv_People17, iv_People18, iv_People19, iv_People20, iv_People21, iv_People22, iv_People23};
    private int[] iv_People_ID = new int[]{R.id.iv_People1, R.id.iv_People2, R.id.iv_People3, R.id.iv_People4, R.id.iv_People5, R.id.iv_People6, R.id.iv_People7, R.id.iv_People8, R.id.iv_People9, R.id.iv_People10, R.id.iv_People11, R.id.iv_People12, R.id.iv_People13, R.id.iv_People14, R.id.iv_People15, R.id.iv_People16, R.id.iv_People17, R.id.iv_People18, R.id.iv_People19, R.id.iv_People20, R.id.iv_People21, R.id.iv_People22, R.id.iv_People23};
    private boolean[] Switch_People;
    private boolean[] Switch_WPeople;
    private int[] T_People_ID = new int[]{R.drawable.people_highlight_01, R.drawable.people_highlight_02, R.drawable.people_highlight_03, R.drawable.people_highlight_04, R.drawable.people_highlight_05, R.drawable.people_highlight_06, R.drawable.people_highlight_07, R.drawable.people_highlight_08, R.drawable.people_highlight_09, R.drawable.people_highlight_10, R.drawable.people_highlight_11, R.drawable.people_highlight_12, R.drawable.people_highlight_13, R.drawable.people_highlight_14, R.drawable.people_highlight_15, R.drawable.people_highlight_16, R.drawable.people_highlight_17, R.drawable.people_highlight_18, R.drawable.people_highlight_19, R.drawable.people_highlight_20, R.drawable.people_highlight_21, R.drawable.people_highlight_22, R.drawable.people_highlight_23};
    private int[] F_People_ID = new int[]{R.drawable.people_01, R.drawable.people_02, R.drawable.people_03, R.drawable.people_04, R.drawable.people_05, R.drawable.people_06, R.drawable.people_07, R.drawable.people_08, R.drawable.people_09, R.drawable.people_10, R.drawable.people_11, R.drawable.people_12, R.drawable.people_13, R.drawable.people_14, R.drawable.people_15, R.drawable.people_16, R.drawable.people_17, R.drawable.people_18, R.drawable.people_19, R.drawable.people_20, R.drawable.people_21, R.drawable.people_22, R.drawable.people_23};


    public WritePeopleDialog(Context context, int screenWidth, int screenHeight, boolean[] Select, DialogListener dialogListener) {
        super(context);
        this.context = context;
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
        this.Switch_People = Select;
        this.dialogListener = dialogListener;
    }

    public interface DialogListener {

        public void refreshActivity(boolean[] Select);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        requestWindowFeature(Window.FEATURE_PROGRESS);
        setContentView(R.layout.write_people);

        setCanceledOnTouchOutside(false);//區域外不消失

        p = getWindow().getAttributes();
        p.height = screenHeight;
        p.width = screenWidth;
        getWindow().setAttributes(p);

        controls();

        if(Switch_People != null) {
            for (int i = 0; i < iv_People_Name.length; i++) {
                Resources iv_Resources = context.getResources();
                if (Switch_People[i]) {
                    Drawable iv_Drawable = iv_Resources.getDrawable(T_People_ID[i]);
                    iv_People_Name[i].setBackground(iv_Drawable);
                } else {
                    Drawable iv_Drawable = iv_Resources.getDrawable(F_People_ID[i]);
                    iv_People_Name[i].setBackground(iv_Drawable);
                }
            }
            Switch_WPeople = Switch_People;
        }
        else{
            Switch_People = new boolean[iv_People_Name.length];
            for(int i = 0;i < Switch_People.length; i++){
                Switch_People[i] = false;
            }
            Switch_WPeople = Switch_People;
        }

        btnOk.setOnClickListener(WritePeopleDialog.this);
        btnClear.setOnClickListener(WritePeopleDialog.this);
        btnCancel.setOnClickListener(WritePeopleDialog.this);

        for(int i = 0; i < iv_People_Name.length; i++) {
            iv_People_Name[i].setOnClickListener(WritePeopleDialog.this);
        }

    }

    private void controls() {
        btnOk = (Button) findViewById(R.id.tablet_ok);
        btnClear = (Button) findViewById(R.id.tablet_clear);
        btnCancel = (Button)findViewById(R.id.tablet_cancel);

        for(int i = 0; i < iv_People_Name.length; i++){
            iv_People_Name[i] = (ImageView) findViewById(iv_People_ID[i]);
        }

    }

    public void onClick(View v)
    {
        if (v == btnOk)//確定
        {
            try {
                Switch_People = Switch_WPeople;
                dialogListener.refreshActivity(Switch_People);
                WritePeopleDialog.this.dismiss();
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
        else if (v == btnClear)//清除
        {
            for(int i = 0; i < iv_People_Name.length; i++) {
                Switch_WPeople[i] = false;
                Resources iv_Resources = context.getResources();
                Drawable iv_Drawable = iv_Resources.getDrawable(F_People_ID[i]);
                iv_People_Name[i].setBackground(iv_Drawable);
            }
        }
        else if (v == btnCancel)//取消
        {
            cancel();
        }

        for(int i = 0; i < iv_People_Name.length; i++) {
            if (v == iv_People_Name[i]) {
                Resources iv_Resources = context.getResources();
                Drawable iv_Drawable;
                if(Switch_WPeople[i]){
                    Switch_WPeople[i] = false;
                    iv_Drawable = iv_Resources.getDrawable(F_People_ID[i]);
                }
                else{
                    Switch_WPeople[i] = true;
                    iv_Drawable = iv_Resources.getDrawable(T_People_ID[i]);
                }
                iv_People_Name[i].setBackground(iv_Drawable);
                break;
            }
        }
    }
}
