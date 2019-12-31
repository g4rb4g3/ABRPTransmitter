package g4rb4g3.at.abrptransmitter.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import g4rb4g3.at.abrptransmitter.MainActivity;

public class ButtonReceiver extends BroadcastReceiver {
  private static int sCalls = 0;
  private static long sLastCall = 0;

  @Override
  public void onReceive(final Context context, Intent intent) {
    if (System.currentTimeMillis() - sLastCall > 2000) {
      sCalls = 0;
    }
    sLastCall = System.currentTimeMillis();
    sCalls++;
    if (sCalls == 2) {
      sCalls = 0;
      Intent i = new Intent(context, MainActivity.class);
      i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
      context.startActivity(i);
    }
  }
}
