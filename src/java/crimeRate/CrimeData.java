/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package crimeRate;

/**
 *
 * @author Tino
 */
//https://jsonformatter.org/json-to-java
public class CrimeData {

    public class Crime {

        private String category;
        private String location_type;
        private Location location;
        private String context;
        private OutcomeStatus outcome_status;
        private String persistent_id;
        private long id;
        private String location_subtype;
        private String month;

        public String getCategory() {
            return category;
        }

        public void setCategory(String value) {
            this.category = value;
        }

        public String getLocationType() {
            return location_type;
        }

        public void setLocationType(String value) {
            this.location_type = value;
        }

        public Location getLocation() {
            return location;
        }

        public void setLocation(Location value) {
            this.location = value;
        }

        public String getContext() {
            return context;
        }

        public void setContext(String value) {
            this.context = value;
        }

        public OutcomeStatus getOutcomeStatus() {
            return outcome_status;
        }

        public void setOutcomeStatus(OutcomeStatus value) {
            this.outcome_status = value;
        }

        public String getPersistentID() {
            return persistent_id;
        }

        public void setPersistentID(String value) {
            this.persistent_id = value;
        }

        public long getID() {
            return id;
        }

        public void setID(long value) {
            this.id = value;
        }

        public String getLocationSubtype() {
            return location_subtype;
        }

        public void setLocationSubtype(String value) {
            this.location_subtype = value;
        }

        public String getMonth() {
            return month;
        }

        public void setMonth(String value) {
            this.month = value;
        }
    }

    public class Location {

        private String latitude;
        private Street street;
        private String longitude;

        public String getLatitude() {
            return latitude;
        }

        public void setLatitude(String value) {
            this.latitude = value;
        }

        public Street getStreet() {
            return street;
        }

        public void setStreet(Street value) {
            this.street = value;
        }

        public String getLongitude() {
            return longitude;
        }

        public void setLongitude(String value) {
            this.longitude = value;
        }
    }

    public class Street {

        private long id;
        private String name;

        public long getID() {
            return id;
        }

        public void setID(long value) {
            this.id = value;
        }

        public String getName() {
            return name;
        }

        public void setName(String value) {
            this.name = value;
        }
    }

    public class OutcomeStatus {

        private String category;
        private String date;

        public String getCategory() {
            return category;
        }

        public void setCategory(String value) {
            this.category = value;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String value) {
            this.date = value;
        }
    }

}
