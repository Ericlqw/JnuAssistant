package org.jnuLib.Util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetWorkUtil {
         
    //NETWORK
    public static boolean isNetworkAvailable(Context mActivity)
    {
        Context context = mActivity.getApplicationContext();
        ConnectivityManager connect = (ConnectivityManager)context.getSystemService(
                Context.CONNECTIVITY_SERVICE);
        if(connect==null)
        {
            return false;
        }else//get all network info
        {
            NetworkInfo[] info = connect.getAllNetworkInfo();
            if(info!=null)
            {
                for(int i=0;i<info.length;i++)
                {
                    if(info[i].getState()==NetworkInfo.State.CONNECTED)
                    {
                        return true;
                    }
                }
            }
        }
        return false;
    }

}

