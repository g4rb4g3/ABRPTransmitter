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

import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HttpsURLConnection;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import g4rb4g3.at.abrptransmitter.R;
import g4rb4g3.at.abrptransmitter.TLSSocketFactory;
import g4rb4g3.at.abrptransmitter.Utils;
import g4rb4g3.at.abrptransmitter.receiver.ConnectivityChangeReceiver;

import static g4rb4g3.at.abrptransmitter.Constants.ABETTERROUTEPLANNER_API_KEY;
import static g4rb4g3.at.abrptransmitter.Constants.ABETTERROUTEPLANNER_API_URL;
import static g4rb4g3.at.abrptransmitter.Constants.ABETTERROUTEPLANNER_JSON_CAR_MODEL;
import static g4rb4g3.at.abrptransmitter.Constants.ABETTERROUTEPLANNER_JSON_CAR_MODEL_IONIQ28;
import static g4rb4g3.at.abrptransmitter.Constants.ABETTERROUTEPLANNER_JSON_CHARGING;
import static g4rb4g3.at.abrptransmitter.Constants.ABETTERROUTEPLANNER_JSON_GPS_ELEVATION;
import static g4rb4g3.at.abrptransmitter.Constants.ABETTERROUTEPLANNER_JSON_GPS_LAT;
import static g4rb4g3.at.abrptransmitter.Constants.ABETTERROUTEPLANNER_JSON_GPS_LON;
import static g4rb4g3.at.abrptransmitter.Constants.ABETTERROUTEPLANNER_JSON_POWER;
import static g4rb4g3.at.abrptransmitter.Constants.ABETTERROUTEPLANNER_JSON_SOC;
import static g4rb4g3.at.abrptransmitter.Constants.ABETTERROUTEPLANNER_JSON_SPEED;
import static g4rb4g3.at.abrptransmitter.Constants.ABETTERROUTEPLANNER_JSON_STATUS;
import static g4rb4g3.at.abrptransmitter.Constants.ABETTERROUTEPLANNER_JSON_STATUS_OK;
import static g4rb4g3.at.abrptransmitter.Constants.ABETTERROUTEPLANNER_JSON_TEMPERATURE_EXT;
import static g4rb4g3.at.abrptransmitter.Constants.ABETTERROUTEPLANNER_JSON_TIME;
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
import static g4rb4g3.at.abrptransmitter.Constants.MESSAGE_TELEMETRY_UPDATED;
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

  private class AverageCollector implements Runnable {
    private LinkedHashMap<Long, Double> mConsumptionCollector = new LinkedHashMap<>();
    private GreenCarManager mGreenCarManager;

    private AverageCollector(GreenCarManager greenCarManager) {
      mGreenCarManager = greenCarManager;
    }

    public double getAverage() {
      if (mConsumptionCollector.size() == 0) {
        return 0.0;
      }
      synchronized (AbrpTransmitterService.this) {
        double average;
        if (mConsumptionCollector.size() == 1) {
          average = mConsumptionCollector.values().iterator().next();
        } else {
          long start = Collections.min(mConsumptionCollector.keySet());
          long end = Collections.max(mConsumptionCollector.keySet());
          long duration = end - start;
          double consumptionSum = 0;
          double lastConsumption = mConsumptionCollector.get(start);
          mConsumptionCollector.remove(start);
          for (Map.Entry<Long, Double> e : mConsumptionCollector.entrySet()) {
            end = e.getKey();
            consumptionSum += lastConsumption * (end - start);
            start = end;
            lastConsumption = e.getValue();
          }
          average = consumptionSum / duration;
        }
        mConsumptionCollector.clear();
        return average;
      }
    }

    @Override
    public void run() {
      long now = System.currentTimeMillis();
      double aircon = mGreenCarManager.getCrDatcAcnCompPwrConW() / 100.0;
      double heating = mGreenCarManager.getCrDatcPtcPwrConW() / 100.0;
      double electric = mGreenCarManager.getCrLdcPwrMonW() / 100.0;
      byte engine = (byte) mGreenCarManager.getCrMcuMotPwrAvnKw();

      synchronized (AbrpTransmitterService.this) {
        mConsumptionCollector.put(now, aircon + heating + electric + engine);
      }
    }
  }

  public class AbrpTransmitterBinder extends Binder {
    public AbrpTransmitterService getService() {
      return AbrpTransmitterService.this;
    }
  }

  private class AbrpUpdater implements Runnable {
    @Override
    public void run() {
      try {
        sLog.debug("AbrpUpdater, updating telemetry object");
        mJTlmObj.put(ABETTERROUTEPLANNER_JSON_TIME, System.currentTimeMillis() / 1000);
        mJTlmObj.put(ABETTERROUTEPLANNER_JSON_SOC, mGreenCarManager.getBatteryChargePersent());
        mJTlmObj.put(ABETTERROUTEPLANNER_JSON_SPEED, mCarInfoManager.getCarSpeed());
        mJTlmObj.put(ABETTERROUTEPLANNER_JSON_CHARGING, mGreenCarManager.getChargeStatus());
        mJTlmObj.put(ABETTERROUTEPLANNER_JSON_POWER, mAverageCollector.getAverage());
        mJTlmObj.put(ABETTERROUTEPLANNER_JSON_TEMPERATURE_EXT, mHvacManager.getAmbientTemperatureC());

        sLog.debug("AbrpUpdater, notify handlers about updated telemetry object");
        notifyHandlers(MESSAGE_TELEMETRY_UPDATED, mJTlmObj);

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
          sLog.warn(msg);
          return;
        }

        sLog.debug("AbrpUpdater, preparing url for telemetry update");
        StringBuilder url = new StringBuilder(ABETTERROUTEPLANNER_API_URL)
            .append(ABETTERROUTEPLANNER_URL_TOKEN).append("=").append(token)
            .append("&").append(ABETTERROUTEPLANNER_URL_API_KEY).append("=").append(ABETTERROUTEPLANNER_API_KEY)
            .append("&").append(ABETTERROUTEPLANNER_URL_TELEMETRY).append("=");
        try {
          url.append(URLEncoder.encode(mJTlmObj.toString(), "UTF-8"));
        } catch (UnsupportedEncodingException e) {
          sLog.error(e.getMessage(), e);
          return;
        }

        sLog.debug("AbrpUpdater, calling url for telemetry update");
        sendUpdate(url.toString());
      } catch (Exception e) {
        if (e instanceof SocketTimeoutException || e instanceof UnknownHostException || e instanceof ConnectException) {
          sLog.error(e.getLocalizedMessage());
        } else {
          sLog.error("error sending telemetry data to abrp", e);
        }
        notifyHandlers(MESSAGE_LAST_ERROR_ABRPSERVICE, e.getLocalizedMessage());
      } finally {
        sLog.debug("AbrpUpdater finished");
        sLog.debug(mScheduledExecutorService.toString());
      }
    }

    private void sendUpdate(String url) throws IOException, NoSuchAlgorithmException, KeyManagementException, JSONException {
      HttpsURLConnection connection = null;
      BufferedReader reader = null;
      try {
        connection = (HttpsURLConnection) new URL(url).openConnection();
        connection.setConnectTimeout((int) INTERVAL_SEND_UPDATE / 2 - 100);
        connection.setReadTimeout((int) INTERVAL_SEND_UPDATE  /  2 - 100);
        connection.setSSLSocketFactory(new TLSSocketFactory());
        connection.connect();

        InputStream stream = connection.getInputStream();
        reader = new BufferedReader(new InputStreamReader(stream));
        StringBuffer buffer = new StringBuffer();
        String line;

        while ((line = reader.readLine()) != null) {
          buffer.append(line);
        }
        String json = buffer.toString();
        JSONObject jsonObject = new JSONObject(json);
        String status = jsonObject.getString(ABETTERROUTEPLANNER_JSON_STATUS);
        if (!status.equals(ABETTERROUTEPLANNER_JSON_STATUS_OK)) {
          sLog.error("error sending telemetry data to abrp, returned api status: " + status);
        } else {
          notifyHandlers(MESSAGE_LAST_UPDATE_SENT, Utils.getTimestamp());
        }
      } finally {
        if (connection != null) {
          connection.disconnect();
        }
        if (reader != null) {
          reader.close();
        }
      }
    }
  }
}
