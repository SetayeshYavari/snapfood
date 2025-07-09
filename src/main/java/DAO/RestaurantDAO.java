package DAO;

import model.Restaurant;
import utils.DB;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RestaurantDAO {

    public void addRestaurant(Restaurant r) throws SQLException {
        String sql = "INSERT INTO restaurants (seller_id, name, address, phone, opening_hours, logo_url, approved) VALUES (?, ?, ?, ?, ?, ?, false)";
        try (Connection conn = DB.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, r.getSellerId());
            stmt.setString(2, r.getName());
            stmt.setString(3, r.getAddress());
            stmt.setString(4, r.getPhone());
            stmt.setString(5, r.getOpeningHours());
            stmt.setString(6, r.getLogoUrl());
            stmt.executeUpdate();
        }
    }

    public List<Restaurant> getAllApprovedRestaurants() throws SQLException {
        List<Restaurant> list = new ArrayList<>();
        String sql = "SELECT * FROM restaurants WHERE approved = true";
        try (Connection conn = DB.getConnection(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Restaurant r = new Restaurant();
                r.setId(rs.getInt("id"));
                r.setSellerId(rs.getInt("seller_id"));
                r.setName(rs.getString("name"));
                r.setAddress(rs.getString("address"));
                r.setPhone(rs.getString("phone"));
                r.setOpeningHours(rs.getString("opening_hours"));
                r.setLogoUrl(rs.getString("logo_url"));
                r.setApproved(rs.getBoolean("approved"));
                list.add(r);
            }
        }
        return list;
    }

    public List<Restaurant> getPendingRestaurants() throws SQLException {
        List<Restaurant> list = new ArrayList<>();
        String sql = "SELECT * FROM restaurants WHERE approved = false";
        try (Connection conn = DB.getConnection(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Restaurant r = new Restaurant();
                r.setId(rs.getInt("id"));
                r.setSellerId(rs.getInt("seller_id"));
                r.setName(rs.getString("name"));
                r.setAddress(rs.getString("address"));
                r.setPhone(rs.getString("phone"));
                r.setOpeningHours(rs.getString("opening_hours"));
                r.setLogoUrl(rs.getString("logo_url"));
                r.setApproved(rs.getBoolean("approved"));
                list.add(r);
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
