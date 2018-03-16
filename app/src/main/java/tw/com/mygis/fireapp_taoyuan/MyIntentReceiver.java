package tw.com.mygis.fireapp_taoyuan;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by Bart on 2014/12/3.
 */
public class MyIntentReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent startupBootIntent = new Intent(context, Login.class);
        startupBootIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(startupBootIntent);
    }
}