package g4rb4g3.at.abrptransmitter;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class NaviGpsChangedReceiver extends BroadcastReceiver {
  public static final String EXTRA_LAT = "com.hkmc.telematics.gis.extra.LAT";
  public static final String EXTRA_LON = "com.hkmc.telematics.gis.extra.LON";
  public static final String EXTRA_ALT = "com.hkmc.telematics.gis.extra.ALT";

  private static NumberHolder mLatLonHolder = new NumberHolder();

  @Override
  public void onReceive(Context context, Intent intent) {
    double lat = intent.getDoubleExtra(EXTRA_LAT, 0);
    double lon = intent.getDoubleExtra(EXTRA_LON, 0);

    if(mLatLonHolder.equals(lat, lon)) {
      return;
    }
    mLatLonHolder.setValues(lat, lon);

    double alt = intent.getDoubleExtra(EXTRA_ALT, 0);

    ABetterRoutePlanner.applyAbrpSettings(context);
    ABetterRoutePlanner.updateGps(lat, lon, alt);
  }
}
