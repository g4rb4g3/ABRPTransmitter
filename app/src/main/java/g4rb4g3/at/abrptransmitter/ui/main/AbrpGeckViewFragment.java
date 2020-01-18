package g4rb4g3.at.abrptransmitter.ui.main;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
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

import static com.lge.ivi.view.IviKeyEvent.KEYCODE_TUNE_DOWN;
import static com.lge.ivi.view.IviKeyEvent.KEYCODE_TUNE_PRESS;
import static com.lge.ivi.view.IviKeyEvent.KEYCODE_TUNE_UP;
import static g4rb4g3.at.abrptransmitter.Constants.ABETTERROUTEPLANNER_AUTH_ACCESS_TOKEN;
import static g4rb4g3.at.abrptransmitter.Constants.ABETTERROUTEPLANNER_AUTH_AUTH_CODE;
import static g4rb4g3.at.abrptransmitter.Constants.ABETTERROUTEPLANNER_AUTH_REDIRECT_URI;
import static g4rb4g3.at.abrptransmitter.Constants.ABETTERROUTEPLANNER_AUTH_URL;
import static g4rb4g3.at.abrptransmitter.Constants.ABETTERROUTEPLANNER_AUTH_URL_GET_TOKEN;
import static g4rb4g3.at.abrptransmitter.Constants.ABETTERROUTEPLANNER_URL;
import static g4rb4g3.at.abrptransmitter.Constants.ABETTERROUTEPLANNER_URL_NOMAP;
import static g4rb4g3.at.abrptransmitter.Constants.PREFERENCES_NAME;
import static g4rb4g3.at.abrptransmitter.Constants.PREFERENCES_NOMAP;
import static g4rb4g3.at.abrptransmitter.Constants.PREFERENCES_TOKEN;

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
  private WebExtension.Port mPort;

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

    GeckoRuntimeSettings.Builder builder = new GeckoRuntimeSettings.Builder()
        .configFilePath(""); //required to get rid of  java.lang.VerifyError: org/yaml/snakeyaml/introspector/PropertyUtils https://bugzilla.mozilla.org/show_bug.cgi?id=1567115
    mGeckoRuntime = GeckoRuntime.create(getContext(), builder.build());

    WebExtension.PortDelegate portDelegate = new WebExtension.PortDelegate() {
      @Override
      public void onPortMessage(final @NonNull Object message,
                                final @NonNull WebExtension.Port port) {
        sLog.info("PortDelegate", "Received message from WebExtension: "
            + message);
      }

      @Override
      public void onDisconnect(final @NonNull WebExtension.Port port) {
        // This port is not usable anymore.
        if (port == mPort) {
          mPort = null;
        }
      }
    };

    WebExtension.MessageDelegate messageDelegate = new WebExtension.MessageDelegate() {
      @Override
      @Nullable
      public void onConnect(final @NonNull WebExtension.Port port) {
        mPort = port;
        mPort.setDelegate(portDelegate);
      }
    };

    WebExtension extension = new WebExtension("resource://android/assets/abrp/", "browser", WebExtension.Flags.ALLOW_CONTENT_MESSAGING);
    extension.setMessageDelegate(messageDelegate, "browser");
    mGeckoRuntime.registerWebExtension(extension).exceptionally(exception -> {
      sLog.error("", exception);
      return null;
    });
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

    if (mGeckoSession == null || !mGeckoSession.isOpen()) {
      if(mGeckoSession == null) {
        mGeckoSession = new GeckoSession();
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

              Intent intent = new Intent();
              intent.setAction("com.hkmc.intent.action.ACTION_ROUTE_SEARCH");
              intent.putExtra("com.hkmc.navi.EXTRA_LATITUDE", lat);
              intent.putExtra("com.hkmc.navi.EXTRA_LONGITUDE", lon);
              intent.putExtra("com.hkmc.navi.EXTRA_KEYWORD", name);
              getContext().sendBroadcast(intent);

              intent = new Intent();
              intent.setComponent(new ComponentName("com.mnsoft.navi", "com.mnsoft.navi.NaviApp"));
              startActivity(intent);
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
          }
        });
      }
      
      mGeckoSession.open(mGeckoRuntime);
      mGeckoView.setSession(mGeckoSession);
      mGeckoSession.loadUri(getAbrpUrl());
    }

    return view;
  }

  @Override
  public void onResume() {
    super.onResume();
    mBtnRealodAbrp.setVisibility(View.VISIBLE);
    String token = getContext().getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE).getString(PREFERENCES_TOKEN, null);
    if (token == null || token.length() == 0) {
      mBtnGetAbrpToken.setVisibility(View.VISIBLE);
    }
  }

  @Override
  public void onPause() {
    super.onPause();
    mBtnRealodAbrp.setVisibility(View.INVISIBLE);
    mPbGeckoView.setVisibility(View.GONE);
    mBtnGetAbrpToken.setVisibility(View.GONE);
  }

  @Override
  public void onDestroyView() {
    super.onDestroyView();
    mGeckoView.releaseSession();
    mGeckoSession.close();
    mGeckoView = null;
  }

  public boolean onKeyEvent(int keycode) {
    if (mPort == null) {
      return false;
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
          return false;
      }
    } catch (JSONException ex) {
      throw new RuntimeException(ex);
    }
    mPort.postMessage(message);
    return true;
  }

  private String getAbrpUrl() {
    boolean nomap = getContext().getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE).getBoolean(PREFERENCES_NOMAP, false);
    String url = ABETTERROUTEPLANNER_URL;
    if (nomap) {
      url += ABETTERROUTEPLANNER_URL_NOMAP;
    }
    return url;
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
        sLog.error("error getting token", e);
      } finally {
        mGeckoSession.loadUri(getAbrpUrl());
      }
    }).start();
  }
}
