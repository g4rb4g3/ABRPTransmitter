package g4rb4g3.at.abrptransmitter;

import java.net.URLEncoder;

public class Constants {

  public static final String ABETTERROUTEPLANNER_URL = "https://abetterrouteplanner.com/";
  public static final String ABETTERROUTEPLANNER_URL_NOMAP = "?nomap=1";

  public static final String ABETTERROUTEPLANNER_AUTH_URL = "https://abetterrouteplanner.com/oauth/auth?client_id=" + ABETTERROUTEPLANNER_CLIENTID + "&response_type=code&redirect_uri=" + URLEncoder.encode(ABETTERROUTEPLANNER_AUTH_REDIRECT_URI) + "&scope=send_telemetry";
  public static final String ABETTERROUTEPLANNER_AUTH_AUTH_CODE = "code";
  public static final String ABETTERROUTEPLANNER_AUTH_ACCESS_TOKEN = "access_token";
  public static final String ABETTERROUTEPLANNER_AUTH_URL_GET_TOKEN = "https://abetterrouteplanner.com/oauth/token?client_id=" + ABETTERROUTEPLANNER_CLIENTID + "&client_secret=" + ABETTERROUTEPLANNER_API_KEY + "&redirect_uri="  + URLEncoder.encode(ABETTERROUTEPLANNER_AUTH_REDIRECT_URI) + "&code=";

  public static final String ABETTERROUTEPLANNER_API_URL = "https://api.iternio.com/1/tlm/send?";
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
  public static final String ABETTERROUTEPLANNER_JSON_STATUS = "status";
  public static final String ABETTERROUTEPLANNER_JSON_STATUS_OK = "ok";

  public static final String EXTRA_LAT = "com.hkmc.telematics.gis.extra.LAT";
  public static final String EXTRA_LON = "com.hkmc.telematics.gis.extra.LON";
  public static final String EXTRA_ALT = "com.hkmc.telematics.gis.extra.ALT";

  public static final long INTERVAL_SEND_UPDATE = 5000L;
  public static final long INTERVAL_AVERAGE_COLLECTOR = 500L;

  public static final String PREFERENCES_NAME = "preferences";
  public static final String PREFERENCES_TOKEN = "abrp_token";
  public static final String PREFERENCES_TRANSMIT_DATA = "transmit_data";
  public static final String PREFERENCES_NOMAP = "nomap";
  public static final String PREFERENCES_APPLY_CSS = "applyCss";
  public static final String PREFERENCES_DISABLE_TAB_SWIPE = "disableTabSwipe";
  public static final String PREFERENCES_LOG_LEVEL = "logLevel";

  public static final int MESSAGE_CONNECTIVITY_CHANGED = 1;
  public static final int MESSAGE_LAST_UPDATE_SENT = 2;
  public static final int MESSAGE_LAST_ERROR_ABRPSERVICE = 3;
  public static final int MESSAGE_TELEMETRY_UPDATED = 4;

  public static final int NOTIFICATION_ID_ABRPTRANSMITTERSERVICE = 1;

  public static final String ABRPTRANSMITTER_RELEASE_URL = "https://api.github.com/repos/g4rb4g3/abrptransmitter/releases";
  public static final String ABRPTRANSMITTER_APK_NAME = "app-debug.apk";
}
