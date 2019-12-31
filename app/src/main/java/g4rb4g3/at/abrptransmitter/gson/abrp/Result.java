package g4rb4g3.at.abrptransmitter.gson.abrp;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Result {

  @SerializedName("car_model")
  @Expose
  private String carModel;
  @SerializedName("routes")
  @Expose
  private List<Route> routes = null;
  @SerializedName("path_indices")
  @Expose
  private PathIndices pathIndices;

  public String getCarModel() {
    return carModel;
  }

  public void setCarModel(String carModel) {
    this.carModel = carModel;
  }

  public List<Route> getRoutes() {
    return routes;
  }

  public void setRoutes(List<Route> routes) {
    this.routes = routes;
  }

  public PathIndices getPathIndices() {
    return pathIndices;
  }

  public void setPathIndices(PathIndices pathIndices) {
    this.pathIndices = pathIndices;
  }

}
