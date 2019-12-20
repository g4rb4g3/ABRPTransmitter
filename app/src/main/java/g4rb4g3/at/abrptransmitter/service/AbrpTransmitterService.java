package g4rb4g3.at.abrptransmitter.service;


import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import androidx.annotation.Nullable;

import com.lge.ivi.carinfo.CarInfoManager;
import com.lge.ivi.greencar.GreenCarManager;
import com.lge.ivi.hvac.HvacManager;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import cz.msebera.android.httpclient.Header;

import static g4rb4g3.at.abrptransmitter.Constants.ABETTERROUTEPLANNER_API_KEY;
import static g4rb4g3.at.abrptransmitter.Constants.ABETTERROUTEPLANNER_JSON_CAR_MODEL;
import static g4rb4g3.at.abrptransmitter.Constants.ABETTERROUTEPLANNER_JSON_CAR_MODEL_IONIQ28;
import static g4rb4g3.at.abrptransmitter.Constants.ABETTERROUTEPLANNER_JSON_CHARGING;
import static g4rb4g3.at.abrptransmitter.Constants.ABETTERROUTEPLANNER_JSON_GPS_ELEVATION;
import static g4rb4g3.at.abrptransmitter.Constants.ABETTERROUTEPLANNER_JSON_GPS_LAT;
import static g4rb4g3.at.abrptransmitter.Constants.ABETTERROUTEPLANNER_JSON_GPS_LON;
import static g4rb4g3.at.abrptransmitter.Constants.ABETTERROUTEPLANNER_JSON_POWER;
import static g4rb4g3.at.abrptransmitter.Constants.ABETTERROUTEPLANNER_JSON_SOC;
import static g4rb4g3.at.abrptransmitter.Constants.ABETTERROUTEPLANNER_JSON_SPEED;
import static g4rb4g3.at.abrptransmitter.Constants.ABETTERROUTEPLANNER_JSON_TEMPERATURE_EXT;
import static g4rb4g3.at.abrptransmitter.Constants.ABETTERROUTEPLANNER_JSON_TIME;
import static g4rb4g3.at.abrptransmitter.Constants.ABETTERROUTEPLANNER_URL;
import static g4rb4g3.at.abrptransmitter.Constants.ABETTERROUTEPLANNER_URL_API_KEY;
import static g4rb4g3.at.abrptransmitter.Constants.ABETTERROUTEPLANNER_URL_TELEMETRY;
import static g4rb4g3.at.abrptransmitter.Constants.ABETTERROUTEPLANNER_URL_TOKEN;
import static g4rb4g3.at.abrptransmitter.Constants.EXTRA_ALT;
import static g4rb4g3.at.abrptransmitter.Constants.EXTRA_LAT;
import static g4rb4g3.at.abrptransmitter.Constants.EXTRA_LON;
import static g4rb4g3.at.abrptransmitter.Constants.PREFERENCES_NAME;
import static g4rb4g3.at.abrptransmitter.Constants.PREFERENCES_TOKEN;
import static g4rb4g3.at.abrptransmitter.Constants.PREFERENCES_TRANSMIT_DATA;
import static g4rb4g3.at.abrptransmitter.Constants.SEND_UPDATE_INTERVAL;

public class AbrpTransmitterService extends Service {
  private static final Logger sLog = LoggerFactory.getLogger(AbrpTransmitterService.class.getSimpleName());
  private final IBinder mBinder = new AbrpTransmitterBinder();
  private List<Handler> mRegisteredHandlers = new ArrayList<>();
  private Timer mTimerSendUpdate = new Timer();
  private JSONObject mJTlmObj = new JSONObject();
  private GreenCarManager mGreenCarManager = null;
  private CarInfoManager mCarInfoManager = null;
  private HvacManager mHvacManager = null;
  private SharedPreferences mSharedPreferences = null;
  private AsyncHttpClient mAsyncHttpClient = new AsyncHttpClient(true, 80, 443);
  private AsyncHttpResponseHandler mAsyncHttpResponseHandler = new AsyncHttpResponseHandler() {
    @Override
    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
    }

