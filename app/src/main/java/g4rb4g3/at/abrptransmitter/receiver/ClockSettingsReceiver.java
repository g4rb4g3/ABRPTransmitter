package g4rb4g3.at.abrptransmitter.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class ClockSettingsReceiver extends BroadcastReceiver {
  private static int mCalls = 0;
  private static long mLastCall = 0;

  @Override
  public void onReceive(Context context, Intent intent) {
    if(System.currentTimeMillis() - mLastCall > 2000) {
      mCalls = 0;
    }
    mLastCall = System.currentTimeMillis();
    mCalls++;
    if (mCalls == 2) {
      mCalls = 0;
      context.startActivity(context.getPackageManager().getLaunchIntentForPackage("com.lge.ivi.engineermode"));
    }
  }
}
