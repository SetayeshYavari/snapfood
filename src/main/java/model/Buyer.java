package model;

public class Buyer extends User {
    public Buyer(int id, String fullName, String phone, String email, String password,
                 String address, String profileImageBase64, BankInfo bankInfo, String status, String salt) {
        super(id, fullName, phone, email, password, "buyer", address, profileImageBase64, bankInfo, status, salt);
    }

    public void validateRequiredFields() {

    };
}
