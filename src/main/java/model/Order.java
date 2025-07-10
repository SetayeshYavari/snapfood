package model;

import java.time.LocalDateTime;
import java.util.List;

public class Order {
    private int id;
    private int userId;
    private int restaurantId;
    private String address;
    private double totalAmount;
    private double tax;
    private double deliveryFee;
    private String status;
    private LocalDateTime createdAt;
    private List<OrderItem> items;

    public Order() {}

    public Order(int id, int userId, int restaurantId, String address,
                 double totalAmount, double tax, double deliveryFee,
                 String status, LocalDateTime createdAt, List<OrderItem> items) {
        this.id = id;
        this.userId = userId;
        this.restaurantId = restaurantId;
        this.address = address;
        this.totalAmount = totalAmount;
        this.tax = tax;
        this.deliveryFee = deliveryFee;
        this.status = status;
        this.createdAt = createdAt;
        this.items = items;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public int getRestaurantId() { return restaurantId; }
    public void setRestaurantId(int restaurantId) { this.restaurantId = restaurantId; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public double getTotalAmount() { return totalAmount; }
    public void setTotalAmount(double totalAmount) { this.totalAmount = totalAmount; }

    public double getTax() { return tax; }
    public void setTax(double tax) { this.tax = tax; }

    public double getDeliveryFee() { return deliveryFee; }
    public void setDeliveryFee(double deliveryFee) { this.deliveryFee = deliveryFee; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public List<OrderItem> getItems() { return items; }
    public void setItems(List<OrderItem> items) { this.items = items; }
}