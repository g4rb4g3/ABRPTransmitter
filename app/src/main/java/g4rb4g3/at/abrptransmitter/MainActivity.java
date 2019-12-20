package g4rb4g3.at.abrptransmitter;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.SocketException;
import java.util.List;

import g4rb4g3.at.abrptransmitter.service.AbrpTransmitterService;

import static g4rb4g3.at.abrptransmitter.Constants.MSG_TOKEN;
import static g4rb4g3.at.abrptransmitter.Constants.PREFERENCES_NAME;
import static g4rb4g3.at.abrptransmitter.Constants.PREFERENCES_TOKEN;
import static g4rb4g3.at.abrptransmitter.Constants.PREFERENCES_TRANSMIT_DATA;

public class MainActivity extends AppCompatActivity {
  private static final Logger sLog = LoggerFactory.getLogger(AbrpTransmitterService.class.getSimpleName());

  AbrpTransmitterService mService;
  boolean mBound = false;

  private TextView mTvToken;
  private TextView mTvCompanionIp;
  private TextView mTvLog;

  private Handler mMsgHandler = new Handler(Looper.getMainLooper()) {
    @Override
    public void handleMessage(Message msg) {
      switch (msg.what) {
        case MSG_TOKEN:
          mTvToken.setText((String) msg.obj);
          break;
        default:
          super.handleMessage(msg);
          break;
      }
    }
  };

  private ServiceConnection connection = new ServiceConnection() {
    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
      AbrpTransmitterService.AbrpTransmitterBinder binder = (AbrpTransmitterService.AbrpTransmitterBinder) service;
      mService = binder.getService();
      mBound = true;

      mService.registerHandler(mMsgHandler);
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {
      mBound = false;
    }
  };

  private CompanionDataExchanger mCompanionDataExchanger = null;
  private BroadcastReceiver mWifiReceiver = new BroadcastReceiver() {
    @Override
    public void onReceive(Context context, Intent intent) {
      ConnectivityManager conMan = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
      NetworkInfo netInfo = conMan.getActiveNetworkInfo();
      if (netInfo != null && netInfo.getType() == ConnectivityManager.TYPE_WIFI) {
        try {
          StringBuilder sb = new StringBuilder();
          List<String> ips = Utils.getIPAddresses();
          for (String ip : ips) {
            if (sb.length() > 0) {
              sb.append(" or ");
            }
            sb.append(ip);
          }
          mTvCompanionIp.setText(sb.toString());
        } catch (SocketException e) {
          String msg = getString(R.string.error_getting_ip);
          sLog.error(msg, e);
          mTvCompanionIp.setText(msg);
        }
      }
    }
  };

  @Override
  protected void onStart() {
    super.onStart();
    Intent intent = new Intent(this, AbrpTransmitterService.class);
    bindService(intent, connection, Context.BIND_AUTO_CREATE);
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    mTvToken = findViewById(R.id.tv_abrp_token);
    mTvCompanionIp = findViewById(R.id.tv_companion_ip);
    mTvLog = findViewById(R.id.tv_log);
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

    registerReceiver(mWifiReceiver, new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE"));

    mCompanionDataExchanger = new CompanionDataExchanger(mMsgHandler);
    Thread thread = new Thread(mCompanionDataExchanger);
    thread.start();

    try {
      mTvLog.setText(Utils.getCurrentLogs((MainApplication) getApplication()));
    } catch (IOException e) {
      String msg = getString(R.string.error_reading_log);
      sLog.error(msg, e);
      mTvLog.setText(msg);
    }
  }

  @Override
  protected void onPause() {
    super.onPause();

    if (mCompanionDataExchanger != null) {
      mCompanionDataExchanger.stop();
      mCompanionDataExchanger = null;
    }

    unregisterReceiver(mWifiReceiver);
  }

  @Override
  protected void onStop() {
    super.onStop();
    mService.unregisterHandler(mMsgHandler);
    unbindService(connection);
    mBound = false;
  }
}
