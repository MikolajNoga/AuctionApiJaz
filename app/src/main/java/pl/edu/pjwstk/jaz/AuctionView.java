package pl.edu.pjwstk.jaz;

import pl.edu.pjwstk.jaz.entities.Auction;
import pl.edu.pjwstk.jaz.entities.AuctionParameter;
import pl.edu.pjwstk.jaz.entities.Photo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AuctionView {

    private int id;
    private String category;
    private String title;
    private String description;
    private Map<Integer, String> photos;
    private Map<String, String> parameters;
    private double price;
    private int version;

    public AuctionView(Auction auction) {
        this.id = auction.getId();
        this.category = auction.getCategory().getName();
        this.title = auction.getTitle();
        this.description = auction.getDescription();
        this.photos = photosConversion(auction.getPhotos());
        this.parameters = parameterConversion(auction.getParameters());
        this.price = auction.getPrice();
        this.version = auction.getVersion();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Map<Integer, String> getPhotos() {
        return photos;
    }

    public void setPhotos(Map<Integer, String> photos) {
        this.photos = photos;
    }

    public Map<String, String> getParameters() {
        return parameters;
    }

    public void setParameters(Map<String, String> parameters) {
        this.parameters = parameters;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public String toStringShortVersion(){
        return "{ Title: " + title + ", " +
                "id: " + id + ", " +
                "miniature: " + photos.get(1) + ", " +
                "price: " + price + "}";
    }

    private Map<Integer, String> photosConversion(List<Photo> photos){
        Map<Integer, String> map = new HashMap<>();
        for (Photo photo : photos) {
            map.put(photo.getPosition(),photo.getName());
        }
        return map;
    }

    private Map<String, String> parameterConversion(List<AuctionParameter> parameters){
        Map<String, String> map = new HashMap<>();
        for (AuctionParameter parameter : parameters) {
            map.put(parameter.getParameter().getKey(),parameter.getValue());
        }
        return map;
    }

}
