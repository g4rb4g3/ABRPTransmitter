package g4rb4g3.at.abrptransmitter.greencar;

import android.os.RemoteException;

import com.lge.ivi.greencar.IEVPowerDisplayListener;

import g4rb4g3.at.abrptransmitter.ABetterRoutePlanner;
import g4rb4g3.at.abrptransmitter.NumberHolder;

public class EVPowerDisplayListener extends IEVPowerDisplayListener.Stub {

  private final NumberHolder mCrDatcAcnCompPwrConWChanged = new NumberHolder();
  private final NumberHolder mCrLdcPwrMonWChanged = new NumberHolder();
  private final NumberHolder mCrMcuMotPwrAvnKwChanged = new NumberHolder();

  @Override
  public void onCrDatcAcnCompPwrConWChanged(int oldValue, int newValue) throws RemoteException {
    if (mCrDatcAcnCompPwrConWChanged.equals(oldValue, newValue)) {
      return;
    }
    mCrDatcAcnCompPwrConWChanged.setValues(oldValue, newValue);
    ABetterRoutePlanner.updateAirconConsumption(newValue * 10);
  }

  @Override
  public void onCrLdcPwrMonWChanged(int oldValue, int newValue) throws RemoteException {
    if (mCrLdcPwrMonWChanged.equals(oldValue, newValue)) {
      return;
    }
    mCrLdcPwrMonWChanged.setValues(oldValue, newValue);
    ABetterRoutePlanner.updateElecticalDeviceConsumption(newValue * 10);
  }

  @Override
  public void onCrMcuMotPwrAvnKwChanged(int oldValue, int newValue) throws RemoteException {
    if (mCrMcuMotPwrAvnKwChanged.equals(oldValue, newValue)) {
      return;
    }
    mCrMcuMotPwrAvnKwChanged.setValues(oldValue, newValue);
    ABetterRoutePlanner.updateEngineConsumption((byte) newValue);
  }

  @Override
  public void onCfBmsFstChaChanged(int oldValue, int newValue) throws RemoteException {
  }

  @Override
  public void onCfBmsFstEvseFltAlramChanged(int oldValue, int newValue) throws RemoteException {
  }

  @Override
  public void onCfVcuDteOffChanged(int oldValue, int newValue) throws RemoteException {
  }

  @Override
  public void onCrBmsQcChgRemainedTimeMinChanged(int oldValue, int newValue) throws RemoteException {
  }

  @Override
  public void onCrVcuDistEmptyAddKmChanged(int oldValue, int newValue) throws RemoteException {
  }

  @Override
  public void onCrVcuDistEmptyKmChanged(int oldValue, int newValue) throws RemoteException {
  }
}
