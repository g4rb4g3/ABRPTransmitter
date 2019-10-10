package g4rb4g3.at.abrptransmitter;

import android.content.Context;
import android.content.SharedPreferences;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import cz.msebera.android.httpclient.Header;
import g4rb4g3.at.abrptransmitter.greencar.BatteryChargeListener;
import g4rb4g3.at.abrptransmitter.greencar.EVPowerDisplayListener;
import g4rb4g3.at.abrptransmitter.greencar.GreenCarGwEvP06ExtraListener;
import g4rb4g3.at.abrptransmitter.hvac.HvacTempListener;

public class ABetterRoutePlanner {
  public static final String TAG = "ABRPTransmitter";
  public static final String ABETTERROUTEPLANNER_URL = "https://api.iternio.com/1/tlm/send?";
  public static final String ABETTERROUTEPLANNER_URL_TOKEN = "token";
  public static final String ABETTERROUTEPLANNER_URL_API_KEY = "api_key";
  public static final String ABETTERROUTEPLANNER_URL_TELEMETRY = "tlm";
  public static final String ABETTERROUTEPLANNER_JSON_TIME = "utc";
  public static final String ABETTERROUTEPLANNER_JSON_CURRENT = "current";             //A
  public static final String ABETTERROUTEPLANNER_JSON_VOLTAGE = "voltage";             //V
  public static final String ABETTERROUTEPLANNER_JSON_POWER = "power";                 //kW
  public static final String ABETTERROUTEPLANNER_JSON_SOC = "soc";                     //%
  public static final String ABETTERROUTEPLANNER_JSON_SOH = "soh";                     //%
  public static final String ABETTERROUTEPLANNER_JSON_SPEED = "speed";                 //km/h
  public static final String ABETTERROUTEPLANNER_JSON_GPS_ELEVATION = "elevation";     //m
  public static final String ABETTERROUTEPLANNER_JSON_GPS_LON = "lon";                 //deg
  public static final String ABETTERROUTEPLANNER_JSON_GPS_LAT = "lat";                 //deg
  public static final String ABETTERROUTEPLANNER_JSON_TEMPERATURE_EXT = "ext_temp";    //C
  public static final String ABETTERROUTEPLANNER_JSON_TEMPERATURE_BATT = "batt_temp";  //C
  public static final String ABETTERROUTEPLANNER_JSON_CHARGING = "is_charging";        //0 driving, 1 charging
  public static final String ABETTERROUTEPLANNER_JSON_CAR_MODEL = "car_model";
  public static final String ABETTERROUTEPLANNER_JSON_CAR_MODEL_IONIQ28 = "hyundai:ioniq:17:28:other";

  private static final String ABETTERROUTEPLANNER_API_KEY = "INSERT YOU API KEY HERE";

  private static String mAbetterrouteplanner_token = null;
  private static GreenCarManager mGreenCarManager;
  private static IBatteryChargeListener mBatteryChargeListener;
  private static IEVPowerDisplayListener mEvPowerDisplayListener;
  private static IGreenCarGwEvP06ExtraListener mGreenCarGwEvP06ExtraListener;

  private static HvacManager mHvacManager;
  private static IHvacTempListener mHvacTempListener;

  private static boolean mTransmitData = false, mIsCharging = false;
  private static CarInfoManager mCarInfoManager;

  private static float mKwElecticalDevice, mKwAircon, mKwHeating;

  private static JSONObject jTlmObj;

  static {
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

    mIsCharging = mGreenCarManager.getChargeStatus() == 1;

    try {
      jTlmObj = new JSONObject();
      jTlmObj.put(ABETTERROUTEPLANNER_JSON_SOC, mGreenCarManager.getBatteryChargePersent());
      jTlmObj.put(ABETTERROUTEPLANNER_JSON_SPEED, mCarInfoManager.getCarSpeed());
      jTlmObj.put(ABETTERROUTEPLANNER_JSON_GPS_LAT, 0.0);
      jTlmObj.put(ABETTERROUTEPLANNER_JSON_GPS_LON, 0.0);
      jTlmObj.put(ABETTERROUTEPLANNER_JSON_CHARGING, mIsCharging ? 1 : 0);
      jTlmObj.put(ABETTERROUTEPLANNER_JSON_CAR_MODEL, ABETTERROUTEPLANNER_JSON_CAR_MODEL_IONIQ28);

      jTlmObj.put(ABETTERROUTEPLANNER_JSON_POWER, 0.0);
      jTlmObj.put(ABETTERROUTEPLANNER_JSON_GPS_ELEVATION, 0.0);
      jTlmObj.put(ABETTERROUTEPLANNER_JSON_TEMPERATURE_EXT, mHvacManager.getAmbientTemperatureC());
    } catch (JSONException e) {
      Log.e(TAG, "error building json object", e);
    }
  }

