package g4rb4g3.at.abrptransmitter.ui.main;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;

import org.json.JSONException;
import org.json.JSONObject;
import org.mozilla.geckoview.AllowOrDeny;
import org.mozilla.geckoview.GeckoResult;
import org.mozilla.geckoview.GeckoRuntime;
import org.mozilla.geckoview.GeckoRuntimeSettings;
import org.mozilla.geckoview.GeckoSession;
import org.mozilla.geckoview.GeckoView;
import org.mozilla.geckoview.WebExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

import javax.net.ssl.HttpsURLConnection;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import g4rb4g3.at.abrptransmitter.R;
import g4rb4g3.at.abrptransmitter.TLSSocketFactory;
import g4rb4g3.at.abrptransmitter.Utils;
import g4rb4g3.at.abrptransmitter.service.AbrpTransmitterService;

import static com.lge.ivi.view.IviKeyEvent.KEYCODE_TUNE_DOWN;
import static com.lge.ivi.view.IviKeyEvent.KEYCODE_TUNE_PRESS;
import static com.lge.ivi.view.IviKeyEvent.KEYCODE_TUNE_UP;
import static g4rb4g3.at.abrptransmitter.Constants.ABETTERROUTEPLANNER_AUTH_ACCESS_TOKEN;
import static g4rb4g3.at.abrptransmitter.Constants.ABETTERROUTEPLANNER_AUTH_AUTH_CODE;
import static g4rb4g3.at.abrptransmitter.Constants.ABETTERROUTEPLANNER_AUTH_REDIRECT_URI;
import static g4rb4g3.at.abrptransmitter.Constants.ABETTERROUTEPLANNER_AUTH_URL;
import static g4rb4g3.at.abrptransmitter.Constants.ABETTERROUTEPLANNER_AUTH_URL_GET_TOKEN;
import static g4rb4g3.at.abrptransmitter.Constants.ABETTERROUTEPLANNER_URL;
import static g4rb4g3.at.abrptransmitter.Constants.MESSAGE_TELEMETRY_UPDATED;
import static g4rb4g3.at.abrptransmitter.Constants.PREFERENCES_ABRP_URL;
import static g4rb4g3.at.abrptransmitter.Constants.PREFERENCES_APPLY_CSS;
import static g4rb4g3.at.abrptransmitter.Constants.PREFERENCES_NAME;
import static g4rb4g3.at.abrptransmitter.Constants.PREFERENCES_TOKEN;
import static g4rb4g3.at.abrptransmitter.Constants.SYSTEM_PROPERTY_LGE_FW_VERSIOIN;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AbrpGeckViewFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AbrpGeckViewFragment extends Fragment {
  private static final Logger sLog = LoggerFactory.getLogger(AbrpGeckViewFragment.class.getSimpleName());

  GeckoView mGeckoView;
  GeckoSession mGeckoSession;
  GeckoRuntime mGeckoRuntime;
  Button mBtnRealodAbrp, mBtnGetAbrpToken;
  ProgressBar mPbGeckoView;
  private WebExtension.Port mWebExtensionPort;

  private SharedPreferences mSharedPreferences;

  private AbrpTransmitterService mService;
  private boolean mBound = false;
  private Handler mHandler = new Handler(Looper.getMainLooper()) {
    @Override
    public void handleMessage(final Message msg) {
      if (mWebExtensionPort == null) {
        return;
      }

      if (msg.what == MESSAGE_TELEMETRY_UPDATED) {
        JSONObject tlm = (JSONObject) msg.obj;
        mWebExtensionPort.postMessage(tlm);
      }
    }
  };

  private ServiceConnection mServiceConnection = new ServiceConnection() {
    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
      AbrpTransmitterService.AbrpTransmitterBinder binder = (AbrpTransmitterService.AbrpTransmitterBinder) service;
      mService = binder.getService();
      mBound = true;

      mService.registerHandler(mHandler);
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {
      mBound = false;
    }
  };

  private String mLgeFirmwareVersion;

  public AbrpGeckViewFragment() {
    // Required empty public constructor
  }

  /**
   * Use this factory method to create a new instance of
   * this fragment using the provided parameters.
   *
   * @return A new instance of fragment AbrpGeckViewFragment.
   */
  public static AbrpGeckViewFragment newInstance() {
    AbrpGeckViewFragment fragment = new AbrpGeckViewFragment();
    return fragment;
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    mSharedPreferences = getContext().getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
    mLgeFirmwareVersion = Utils.getSystemProperty(SYSTEM_PROPERTY_LGE_FW_VERSIOIN);

    GeckoRuntimeSettings.Builder builder = new GeckoRuntimeSettings.Builder()
            .remoteDebuggingEnabled(true);
    mGeckoRuntime = GeckoRuntime.create(getContext(), builder.build());

    registerWebExtension();
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_abrp_geck_view, container, false);

    mBtnRealodAbrp = getActivity().findViewById(R.id.btn_reload_abrp);
    mBtnRealodAbrp.setOnClickListener(v -> mGeckoSession.reload());
    mBtnGetAbrpToken = getActivity().findViewById(R.id.btn_get_abrp_token);
    mBtnGetAbrpToken.setOnClickListener(v -> mGeckoSession.loadUri(ABETTERROUTEPLANNER_AUTH_URL));
    mPbGeckoView = view.findViewById(R.id.pb_geckoview);
    mGeckoView = view.findViewById(R.id.gv_abrp);

    return view;
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);

    if (mGeckoSession == null || !mGeckoSession.isOpen()) {
      if (mGeckoSession == null) {
        mGeckoSession = new GeckoSession();
        mGeckoSession.setPermissionDelegate(new GeckoPermissionDelegate());
        mGeckoSession.setNavigationDelegate(new GeckoSession.NavigationDelegate() {
          @Nullable
          @Override
          public GeckoResult<AllowOrDeny> onLoadRequest(@NonNull GeckoSession session, @NonNull LoadRequest request) {
            if (request.uri.startsWith("geo:")) {
              String[] parts = request.uri.replace("geo:", "").replaceFirst("\\?.*\\(", ",").replace(")", "").split(",");
              String lat = parts[0];
              String lon = parts[1];
              String name;
              try {
                name = URLDecoder.decode(parts[2], "UTF-8");
              } catch (UnsupportedEncodingException e) {
                name = "";
              }

              Intent intent1 = new Intent();
              intent1.setComponent(new ComponentName("com.mnsoft.navi", "com.mnsoft.navi.NaviApp"));
              startActivity(intent1);

              final String finalName = name;
              new Handler().postDelayed(() -> {
                Intent intent = new Intent();
                if("XX.EUR.SOP.00.191209".equals(mLgeFirmwareVersion)) {
                  intent.setAction("com.hkmc.intent.action.ACTION_SHOW_MAP");
                } else {
                  intent.setAction("com.hkmc.intent.action.ACTION_ROUTE_SEARCH");
                }
                intent.putExtra("com.hkmc.navi.EXTRA_LATITUDE", lat);
                intent.putExtra("com.hkmc.navi.EXTRA_LONGITUDE", lon);
                intent.putExtra("com.hkmc.navi.EXTRA_KEYWORD", finalName);
                getContext().sendBroadcast(intent);
              }, 1000);
            } else if (request.uri.startsWith(ABETTERROUTEPLANNER_AUTH_REDIRECT_URI)) {
              if (request.uri.contains(ABETTERROUTEPLANNER_AUTH_AUTH_CODE)) {
                String authCode = request.uri.substring(request.uri.indexOf(ABETTERROUTEPLANNER_AUTH_AUTH_CODE) + ABETTERROUTEPLANNER_AUTH_AUTH_CODE.length() + 1);
                if (authCode.contains("&")) {
                  authCode = authCode.substring(0, authCode.indexOf("&"));
                }
                loadToken(authCode);
              }
              return GeckoResult.DENY;
            }
            return null;
          }
        });


        mGeckoSession.setProgressDelegate(new GeckoSession.ProgressDelegate() {
          @Override
          public void onProgressChange(@NonNull GeckoSession geckoSession, int progress) {
            mPbGeckoView.setProgress(progress);
            if (progress > 0 && progress < 100) {
              mPbGeckoView.setVisibility(View.VISIBLE);
            } else {
              mPbGeckoView.setVisibility(View.GONE);
            }

            if (progress == 100) {
              setCss();
            }
          }
        });
      }

      mGeckoSession.open(mGeckoRuntime);
      mGeckoView.setSession(mGeckoSession);
      mGeckoSession.loadUri(getAbrpUrl());
    }
  }

  @Override
  public void onResume() {
    super.onResume();
    mBtnRealodAbrp.setVisibility(View.VISIBLE);
    String token = mSharedPreferences.getString(PREFERENCES_TOKEN, null);
    if (token == null || token.length() == 0) {
      mBtnGetAbrpToken.setVisibility(View.VISIBLE);
    }

    setCss();

    Intent intent = new Intent(getContext(), AbrpTransmitterService.class);
    getActivity().bindService(intent, mServiceConnection, Context.BIND_AUTO_CREATE);
  }

  @Override
  public void onPause() {
    super.onPause();
    mBtnRealodAbrp.setVisibility(View.INVISIBLE);
    mPbGeckoView.setVisibility(View.GONE);
    mBtnGetAbrpToken.setVisibility(View.GONE);

    if (mBound) {
      mService.unregisterHandler(mHandler);
      getActivity().unbindService(mServiceConnection);
    }
  }

  @Override
  public void onDestroyView() {
    super.onDestroyView();
    mGeckoView.releaseSession();
    mGeckoSession.close();
    mGeckoView = null;
  }

  public void onKeyEvent(int keycode, int action) {
    if (action != KeyEvent.ACTION_DOWN) {
      return;
    }
    if (mWebExtensionPort == null) {
      return;
    }
    JSONObject message = new JSONObject();
    try {
      switch (keycode) {
        case KEYCODE_TUNE_UP:
          message.put("zoomIn", true);
          break;
        case KEYCODE_TUNE_DOWN:
          message.put("zoomOut", true);
          break;
        case KEYCODE_TUNE_PRESS:
          message.put("toggleNight", true);
          break;
        default:
          return;
      }
    } catch (JSONException ex) {
      sLog.error("error building onKeyEvent json", ex);
    }
    mWebExtensionPort.postMessage(message);
  }

  private String getAbrpUrl() {
    return getContext().getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE).getString(PREFERENCES_ABRP_URL, ABETTERROUTEPLANNER_URL);
  }

  private void loadToken(String authCode) {
    new Thread(() -> {
      try {
        URL url = new URL(ABETTERROUTEPLANNER_AUTH_URL_GET_TOKEN + authCode);
        HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
        connection.setSSLSocketFactory(new TLSSocketFactory());
        connection.connect();
        InputStream stream = connection.getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
        StringBuffer buffer = new StringBuffer();
        String line;

        while ((line = reader.readLine()) != null) {
          buffer.append(line);
        }
        String json = buffer.toString();
        JSONObject jsonObject = new JSONObject(json);

        SharedPreferences.Editor sped = getContext().getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE).edit();
        sped.putString(PREFERENCES_TOKEN, jsonObject.getString(ABETTERROUTEPLANNER_AUTH_ACCESS_TOKEN));
        sped.commit();

        getActivity().runOnUiThread(() -> mBtnGetAbrpToken.setVisibility(View.GONE));
      } catch (IOException | JSONException | NoSuchAlgorithmException | KeyManagementException e) {
        sLog.error("error receiving token from abrp website", e);
      } finally {
        mGeckoSession.loadUri(getAbrpUrl());
      }
    }).start();
  }

  private void registerWebExtension() {
    if (mWebExtensionPort != null) {
      return;
    }

    WebExtension.MessageDelegate messageDelegate = new WebExtension.MessageDelegate() {
      @Override
      @Nullable
      public void onConnect(final @NonNull WebExtension.Port port) {
        mWebExtensionPort = port;
        mWebExtensionPort.setDelegate(new WebExtension.PortDelegate() {
          @Override
          public void onPortMessage(@NonNull Object message, @NonNull WebExtension.Port port) {
            sLog.error("Received error message from WebExtension: " + message);
          }

          @NonNull
          @Override
          public void onDisconnect(@NonNull WebExtension.Port port) {
            // This port is not usable anymore.
            if (port == mWebExtensionPort) {
              mWebExtensionPort = null;
            }
          }
        });

        setCss();
      }
    };

    mGeckoRuntime.getWebExtensionController()
        .installBuiltIn("resource://android/assets/abrp/")
        .accept(
            extension -> extension.setMessageDelegate(messageDelegate, "browser"),
            e -> Log.e("MessageDelegate", "Error registering WebExtension", e)
        );
  }

  private void setCss() {
    if (mWebExtensionPort != null) {
      JSONObject message = new JSONObject();
      try {
        if (mSharedPreferences.getBoolean(PREFERENCES_APPLY_CSS, false)) {
          message.put("setCss", true);
        } else {
          message.put("removeCss", true);
        }
      } catch (JSONException ex) {
        sLog.error("error building setCss json", ex);
      }
      mWebExtensionPort.postMessage(message);
    }
  }

  private class GeckoPermissionDelegate implements GeckoSession.PermissionDelegate {
    @Override
    public void onContentPermissionRequest(final GeckoSession session,
                                           final String uri,
                                           final int type, final Callback callback) {
      switch (type) {
        case PERMISSION_GEOLOCATION:
          callback.reject();
          break;
        default:
          callback.grant();
          break;
      }
    }
  }
}
