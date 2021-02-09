package pl.edu.pjwstk.jaz.requests;

import java.util.List;

public class AuctionRequest {

    private String title;
    private String description;
    private double price;
    private int category_id;
    private List<PhotoRequest> photos;
    private List<ParameterRequest> parameters;

    public AuctionRequest(String title, String description, double price, int category_id,
                          List<PhotoRequest> photos, List<ParameterRequest> parameters) {
        this.title = title;
        this.description = description;
        this.price = price;
        this.category_id = category_id;
        this.photos = photos;
        this.parameters = parameters;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public double getPrice() {
        return price;
    }

    public int getCategory_id() {
        return category_id;
    }

    public List<PhotoRequest> getPhotos() {
        return photos;
    }

    public List<ParameterRequest> getParameters() {
        return parameters;
    }
}
