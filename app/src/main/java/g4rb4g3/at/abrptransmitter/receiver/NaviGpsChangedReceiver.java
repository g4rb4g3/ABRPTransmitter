package g4rb4g3.at.abrptransmitter.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import g4rb4g3.at.abrptransmitter.service.AbrpTransmitterService;

import static g4rb4g3.at.abrptransmitter.Constants.EXTRA_ALT;
import static g4rb4g3.at.abrptransmitter.Constants.EXTRA_LAT;
import static g4rb4g3.at.abrptransmitter.Constants.EXTRA_LON;

public class NaviGpsChangedReceiver extends BroadcastReceiver {
  @Override
  public void onReceive(Context context, Intent intent) {
    double lat = intent.getDoubleExtra(EXTRA_LAT, 0);
    double lon = intent.getDoubleExtra(EXTRA_LON, 0);
    double alt = intent.getDoubleExtra(EXTRA_ALT, 0);

    Intent serviceIntent = new Intent(context, AbrpTransmitterService.class);
    serviceIntent.putExtra(EXTRA_LAT, lat);
    serviceIntent.putExtra(EXTRA_LON, lon);
    serviceIntent.putExtra(EXTRA_ALT, alt);
    context.startService(serviceIntent);
  }
}
