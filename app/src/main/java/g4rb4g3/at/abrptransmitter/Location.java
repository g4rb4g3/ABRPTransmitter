package g4rb4g3.at.abrptransmitter;

public class Location extends android.location.Location {
  private double distanceFromStart;
  private float distance;

  public Location(double lat, double lon, double alt) {
    super("Hyundai");
    setLatitude(lat);
    setLongitude(lon);
    setAltitude(alt);
  }

  public Location(double lat, double lon, double alt, double distanceFromStart) {
    super("Hyundai");
    setLatitude(lat);
    setLongitude(lon);
    setAltitude(alt);
    this.distanceFromStart = distanceFromStart;
  }

  public String toString() {
    return "lat: " + getLatitude() + " lon: " + getLongitude() + " alt: " + getAltitude() + " distanceFromStart: " + this.distanceFromStart;
  }

  @Override
  public float distanceTo(android.location.Location dest) {
    this.distance = super.distanceTo(dest);
    return this.distance;
  }

  public float getDistance() {
    return distance;
  }

  public double getDistanceFromStart() {
    return distanceFromStart;
  }
}
