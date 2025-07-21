package model;

public class BankInfo {
    String bank_name;
    String account_number;

    public BankInfo(String bank_name, String account_number) {
        this.bank_name = bank_name;
        this.account_number = account_number;
    }

    public String getBankName() { return bank_name; }
    public void setBankName(String bank_name) { this.bank_name = bank_name; }

    public String getAccountNumber() { return account_number; }
    public void setAccountNumber(String account_number) { this.account_number = account_number; }
}
