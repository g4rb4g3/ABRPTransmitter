package g4rb4g3.at.abrptransmitter.gson.abrp;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PathIndices {

  @SerializedName("lat")
  @Expose
  private Integer lat;
  @SerializedName("lon")
  @Expose
  private Integer lon;
  @SerializedName("soc_perc")
  @Expose
  private Integer socPerc;
  @SerializedName("cons_per_km")
  @Expose
  private Integer consPerKm;
  @SerializedName("speed")
  @Expose
  private Integer speed;
  @SerializedName("remaining_time")
  @Expose
  private Integer remainingTime;
  @SerializedName("remaining_dist")
  @Expose
  private Integer remainingDist;
  @SerializedName("instruction")
  @Expose
  private Integer instruction;
  @SerializedName("speed_limit")
  @Expose
  private Integer speedLimit;
  @SerializedName("elevation")
  @Expose
  private Integer elevation;
  @SerializedName("path_distance")
  @Expose
  private Integer pathDistance;

  public Integer getLat() {
    return lat;
  }

  public void setLat(Integer lat) {
    this.lat = lat;
  }

  public Integer getLon() {
    return lon;
  }

  public void setLon(Integer lon) {
    this.lon = lon;
  }

  public Integer getSocPerc() {
    return socPerc;
  }

  public void setSocPerc(Integer socPerc) {
    this.socPerc = socPerc;
  }

  public Integer getConsPerKm() {
    return consPerKm;
  }

  public void setConsPerKm(Integer consPerKm) {
    this.consPerKm = consPerKm;
  }

  public Integer getSpeed() {
    return speed;
  }

  public void setSpeed(Integer speed) {
    this.speed = speed;
  }

  public Integer getRemainingTime() {
    return remainingTime;
  }

  public void setRemainingTime(Integer remainingTime) {
    this.remainingTime = remainingTime;
  }

  public Integer getRemainingDist() {
    return remainingDist;
  }

  public void setRemainingDist(Integer remainingDist) {
    this.remainingDist = remainingDist;
  }

  public Integer getInstruction() {
    return instruction;
  }

  public void setInstruction(Integer instruction) {
    this.instruction = instruction;
  }

  public Integer getSpeedLimit() {
    return speedLimit;
  }

  public void setSpeedLimit(Integer speedLimit) {
    this.speedLimit = speedLimit;
  }

  public Integer getElevation() {
    return elevation;
  }

  public void setElevation(Integer elevation) {
    this.elevation = elevation;
  }

  public Integer getPathDistance() {
    return pathDistance;
  }

  public void setPathDistance(Integer pathDistance) {
    this.pathDistance = pathDistance;
  }

}
