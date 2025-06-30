package model;

public class User {
    private int id;
    private String fullName;
    private String phone;
    private String email;
    private String password;
    private String role;
    private String address;
    private String profilePhotoUrl;
    private String bankInfo;
    private String brandName;
    private String restaurantDescription;
    private String salt;

    public User(int id, String fullName, String phone, String email, String password,
                String role, String address, String profilePhotoUrl, String bankInfo,
                String brandName, String restaurantDescription, String salt) {
        this.id = id;
        this.fullName = fullName;
        this.phone = phone;
        this.email = email;
        this.password = password;
        this.role = role;
        this.address = address;
        this.profilePhotoUrl = profilePhotoUrl;
        this.bankInfo = bankInfo;
        this.brandName = brandName;
        this.restaurantDescription = restaurantDescription;
        this.salt = salt;
    }

    // --- Getters and Setters ---

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getProfilePhotoUrl() { return profilePhotoUrl; }
    public void setProfilePhotoUrl(String profilePhotoUrl) { this.profilePhotoUrl = profilePhotoUrl; }

    public String getBankInfo() { return bankInfo; }
    public void setBankInfo(String bankInfo) { this.bankInfo = bankInfo; }

    public String getBrandName() { return brandName; }
    public void setBrandName(String brandName) { this.brandName = brandName; }

    public String getRestaurantDescription() { return restaurantDescription; }
    public void setRestaurantDescription(String restaurantDescription) { this.restaurantDescription = restaurantDescription; }

    public String getSalt() { return salt; }
    public void setSalt(String salt) { this.salt = salt; }

}
