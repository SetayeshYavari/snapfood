package DAO;

import model.Restaurant;
import utils.DB;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RestaurantDAO {

    public void addRestaurant(Restaurant r) throws SQLException {
        String sql = "INSERT INTO restaurants (id, name, address, phone, approved, logo_url, tax_fee, additional_fee) VALUES (?, ?, ?, ?, false, ?, ?, ?)";
        try (Connection conn = DB.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, r.getId());
            stmt.setString(2, r.getName());
            stmt.setString(3, r.getAddress());
            stmt.setString(4, r.getPhone());
            stmt.setBoolean(5, r.getApproved());
            stmt.setString(6, r.getLogoBase64());
            stmt.setInt(7, r.getTax_fee());
            stmt.setInt(8, r.getAdditional_fee());
            stmt.executeUpdate();
        }
    }

    public List<Restaurant> getRestaurantsByApproval(boolean approved) throws SQLException {
        List<Restaurant> list = new ArrayList<>();
        String sql = "SELECT * FROM restaurants WHERE approved = ?";
        try (Connection conn = DB.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setBoolean(1, approved);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Restaurant r = new Restaurant();
                    r.setId(rs.getInt("id"));
                    r.setName(rs.getString("name"));
                    r.setAddress(rs.getString("address"));
                    r.setPhone(rs.getString("phone"));
                    r.setApproved(rs.getBoolean("approved"));
                    r.setLogoBase64(rs.getString("logo_url"));
                    r.setTax_fee(rs.getInt("tax_fee"));
                    r.setAdditional_fee(rs.getInt("additional_fee"));
                    list.add(r);
                }
            }
        }
        return list;
    }


    public void approveRestaurant(int id) throws SQLException {
        String sql = "UPDATE restaurants SET approved = true WHERE id = ?";
        try (Connection conn = DB.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }
}
