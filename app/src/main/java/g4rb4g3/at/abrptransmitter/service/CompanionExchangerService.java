package g4rb4g3.at.abrptransmitter.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;

import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Iterator;

import androidx.annotation.Nullable;

import static g4rb4g3.at.abrptransmitter.Constants.ABETTERROUTEPLANNER_URL_TOKEN;
import static g4rb4g3.at.abrptransmitter.Constants.COMPANION_EXCHANGE_PORT;
import static g4rb4g3.at.abrptransmitter.Constants.PREFERENCES_AUTOSTART_COMPANION;
import static g4rb4g3.at.abrptransmitter.Constants.PREFERENCES_NAME;
import static g4rb4g3.at.abrptransmitter.Constants.PREFERENCES_TOKEN;

public class CompanionExchangerService extends Service {
  private static final Logger sLog = LoggerFactory.getLogger(CompanionExchangerService.class.getSimpleName());
  private SharedPreferences mSharedPreferences;

  private volatile boolean mStart = false;
  private ServerSocket mServerSocket;

  private Runnable mRunnable = new Runnable() {
    @Override
    public void run() {
      try {
        mServerSocket = new ServerSocket(COMPANION_EXCHANGE_PORT);
        while (mStart) {
          Socket socket = mServerSocket.accept();
          BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

          String line;
          while ((line = in.readLine()) != null && mStart) {
            try {
              JSONObject jsonObject = new JSONObject(line);
              for (Iterator<String> iterator = jsonObject.keys(); iterator.hasNext(); ) {
                String key = iterator.next();
                switch (key) {
                  case ABETTERROUTEPLANNER_URL_TOKEN:
                    String token = jsonObject.getString(key);
                    SharedPreferences.Editor sped = mSharedPreferences.edit();
                    sped.putString(PREFERENCES_TOKEN, token);
                    sped.commit();
                    break;
                  default:
                    sLog.debug("unknown json key: " + key + " with value: " + jsonObject.get(key));
                    break;
                }
              }
            } catch (JSONException e) {
              sLog.debug("skipping non json message [" + line + "]", e);
            }
          }
          socket.close();
        }
        mServerSocket.close();
      } catch (IOException e) {
        sLog.error("CompanionDataExchanger.run()", e);
      }
    }
  };


  private SharedPreferences.OnSharedPreferenceChangeListener mOnSharedPreferenceChangeListener = new SharedPreferences.OnSharedPreferenceChangeListener() {
    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
      if (PREFERENCES_AUTOSTART_COMPANION.equals(key)) {
        mStart = mSharedPreferences.getBoolean(PREFERENCES_AUTOSTART_COMPANION, false);
        if (mStart) {
          start();
        } else {
          stop();
        }
      }
    }
  };

  @Nullable
  @Override
  public IBinder onBind(Intent intent) {
    return null;
  }

  @Override
  public void onCreate() {
    mSharedPreferences = getApplicationContext().getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);

    mStart = mSharedPreferences.getBoolean(PREFERENCES_AUTOSTART_COMPANION, false);
    if (mStart) {
      start();
    }

    mSharedPreferences.registerOnSharedPreferenceChangeListener(mOnSharedPreferenceChangeListener);
  }

  @Override
  public void onDestroy() {
    stop();
    mSharedPreferences.unregisterOnSharedPreferenceChangeListener(mOnSharedPreferenceChangeListener);
  }

  private void start() {
    Thread thread = new Thread(mRunnable);
    thread.start();
  }

  private void stop() {
    mStart = false;
    try {
      mServerSocket.close();
    } catch (IOException e) {
      sLog.error("CompanionDataExchanger.stop()", e);
    }
  }
}
