package g4rb4g3.at.abrptransmitter;

public class Location {
  private double lat, lon, alt, distanceFromStart, distance;

  public Location(double lat, double lon, double alt) {
    this.lat = lat;
    this.lon = lon;
    this.alt = alt;
  }

  public Location(double lat, double lon, double alt, double distanceFromStart) {
    this.lat = lat;
    this.lon = lon;
    this.alt = alt;
    this.distanceFromStart = distanceFromStart;
  }

  public Location calcDistance(Location location) {
    final int R = 6371; // Radius of the earth
    double latDistance = Math.toRadians(location.lat - this.lat);
    double lonDistance = Math.toRadians(location.lon - this.lon);
    double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
        + Math.cos(Math.toRadians(location.lat)) * Math.cos(Math.toRadians(this.lat))
        * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
    double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
    this.distance = R * c / 1000;

    double height = location.alt - this.alt;

    this.distance = Math.pow(distance, 2) + Math.pow(height, 2);

    this.distance = Math.sqrt(distance);
    return this;
  }

  public String toString() {
    return "lat: " + this.lat + " lon: " + this.lon + " alt: " + this.alt + " distanceFromStart: " + this.distanceFromStart;
  }

  public double getDistance() {
    return distance;
  }

  public double getDistanceFromStart() {
    return distanceFromStart;
  }
}
