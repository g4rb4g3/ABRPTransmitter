package g4rb4g3.at.abrptransmitter.service;

import android.app.Notification;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Binder;
import android.os.IBinder;

import com.github.mikephil.charting.data.Entry;
import com.lge.ivi.greencar.GreenCarManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import g4rb4g3.at.abrptransmitter.Location;
import g4rb4g3.at.abrptransmitter.R;
import g4rb4g3.at.abrptransmitter.abrp.IRoutePlan;
import g4rb4g3.at.abrptransmitter.abrp.RoutePlan;
import g4rb4g3.at.abrptransmitter.gson.abrp.GsonRoutePlan;
import g4rb4g3.at.abrptransmitter.gson.abrp.PathIndices;
import g4rb4g3.at.abrptransmitter.gson.abrp.Route;
import g4rb4g3.at.abrptransmitter.gson.abrp.Step;

import static g4rb4g3.at.abrptransmitter.Constants.ACTION_GPS_CHANGED;
import static g4rb4g3.at.abrptransmitter.Constants.EXTRA_ALT;
import static g4rb4g3.at.abrptransmitter.Constants.EXTRA_LAT;
import static g4rb4g3.at.abrptransmitter.Constants.EXTRA_LON;
import static g4rb4g3.at.abrptransmitter.Constants.INTERVAL_SEND_UPDATE;
import static g4rb4g3.at.abrptransmitter.Constants.NOTIFICATION_ID_ABRPCONSUMPTIONSERVICE;
import static g4rb4g3.at.abrptransmitter.Constants.PREFERENCES_NAME;
import static g4rb4g3.at.abrptransmitter.Constants.PREFERENCES_TOKEN;

public class AbrpConsumptionService extends Service implements IRoutePlan {
  private static final Logger sLog = LoggerFactory.getLogger(AbrpConsumptionService.class.getSimpleName());
  private final IBinder mBinder = new AbrpConsumptionBinder();
  private SharedPreferences mSharedPreferences;
  private ArrayList<Entry> mEstimatedSocValues;
  private ArrayList<Entry> mHeightValues;
  private ArrayList<Entry> mRealSocValues;
  private ArrayList<Location> mRouteLocations;
  private GreenCarManager mGreenCarManager;
  private Location mCurrentLocation = null;
  private ScheduledExecutorService mScheduledExecutorService;
  private RoutePlan mRoutePlan;
  private ArrayList<IAbrpConsumptionService> mListeners = new ArrayList<>();
  private BroadcastReceiver mNaviGpsReceiver = new BroadcastReceiver() {
    @Override
    public void onReceive(Context context, Intent intent) {
      double lat = intent.getDoubleExtra(EXTRA_LAT, 0);
      double lon = intent.getDoubleExtra(EXTRA_LON, 0);
      double alt = intent.getDoubleExtra(EXTRA_ALT, 0);
      mCurrentLocation = new Location(lat, lon, alt);
    }
  };

  @Nullable
  @Override
  public IBinder onBind(Intent intent) {
    return mBinder;
  }

  @Override
  public void onCreate() {
    Notification notification = new NotificationCompat.Builder(this, null)
        .setContentTitle(getString(R.string.app_name))
        .setSmallIcon(R.mipmap.ic_launcher)
        .setPriority(NotificationCompat.PRIORITY_MAX)
        .build();
    startForeground(NOTIFICATION_ID_ABRPCONSUMPTIONSERVICE, notification);

    mSharedPreferences = getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
    registerReceiver(mNaviGpsReceiver, new IntentFilter(ACTION_GPS_CHANGED));
    mGreenCarManager = GreenCarManager.getInstance(getApplicationContext());
    mScheduledExecutorService = Executors.newScheduledThreadPool(1);
    mRoutePlan = RoutePlan.getInstance();
  }

  @Override
  public void onDestroy() {
    unregisterReceiver(mNaviGpsReceiver);
    mRoutePlan.removeListener(this);
    stopForeground(true);
  }

