package g4rb4g3.at.abrptransmitter.gson.abrp;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Charger {

  @SerializedName("id")
  @Expose
  private Integer id;
  @SerializedName("lat")
  @Expose
  private Double lat;
  @SerializedName("lon")
  @Expose
  private Double lon;
  @SerializedName("url")
  @Expose
  private String url;
  @SerializedName("name")
  @Expose
  private String name;
  @SerializedName("region")
  @Expose
  private String region;
  @SerializedName("status")
  @Expose
  private String status;
  @SerializedName("address")
  @Expose
  private String address;
  @SerializedName("comment")
  @Expose
  private Object comment;
  @SerializedName("outlets")
  @Expose
  private List<Outlet> outlets = null;
  @SerializedName("locationid")
  @Expose
  private String locationid;
  @SerializedName("network_id")
  @Expose
  private Integer networkId;
  @SerializedName("network_icon")
  @Expose
  private Object networkIcon;
  @SerializedName("network_name")
  @Expose
  private String networkName;

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
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

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
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

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public String getAddress() {
    return address;
  }

  public void setAddress(String address) {
    this.address = address;
  }

  public Object getComment() {
    return comment;
  }

  public void setComment(Object comment) {
    this.comment = comment;
  }

  public List<Outlet> getOutlets() {
    return outlets;
  }

  public void setOutlets(List<Outlet> outlets) {
    this.outlets = outlets;
  }

  public String getLocationid() {
    return locationid;
  }

  public void setLocationid(String locationid) {
    this.locationid = locationid;
  }

  public Integer getNetworkId() {
    return networkId;
  }

  public void setNetworkId(Integer networkId) {
    this.networkId = networkId;
  }

  public Object getNetworkIcon() {
    return networkIcon;
  }

  public void setNetworkIcon(Object networkIcon) {
    this.networkIcon = networkIcon;
  }

  public String getNetworkName() {
    return networkName;
  }

  public void setNetworkName(String networkName) {
    this.networkName = networkName;
  }
}
