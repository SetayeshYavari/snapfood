package DAO;

import model.FoodItem;
import utils.DB;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FoodItemDAO {

    public List<FoodItem> getFoodItemsByRestaurant(int restaurantId) {
        List<FoodItem> list = new ArrayList<>();
        String sql = "SELECT * FROM food_items WHERE restaurant_id = ?";
        try (Connection conn = DB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, restaurantId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                FoodItem item = mapResultSetToFoodItem(rs);
                list.add(item);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public FoodItem getFoodItem(int id) {
        String sql = "SELECT * FROM food_items WHERE id = ?";
        try (Connection conn = DB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return mapResultSetToFoodItem(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public FoodItem addFoodItem(FoodItem item) {
        String sql = "INSERT INTO food_items (restaurant_id, name, image_url, description, price, stock, category, keywords) VALUES (?, ?, ?, ?, ?, ?, ?, ?) RETURNING id";
        try (Connection conn = DB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, item.getRestaurantId());
            stmt.setString(2, item.getName());
            stmt.setString(3, item.getImageUrl());
            stmt.setString(4, item.getDescription());
            stmt.setDouble(5, item.getPrice());
            stmt.setInt(6, item.getStock());
            stmt.setString(7, item.getCategory());
            stmt.setString(8, item.getKeywords());

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                item.setId(rs.getInt("id"));
                return item;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean updateFoodItem(FoodItem item) {
        String sql = "UPDATE food_items SET name = ?, image_url = ?, description = ?, price = ?, stock = ?, category = ?, keywords = ? WHERE id = ? AND restaurant_id = ?";
        try (Connection conn = DB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, item.getName());
            stmt.setString(2, item.getImageUrl());
            stmt.setString(3, item.getDescription());
            stmt.setDouble(4, item.getPrice());
            stmt.setInt(5, item.getStock());
            stmt.setString(6, item.getCategory());
            stmt.setString(7, item.getKeywords());
            stmt.setInt(8, item.getId());
            stmt.setInt(9, item.getRestaurantId());

            int affected = stmt.executeUpdate();
            return affected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean deleteFoodItem(int id, int restaurantId) {
        String sql = "DELETE FROM food_items WHERE id = ? AND restaurant_id = ?";
        try (Connection conn = DB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.setInt(2, restaurantId);
            int affected = stmt.executeUpdate();
            return affected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private FoodItem mapResultSetToFoodItem(ResultSet rs) throws SQLException {
        FoodItem item = new FoodItem();
        item.setId(rs.getInt("id"));
        item.setRestaurantId(rs.getInt("restaurant_id"));
        item.setName(rs.getString("name"));
        item.setImageUrl(rs.getString("image_url"));
        item.setDescription(rs.getString("description"));
        item.setPrice(rs.getDouble("price"));
        item.setStock(rs.getInt("stock"));
        item.setCategory(rs.getString("category"));
        item.setKeywords(rs.getString("keywords"));
        return item;
    }
}

