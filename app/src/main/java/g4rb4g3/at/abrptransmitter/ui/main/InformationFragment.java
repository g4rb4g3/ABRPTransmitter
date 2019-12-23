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

import androidx.fragment.app.Fragment;
import g4rb4g3.at.abrptransmitter.R;
import g4rb4g3.at.abrptransmitter.service.AbrpTransmitterService;
import g4rb4g3.at.abrptransmitter.service.AbrpTransmitterService.AbrpTransmitterBinder;

public class InformationFragment extends Fragment {

  private AbrpTransmitterService mService;
  private boolean mBound = false;

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
    public void handleMessage(Message msg) {

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
    return inflater.inflate(R.layout.fragment_information, container, false);
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
