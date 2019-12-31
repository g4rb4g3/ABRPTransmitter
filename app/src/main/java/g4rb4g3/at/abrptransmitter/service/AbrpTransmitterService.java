package g4rb4g3.at.abrptransmitter.service;


import android.app.Notification;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;

import com.lge.ivi.carinfo.CarInfoManager;
import com.lge.ivi.greencar.GreenCarManager;
import com.lge.ivi.hvac.HvacManager;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import cz.msebera.android.httpclient.Header;
import g4rb4g3.at.abrptransmitter.R;
import g4rb4g3.at.abrptransmitter.Utils;
import g4rb4g3.at.abrptransmitter.receiver.ConnectivityChangeReceiver;

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
import static g4rb4g3.at.abrptransmitter.Constants.ABETTERROUTEPLANNER_TELEMETRY_URL;
import static g4rb4g3.at.abrptransmitter.Constants.ABETTERROUTEPLANNER_URL_API_KEY;
import static g4rb4g3.at.abrptransmitter.Constants.ABETTERROUTEPLANNER_URL_TELEMETRY;
import static g4rb4g3.at.abrptransmitter.Constants.ABETTERROUTEPLANNER_URL_TOKEN;
import static g4rb4g3.at.abrptransmitter.Constants.EXTRA_ALT;
import static g4rb4g3.at.abrptransmitter.Constants.EXTRA_LAT;
import static g4rb4g3.at.abrptransmitter.Constants.EXTRA_LON;
import static g4rb4g3.at.abrptransmitter.Constants.INTERVAL_AVERAGE_COLLECTOR;
import static g4rb4g3.at.abrptransmitter.Constants.INTERVAL_SEND_UPDATE;
import static g4rb4g3.at.abrptransmitter.Constants.MESSAGE_CONNECTIVITY_CHANGED;
import static g4rb4g3.at.abrptransmitter.Constants.MESSAGE_LAST_ERROR_ABRPSERVICE;
import static g4rb4g3.at.abrptransmitter.Constants.MESSAGE_LAST_UPDATE_SENT;
import static g4rb4g3.at.abrptransmitter.Constants.NOTIFICATION_ID_ABRPTRANSMITTERSERVICE;
import static g4rb4g3.at.abrptransmitter.Constants.PREFERENCES_NAME;
import static g4rb4g3.at.abrptransmitter.Constants.PREFERENCES_TOKEN;
import static g4rb4g3.at.abrptransmitter.Constants.PREFERENCES_TRANSMIT_DATA;

public class AbrpTransmitterService extends Service {
  private static final Logger sLog = LoggerFactory.getLogger(AbrpTransmitterService.class.getSimpleName());
  private final IBinder mBinder = new AbrpTransmitterBinder();
  private List<Handler> mRegisteredHandlers = new ArrayList<>();
  private ScheduledExecutorService mScheduledExecutorService = null;
  private AverageCollector mAverageCollector = null;
  private JSONObject mJTlmObj = new JSONObject();
  private GreenCarManager mGreenCarManager = null;
  private CarInfoManager mCarInfoManager = null;
  private HvacManager mHvacManager = null;
  private SharedPreferences mSharedPreferences = null;
  private boolean mWifiConnected = false;
  private Handler mHandler = new Handler(Looper.getMainLooper()) {
    @Override
    public void handleMessage(Message msg) {
      switch (msg.what) {
        case MESSAGE_CONNECTIVITY_CHANGED:
          mWifiConnected = ((Collection<String>) msg.obj).size() > 0;
          break;
      }
    }
  };
  private ConnectivityChangeReceiver mConnectivityChangeReceiver = new ConnectivityChangeReceiver(mHandler);

  @Nullable
  @Override
  public IBinder onBind(Intent intent) {
    return mBinder;
  }

