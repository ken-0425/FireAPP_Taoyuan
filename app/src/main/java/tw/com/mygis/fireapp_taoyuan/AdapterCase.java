package tw.com.mygis.fireapp_taoyuan;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Bart on 2015/1/30.
 */
public class AdapterCase extends BaseAdapter {

    private ArrayList<HashMap<String, String>> mAppList;
    private LayoutInflater mInflater;
    private Context mContext;
    private int layout;
    private String[] keyString;
    private int[] valueViewID;

    private AlertDialog.Builder dialog;

    private ItemView itemView;

    private class ItemView {
        TextView tv_CaseID;
        TextView tv_Address;
        Button btn_Delete;
    }

    public AdapterCase(Context c, ArrayList<HashMap<String, String>> appList, int resource, String[] from, int[] to) {
        mAppList = appList;
        mContext = c;
        mInflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        layout = resource;
        keyString = new String[from.length];
        valueViewID = new int[to.length];
        System.arraycopy(from, 0, keyString, 0, from.length);
        System.arraycopy(to, 0, valueViewID, 0, to.length);

        Log.d("allenj", getCount() + "");
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
            itemView.tv_CaseID = (TextView)convertView.findViewById(valueViewID[0]);
            itemView.tv_Address = (TextView)convertView.findViewById(valueViewID[1]);
            itemView.btn_Delete = (Button)convertView.findViewById(valueViewID[2]);
            convertView.setTag(itemView);
        }

        HashMap<String, String> appInfo = mAppList.get(position);
        if (appInfo != null) {
            String name = (String) appInfo.get(keyString[0]);
            String info = (String) appInfo.get(keyString[1]);
            itemView.tv_CaseID.setText(name);
            itemView.tv_Address.setText(info);
            itemView.btn_Delete.setOnClickListener(new ItemButton_Click(position));
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
                dialog = new AlertDialog.Builder(mContext);
                dialog.setTitle("提示"); //設定dialog 的title顯示內容
                dialog.setMessage("是否結束案件?");
                dialog.setCancelable(false); //關閉 Android 系統的主要功能鍵(menu,home等...)
                dialog.setPositiveButton("是", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        HashMap<String, String> appInfo = mAppList.get(position);
                        if (appInfo != null) {
                            SQLite SQLite = new SQLite(mContext);
                            SQLiteDatabase DB = SQLite.getWritableDatabase();
                            DB.delete("MainCases", "CaseID=?", new String[]{appInfo.get("CaseID")});
                            DB.delete("MainTab1", "CaseID=?", new String[]{appInfo.get("CaseID")});
                            DB.delete("MainTab2", "CaseID=?", new String[]{appInfo.get("CaseID")});
                            DB.delete("MainTab3", "CaseID=?", new String[]{appInfo.get("CaseID")});
                            DB.delete("MainTab4", "CaseID=?", new String[]{appInfo.get("CaseID")});
                            DB.delete("MainTab5", "CaseID=?", new String[]{appInfo.get("CaseID")});
                            DB.delete("MainHandWrite", "CaseID=?", new String[]{appInfo.get("CaseID")});
                            SQLite.close();
                            DB.close();
                            mAppList.remove(position);
                            notifyDataSetChanged();
                        }
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
