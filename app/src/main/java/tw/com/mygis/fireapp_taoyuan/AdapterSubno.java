package tw.com.mygis.fireapp_taoyuan;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Bart on 2015/1/30.
 */
public class AdapterSubno extends BaseAdapter {

    private ArrayList<HashMap<String, String>> mAppList;
    private LayoutInflater mInflater;
    private Context mContext;
    private int layout;
    private int DrawerItemPosition;
    private String[] keyString;
    private int[] valueViewID;

    private AlertDialog.Builder dialog;

    private ItemView itemView;

    private class ItemView {
        ImageView iv_Select;
        TextView tv_SubnoID;
        Button btn_Delete;
    }

    public AdapterSubno(Context c, ArrayList<HashMap<String, String>> appList, int resource, int ItemPosition, String[] from, int[] to) {
        mAppList = appList;
        mContext = c;
        mInflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        layout = resource;
        DrawerItemPosition = ItemPosition;
        keyString = new String[from.length];
        valueViewID = new int[to.length];
        System.arraycopy(from, 0, keyString, 0, from.length);
        System.arraycopy(to, 0, valueViewID, 0, to.length);
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        //return 0;
        return mAppList.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        //return null;
        return mAppList.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        //return 0;
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        //return null;

        if (convertView != null) {
            itemView = (ItemView) convertView.getTag();
        } else {
            convertView = mInflater.inflate(layout, null);
            itemView = new ItemView();
            itemView.iv_Select = (ImageView)convertView.findViewById(valueViewID[0]);
            itemView.tv_SubnoID = (TextView)convertView.findViewById(valueViewID[1]);
            itemView.btn_Delete = (Button)convertView.findViewById(valueViewID[2]);
            convertView.setTag(itemView);
        }

        HashMap<String, String> appInfo = mAppList.get(position);
        if (appInfo != null) {
            String name = (String) appInfo.get(keyString[0]);
            itemView.tv_SubnoID.setText(name);
            itemView.btn_Delete.setOnClickListener(new ItemButton_Click(position));
            itemView.iv_Select.setBackgroundResource(0);
            if(DrawerItemPosition == position){
                itemView.iv_Select.setBackgroundColor(Color.parseColor("#7d4aff"));
            }
        }

        return convertView;
    }

    class ItemButton_Click implements OnClickListener {
        private int position;

        ItemButton_Click(int pos) {
            position = pos;
        }

        @Override
        public void onClick(View v) {
            int ID = v.getId();
            if (ID == itemView.btn_Delete.getId()) {
                if(DrawerItemPosition == position){
                    dialog = new AlertDialog.Builder(mContext);
                    dialog.setTitle("提示"); //設定dialog 的title顯示內容
                    dialog.setMessage("無法刪除使用中表單!");
                    dialog.setCancelable(false); //關閉 Android 系統的主要功能鍵(menu,home等...)
                    dialog.setPositiveButton("確定", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });
                    dialog.show();
                }
                else {
                    dialog = new AlertDialog.Builder(mContext);
                    dialog.setTitle("提示"); //設定dialog 的title顯示內容
                    dialog.setMessage("是否刪除表單?");
                    dialog.setCancelable(false); //關閉 Android 系統的主要功能鍵(menu,home等...)
                    dialog.setPositiveButton("是", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            HashMap<String, String> appInfo = mAppList.get(position);
                            if (appInfo != null) {
                                SQLite SQLite = new SQLite(mContext);
                                SQLiteDatabase DB = SQLite.getWritableDatabase();
                                String[] parameter = {appInfo.get("CaseID"), appInfo.get("Subno")};
                                DB.delete("MainTab1", "CaseID=? AND Subno=?", parameter);
                                DB.delete("MainTab2", "CaseID=? AND Subno=?", parameter);
                                DB.delete("MainTab3", "CaseID=? AND Subno=?", parameter);
                                DB.delete("MainTab4", "CaseID=? AND Subno=?", parameter);
                                DB.delete("MainTab5", "CaseID=? AND Subno=?", parameter);
                                DB.delete("MainHandWrite", "CaseID=? AND Subno=?", parameter);
                                SQLite.close();
                                DB.close();
                                mAppList.remove(position);
                                notifyDataSetChanged();
                            }
                            if (mAppList.size() < 1) {
                                Intent intent = new Intent(mContext, MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                mContext.startActivity(intent);
                            }
                            if (position < DrawerItemPosition) {
                                DrawerItemPosition--;
                            }
                            Log.e("log_tag", "位子:" + DrawerItemPosition);
                            notifyDataSetChanged();
                        }
                    });
                    dialog.setNegativeButton("否", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    dialog.show();
                }
            }
        }
    }
}