  @Override
  public int onStartCommand(Intent intent, int flags, int startId) {
    mRoutePlan.addListener(this);
    mRoutePlan.requestPlan(mSharedPreferences.getString(PREFERENCES_TOKEN, null));
    return START_NOT_STICKY;
  }

  @Override
  public void planReady(GsonRoutePlan route) {
    Route r = route.getResult().getRoutes().get(0); //TODO: can there be more then one planned route?
    PathIndices pi = route.getResult().getPathIndices();
    mEstimatedSocValues = new ArrayList<>();
    mHeightValues = new ArrayList<>();
    mRouteLocations = new ArrayList<>();
    mRealSocValues = new ArrayList<>();
    double totalDist = Math.ceil(r.getTotalDist() / 1000.0);
    for (Step step : r.getSteps()) {
      if (step.getPath() == null) {
        continue;
      }
      for (List<Double> path : step.getPath()) {
        double elevation = path.get(pi.getElevation());
        double soc = path.get(pi.getSocPerc());
        double remainingDist = path.get(pi.getRemainingDist());
        mHeightValues.add(new Entry((float) (totalDist - remainingDist), (float) elevation));
        mEstimatedSocValues.add(new Entry((float) (totalDist - remainingDist), (float) soc));
        mRouteLocations.add(new Location(path.get(pi.getLat()), path.get(pi.getLon()), elevation, totalDist - remainingDist));
      }
    }

    for (IAbrpConsumptionService i : mListeners) {
      i.setChartData(mHeightValues, mEstimatedSocValues, mRealSocValues);
    }
    mScheduledExecutorService.scheduleWithFixedDelay(new RealSocWatcher(mGreenCarManager), INTERVAL_SEND_UPDATE, INTERVAL_SEND_UPDATE, TimeUnit.MILLISECONDS);
  }

  @Override
  public void planFailed() {

  }

  public void addListener(IAbrpConsumptionService listener) {
    mListeners.add(listener);
  }

  public void removeListener(IAbrpConsumptionService listener) {
    mListeners.remove(listener);
  }

  private Location getClosestLocation(Location currentLocation) {
    double closest = Double.MAX_VALUE;
    Location closestLocation = null;
    for (Location l : mRouteLocations) {
      float distance = l.distanceTo(currentLocation);
      if (distance < closest) {
        closest = distance;
        closestLocation = l;
      }
    }
    sLog.info("closest distance: " + closest + " obj: [" + closestLocation.toString() + "]");
    return closestLocation;
  }

  public interface IAbrpConsumptionService {
    void setChartData(ArrayList<Entry> heightValues, ArrayList<Entry> estimatedSocValues, ArrayList<Entry> realSocValues);

    void updateChartData(ArrayList<Entry> realSocValues);
  }

  private class RealSocWatcher implements Runnable {
    private GreenCarManager mGreenCarManager;

    public RealSocWatcher(GreenCarManager greenCarManager) {
      this.mGreenCarManager = greenCarManager;
    }

    @Override
    public void run() {
      int soc = mGreenCarManager.getBatteryChargePersent();
      Location closest = getClosestLocation(mCurrentLocation);
      int x = (int) Math.floor(closest.getDistanceFromStart() + closest.getDistance());
      boolean addNew = true;
      for (Entry s : mRealSocValues) {
        if (s.getX() == x) {
          s.setY(soc);
          addNew = false;
          break;
        }
      }
      if (addNew) {
        mRealSocValues.add(new Entry(x, soc));
        if (mRealSocValues.size() == 1) {
          //add second value to get a line drawn
          mRealSocValues.add(new Entry(x + 1, soc));
        }
      }
      for (IAbrpConsumptionService i : mListeners) {
        i.updateChartData(mRealSocValues);
      }
    }
  }

  public class AbrpConsumptionBinder extends Binder {
    public AbrpConsumptionService getService() {
      return AbrpConsumptionService.this;
    }
  }
}