  @Override
  public void onCreate() {
    sLog.info(AbrpTransmitterService.class.getSimpleName() + " starting");
    Notification notification = new NotificationCompat.Builder(this, null)
        .setContentTitle(getString(R.string.app_name))
        .setSmallIcon(R.mipmap.ic_launcher)
        .setPriority(NotificationCompat.PRIORITY_MAX)
        .build();
    startForeground(NOTIFICATION_ID_ABRPTRANSMITTERSERVICE, notification);

    mSharedPreferences = getApplicationContext().getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
    mGreenCarManager = GreenCarManager.getInstance(getApplicationContext());
    mCarInfoManager = CarInfoManager.getInstance();
    mHvacManager = HvacManager.getInstance();
    mAverageCollector = new AverageCollector(mGreenCarManager);
    try {
      mJTlmObj.put(ABETTERROUTEPLANNER_JSON_CAR_MODEL, ABETTERROUTEPLANNER_JSON_CAR_MODEL_IONIQ28);
      mJTlmObj.put(ABETTERROUTEPLANNER_JSON_GPS_LAT, 0.0);
      mJTlmObj.put(ABETTERROUTEPLANNER_JSON_GPS_LON, 0.0);
      mJTlmObj.put(ABETTERROUTEPLANNER_JSON_GPS_ELEVATION, 0.0);
    } catch (JSONException e) {
      sLog.error("error building json object", e);
    }
    getApplicationContext().registerReceiver(mConnectivityChangeReceiver, new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE"));
    mScheduledExecutorService = Executors.newScheduledThreadPool(2);
    mScheduledExecutorService.scheduleWithFixedDelay(new AbrpUpdater(), INTERVAL_SEND_UPDATE, INTERVAL_SEND_UPDATE, TimeUnit.MILLISECONDS);
    mScheduledExecutorService.scheduleAtFixedRate(mAverageCollector, INTERVAL_AVERAGE_COLLECTOR, INTERVAL_AVERAGE_COLLECTOR, TimeUnit.MILLISECONDS);
  }

  @Override
  public int onStartCommand(Intent intent, int flags, int startId) {
    if (intent != null) {
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
    }
    return START_STICKY;
  }

  @Override
  public void onDestroy() {
    sLog.info(AbrpTransmitterService.class.getSimpleName() + " shutting down");
    mScheduledExecutorService.shutdownNow();
    getApplicationContext().unregisterReceiver(mConnectivityChangeReceiver);
    stopForeground(true);
  }

  public void registerHandler(Handler handler) {
    mRegisteredHandlers.add(handler);
  }

  public void unregisterHandler(Handler handler) {
    mRegisteredHandlers.remove(handler);
  }

  public JSONObject getTelemetryObject() {
    return mJTlmObj;
  }

  private void notifyHandlers(int what, Object obj) {
    Message msg = new Message();
    msg.what = what;
    msg.obj = obj;
    for (Handler handler : mRegisteredHandlers) {
      handler.sendMessage(msg);
    }
  }

  private static class AverageCollector implements Runnable {
    private LinkedHashMap<Long, Double> sConsumptionCollector = new LinkedHashMap<>();
    private GreenCarManager sGreenCarManager;
    private volatile boolean sCollect = true;

    private AverageCollector(GreenCarManager greenCarManager) {
      sGreenCarManager = greenCarManager;
    }

    public double getAverage() {
      if (sConsumptionCollector.size() == 0) {
        return 0.0;
      }
      sCollect = false;
      double average;
      if (sConsumptionCollector.size() == 1) {
        average = sConsumptionCollector.values().iterator().next();
      } else {
        long start = Collections.min(sConsumptionCollector.keySet());
        long end = Collections.max(sConsumptionCollector.keySet());
        long duration = end - start;
        double consumptionSum = 0;
        double lastConsumption = sConsumptionCollector.get(start);
        sConsumptionCollector.remove(start);
        for (Map.Entry<Long, Double> e : sConsumptionCollector.entrySet()) {
          end = e.getKey();
          consumptionSum += lastConsumption * (end - start);
          start = end;
          lastConsumption = e.getValue();
        }
        average = consumptionSum / duration;
      }
      sConsumptionCollector.clear();
      sCollect = true;
      return average;
    }

    @Override
    public void run() {
      if (!sCollect) {
        return;
      }
      double aircon = sGreenCarManager.getCrDatcAcnCompPwrConW() / 100.0;
      double heating = sGreenCarManager.getCrDatcPtcPwrConW() / 100.0;
      double electric = sGreenCarManager.getCrLdcPwrMonW() / 100.0;
      byte engine = (byte) sGreenCarManager.getCrMcuMotPwrAvnKw();

      sConsumptionCollector.put(System.currentTimeMillis(), aircon + heating + electric + engine);
    }
  }

  public class AbrpTransmitterBinder extends Binder {
    public AbrpTransmitterService getService() {
      return AbrpTransmitterService.this;
    }
  }

  private class AbrpUpdater implements Runnable {
    private AsyncHttpClient mAsyncHttpClient = new AsyncHttpClient(true, 80, 443);
    private AsyncHttpResponseHandler mAsyncHttpResponseHandler = new AsyncHttpResponseHandler() {
      @Override
      public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
        notifyHandlers(MESSAGE_LAST_UPDATE_SENT, Utils.getTimestamp());
      }

      @Override
      public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
        if (error instanceof IOException && error.getMessage().startsWith("UnknownHostException")) {
          sLog.info(error.getMessage());
        } else {
          sLog.error("error sending update", error);
        }
        notifyHandlers(MESSAGE_LAST_ERROR_ABRPSERVICE, error.getLocalizedMessage());
      }

      @Override
      public boolean getUseSynchronousMode() {
        return false;
      }
    };