    @Override
    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
      sLog.error("failed to send update http error " + statusCode, error);
    }

    @Override
    public boolean getUseSynchronousMode() {
      return false;
    }
  };
  private TimerTask mTimerTaskSendUpdate = new TimerTask() {
    @Override
    public void run() {
      try {
        sendUpdate();
      } catch (JSONException e) {
        sLog.error("error sending update", e);
      }
    }
  };

  @Nullable
  @Override
  public IBinder onBind(Intent intent) {
    return mBinder;
  }

  @Override
  public void onCreate() {
    mSharedPreferences = getApplicationContext().getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
    mGreenCarManager = GreenCarManager.getInstance(getApplicationContext());
    mCarInfoManager = CarInfoManager.getInstance();
    mHvacManager = HvacManager.getInstance();
    try {
      mJTlmObj.put(ABETTERROUTEPLANNER_JSON_CAR_MODEL, ABETTERROUTEPLANNER_JSON_CAR_MODEL_IONIQ28);
      mJTlmObj.put(ABETTERROUTEPLANNER_JSON_GPS_LAT, 0.0);
      mJTlmObj.put(ABETTERROUTEPLANNER_JSON_GPS_LON, 0.0);
      mJTlmObj.put(ABETTERROUTEPLANNER_JSON_GPS_ELEVATION, 0.0);
    } catch (JSONException e) {
      sLog.error("error building json object", e);
    }
    mAsyncHttpClient.setTimeout((int) SEND_UPDATE_INTERVAL - 200); // request needs to timeout before next request so we do not end up with multiple concurrent requests
    mTimerSendUpdate.schedule(mTimerTaskSendUpdate, SEND_UPDATE_INTERVAL, SEND_UPDATE_INTERVAL);
  }

  @Override
  public int onStartCommand(Intent intent, int flags, int startId) {
    double lat = intent.getDoubleExtra(EXTRA_LAT, 0.0);
    double lon = intent.getDoubleExtra(EXTRA_LON, 0.0);
    double alt = intent.getDoubleExtra(EXTRA_ALT, 0.0);

    if (lat != 0.0 && lon != 0.0) {
      try {
        mJTlmObj.put(ABETTERROUTEPLANNER_JSON_GPS_LAT, lat);
        mJTlmObj.put(ABETTERROUTEPLANNER_JSON_GPS_LON, lon);
        mJTlmObj.put(ABETTERROUTEPLANNER_JSON_GPS_ELEVATION, alt);
      } catch (JSONException e) {
        sLog.error("error updating json object with gps data", e);
      }
    }

    return START_NOT_STICKY;
  }

  @Override
  public void onDestroy() {
    super.onDestroy();

  }

  public void registerHandler(Handler handler) {
    mRegisteredHandlers.add(handler);
  }

  public void unregisterHandler(Handler handler) {
    mRegisteredHandlers.remove(handler);
  }

  public JSONObject getTelemetryObject() throws JSONException {
    JSONObject tlmObj = new JSONObject();
    for (Iterator<String> iterator = mJTlmObj.keys(); iterator.hasNext(); ) {
      String key = iterator.next();
      tlmObj.put(key, mJTlmObj.optJSONObject(key));
    }
    return tlmObj;
  }

  private void sendUpdate() throws JSONException {
    if (!mSharedPreferences.getBoolean(PREFERENCES_TRANSMIT_DATA, false)) {
      return;
    }
    if (mJTlmObj.getDouble(ABETTERROUTEPLANNER_JSON_GPS_LAT) == 0.0 && mJTlmObj.getDouble(ABETTERROUTEPLANNER_JSON_GPS_LON) == 0.0) {
      return;
    }
    String token = mSharedPreferences.getString(PREFERENCES_TOKEN, null);
    if (token == null || token.length() == 0) {
      sLog.error("transmitting data enabled but missing abrp token");
      return;
    }
    if (!updateTelemetryObject()) {
      return;
    }
    StringBuilder url = new StringBuilder(ABETTERROUTEPLANNER_URL)
        .append(ABETTERROUTEPLANNER_URL_TOKEN).append("=").append(token)
        .append("&").append(ABETTERROUTEPLANNER_URL_API_KEY).append("=").append(ABETTERROUTEPLANNER_API_KEY)
        .append("&").append(ABETTERROUTEPLANNER_URL_TELEMETRY).append("=");
    try {
      url.append(URLEncoder.encode(mJTlmObj.toString(), "UTF-8"));
    } catch (UnsupportedEncodingException e) {
      sLog.error(e.getMessage(), e);
      return;
    }

    mAsyncHttpClient.get(url.toString(), mAsyncHttpResponseHandler);
  }

  private boolean updateTelemetryObject() {
    double aircon = mGreenCarManager.getCrDatcAcnCompPwrConW() / 100.0;
    double heating = mGreenCarManager.getCrDatcPtcPwrConW() / 100.0;
    double electric = mGreenCarManager.getCrLdcPwrMonW() / 100.0;
    double engine = mGreenCarManager.getCrMcuMotPwrAvnKw();
    try {
      mJTlmObj.put(ABETTERROUTEPLANNER_JSON_TIME, System.currentTimeMillis() / 1000);
      mJTlmObj.put(ABETTERROUTEPLANNER_JSON_SOC, mGreenCarManager.getBatteryChargePersent());
      mJTlmObj.put(ABETTERROUTEPLANNER_JSON_SPEED, mCarInfoManager.getCarSpeed());
      mJTlmObj.put(ABETTERROUTEPLANNER_JSON_CHARGING, mGreenCarManager.getChargeStatus());
      mJTlmObj.put(ABETTERROUTEPLANNER_JSON_POWER, aircon + heating + electric + engine);
      mJTlmObj.put(ABETTERROUTEPLANNER_JSON_TEMPERATURE_EXT, mHvacManager.getAmbientTemperatureC());
      return true;
    } catch (JSONException e) {
      sLog.error("error updating json object with vehicle data", e);
    }
    return false;
  }

  public class AbrpTransmitterBinder extends Binder {
    public AbrpTransmitterService getService() {
      return AbrpTransmitterService.this;
    }
  }
}
