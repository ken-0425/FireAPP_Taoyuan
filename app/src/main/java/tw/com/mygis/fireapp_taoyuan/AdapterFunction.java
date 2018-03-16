package tw.com.mygis.fireapp_taoyuan;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Bart on 2015/1/30.
 */
public class AdapterFunction extends BaseAdapter {

    private LayoutInflater mInflater;
    private Context mContext;
    private int layout;
    private int[] Images;
    private String[] keyString;
    private int[] valueViewID;

    private ItemView itemView;

    private class ItemView {
        ImageView iv_ItemName;
        TextView tv_ItemName;
    }

    public AdapterFunction(Context c, int resource, int[] ItemImage, String[] from, int[] to) {
        mContext = c;
        mInflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        layout = resource;
        Images = new int[ItemImage.length];
        keyString = new String[from.length];
        valueViewID = new int[to.length];
        System.arraycopy(ItemImage, 0, Images, 0, ItemImage.length);
        System.arraycopy(from, 0, keyString, 0, from.length);
        System.arraycopy(to, 0, valueViewID, 0, to.length);
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        //return 0;
        return keyString.length;
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        //return null;
        return keyString[position];
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
            itemView.iv_ItemName = (ImageView)convertView.findViewById(valueViewID[0]);
            itemView.tv_ItemName = (TextView)convertView.findViewById(valueViewID[1]);
            convertView.setTag(itemView);
        }
        Resources iv_Resources = mContext.getResources();
        Drawable iv_Drawable;
        iv_Drawable = iv_Resources.getDrawable(Images[position]);
        itemView.iv_ItemName.setBackground(iv_Drawable);
        itemView.tv_ItemName.setText(keyString[position]);

        return convertView;
    }
}
