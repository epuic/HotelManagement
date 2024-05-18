package util;

public class LocationInMeters {
    private static final double R = 6378137;
    private double latitudeInMeters;
    private double longitudeInMeters;

    public double getLatitude() {
        return latitudeInMeters;
    }

    public void setLatitude(double latitudeInMeters) {
        this.latitudeInMeters = latitudeInMeters;
    }

    public double getLongitude() {
        return longitudeInMeters;
    }

    public void setLongitude(double longitudeInMeters) {
        this.longitudeInMeters = longitudeInMeters;
    }



    public static LocationInMeters trasformInMeters(double latitude, double longitude) {
        LocationInMeters meters = new LocationInMeters();
        double latRad = Math.toRadians(latitude);
        double lonRad = Math.toRadians(longitude);
        double x = R * lonRad;
        double y = R * Math.log(Math.tan(Math.PI / 4 + latRad / 2));
        meters.setLatitude(x);
        meters.setLongitude(y);
        return meters;
    }

    public static double calculateDistance(double x1, double y1, double x2, double y2) {
        return Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
    }

}
