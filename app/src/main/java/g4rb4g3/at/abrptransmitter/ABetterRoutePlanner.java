package g4rb4g3.at.abrptransmitter;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.util.Pair;
import android.util.Log;

import com.lge.ivi.carinfo.CarInfoManager;
import com.lge.ivi.greencar.GreenCarManager;
import com.lge.ivi.greencar.IBatteryChargeListener;
import com.lge.ivi.greencar.IEVPowerDisplayListener;
import com.lge.ivi.greencar.IGreenCarGwEvP06ExtraListener;
import com.lge.ivi.hvac.HvacManager;
import com.lge.ivi.hvac.IHvacTempListener;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import cz.msebera.android.httpclient.Header;
import g4rb4g3.at.abrptransmitter.greencar.BatteryChargeListener;
import g4rb4g3.at.abrptransmitter.greencar.EVPowerDisplayListener;
import g4rb4g3.at.abrptransmitter.greencar.GreenCarGwEvP06ExtraListener;
import g4rb4g3.at.abrptransmitter.hvac.HvacTempListener;

public class ABetterRoutePlanner {
  public static final String TAG = "ABRPTransmitter";
  public static String mAbetterrouteplanner_user = null;

  public static final String ABETTERROUTEPLANNER_URL = "http://api.iternio.com/1/tlm/ioniq28";
  public static final String ABETTERROUTEPLANNER_EMAIL = "eml";
  public static final String ABETTERROUTEPLANNER_SESSION_ID = "session";
  public static final String ABETTERROUTEPLANNER_TIME = "time";
  public static final String ABETTERROUTEPLANNER_PACK_CURRENT = "eeba76";    //A
  public static final String ABETTERROUTEPLANNER_PACK_VOLTAGE = "e6f992";    //V
  public static final String ABETTERROUTEPLANNER_PACK_POWER = "e0484f";      //kW
  public static final String ABETTERROUTEPLANNER_PACK_SOC = "e50c2b";        //%
  public static final String ABETTERROUTEPLANNER_PACK_CAPACITY = "ea9e51";   //%
  public static final String ABETTERROUTEPLANNER_CHARGER_POWER = "e52d8a";    //unitless, 1 or 0 depending on if charging is taking place
  public static final String ABETTERROUTEPLANNER_OBD_SPEED = "ee4ea0";        //km/h
  public static final String ABETTERROUTEPLANNER_GPS_SPEED = "ff1001";        //km/h
  public static final String ABETTERROUTEPLANNER_GPS_ELEVATION = "ff1010";    //m
  public static final String ABETTERROUTEPLANNER_GPS_LON = "ff1005";          //deg
  public static final String ABETTERROUTEPLANNER_GPS_LAT = "ff1006";          //deg
  public static final String ABETTERROUTEPLANNER_TEMPERATURE = "ext_temp";    //C

  private static GreenCarManager mGreenCarManager;
  private static IBatteryChargeListener mBatteryChargeListener;
  private static IEVPowerDisplayListener mEvPowerDisplayListener;
  private static IGreenCarGwEvP06ExtraListener mGreenCarGwEvP06ExtraListener;

  private static HvacManager mHvacManager;
  private static IHvacTempListener mHvacTempListener;

  private static String mSessionId;
  private static boolean mLoggedin = false, mTransmitData = false;
  private static CarInfoManager mCarInfoManager;

  private static double mLat, mLon, mAlt;
  private static int mSoc, mCarSpeed, mKwEngine;
  private static float mTemperature, mKwElecticalDevice, mKwAircon, mKwHeating;

  static {
    Calendar calendar = Calendar.getInstance();
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyymmddHHmmss");
    mSessionId = simpleDateFormat.format(calendar.getTime());

    mCarInfoManager = CarInfoManager.getInstance();
    mGreenCarManager = GreenCarManager.getInstance(null);
    mHvacManager = HvacManager.getInstance();

    mBatteryChargeListener = new BatteryChargeListener();
    mGreenCarManager.register(mBatteryChargeListener);

    mEvPowerDisplayListener = new EVPowerDisplayListener();
    mGreenCarManager.register(mEvPowerDisplayListener);

    mGreenCarGwEvP06ExtraListener = new GreenCarGwEvP06ExtraListener();
    mGreenCarManager.register(mGreenCarGwEvP06ExtraListener);

    mHvacTempListener = new HvacTempListener();
    mHvacManager.registerHvacTempListener(mHvacTempListener);

    mSoc = mGreenCarManager.getBatteryChargePersent();
    mTemperature = mHvacManager.getAmbientTemperatureC();
  }