    @Override
    public void run() {
      try {
        mAsyncHttpClient.setTimeout((int) INTERVAL_SEND_UPDATE - 200); // request needs to timeout before next request so we do not end up with multiple concurrent requests

        if (!mWifiConnected) {
          notifyHandlers(MESSAGE_LAST_ERROR_ABRPSERVICE, getString(R.string.no_wifi_ip));
          return;
        }
        if (!mSharedPreferences.getBoolean(PREFERENCES_TRANSMIT_DATA, false)) {
          return;
        }
        if (mJTlmObj.getDouble(ABETTERROUTEPLANNER_JSON_GPS_LAT) == 0.0 && mJTlmObj.getDouble(ABETTERROUTEPLANNER_JSON_GPS_LON) == 0.0) {
          return;
        }
        String token = mSharedPreferences.getString(PREFERENCES_TOKEN, null);
        if (token == null || token.length() == 0) {
          String msg = getString(R.string.token_missing);
          notifyHandlers(MESSAGE_LAST_ERROR_ABRPSERVICE, msg);
          sLog.error(msg);
          return;
        }

        mJTlmObj.put(ABETTERROUTEPLANNER_JSON_TIME, System.currentTimeMillis() / 1000);
        mJTlmObj.put(ABETTERROUTEPLANNER_JSON_SOC, mGreenCarManager.getBatteryChargePersent());
        mJTlmObj.put(ABETTERROUTEPLANNER_JSON_SPEED, mCarInfoManager.getCarSpeed());
        mJTlmObj.put(ABETTERROUTEPLANNER_JSON_CHARGING, mGreenCarManager.getChargeStatus());
        mJTlmObj.put(ABETTERROUTEPLANNER_JSON_POWER, mAverageCollector.getAverage());
        mJTlmObj.put(ABETTERROUTEPLANNER_JSON_TEMPERATURE_EXT, mHvacManager.getAmbientTemperatureC());

        StringBuilder url = new StringBuilder(ABETTERROUTEPLANNER_TELEMETRY_URL)
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
      } catch (JSONException e) {
        sLog.error("error sending update", e);
      }
    }
  }
}
