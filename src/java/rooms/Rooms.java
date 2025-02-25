/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rooms;

import java.time.LocalDate;
import com.google.gson.annotations.SerializedName;

/**
 *
 * @author Tino
 */
//https://jsonformatter.org/json-to-java
public class Rooms {

    private Room[] rooms;

    public Room[] getRooms() {
        return rooms;
    }

    public void setRooms(Room[] value) {
        this.rooms = value;
    }

    public class Room {

        private long id;
        private String name;
        private Location location;
        private Details details;
        @SerializedName("price_per_month_gbp")
        private long pricePerMonthGbp;
        @SerializedName("availability_date")
        private String availabilityDate;
        @SerializedName("spoken_languages")
        private String[] spokenLanguages;

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

        public Location getLocation() {
            return location;
        }

        public void setLocation(Location value) {
            this.location = value;
        }

        public Details getDetails() {
            return details;
        }

        public void setDetails(Details value) {
            this.details = value;
        }

        public long getPricePerMonthGbp() {
            return pricePerMonthGbp;
        }

        public void setPricePerMonthGbp(long value) {
            this.pricePerMonthGbp = value;
        }

        public String getAvailabilityDate() {
            return availabilityDate;
        }

        public void setAvailabilityDate(String value) {
            this.availabilityDate = value;
        }

        public String[] getSpokenLanguages() {
            return spokenLanguages;
        }

        public void setSpokenLanguages(String[] value) {
            this.spokenLanguages = value;
        }
    }

    public class Details {

        private boolean furnished;
        private String[] amenities;
        @SerializedName("live_in_landlord")
        private boolean liveInLandlord;
        @SerializedName("shared_with")
        private long sharedWith;
        @SerializedName("bills_included")
        private boolean billsIncluded;
        @SerializedName("bathroom_shared")
        private boolean bathroomShared;

        public boolean isFurnished() {
            return furnished;
        }

        public void setFurnished(boolean value) {
            this.furnished = value;
        }

        public String[] getAmenities() {
            return amenities;
        }

        public void setAmenities(String[] value) {
            this.amenities = value;
        }

        public boolean isLiveInLandlord() {
            return liveInLandlord;
        }

        public void setLiveInLandlord(boolean value) {
            this.liveInLandlord = value;
        }

        public long getSharedWith() {
            return sharedWith;
        }

        public void setSharedWith(long value) {
            this.sharedWith = value;
        }

        public boolean isBillsIncluded() {
            return billsIncluded;
        }

        public void setBillsIncluded(boolean value) {
            this.billsIncluded = value;
        }

        public boolean isBathroomShared() {
            return bathroomShared;
        }

        public void setBathroomShared(boolean value) {
            this.bathroomShared = value;
        }
    }

    public class Location {

        private String city;
        private String county;
        private String postcode;

        public String getCity() {
            return city;
        }

        public void setCity(String value) {
            this.city = value;
        }

        public String getCounty() {
            return county;
        }

        public void setCounty(String value) {
            this.county = value;
        }

        public String getPostcode() {
            return postcode;
        }

        public void setPostcode(String value) {
            this.postcode = value;
        }
    }

}