  public static void updateGps(double lat, double lon, double alt) {
    try {
      jTlmObj.put(ABETTERROUTEPLANNER_JSON_GPS_LAT, lat);
      jTlmObj.put(ABETTERROUTEPLANNER_JSON_GPS_LON, lon);
      jTlmObj.put(ABETTERROUTEPLANNER_JSON_GPS_ELEVATION, alt);
      sendUpdate();
    } catch (JSONException e) {
      Log.e(TAG, "error updating json object", e);
    }
  }

  public static void updateSoC(int soc) {
    try {
      jTlmObj.put(ABETTERROUTEPLANNER_JSON_SOC, soc);
      sendUpdate();
    } catch (JSONException e) {
      Log.e(TAG, "error updating json object", e);
    }
  }

  public static void updateTemperature(float temperature) {
    try {
      jTlmObj.put(ABETTERROUTEPLANNER_JSON_TEMPERATURE_EXT, temperature);
      sendUpdate();
    } catch (JSONException e) {
      Log.e(TAG, "error updating json object", e);
    }
  }

  public static void updateEngineConsumption(int kw) {
    try {
      jTlmObj.put(ABETTERROUTEPLANNER_JSON_POWER, kw + mKwAircon + mKwElecticalDevice + mKwHeating);
      sendUpdate();
    } catch (JSONException e) {
      Log.e(TAG, "error updating json object", e);
    }
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

  public static void updateIsCharging(boolean isCharging) { mIsCharging =  isCharging; }

  private static void sendUpdate() throws JSONException {
    if (!mTransmitData) {
      return;
    }
    if (mAbetterrouteplanner_token == null) {
      Log.e(TAG, "missing abrp token");
      return;
    }
    if (jTlmObj.getDouble(ABETTERROUTEPLANNER_JSON_GPS_LAT) == 0.0 && jTlmObj.getDouble(ABETTERROUTEPLANNER_JSON_GPS_LON) == 0.0) {
      return;
    }
    jTlmObj.put(ABETTERROUTEPLANNER_JSON_TIME, System.currentTimeMillis() / 1000);
    jTlmObj.put(ABETTERROUTEPLANNER_JSON_SPEED, mCarInfoManager.getCarSpeed());
    jTlmObj.put(ABETTERROUTEPLANNER_JSON_CHARGING, mIsCharging ? 1 : 0);

    StringBuilder url = new StringBuilder(ABETTERROUTEPLANNER_URL)
        .append(ABETTERROUTEPLANNER_URL_TOKEN).append("=").append(mAbetterrouteplanner_token)
        .append("&").append(ABETTERROUTEPLANNER_URL_API_KEY).append("=").append(ABETTERROUTEPLANNER_API_KEY)
        .append("&").append(ABETTERROUTEPLANNER_URL_TELEMETRY).append("=");
    try {
      url.append(URLEncoder.encode(jTlmObj.toString(), "UTF-8"));
    } catch (UnsupportedEncodingException e) {
      Log.e(TAG, "UnsupportedEncodingException", e);
      return;
    }
    AsyncHttpClient asyncHttpClient = new AsyncHttpClient(true, 80, 443);
    asyncHttpClient.get(url.toString(), new AsyncHttpResponseHandler() {
      @Override
      public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

      }

      @Override
      public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
        Log.e(TAG, String.valueOf(statusCode), error);
      }

      @Override
      public boolean getUseSynchronousMode() {
        return false;
      }
    });
  }

  public static void applyAbrpSettings(Context context) {
    SharedPreferences sp = context.getSharedPreferences(MainActivity.PREFERENCES_NAME, Context.MODE_PRIVATE);
    mTransmitData = sp.getBoolean(MainActivity.PREFERENCES_TRANSMIT_DATA, false);
    mAbetterrouteplanner_token = sp.getString(MainActivity.PREFERENCES_TOKEN, null);
  }

  @Override
  protected void finalize() {
    mGreenCarManager.unregister(mBatteryChargeListener);
    mGreenCarManager.unregister(mEvPowerDisplayListener);
    mGreenCarManager.unregister(mGreenCarGwEvP06ExtraListener);

    mHvacManager.unRegisterHvacTempListener(mHvacTempListener);
  }
}
