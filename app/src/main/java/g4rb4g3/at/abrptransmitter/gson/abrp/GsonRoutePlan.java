package g4rb4g3.at.abrptransmitter.gson.abrp;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GsonRoutePlan {

  @SerializedName("status")
  @Expose
  private String status;
  @SerializedName("metadata")
  @Expose
  private Metadata metadata;
  @SerializedName("result")
  @Expose
  private Result result;

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public Metadata getMetadata() {
    return metadata;
  }

  public void setMetadata(Metadata metadata) {
    this.metadata = metadata;
  }

  public Result getResult() {
    return result;
  }

  public void setResult(Result result) {
    this.result = result;
  }

}

