package g4rb4g3.at.abrptransmitter.gson.abrp;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Route {

  @SerializedName("steps")
  @Expose
  private List<Step> steps = null;
  @SerializedName("total_dist")
  @Expose
  private Integer totalDist;
  @SerializedName("average_consumption")
  @Expose
  private Double averageConsumption;
  @SerializedName("total_drive_duration")
  @Expose
  private Integer totalDriveDuration;
  @SerializedName("total_charge_duration")
  @Expose
  private Integer totalChargeDuration;

  public List<Step> getSteps() {
    return steps;
  }

  public void setSteps(List<Step> steps) {
    this.steps = steps;
  }

  public Integer getTotalDist() {
    return totalDist;
  }

  public void setTotalDist(Integer totalDist) {
    this.totalDist = totalDist;
  }

  public Double getAverageConsumption() {
    return averageConsumption;
  }

  public void setAverageConsumption(Double averageConsumption) {
    this.averageConsumption = averageConsumption;
  }

  public Integer getTotalDriveDuration() {
    return totalDriveDuration;
  }

  public void setTotalDriveDuration(Integer totalDriveDuration) {
    this.totalDriveDuration = totalDriveDuration;
  }

  public Integer getTotalChargeDuration() {
    return totalChargeDuration;
  }

  public void setTotalChargeDuration(Integer totalChargeDuration) {
    this.totalChargeDuration = totalChargeDuration;
  }

}
