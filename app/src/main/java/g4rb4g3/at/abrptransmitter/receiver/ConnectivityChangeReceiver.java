package g4rb4g3.at.abrptransmitter.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.Message;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.SocketException;
import java.util.Collections;
import java.util.List;

import androidx.annotation.NonNull;
import g4rb4g3.at.abrptransmitter.R;
import g4rb4g3.at.abrptransmitter.Utils;

import static g4rb4g3.at.abrptransmitter.Constants.MESSAGE_CONNECTIVITY_CHANGED;

public class ConnectivityChangeReceiver extends BroadcastReceiver {
  private Handler mHandler;

  public ConnectivityChangeReceiver(@NonNull Handler handler) {
    mHandler = handler;
  }

  @Override
  public void onReceive(Context context, Intent intent) {
    ConnectivityManager conMan = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
    NetworkInfo netInfo = conMan.getActiveNetworkInfo();
    if (netInfo != null && netInfo.getType() == ConnectivityManager.TYPE_WIFI) {
      List<String> ips = Utils.getIPAddresses();
      Message msg = new Message();
      msg.what = MESSAGE_CONNECTIVITY_CHANGED;
      msg.obj = Collections.unmodifiableCollection(ips);
      mHandler.sendMessage(msg);
    }
  }
}
