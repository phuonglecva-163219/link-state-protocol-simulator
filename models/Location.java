package models;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class Location {
    private String name;
    private double longtitude;
    private double latitude;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getLongtitude() {
        return longtitude;
    }

    @Override
    public String toString() {
        return "Location{" +
                "name='" + name + '\'' +
                ", longtitude=" + longtitude +
                ", latitude=" + latitude +
                '}';
    }

    public void setLongtitude(double longtitude) {
        this.longtitude = longtitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public Location(String name, double longtitude, double latitude) {
        this.name = name;
        this.longtitude = longtitude;
        this.latitude = latitude;
    }
//
//    public static void main(String[] args) {
//        String thisLine = null;
//        List<Location> cities = new ArrayList<Location>();
//        try {
//            // open input stream test.txt for reading purpose.
//            BufferedReader br = new BufferedReader(new FileReader("E://vn.csv"));
//            br.readLine();
//            while ((thisLine = br.readLine()) != null) {
//                String cityName = thisLine.split(",")[0];
//                double longtitude = Double.parseDouble(thisLine.split(",")[1]);
//                double latitude = Double.parseDouble(thisLine.split(",")[2]);
////                System.out.print(cityName+"\t");
////                System.out.print(longtitude+"\t");
////                System.out.print(latitude+"\n");
//                cities.add(new Location(cityName, longtitude, latitude));
//            }
//            for(Location l: cities) {
//                System.out.println(l);
//            }
//        } catch(Exception e) {
//            e.printStackTrace();
//        }
//
//        Location root = cities.get(0);
//        for(Location other: cities) {
//            System.out.println(root.distanceTo(other));
//        }
//
//    }
    public  double distance(double lat1,
                                  double lat2, double lon1,
                                  double lon2)
    {

        // The math module contains a function
        // named toRadians which converts from
        // degrees to radians.
        lon1 = Math.toRadians(lon1);
        lon2 = Math.toRadians(lon2);
        lat1 = Math.toRadians(lat1);
        lat2 = Math.toRadians(lat2);

        // Haversine formula
        double dlon = lon2 - lon1;
        double dlat = lat2 - lat1;
        double a = Math.pow(Math.sin(dlat / 2), 2)
                + Math.cos(lat1) * Math.cos(lat2)
                * Math.pow(Math.sin(dlon / 2),2);

        double c = 2 * Math.asin(Math.sqrt(a));

        // Radius of earth in kilometers. Use 3956
        // for miles
        double r = 6371;

        // calculate the result
        return(c * r);
    }

    public double distanceTo(Location otherNode) {
        return distance(this.latitude, otherNode.latitude, this.longtitude, otherNode.longtitude);
    }

    public static List<Location> getListLocation() {
        String thisLine = null;
        List<Location> cities = new ArrayList<Location>();
        try {
            // open input stream test.txt for reading purpose.
            BufferedReader br = new BufferedReader(new FileReader("E://vn.csv"));
            br.readLine();
            while ((thisLine = br.readLine()) != null) {
                String cityName = thisLine.split(",")[0];
                double longtitude = Double.parseDouble(thisLine.split(",")[1]);
                double latitude = Double.parseDouble(thisLine.split(",")[2]);
//                System.out.print(cityName+"\t");
//                System.out.print(longtitude+"\t");
//                System.out.print(latitude+"\n");
                cities.add(new Location(cityName, longtitude, latitude));
            }
//            for(Location l: cities) {
//                System.out.println(l);
//            }
        } catch(Exception e) {
            e.printStackTrace();
        }
        return cities;
    }
}
