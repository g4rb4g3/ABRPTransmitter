package g4rb4g3.at.abrptransmitter.abrp;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.internal.bind.TypeAdapters;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import g4rb4g3.at.abrptransmitter.gson.abrp.DoubleTypeAdapter;
import g4rb4g3.at.abrptransmitter.gson.abrp.GsonRoutePlan;

import static g4rb4g3.at.abrptransmitter.Constants.ABETTERROUTEPLANNER_API_KEY;
import static g4rb4g3.at.abrptransmitter.Constants.ABETTERROUTEPLANNER_PLAN_URL;
import static g4rb4g3.at.abrptransmitter.Constants.ABETTERROUTEPLANNER_URL_API_KEY;
import static g4rb4g3.at.abrptransmitter.Constants.ABETTERROUTEPLANNER_URL_TOKEN;

public class RoutePlan {
  private static AsyncHttpClient asyncHttpClient;
  private static RoutePlan instance;
  private List<IRoutePlan> listeners = new ArrayList<>();
  private Gson gson;

  private RoutePlan() {
    asyncHttpClient = new AsyncHttpClient(true, 80, 443);
    asyncHttpClient.setTimeout(5000);

    GsonBuilder builder = new GsonBuilder();
    builder.registerTypeAdapterFactory(TypeAdapters.newFactory(double.class, Double.class, new DoubleTypeAdapter()));
    gson = builder.create();
  }

  public static RoutePlan getInstance() {
    if (instance == null) {
      instance = new RoutePlan();
    }
    return instance;
  }

  public void addListener(IRoutePlan listener) {
    this.listeners.add(listener);
  }

  public void removeListener(IRoutePlan listener) {
    this.listeners.remove(listener);
  }

  private void parsePlanResponse(byte[] responseBody) {
    try {
      GsonRoutePlan gsonRoute = gson.fromJson(new String(responseBody), GsonRoutePlan.class);
      for (IRoutePlan listener : listeners) {
        listener.planReady(gsonRoute);
      }
    } catch (Exception e) {
      Log.d("ABRPTransmitter", "Failed to parse JSON: " + e.toString());
      for (IRoutePlan listener : listeners) {
        listener.planFailed();
      }
    }
  }

  public void requestPlan(String userToken) {
    StringBuilder url = new StringBuilder(ABETTERROUTEPLANNER_PLAN_URL)
        .append(ABETTERROUTEPLANNER_URL_TOKEN)
        .append("=").append(userToken)
        .append("&")
        .append(ABETTERROUTEPLANNER_URL_API_KEY)
        .append("=")
        .append(ABETTERROUTEPLANNER_API_KEY);
    Log.d("ABRPTransmitter", url.toString());
    asyncHttpClient.get(url.toString(), new AsyncHttpResponseHandler() {

      @Override
      public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
        parsePlanResponse(responseBody);
      }

      @Override
      public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
        Log.e("ABRPTransmitter", String.valueOf(statusCode), error);
        for (IRoutePlan listener : listeners) {
          listener.planFailed();
        }
      }

      @Override
      public boolean getUseSynchronousMode() {
        return false;
      }

    });
  }
}
