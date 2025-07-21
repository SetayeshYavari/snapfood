package model;

public class Coupon {
    public enum CouponType {
        fixed,
        percent
    }

    private int id;
    private String couponCode;
    private CouponType type;
    private double value;
    private int minPrice;
    private int userCount;
    private String startDate;
    private String endDate;

    public Coupon(int id,String couponCode, CouponType type, double value, int minPrice, int userCount, String startDate, String endDate) {
        this.id = id;
        this.couponCode = couponCode;
        this.type = type;
        this.value = value;
        this.minPrice = minPrice;
        this.userCount = userCount;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public Coupon() {}

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getCouponCode() { return couponCode; }
    public void setCouponCode(String couponCode) { this.couponCode = couponCode; }

    public CouponType getType() { return type; }
    public void setType(CouponType type) { this.type = type; }

    public double getValue() { return value; }
    public void setValue(double value) { this.value = value; }

    public int getMinPrice() { return minPrice; }
    public void setMinPrice(int minPrice) { this.minPrice = minPrice; }

    public int getUserCount() { return userCount; }
    public void setUserCount(int userCount) { this.userCount = userCount; }

    public String getStartDate() { return startDate; }
    public void setStartDate(String startDate) { this.startDate = startDate; }

    public String getEndDate() { return endDate; }
    public void setEndDate(String endDate) { this.endDate = endDate; }
}