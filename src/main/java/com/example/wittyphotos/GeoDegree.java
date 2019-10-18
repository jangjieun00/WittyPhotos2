package com.example.wittyphotos;

public class GeoDegree {
    private boolean valid = false;
    private double latitude, longitude;

    public GeoDegree(String lat, String lat_ref, String lon, String lon_ref) {
        if ((lat != null) && (lat_ref != null) && (lon != null) && (lon_ref != null)) {
            valid = true;
            if (lat_ref.equals("N")) {
                latitude = convertToDegree(lat);
            } else {
                latitude = 0 - convertToDegree(lat);
            }

            if (lon_ref.equals("E")) {
                longitude = convertToDegree(lon);
            } else {
                longitude = 0 - convertToDegree(lon);
            }
        }
    };

    private Float convertToDegree(String stringDMS) {
        Float result = null;
        String[] DMS = stringDMS.split(",", 3);

        String[] stringD = DMS[0].split("/", 2);
        Double D0 = new Double(stringD[0]);
        Double D1 = new Double(stringD[1]);
        Double FloatD = D0 / D1;

        String[] stringM = DMS[1].split("/", 2);
        Double M0 = new Double(stringM[0]);
        Double M1 = new Double(stringM[1]);
        Double FloatM = M0 / M1;

        String[] stringS = DMS[2].split("/", 2);
        Double S0 = new Double(stringS[0]);
        Double S1 = new Double(stringS[1]);
        Double FloatS = S0 / S1;

        result = new Float(FloatD + (FloatM / 60) + (FloatS / 3600));

        return result;

    };

    public boolean isValid() {
        return valid;
    }

    @Override
    public String toString() {
        return (String.valueOf(latitude) + ", " + String.valueOf(longitude));
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public int getLatitudeE6() {
        return (int) (latitude * 1000000);
    }

    public int getLongitudeE6() {
        return (int) (longitude * 1000000);
    }

}
