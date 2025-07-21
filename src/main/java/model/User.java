package model;

public abstract class User {

    private int id;
    private String fullName;
    private String phone;
    private String email;
    private String password;
    private String role;
    private String address;
    private String profileImageBase64;
    private BankInfo bankInfo;
    private String status;
    private String salt;

    public User(int id, String fullName, String phone, String email, String password, String role, 
                String address, String profileImageBase64, BankInfo bankInfo, String status, String salt) {
        this.id = id;
        this.fullName = fullName;
        this.phone = phone;
        this.email = email;
        this.password = password;
        this.role = role;
        this.address = address;
        this.profileImageBase64 = profileImageBase64;
        this.bankInfo = bankInfo;
        this.status = status;
        this.salt = salt;
    }

    public User() {}

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

    public String getProfileImageBase64() { return profileImageBase64; }
    public void setProfileImageBase64(String profileImageBase64) { this.profileImageBase64 = profileImageBase64; }

    public BankInfo getBankInfo() { return bankInfo; }
    public void setBankInfo(BankInfo bankInfo) { this.bankInfo = bankInfo; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getSalt() { return salt; }
    public void setSalt(String salt) { this.salt = salt; }

    public abstract void validateRequiredFields();

    public static User createUserFromRole(int id, String fullName, String phone, String email, String password,
                                          String role, String address, String profileImageBase64, BankInfo bankInfo, String status, String salt) {
        switch (role.toLowerCase()) {
            case "seller":
                return new Vendor(id, fullName, phone, email, password, address, profileImageBase64, bankInfo, status, salt);
            case "courier":
                return new Courier(id, fullName, phone, email, password, address, profileImageBase64, bankInfo, status, salt);
            case "buyer":
            default:
                return new Buyer(id, fullName, phone, email, password, address, profileImageBase64, bankInfo, status, salt);
        }
    }
}
