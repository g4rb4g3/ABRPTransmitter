package g4rb4g3.at.abrptransmitter.ui.main;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;

import androidx.fragment.app.Fragment;
import g4rb4g3.at.abrptransmitter.R;
import g4rb4g3.at.abrptransmitter.receiver.ConnectivityChangeReceiver;
import g4rb4g3.at.abrptransmitter.service.AbrpTransmitterService;

import static g4rb4g3.at.abrptransmitter.Constants.MESSAGE_CONNECTIVITY_CHANGED;
import static g4rb4g3.at.abrptransmitter.Constants.PREFERENCES_AUTOSTART_COMPANION;
import static g4rb4g3.at.abrptransmitter.Constants.PREFERENCES_NAME;
import static g4rb4g3.at.abrptransmitter.Constants.PREFERENCES_TOKEN;
import static g4rb4g3.at.abrptransmitter.Constants.PREFERENCES_TRANSMIT_DATA;

public class SettingsFragment extends Fragment {
  private static final Logger sLog = LoggerFactory.getLogger(AbrpTransmitterService.class.getSimpleName());
  private TextView mTvToken;
  private TextView mTvCompanionIp;
  private Button mBtSave;
  private CheckBox mCbTransmitData;
  private CheckBox mCbAutostartCompanion;
  private SharedPreferences mSharedPreferences;
  private SharedPreferences.OnSharedPreferenceChangeListener mOnSharedPreferenceChangeListener;

  private Handler mHandler = new Handler(Looper.getMainLooper()) {
    @Override
    public void handleMessage(Message msg) {
      switch (msg.what) {
        case MESSAGE_CONNECTIVITY_CHANGED:
          final StringBuilder sb = new StringBuilder();
          for (String ip : ((Collection<String>) msg.obj)) {
            if (sb.length() > 0) {
              sb.append(" or ");
            }
            sb.append(ip);
            getActivity().runOnUiThread(new Runnable() {
              @Override
              public void run() {
                mTvCompanionIp.setText(sb.toString());
              }
            });
          }
          break;
      }
    }
  };
  private BroadcastReceiver mConnectivityChangedReceiver = new ConnectivityChangeReceiver(mHandler);

  public SettingsFragment() {
    // Required empty public constructor
  }

  public static SettingsFragment newInstance() {
    SettingsFragment fragment = new SettingsFragment();
    return fragment;
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    mSharedPreferences = getContext().getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_settings, container, false);

    mBtSave = view.findViewById(R.id.save);
    mTvToken = view.findViewById(R.id.tv_abrp_token);
    mTvCompanionIp = view.findViewById(R.id.tv_companion_ip);
    mCbTransmitData = view.findViewById(R.id.cb_transmit);
    mCbAutostartCompanion = view.findViewById(R.id.cb_autostart_companion);

    mBtSave.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        SharedPreferences.Editor sped = mSharedPreferences.edit();
        sped.putString(PREFERENCES_TOKEN, mTvToken.getText().toString());
        sped.putBoolean(PREFERENCES_TRANSMIT_DATA, mCbTransmitData.isChecked());
        sped.putBoolean(PREFERENCES_AUTOSTART_COMPANION, mCbAutostartCompanion.isChecked());
        sped.commit();

        Toast.makeText(getContext(), getText(R.string.saved), Toast.LENGTH_LONG).show();
      }
    });

    mTvToken.setText(mSharedPreferences.getString(PREFERENCES_TOKEN, ""));
    mCbTransmitData.setChecked(mSharedPreferences.getBoolean(PREFERENCES_TRANSMIT_DATA, false));
    mCbAutostartCompanion.setChecked(mSharedPreferences.getBoolean(PREFERENCES_AUTOSTART_COMPANION, false));

    mOnSharedPreferenceChangeListener = new SharedPreferences.OnSharedPreferenceChangeListener() {
      @Override
      public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        //only settings that can be changed with the companion app
        switch (key) {
          case PREFERENCES_TOKEN:
            mTvToken.setText(mSharedPreferences.getString(PREFERENCES_TOKEN, ""));
            break;
          case PREFERENCES_TRANSMIT_DATA:
            mCbTransmitData.setChecked(mSharedPreferences.getBoolean(PREFERENCES_TRANSMIT_DATA, false));
            break;
        }
      }
    };
    return view;
  }

  @Override
  public void onResume() {
    super.onResume();
    getContext().registerReceiver(mConnectivityChangedReceiver, new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE"));
    mSharedPreferences.registerOnSharedPreferenceChangeListener(mOnSharedPreferenceChangeListener);
  }

  @Override
  public void onPause() {
    super.onPause();
    getContext().unregisterReceiver(mConnectivityChangedReceiver);
    mSharedPreferences.unregisterOnSharedPreferenceChangeListener(mOnSharedPreferenceChangeListener);
  }
}
