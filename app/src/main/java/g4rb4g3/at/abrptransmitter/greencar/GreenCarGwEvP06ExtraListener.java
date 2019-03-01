package g4rb4g3.at.abrptransmitter.greencar;

import android.os.RemoteException;

import com.lge.ivi.greencar.IGreenCarGwEvP06ExtraListener;

import g4rb4g3.at.abrptransmitter.ABetterRoutePlanner;
import g4rb4g3.at.abrptransmitter.NumberHolder;

public class GreenCarGwEvP06ExtraListener extends IGreenCarGwEvP06ExtraListener.Stub {

  private final NumberHolder mCrDatcPtcPwrConWChanged = new NumberHolder();

  @Override
  public void onCrDatcPtcPwrConWChanged(int oldValue, int newValue) throws RemoteException {
    if (mCrDatcPtcPwrConWChanged.equals(oldValue, newValue)) {
      return;
    }
    mCrDatcPtcPwrConWChanged.setValues(oldValue, newValue);
    ABetterRoutePlanner.updateHeatingConsumption(newValue * 10);
  }

  @Override
  public void onCfObcRdyChanged(int oldValue, int newValue) throws RemoteException {
  }

  @Override
  public void onCfVcuLowSocLpChanged(int oldValue, int newValue) throws RemoteException {
  }
}
