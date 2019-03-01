package g4rb4g3.at.abrptransmitter.hvac;

import android.os.RemoteException;

import com.lge.ivi.hvac.IHvacTempListener;

import g4rb4g3.at.abrptransmitter.ABetterRoutePlanner;

public class HvacTempListener extends IHvacTempListener.Stub {

  private float mAmbientTempMessageC;

  @Override
  public void onAmbientTempMessage(float[] floats, int i) throws RemoteException {
    if (floats[0] == mAmbientTempMessageC) {
      return;
    }
    mAmbientTempMessageC = floats[0];
    ABetterRoutePlanner.updateTemperature(mAmbientTempMessageC);
  }

  @Override
  public void onInnerTempMessage(float[] floats, int i) throws RemoteException {
  }
}
