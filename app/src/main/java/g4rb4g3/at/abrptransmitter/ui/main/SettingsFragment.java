package g4rb4g3.at.abrptransmitter.ui.main;

import android.app.ProgressDialog;
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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.lge.ivi.media.ExtMediaManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.LinkedHashMap;

import androidx.fragment.app.Fragment;
import g4rb4g3.at.abrptransmitter.R;
import g4rb4g3.at.abrptransmitter.Utils;
import g4rb4g3.at.abrptransmitter.asynctasks.AbrpTransmitterReleaseLoader;
import g4rb4g3.at.abrptransmitter.receiver.ConnectivityChangeReceiver;
import g4rb4g3.at.abrptransmitter.service.AbrpTransmitterService;

import static g4rb4g3.at.abrptransmitter.Constants.ABRPTRANSMITTER_APK_NAME;
import static g4rb4g3.at.abrptransmitter.Constants.ABRPTRANSMITTER_RELEASE_URL;
import static g4rb4g3.at.abrptransmitter.Constants.MESSAGE_CONNECTIVITY_CHANGED;
import static g4rb4g3.at.abrptransmitter.Constants.PREFERENCES_AUTOSTART_COMPANION;
import static g4rb4g3.at.abrptransmitter.Constants.PREFERENCES_NAME;
import static g4rb4g3.at.abrptransmitter.Constants.PREFERENCES_NOMAP;
import static g4rb4g3.at.abrptransmitter.Constants.PREFERENCES_TOKEN;
import static g4rb4g3.at.abrptransmitter.Constants.PREFERENCES_TRANSMIT_DATA;

public class SettingsFragment extends Fragment implements View.OnClickListener, AbrpTransmitterReleaseLoader.IAbrpTransmitterReleaseLoader {
  private static final Logger sLog = LoggerFactory.getLogger(AbrpTransmitterService.class.getSimpleName());
  private TextView mTvToken;
  private TextView mTvCompanionIp;
  private Button mBtSave;
  private Button mBtLoadReleases;
  private CheckBox mCbTransmitData;
  private CheckBox mCbAutostartCompanion;
  private CheckBox mCbNoMap;
  private Spinner mSpReleases;
  private SharedPreferences mSharedPreferences;
  private SharedPreferences.OnSharedPreferenceChangeListener mOnSharedPreferenceChangeListener;
  private LinkedHashMap<String, String> mReleases = new LinkedHashMap<>();
  private ProgressDialog mProgressDialog;

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

    mBtSave = view.findViewById(R.id.bt_save);
    mBtLoadReleases = view.findViewById(R.id.btn_load_releases);
    mTvToken = view.findViewById(R.id.tv_abrp_token);
    mTvCompanionIp = view.findViewById(R.id.tv_companion_ip);
    mCbTransmitData = view.findViewById(R.id.cb_transmit);
    mCbAutostartCompanion = view.findViewById(R.id.cb_autostart_companion);
    mCbNoMap = view.findViewById(R.id.cb_nomap);
    mSpReleases = view.findViewById(R.id.sp_releases);

    mBtSave.setOnClickListener(this);
    mBtLoadReleases.setOnClickListener(this);

    mCbTransmitData.setChecked(mSharedPreferences.getBoolean(PREFERENCES_TRANSMIT_DATA, false));
    mCbAutostartCompanion.setChecked(mSharedPreferences.getBoolean(PREFERENCES_AUTOSTART_COMPANION, false));
    mCbNoMap.setChecked(mSharedPreferences.getBoolean(PREFERENCES_NOMAP, false));

    mProgressDialog = new ProgressDialog(getContext());
    mProgressDialog.setCancelable(false);

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
    mTvToken.setText(mSharedPreferences.getString(PREFERENCES_TOKEN, ""));
    mSharedPreferences.registerOnSharedPreferenceChangeListener(mOnSharedPreferenceChangeListener);
  }

  @Override
  public void onPause() {
    super.onPause();
    getContext().unregisterReceiver(mConnectivityChangedReceiver);
    mSharedPreferences.unregisterOnSharedPreferenceChangeListener(mOnSharedPreferenceChangeListener);
  }

  @Override
  public void onClick(View v) {
    switch (v.getId()) {
      case R.id.bt_save:
        SharedPreferences.Editor sped = mSharedPreferences.edit();
        sped.putString(PREFERENCES_TOKEN, mTvToken.getText().toString());
        sped.putBoolean(PREFERENCES_TRANSMIT_DATA, mCbTransmitData.isChecked());
        sped.putBoolean(PREFERENCES_AUTOSTART_COMPANION, mCbAutostartCompanion.isChecked());
        sped.putBoolean(PREFERENCES_NOMAP, mCbNoMap.isChecked());
        sped.commit();

        Toast.makeText(getContext(), getText(R.string.saved), Toast.LENGTH_LONG).show();
        break;
      case R.id.btn_load_releases:
        if(Utils.getIPAddresses().size() == 0) {
          Toast.makeText(getContext(), getString(R.string.no_wifi_ip), Toast.LENGTH_LONG).show();
          return;
        }
        if (mBtLoadReleases.getText().equals(getString(R.string.load_releases))) {
          new AbrpTransmitterReleaseLoader(getContext(), SettingsFragment.this).execute(ABRPTRANSMITTER_RELEASE_URL);
        } else {
          String url = mReleases.get(mSpReleases.getSelectedItem().toString());
          if ("".equals(url)) {
            Toast.makeText(getContext(), getString(R.string.choose_release_first), Toast.LENGTH_LONG).show();
            return;
          }
          new AbrpTransmitterReleaseLoader(getContext(), SettingsFragment.this).execute(url);
        }
        break;
    }
  }

  @Override
  public void releasesLoadComplete(LinkedHashMap<String, String> result) {
    mReleases = result;
    ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), R.layout.spinner_item, mReleases.keySet().toArray(new String[mReleases.keySet().size()]));
    mSpReleases.setAdapter(adapter);
    if (mProgressDialog.isShowing()) {
      mProgressDialog.dismiss();
    }
    mBtLoadReleases.setText(getString(R.string.download_amp_install));
  }

  @Override
  public void downloadComplete(String result) {
    mProgressDialog.setMessage(getString(R.string.installing));
    new Handler().postDelayed(new Runnable() {
      @Override
      public void run() {
        ExtMediaManager extMediaManager = ExtMediaManager.getInstance(getContext());
        extMediaManager.excute("pm install -r /sdcard/" + ABRPTRANSMITTER_APK_NAME, null);
      }
    }, 5000);
  }

  @Override
  public void setProgressDialogMessage(final int id) {
    getActivity().runOnUiThread(new Runnable() {
      @Override
      public void run() {
        String msg = getString(id);
        if (id == R.string.please_wait_downloading) {
          msg += " " + mSpReleases.getSelectedItem().toString();
        }
        mProgressDialog.setMessage(msg);
        mProgressDialog.show();
      }
    });
  }

  @Override
  public void loadFailed(final String msg) {
    getActivity().runOnUiThread(new Runnable() {
      @Override
      public void run() {
        if (mProgressDialog.isShowing()) {
          mProgressDialog.dismiss();
        }
        Toast.makeText(getContext(), msg, Toast.LENGTH_LONG).show();
      }
    });
  }

}
