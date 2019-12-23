package g4rb4g3.at.abrptransmitter.ui.main;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import g4rb4g3.at.abrptransmitter.R;
import g4rb4g3.at.abrptransmitter.Utils;
import g4rb4g3.at.abrptransmitter.service.AbrpTransmitterService;
import g4rb4g3.at.abrptransmitter.service.AbrpTransmitterService.AbrpTransmitterBinder;

import static g4rb4g3.at.abrptransmitter.Constants.MESSAGE_LAST_ERROR_ABRPSERVICE;
import static g4rb4g3.at.abrptransmitter.Constants.MESSAGE_LAST_UPDATE_SENT;

public class InformationFragment extends Fragment {

  private AbrpTransmitterService mService;
  private boolean mBound = false;
  private TextView mTvLastErrorMsg;
  private TextView mTvLastUpdateSentMsg;

  private ServiceConnection mServiceConnection = new ServiceConnection() {
    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
      AbrpTransmitterBinder binder = (AbrpTransmitterBinder) service;
      mService = binder.getService();
      mBound = true;
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {
      mBound = false;
    }
  };

  private Handler mHandler = new Handler(Looper.getMainLooper()) {
    @Override
    public void handleMessage(final Message msg) {
      switch(msg.what) {
        case MESSAGE_LAST_ERROR_ABRPSERVICE:
          getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
              mTvLastErrorMsg.setText(Utils.getTimestamp() + ": " + (String)msg.obj);
            }
          });
          break;
        case MESSAGE_LAST_UPDATE_SENT:
          getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
              mTvLastUpdateSentMsg.setText((String)msg.obj);
            }
          });
          break;
      }
    }
  };

  public InformationFragment() {
    // Required empty public constructor
  }

  public static InformationFragment newInstance() {
    InformationFragment fragment = new InformationFragment();
    return fragment;
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    Intent intent = new Intent(getContext(), AbrpTransmitterService.class);
    getActivity().bindService(intent, mServiceConnection, Context.BIND_AUTO_CREATE);
  }

  @Override
  public void onDestroy() {
    super.onDestroy();
    getActivity().unbindService(mServiceConnection);
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    // Inflate the layout for this fragment
    View view = inflater.inflate(R.layout.fragment_information, container, false);
    mTvLastErrorMsg = view.findViewById(R.id.tv_last_error_msg);
    mTvLastUpdateSentMsg = view.findViewById(R.id.tv_last_update_sent_msg)
    return view;
  }

  @Override
  public void onAttach(Context context) {
    super.onAttach(context);
  }

  @Override
  public void onDetach() {
    super.onDetach();
  }

  @Override
  public void onResume() {
    super.onResume();
    mService.registerHandler(mHandler);
  }

  @Override
  public void onPause() {
    super.onPause();
    mService.unregisterHandler(mHandler);
  }
}
