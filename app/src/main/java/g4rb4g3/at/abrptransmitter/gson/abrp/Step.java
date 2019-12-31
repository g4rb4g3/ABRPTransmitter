package g4rb4g3.at.abrptransmitter.gson.abrp;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Step {

  @SerializedName("id")
  @Expose
  private int id;
  @SerializedName("lat")
  @Expose
  private Double lat;
  @SerializedName("lon")
  @Expose
  private Double lon;
  @SerializedName("name")
  @Expose
  private String name;
  @SerializedName("region")
  @Expose
  private String region;
  @SerializedName("wp_type")
  @Expose
  private int wpType;
  @SerializedName("max_speed")
  @Expose
  private Double maxSpeed;
  @SerializedName("drive_dist")
  @Expose
  private int driveDist;
  @SerializedName("is_charger")
  @Expose
  private Boolean isCharger;
  @SerializedName("is_station")
  @Expose
  private Boolean isStation;
  @SerializedName("utc_offset")
  @Expose
  private int utcOffset;
  @SerializedName("is_waypoint")
  @Expose
  private Boolean isWaypoint;
  @SerializedName("arrival_dist")
  @Expose
  private int arrivalDist;
  @SerializedName("arrival_perc")
  @Expose
  private int arrivalPerc;
  @SerializedName("charger_type")
  @Expose
  private String chargerType;
  @SerializedName("is_mod_speed")
  @Expose
  private Boolean isModSpeed;
  @SerializedName("waypoint_idx")
  @Expose
  private int waypointIdx;
  @SerializedName("is_valid_step")
  @Expose
  private Boolean isValidStep;
  @SerializedName("wait_duration")
  @Expose
  private int waitDuration;
  @SerializedName("departure_dist")
  @Expose
  private int departureDist;
  @SerializedName("departure_perc")
  @Expose
  private int departurePerc;
  @SerializedName("drive_duration")
  @Expose
  private int driveDuration;
  @SerializedName("is_destcharger")
  @Expose
  private Boolean isDestcharger;
  @SerializedName("is_end_station")
  @Expose
  private Boolean isEndStation;
  @SerializedName("is_new_waypoint")
  @Expose
  private Boolean isNewWaypoint;
  @SerializedName("arrival_duration")
  @Expose
  private int arrivalDuration;
  @SerializedName("departure_duration")
  @Expose
  private int departureDuration;
  @SerializedName("is_amenity_charger")
  @Expose
  private Boolean isAmenityCharger;
  @SerializedName("path")
  @Expose
  private List<List<Double>> path = null;
  @SerializedName("charger")
  @Expose
  private Charger charger;
  @SerializedName("charge_cost")
  @Expose
  private double chargeCost;
  @SerializedName("charge_profile")
  @Expose
  private List<List<Integer>> chargeProfile = null;
  @SerializedName("charge_duration")
  @Expose
  private int chargeDuration;
  @SerializedName("charge_cost_currency")
  @Expose
  private String chargeCostCurrency;

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public Double getLat() {
    return lat;
  }

  public void setLat(Double lat) {
    this.lat = lat;
  }

  public Double getLon() {
    return lon;
  }

  public void setLon(Double lon) {
    this.lon = lon;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getRegion() {
    return region;
  }

  public void setRegion(String region) {
    this.region = region;
  }

  public int getWpType() {
    return wpType;
  }

  public void setWpType(int wpType) {
    this.wpType = wpType;
  }

  public Double getMaxSpeed() {
    return maxSpeed;
  }

  public void setMaxSpeed(Double maxSpeed) {
    this.maxSpeed = maxSpeed;
  }

  public int getDriveDist() {
    return driveDist;
  }

  public void setDriveDist(int driveDist) {
    this.driveDist = driveDist;
  }

  public Boolean getIsCharger() {
    return isCharger;
  }

  public void setIsCharger(Boolean isCharger) {
    this.isCharger = isCharger;
  }

  public Boolean getIsStation() {
    return isStation;
  }

  public void setIsStation(Boolean isStation) {
    this.isStation = isStation;
  }

  public int getUtcOffset() {
    return utcOffset;
  }

  public void setUtcOffset(int utcOffset) {
    this.utcOffset = utcOffset;
  }

  public Boolean getIsWaypoint() {
    return isWaypoint;
  }

  public void setIsWaypoint(Boolean isWaypoint) {
    this.isWaypoint = isWaypoint;
  }

  public int getArrivalDist() {
    return arrivalDist;
  }

  public void setArrivalDist(int arrivalDist) {
    this.arrivalDist = arrivalDist;
  }

  public int getArrivalPerc() {
    return arrivalPerc;
  }

  public void setArrivalPerc(int arrivalPerc) {
    this.arrivalPerc = arrivalPerc;
  }

  public String getChargerType() {
    return chargerType;
  }

  public void setChargerType(String chargerType) {
    this.chargerType = chargerType;
  }

  public Boolean getIsModSpeed() {
    return isModSpeed;
  }

  public void setIsModSpeed(Boolean isModSpeed) {
    this.isModSpeed = isModSpeed;
  }

  public int getWaypointIdx() {
    return waypointIdx;
  }

  public void setWaypointIdx(int waypointIdx) {
    this.waypointIdx = waypointIdx;
  }

  public Boolean getIsValidStep() {
    return isValidStep;
  }

  public void setIsValidStep(Boolean isValidStep) {
    this.isValidStep = isValidStep;
  }

  public int getWaitDuration() {
    return waitDuration;
  }

  public void setWaitDuration(int waitDuration) {
    this.waitDuration = waitDuration;
  }

  public int getDepartureDist() {
    return departureDist;
  }

  public void setDepartureDist(int departureDist) {
    this.departureDist = departureDist;
  }

  public int getDeparturePerc() {
    return departurePerc;
  }

  public void setDeparturePerc(int departurePerc) {
    this.departurePerc = departurePerc;
  }

  public int getDriveDuration() {
    return driveDuration;
  }

  public void setDriveDuration(int driveDuration) {
    this.driveDuration = driveDuration;
  }

  public Boolean getIsDestcharger() {
    return isDestcharger;
  }

  public void setIsDestcharger(Boolean isDestcharger) {
    this.isDestcharger = isDestcharger;
  }

  public Boolean getIsEndStation() {
    return isEndStation;
  }

  public void setIsEndStation(Boolean isEndStation) {
    this.isEndStation = isEndStation;
  }

  public Boolean getIsNewWaypoint() {
    return isNewWaypoint;
  }

  public void setIsNewWaypoint(Boolean isNewWaypoint) {
    this.isNewWaypoint = isNewWaypoint;
  }

  public int getArrivalDuration() {
    return arrivalDuration;
  }

  public void setArrivalDuration(int arrivalDuration) {
    this.arrivalDuration = arrivalDuration;
  }

  public int getDepartureDuration() {
    return departureDuration;
  }

  public void setDepartureDuration(int departureDuration) {
    this.departureDuration = departureDuration;
  }

  public Boolean getIsAmenityCharger() {
    return isAmenityCharger;
  }

  public void setIsAmenityCharger(Boolean isAmenityCharger) {
    this.isAmenityCharger = isAmenityCharger;
  }

  public List<List<Double>> getPath() {
    return path;
  }

  public void setPath(List<List<Double>> path) {
    this.path = path;
  }

  public Charger getCharger() {
    return charger;
  }

  public void setCharger(Charger charger) {
    this.charger = charger;
  }

  public double getChargeCost() {
    return chargeCost;
  }

  public void setChargeCost(int chargeCost) {
    this.chargeCost = chargeCost;
  }

  public List<List<Integer>> getChargeProfile() {
    return chargeProfile;
  }

  public void setChargeProfile(List<List<Integer>> chargeProfile) {
    this.chargeProfile = chargeProfile;
  }

  public int getChargeDuration() {
    return chargeDuration;
  }

  public void setChargeDuration(int chargeDuration) {
    this.chargeDuration = chargeDuration;
  }

  public String getChargeCostCurrency() {
    return chargeCostCurrency;
  }

  public void setChargeCostCurrency(String chargeCostCurrency) {
    this.chargeCostCurrency = chargeCostCurrency;
  }
}
