package g4rb4g3.at.abrptransmitter.gson.abrp;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Metadata {

  @SerializedName("plan_time")
  @Expose
  private Double planTime;
  @SerializedName("plan_uuid")
  @Expose
  private String planUuid;

  public Double getPlanTime() {
    return planTime;
  }

  public void setPlanTime(Double planTime) {
    this.planTime = planTime;
  }

  public String getPlanUuid() {
    return planUuid;
  }

  public void setPlanUuid(String planUuid) {
    this.planUuid = planUuid;
  }

}
