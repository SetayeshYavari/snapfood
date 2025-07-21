package model;

import java.util.List;

public class FoodItem {
    private int id;
    private String name;
    private String imageBase64;
    private String description;
    private int vendor_id;
    private double price;
    private int supply;
    private List<String> keywords;

    // Constructors, getters and setters
    public FoodItem() {}

    public FoodItem(int id, int vendor_id, String name, String imageUrl, String description,
                    double price, int stock, List<String> keywords) {
        this.id = id;
        this.vendor_id = vendor_id;
        this.name = name;
        this.imageBase64 = imageUrl;
        this.description = description;
        this.price = price;
        this.supply = stock;
        this.keywords = keywords;
    }

    // Getters and setters...
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getVendor_id() { return vendor_id; }
    public void setVendor_id(int vendor_Id) { this.vendor_id = vendor_Id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getImageBase64() { return imageBase64; }
    public void setImageBase64(String imageBase64) { this.imageBase64 = imageBase64; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public int getSupply() { return supply; }
    public void setSupply(int supply) { this.supply = supply; }

    public List<String> getKeywords() { return keywords; }
    public void setKeywords(List<String> keywords) { this.keywords = keywords; }
}
