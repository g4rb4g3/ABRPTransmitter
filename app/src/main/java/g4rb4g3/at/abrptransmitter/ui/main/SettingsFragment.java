package g4rb4g3.at.abrptransmitter.ui.main;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import g4rb4g3.at.abrptransmitter.R;

import static g4rb4g3.at.abrptransmitter.Constants.PREFERENCES_AUTOSTART_COMPANION;
import static g4rb4g3.at.abrptransmitter.Constants.PREFERENCES_NAME;
import static g4rb4g3.at.abrptransmitter.Constants.PREFERENCES_TOKEN;
import static g4rb4g3.at.abrptransmitter.Constants.PREFERENCES_TRANSMIT_DATA;

public class SettingsFragment extends Fragment {
  private TextView mTvToken;
  private Button mBtSave;
  private CheckBox mCbTransmitData;
  private CheckBox mCbAutostartCompanion;
  private SharedPreferences mSharedPreferences;

  private SharedPreferences.OnSharedPreferenceChangeListener mOnSharedPreferenceChangeListener;

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

    mSharedPreferences.registerOnSharedPreferenceChangeListener(mOnSharedPreferenceChangeListener);
    return view;
  }

  @Override
  public void setUserVisibleHint(boolean isVisibleToUser) {
    super.setUserVisibleHint(isVisibleToUser);

    if(mOnSharedPreferenceChangeListener != null) {
      if(isVisibleToUser) {
        mSharedPreferences.registerOnSharedPreferenceChangeListener(mOnSharedPreferenceChangeListener);
      } else {
        mSharedPreferences.unregisterOnSharedPreferenceChangeListener(mOnSharedPreferenceChangeListener);
      }
    }
  }
}
