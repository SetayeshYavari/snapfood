package DAO;

import model.Coupon;
import model.Coupon.CouponType;
import utils.DB;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CouponDAO {

    // Add coupon and return with generated id
    public static Coupon addCoupon(Coupon coupon) {
        String sql = "INSERT INTO coupons (coupon_code, type, value, min_price, user_count, start_date, end_date) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?) RETURNING id";

        try (Connection conn = DB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, coupon.getCouponCode());
            stmt.setString(2, coupon.getType().name());  // enum به رشته تبدیل می‌کنیم
            stmt.setDouble(3, coupon.getValue());
            stmt.setInt(4, coupon.getMinPrice());
            stmt.setInt(5, coupon.getUserCount());
            stmt.setString(6, coupon.getStartDate());
            stmt.setString(7, coupon.getEndDate());

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                coupon.setId(rs.getInt("id"));
                return coupon;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // Update coupon
    public static boolean updateCoupon(Coupon coupon) {
        String sql = "UPDATE coupons SET coupon_code=?, type=?, value=?, min_price=?, user_count=?, start_date=?, end_date=? WHERE id=?";

        try (Connection conn = DB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, coupon.getCouponCode());
            stmt.setString(2, coupon.getType().name());
            stmt.setDouble(3, coupon.getValue());
            stmt.setInt(4, coupon.getMinPrice());
            stmt.setInt(5, coupon.getUserCount());
            stmt.setString(6, coupon.getStartDate());
            stmt.setString(7, coupon.getEndDate());
            stmt.setInt(8, coupon.getId());

            int rows = stmt.executeUpdate();
            return rows > 0;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // Delete coupon by id
    public static boolean deleteCoupon(int id) {
        String sql = "DELETE FROM coupons WHERE id = ?";

        try (Connection conn = DB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            int rows = stmt.executeUpdate();
            return rows > 0;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // Get coupon by id
    public static Coupon getCouponById(int id) {
        String sql = "SELECT * FROM coupons WHERE id = ?";

        try (Connection conn = DB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return extractCoupon(rs);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // Get coupon by code
    public static Coupon getCouponByCode(String couponCode) {
        String sql = "SELECT * FROM coupons WHERE couponCode = ?";

        try (Connection conn = DB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, couponCode);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return extractCoupon(rs);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // Get all coupons
    public static List<Coupon> getAllCoupons() {
        List<Coupon> coupons = new ArrayList<>();
        String sql = "SELECT * FROM coupons";

        try (Connection conn = DB.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                coupons.add(extractCoupon(rs));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return coupons;
    }

    // Helper method to convert ResultSet -> Coupon
    private static Coupon extractCoupon(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        String couponCode = rs.getString("coupon_code");
        CouponType type = CouponType.valueOf(rs.getString("type")); // تبدیل رشته به enum
        double value = rs.getDouble("value");
        int minPrice = rs.getInt("min_price");
        int userCount = rs.getInt("user_count");
        String startDate = rs.getString("start_date");
        String endDate = rs.getString("end_date");

        return new Coupon(id, couponCode, type, value, minPrice, userCount, startDate, endDate);
    }
}