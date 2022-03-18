package com.asset.assetapp.models;

import android.graphics.Bitmap;

public class Asset {
    private String Id;
    private Bitmap Image;
    private String Title;
    private String Description;
    private String SerialNumber;
    private String Manufacturer;
    private String ModelNumber;
    private Location Location;

    public Asset(String id, Bitmap image, String title, String description, String serialNumber, String manufacturer, String modelNumber, Asset.Location location) {
        Id = id;
        Image = image;
        Title = title;
        Description = description;
        SerialNumber = serialNumber;
        Manufacturer = manufacturer;
        ModelNumber = modelNumber;
        Location = location;
    }

    public static class Location {
        private double Latitude;
        private double Longitude;

        public Location(double latitude, double longitude) {
            Latitude = latitude;
            Longitude = longitude;
        }

        public double getLatitude() {
            return Latitude;
        }
        public void setLatitude(double latitude) {
            Latitude = latitude;
        }

        public double getLongitude() {
            return Longitude;
        }
        public void setLongitude(double longitude) {
            Longitude = longitude;
        }
    }

    public String getId() {
        return Id;
    }
    public void setId(String id) {
        Id = id;
    }

    public Bitmap getImage() {
        return Image;
    }
    public void setImage(Bitmap image) {
        Image = image;
    }

    public String getTitle() {
        return Title;
    }
    public void setTitle(String title) {
        Title = title;
    }

    public String getDescription() {
        return Description;
    }
    public void setDescription(String description) {
        Description = description;
    }

    public String getSerialNumber() {
        return SerialNumber;
    }
    public void setSerialNumber(String serialNumber) {
        SerialNumber = serialNumber;
    }

    public String getManufacturer() {
        return Manufacturer;
    }
    public void setManufacturer(String manufacturer) {
        Manufacturer = manufacturer;
    }

    public String getModelNumber() {
        return ModelNumber;
    }
    public void setModelNumber(String modelNumber) {
        ModelNumber = modelNumber;
    }

    public Location getLocation() { return Location; }
    public void setLocation(Location location) { Location = location; }
}
