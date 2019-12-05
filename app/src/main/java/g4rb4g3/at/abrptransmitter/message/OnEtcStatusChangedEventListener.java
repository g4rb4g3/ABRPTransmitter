package g4rb4g3.at.abrptransmitter.message;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;

import com.lge.ivi.IVIMessage;
import com.lge.ivi.ModeManager;
import com.lge.ivi.OnIVIMessageListener;
import com.lge.ivi.carinfo.CarInfoManager;

import g4rb4g3.at.abrptransmitter.MainActivity;

public class OnEtcStatusChangedEventListener extends OnIVIMessageListener {

  private CarInfoManager mCarInfoManager;
  private ModeManager mModeManager;
  private SharedPreferences mSharedPreferences;

  public OnEtcStatusChangedEventListener(Context context) {
    mCarInfoManager = CarInfoManager.getInstance();
    mModeManager = ModeManager.getInstance(context);
    mSharedPreferences = context.getSharedPreferences(MainActivity.PREFERENCES_NAME, Context.MODE_PRIVATE);
    mute();
  }

  @Override
  public void onIVIMessage(IVIMessage iviMessage) {
    mute();
  }

  private void mute() {
    if(mSharedPreferences.getBoolean(MainActivity.PREFERENCES_MUTE_REVERSE_GEAR, false)) {
      new Handler().postDelayed(new Runnable() {
        @Override
        public void run() {
          String carIf = mCarInfoManager.getCarIF();
          mModeManager.setMute(carIf.charAt(3) == '1');
        }
      }, 500);
    }
  }
}
