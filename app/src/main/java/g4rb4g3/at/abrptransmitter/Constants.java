package g4rb4g3.at.abrptransmitter;

public class Constants {

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

  public static final String EXTRA_LAT = "com.hkmc.telematics.gis.extra.LAT";
  public static final String EXTRA_LON = "com.hkmc.telematics.gis.extra.LON";
  public static final String EXTRA_ALT = "com.hkmc.telematics.gis.extra.ALT";

  public static final long SEND_UPDATE_INTERVAL = 5000L;

  public static final String PREFERENCES_NAME = "preferences";
  public static final String PREFERENCES_TOKEN = "abrp_token";
  public static final String PREFERENCES_TRANSMIT_DATA = "transmit_data";
  public static final String PREFERENCES_AUTOSTART_COMPANION = "autostart_companion";

  public static final int COMPANION_EXCHANGE_PORT = 6942;
}
