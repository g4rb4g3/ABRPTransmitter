package g4rb4g3.at.abrptransmitter.greencar;

import android.os.RemoteException;

import com.lge.ivi.greencar.IBatteryChargeListener;

import g4rb4g3.at.abrptransmitter.ABetterRoutePlanner;
import g4rb4g3.at.abrptransmitter.NumberHolder;

public class BatteryChargeListener extends IBatteryChargeListener.Stub {

  private final NumberHolder mPHEVBatteryChargePersentChanged = new NumberHolder();

  @Override
  public void onPHEVBatteryChargePersentChanged(int oldValue, int newValue) throws RemoteException {
    if (mPHEVBatteryChargePersentChanged.equals(oldValue, newValue)) {
      return;
    }
    mPHEVBatteryChargePersentChanged.setValues(oldValue, newValue);
    ABetterRoutePlanner.updateSoC(newValue);
  }

  @Override
  public void onChargeRemainedTimeminChanged(int oldValue, int newValue) throws RemoteException {
  }

  @Override
  public void onPHEVBatteryChargeTime120VChanged(int oldValue, int newValue) throws RemoteException {
  }

  @Override
  public void onPHEVBatteryChargeTime240VChanged(int oldValue, int newValue) throws RemoteException {
  }

  @Override
  public void onPHEVChargeStatusChanged(int oldValue, int newValue) throws RemoteException {
  }

  @Override
  public void onPHEVStandardCharge120VChanged(int oldValue, int newValue) throws RemoteException {
  }

  @Override
  public void onPHEVStandardChargeChanged(int oldValue, int newValue) throws RemoteException {
  }
}
