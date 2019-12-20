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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Timer;
import java.util.TimerTask;

import cz.msebera.android.httpclient.Header;
import g4rb4g3.at.abrptransmitter.greencar.BatteryChargeListener;
import g4rb4g3.at.abrptransmitter.greencar.EVPowerDisplayListener;
import g4rb4g3.at.abrptransmitter.greencar.GreenCarGwEvP06ExtraListener;
import g4rb4g3.at.abrptransmitter.hvac.HvacTempListener;

public class ABetterRoutePlanner {
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
  private static final long sSendUpdateInterval = 5000L;
  private static final Logger LOG = LoggerFactory.getLogger(ABetterRoutePlanner.class.getSimpleName());
  private static String sAbetterrouteplanner_token = null;
  private static GreenCarManager sGreenCarManager;
  private static IBatteryChargeListener sBatteryChargeListener;
  private static IEVPowerDisplayListener sEvPowerDisplayListener;
  private static IGreenCarGwEvP06ExtraListener sGreenCarGwEvP06ExtraListener;
  private static HvacManager sHvacManager;
  private static IHvacTempListener sHvacTempListener;
  private static boolean sTransmitData = false, sIsCharging = false;
  private static CarInfoManager sCarInfoManager;
  private static Average sAverageElectricalDevice = new Average();
  private static Average sAverageHeating = new Average();
  private static Average sAverageAirCon = new Average();
  private static Average sAverageEngine = new Average();
  private static JSONObject sJTlmObj;
  private static AsyncHttpClient sAsyncHttpClient;
  private static Timer sTSendUpdate;
  private static TimerTask sTtSendUpdate = new TimerTask() {
    @Override
    public void run() {
      try {
        sendUpdate();
      } catch (JSONException e) {
        Log.e(MainActivity.TAG, "error sending update", e);
      }
    }
  };

  static {
    sCarInfoManager = CarInfoManager.getInstance();
    sGreenCarManager = GreenCarManager.getInstance(null);
    sHvacManager = HvacManager.getInstance();
    sAsyncHttpClient = new AsyncHttpClient(true, 80, 443);
    sAsyncHttpClient.setTimeout((int) sSendUpdateInterval - 200); // request needs to timeout before next request so we do not end up with multiple concurrent requests

    sBatteryChargeListener = new BatteryChargeListener();
    sGreenCarManager.register(sBatteryChargeListener);

    sEvPowerDisplayListener = new EVPowerDisplayListener();
    sGreenCarManager.register(sEvPowerDisplayListener);

    sGreenCarGwEvP06ExtraListener = new GreenCarGwEvP06ExtraListener();
    sGreenCarManager.register(sGreenCarGwEvP06ExtraListener);

    sHvacTempListener = new HvacTempListener();
    sHvacManager.registerHvacTempListener(sHvacTempListener);

    sIsCharging = sGreenCarManager.getChargeStatus() == 1;

    try {
      sJTlmObj = new JSONObject();
      sJTlmObj.put(ABETTERROUTEPLANNER_JSON_SOC, sGreenCarManager.getBatteryChargePersent());
      sJTlmObj.put(ABETTERROUTEPLANNER_JSON_SPEED, sCarInfoManager.getCarSpeed());
      sJTlmObj.put(ABETTERROUTEPLANNER_JSON_GPS_LAT, 0.0);
      sJTlmObj.put(ABETTERROUTEPLANNER_JSON_GPS_LON, 0.0);
      sJTlmObj.put(ABETTERROUTEPLANNER_JSON_CHARGING, sIsCharging ? 1 : 0);
      sJTlmObj.put(ABETTERROUTEPLANNER_JSON_CAR_MODEL, ABETTERROUTEPLANNER_JSON_CAR_MODEL_IONIQ28);

      sJTlmObj.put(ABETTERROUTEPLANNER_JSON_POWER, 0.0);
      sJTlmObj.put(ABETTERROUTEPLANNER_JSON_GPS_ELEVATION, 0.0);
      sJTlmObj.put(ABETTERROUTEPLANNER_JSON_TEMPERATURE_EXT, sHvacManager.getAmbientTemperatureC());
    } catch (JSONException e) {
      Log.e(MainActivity.TAG, "error building json object", e);
    }
    sTSendUpdate = new Timer();
    sTSendUpdate.schedule(sTtSendUpdate, sSendUpdateInterval, sSendUpdateInterval);
  }

  public static void updateGps(double lat, double lon, double alt) {
    try {
      sJTlmObj.put(ABETTERROUTEPLANNER_JSON_GPS_LAT, lat);
      sJTlmObj.put(ABETTERROUTEPLANNER_JSON_GPS_LON, lon);
      sJTlmObj.put(ABETTERROUTEPLANNER_JSON_GPS_ELEVATION, alt);
    } catch (JSONException e) {
      Log.e(MainActivity.TAG, "error updating json object", e);
    }
  }

