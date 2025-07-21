package DAO;

import model.BankInfo;
import model.User;
import utils.DB;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDAO {

    // ✅ Add user and return inserted user with ID
    public static User addUser(User user) {

        String sql = "INSERT INTO users (full_name, phone, email, password, role, address, profile_photo_url, bank_name, account_number, status, salt) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) RETURNING id";

        try (Connection conn = DB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, user.getFullName());
            stmt.setString(2, user.getPhone());
            stmt.setString(3, user.getEmail());
            stmt.setString(4, user.getPassword());
            stmt.setString(5, user.getRole());
            stmt.setString(6, user.getAddress());
            stmt.setString(7, user.getProfileImageBase64());
            stmt.setString(8, user.getBankInfo().getBankName());
            stmt.setString(9, user.getBankInfo().getAccountNumber());
            stmt.setString(10, user.getStatus());
            stmt.setString(11, user.getSalt());

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                user.setId(rs.getInt("id"));
                System.out.println("✅ User inserted with ID: " + user.getId());
                return user;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    // ✅ Update user
    public static boolean updateUser(User user) {
        String sql = "UPDATE users SET full_name=?, phone=?, email=?, password=?, role=?, address=?, profile_photo_url=?, bank_name=?, account_number=?, status=?, salt=?  WHERE id=?";

        try (Connection conn = DB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, user.getFullName());
            stmt.setString(2, user.getPhone());
            stmt.setString(3, user.getEmail());
            stmt.setString(4, user.getPassword());
            stmt.setString(5, user.getRole());
            stmt.setString(6, user.getAddress());
            stmt.setString(7, user.getProfileImageBase64());
            stmt.setString(8, user.getBankInfo().getBankName());
            stmt.setString(9, user.getBankInfo().getAccountNumber());
            stmt.setString(10, user.getStatus());
            stmt.setString(11, user.getSalt());
            stmt.setInt(12, user.getId());

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // ✅ Delete user
    public static boolean deleteUser(int id) {
        String sql = "DELETE FROM users WHERE id = ?";

        try (Connection conn = DB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // ✅ Find by phone (used in login/register)
    public static User findByPhone(String phone) {
        String sql = "SELECT * FROM users WHERE phone = ?";

        try (Connection conn = DB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, phone);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return extractUser(rs);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    // ✅ Get by ID
    public static User getUserById(int id) {
        String sql = "SELECT * FROM users WHERE id = ?";

        try (Connection conn = DB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return extractUser(rs);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    // ✅ Get all users
    public static List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM users";

        try (Connection conn = DB.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                users.add(extractUser(rs));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return users;
    }

    // ✅ Helper method to convert ResultSet → User
    private static User extractUser(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        String fullName = rs.getString("full_name");
        String phone = rs.getString("phone");
        String email = rs.getString("email");
        String password = rs.getString("password");
        String role = rs.getString("role");
        String address = rs.getString("address");
        String profilePhoto = rs.getString("profile_photo_url");
        String bank_name = rs.getString("bank_name");
        String account_number = rs.getString("account_number");
        BankInfo bankInfo = new BankInfo(bank_name, account_number);
        String status = rs.getString("status");
        String salt = rs.getString("salt");

        return User.createUserFromRole(id, fullName, phone, email, password, role, address, profilePhoto, bankInfo, status, salt);
    }

}