  private static void login() {
    send(new Pair<String, Object>("defaultUnit" + ABETTERROUTEPLANNER_PACK_CURRENT, "A"),
        new Pair<String, Object>("defaultUnit" + ABETTERROUTEPLANNER_PACK_VOLTAGE, "V"),
        new Pair<String, Object>("defaultUnit" + ABETTERROUTEPLANNER_PACK_POWER, "kW"),
        new Pair<String, Object>("defaultUnit" + ABETTERROUTEPLANNER_PACK_SOC, "%"),
        new Pair<String, Object>("defaultUnit" + ABETTERROUTEPLANNER_PACK_CAPACITY, "%"),
        new Pair<String, Object>("defaultUnit" + ABETTERROUTEPLANNER_OBD_SPEED, "km/h"),
        new Pair<String, Object>("defaultUnit" + ABETTERROUTEPLANNER_GPS_SPEED, "km/h"),
        new Pair<String, Object>("defaultUnit" + ABETTERROUTEPLANNER_GPS_ELEVATION, "m"),
        new Pair<String, Object>("defaultUnit" + ABETTERROUTEPLANNER_GPS_LON, "deg"),
        new Pair<String, Object>("defaultUnit" + ABETTERROUTEPLANNER_GPS_LAT, "deg"),
        new Pair<String, Object>("defaultUnit" + ABETTERROUTEPLANNER_TEMPERATURE, "C")
    );
    mLoggedin = true;
  }

  public static void updateGps(double lat, double lon, double alt) {
    mLat = lat;
    mLon = lon;
    mAlt = alt;
    sendUpdate();
  }

  public static void updateSoC(int soc) {
    mSoc = soc;
    sendUpdate();
  }

  public static void updateTemperature(float temperature) {
    mTemperature = temperature;
    sendUpdate();
  }

  public static void updateEngineConsumption(int kw) {
    mKwEngine = kw;
    sendUpdate();
  }

  public static void updateElecticalDeviceConsumption(int w) {
    mKwElecticalDevice = (float) (w / 1000.0);
  }

  public static void updateAirconConsumption(int w) {
    mKwAircon = (float) (w / 1000.0);
  }

  public static void updateHeatingConsumption(int w) {
    mKwHeating = (float) (w / 1000.0);
  }

  private static void sendUpdate() {
    if(!mTransmitData) {
      return;
    }
    if(mAbetterrouteplanner_user == null) {
      Log.e(TAG, "missing abrp mail");
      return;
    }
    if (!mLoggedin) {
      login();
    }
    if (mAlt == 0 && mLon == 0) {
      return;
    }
    mCarSpeed = mCarInfoManager.getCarSpeed();

    send(new Pair<String, Object>("k" + ABETTERROUTEPLANNER_GPS_LAT, mLat),
        new Pair<String, Object>("k" + ABETTERROUTEPLANNER_GPS_LON, mLon),
        new Pair<String, Object>("k" + ABETTERROUTEPLANNER_GPS_ELEVATION, mAlt),
        new Pair<String, Object>("k" + ABETTERROUTEPLANNER_PACK_SOC, mSoc),
        new Pair<String, Object>("k" + ABETTERROUTEPLANNER_TEMPERATURE, mTemperature),
        new Pair<String, Object>("k" + ABETTERROUTEPLANNER_OBD_SPEED, mCarSpeed),
        new Pair<String, Object>("k" + ABETTERROUTEPLANNER_CHARGER_POWER, 0),
        new Pair<String, Object>("k" + ABETTERROUTEPLANNER_PACK_POWER, mKwEngine + mKwElecticalDevice + mKwHeating + mKwAircon)
    );
  }

  private static String buildUrl(Pair<String, Object>... queryParams) throws UnsupportedEncodingException {
    StringBuilder url = new StringBuilder(ABETTERROUTEPLANNER_URL)
        .append("?").append(ABETTERROUTEPLANNER_EMAIL).append("=").append(URLEncoder.encode(mAbetterrouteplanner_user, "UTF-8"))
        .append("&").append(ABETTERROUTEPLANNER_SESSION_ID).append("=").append(mSessionId)
        .append("&").append(ABETTERROUTEPLANNER_TIME).append("=").append(System.currentTimeMillis());
    for (Pair<String, Object> queryParam : queryParams) {
      url.append("&").append(queryParam.first).append("=").append(URLEncoder.encode(queryParam.second.toString(), "UTF-8"));
    }
    return url.toString();
  }

  private static void send(Pair<String, Object>... queryParams) {
    try {
      String url = buildUrl(queryParams);
      AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
      asyncHttpClient.get(url, new AsyncHttpResponseHandler() {
        @Override
        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

        }

        @Override
        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
          Log.e(TAG, String.valueOf(statusCode), error);
        }
      });
    } catch (UnsupportedEncodingException e) {
      Log.e(TAG, "UnsupportedEncodingException", e);
    }
  }

  public static void applyAbrpSettings(Context context) {
    SharedPreferences sp = context.getSharedPreferences(MainActivity.PREFERENCES_NAME, Context.MODE_PRIVATE);
    mTransmitData = sp.getBoolean(MainActivity.PREFERENCES_TRANSMIT_DATA, false);
    mAbetterrouteplanner_user = sp.getString(MainActivity.PREFERENCES_MAIL, null);
  }

  @Override
  protected void finalize() {
    mGreenCarManager.unregister(mBatteryChargeListener);
    mGreenCarManager.unregister(mEvPowerDisplayListener);
    mGreenCarManager.unregister(mGreenCarGwEvP06ExtraListener);

    mHvacManager.unRegisterHvacTempListener(mHvacTempListener);
  }
}