  public static void updateSoC(int soc) {
    try {
      sJTlmObj.put(ABETTERROUTEPLANNER_JSON_SOC, soc);
    } catch (JSONException e) {
      Log.e(MainActivity.TAG, "error updating json object", e);
    }
  }

  public static void updateTemperature(float temperature) {
    try {
      sJTlmObj.put(ABETTERROUTEPLANNER_JSON_TEMPERATURE_EXT, temperature);
    } catch (JSONException e) {
      Log.e(MainActivity.TAG, "error updating json object", e);
    }
  }

  public static void updateEngineConsumption(int kw) {
    sAverageEngine.addValueToAverageConsumption(kw);
  }

  public static void updateElectricalDeviceConsumption(int w) {
    sAverageElectricalDevice.addValueToAverageConsumption((float) (w / 1000.0));
  }

  public static void updateAirconConsumption(int w) {
    sAverageAirCon.addValueToAverageConsumption((float) (w / 1000.0));
  }

  public static void updateHeatingConsumption(int w) {
    sAverageHeating.addValueToAverageConsumption((float) (w / 1000.0));
  }

  public static void updateIsCharging(boolean isCharging) {
    sIsCharging = isCharging;
  }

  private static void sendUpdate() throws JSONException {
    if (!sTransmitData) {
      return;
    }
    if (sAbetterrouteplanner_token == null) {
      Log.e(MainActivity.TAG, "missing abrp token");
      return;
    }
    if (sJTlmObj.getDouble(ABETTERROUTEPLANNER_JSON_GPS_LAT) == 0.0 && sJTlmObj.getDouble(ABETTERROUTEPLANNER_JSON_GPS_LON) == 0.0) {
      return;
    }
    // sum up all average values and use intermediate variables to be able to log suspicious values
    float AverageHeating = sAverageHeating.getAverageConsumption();
    float AverageAirCon = sAverageAirCon.getAverageConsumption();
    float AverageEngine = sAverageEngine.getAverageConsumption();
    float AverageElectricalDevice = sAverageElectricalDevice.getAverageConsumption();
    float average = AverageHeating + AverageAirCon + AverageEngine + AverageElectricalDevice;
    if (average > -100 && average < 100) { // regen and consumption check
      sJTlmObj.put(ABETTERROUTEPLANNER_JSON_POWER, average);
    } else {
      sJTlmObj.put(ABETTERROUTEPLANNER_JSON_POWER, 0);
      LOG.debug("detected suspicious average value: " + average);
      LOG.debug("sAverageHeating: " + AverageHeating);
      LOG.debug("sAverageAirCon: " + AverageAirCon);
      LOG.debug("sAverageEngine: " + AverageEngine);
      LOG.debug("sAverageElectricalDevice: " + AverageElectricalDevice);
    }
    sJTlmObj.put(ABETTERROUTEPLANNER_JSON_TIME, System.currentTimeMillis() / 1000);
    sJTlmObj.put(ABETTERROUTEPLANNER_JSON_SPEED, sCarInfoManager.getCarSpeed());
    sJTlmObj.put(ABETTERROUTEPLANNER_JSON_CHARGING, sIsCharging ? 1 : 0);

    StringBuilder url = new StringBuilder(ABETTERROUTEPLANNER_URL)
        .append(ABETTERROUTEPLANNER_URL_TOKEN).append("=").append(sAbetterrouteplanner_token)
        .append("&").append(ABETTERROUTEPLANNER_URL_API_KEY).append("=").append(ABETTERROUTEPLANNER_API_KEY)
        .append("&").append(ABETTERROUTEPLANNER_URL_TELEMETRY).append("=");
    try {
      url.append(URLEncoder.encode(sJTlmObj.toString(), "UTF-8"));
    } catch (UnsupportedEncodingException e) {
      Log.e(MainActivity.TAG, "UnsupportedEncodingException", e);
      return;
    }

    sAsyncHttpClient.get(url.toString(), new AsyncHttpResponseHandler() {
      @Override
      public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

      }

      @Override
      public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
        Log.e(MainActivity.TAG, String.valueOf(statusCode), error);
      }

      @Override
      public boolean getUseSynchronousMode() {
        return false;
      }
    });
  }

  public static void applyAbrpSettings(Context context) {
    SharedPreferences sp = context.getSharedPreferences(MainActivity.PREFERENCES_NAME, Context.MODE_PRIVATE);
    sTransmitData = sp.getBoolean(MainActivity.PREFERENCES_TRANSMIT_DATA, false);
    sAbetterrouteplanner_token = sp.getString(MainActivity.PREFERENCES_TOKEN, null);
  }

  @Override
  protected void finalize() {
    sTSendUpdate.cancel();
    sGreenCarManager.unregister(sBatteryChargeListener);
    sGreenCarManager.unregister(sEvPowerDisplayListener);
    sGreenCarManager.unregister(sGreenCarGwEvP06ExtraListener);

    sHvacManager.unRegisterHvacTempListener(sHvacTempListener);
  }
}
