package g4rb4g3.at.abrptransmitter;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Collections;
import java.util.List;

import cz.msebera.android.httpclient.conn.util.InetAddressUtils;

public class MainActivity extends AppCompatActivity {

  public static final String TAG = "ABRPTransmitter";

  public static final String PREFERENCES_NAME = "preferences";
  public static final String PREFERENCES_TOKEN = "abrp_token";
  public static final String PREFERENCES_TRANSMIT_DATA = "transmit_data";

  private TextView mTvToken;
  private TextView mTvCompanionIp;

  public Handler mCompMsgHandler = new Handler(Looper.getMainLooper()) {
    @Override
    public void handleMessage(Message msg) {
      switch (msg.what) {
        case CompanionDataExchanger.MSG_TOKEN:
          mTvToken.setText((String)msg.obj);
          break;
        default:
          super.handleMessage(msg);
          break;
      }
    }
  };

  private CompanionDataExchanger mCompanionDataExchanger = null;
  private BroadcastReceiver wifiReceiver = new BroadcastReceiver() {
    @Override
    public void onReceive(Context context, Intent intent) {
      ConnectivityManager conMan = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
      NetworkInfo netInfo = conMan.getActiveNetworkInfo();
      if (netInfo != null && netInfo.getType() == ConnectivityManager.TYPE_WIFI) {
        mTvCompanionIp.setText(getIPAddress());
      }
    }
  };

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

     mTvToken = findViewById(R.id.tv_abrp_token);
     mTvCompanionIp = findViewById(R.id.tv_companion_ip);
  }

  @Override
  protected void onResume() {
    super.onResume();
    final SharedPreferences sp = getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);

    mTvToken.setText(sp.getString(PREFERENCES_TOKEN, ""));

    final CheckBox cbTrsansmitData = findViewById(R.id.cb_transmit);
    cbTrsansmitData.setChecked(sp.getBoolean(PREFERENCES_TRANSMIT_DATA, false));

    Button btSave = findViewById(R.id.save);
    btSave.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        SharedPreferences.Editor sped = sp.edit();
        sped.putString(PREFERENCES_TOKEN, mTvToken.getText().toString());
        sped.putBoolean(PREFERENCES_TRANSMIT_DATA, cbTrsansmitData.isChecked());
        sped.commit();

        Toast.makeText(getApplicationContext(), getText(R.string.saved), Toast.LENGTH_LONG).show();
      }
    });


    mTvCompanionIp.setText(getIPAddress());

    registerReceiver(wifiReceiver, new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE"));

    mCompanionDataExchanger = new CompanionDataExchanger(mCompMsgHandler);
    Thread thread = new Thread(mCompanionDataExchanger);
    thread.start();

    VncServer.starStopServer(getApplicationContext());
  }

  @Override
  protected void onPause() {
    super.onPause();

    if (mCompanionDataExchanger != null) {
      mCompanionDataExchanger.stop();
      mCompanionDataExchanger = null;
    }

    unregisterReceiver(wifiReceiver);
  }

  private static String getIPAddress() {
    StringBuilder ip = new StringBuilder();
    try {
      List<NetworkInterface> interfaces = Collections.list(NetworkInterface.getNetworkInterfaces());
      for (NetworkInterface intf : interfaces) {
        List<InetAddress> addrs = Collections.list(intf.getInetAddresses());
        for (InetAddress addr : addrs) {
          if (!addr.isLoopbackAddress()) {
            if(ip.length() > 0) {
              ip.append(" or ");
            }
            if(InetAddressUtils.isIPv4Address(addr.getHostAddress())) {
              ip.append(addr.getHostAddress());
            } else {
              ip.append(addr.getHostAddress().substring(0, addr.getHostAddress().indexOf("%")));
            }
          }
        }
      }
    } catch (Exception e) {
      Log.e(TAG, "error getting ip", e);
    }
    if(ip.length() == 0) {
      return "error getting ip address";
    } else {
      return ip.toString();
    }
  }
}
