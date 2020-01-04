package g4rb4g3.at.abrptransmitter.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class ClockSettingsReceiver extends BroadcastReceiver {
  private static int sCalls = 0;
  private static long sLastCall = 0;

  @Override
  public void onReceive(Context context, Intent intent) {
    if (System.currentTimeMillis() - sLastCall > 2000) {
      sCalls = 0;
    }
    sLastCall = System.currentTimeMillis();
    sCalls++;
    if (sCalls == 2) {
      sCalls = 0;
      context.startActivity(context.getPackageManager().getLaunchIntentForPackage("com.lge.ivi.engineermode"));
    }
  }
}
