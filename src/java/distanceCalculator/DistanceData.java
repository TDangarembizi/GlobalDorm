/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package distanceCalculator;

/**
 *
 * @author Tino
 */
//https://jsonformatter.org/json-to-java
public class DistanceData {

    public static class Data {

        String status;
        String match_type;
        String input;
        PostcodeData data;
        String[] copyright;

        public static class PostcodeData {

            String postcode;
            String status;
            String usertype;
            int easting;
            int northing;
            int positional_quality_indicator;
            String country;
            String latitude;
            String longitude;
            String postcode_no_space;
            String postcode_fixed_width_seven;
            String postcode_fixed_width_eight;
            String postcode_area;
            String postcode_district;
            String postcode_sector;
            String outcode;
            String incode;
        }
    }

    public class Distance {

        private String code;
        private Route[] routes;
        private Waypoint[] waypoints;

        public String getCode() {
            return code;
        }

        public void setCode(String value) {
            this.code = value;
        }

        public Route[] getRoutes() {
            return routes;
        }

        public void setRoutes(Route[] value) {
            this.routes = value;
        }

        public Waypoint[] getWaypoints() {
            return waypoints;
        }

        public void setWaypoints(Waypoint[] value) {
            this.waypoints = value;
        }
    }

    public class Route {

        private Leg[] legs;
        private String weightName;
        private double weight;
        private double duration;
        private double distance;

        public Leg[] getLegs() {
            return legs;
        }

        public void setLegs(Leg[] value) {
            this.legs = value;
        }

        public String getWeightName() {
            return weightName;
        }

        public void setWeightName(String value) {
            this.weightName = value;
        }

        public double getWeight() {
            return weight;
        }

        public void setWeight(double value) {
            this.weight = value;
        }

        public double getDuration() {
            return duration;
        }

        public void setDuration(double value) {
            this.duration = value;
        }

        public double getDistance() {
            return distance;
        }

        public void setDistance(double value) {
            this.distance = value;
        }
    }

    public class Leg {

        private Object[] steps;
        private String summary;
        private double weight;
        private double duration;
        private double distance;

        public Object[] getSteps() {
            return steps;
        }

        public void setSteps(Object[] value) {
            this.steps = value;
        }

        public String getSummary() {
            return summary;
        }

        public void setSummary(String value) {
            this.summary = value;
        }

        public double getWeight() {
            return weight;
        }

        public void setWeight(double value) {
            this.weight = value;
        }

        public double getDuration() {
            return duration;
        }

        public void setDuration(double value) {
            this.duration = value;
        }

        public double getDistance() {
            return distance;
        }

        public void setDistance(double value) {
            this.distance = value;
        }
    }

    public class Waypoint {

        private String hint;
        private double distance;
        private String name;
        private double[] location;

        public String getHint() {
            return hint;
        }

        public void setHint(String value) {
            this.hint = value;
        }

        public double getDistance() {
            return distance;
        }

        public void setDistance(double value) {
            this.distance = value;
        }

        public String getName() {
            return name;
        }

        public void setName(String value) {
            this.name = value;
        }

        public double[] getLocation() {
            return location;
        }

        public void setLocation(double[] value) {
            this.location = value;
        }
    }
}
