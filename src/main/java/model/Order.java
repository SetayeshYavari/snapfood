package model;

public class Order {
    public enum Status {
        submitted,
        unpaid_and_cancelled,
        waiting_vendor,
        cancelled,
        finding_courier,
        on_the_way,
        completed
    }

    private int id;
    private String deliveryAddress;
    private int customerId;
    private int vendorId;
    private int couponId;
    private int itemIds;
    private int rawPrice;
    private int taxFee;
    private int additionalFee;
    private int courierFee;
    private int payPrice;
    private int courierId;
    private Status status;
    private String createdAt;
    private String updatedAt;

    public Order(int id, String deliveryAddress, int customerId, int vendorId, int couponId, int itemIds,
                 int rawPrice, int taxFee, int additionalFee, int courierFee, int payPrice, int courierId,
                 Status status, String createdAt, String updatedAt) {
        this.id = id;
        this.deliveryAddress = deliveryAddress;
        this.customerId = customerId;
        this.vendorId = vendorId;
        this.couponId = couponId;
        this.itemIds = itemIds;
        this.rawPrice = rawPrice;
        this.taxFee = taxFee;
        this.additionalFee = additionalFee;
        this.courierFee = courierFee;
        this.payPrice = payPrice;
        this.courierId = courierId;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public String getDeliveryAddress() { return deliveryAddress; }
    public void setDeliveryAddress(String deliveryAddress) { this.deliveryAddress = deliveryAddress; }
    
    public int getCustomerId() { return customerId; }
    public void setCustomerId(int customerId) { this.customerId = customerId; }
    
    public int getVendorId() { return vendorId; }
    public void setVendorId(int vendorId) { this.vendorId = vendorId; }
    
    public int getCouponId() { return couponId; }
    public void setCouponId(int couponId) { this.couponId = couponId; }
    
    public int getItemIds() { return itemIds; }
    public void setItemIds(int itemIds) { this.itemIds = itemIds; }
    
    public int getRawPrice() { return rawPrice; }
    public void setRawPrice(int rawPrice) { this.rawPrice = rawPrice; }

    public int getTaxFee() { return taxFee; }
    public void setTaxFee(int taxFee) { this.taxFee = taxFee; }

    public int getAdditionalFee() { return additionalFee; }
    public void setAdditionalFee(int additionalFee) { this.additionalFee = additionalFee; }

    public int getCourierFee() { return courierFee; }
    public void setCourierFee(int courierFee) { this.courierFee = courierFee; }

    public int getPayPrice() { return payPrice; }
    public void setPayPrice(int payPrice) { this.payPrice = payPrice; }

    public int getCourierId() { return courierId; }
    public void setCourierId(int courierId) { this.courierId = courierId; }

    public Status getStatus() { return status; }
    public void setStatus(Status status) { this.status = status; }

    public String getCreatedAt() { return createdAt; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }

    public String getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(String updatedAt) { this.updatedAt = updatedAt; }
}